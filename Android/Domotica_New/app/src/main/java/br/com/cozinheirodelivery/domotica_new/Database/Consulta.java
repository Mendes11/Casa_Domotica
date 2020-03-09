package br.com.cozinheirodelivery.domotica_new.Database;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class Consulta {
	// Variaveis necessarias
	//private static final String url = "http://www.acervoindustrial.com.br/teste/get.php";
	//private static final String url = "http://ocozinheirodelivery.no-ip.org:2020/Cozinheiro/get.php";
	private static String url = Addresses.getServerUrl();
	//private static String url = "http://192.168.1.43:80/Cozinheiro/get.php";
	String sql;
	int numColunas;
	String[] nomDados;
	String where = null;
	JSONParser jsonParser = new JSONParser();
	public int success = 0;
	public int linhas;
	JSONArray result = null;
	String[][] dados;
	AsyncRequestObject asyncObj;
	// Construtor
	public Consulta(){
		
	}
	
	//Metodo chamado pela classe que instanciou
	
	public void consultar(AsyncRequestObject asyncObj){
		this.asyncObj = asyncObj;
		comunicar();
	}
	public String[][] consultar(String address,String where){
		this.where = where;
		comunicar();
		return dados;
	}
	// funcao que faz toda a comunicacao e armazenamento de dados
	public AsyncRequestObject comunicar() {
		// Building Parameters
		HashMap<String,String> params = new HashMap<>();
		if(where != null){
			params.put("args",where);
		}
		params.put("iIDUsuario",""+asyncObj.getiIDUsuario());
		if(asyncObj.getJsonObject() != null) {
			params.put("json_data", asyncObj.getJsonObject().toString());
		}
		JSONObject json = null;
		try {
			String auxurl = url+asyncObj.getPhpAddress();
			json = jsonParser.makeHttpRequest(auxurl, "GET", params);
			if(json != null) {
				asyncObj.setbSuccess((json.getBoolean("success")));
				if(!json.isNull("data")) {
					asyncObj.setJsonResult(json.getJSONObject("data"));
				}else{
					asyncObj.setJsonResult(null);
				}
				asyncObj.setCodError(json.getInt("errorCode"));
				asyncObj.setcError(json.getString("errorMessage"));
			}else{
				asyncObj.setbSuccess(false);
				asyncObj.setCodError(6);
				asyncObj.setcError("FileNotFound");
			}
			return asyncObj;
		}catch(Exception e){
            asyncObj.setbSuccess(false);
			asyncObj.setcError("Erro ao realizar comunicação com servidor;");
			e.printStackTrace();
		}
		return asyncObj;
	}

}
