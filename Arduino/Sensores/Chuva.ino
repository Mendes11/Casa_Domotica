void beginChuva(){
  pinMode (SENSOR_CHUVA_PORT, INPUT);
}

void checkChuva(){
  if(digitalRead(SENSOR_CHUVA_PORT) == LOGICA_SENSOR_CHUVA){
      // Começou a chover, seta isChuva.
      isChuva = 1;
    }else{
      isChuva = 0;
    }
}


void getChuvaInfo(JsonObject& obj){
  obj[IS_CHUVA] = isChuva;
}
void getChuvaInfo2(int *arg){
 *arg = isChuva;
}

