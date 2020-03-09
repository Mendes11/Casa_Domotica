package br.com.cozinheirodelivery.domotica_new.Objects;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v4.os.AsyncTaskCompat;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import br.com.cozinheirodelivery.domotica_new.Database.AsyncRequest;
import br.com.cozinheirodelivery.domotica_new.Database.AsyncRequestObject;

/**
 * Created by Mendes on 11/03/2017.
 */

public class EntradaController extends Entrada {
    Context c;
    int iIDUsuario;
    AsyncRequest mAsync;
    boolean isTaskRunning = false;
    ProgressDialog d;
    onEntradaBDResponse mListener;
    public interface onEntradaBDResponse{
        void onCheckResponse();
        void onPortaJobResponse(boolean isSuccess);
        void onPortaoJobResponse(boolean isSuccess);
        void onMigueJobresponse(boolean isSuccess);
    }

    public EntradaController(Context c, int iIDUsuario, final onEntradaBDResponse mListener){
        this.c = c;
        this.iIDUsuario = iIDUsuario;
        this.mListener = mListener;
        oPorta = new PortaController(c, iIDUsuario, new PortaController.onBDResponse() {
            @Override
            public void onSendJobResponse(boolean success) {
                mListener.onPortaJobResponse(success);
            }
        });
        oPortao = new PortaoController(c, iIDUsuario, new PortaoController.onBDResponse() {
            @Override
            public void onSendJobResponse(boolean success) {
                mListener.onPortaoJobResponse(success);
            }
        });
    }
    public void checkBD(boolean isNew){
        this.mAsync = new AsyncRequest();
        isTaskRunning = true;
        AsyncRequestObject obj = new AsyncRequestObject();
        obj.setMethod("GET");
        obj.setPhpAddress("/checkEntrada.php");
        if(isNew) {
            d = new ProgressDialog(c);
            d.setTitle("Carregando Dados");
            d.setMessage("Aguarde...");
            d.show();
        }
        obj.setiIDUsuario(iIDUsuario);
        obj.setJsonObject(getJsonObject());
        Log.d("JSON_OBJECT",getJsonObject().toString());
        obj.setRunOnce(false);
        obj.setRunBackGround(true);
        mAsync.setData(c, obj, new AsyncRequest.OnAsyncRequestComplete() {
            @Override
            public void asyncResponse(AsyncRequestObject response) {
                trataDados(response);
                response.setJsonObject(getJsonObject());
                if(d.isShowing()){d.dismiss();}
            }

            @Override
            public void backgroundAsyncSync(AsyncRequest asyncRequest) {
                mAsync = asyncRequest;
            }
        });
        AsyncTaskCompat.executeParallel(mAsync);
    }
    public void trataDados(AsyncRequestObject obj){
        if(obj.isbSuccess()) {
            try {
                JSONObject c = obj.getJsonResult();
                if(c.has("Porta")) {
                    JSONArray aux = c.getJSONArray("Porta");
                    oPorta.trataDados(aux.getJSONObject(0));
                }
                if(c.has("Portao")){
                    JSONArray arr = c.getJSONArray("Portao");
                    JSONObject oJson = arr.getJSONObject(0);
                    oPortao.trataDados(oJson);
                }
                if(c.has("bMigue")){
                    setMigue(c.getInt("bMigue"));
                }
                mListener.onCheckResponse();
            } catch (JSONException e) {
                Toast.makeText(c, "Erro ao tratar Dados em EntradaController", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }else{
            if(obj.getCodError() != 6) { // erro 6 = FileNotFound provavelmente provindo de um timeout.
                Toast.makeText(c, obj.getcError(), Toast.LENGTH_SHORT).show();
            }
        }

    }
    // Transforma o ObjetoSala em JsonObject
    private JSONObject getJsonObject() {
        JSONObject c = new JSONObject();
        try {
            c.put("Porta",oPorta.getJsonObject());
            c.put("Portao",oPortao.getJsonObject());
            c.put("bMigue",getMigue());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return c;
    }
    public void cancelRunningTasks(){
        if(!mAsync.isCancelled()) {
            mAsync.cancel(true);
        }
        while(!mAsync.isCancelled()) {
            isTaskRunning = false;
        }
    }
    public boolean isTaskRunning(){
        return isTaskRunning;
    }
    public void sendMigueJob(){
        JSONObject json = new JSONObject();
        try {
            json.put("bMigue", getMigue());
            json.put("iIDUsuario",iIDUsuario);
            AsyncRequestObject obj = new AsyncRequestObject();
            obj.setMethod("POST");
            obj.setPhpAddress("/postMigue.php");
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
                        mListener.onMigueJobresponse(false);
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
}
