package br.com.cozinheirodelivery.domotica_new.Objects;

import android.content.Context;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import br.com.cozinheirodelivery.domotica_new.Database.AsyncRequest;
import br.com.cozinheirodelivery.domotica_new.Database.AsyncRequestObject;

/**
 * Created by Mendes on 09/03/2017.
 */

public class TomadaController extends Tomada {
    onBDResponse mListener;
    Context c;
    int iIDUsuario;
    public interface onBDResponse{
        void onSendJobResponse(TomadaController t,boolean success);
    }
    public TomadaController(Context c, int iIDUsuario,onBDResponse mListener){
        super();
        this.c = c;
        this.iIDUsuario = iIDUsuario;
        this.mListener = mListener;
    }
    public void sendJob(){
            JSONObject json = new JSONObject();
            try {
                json.put("bStatus",getbStatus());
                json.put("iIDTomada",getiIDTomada());
                json.put("iIDUsuario",iIDUsuario);
                AsyncRequestObject obj = new AsyncRequestObject();
                obj.setMethod("POST");
                obj.setPhpAddress("/postTomada.php");
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
                            mListener.onSendJobResponse(TomadaController.this,false);
                        }
                    }

                    @Override
                    public void backgroundAsyncSync(AsyncRequest asyncRequest) {
                        // Não usado para o job.
                    }
                }).execute();
            }catch (JSONException e){

            }
        }
    public JSONObject getJsonObject() {
        JSONObject c = new JSONObject();
        try {
            c.put("iIDTomada", getiIDTomada());
            c.put("cNome", cNome);
            c.put("bStatus", getbStatus());
            c.put("iIDComodo",getiIDComodo());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return c;
    }
    public void trataDados(JSONObject obj){
        try{
            setiIDTomada(obj.getInt("iIDTomada"));
            setcNome(obj.getString("cNome"));
            setbStatus(obj.getInt("bStatus"));
            setiIDComodo(obj.getInt("iIDComodo"));
        }catch (Exception e){

        }

    }
}
