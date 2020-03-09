// Biblioteca da Comunicação
#include <ArduinoJson.h>
#include <Wire.h>
#include "Constants.h"
#include <Stepper.h>
#include <MemoryFree.h>
// Inclusão dos atuadores
#include "Janela.h"
#include "Luminosidade.h"
#include "Porta.h"
#include "Portao.h"
#include "Teto.h"
#include "Tomada.h"

//#include <AFMotor.h>
//#include <PID_v1.h>




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
unsigned long t;
void setup() {
  // put your setup code here, to run once:
  isNewJson = false;
  newEvent = false;
  Serial.begin(9600);
  Serial.println("Iniciando Operação");
  //beginPiscina();
  beginSala();
  beginEntrada();
  beginLavanderia();
  beginI2C();
  //DynamicJsonBuffer buff;
    //JsonArray& root = buff.createArray();
    //generateJson2(root);
  //  char aux[JSON_CHAR_BUFFER];
  //  root.printTo(Serial);
  t = millis();
  
}


int test = 100;
void loop() {
  if(millis() - t > 1000){
    //Serial.println("Ping");
    t = millis();
    //Serial.println(analogRead(8));
    //getMessage();
  }
  // put your main code here, to run repeatedly:
  if(isNewJson){
    // Verificar o json e analisar as variáveis.
    getJsonObjects();
  }else{
    controleSala();
    //controlePiscina();
    controleEntrada();
    controleLavanderia();
  }
}

