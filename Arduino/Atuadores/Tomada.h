#include "Arduino.h"
#include "Constants.h"
#include <ArduinoJson.h>
#ifndef Tomada_h
#define Tomada_h
class Tomada{
  public:
    Tomada::Tomada(int tomada_digitalPort);
    void onNewTomadaJob(boolean myJob);
    void beginTomadas(void);
    void controleTomada(void);
    void getTomadaInfo(JsonArray& data);
    int getTomadaInfo2(void);
  private:
    int _digitalPort;
    boolean _bStatus;
};
#endif

