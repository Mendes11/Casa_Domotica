package br.com.cozinheirodelivery.domotica_new.Objects;

/**
 * Created by Mendes on 11/03/2017.
 */

public class Piscina {
    TetoController oTeto;
    LuminosidadeController oLuminosidade;
    int iIDPiscina = 3;
    public TetoController getoTeto() {
        return oTeto;
    }

    public void setoTeto(TetoController oTeto) {
        this.oTeto = oTeto;
    }

    public LuminosidadeController getoLuminosidade() {
        return oLuminosidade;
    }

    public void setoLuminosidade(LuminosidadeController oLuminosidade) {
        this.oLuminosidade = oLuminosidade;
    }

    public int getiIDPiscina() {
        return iIDPiscina;
    }

    public void setiIDPiscina(int iIDPiscina) {
        this.iIDPiscina = iIDPiscina;
    }
}
