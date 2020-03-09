#include "Arduino.h"
#include <ArduinoJson.h>
#include "Constants.h"
#include <AFMotor.h>
#ifndef Portao_h
#define Portao_h
class Portao{
  public:
    Portao(int endstop_open_port,int endstop_close_port,int motorID);
    void beginPortao(void);
    void onNewPortaoJob(JsonObject& root);
    boolean getPortaoStatus(void);
    void controlePortao(void);
    void getPortaoInfo(JsonObject& portaoJson);
    void getPortaoInfo2(int arr[4]);
  private:
    boolean _bOpen;
    int _timeToClose;
    unsigned long _lastOpen;
    boolean _bSetOpen;
    int _iPos;
    int _iIDDeviceStatus;
    int _endstop_open_port;
    int _endstop_close_port;
    AF_DCMotor *_PortaoMotor;
};
#endif
