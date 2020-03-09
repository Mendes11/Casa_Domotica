#include "Arduino.h"
#include <ArduinoJson.h>
#include <AFMotor.h>
#include "Constants.h"
#include "Portao.h"

Portao::Portao(int endstop_open_port,int endstop_close_port,int motorID){
  _endstop_open_port = endstop_open_port;
  _endstop_close_port = endstop_close_port;
  _PortaoMotor = new AF_DCMotor(motorID);
  beginPortao();
}
void Portao::beginPortao(){
  pinMode(_endstop_open_port,INPUT);
  pinMode(_endstop_close_port,INPUT);
  _bOpen = false;
  _bSetOpen = false;
  _iIDDeviceStatus = 5;
  _timeToClose = 10000; // 10s padrão.
}

void Portao::onNewPortaoJob(JsonObject& root){
  if(root.containsKey(PORTAO_JSON_JOB)){ // 0 = fechar ; 1 = abrir
     if(root[PORTAO_JSON_JOB] == 0){
      _iIDDeviceStatus = DEVICE_FECHANDO;
      _bSetOpen = false;
     }else{
      _iIDDeviceStatus = DEVICE_ABRINDO;
      _bSetOpen = true;
     }
  }
  if(root.containsKey(PORTAO_JSON_JOB_TIME)){
    _timeToClose = root[PORTAO_JSON_JOB_TIME]; // s to ms
  }
}

boolean Portao::getPortaoStatus(){
  
  if(digitalRead(_endstop_open_port) == PORTAO_EDNSTOP_LOGIC_CLICKED_REVERSE){
    return 1;
  }else if (digitalRead(_endstop_close_port) == PORTAO_EDNSTOP_LOGIC_CLICKED){
    return 0;
  }else{
    return -1; //Meio do caminho.
  }
}
void Portao::controlePortao(){
  boolean bruteClose = false;
  if(_bSetOpen == false && _bOpen == false && getPortaoStatus() == -1){
    // Situação em que o sistema se perdeu ou se iniciou, logo vai para o estado de fechar.
    _iIDDeviceStatus = DEVICE_FECHANDO;
    bruteClose = true;
    _bOpen = true;
  }
  _PortaoMotor->setSpeed((int)PORTAO_PWM*255/100);
  if(_bSetOpen){
      Serial.println("Abrindo Portão");
      _PortaoMotor->run(FORWARD); // CONFIRMAR
      if(getPortaoStatus() == 1){
        // Terminou de Abrir, zera bSetOpen, seta bOpen e inicia contador.
        _iIDDeviceStatus = DEVICE_ABERTO;
        _PortaoMotor->run(RELEASE);
        _bSetOpen = false;
        _bOpen = true;
        _lastOpen = millis();
      }
  }
  if(_bOpen){
    if(((millis() - _lastOpen) > _timeToClose) || bruteClose){
      // Chegou a hora de fechar.
      _iIDDeviceStatus = DEVICE_FECHANDO;
      _PortaoMotor->run(BACKWARD);
    }
    if(getPortaoStatus() == 0){
      _iIDDeviceStatus = DEVICE_FECHADO;
      _PortaoMotor->run(RELEASE);
      _bOpen = false;
    }
  }
}
void Portao::getPortaoInfo2(int arr[4]){
  if(_bOpen){
    arr[0] = 1;
  }else{
    arr[0] = 0;
  }
  //bOpen = _bOpen;
  arr[1] = _iPos;
  arr[2] = _iIDDeviceStatus;
  arr[3] = _timeToClose;
}
void Portao::getPortaoInfo(JsonObject& portaoJson){
  portaoJson[PORTAO_JSON_JOB] = _bOpen;
  portaoJson[PORTAO_JSON_JOB_POS] = _iPos;
  portaoJson[PORTAO_JSON_DEVICE_STATUS] = _iIDDeviceStatus;
  portaoJson[PORTAO_JSON_JOB_TIME] = _timeToClose;
}

