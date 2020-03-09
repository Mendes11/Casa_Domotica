#include <SPI.h>
#include <MFRC522.h>
struct RFID_STRUCT{
  int isNewLog;
  //byte lastTag[4];
  String lastTag;
  int action;
  MFRC522 *rf;   // Create MFRC522 instance.
}rfid;

// Action = 0 -> Porta
// Action = 1 -> Portao

void getRFIDInfo(JsonObject& obj){
  obj[RFID_IS_NEW_ENTRY] = rfid.isNewLog;
  DynamicJsonBuffer buff;
  /*JsonArray& mArr = buff.createArray();
  mArr.add(rfid.lastTag[0]);
  mArr.add(rfid.lastTag[1]);
  mArr.add(rfid.lastTag[2]);
  mArr.add(rfid.lastTag[3]);
  obj[RFID_LAST_TAG] = mArr;*/
  //String test = String(rfid.lastTag,HEX);
  //Serial.println(test);
  rfid.lastTag.toUpperCase();
  obj[RFID_LAST_TAG] = rfid.lastTag;
  if(rfid.action == 0){
    obj[RFID_ACTION] = RFID_ACTION_PORTA;
  }else{
    obj[RFID_ACTION] = RFID_ACTION_PORTAO;
  }
  if(rfid.isNewLog == 1){
    rfid.isNewLog = 0;
  }
}
void getRFIDInfo2(String& arg1,String& arg2,int *arg3){
  arg1 = rfid.lastTag;
  arg1.toUpperCase();
  if(rfid.action == 0){
    arg2 = RFID_ACTION_PORTA;
  }else{
    arg2 = RFID_ACTION_PORTAO;
  }
  *arg3 = rfid.isNewLog;
  
}
void beginRFID(){
  rfid.isNewLog = false;
  rfid.lastTag[0] = 0;
  rfid.lastTag[1] = 0;
  rfid.lastTag[2] = 0;
  rfid.lastTag[3] = 0;
  rfid.action = 0;
  pinMode(RFID_CHANGE_ACTION_PORT,INPUT);
  SPI.begin();
  rfid.rf = new MFRC522(RFID_SS_PORT, RFID_RST_PORT);
  rfid.rf->PCD_Init();
}
void checkRFID(){
  if (rfid.rf->PICC_IsNewCardPresent()) { //If a new PICC placed to RFID reader continue
    if(rfid.rf->PICC_ReadCardSerial()){
      // Card read is complete
      //Serial.println(F("Scanned PICC's UID:"));
      byte lastTag[4];
      rfid.lastTag = "";
      for (int i = 0; i < 4; i++) {  //
        lastTag[i] = rfid.rf->uid.uidByte[i];
        Serial.print(lastTag[i], HEX);
        String aux = String(lastTag[i],HEX);
        rfid.lastTag += aux;
      }
      Serial.println("");
      if(digitalRead(RFID_CHANGE_ACTION_PORT) == HIGH){
        rfid.action = 0;
      }else{
        rfid.action = 0;
      }
      rfid.isNewLog = 1;
      rfid.rf->PICC_HaltA(); // Stop reading
    }
  }
}


