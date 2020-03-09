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

public class LavanderiaController extends Lavanderia {
    Context c;
    onLavanderiaBDResponse mListener;
    int iIDUsuario;
    AsyncRequest mAsync;
    boolean isTaskRunning = false;
    ProgressDialog d;
    public LavanderiaController(Context c, int iIDUsuario, final onLavanderiaBDResponse mListener){
        this.c = c;
        this.iIDUsuario = iIDUsuario;
        this.mListener = mListener;
        oTeto = new TetoController(c, iIDUsuario, new TetoController.onBDResponse() {
            @Override
            public void onSendJobResponse(boolean success) {
                mListener.onTetoJobResponse(success);
            }
        });
        oTeto.setiIDComodo(getiIDLavanderia());
    }
    public interface onLavanderiaBDResponse{
        void onCheckResponse();
        void onTetoJobResponse(boolean isSuccess);
    }

    public void checkBD(boolean isNew){
        this.mAsync = new AsyncRequest();
        isTaskRunning = true;
        AsyncRequestObject obj = new AsyncRequestObject();
        obj.setMethod("GET");
        obj.setPhpAddress("/checkLavanderia.php");
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
                if(c.has("Lavanderia")){
                    JSONArray arr = c.getJSONArray("Lavanderia");
                    JSONObject oJson = arr.getJSONObject(0);
                    setiUserCont(oJson.getInt("iUserCont"));
                }
                mListener.onCheckResponse();
            } catch (JSONException e) {
                Toast.makeText(c, "Erro ao tratar Dados em LavanderiaController", Toast.LENGTH_SHORT).show();
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
            lavanderia.put("iUserCont",getiUserCont());
            c.put("Lavanderia",lavanderia);
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
