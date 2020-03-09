#include "Arduino.h"
#include "Teto.h"
#include "Constants.h"
#include <AFMotor.h>
#include <ArduinoJson.h>
#include <Stepper.h>

Teto::Teto(int motorID,int endstop_open_port, int endstop_close_port){
  _TetoMotor = new AF_DCMotor(motorID);
  _endstop_open_port = endstop_open_port;
  _endstop_close_port = endstop_close_port;
  beginTeto();
  _isMotorPasso = false;
}
Teto::Teto(int arg1,int arg2,int arg3,int arg4,int arg5){
  _M = new Stepper(arg1, arg2, arg3, arg4, arg5);
  _M->setSpeed(60);
  _isMotorPasso = true;
}
void Teto::beginTeto(){
  if(_isMotorPasso){
    _iPasso = 3; //Inicial aberto.
  }else{
    pinMode(_endstop_open_port,INPUT);
    pinMode(_endstop_close_port,INPUT);
    _iPos = getTetoStatus();  
    if(_iPos == -1){
      // caso meio termo ao iniciar, seta para fechar? ou continua como -1 e espera o rasp verificar que está errado e atuar.
      _iSetpoint = 0; // Tem que tratar isso no rasp então ou trocar para 0 (padrão fechar). 
    }else{
      _iSetpoint = _iPos;
    }
  }
}

void Teto::onNewTetoJob(JsonObject& root){
  if(root.containsKey(TETO_JSON_JOB)){ // 0 = fechar ; 1 = abrir
     if(root[TETO_JSON_JOB] == 0){
      _iSetpoint = 0;
     }else{
      _iSetpoint = 1;
     }
  }
}

void Teto::getTetoInfo(JsonObject& tetoJson){
  tetoJson[TETO_POSITION_ID] = _iPos;
  tetoJson[TETO_JSON_JOB] = _iSetpoint;
}
void Teto::getTetoInfo2(int TetoArr[2]){
  TetoArr[0] = _iPos;
  TetoArr[1] = _iSetpoint;
}
int Teto::getTetoStatus(){
  if(_isMotorPasso){
    if(_iPasso == PISCINA_PASSOS){ //Está aberto
      return PISCINA_OPEN_LOGIC; //setpoint fechar
    }else if(_iPasso == 0){
      return PISCINA_OPEN_LOGIC^1;
    }else{
      return -1;
    }
  }else{
    if(digitalRead(_endstop_open_port) == TETO_EDNSTOP_LOGIC_CLICKED){
      //Serial.println("Aberto");
      return 1;
    }else if (digitalRead(_endstop_close_port) == TETO_EDNSTOP_LOGIC_CLICKED){
      //Serial.println("Fechado");
      return 0;
    }else{
      return -1; //Meio do caminho.
    }
  }
}
void Teto::controleTeto(){
  _iPos = getTetoStatus();
  if(_isMotorPasso){
    if(_iSetpoint != _iPos){
      if(_iSetpoint == PISCINA_OPEN_LOGIC){
      //Abre
        _M->step (2048);
        _iPasso++;
      }else{
      //fecha
        _M->step (-2048);
        _iPasso--;
      }
    }
  }else{
    _TetoMotor->setSpeed((int)TETO_PWM*255/100);
    if(_iSetpoint != _iPos){
      if(_iSetpoint == 1){ // Se setpoint for 1 = abrir vai na direção que abre
        _TetoMotor->run(FORWARD); // CONFIRMAR
      }else if (_iSetpoint == 0){
        //Serial.println("Fechando Teto");
        _TetoMotor->run(BACKWARD); // CONFIRMAR
      }
    }else{
      _TetoMotor->run(RELEASE);
    }
  }
}
