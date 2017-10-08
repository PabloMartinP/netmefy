package ar.com.netmefy.netmefy.services;

import android.content.Context;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.android.volley.Response;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import ar.com.netmefy.netmefy.router.ConfigWifi;
import ar.com.netmefy.netmefy.router.RestartTry;
import ar.com.netmefy.netmefy.router.models.InternetSpeed;
import ar.com.netmefy.netmefy.router.models.WifiSignalResult;

/**
 * Created by fiok on 12/08/2017.
 */

public class WifiUtils {

    /*public static void ping(String url, int count, Response.Listener success, Response.Listener finish){
        final int DELAR_SECS = 5;
        final int DELAY_BETWEEN_INTENTS_SECS = 2;

        Timer timer = new Timer();
        timer.schedule(new TimerTask()
        {
            int i = 0;
            @Override
            public void run()
            {
                boolean termino  = false;
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
                            termino = true;
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
                if(!termino)
                    listener.onResponse((Object) restartTry);
            }
        }, DELAR_SECS*1000, DELAY_BETWEEN_INTENTS_SECS*1000);
    }*/

    public static String ping(String url, int count) {
        String str = "";
        try {
            Process process = Runtime.getRuntime().exec(
                    "/system/bin/ping -c "+String.valueOf(count)+" " + url);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            int i;
            char[] buffer = new char[4096];
            StringBuffer output = new StringBuffer();
            while ((i = reader.read(buffer)) > 0)
                output.append(buffer, 0, i);
            reader.close();

            // body.append(output.toString()+"\n");
            str = output.toString();
            // Log.d(TAG, str);
        } catch (IOException e) {
            // body.append("Error\n");
            e.printStackTrace();
        }
        return str;
    }

    public static void testDownloadSpeedWithFast(Context context, final AppCompatActivity appCompatActivity, final Response.Listener<InternetSpeed> progress, final Response.Listener<InternetSpeed> success){
        final AtomicBoolean TERMINO;
        final AtomicReference<String> speedUnitOk;
        final AtomicReference<String> speedOk;

        //////////////////////////////////
        TERMINO = new AtomicBoolean(false);
        speedUnitOk = new AtomicReference<String>("");
        speedOk = new AtomicReference<String>("");
        speedOk.set("0");
        ///////////////////////////////
        TERMINO.set(false);
        speedOk.set("0");
        speedUnitOk.set("");


        class MyJavaScriptInterface
        {
            @JavascriptInterface
            //@SuppressWarnings("unused")
            public void processHTML(String html)
            {
                Document document = Jsoup.parse(html);
                String speed, speedUnit;
                speed = document.getElementById("speed-value").html();
                speedUnit = document.getElementById("speed-units").html();

                //si es distinto a mbps le setteo uno porque no me importa nada vieja
                if(!speedUnit.equalsIgnoreCase("mbps")){
                    speedUnit = "Mbps";
                    speed = "1";
                }

                speedOk.set(speed);
                speedUnitOk.set(speedUnit);
                /*if(speedUnit.equalsIgnoreCase("&nbsp;")) {//a vces trae fruta, le setteo uno porque si
                    speedUnitOk.set("Mbps");
                    speed = "1";
                    speedOk.set(speed);
                }
                else
                    speedUnitOk.set(speedUnit);*/


                //btnok.setText(speed + " " + speedUnit);

                InternetSpeed internetSpeed = new InternetSpeed();
                internetSpeed.set_speed(speedOk.get());
                internetSpeed.set_unit(speedUnitOk.get());

                if(document.getElementById("after-test-actions").getElementsByAttribute("style").size()==1){
                    TERMINO.set(true);
                    success.onResponse(internetSpeed);
                    //btnok.setText(speed + " " + speedUnit + " OK");
                }else{
                    progress.onResponse(internetSpeed);
                }
            }
        }


        //final WebView browser = (WebView)findViewById(R.id.webview);
        //final WebView browser = webView;
        final WebView browser = new WebView(context);


        /* JavaScript must be enabled if you want it to work, obviously */
        browser.getSettings().setJavaScriptEnabled(true);

        /* Register a new JavaScript interface called HTMLOUT */
        browser.addJavascriptInterface(new MyJavaScriptInterface(), "HTMLOUT");

        /* WebViewClient must be set BEFORE calling loadUrl! */
        browser.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url)
            {
                final int DELAY_RESTART_SECS = 5;
                final int DELAY_BETWEEN_INTENT_TO_RECONNCET_SECS = 2;
                //btn.setText("HOA");
                Timer timer = new Timer();
                timer.schedule(new TimerTask()
                {
                    int i = 0;
                    @Override
                    public void run()
                    {
                        try{
                            //InternetSpeed internetSpeed = new InternetSpeed();
                            //internetSpeed.set_speed(Integer.valueOf(speedOk.get()));
                            //internetSpeed.set_unit(speedUnitOk.get());

                            if(TERMINO.get()) {
                                //success.onResponse(internetSpeed);
                                this.cancel();
                            }
                            else{
                                appCompatActivity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        browser.loadUrl("javascript:window.HTMLOUT.processHTML('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');");
                                    }
                                });

                                //progress.onResponse(internetSpeed);
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        i++;
                    }
                }, DELAY_RESTART_SECS*1000, DELAY_BETWEEN_INTENT_TO_RECONNCET_SECS*1000);

            }
        });

        browser.loadUrl("https://fast.com/es/");
    }

    public static void checkSignal(final Context context, final Response.Listener<WifiSignalResult> success){
        final int DELAY_SECS = 0;
        final int DELAY_BETWEEN_INTENT_SECS = 1;
        final WifiSignalResult result   =new WifiSignalResult();
        final int numberOfLevels = 5;
        Timer timer = new Timer();
        timer.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

                final WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                int rssi = wifiInfo.getRssi();
                int level = WifiManager.calculateSignalLevel(rssi, numberOfLevels);
                result.newIntent(level, rssi);
                success.onResponse( result);
            }
        }, DELAY_SECS*1000, DELAY_BETWEEN_INTENT_SECS*1000);


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
                boolean ok1 = wifiManager.enableNetwork(network.networkId, true);
                boolean ok2 = wifiManager.saveConfiguration();
                boolean ok3 = wifiManager.setWifiEnabled(true);

                String actualSsid = getWifiName(context);
                if(actualSsid!="")
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

    /*
    * devuelve el Id de la red actualmente conectada*/
    public static int getNetworkId(WifiManager manager) {
        try {
            //WifiManager manager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            if (manager.isWifiEnabled()) {
                WifiInfo wifiInfo = manager.getConnectionInfo();
                if (wifiInfo != null) {
                    NetworkInfo.DetailedState state = WifiInfo.getDetailedStateOf(wifiInfo.getSupplicantState());
                    if (state == NetworkInfo.DetailedState.CONNECTED || state == NetworkInfo.DetailedState.OBTAINING_IPADDR) {
                        //return wifiInfo.getSSID().replace("\"", "");
                        return wifiInfo.getNetworkId();
                    }
                }
            }
            return -1;
        }catch (Exception ex){
            ex.printStackTrace();
            return -2;
        }
    }

    public static void disconnectFromWifi(String ssid, Context context){
        WifiManager manager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        manager.disconnect();
    }

    public static void connectToNetwork(final ConfigWifi configWifi, final Context context, final Response.Listener listener, final Response.Listener listenerSuccess){

        final String ssidtoconnect = configWifi.getSsid();

        disconnectFromWifi(ssidtoconnect, context);

        //elimino el wifi actual y lo agrego de nuevo,
        //TODO: solo hace falta cuando se cambia la password o ssid, en otros casos no hace falta
        removeWifiWithEqualSsid(configWifi.getSsid(), context);

        addWifiConfig(configWifi.getSsid(), configWifi.getPassword(), context);

        final int DELAY_RESTART_SECS = 5;
        final int DELAY_BETWEEN_INTENT_TO_RECONNCET_SECS = 2;

        Timer timer = new Timer();
        timer.schedule(new TimerTask()
        {


            int i = 0;
            @Override
            public void run()
            {
                boolean termino  = false;
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
                            termino = true;
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
                if(!termino)
                    listener.onResponse((Object) restartTry);
            }
        }, DELAY_RESTART_SECS*1000, DELAY_BETWEEN_INTENT_TO_RECONNCET_SECS*1000);
    }


    private static void removeWifiWithEqualSsid(String ssidToRemove, Context context){
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

        //elimino todos menos el actualmente conectado
        //int actualNetworkdId = getNetworkId(wifiManager);

        List<WifiConfiguration> list = wifiManager.getConfiguredNetworks();
        for( WifiConfiguration i : list ) {

            //if(i.networkId != actualNetworkdId) {
                if(i.SSID.replace("\"", "").equals(ssidToRemove)){
                    wifiManager.removeNetwork(i.networkId);
                    wifiManager.saveConfiguration();
                }
            //}

        }

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
        //removeWifiWithEqualSsid(ssid, wifiManager);

        int newNetworkId = wifiManager.addNetwork(conf);

        if(newNetworkId == -1){

            String j = null;//TODO: Esto es para que tire excepcion
            j.toLowerCase();
        }

        //TODO: Deberia chequar si devuelve -1 que lance una excepcion
        //wifiManager.enableNetwork(newNetworkId, true);
        wifiManager.saveConfiguration();
        //wifiManager.setWifiEnabled(true);
    }
}
