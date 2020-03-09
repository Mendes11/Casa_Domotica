#ifndef Teto_h
#define Teto_h
#include "Arduino.h"
#include "Constants.h"
#include <AFMotor.h>
#include <ArduinoJson.h>
#include <Stepper.h>

class Teto{
  public:
    Teto(int arg1,int arg2,int arg3,int arg4,int arg5); //Motor de Passo
    Teto(int motorID,int endstop_open_port,int endstop_close_port); //Motor normal.
    void beginTeto(void);
    void onNewTetoJob(JsonObject& root);
    void getTetoInfo(JsonObject& tetoJson);
    int getTetoStatus(void);
    void controleTeto(void);
    void getTetoInfo2(int TetoArr[2]);
  private:
    AF_DCMotor *_TetoMotor;
    Stepper *_M;
    int _iPos;
    int _iSetpoint;
    int _endstop_open_port;
    int _endstop_close_port;
    boolean _isMotorPasso;
    int _iIDDeviceStatus;
    int _iPasso;
};
#endif
