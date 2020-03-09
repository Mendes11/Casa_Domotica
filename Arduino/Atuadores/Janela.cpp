#include "Arduino.h"
#include "Constants.h"
#include "Janela.h"
#include <ArduinoJson.h>
#include <AFMotor.h>

#define MINIMO  46
#define MAXIMO  970
#define PORCENTAGEM_ERRO  5

Janela::Janela(int motorID,int analogPort){
  _JanelaMotor = new AF_DCMotor(motorID);
  _analogPort = analogPort;
  beginJanela();
}
void Janela::beginJanela(){
  _isStopped = true;
  _JanelaPos = analogRead(_analogPort);
  //_JanelaPos = map(_JanelaPos,MINIMO,MAXIMO,0,100);
  getPos();
  _JanelaSetpoint = _JanelaPos;
  Serial.print("Posicao Atual : ");
  Serial.println(_JanelaPos);
}
void Janela::controleJanela(){
  getPos();
  _isStopped = false;
  _JanelaError = _JanelaSetpoint - _JanelaPos;
  _JanelaMotor->setSpeed((int)JANELA_PWM*255/100);
  if(_JanelaError >= (100*PORCENTAGEM_ERRO/100)){
    //Girar para um Lado
    _JanelaMotor->run(FORWARD);
  }else if(_JanelaError <= (-100*PORCENTAGEM_ERRO/100)){
    //Girar para outro Lado
    _JanelaMotor->run(BACKWARD);
  }else{
    _isStopped = true;
    _JanelaPos = _JanelaSetpoint;
    _JanelaMotor->run(RELEASE);
  }
}
void Janela::getPos(){
  _JanelaPos = analogRead(_analogPort);
 // Serial.println(_JanelaPos);
  _JanelaPos = map(_JanelaPos,MINIMO,MAXIMO,0,100);
  if(_JanelaPos < 0){
    _JanelaPos = 0;
  }
  if(_JanelaPos > 100){
    _JanelaPos = 100;
  }
  
}
void Janela::setSetpoint(int i){
  _JanelaSetpoint = i;
  if(_JanelaSetpoint > 100){
          _JanelaSetpoint = 100;
        }
   if(_JanelaSetpoint < 0){
    _JanelaSetpoint = 0;
   }
}
void Janela::getJanelaInfo2(int arr[]){
  if(_isStopped){
    arr[0] = _JanelaSetpoint;
  }else{
    getPos();
    arr[0] = _JanelaPos;
  }
   arr[1] = _JanelaSetpoint;
}
void Janela::getJanelaInfo(JsonObject& janela){
  if(_isStopped){
    janela[JANELA_POS] = _JanelaSetpoint;
  }else{
    janela[JANELA_POS] = _JanelaPos;
  }
  janela[JANELA_SETPOINT] = _JanelaSetpoint;
}

void Janela::onNewJanelaJob(JsonObject& root){
  Serial.println("New Janela Job");
  
  if(root.containsKey(JANELA_SETPOINT)){
    _JanelaSetpoint = root[JANELA_SETPOINT];
    Serial.print("\tSetpoint = ");
    Serial.println(_JanelaSetpoint);
        if(_JanelaSetpoint > 100){
          _JanelaSetpoint = 100;
        }
        if(_JanelaSetpoint < 0){
          _JanelaSetpoint = 0;
        }
    //Serial.print("New Janela SP = ");
    //Serial.println(_JanelaSetpoint);
  }
}

