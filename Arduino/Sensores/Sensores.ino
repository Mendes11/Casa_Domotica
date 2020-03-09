// Biblioteca da Comunicação
#include <ArduinoJson.h>
#include <Wire.h>
#include "Constants.h"

//################# Variáveis responsáveis pela comunicação I2C

char jsonChar[JSON_CHAR_BUFFER];
boolean isNewJson,newEvent;

// ########### Variáveis I2C ###############
int i;
int receiveCount;
int receiveAmount;
int ii;
int sendCount;
int sendAmount;

// ########## VARIÁVEIS ################
int isChuva = 0;
int RFID_ID;
int mInfoSize = 0;
unsigned long t;
void setup() {
  // put your setup code here, to run once:
  beginChuva();
  Serial.begin(9600);
  beginRFID();
  beginContador();
  beginI2C();
  //StaticJsonBuffer<JSON_CHAR_BUFFER> jsonBuffer;
  /*DynamicJsonBuffer jsonBuffer;
  JsonObject& root = jsonBuffer.createObject();

  generateJson(root);
  root.printTo(mInfo,JSON_CHAR_BUFFER);
  mInfoSize = root.measureLength();
  //Serial.println("");
  */
  t = millis();
}
void loop() {
  // put your main code here, to run repeatedly:
  checkChuva();
  checkRFID();
  checkContador();
  //if(millis() - t >1000){
   //StaticJsonBuffer<JSON_CHAR_BUFFER> jsonBuffer;
  //DynamicJsonBuffer jsonBuffer;
  //JsonObject& root = jsonBuffer.createObject();

  //generateJson(root);
  //root.printTo(Serial);
  //root.printTo(mInfo,JSON_CHAR_BUFFER);
  //mInfoSize = root.measureLength();
  //t = millis();
  //String aux;
  //getMessage(aux);
  //Serial.println(aux);
  //}
}
