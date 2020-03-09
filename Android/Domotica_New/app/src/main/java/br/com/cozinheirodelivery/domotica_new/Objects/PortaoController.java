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

public class PortaoController extends Portao {
    Context c;
    int iIDUsuario;
    onBDResponse bdResponse;
    public interface onBDResponse{
        void onSendJobResponse(boolean success);
    }

    public PortaoController(Context c, int iIDUsuario, onBDResponse bdResponse){
        super();
        this.c = c;
        this.bdResponse = bdResponse;
        this.iIDUsuario = iIDUsuario;
    }
    public void sendJob(){
        JSONObject json = new JSONObject();
        try {
            json.put("bOpen", getbOpen());
            json.put("iIDPortao",getiIDPortao());
            json.put("fAutoCloseTime",getfTimeAutoClose());
            json.put("iIDUsuario",iIDUsuario);
            Toast.makeText(c, json.toString(), Toast.LENGTH_SHORT).show();
            AsyncRequestObject obj = new AsyncRequestObject();
            obj.setMethod("POST");
            obj.setPhpAddress("/postPortao.php");
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
            e.printStackTrace();
        }
    }
    public void trataDados(JSONObject c){
        try {
            setiIDPortao(c.getInt("iIDPortao"));
            setbOpen(c.getInt("bOpen"));
            setbPos(c.getInt("bPos"));
            setcNome(c.getString("cNome"));
            setfTimeAutoClose(c.getDouble("fAutoCloseTime"));
            setbEnabled(c.getInt("bEnabled"));
            setiIDDeviceStatus(c.getInt("iIDDeviceStatus"));
            //setiIDComodo(c.getInt("iIDComodo"));
            //bdResponse.onCheckBDResponse(); chamado agora no SalaController
        } catch (JSONException e) {
            Toast.makeText(this.c, "Erro ao tratar Dados em PortaoController", Toast.LENGTH_SHORT).show();
        }
    }
    public JSONObject getJsonObject() {
        JSONObject c = new JSONObject();
        try {
            c.put("iIDPortao",getiIDPortao());
            c.put("bOpen", getbOpen());
            c.put("bEnabled",getbEnabled());
            c.put("bPos",getbPos());
            c.put("fAutoCloseTime", (double)getfTimeAutoClose());
            c.put("iIDDeviceStatus", getiIDDeviceStatus());
            c.put("cNome",getcNome());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return c;
    }

}
