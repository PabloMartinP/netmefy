package ar.com.netmefy.netmefy.services;



import android.app.Activity;
import android.app.ProgressDialog;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import ar.com.netmefy.netmefy.MainActivity;
import ar.com.netmefy.netmefy.router.Device;


/**
 * Created by fiok on 16/07/2017.
 */

public class Utils {

    public static Map<String, String> toMap(Object object) throws IllegalAccessException {
        Map<String, String> map = new HashMap<String, String>();

        Object someObject = object;
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

    public static ProgressDialog getProgressBar(Activity activity, String message){
        ProgressDialog progressBar = new ProgressDialog(activity);
        progressBar.setMessage(message);
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setCancelable(false);
        return progressBar;
    }

    public static String getTextBetween(String text, String textBefore, String textAfter, String textOnError){
        int find = text.indexOf(textBefore );
        if(find == -1) return textOnError;

        int indexTextBegin = text.indexOf(textBefore) + textBefore.length();
        String result;
        if(indexTextBegin >0){
            String textWithoutBegin = text.substring(indexTextBegin );
            result= textWithoutBegin.substring(0, textWithoutBegin.indexOf(textAfter));
            return result;
        }else{
            return textOnError;
        }

    }

    public  static List<String> getListDevicesString(List<Device> listDevices){
        List<String> list = new ArrayList<String>();


        for (final Device device: listDevices) {
            String deviceString = "Nombre: " + device.getName() + ", Ip: " + device.getIp() + ", Mac: " + device.getMac();
            list.add(deviceString);
        }
        return list;
    }


    public static boolean isNumeric(String str)
    {
        try
        {
            double d = Double.parseDouble(str);
        }
        catch(NumberFormatException nfe)
        {
            return false;
        }
        return true;
    }



}
