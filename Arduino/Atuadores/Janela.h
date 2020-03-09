#include "Arduino.h"
#include "Constants.h"
#include <ArduinoJson.h>
#include <AFMotor.h>

#define MINIMO  45
#define MAXIMO  920
#define PORCENTAGEM_ERRO  5

#ifndef Janela_h
#define Janela_h
class Janela{
  public:
    Janela(int motorID,int _analogPort);
    void beginJanela(void);
    void controleJanela(void);
    void getPos(void);
    void getJanelaInfo(JsonObject& janela);
    void onNewJanelaJob(JsonObject& root);
    void getJanelaInfo2(int arr[]);
    void setSetpoint(int i);
  private:
    AF_DCMotor *_JanelaMotor;
    int _JanelaPos;
    int _JanelaSetpoint;
    int _JanelaError;
    int _analogPort;
    boolean _isStopped;
    int _iIDDeviceStatus;
};
#endif

