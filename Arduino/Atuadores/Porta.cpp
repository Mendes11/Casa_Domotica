#include <ArduinoJson.h>
#include "Constants.h"
#include "Arduino.h"
#include "Porta.h"
Porta::Porta(int digitalPort,int manualOpenPort){
  _digitalPort = digitalPort;
  _manualOpenPort = manualOpenPort;
  beginPorta();
}
void Porta::onNewPortaJob(JsonObject& root){
  if(root.containsKey(PORTA_JSON_JOB_OPEN)){
    if(root[PORTA_JSON_JOB_OPEN] == 1){ // Abrir
      _bSetOpen = true;
    }
  }
  if(root.containsKey(PORTA_JSON_JOB_TIME)){
    _timeToClose = (unsigned long) root[PORTA_JSON_JOB_TIME]; // s to ms
  }
}

void Porta::beginPorta(){
  pinMode(_digitalPort,OUTPUT);
  pinMode(_manualOpenPort,INPUT);
  digitalWrite(_digitalPort,RELAY_ACTIVATE_LOGIC);
  _bOpen = false;
  _bSetOpen = false;
  _iIDDeviceStatus = 5;
  _timeToClose = 5000; // 5s padrão.
}

void Porta::controlePorta(){
  if(digitalRead(_manualOpenPort) == PORTA_MANUAL_OPEN_PORT_LOGIC && ENTRADA_PORTA_MANUAL){
    _bSetOpen = true;
  }
  if(_bSetOpen){
    Serial.println("Abrindo Porta");
    _bSetOpen = false;
    _bOpen = true;
    digitalWrite(_digitalPort,RELAY_DEACTIVATE_LOGIC);
    _iIDDeviceStatus = DEVICE_ABERTO;
    _lastOpen = millis();
  }
  if(_bOpen){
    if((millis() - _lastOpen) > _timeToClose){
      digitalWrite(_digitalPort,RELAY_ACTIVATE_LOGIC);
      _iIDDeviceStatus = DEVICE_FECHADO;
      _bOpen = false;
    }
  }
}

void Porta::getPortaInfo2(int arr[3]){
  if(_bOpen){
    arr[0] = 1;
  }else{
    arr[0] = 0;
  }
  //bOpen = _bOpen; // OU o setOpen?
  arr[1] = _iIDDeviceStatus;
  arr[2] = _timeToClose;
}
void Porta::getPortaInfo(JsonObject& portaJson){
  portaJson[PORTA_JSON_JOB_OPEN] = _bOpen; // OU o setOpen?
  portaJson[PORTAO_JSON_DEVICE_STATUS] = _iIDDeviceStatus;
  portaJson[PORTA_JSON_JOB_TIME] = _timeToClose;
}

