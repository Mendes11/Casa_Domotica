package br.com.cozinheirodelivery.domotica_new.Objects;

/**
 * Created by Mendes on 11/03/2017.
 */

public class Entrada {
    PortaController oPorta;
    PortaoController oPortao;
    int migue;
    public PortaController getoPorta() {
        return oPorta;
    }

    public int getMigue() {
        return migue;
    }

    public void setMigue(int migue) {
        this.migue = migue;
    }

    public void setoPorta(PortaController oPorta) {
        this.oPorta = oPorta;
    }

    public PortaoController getoPortao() {
        return oPortao;
    }

    public void setoPortao(PortaoController oPortao) {
        this.oPortao = oPortao;
    }
}
