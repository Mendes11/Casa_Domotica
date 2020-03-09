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

public class PortaController extends Porta {
    Context c;
    int iIDUsuario;
    onBDResponse bdResponse;
    public interface onBDResponse{
        void onSendJobResponse(boolean success);
    }

    public PortaController(Context c, int iIDUsuario, onBDResponse bdResponse){
        super();
        this.c = c;
        this.bdResponse = bdResponse;
        this.iIDUsuario = iIDUsuario;
    }
    public void sendJob(){
        JSONObject json = new JSONObject();
        try {
            json.put("bOpen", getbOpen());
            json.put("iIDPorta",getiIDPorta());
            json.put("fAutoCloseTime",getfTimeAutoClose());
            json.put("iIDUsuario",iIDUsuario);
            AsyncRequestObject obj = new AsyncRequestObject();
            obj.setMethod("POST");
            obj.setPhpAddress("/postPorta.php");
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
            setiIDPorta(c.getInt("iIDPorta"));
            setbOpen(c.getInt("bOpen"));
            setcNome(c.getString("cNome"));
            setfTimeAutoClose(c.getDouble("fAutoCloseTime"));
            setbHabilitado(c.getInt("bHabilitado"));
            setiIDDeviceStatus(c.getInt("iIDDeviceStatus"));
            //setiIDComodo(c.getInt("iIDComodo"));
            //bdResponse.onCheckBDResponse(); chamado agora no SalaController
        } catch (JSONException e) {
            Toast.makeText(this.c, "Erro ao tratar Dados em PortaController", Toast.LENGTH_SHORT).show();
        }
    }
    public JSONObject getJsonObject() {
        JSONObject c = new JSONObject();
        try {
            c.put("iIDPorta",getiIDPorta());
            c.put("bOpen", getbOpen());
            c.put("bHabilitado",getbHabilitado());
            c.put("fAutoCloseTime", getfTimeAutoClose());
            c.put("iIDDeviceStatus", getiIDDeviceStatus());
            c.put("cNome",getcNome());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return c;
    }

}
