struct EntradaStruct{
  Porta *porta;
  Portao *portao;
}entrada;

void beginEntrada(){
  entrada.porta = new Porta(ENTRADA_PORTA_DIGITAL_PORT,ENTRADA_PORTA_MANUAL_OPEN_PORT);
  entrada.portao = new Portao(ENTRADA_PORTAO_ENDSTOP_OPEN_PORT,ENTRADA_PORTAO_ENDSTOP_CLOSED_PORT,ENTRADA_PORTAO_MOTOR_NUM);
}
void controleEntrada(){
  entrada.porta->controlePorta();
  entrada.portao->controlePortao();
}
void onNewEntradaJob(JsonObject& root){
  if(root.containsKey(PORTA_JSON_ID)){
    entrada.porta->onNewPortaJob(root[PORTA_JSON_ID]);
  }
  if(root.containsKey(PORTAO_JSON_ID)){
    entrada.portao->onNewPortaoJob(root[PORTAO_JSON_ID]);
  }
}
void getEntradaInfo(JsonObject& obj){
  DynamicJsonBuffer buff;
  JsonObject& porta = buff.createObject();
  JsonObject& portao = buff.createObject();
  entrada.porta->getPortaInfo(porta);
  obj[PORTA_JSON_ID] = porta;
  entrada.portao->getPortaoInfo(portao);
  obj[PORTAO_JSON_ID] = portao;
  //obj.printTo(Serial);
}

