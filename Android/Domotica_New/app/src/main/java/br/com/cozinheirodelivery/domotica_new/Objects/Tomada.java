package br.com.cozinheirodelivery.domotica_new.Objects;

/**
 * Created by Mendes on 09/03/2017.
 */

public class Tomada {
    int iIDTomada = -1;
    String cNome = "";
    int bStatus = 0;
    int iIDComodo;
    public Tomada(){

    }

    public int getiIDComodo() {
        return iIDComodo;
    }

    public void setiIDComodo(int iIDComodo) {
        this.iIDComodo = iIDComodo;
    }

    public int getiIDTomada() {
        return iIDTomada;
    }

    public void setiIDTomada(int iIDTomada) {
        this.iIDTomada = iIDTomada;
    }

    public int getbStatus() {
        return bStatus;
    }

    public void setbStatus(int bStatus) {
        this.bStatus = bStatus;
    }

    public String getcNome() {
        return cNome;
    }

    public void setcNome(String cNome) {
        this.cNome = cNome;
    }
}
