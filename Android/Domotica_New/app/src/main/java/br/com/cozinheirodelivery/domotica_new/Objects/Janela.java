package br.com.cozinheirodelivery.domotica_new.Objects;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Mendes on 02/03/2017.
 */

public class Janela implements Parcelable {
    int iIDJanela = -1;
    int iSetpoint = 0;
    int iPosition = 0;
    String cNome = "";
    int bAvancado = -1;
    int iIDComodo;
    String cDeviceStatus = "";

    protected Janela(Parcel in) {
        iIDJanela = in.readInt();
        iSetpoint = in.readInt();
        iPosition = in.readInt();
        bAvancado = in.readInt();
        cNome = in.readString();
        cDeviceStatus = in.readString();
    }

    public int getiIDComodo() {
        return iIDComodo;
    }

    public void setiIDComodo(int iIDComodo) {
        this.iIDComodo = iIDComodo;
    }

    public static final Creator<Janela> CREATOR = new Creator<Janela>() {
        @Override
        public Janela createFromParcel(Parcel in) {
            return new Janela(in);
        }

        @Override
        public Janela[] newArray(int size) {
            return new Janela[size];
        }
    };

    public Janela() {

    }

    public int getbAvancado() {
        return bAvancado;
    }

    public void setbAvancado(int bAvancado) {
        this.bAvancado = bAvancado;
    }

    public int getiIDJanela() {
        return iIDJanela;
    }

    public void setiIDJanela(int iIDJanela) {
        this.iIDJanela = iIDJanela;
    }

    public int getiSetpoint() {
        return iSetpoint;
    }

    public void setiSetpoint(int iSetpoint) {
        this.iSetpoint = iSetpoint;
    }

    public int getiPosition() {
        return iPosition;
    }

    public void setiPosition(int iPosition) {
        this.iPosition = iPosition;
    }

    public String getcNome() {
        return cNome;
    }

    public void setcNome(String cNome) {
        this.cNome = cNome;
    }

    public String getcDeviceStatus() {
        return cDeviceStatus;
    }

    public void setcDeviceStatus(String cDeviceStatus) {
        this.cDeviceStatus = cDeviceStatus;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(iIDJanela);
        parcel.writeInt(iSetpoint);
        parcel.writeInt(iPosition);
        parcel.writeInt(bAvancado);
        parcel.writeString(cNome);
        parcel.writeString(cDeviceStatus);
    }
}
