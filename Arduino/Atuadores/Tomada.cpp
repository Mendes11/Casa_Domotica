#include "Arduino.h"
#include "Constants.h"
#include <ArduinoJson.h>
#include "Tomada.h"
Tomada::Tomada(int tomada_digitalPort){
   _digitalPort = tomada_digitalPort;
   beginTomadas();
}

void Tomada::onNewTomadaJob(boolean myJob){ //Esse caso eu farei o tratamento externamente, já que é um array...
  _bStatus = myJob;
}

void Tomada::beginTomadas(){
  _bStatus = true;
  pinMode(_digitalPort,OUTPUT);
  digitalWrite(_digitalPort,RELAY_ACTIVATE_LOGIC);
}

void Tomada::controleTomada(){
    if(_bStatus){
      digitalWrite(_digitalPort,RELAY_ACTIVATE_LOGIC);
    }else{
      digitalWrite(_digitalPort,RELAY_DEACTIVATE_LOGIC);
    }
}
int Tomada::getTomadaInfo2(){
  if(_bStatus){
    return 1;
  }else{
    return 0;
  }
}

// O certo seria retornar um jsonObject, mas como aqui só retorna um valor, vou dar um migué.
void Tomada::getTomadaInfo(JsonArray& data){
  //JsonArray& data = tomadaJson.createNestedArray(TOMADAS_JSON_JOB_ARRAY_ID); //Isso aqui vai para o getJsonObject do cômodo.
    if(_bStatus == true){
      data.add(1);
    }else{
      data.add(0);
    }
}

