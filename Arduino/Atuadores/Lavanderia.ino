struct LavanderiaStruct{
  Teto *teto;
}lavanderia;

void beginLavanderia(){
  lavanderia.teto = new Teto(LAVANDERIA_TETO_MOTOR_NUM,LAVANDERIA_TETO_ENDSTOP_OPEN_PORT,LAVANDERIA_TETO_ENDSTOP_CLOSED_PORT);
}
void controleLavanderia(){
  lavanderia.teto->controleTeto();
}
void onNewLavanderiaJob(JsonObject& root){
  if(root.containsKey(TETO_JSON_ID)){
    lavanderia.teto->onNewTetoJob(root[TETO_JSON_ID]);
  }
}
void getLavanderiaInfo(JsonObject& obj){
  DynamicJsonBuffer buff;
  JsonObject& teto = buff.createObject();
  lavanderia.teto->getTetoInfo(teto);
  obj[TETO_JSON_ID] = teto;
}
