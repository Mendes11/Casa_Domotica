#include "Arduino.h"
#include "Constants.h"
#include <ArduinoJson.h>
#include <PID_v1.h>
#ifndef Luminosidade_h
#define Luminosidade_h

class Luminosidade{
  public:
    Luminosidade(int ldrPort,int ledPort);
    void onNewLuminosidadeJob(JsonObject& root);
    double getSensorVal(boolean isCalibration);
    void controleLuminosidade(void);
    void getLuminosidadeInfo(JsonObject& luminosidadeJson);
    void getLuminosidadeInfo2(int arr[]);
  private:
    int _ldrPort;
    int _ledPort;
    double _pidSetpoint;
    double _pidSensor;
    double _output;
    int _iSetpoint;
    int _bControle;
    int _bManualOn;
    int _minVal;
    unsigned long _mTime;
    int _maxVal;
    boolean _isCalibration;
    PID *_myPID;
};
#endif
