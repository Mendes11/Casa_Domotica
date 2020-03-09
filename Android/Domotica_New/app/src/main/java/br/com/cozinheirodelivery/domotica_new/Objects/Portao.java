package br.com.cozinheirodelivery.domotica_new.Objects;

/**
 * Created by Mendes on 11/03/2017.
 */

public class Portao {
    int iIDPortao,bOpen,bPos,bEnabled,iIDDeviceStatus;
    String cNome;
    Double fTimeAutoClose = 5.0;

    public int getiIDPortao() {
        return iIDPortao;
    }

    public void setiIDPortao(int iIDPortao) {
        this.iIDPortao = iIDPortao;
    }

    public int getbOpen() {
        return bOpen;
    }

    public void setbOpen(int bOpen) {
        this.bOpen = bOpen;
    }

    public int getbPos() {
        return bPos;
    }

    public void setbPos(int bPos) {
        this.bPos = bPos;
    }

    public int getbEnabled() {
        return bEnabled;
    }

    public void setbEnabled(int bEnabled) {
        this.bEnabled = bEnabled;
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
