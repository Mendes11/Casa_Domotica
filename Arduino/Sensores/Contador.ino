struct Comodo{
  int iUserCont;
};
struct Comodo Sala;
struct Comodo Lavanderia;

void beginContador(){
  pinMode(IR_ENT_SALA_PORT,INPUT);  // |
     pinMode(IR_ENT_LAVANDERIA_PORT,INPUT);  // |
     pinMode(IR_SAIDA_SALA_PORT,INPUT);  // |-> Pinos de IR "setados" como entrada
     pinMode(IR_SAIDA_LAVANDERIA_PORT,INPUT);  // |
}

void checkContador(){
  int val_ent_1 = digitalRead (IR_ENT_SALA_PORT); // |
  int val_ent_2 = digitalRead (IR_SAIDA_LAVANDERIA_PORT); // |-> Mede os IR de entrada

  int val_sai_1 = digitalRead (IR_SAIDA_SALA_PORT); // |
  int val_sai_2 = digitalRead (IR_ENT_LAVANDERIA_PORT); // |-> Mede os IR de saida

     if ((val_ent_1 != 1)) // verifica se alguem esta entrando na sala
     {
           Sala.iUserCont++;
           Serial.println("Foi");
           delay (200);                     // Aguarda a pessoa passar pelo sensor
     }
     if((val_ent_2 != 1)){
          Lavanderia.iUserCont --;
          if(Lavanderia.iUserCont <0){
            Lavanderia.iUserCont = 0;
          }else{ //Não pode entrar mais gente na sala do que tinha na lavanderia
            Sala.iUserCont++;
          }
          delay(200);
     }
     if ((val_sai_1 != 1)) // Verifica se alguem esta saindo da sala
     {
           Sala.iUserCont--;
           if(Sala.iUserCont < 0){
            Sala.iUserCont = 0;
           }
           delay (200);                     // Aguada a pessoa passar pelo sensor
     }
     if ((val_sai_2 != 1)){
           Sala.iUserCont--;
           if(Sala.iUserCont < 0 ){
            Sala.iUserCont = 0;
           }else{
            Lavanderia.iUserCont ++;
           }
           delay(200);
     }
}

void getSalaInfo(JsonObject& obj){
  obj[COMODO_USER_COUNT] = Sala.iUserCont;
}
void getSalaInfo2(int *arg){
  *arg = Sala.iUserCont;
}
void getLavanderiaInfo(JsonObject& obj){
  obj[COMODO_USER_COUNT] = Lavanderia.iUserCont;
}
void getLavanderiaInfo2(int *arg){
  *arg = Lavanderia.iUserCont;
}

