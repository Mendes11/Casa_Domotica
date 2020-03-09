package br.com.cozinheirodelivery.domotica_new.Objects;

import java.util.List;

/**
 * Created by Mendes on 09/03/2017.
 */

public class Sala {
    int iIDComodo = 1;
    String cNome;
    int iUserCont;
    JanelaController oJanela;
    List<TomadaController> tomadaList;
    LuminosidadeController oLuminosidade;

    public Sala(){

    }

    public LuminosidadeController getoLuminosidade() {
        return oLuminosidade;
    }

    public void setoLuminosidade(LuminosidadeController oLuminosidade) {
        this.oLuminosidade = oLuminosidade;
    }

    public int getiIDComodo() {
        return iIDComodo;
    }

    public void setiIDComodo(int iIDComodo) {
        this.iIDComodo = iIDComodo;
    }

    public String getcNome() {
        return cNome;
    }

    public void setcNome(String cNome) {
        this.cNome = cNome;
    }

    public int getiUserCont() {
        return iUserCont;
    }

    public void setiUserCont(int iUserCont) {
        this.iUserCont = iUserCont;
    }

    public List<TomadaController> getTomadaList() {
        return tomadaList;
    }

    public void setTomadaList(List<TomadaController> tomadaList) {
        this.tomadaList = tomadaList;
    }

    public JanelaController getoJanela() {
        return oJanela;
    }

    public void setoJanela(JanelaController oJanela) {
        this.oJanela = oJanela;
    }
}
