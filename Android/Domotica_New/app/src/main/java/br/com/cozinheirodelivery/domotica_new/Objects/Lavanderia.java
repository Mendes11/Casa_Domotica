package br.com.cozinheirodelivery.domotica_new.Objects;

/**
 * Created by Mendes on 11/03/2017.
 */

public class Lavanderia {
    int iIDLavanderia = 2;
    int iUserCont = -1;
    String cNome;
    TetoController oTeto;

    public TetoController getoTeto() {
        return oTeto;
    }

    public void setoTeto(TetoController oTeto) {
        this.oTeto = oTeto;
    }

    public int getiIDLavanderia() {
        return iIDLavanderia;
    }

    public void setiIDLavanderia(int iIDLavanderia) {
        this.iIDLavanderia = iIDLavanderia;
    }

    public int getiUserCont() {
        return iUserCont;
    }

    public void setiUserCont(int iUserCont) {
        this.iUserCont = iUserCont;
    }

    public String getcNome() {
        return cNome;
    }

    public void setcNome(String cNome) {
        this.cNome = cNome;
    }
}
