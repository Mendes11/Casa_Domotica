package br.com.cozinheirodelivery.domotica_new.Objects;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Mendes on 02/03/2017.
 */

public class Luminosidade implements Parcelable{
    int iIDLuminosidade,iSetpoint,iSensor;
    int bControle,bManualOn;
    int iIDComodo;
    protected Luminosidade(Parcel in) {
        iIDLuminosidade = in.readInt();
        iSetpoint = in.readInt();
        iSensor = in.readInt();
    }
    public Luminosidade(){

    }
    public static final Creator<Luminosidade> CREATOR = new Creator<Luminosidade>() {
        @Override
        public Luminosidade createFromParcel(Parcel in) {
            return new Luminosidade(in);
        }

        @Override
        public Luminosidade[] newArray(int size) {
            return new Luminosidade[size];
        }
    };

    public int getiIDComodo() {
        return iIDComodo;
    }

    public void setiIDComodo(int iIDComodo) {
        this.iIDComodo = iIDComodo;
    }

    public int getiIDLuminosidade() {
        return iIDLuminosidade;
    }

    public void setiIDLuminosidade(int iIDLuminosidade) {
        this.iIDLuminosidade = iIDLuminosidade;
    }

    public int getiSetpoint() {
        return iSetpoint;
    }

    public void setiSetpoint(int iSetpoint) {
        this.iSetpoint = iSetpoint;
    }

    public int getiSensor() {
        return iSensor;
    }

    public void setiSensor(int iSensor) {
        this.iSensor = iSensor;
    }

    public int getbControle() {
        return bControle;
    }

    public void setbControle(int bControle) {
        this.bControle = bControle;
    }

    public int getbManualOn() {
        return bManualOn;
    }

    public void setbManualOn(int bManualOn) {
        this.bManualOn = bManualOn;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(iIDLuminosidade);
        parcel.writeInt(iSetpoint);
        parcel.writeInt(iSensor);
    }
}
