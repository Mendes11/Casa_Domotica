package br.com.cozinheirodelivery.domotica_new.Database;

/**
 * Created by Mendes on 16/07/2016.
 */
public class Addresses {
    //private static final String serverUrl = "http://sujeirapi.ddns.net:2040/Domotica";
    private static final String serverUrl = "http://192.168.1.103/DomoticaBKP";
    private static final String postUrl = serverUrl+"/post.php";
    private static final String getUrl = serverUrl+"/get.php";
    private static final String uploadImgUrl = serverUrl+"/fileUpload.php";

    public static String getServerUrl(){ return serverUrl; }

    public static String getPostUrl() {
        return postUrl;
    }

    public static String getGetUrl() {
        return getUrl;
    }

    public static String getUploadImgUrl() {
        return uploadImgUrl;
    }
}
