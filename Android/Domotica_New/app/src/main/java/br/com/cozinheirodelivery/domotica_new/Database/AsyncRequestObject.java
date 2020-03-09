package br.com.cozinheirodelivery.domotica_new.Database;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Mendes on 08/01/2016.
 */
public class AsyncRequestObject {
    String[][] dados;
    String sql;
    String[] sqlArray;
    String[][] relacao;
    String[] nomColumns;
    String identifier;
    String method;
    String codReturned;
    String cError;
    int codError;
    boolean getCodigo = false;
    boolean bSuccess = false;
    JSONObject jsonResult;
    JSONObject jsonObject;
    String phpAddress = "";
    int iIDUsuario;
    boolean runBackGround = false;
    boolean runOnce = false;

    public AsyncRequestObject(){

    }

    public boolean isRunOnce() {
        return runOnce;
    }

    public void setRunOnce(boolean runOnce) {
        this.runOnce = runOnce;
    }

    public boolean isRunBackGround() {
        return runBackGround;
    }

    public void setRunBackGround(boolean runBackGround) {
        this.runBackGround = runBackGround;
    }

    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public int getiIDUsuario() {
        return iIDUsuario;
    }

    public void setiIDUsuario(int iIDUsuario) {
        this.iIDUsuario = iIDUsuario;
    }

    public String getPhpAddress() {
        return phpAddress;
    }

    public void setPhpAddress(String phpAddress) {
        this.phpAddress = phpAddress;
    }

    public JSONObject getJsonResult() {
        return jsonResult;
    }

    public void setJsonResult(JSONObject jsonResult) {
        this.jsonResult = jsonResult;
    }

    public int getCodError() {
        return codError;
    }

    public void setCodError(int codError) {
        this.codError = codError;
    }

    public String getCodReturned() {
        return codReturned;
    }

    public void setCodReturned(String codReturned) {
        this.codReturned = codReturned;
    }

    public boolean isbSuccess() {
        return bSuccess;
    }

    public void setbSuccess(boolean bSuccess) {
        this.bSuccess = bSuccess;
    }

    public String getcError() {
        return cError;
    }

    public void setcError(String cError) {
        this.cError = cError;
    }

    public String[] getSqlArray() {
        return sqlArray;
    }

    public void setSqlArray(String[] sqlArray) {
        this.sqlArray = sqlArray;
    }

    public String[][] getRelacao() {
        return relacao;
    }

    public void setRelacao(String[][] relacao) {
        this.relacao = relacao;
    }

    public boolean isGetCodigo() {
        return getCodigo;
    }

    public void setGetCodigo(boolean getCodigo) {
        this.getCodigo = getCodigo;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String[][] getDados() {
        return dados;
    }

    public void setDados(String[][] dados) {
        this.dados = dados;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public String[] getNomColumns() {
        return nomColumns;
    }

    public void setNomColumns(String[] nomColumns) {
        this.nomColumns = nomColumns;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
}
