package ar.com.netmefy.netmefy.router.tplink.TLWR941ND;


import android.content.Context;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import ar.com.netmefy.netmefy.router.ConfigWifi;
import ar.com.netmefy.netmefy.router.Device;
import ar.com.netmefy.netmefy.router.RequestQueueSingleton;
import ar.com.netmefy.netmefy.router.RestartTry;
import ar.com.netmefy.netmefy.router.Router;
import ar.com.netmefy.netmefy.router.RouterConstants;
import ar.com.netmefy.netmefy.router.UrlRouter;
import ar.com.netmefy.netmefy.router.eUrl;
import ar.com.netmefy.netmefy.services.Utils;

public class TPLink extends Router {


    public TPLink(Context context){
        _context = context;
        _queue = RequestQueueSingleton.getInstance(this._context).getRequestQueue();
        _routerConstants = new RouterConstants(RouterConstants.eRouter.TPLink);
    }

    @Override
    public StringRequest newStringRequest( UrlRouter urlRouter, Response.Listener listener, Response.ErrorListener errorListener) {
        return new StringRequestRouter( urlRouter, listener, errorListener);
    }

    /*
    * los tplink no necesitan login por eso nohace nada este metodo*/
    public void login(final Response.Listener listener, final Response.ErrorListener errorListener){
        listener.onResponse("");
    }

    @Override
    public void restart(final Response.Listener listener, final Response.ErrorListener errorListener) {
        StringRequestRouter sr = new StringRequestRouter(
                _routerConstants.get(eUrl.RESTART),
                listener,
                errorListener);

        execute(sr);
    }

    @Override
    public void setWifiSsid(final String newSsid, final Response.Listener listener, final Response.ErrorListener errorListener){
        super.setWifiSsid(newSsid,
                new Response.Listener() {
                    @Override
                    public void onResponse(Object response) {
                        setValue(newSsid, _routerConstants.get(eUrl.WIFI_SET_SSID),
                                new Response.Listener() {
                                    @Override
                                    public void onResponse(Object response) {
                                        restartAndWaitUntilConnected(listener, errorListener);
                                    }
                                }, errorListener);
                    }
                }, errorListener);
    }

    @Override
    public void setWifiPassword(final String newPassword, final Response.Listener listener, final Response.ErrorListener errorListener){
        super.setWifiPassword(newPassword,
                new Response.Listener() {
                    @Override
                    public void onResponse(Object response) {
                        setValue(newPassword, _routerConstants.get(eUrl.WIFI_SET_PASSWORD),
                                new Response.Listener() {
                                    @Override
                                    public void onResponse(Object response) {
                                        restartAndWaitUntilConnected(listener, errorListener);
                                    }
                                }, errorListener);
                    }
                }, errorListener);
    }



    @Override
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
    protected List<Device> parseHtmlListDevices(String html){
        String result = html;
        String find = "var DHCPDynList = new Array(\n";
        int pinit;
        pinit = result.indexOf(find) + find.length();
        int pend;
        pend = result.indexOf("</SCRIPT>");

        String aux ;
        aux = result.substring(pinit, pend);

        String[] devicesString = aux.split("\n");
        List<Device> listDevicesAux = new ArrayList<Device>();
        Device device;
        for (int i=0;i<devicesString.length-1;i++){
            device = Device.newFromString(devicesString[i]);
            listDevicesAux.add(device);
        }

        List<Device>  listDevices;
        listDevices = listDevicesAux;
        return listDevices;
    }


}
