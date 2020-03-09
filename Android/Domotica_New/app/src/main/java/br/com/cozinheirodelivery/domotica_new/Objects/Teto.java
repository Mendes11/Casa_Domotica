package br.com.cozinheirodelivery.domotica_new.Objects;

/**
 * Created by Mendes on 11/03/2017.
 */

public class Teto {
    int iIDTeto,iSetpoint,iPos,bAvancado,iIDDeviceStatus,iIDComodo;
    String cNome;

    public int getiIDTeto() {
        return iIDTeto;
    }

    public int getiIDComodo() {
        return iIDComodo;
    }

    public void setiIDComodo(int iIDComodo) {
        this.iIDComodo = iIDComodo;
    }

    public void setiIDTeto(int iIDTeto) {
        this.iIDTeto = iIDTeto;
    }

    public int getiSetpoint() {
        return iSetpoint;
    }

    public void setiSetpoint(int iSetpoint) {
        this.iSetpoint = iSetpoint;
    }

    public int getiPos() {
        return iPos;
    }

    public void setiPos(int iPos) {
        this.iPos = iPos;
    }

    public int getbAvancado() {
        return bAvancado;
    }

    public void setbAvancado(int bAvancado) {
        this.bAvancado = bAvancado;
    }

    public int getiIDDeviceStatus() {
        return iIDDeviceStatus;
    }

    public void setiIDDeviceStatus(int iIDDeviceStatus) {
        this.iIDDeviceStatus = iIDDeviceStatus;
    }

    public String getcNome() {
        return cNome;
    }

    public void setcNome(String cNome) {
        this.cNome = cNome;
    }
}
