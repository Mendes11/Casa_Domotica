#include "Arduino.h"
#include "Constants.h"
#include <ArduinoJson.h>
#include <PID_v1.h>
#include "Luminosidade.h"

Luminosidade::Luminosidade(int ldrPort,int ledPort){
  _ldrPort = ldrPort;
  _ledPort = ledPort;
  pinMode(_ledPort,OUTPUT);
  _pidSetpoint = 0.0;
  _iSetpoint = 80;
  _output = 0.0;
  _bManualOn = 0;
  _pidSensor = 0.0;
  _bControle = 0;
  _minVal = 20; //Fazendo o contrário ele irá se calibrar para se ajustar...
  _maxVal = 230;
  _isCalibration = true;
  _pidSensor = analogRead(_ldrPort);
  _pidSetpoint = ((double)(_iSetpoint/100.0)*_maxVal)+_minVal;
  _myPID = new PID(&_pidSensor,&_output,&_pidSetpoint,0.28,0.45,0.01,DIRECT);
  _myPID->SetMode(AUTOMATIC);
}

void Luminosidade::onNewLuminosidadeJob(JsonObject& root){
  Serial.println("New Luminosidade Job");
  if(root.containsKey(LUMINOSIDADE_JOB)){ // 0 = fechar ; 1 = abrir
      _iSetpoint = root[LUMINOSIDADE_JOB];
      _pidSetpoint = ((double)(_iSetpoint/100.0)*_maxVal)+_minVal;
      Serial.print("Setpoint");
      Serial.println(_pidSetpoint);
  }
  if(root.containsKey(LUMINOSIDADE_CONTROLE)){
    _bControle = root[LUMINOSIDADE_CONTROLE];
    Serial.print("Controle");
    Serial.println(_bControle);
  }
  if(root.containsKey(LUMINOSIDADE_MANUAL_ON)){
    _bManualOn = root[LUMINOSIDADE_MANUAL_ON];
    Serial.print("ManualOn");
    Serial.println(_bManualOn);
  }
  if(root.containsKey(LUMINOSIDADE_CALIBRATION)){
    if(root[LUMINOSIDADE_CALIBRATION] == 1){
      _isCalibration = true;
    }
  }
}
double Luminosidade::getSensorVal(boolean isCalibration){
  int val = analogRead(_ldrPort);    // ler o valor do LDR
  if(isCalibration){
    if(val < _minVal){
      _minVal = val;
    }else if (val > _maxVal){
      _maxVal = val;
    }
  }
  double _iVal = ((double)(val - _minVal)/_maxVal)*100.0; //Valor lido menos menor valor obtido sobre valor máximo obtido dá a porcentagem do valor lido em relação à calibração. Os valores mínimo e máximo são atualizados conforme o passar do tempo até se ajustarem...
                                         //É possível que o ideal seria jogar isso no raspberry para ele ter um controle de atualizar esses valores conforme o tempo, para diferentes estações do ano.
  return _iVal;
}

void Luminosidade::controleLuminosidade(){
  if(!_isCalibration){ // calibração... ideal startar com luzes apagadas e de noite.
    if(_bControle == 0){
      _pidSensor = analogRead(_ldrPort);
      _myPID->Compute();
      analogWrite(_ledPort,_output);
    }else{
      if(_bManualOn == 1){
        digitalWrite(_ledPort,HIGH);
      }else{
        digitalWrite(_ledPort,LOW);
      }
    }
  }else{
    _minVal = 1023;
    _maxVal = 0;
    digitalWrite(_ledPort,LOW);
    delay(300);
    getSensorVal(true);
    delay(300);
    getSensorVal(true);
    delay(300);
    getSensorVal(true);
    delay(300);
    getSensorVal(true);
    delay(300);
    digitalWrite(_ledPort,HIGH);
    delay(300);
    getSensorVal(true);
    delay(300);
    getSensorVal(true);
    delay(300);
    getSensorVal(true);
    delay(300);
    getSensorVal(true);
    delay(300);
    _isCalibration = false;
  }
}

void Luminosidade::getLuminosidadeInfo2(int arr[]){
  arr[0] = (int)(getSensorVal(false)*100);
  arr[1] = _iSetpoint;
  arr[2] = _bControle;
  arr[3] = _bManualOn;
}
void Luminosidade::getLuminosidadeInfo(JsonObject& luminosidadeJson){
  luminosidadeJson[LUMINOSIDADE_SENSOR] = getSensorVal(false);
  luminosidadeJson[LUMINOSIDADE_JOB] = _iSetpoint;
  luminosidadeJson[LUMINOSIDADE_CONTROLE] = _bControle;
  luminosidadeJson[LUMINOSIDADE_MANUAL_ON] = _bManualOn;
}
