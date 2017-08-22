package ar.com.netmefy.netmefy.services.api.entity;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by fiok on 21/08/2017.
 */

public class SaveToken {
    public int id_entidad ;
    public boolean es_cliente ;
    public boolean es_tecnico ;
    public String tokenid;

    public String getTokenid() {
        return tokenid;
    }

    public void setTokenid(String tokenid) {
        this.tokenid = tokenid;
    }

    public int getId_entidad() {
        return id_entidad;
    }

    public void setId_entidad(int id_entidad) {
        this.id_entidad = id_entidad;
    }

    public boolean isEs_cliente() {
        return es_cliente;
    }

    public void setEs_cliente(boolean es_cliente) {
        this.es_cliente = es_cliente;
    }

    public boolean isEs_tecnico() {
        return es_tecnico;
    }

    public void setEs_tecnico(boolean es_tecnico) {
        this.es_tecnico = es_tecnico;
    }

    public Map<String, String> toMap() throws IllegalAccessException {
        Map<String, String> map = new HashMap<String, String>();

        Object someObject = this;
        for (Field field : someObject.getClass().getDeclaredFields()) {
            field.setAccessible(true); // You might want to set modifier to public first.
            Object value = field.get(someObject);
            if (value != null) {
                System.out.println(field.getName() + "=" + value);
            }
            if(value !=null && !field.getName().equalsIgnoreCase("serialVersionUID"))
                map.put(field.getName(), value.toString());
        }
        return map;
    }
}
