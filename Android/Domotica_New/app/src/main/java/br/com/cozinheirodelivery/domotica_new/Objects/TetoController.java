package br.com.cozinheirodelivery.domotica_new.Objects;

import android.content.Context;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import br.com.cozinheirodelivery.domotica_new.Database.AsyncRequest;
import br.com.cozinheirodelivery.domotica_new.Database.AsyncRequestObject;

/**
 * Created by Mendes on 11/03/2017.
 */

public class TetoController extends Teto {
    onBDResponse bdResponse;
    Context c;
    int iIDUsuario;
    public interface onBDResponse{
        void onSendJobResponse(boolean success);
    }
    public TetoController(Context c, int iIDUsuario, onBDResponse bdResponse){
        super();
        this.c = c;
        this.bdResponse = bdResponse;
        this.iIDUsuario = iIDUsuario;
    }

    public void sendJob(){
        JSONObject json = new JSONObject();
        try {
            json.put("bOpen", getiSetpoint());
            json.put("bAvancado",getbAvancado());
            json.put("iIDTeto",getiIDTeto());
            json.put("iIDUsuario",iIDUsuario);
            AsyncRequestObject obj = new AsyncRequestObject();
            obj.setMethod("POST");
            obj.setPhpAddress("/postTeto.php");
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
    public void trataDados(JSONObject c){
        try {
            setiIDTeto(c.getInt("iIDTeto"));
            setiSetpoint(c.getInt("iSetpoint"));
            setiPos(c.getInt("iPos"));
            setbAvancado(c.getInt("bAvancado"));
            setiIDDeviceStatus(c.getInt("iIDDeviceStatus"));
            setiIDComodo(c.getInt("iIDComodo"));
            //bdResponse.onCheckBDResponse(); chamado agora no SalaController
        } catch (JSONException e) {
            Toast.makeText(this.c, "Erro ao tratar Dados em TetoController", Toast.LENGTH_SHORT).show();
        }
    }
    public JSONObject getJsonObject() {
        JSONObject c = new JSONObject();
        try {
            c.put("iIDTeto",getiIDTeto());
            c.put("iSetpoint", getiSetpoint());
            c.put("iPos",getiPos());
            c.put("bAvancado", getbAvancado());
            c.put("iIDDeviceStatus", getiIDDeviceStatus());
            c.put("iIDComodo",getiIDComodo());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return c;
    }
}
