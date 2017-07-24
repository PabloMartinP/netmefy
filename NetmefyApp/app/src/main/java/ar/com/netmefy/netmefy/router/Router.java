package ar.com.netmefy.netmefy.router;

import android.content.Context;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import ar.com.netmefy.netmefy.router.tplink.TLWR941ND.StringRequestRouter;
import ar.com.netmefy.netmefy.router.tplink.TLWR941ND.TPLinkConstants;
import ar.com.netmefy.netmefy.services.Utils;

/**
 * Created by fiok on 24/06/2017.
 */

public abstract class Router {
    final String NETWORK_ADDITIONAL_SECURITY_TKIP = "tkip";
    final String NETWORK_ADDITIONAL_SECURITY_AES = "aes";
    final String NETWORK_ADDITIONAL_SECURITY_WEP = "wep";
    final String NETWORK_ADDITIONAL_SECURITY_NONE = "";
    final String BACKSLASH = "\"";


    protected RouterConstants _routerConstants;
    protected  Context _context;
    protected RequestQueue _queue ;

    public void execute(StringRequest stringRequest){
        _queue.add(stringRequest);
    }

    public  abstract void restart(Response.Listener listener, Response.ErrorListener errorListener);


    public void restartAndWaitUntilConnected(final Response.Listener listener, final Response.ErrorListener errorListener) {
        //antes de hacer el restart obtengo el ssid,
        // para que al reconectar sepa a que ssid tengo que conectarme
        //esto es porque los cells por default al perder conexion con un AP
        //se conectan a otro que tenga configurado y este al alcance
        final int DELAY_RESTART_SECS = 20;
        final int DELAY_BETWEEN_INTENT_TO_RECONNCET_SECS = 5;

        getWifiSsid(new Response.Listener<String>() {
            @Override
            public void onResponse(String ssid) {

                final String ssidtoconnect = ssid;
                restart(new Response.Listener() {
                    @Override
                    public void onResponse(Object response) {

                        connectToNetwork(ssidtoconnect, listener);

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        errorListener.onErrorResponse(error);
                    }
                });

            }
        }, errorListener);
    }

    public abstract StringRequest newStringRequest(UrlRouter urlRouter,
                               Response.Listener listener,
                               Response.ErrorListener errorListener);

    protected abstract void login(final Response.Listener<String> listener, final Response.ErrorListener errorListener);
    //public abstract void getWifiSsid(Response.Listener<String> listener, Response.ErrorListener errorListener);
    public void getWifiSsid(final Response.Listener<String> listener, final Response.ErrorListener errorListener){
        login(new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                getValueFromHtmlResponse(_routerConstants.get(eUrl.WIFI_GET_SSID), listener, errorListener);
            }
        }, errorListener);
    }

    /*
    * intenta conectar a un ssid especifico,
    * si esta conectado a otra red la desconecta y se conecta a la ssid
    * Importante: El SSID ya tienen que estar agregado a la lista de redes del android
    * */
    private  boolean tryConnectToNetwork(String ssid){
        WifiManager wifiManager = (WifiManager)_context.getSystemService(Context.WIFI_SERVICE);
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
    public String getWifiName() {
        try {
            WifiManager manager = (WifiManager) _context.getSystemService(Context.WIFI_SERVICE);
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
    private void disconnectFromWifi(String ssid){
        WifiManager manager = (WifiManager) _context.getSystemService(Context.WIFI_SERVICE);
        manager.disconnect();
    }
    protected void connectToNetwork(final String ssidtoconnect, final Response.Listener listener){
        disconnectFromWifi(ssidtoconnect);


        final int DELAY_RESTART_SECS = 20;
        final int DELAY_BETWEEN_INTENT_TO_RECONNCET_SECS = 5;

        Timer timer = new Timer();
        timer.schedule(new TimerTask()
        {
            int i = 0;
            @Override
            public void run()
            {
                i++;
                Context context = _context;
                RestartTry restartTry;
                if (tryConnectToNetwork(ssidtoconnect)){
                    String actualSsid = getWifiName();
                    if (actualSsid.equalsIgnoreCase(ssidtoconnect)){
                        //info = "OOOok" +Integer.toString(i);
                        //restartTry.set_success(true);
                        restartTry  = new RestartTry(true, i, "");
                        tryConnectToNetwork(ssidtoconnect);
                        actualSsid = getWifiName();

                        if(actualSsid !="")
                            this.cancel();
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

    protected void getValueFromHtmlResponse(final UrlRouter urlRouter,final Response.Listener listener, final Response.ErrorListener errorListener) {
        StringRequest stringRequest = newStringRequest(
                urlRouter,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String result) {
                        String value= Utils.getTextBetween(result, urlRouter.get_htmlBefore(), urlRouter.get_htmlAfter(), urlRouter.get_textOnError());
                        listener.onResponse(value);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        errorListener.onErrorResponse(error);
                    }
                });
        execute(stringRequest);

    }

    public void getWifiPassword(final Response.Listener listener, final Response.ErrorListener errorListener) {
        //final UrlRouter constants = _routerConstants.get(eUrl.WIFI_GET_PASSWORD);
        login(new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                getValueFromHtmlResponse(_routerConstants.get(eUrl.WIFI_GET_PASSWORD), listener, errorListener);
            }
        }, errorListener);
    }

    protected void setValue(String newValue, final UrlRouter urlRouter, final Response.Listener listener, final Response.ErrorListener errorListener){
        urlRouter.set_newValue(newValue);
        StringRequest stringRequest = newStringRequest(
                urlRouter,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String result) {
                        listener.onResponse("ok");
                    }
                },errorListener);
        execute(stringRequest);
    }

    protected void setValueWithSessionKey(String newValue, String sessionKey, final UrlRouter urlRouter, final Response.Listener listener, final Response.ErrorListener errorListener){

        urlRouter.addSessionKey(sessionKey);

        setValue(newValue, urlRouter, listener, errorListener);
    }

    /*
    * Agrega a la lista de redes conocidas del android a la nueva red
    * */
    public void setWifiSsid(final String newSsid, final Response.Listener listener, final Response.ErrorListener errorListener) {
        getWifiPassword(new Response.Listener<String>() {
            @Override
            public void onResponse(String password) {
                final ConfigWifi configWifi = new ConfigWifi(newSsid, password);
                saveWifiChanges(configWifi);
                //listener.onResponse("ok");
                listener.onResponse(newSsid);
            }
        }, errorListener);
    }

    /*
    * Agrega a la lista de redes conocidas del android a la nueva red
    * */
    public void setWifiPassword(final String newPassword, final Response.Listener listener, final Response.ErrorListener errorListener) {
        getWifiSsid(new Response.Listener<String>() {
            @Override
            public void onResponse(String ssid) {
                final ConfigWifi configWifi = new ConfigWifi(ssid, newPassword);
                saveWifiChanges(configWifi);
                //listener.onResponse("ok");
                listener.onResponse(ssid);
            }
        }, errorListener);

    }


    public abstract void setConfigWifiAndRestart(ConfigWifi configWifi, Response.Listener listener, Response.ErrorListener errorListener) ;
    public abstract void setConfigWifi(ConfigWifi configWifi, Response.Listener listener, Response.ErrorListener errorListener);


    protected abstract List<Device> parseHtmlListDevices(String html);

    public void listDevicesConnected(final Response.Listener listener, Response.ErrorListener errorListener ) {

    /*
    * ESTE TPLINK AL PARECER ACUMULA LA LISTA DE LOS DHCP,
    * Y CUANDO SE DESCONECTA UNO NO LO LIMPIA DE LA LISTA,
    * ASI QUE MUESTRA ALGUNOS DEVICES QUE YA SE DESCONECTARON
    * POR AHORA LA SOLUCION SERIA RESTART
    * */
        final UrlRouter constants = _routerConstants.get(eUrl.LIST_CONNECTED);
        //StringRequestRouter stringRequest = new StringRequestRouter(Request.Method.GET,
        StringRequest stringRequest = newStringRequest(
                constants,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //String result = response.toString();
                        //ResponseTPLink.setListDevices(result);
                        List<Device> list = parseHtmlListDevices(response);
                        //listener.onResponse(ResponseTPLink.getListDevices());
                        listener.onResponse(list);
                    }
                },
                errorListener);

        execute(stringRequest);
    }

    public void getConfigWifi(final Response.Listener listener, final Response.ErrorListener errorListener) {
        this.getWifiSsid(new Response.Listener<String>() {
            @Override
            public void onResponse(final String ssid) {
                getWifiPassword(new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String password) {
                        ConfigWifi configWifi = new ConfigWifi();

                        configWifi.setSsid(ssid);
                        configWifi.setPassword(password);

                        listener.onResponse(configWifi);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        errorListener.onErrorResponse(error);
                    }
                });

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                errorListener.onErrorResponse(error);
            }
        });

    }

    public void saveWifiChanges(ConfigWifi configWifi ) {
        addWifiConfig(configWifi.getSsid(), configWifi.getPassword(),"wpa2", NETWORK_ADDITIONAL_SECURITY_AES);
    }
    public void addWifiConfig(String ssid,String password,String securityParam,String securityDetailParam) {


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
        WifiManager wifiManager = (WifiManager) _context.getSystemService(Context.WIFI_SERVICE);

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
