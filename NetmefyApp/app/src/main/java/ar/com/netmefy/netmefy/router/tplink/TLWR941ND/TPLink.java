package ar.com.netmefy.netmefy.router.tplink.TLWR941ND;


import android.content.Context;
import android.graphics.Bitmap;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import ar.com.netmefy.netmefy.router.ConfigWifi;
import ar.com.netmefy.netmefy.router.Device;
import ar.com.netmefy.netmefy.router.RequestQueueSingleton;
import ar.com.netmefy.netmefy.router.Router;


final class Constantsok {

    public static String NETWROK_ADDITIONAL_SECURITY_TKIP = "tkip";
    public static String NETWROK_ADDITIONAL_SECURITY_AES = "aes";
    public static String NETWROK_ADDITIONAL_SECURITY_WEP = "wep";
    public static String NETWROK_ADDITIONAL_SECURITY_NONE = "";
    public static String BACKSLASH = "\\";
}

/**
 * Created by fiok on 24/06/2017.
 */

public class TPLink extends Router {


    public TPLink(Context context){
        _context = context;
        _queue = RequestQueueSingleton.getInstance(this._context).getRequestQueue();

    }

    @Override
    public void restart(final Response.Listener listener, final Response.ErrorListener errorListener) {
        StringRequestRouter sr = new StringRequestRouter(Request.Method.GET,
                TPLinkConstants.URL_RESTART,
                TPLinkConstants.URL_RESTART_REFERRER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        listener.onResponse("ok");

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                errorListener.onErrorResponse(error);
            }
        });
        execute(sr);
    }
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

    @Override
    public void restartAndWaitUntilConnected(final Response.Listener listener, final Response.ErrorListener errorListener) {

        getWifiSsid(new Response.Listener<String>() {
            @Override
            public void onResponse(String ssid) {
                final String ssidtoconnect = ssid;
                restart(new Response.Listener() {
                    @Override
                    public void onResponse(Object response) {

                        Timer timer = new Timer();
                        timer.schedule(new TimerTask()
                        {
                            int i = 0;
                            @Override
                            public void run()
                            {
                                i++;
                                Context context = _context;
                                String info = "";

                                if (tryConnectToNetwork(ssidtoconnect)){
                                    String actualSsid = getWifiName();
                                    if (actualSsid.equalsIgnoreCase(ssidtoconnect)){

                                        info = "ok" +Integer.toString(i);

                                        listener.onResponse((Object) info);
                                        this.cancel();
                                    }else{
                                        info = ""+Integer.toString(i);;
                                    }
                                }else{
                                    info = ""+Integer.toString(i);;
                                }


                            }
                        }, 30000, 5000);
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

    @Override
    public void setWifiSsid(String newSsid, final Response.Listener listener, final Response.ErrorListener errorListener){
        String url;
        url = TPLinkConstants.URL_WIFI_SET_SSID.replace(TPLinkConstants.URL_WIFI_SET_SSID_PARAM, newSsid);
        StringRequestRouter stringRequest = new StringRequestRouter(Request.Method.GET,
                url,
                TPLinkConstants.URL_WIFI_SET_SSID_REFERRER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String result) {
                        String ssid = result;
                        listener.onResponse(ssid);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        errorListener.onErrorResponse(error);
                    }
                });
        //_queue.add(stringRequest);
        execute(stringRequest);
    }

    @Override
    public void setWifiPassword(String newPassword, final Response.Listener listener, final Response.ErrorListener errorListener) {
        String url;
        url = TPLinkConstants.URL_WIFI_SET_PASSWORD.replace(TPLinkConstants.URL_WIFI_SET_PASSWORD_PARAM, newPassword);
        StringRequestRouter stringRequest = new StringRequestRouter(Request.Method.GET,
                url,
                TPLinkConstants.URL_WIFI_SET_PASSWORD_REFERRER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String result) {
                        String ssid = result;
                        listener.onResponse(ssid);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        errorListener.onErrorResponse(error);
                    }
                });
        //_queue.add(stringRequest);
        execute(stringRequest);
    }

    @Override
    public void saveWifiChanges(ConfigWifi configWifi ) {
        addWifiConfig(configWifi.getSsid(), configWifi.getPassword(),"wpa2", Constantsok.NETWROK_ADDITIONAL_SECURITY_AES );


    }

    public void addWifiConfig(String ssid,String password,String securityParam,String securityDetailParam) {
          /*String NETWROK_ADDITIONAL_SECURITY_TKIP = "tkip";
          String NETWROK_ADDITIONAL_SECURITY_AES = "aes";
          String NETWROK_ADDITIONAL_SECURITY_WEP = "wep";
          String NETWROK_ADDITIONAL_SECURITY_NONE = "";
          String BACKSLASH = "\\";*/

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
            conf.SSID = Constantsok.BACKSLASH + wifiName + Constantsok.BACKSLASH;
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
            conf.preSharedKey = Constantsok.BACKSLASH + password + Constantsok.BACKSLASH;
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
        if (securityDetails.equalsIgnoreCase(Constantsok.NETWROK_ADDITIONAL_SECURITY_TKIP)) {
            conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
        } else if (securityDetails.equalsIgnoreCase(Constantsok.NETWROK_ADDITIONAL_SECURITY_AES)) {
            conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
        } else if (securityDetails.equalsIgnoreCase(Constantsok.NETWROK_ADDITIONAL_SECURITY_WEP)) {
            conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
            conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
        } else if (securityDetails.equalsIgnoreCase(Constantsok.NETWROK_ADDITIONAL_SECURITY_NONE)) {
            conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.NONE);
        }
        WifiManager wifiManager = (WifiManager) _context.getSystemService(Context.WIFI_SERVICE);

        int newNetworkId = wifiManager.addNetwork(conf);
        wifiManager.enableNetwork(newNetworkId, true);
        wifiManager.saveConfiguration();
        wifiManager.setWifiEnabled(true);
    }

    @Override
    public void setConfigWifiAndRestart(final ConfigWifi configWifi, final Response.Listener listener, final Response.ErrorListener errorListener) {
        saveWifiChanges(configWifi);

        setConfigWifi(configWifi,
                new Response.Listener() {
                    @Override
                    public void onResponse(Object response) {

                        restartAndWaitUntilConnected(listener, errorListener);
                    }
                }, errorListener);
        /*
        setConfigWifi(configWifi,
                new Response.Listener() {
                    @Override
                    public void onResponse(Object response) {
                        restart(new Response.Listener() {
                            @Override
                            public void onResponse(Object response) {
                                Timer timer = new Timer();
                                timer.schedule(new TimerTask()
                                {
                                    @Override
                                    public void run()
                                    {
                                        Context context = _context;
                                        String info = "";
                                        try{


                                            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                                            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                                            if (activeNetwork != null) { // connected to the internet
                                                if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                                                    // connected to wifi
                                                    //Toast.makeText(context, activeNetwork.getTypeName(), Toast.LENGTH_SHORT).show();
                                                    info = activeNetwork.getTypeName();
                                                } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                                                    // connected to the mobile provider's data plan
                                                    //Toast.makeText(context, activeNetwork.getTypeName(), Toast.LENGTH_SHORT).show();
                                                    info = activeNetwork.getTypeName();
                                                }
                                            } else {
                                                // not connected to the internet
                                                //Toast.makeText(context, "No Conectado!!!", Toast.LENGTH_SHORT).show();
                                            }
                                            listener.onResponse((Object) info);

                                        }catch (Exception ex){
                                            //Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT).show();
                                            info = ex.getMessage();
                                        }

                                    }
                                }, 0, 5000);
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
*/
    }

    @Override
    public void getWifiSsid(final Response.Listener<String> listener, final Response.ErrorListener errorListener){
        StringRequestRouter stringRequest = new StringRequestRouter(Request.Method.GET,
                TPLinkConstants.URL_WIFI_GET_SSID,
                TPLinkConstants.URL_WIFI_GET_SSID_REFERRER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String result) {
                        String ssid;

                        String textBefore = "0, 8, 0, ";
                        int pinit = result.indexOf(textBefore) + textBefore.length();
                        int pend = result.indexOf(", 108, 2, 1, ");
                        String aux ;
                        aux = result.substring(pinit, pend);

                        ssid = aux.replace("\"", "");

                        listener.onResponse(ssid);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        errorListener.onErrorResponse(error);
                    }
                });

        //_queue.add(stringRequest);
        execute(stringRequest);
    }

    @Override
    public void getWifiPassword(final Response.Listener listener, final Response.ErrorListener errorListener) {
        StringRequestRouter stringRequest = new StringRequestRouter(Request.Method.GET,
                TPLinkConstants.URL_WIFI_GET_PASSWORD,
                TPLinkConstants.URL_WIFI_GET_PASSWORD_REFERRER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String result) {
                        String textBefore = "8, 1, 3, \"132\", 1, 0, \"\", 1812, \"\", ";
                        int pinit = result.indexOf(textBefore) + textBefore.length();
                        int pend = result.indexOf(", 1, 0, 0, 1, ");
                        String aux ;
                        aux = result.substring(pinit, pend);

                        String password;
                        password= aux.replace("\"", "");
                        listener.onResponse(password);
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


    @Override
    public void setConfigWifi(final ConfigWifi configWifi, final Response.Listener listener, final Response.ErrorListener errorListener) {
        this.setWifiSsid(configWifi.getSsid(),
                new Response.Listener() {
                    @Override
                    public void onResponse(Object response) {
                        setWifiPassword(configWifi.getPassword(),
                                new Response.Listener() {
                                    @Override
                                    public void onResponse(Object response) {

                                        listener.onResponse("ok");
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

    @Override
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

    @Override
    public void listDevicesConnected(final Response.Listener<List<Device>> listener, Response.ErrorListener errorListener ) {

        StringRequestRouter stringRequest = new StringRequestRouter(Request.Method.GET,
                TPLinkConstants.URL_LIST_CONNECTED,
                TPLinkConstants.URL_LIST_CONNECTED_REFERRER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String result = response.toString();
                        ResponseTPLink.setListDevices(result);

                        listener.onResponse(ResponseTPLink.getListDevices());

                    }
                },
                errorListener);

        execute(stringRequest);
    }
}
