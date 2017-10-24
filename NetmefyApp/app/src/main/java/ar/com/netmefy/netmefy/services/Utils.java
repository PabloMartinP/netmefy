package ar.com.netmefy.netmefy.services;



import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

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
        Gson gson = new Gson();
        Object someObject = object;
        for (Field field : someObject.getClass().getDeclaredFields()) {
            field.setAccessible(true); // You might want to set modifier to public first.
            Object value = field.get(someObject);
            //if (value != null) {
//                System.out.println(field.getName() + "=" + value);
//            }
            if(value !=null && !field.getName().equalsIgnoreCase("serialVersionUID")){
                if(value.getClass().getName().contains("ArrayList")){
                    int i = 0;
                    ArrayList list = ((ArrayList) value);
                    //String valueList = Utils.toMap(list).toString();
                    ArrayList<String> valueList = new ArrayList<>();
                    for (Object o : list) {
                        /*try {
                            JSONArray array=new JSONArray(Utils.toMap(o).toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }*/
                        String json = gson.toJson(o);

                        valueList.add(json);
                    }
                    //JSONArray jsArray = new JSONArray(valueList);
                    //map.put(field.getName(), jsArray.toString());
                    map.put(field.getName(), valueList.toString());
                    //map.put(field.getName(), value.toString());
                }else{
                    map.put(field.getName(), value.toString());
                }

            }

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


    public static void newToast(Context context, String mensaje) {
        Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show();
    }

    public static int getResIdFromImageName(Activity activity, String drawbleName){
        int resID =activity.getResources().getIdentifier(drawbleName , "drawable", activity.getPackageName());
        return resID;
    }
}
