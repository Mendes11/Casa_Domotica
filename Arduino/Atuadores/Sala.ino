struct Sala{
  Luminosidade *luz;
  Tomada *tomada[TOMADAS_NUM];
  Janela *janela;
}sala;

void beginSala () {
  sala.luz = new Luminosidade(SALA_LDR,SALA_LED_PORT);
  sala.janela = new Janela(SALA_JANELA_MOTOR_NUM,SALA_JANELA_ANALOG_PORT);
  int ports[] = TOMADAS_DIGITAL_PORTS;
  for (int z = 0; z<TOMADAS_NUM;z++){
    sala.tomada[z] = new Tomada(ports[z]);
  }
}
void controleSala(){
  sala.luz->controleLuminosidade();
  sala.janela->controleJanela();
  for (int z = 0; z<TOMADAS_NUM;z++){
    sala.tomada[z]->controleTomada();
  }
}

void onNewSalaJob(JsonObject& root){
  if(root.containsKey(JANELA_JSON_ID)){
    sala.janela->onNewJanelaJob(root[JANELA_JSON_ID]);
  }
  if(root.containsKey(LUMINOSIDADE_JSON_ID)){
    sala.luz->onNewLuminosidadeJob(root[LUMINOSIDADE_JSON_ID]); 
  }
  if(root.containsKey(TOMADAS_JSON_ID)){
    if(root[TOMADAS_JSON_ID].is<JsonArray&>()){
      JsonArray& arr = root[TOMADAS_JSON_ID];
      for(int z = 0; z < TOMADAS_NUM; z++){
        if(arr[z] == 1){
          sala.tomada[z]->onNewTomadaJob(true);
        }else{
          sala.tomada[z]->onNewTomadaJob(false);
        }
      }
    }
  }
}
void getSalaInfo(JsonObject& obj){
  DynamicJsonBuffer buff;
  JsonObject& tomada = buff.createObject();
  JsonObject& luz = buff.createObject();
  JsonObject& janela = buff.createObject();
  sala.janela->getJanelaInfo(janela);  
  sala.luz->getLuminosidadeInfo(luz);
  JsonArray& tomadas = buff.createArray();
  for(int z = 0; z < TOMADAS_NUM; z++){
    sala.tomada[z]->getTomadaInfo(tomadas);
  }
  //tomadas.printTo(Serial);
  obj[JANELA_JSON_ID] = janela;
  obj[LUMINOSIDADE_JSON_ID] = luz;
  obj[TOMADAS_JSON_ID] = tomadas;
  //obj.printTo(Serial);
}
