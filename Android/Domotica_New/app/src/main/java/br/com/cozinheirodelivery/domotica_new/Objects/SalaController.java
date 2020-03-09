package br.com.cozinheirodelivery.domotica_new.Objects;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.os.AsyncTaskCompat;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import br.com.cozinheirodelivery.domotica_new.Database.AsyncRequest;
import br.com.cozinheirodelivery.domotica_new.Database.AsyncRequestObject;

/**
 * Created by Mendes on 09/03/2017.
 */

public class SalaController extends Sala {
    int iIDUsuario;
    Context c;
    onSalaBDResponse mListener;
    AsyncTaskCompat mCheckTask;
    AsyncRequest mAsync;
    ProgressDialog d;
    boolean isTaskRunning = false;
    public interface onSalaBDResponse{
        void onCheckResponse();
        void onLuminosidadeJobResponse(boolean isSuccess);
        void onJanelaJobResponse(boolean isSuccess);
        void onTomadaJobResponse(TomadaController t,boolean isSuccess);

    }
    public SalaController(Context c, int iIDUsuario, final onSalaBDResponse mListener){
        super();
        this.iIDUsuario = iIDUsuario;
        this.c = c;
        this.mListener = mListener;
        oJanela = new JanelaController(c, iIDUsuario, new JanelaController.onBDResponse() {
            @Override
            public void onSendJobResponse(boolean success) {
                mListener.onJanelaJobResponse(success);
            }
        });
        oJanela.setiIDComodo(getiIDComodo());
        // Migué para inicializar tudo.... Eu realmente não to com saco pra criar todos os métodos php...
        tomadaList = new ArrayList<>();
        for(int i = 0; i< 3;i++){
            tomadaList.add(newTomadaController());
        }
        oLuminosidade = new LuminosidadeController(c, iIDUsuario, new LuminosidadeController.onBDResponse() {
            @Override
            public void onSendJobResponse(boolean success) {
                mListener.onLuminosidadeJobResponse(success);
            }
        });
        oLuminosidade.setiIDComodo(getiIDComodo());
    }

    public void checkBD(boolean isNew){
        this.mAsync = new AsyncRequest();
        isTaskRunning = true;
        AsyncRequestObject obj = new AsyncRequestObject();
        obj.setMethod("GET");
        obj.setPhpAddress("/checkSala.php");
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
        mCheckTask.executeParallel(mAsync);
    }

    public void trataDados(AsyncRequestObject obj){
        if(obj.isbSuccess()) {
            try {
                JSONObject c = obj.getJsonResult();
                if(c.has("Janela")) {
                    JSONArray aux = c.getJSONArray("Janela");
                    oJanela.trataDados(aux.getJSONObject(0)); // Já que não tratei como lista aqui, faz assim...
                }
                if(c.has("Tomadas")){
                    JSONArray arr = c.getJSONArray("Tomadas");
                    int size = arr.length();
                    for(int i = 0; i< size; i++){
                        if(tomadaList.size() > i){
                            tomadaList.get(i).trataDados(arr.getJSONObject(i));
                        }else{
                            tomadaList.add(newTomadaController());
                            tomadaList.get(i).trataDados(arr.getJSONObject(i));
                        }
                    }
                }
                if(c.has("Luminosidade")){
                    JSONArray arr = c.getJSONArray("Luminosidade");
                    oLuminosidade.trataDados(arr.getJSONObject(0));
                }
                if(c.has("Sala")){
                    JSONArray arr = c.getJSONArray("Sala");
                    JSONObject oJson = arr.getJSONObject(0);
                    setiUserCont(oJson.getInt("iUserCont"));
                }
                mListener.onCheckResponse();
            } catch (JSONException e) {
                Toast.makeText(c, "Erro ao tratar Dados em SalaController", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }else{
            if(obj.getCodError() != 6) { // erro 6 = FileNotFound provavelmente provindo de um timeout.
                Toast.makeText(c, obj.getcError(), Toast.LENGTH_SHORT).show();
            }
        }

    }

    private TomadaController newTomadaController(){
        TomadaController t = new TomadaController(c, iIDUsuario, new TomadaController.onBDResponse() {
            @Override
            public void onSendJobResponse(TomadaController t,boolean success) {
                mListener.onTomadaJobResponse(t,success);
            }
        });
        t.setiIDComodo(getiIDComodo());
        return t;
    }
    // Transforma o ObjetoSala em JsonObject
    private JSONObject getJsonObject() {
        JSONObject c = new JSONObject();
        try {
            JSONObject sala = new JSONObject();
            sala.put("iUserCont",getiUserCont());
            c.put("Sala",sala);
            c.put("Janela",oJanela.getJsonObject());
            c.put("Luminosidade",oLuminosidade.getJsonObject());
            // Inserindo o array de tomadas
            JSONArray mArray = new JSONArray();
            for(TomadaController obj : tomadaList){
                mArray.put(obj.getJsonObject());
            }
            c.put("Tomadas",mArray);
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
}
