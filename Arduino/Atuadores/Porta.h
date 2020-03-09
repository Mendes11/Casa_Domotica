#include <ArduinoJson.h>
#include "Constants.h"
#include "Arduino.h"
#ifndef Porta_h
#define Porta_h

class Porta{
  public:
    Porta(int digitalPort,int manualOpenPort);
    void onNewPortaJob(JsonObject& root);
    void beginPorta(void);
    void controlePorta();
    void getPortaInfo(JsonObject& portaJson);
    void getPortaInfo2(int arr[3]);
  private:
    boolean _bOpen;
    unsigned long _timeToClose;
    unsigned long _lastOpen;
    boolean _bSetOpen;
    int _digitalPort;
    int _iIDDeviceStatus;
    int _manualOpenPort;
};
#endif

