package br.com.cozinheirodelivery.domotica_new.Database;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class Post {
	String sql = "";
    String[] sqlArray;
    String[][] relacao;
    String dados[][];
	int codigo = -1;
	boolean buscaCodigo = false;
	int sucesso = 0;
	int c = 1;
	AsyncRequestObject asyncObj;
	//private static String url = "http://www.acervoindustrial.com.br/teste/post.php";
    //static final String url = "http://ocozinheirodelivery.no-ip.org:2020/Cozinheiro/post.php";
	private static String url = Addresses.getServerUrl();
	//private static String url = "http://192.168.1.43:80/Cozinheiro/post.php";
	JSONParser jsonParser = new JSONParser();

	public Post() {

	}

	public void cadastrar(AsyncRequestObject obj){
		this.asyncObj = obj;
		comunicar();
	}
	// funcao que faz a comunicacao pada cadastro no servidor
	public void comunicar() {
		HashMap<String, String> params = new HashMap<>();
		params.put("iIDUsuario",""+asyncObj.getiIDUsuario());
		if(asyncObj.getJsonObject() != null) {
			params.put("json_data", asyncObj.getJsonObject().toString());
		}
		JSONObject json = null;
		try{
			json = jsonParser.makeHttpRequest(url+asyncObj.getPhpAddress(), "POST", params);
		}catch(Exception e){
			json = null;
		}
		//Log.d("Analisar isso aqui : Create Response", json.toString());

		try {
			asyncObj.setbSuccess((json.getBoolean("success")));
			if(json.has("data")) {
				asyncObj.setJsonResult(json.getJSONObject("data"));
			}
			asyncObj.setCodError(json.getInt("errorCode"));
			asyncObj.setcError(json.getString("errorMessage"));

		} catch (JSONException e) {
			asyncObj.setbSuccess(false);
			e.printStackTrace();
			asyncObj.setcError(e.getMessage());
            dados = null;
		} catch (Exception e){
			asyncObj.setbSuccess(false);

            e.printStackTrace();
            dados = null;
        }
	}
}