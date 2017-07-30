package ar.com.netmefy.netmefy.services;

import android.content.Context;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;

import com.android.volley.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import ar.com.netmefy.netmefy.router.Device;
import ar.com.netmefy.netmefy.router.RestartTry;

/**
 * Created by fiok on 16/07/2017.
 */

public class Utils {

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


    /*
    * intenta conectar a un ssid especifico,
    * si esta conectado a otra red la desconecta y se conecta a la ssid
    * Importante: El SSID ya tienen que estar agregado a la lista de redes del android
    * */
    public static boolean tryConnectToNetwork(String ssid, Context context){
        WifiManager wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
        for (WifiConfiguration network:wifiManager.getConfiguredNetworks()) {
            if (network.SSID.replace("\"", "").equalsIgnoreCase(ssid)) {
                wifiManager.enableNetwork(network.networkId, true);
                wifiManager.saveConfiguration();
                wifiManager.setWifiEnabled(true);
                return true;
            }
        }
        return false;
    }

    /*
    * trae el ssid de la red conectada actualmente
    * */
    public static String getWifiName(Context context) {
        try {
            WifiManager manager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            if (manager.isWifiEnabled()) {
                WifiInfo wifiInfo = manager.getConnectionInfo();
                if (wifiInfo != null) {
                    NetworkInfo.DetailedState state = WifiInfo.getDetailedStateOf(wifiInfo.getSupplicantState());
                    if (state == NetworkInfo.DetailedState.CONNECTED || state == NetworkInfo.DetailedState.OBTAINING_IPADDR) {
                        return wifiInfo.getSSID().replace("\"", "");
                    }
                }
            }
            return "";
        }catch (Exception ex){
            ex.printStackTrace();
            return "";
        }
    }
    public static void disconnectFromWifi(String ssid, Context context){
        WifiManager manager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        manager.disconnect();
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


    public static void connectToNetwork(final String ssidtoconnect, final Context context, final Response.Listener listener, final Response.Listener listenerSuccess){


        disconnectFromWifi(ssidtoconnect, context);

        final int DELAY_RESTART_SECS = 10;
        final int DELAY_BETWEEN_INTENT_TO_RECONNCET_SECS = 5;

        Timer timer = new Timer();
        timer.schedule(new TimerTask()
        {


            int i = 0;
            @Override
            public void run()
            {
                i++;
                RestartTry restartTry;
                if (tryConnectToNetwork(ssidtoconnect, context)){
                    String actualSsid = getWifiName(context);
                    if (actualSsid.equalsIgnoreCase(ssidtoconnect)){
                        //info = "OOOok" +Integer.toString(i);
                        //restartTry.set_success(true);
                        restartTry  = new RestartTry(true, i, "");
                        tryConnectToNetwork(ssidtoconnect, context);
                        actualSsid = getWifiName(context);

                        if(actualSsid !="") {
                            this.cancel();
                            listenerSuccess.onResponse("ok");
                        }
                        else
                            restartTry  = new RestartTry(false, i, "El ssid actual ["+actualSsid+"] no es igual al configurado ["+ssidtoconnect+"]");
                    }else{
                        //info = "NOO-"+Integer.toString(i);;
                        restartTry  = new RestartTry(false, i, "El ssid actual ["+actualSsid+"] no es igual al configurado ["+ssidtoconnect+"]");
                    }
                }else{
                    //info = "HHHHH-"+Integer.toString(i);;
                    restartTry  = new RestartTry(false, i, "SSID ["+ssidtoconnect+"] no encontrado");
                }

                listener.onResponse((Object) restartTry);
            }
        }, DELAY_RESTART_SECS*1000, DELAY_BETWEEN_INTENT_TO_RECONNCET_SECS*1000);
    }

    public static void addWifiConfig(String ssid,String password, Context context) {

        final String NETWORK_ADDITIONAL_SECURITY_TKIP = "tkip";
        final String NETWORK_ADDITIONAL_SECURITY_AES = "aes";
        final String NETWORK_ADDITIONAL_SECURITY_WEP = "wep";
        final String NETWORK_ADDITIONAL_SECURITY_NONE = "";
        final String BACKSLASH = "\"";


        String securityParam, securityDetailParam;
        securityParam = "wpa2";
        securityDetailParam = NETWORK_ADDITIONAL_SECURITY_AES;

        if (ssid == null) {
            throw new IllegalArgumentException(
                    "Required parameters can not be NULL #");
        }

        String wifiName = ssid;
        WifiConfiguration conf = new WifiConfiguration();
        // On devices with version Kitkat and below, We need to send SSID name
        // with double quotes. On devices with version Lollipop, We need to send
        // SSID name without double quotes
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            conf.SSID = wifiName;
        } else {
            conf.SSID = BACKSLASH + wifiName + BACKSLASH;
        }
        String security = securityParam;
        if (security.equalsIgnoreCase("WEP")) {
            conf.wepKeys[0] = password;
            conf.wepTxKeyIndex = 0;
            conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
        } else if (security.equalsIgnoreCase("NONE")) {
            conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
        } else if ("WPA"
                .equalsIgnoreCase(security)
                || "WPA2"
                .equalsIgnoreCase(security)
                || "WPA/WPA2 PSK"
                .equalsIgnoreCase(security)) {
            // appropriate ciper is need to set according to security type used,
            // ifcase of not added it will not be able to connect
            conf.preSharedKey = BACKSLASH + password + BACKSLASH;
            conf.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
            conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            conf.status = WifiConfiguration.Status.ENABLED;
            conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
            conf.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
            conf.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
        }
        String securityDetails = securityDetailParam;
        if (securityDetails.equalsIgnoreCase(NETWORK_ADDITIONAL_SECURITY_TKIP)) {
            conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
        } else if (securityDetails.equalsIgnoreCase(NETWORK_ADDITIONAL_SECURITY_AES)) {
            conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
        } else if (securityDetails.equalsIgnoreCase(NETWORK_ADDITIONAL_SECURITY_WEP)) {
            conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
            conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
        } else if (securityDetails.equalsIgnoreCase(NETWORK_ADDITIONAL_SECURITY_NONE)) {
            conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.NONE);
        }
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

        int newNetworkId = wifiManager.addNetwork(conf);

        if(newNetworkId == -1){

            String j = null;
            j.toLowerCase();
        }

        //TODO: Deberia chequar si devuelve -1 que lance una excepcion
        //wifiManager.enableNetwork(newNetworkId, true);
        wifiManager.saveConfiguration();
        //wifiManager.setWifiEnabled(true);
    }

}
