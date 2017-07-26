package ar.com.netmefy.netmefy.router.tplink.TLWR941ND;


import android.content.Context;


import com.android.volley.Response;

import com.android.volley.toolbox.StringRequest;

import java.util.ArrayList;
import java.util.List;

import ar.com.netmefy.netmefy.router.Device;
import ar.com.netmefy.netmefy.router.RequestQueueSingleton;

import ar.com.netmefy.netmefy.router.Router;
import ar.com.netmefy.netmefy.router.RouterConstants;
import ar.com.netmefy.netmefy.router.UrlRouter;
import ar.com.netmefy.netmefy.router.eUrl;


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
    public String getName(){
        return "TPLink(inPark) TL-WR941ND";
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
    public void _setWifiSsid(final String newSsid, final Response.Listener progressListener, final Response.ErrorListener errorListener, final Response.Listener successListener){
        setValue(newSsid, _routerConstants.get(eUrl.WIFI_SET_SSID), new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                restartAndWaitUntilConnected(progressListener, errorListener, successListener);
            }
        }, errorListener);
        /*super.setWifiSsid(newSsid,
                new Response.Listener() {
                    @Override
                    public void onResponse(Object response) {
                        setValue(newSsid, _routerConstants.get(eUrl.WIFI_SET_SSID),
                                new Response.Listener() {
                                    @Override
                                    public void onResponse(Object response) {
                                        restartAndWaitUntilConnected(listener, errorListener, listenerSuccess);
                                    }
                                }, errorListener);
                    }
                }, errorListener, listenerSuccess);
        */
    }

    @Override
    public void _setWifiPassword(final String newPassword, final Response.Listener progressListener, final Response.ErrorListener errorListener, final Response.Listener successListener){
        setValue(newPassword, _routerConstants.get(eUrl.WIFI_SET_PASSWORD), new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                restartAndWaitUntilConnected(progressListener, errorListener, successListener);
            }
        }, errorListener);
        /*super.setWifiPassword(newPassword,
                new Response.Listener() {
                    @Override
                    public void onResponse(Object response) {
                        setValue(newPassword, _routerConstants.get(eUrl.WIFI_SET_PASSWORD),
                                new Response.Listener() {
                                    @Override
                                    public void onResponse(Object response) {
                                        restartAndWaitUntilConnected(listener, errorListener, listenerSuccess);
                                    }
                                }, errorListener);
                    }
                }, errorListener, listenerSuccess);
        */
    }

/*
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

    }*/



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

    public void getListBlocked(final Response.Listener listener, final Response.ErrorListener errorListener){

    }


    public void logout(final Response.Listener listener,final Response.ErrorListener errorListener){

    }

    @Override
    public void addBlockByMac(String mac, Response.Listener progressListener, Response.ErrorListener errorListener, Response.Listener successListener) {

    }

    @Override
    public void removeBlockByMac(String mac, Response.Listener progressListener, Response.ErrorListener errorListener, Response.Listener successListener) {

    }
}
