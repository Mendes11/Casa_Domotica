package br.com.cozinheirodelivery.domotica_new.Objects;

import android.content.Context;
import android.os.Parcel;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import br.com.cozinheirodelivery.domotica_new.Database.AsyncRequest;
import br.com.cozinheirodelivery.domotica_new.Database.AsyncRequestObject;

/**
 * Created by Mendes on 10/03/2017.
 */

public class LuminosidadeController extends Luminosidade {
    onBDResponse bdResponse;
    Context c;
    int iIDUsuario;
    public interface onBDResponse{
        void onSendJobResponse(boolean success);
    }

    protected LuminosidadeController(Parcel in) {
        super(in);
    }

    public LuminosidadeController(Context c, int iIDUsuario, onBDResponse bdResponse){
        super();
        this.c = c;
        this.bdResponse = bdResponse;
        this.iIDUsuario = iIDUsuario;
    }
    public void sendJob(){
        JSONObject json = new JSONObject();
        try {
            json.put("iSetpoint", getiSetpoint());
            json.put("bManualOn", getbManualOn());
            json.put("bControle",getbControle());
            json.put("iIDLuminosidade",getiIDLuminosidade());
            json.put("iIDUsuario",iIDUsuario);
            AsyncRequestObject obj = new AsyncRequestObject();
            obj.setMethod("POST");
            obj.setPhpAddress("/postLuminosidade.php");
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
            setiIDLuminosidade(c.getInt("iIDLuminosidade"));
            setiSetpoint(c.getInt("iSetpoint"));
            setiSensor(c.getInt("iSensor"));
            setbControle(c.getInt("bControle"));
            setbManualOn(c.getInt("bManualOn"));
            setiIDComodo(c.getInt("iIDComodo"));
            //bdResponse.onCheckBDResponse(); chamado agora no SalaController
        } catch (JSONException e) {
            Toast.makeText(this.c, "Erro ao tratar Dados em LuminosidadeController", Toast.LENGTH_SHORT).show();
        }
    }
    public JSONObject getJsonObject() {
        JSONObject c = new JSONObject();
        try {
            c.put("iIDLuminosidade",getiIDLuminosidade());
            c.put("iSetpoint", getiSetpoint());
            c.put("iSensor",getiSensor() );
            c.put("bControle", getbControle());
            c.put("bManualOn", getbManualOn());
            c.put("iIDComodo",getiIDComodo());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return c;
    }
}
