struct Piscina{
  Teto *mTeto;
  Luminosidade *mLuz;
}piscina;

void beginPiscina () {
  piscina.mLuz = new Luminosidade(PISCINA_LDR,PISCINA_LED_TETO_PORT);
  piscina.mTeto = new Teto(500,8,10,9,11);
  //pinMode (13, OUTPUT);
  //pinMode (A1, INPUT);
  //pinMode (12, OUTPUT);
  //pinMode (7, OUTPUT);
  //pinMode (3, INPUT);
  //pinMode (2, OUTPUT);
  
}


void controlePiscina(){
  piscina.mLuz->controleLuminosidade();
  piscina.mTeto->controleTeto();
}

void piscinaLigaRGB(){
  //while ((vslz < 50) && (vbll == LOW)) {
      digitalWrite (13, HIGH);
      digitalWrite (12, LOW);
      digitalWrite (7, HIGH);
      delay (500);
      digitalWrite (7, LOW);
      digitalWrite (2, HIGH);
      delay (500);
      digitalWrite (2, LOW);
      digitalWrite (12, HIGH);
      delay (500);
  //}
}
void logicaEstranhaComRGB(){
  digitalWrite (12, LOW);
  digitalWrite (7, HIGH);
  digitalWrite (13, LOW);
  delay (500);
}

void onNewPiscinaJob(JsonObject& root){
  if(root.containsKey(TETO_JSON_ID)){
    piscina.mTeto->onNewTetoJob(root[TETO_JSON_ID]);
  }
  if(root.containsKey(LUMINOSIDADE_JSON_ID)){
    piscina.mLuz->onNewLuminosidadeJob(root[LUMINOSIDADE_JSON_ID]); 
  }
}
void getPiscinaInfo(JsonObject& obj){
  StaticJsonBuffer<150> buff;
  JsonObject& teto = buff.createObject();
  JsonObject& luz = buff.createObject();
  piscina.mTeto->getTetoInfo(teto);
  piscina.mLuz->getLuminosidadeInfo(luz);
  obj[TETO_JSON_ID] = teto;
  obj[LUMINOSIDADE_JSON_ID] = luz;
  //obj.printTo(Serial);
}

