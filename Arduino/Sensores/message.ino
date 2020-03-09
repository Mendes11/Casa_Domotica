void getMessage(String& msg){
  String cRFID,action;
  int bNewEntry =0;
  int bChuva=0;
  int salaCont=0;
  int lavCont=0;
  getChuvaInfo2(&bChuva);
  getSalaInfo2(&salaCont);
  getLavanderiaInfo2(&lavCont);
  getRFIDInfo2(cRFID,action,&bNewEntry);
  msg = "{\"RFID\":{\"bNewEntry\":"+String(bNewEntry,DEC);
  msg +=",\"cRFID\":\""+cRFID;
  msg +="\",\"action\":\""+action;
  msg +="\"},\"Chuva\":{\"bChuva\":"+String(bChuva,DEC);
  msg +="},\"Sala\":{\"iUserCont\":"+String(salaCont,DEC);
  msg +="},\"Lavanderia\":{\"iUserCont\":"+String(lavCont,DEC);
  msg +="}}";
  //Serial.println(msg);
}

