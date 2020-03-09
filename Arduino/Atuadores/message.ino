void getMessage(String& test){
  int Jarr[2];
  sala.janela->getJanelaInfo2(Jarr);
  int SJP = Jarr[0];
  int SJSP = Jarr[1];
  
  int LUArr[4];
  sala.luz->getLuminosidadeInfo2(LUArr);
  int SLUS = LUArr[0]; // ALTEREI PARA INT AQUI
  int SLUSP = LUArr[1];
  int SLUC = LUArr[2];
  int SLUM = LUArr[3];

  
  int ST1 = sala.tomada[0]->getTomadaInfo2();
  int ST2 = sala.tomada[1]->getTomadaInfo2();
  int ST3 = sala.tomada[2]->getTomadaInfo2();

  
  int EPAArr[3];
  entrada.porta->getPortaInfo2(EPAArr);
  int EPAO = EPAArr[0];
  int EPAD = EPAArr[1];
  int EPAF = EPAArr[2];

  
  int EPOO = 0;
  int EPOP = 1;
  int EPOD = 5;
  int EPOF = 20000;
  int POArr[4];
  entrada.portao->getPortaoInfo2(POArr);
  EPOO = POArr[0];
  EPOP = POArr[1];
  EPOD = POArr[2];
  EPOF = POArr[3];

  int TetoArr[2];
  lavanderia.teto->getTetoInfo2(TetoArr);
  int LTP = TetoArr[0];
  int LTSP = TetoArr[1];
  test = "{\"S\":{\"J\":{\"P\":";
  test += SJP;
  test +=",\"SP\":";
  test +=SJSP;
  test +="},\"Lu\":{\"S\":";
  test +=SLUS;
  test+=",\"SP\":";
  test +=SLUSP;
  test+=",\"C\":";
  test+=SLUC;
  test +=",\"M\":";
  test +=SLUM;
  test +="},\"T\":[";
  test +=ST1;
  test +=",";
  test +=ST2;
  test +=",";
  test +=ST3;
  test +="]},\"E\":{\"PA\":{\"O\":";
  test +=EPAO;
  test +=",\"D\":";
  test +=EPAD;
  test +=",\"F\":";
  test +=EPAF;
  test +="},\"PO\":{\"O\":";
  test +=EPOO;
  test +=",\"P\":";
  test +=EPOP;
  test +=",\"D\":";
  test +=EPOD;
  test +=",\"F\":";
  test +=EPOF;
  test +="}},\"L\":{\"T\":{\"P\":";
  test +=LTP;
  test +=",\"SP\":";
  test +=LTSP;
  test +="}}}";
  //Serial.println(test);
}

