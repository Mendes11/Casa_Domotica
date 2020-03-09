package br.com.cozinheirodelivery.domotica_new.Database;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.os.AsyncTaskCompat;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Mendes on 04/01/2016.
 */
public class AsyncRequest extends AsyncTask<String, Integer, String> {
    OnAsyncRequestComplete caller;
    Context context;
    ProgressDialog pDialog;
    AsyncRequestObject obj;
    List<AsyncRequestObject> objList = null;

    // Interface to be implemented by calling activity
    public interface OnAsyncRequestComplete {
        public void asyncResponse(AsyncRequestObject response);
        void backgroundAsyncSync(AsyncRequest asyncRequest);
    }

    public AsyncRequest(Context c,AsyncRequestObject obj,  OnAsyncRequestComplete a) {
        caller = (OnAsyncRequestComplete) a;
        context = c;
        this.obj = obj;
    }

    public AsyncRequest(){

    }
    public void setData(Context c,AsyncRequestObject obj,  OnAsyncRequestComplete a){
        caller = (OnAsyncRequestComplete) a;
        context = c;
        this.obj = obj;
    }
    public void onPreExecute() {
        if(!obj.isRunBackGround()) {
            pDialog = new ProgressDialog(context);
            pDialog.setMessage("Carregando dados... Aguarde"); // typically you will define such
            // strings in a remote file.
            pDialog.show();
        }
    }

    @Override
    protected String doInBackground(String... params) {

            if (obj.getMethod() == "POST") {
                post(obj);
            }
            if (obj.getMethod() == "GET") {
                get(obj);
            }
        return null;
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        try {
            Log.d("Cancelado","Cancelado");
            this.finalize();
            caller = null;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    public void onPostExecute(String response) {
        if (pDialog != null && pDialog.isShowing()) {
            pDialog.dismiss();
        }
        caller.asyncResponse(obj);
        if(!obj.isRunOnce() && !isCancelled()) {
            AsyncRequest req = new AsyncRequest();
            caller.backgroundAsyncSync(req);
            req.setData(context, obj, caller);
            Log.d("JSON",obj.getJsonObject().toString());
            req.execute();
            /*try {
                this.cancel(true);
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }*/
        }
    }

    private void get(AsyncRequestObject asyncObj){
        Consulta c = new Consulta();
        c.consultar(asyncObj);
    }
    private void post(AsyncRequestObject obj){
        Post p = new Post();
        p.cadastrar(obj);
    }
    private String checkMySQLErrors(String errorCode){
        String errorMessage = "";
        try{
            int iError = Integer.parseInt(errorCode);
            switch (iError){
                case 1062:
                    errorMessage = "Atenção, cadastro já existente. Por favor, tente outro.";
                    break;
                default:
                    errorMessage = "Atenção, não foi possível realizar a ação, tente novamente.";
            }
        }catch (Exception e){
            errorMessage = errorCode+" - Erro desconhecido, contate o administrador.";
        }
        return errorMessage;
    }
}
