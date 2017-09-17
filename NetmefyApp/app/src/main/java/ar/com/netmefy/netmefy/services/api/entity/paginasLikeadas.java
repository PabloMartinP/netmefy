package ar.com.netmefy.netmefy.services.api.entity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by fiok on 17/09/2017.
 */

public class paginasLikeadas {
    //var data = { cliente_sk: 1, usuario_sk: 1, paginas: ['unaFanPage();', 'OtraFanPage();;'] };
    public int cliente_sk;
    public int usuario_sk;
    public ArrayList<String> paginas;
    //public String[] paginas;

    public Map<String, String> toMap() throws IllegalAccessException {
        Map<String, String> map = new HashMap<String, String>();

        Object someObject = this;
        for (Field field : someObject.getClass().getDeclaredFields()) {
            field.setAccessible(true); // You might want to set modifier to public first.
            Object value = field.get(someObject);
            //if (value != null) {
//                System.out.println(field.getName() + "=" + value);
//            }
            if(value !=null && !field.getName().equalsIgnoreCase("serialVersionUID"))
                map.put(field.getName(), value.toString());
        }
        return map;
    }
}
