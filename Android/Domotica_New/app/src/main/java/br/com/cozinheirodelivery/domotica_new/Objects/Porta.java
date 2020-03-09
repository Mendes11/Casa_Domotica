package br.com.cozinheirodelivery.domotica_new.Objects;

/**
 * Created by Mendes on 11/03/2017.
 */

public class Porta {
    int iIDPorta,bOpen,bHabilitado,iIDDeviceStatus;
    String cNome;
    Double fTimeAutoClose = 5.0;

    public int getiIDPorta() {
        return iIDPorta;
    }

    public void setiIDPorta(int iIDPorta) {
        this.iIDPorta = iIDPorta;
    }

    public int getbOpen() {
        return bOpen;
    }

    public void setbOpen(int bOpen) {
        this.bOpen = bOpen;
    }

    public int getbHabilitado() {
        return bHabilitado;
    }

    public void setbHabilitado(int bHabilitado) {
        this.bHabilitado = bHabilitado;
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

    public Double getfTimeAutoClose() {
        return fTimeAutoClose;
    }

    public void setfTimeAutoClose(Double fTimeAutoClose) {
        this.fTimeAutoClose = fTimeAutoClose;
    }
}
