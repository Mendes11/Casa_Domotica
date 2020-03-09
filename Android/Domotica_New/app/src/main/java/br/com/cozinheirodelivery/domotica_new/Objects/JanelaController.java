package br.com.cozinheirodelivery.domotica_new.Objects;

import android.content.Context;
import android.os.Parcel;
import android.support.v4.os.AsyncTaskCompat;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import br.com.cozinheirodelivery.domotica_new.Database.AsyncRequest;
import br.com.cozinheirodelivery.domotica_new.Database.AsyncRequestObject;

/**
 * Created by Mendes on 02/03/2017.
 */

public class JanelaController extends Janela {
    onBDResponse bdResponse;
    Context c;
    int iIDUsuario;
    public interface onBDResponse{
        void onSendJobResponse(boolean success);
    }

    public JanelaController(Context c, int iIDUsuario, onBDResponse bdResponse){
        super();
        this.c = c;
        this.bdResponse = bdResponse;
        this.iIDUsuario = iIDUsuario;
    }
    protected JanelaController(Parcel in) {
        super(in);
    }


    public void sendJob(int iSetpoint){
        JSONObject json = new JSONObject();
        try {
            json.put("iSetpoint", iSetpoint);
            json.put("bAvancado", 0);
            json.put("bNewAvancado",0);
            json.put("iIDJanela",1);
            json.put("iIDUsuario",iIDUsuario);
            AsyncRequestObject obj = new AsyncRequestObject();
            obj.setMethod("POST");
            obj.setPhpAddress("/postJanela.php");
            obj.setiIDUsuario(iIDUsuario);
            obj.setJsonObject(json);
            obj.setRunOnce(true);
            new AsyncRequest(c, obj, new AsyncRequest.OnAsyncRequestComplete() {
                @Override
                public void asyncResponse(AsyncRequestObject response) {
                    if(response.isbSuccess()){
                        Toast.makeText(c, "Tarefa concluída", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(c, "Erro ao processar tarefa. \n"+response.getcError(), Toast.LENGTH_SHORT).show();
                        bdResponse.onSendJobResponse(false);
                    }
                }

                @Override
                public void backgroundAsyncSync(AsyncRequest asyncRequest) {
                    // Nâo Usado para o job.
                }
            }).execute();
        }catch (JSONException e){

        }
    }
    public void closeTasks(){

    }
    public void trataDados(JSONObject c){
            try {
                iIDJanela = c.getInt("iIDJanela");
                cNome = c.getString("cNome");
                iSetpoint = c.getInt("iSetpoint");
                iPosition = c.getInt("iPos");
                bAvancado = c.getInt("bAvancado");
                iIDComodo = c.getInt("iIDComodo");
                cDeviceStatus = c.getString("cDeviceName");
                //bdResponse.onCheckBDResponse(); chamado agora no SalaController
            } catch (JSONException e) {
                Toast.makeText(this.c, "Erro ao tratar Dados em JanelaCOntroller", Toast.LENGTH_SHORT).show();
            }
    }
    public JSONObject getJsonObject() {
        JSONObject c = new JSONObject();
        try {
            c.put("iIDJanela", iIDJanela);
            c.put("cNome", cNome);
            c.put("iSetpoint", iSetpoint);
            c.put("iPos", iPosition);
            c.put("bAvancado", bAvancado);
            c.put("cDeviceName", cDeviceStatus);
            c.put("iIDComodo",iIDComodo);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return c;
    }
}
