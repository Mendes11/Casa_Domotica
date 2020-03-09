
// ################### COMUNICAÇÃO COM RPI POR I2C, TODAS AS FUNÇÕES RESPONSÁVEIS VEM AQUI. ########################

void beginI2C(){
  Wire.begin(ARDUINO_SENSORES_ADDRESS);
  Wire.onReceive(onReceiveEvent);
  Wire.onRequest(onSendData);
  i = 0;
  receiveCount = 0;
  receiveAmount = 0;
  ii = 0;
  sendCount = 0;
  sendAmount = 0;
}
void generateJson(JsonObject& root){
  DynamicJsonBuffer jsonBuffer;
  //######### Informação do RFID ############
  JsonObject& RFID_json = jsonBuffer.createObject();
  getRFIDInfo(RFID_json);
  root[RFID] = RFID_json;
  //######### Informação da Chuva ###########
  JsonObject& chuva = jsonBuffer.createObject();
  getChuvaInfo(chuva);
  root[CHUVA] = chuva;
  //######### Informação da Sala ############
  JsonObject& salaJson = jsonBuffer.createObject();
  getSalaInfo(salaJson);
  root[COMODO_SALA_ID] = salaJson;
  //######### Informação da Sala ############
  JsonObject& lavanderiaJson = jsonBuffer.createObject();
  getLavanderiaInfo(lavanderiaJson);
  root[COMODO_LAVANDERIA_ID] = lavanderiaJson;
}
void createData(char* myChar) {
  //StaticJsonBuffer<JSON_CHAR_BUFFER> jsonBuffer;
  //DynamicJsonBuffer jsonBuffer;
  //JsonObject& root = jsonBuffer.createObject();
  //generateJson(root);
  //root.printTo(Serial);
  String msg;
  getMessage(msg);
  if(sendCount == 0){
    //sendAmount = (int) root.measureLength()/31;
    //sendAmount = (int) mInfoSize/31;
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
    //String aux = String(mInfo);
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
    if(rfid.isNewLog == 1){
      rfid.isNewLog = 0;
    }
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
    
    if (root.containsKey(RFID)) {
      //onNewJanelaJob(root[RFID_PORTA]);
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
    Serial.println(jsonChar);
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
