package br.com.cozinheirodelivery.domotica_new.Objects;

import java.util.HashMap;

/**
 * Created by Mendes on 11/03/2017.
 */

public class DeviceStatus {
    public static HashMap<Integer,String> map = new HashMap<Integer,String>(){{
        put(1,"Abrindo");
        put(2,"Fechando");
        put(3,"Obstruído");
        put(4,"Aberto");
        put(5,"Fechado");
        put(6,"Movendo");
        put(7,"Pronto");
    }};
    public static String getDeviceStatus(int i){
        return map.get(i);
    }
}
