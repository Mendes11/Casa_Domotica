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

public class PiscinaController extends Piscina {
    Context c;
    int iIDUsuario;
    onPiscinaBDResponse mListener;
    AsyncRequest mAsync;
    boolean isTaskRunning = false;
    ProgressDialog d;
    public interface onPiscinaBDResponse{
        void onCheckResponse();
        void onTetoJobResponse(boolean isSuccess);
        void onLuminosidadeJobResponse(boolean isSuccess);
    }

    public PiscinaController(Context c, int iIDUsuario, final onPiscinaBDResponse mListener){
        this.c = c;
        this.iIDUsuario = iIDUsuario;
        this.mListener = mListener;
        oTeto = new TetoController(c, iIDUsuario, new TetoController.onBDResponse() {
            @Override
            public void onSendJobResponse(boolean success) {
                mListener.onTetoJobResponse(success);
            }
        });
        oTeto.setiIDComodo(getiIDPiscina());
        oLuminosidade = new LuminosidadeController(c, iIDUsuario, new LuminosidadeController.onBDResponse() {
            @Override
            public void onSendJobResponse(boolean success) {
                mListener.onLuminosidadeJobResponse(success);
            }
        });
        oLuminosidade.setiIDComodo(getiIDPiscina());
    }
    public void checkBD(boolean isNew){
        this.mAsync = new AsyncRequest();
        isTaskRunning = true;
        AsyncRequestObject obj = new AsyncRequestObject();
        obj.setMethod("GET");
        obj.setPhpAddress("/checkPiscina.php");
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
                if(c.has("Teto")) {
                    JSONArray aux = c.getJSONArray("Teto");
                    oTeto.trataDados(aux.getJSONObject(0));
                }
                if(c.has("Luminosidade")){
                    JSONArray arr = c.getJSONArray("Luminosidade");
                    JSONObject oJson = arr.getJSONObject(0);
                    oLuminosidade.trataDados(oJson);
                }
                mListener.onCheckResponse();
            } catch (JSONException e) {
                Toast.makeText(c, "Erro ao tratar Dados em PiscinaController", Toast.LENGTH_SHORT).show();
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
            JSONObject lavanderia = new JSONObject();
            c.put("Luminosidade",oLuminosidade.getJsonObject());
            c.put("Teto",oTeto.getJsonObject());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return c;
    }
    public void cancelRunningTasks(){
        if(!mAsync.isCancelled()) {
            mAsync.cancel(true);
        }
        isTaskRunning = false;
    }
    public boolean isTaskRunning(){
        return isTaskRunning;
    }
}
