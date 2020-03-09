
// ################### COMUNICAÇÃO COM RPI POR I2C, TODAS AS FUNÇÕES RESPONSÁVEIS VEM AQUI. ########################

void beginI2C(){
  Wire.begin(ARDUINO_ATUADORES_ADDRESS);
  Wire.onReceive(onReceiveEvent);
  Wire.onRequest(onSendData);
  i = 0;
  receiveCount = 0;
  receiveAmount = 0;
  ii = 0;
  sendCount = 0;
  sendAmount = 0;
}
int readComb = 0;

void generateJson2(JsonArray& root){
  
}
void generateJson(JsonObject& root){
  //Serial.println(freeMemory());
  DynamicJsonBuffer jsonBuffer;
  JsonObject& salaJson = jsonBuffer.createObject();
  JsonObject& entradaJson = jsonBuffer.createObject();
  JsonObject& lavanderiaJson = jsonBuffer.createObject();
  //JsonObject& piscinaJson = jsonBuffer.createObject();
  //getSalaInfo(salaJson);
  //root[SALA_JSON_ID] = salaJson;
  getEntradaInfo(entradaJson);
  root[ENTRADA_JSON_ID] = entradaJson;
    //getPiscinaInfo(piscinaJson);
    //root[PISCINA_JSON_ID] = piscinaJson;
  getLavanderiaInfo(lavanderiaJson);
  root[LAVANDERIA_JSON_ID] = lavanderiaJson;  
  //Serial.println(freeMemory());
  //Serial.println("Objects criados");
  
  Serial.println(root.measureLength());
  
  //Serial.println(root.measureLength());
  
  //Serial.println(root.measureLength());
  //root.printTo(Serial);
  //Serial.println("");
}
void createData(char* myChar) {
  //Serial.println("Starting");
  //Serial.println(freeMemory());
  //StaticJsonBuffer<JSON_CHAR_BUFFER> jsonBuffer;
  //JsonObject& root = jsonBuffer.createObject();
  //generateJson(root);
  String msg;
  getMessage(msg);
  //Serial.println(msg);
  if(sendCount == 0){
    //sendAmount = (int) root.measureLength()/31;
    sendAmount = (int) msg.length()/31;
    sendAmount += 2;
    sendCount++;
    String aux = "{\"AMOUNT\":";
    aux.concat(sendAmount);
    aux += "}";
    aux.toCharArray(myChar,32);
  }else{
    //char buff[JSON_CHAR_BUFFER];
    //root.printTo(buff,JSON_CHAR_BUFFER);
    //String aux = String(buff);
    //aux = aux.substring(0+(31*ii),31*(1+ii));
    //aux.toCharArray(myChar,32);
    msg = msg.substring(0+(31*ii),31*(1+ii));
    msg.toCharArray(myChar,32);
    ii++;
    sendCount++;
  }
  if(sendCount == sendAmount && sendAmount != 0){
    ii = 0;
    sendCount = 0;
    sendAmount = 0;
  }
}

void getJsonObjects() {
  StaticJsonBuffer<JSON_CHAR_BUFFER> jsonBuffer;
  JsonObject& root = jsonBuffer.parseObject(jsonChar);
  if (!root.success()) {
    Serial.println("parseObject failed");
    //Serial.println(jsonChar);
    clearArray();
    receiveAmount = 0;
    receiveCount = 0;
    i = 0;
  } else {
    // Lógica para pegar os valores desejados pelo json.
    if(root.containsKey(PISCINA_JSON_ID)){
      onNewPiscinaJob(root[PISCINA_JSON_ID]);
    }else if(root.containsKey(SALA_JSON_ID)){
      onNewSalaJob(root[SALA_JSON_ID]);
    }else if (root.containsKey(LAVANDERIA_JSON_ID)){
      onNewLavanderiaJob(root[LAVANDERIA_JSON_ID]);
    }else if (root.containsKey(ENTRADA_JSON_ID)){
      onNewEntradaJob(root[ENTRADA_JSON_ID]);    
    }else {
      //Deu ruim em algo, reinicia tudo.
      clearArray();
      receiveAmount = 0;
      receiveCount = 0;
      i = 0;
    }
  }
  isNewJson = false;
  clearArray();
}
void checkNewEvent() {
  newEvent = false;
  if (receiveCount == 0) {
    // Primeiro pacote recebido, nele haverá os dados sobre quantos pacotes de 32 caracteres serão recebidos
    StaticJsonBuffer<32> jsonBuffer;
    JsonObject& root = jsonBuffer.parseObject(jsonChar);
    if (root.success()) {
      if (root.containsKey(RECEIVE_AMOUNT)) {
        receiveAmount = root[RECEIVE_AMOUNT];
        clearArray();
        i = 0;
        receiveCount++;
      } else {
        receiveAmount = 0;
        receiveCount = 0;
        i = 0;
        clearArray();
      }
    } else {
      Serial.println("Erro ao receber o packetAMount");
      receiveAmount = 0;
      receiveCount = 0;
      i = 0;
      clearArray();
    }
  } else {
    receiveCount++;
  }
  if ((receiveCount == receiveAmount) && (receiveAmount != 0)) {
    isNewJson = true;
    receiveCount = 0;
    receiveAmount = 0;
    i = 0;
  }
}
void clearArray() {
  for (int j = 0; j < JSON_CHAR_BUFFER; j++) {
    jsonChar[j] = ' ';
  }
}
void onReceiveEvent(int howMany) {
  boolean aux = false;
  if (howMany != 1) {
    while (1 <= Wire.available()) { // loop through all but the last
      if (aux) {
        jsonChar[i] = Wire.read(); // receive byte as a character

        i++;
      } else {
        aux = true;
        jsonChar[i] = Wire.read();
      }
    }
    //Serial.println(jsonChar);
    while(Wire.available()){
      Wire.read();
    }
    //newEvent = true;
    checkNewEvent();
  }
}

void onSendData() {
  char myChar[32];
  createData(myChar);
  //Serial.println(myChar);
  Wire.write(myChar);
  while (Wire.available()){ //Por alguma razão o buffer não se limpa (culpa do raspberry?)
    Wire.read();
  }
}
