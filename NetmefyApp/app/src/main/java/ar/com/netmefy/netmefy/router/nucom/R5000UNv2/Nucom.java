package ar.com.netmefy.netmefy.router.nucom.R5000UNv2;

import android.content.Context;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.ArrayList;
import java.util.List;


import ar.com.netmefy.netmefy.router.Device;
import ar.com.netmefy.netmefy.router.RequestQueueSingleton;
import ar.com.netmefy.netmefy.router.Router;
import ar.com.netmefy.netmefy.router.RouterConstants;
import ar.com.netmefy.netmefy.router.eUrl;
import ar.com.netmefy.netmefy.router.UrlRouter;
import ar.com.netmefy.netmefy.services.Utils;

/**
 * Created by fiok on 09/07/2017.
 */

public class Nucom extends Router {

    boolean alreadyLogin;
    public Nucom(Context context){
        _context = context;
        _queue = RequestQueueSingleton.getInstance(this._context).getRequestQueue();
        alreadyLogin = true;
        _routerConstants = new RouterConstants(RouterConstants.eRouter.Nucom);
    }

    @Override
    public StringRequest newStringRequest(UrlRouter urlRouter, Response.Listener listener, Response.ErrorListener errorListener) {
        return new StringRequestRouterNucom(urlRouter, listener, errorListener);
    }

    private void getSessionKey(eUrl eUrl, final Response.Listener<String> listener, final Response.ErrorListener errorListener) {
        getValueFromHtmlResponse(_routerConstants.get(eUrl),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(Utils.isNumeric(response))
                            listener.onResponse(response);
                        else
                            errorListener.onErrorResponse(new VolleyError(response));

                    }
                }, errorListener);
    }

    @Override
    public void restart(final Response.Listener listener, final Response.ErrorListener errorListener) {

        getSessionKey(eUrl.RESTART_TO_GET_SESSIONKEY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String sessionKey) {
                        final UrlRouter urlRestart;
                        urlRestart = _routerConstants.get(eUrl.RESTART);
                        urlRestart.addSessionKey(sessionKey);
                        executeRequest(urlRestart, listener, errorListener);
                    }
                }, errorListener);
    }


    @Override
    protected void login(final Response.Listener listener,final Response.ErrorListener errorListener){
        executeRequest(_routerConstants.get(eUrl.LOGIN), listener, errorListener);
    }

    protected void logout(final Response.Listener listener,final Response.ErrorListener errorListener){
        executeRequest(_routerConstants.get(eUrl.LOGOUT), listener, errorListener);
    }

    @Override
    public void getConfigWifi(final Response.Listener listener, final Response.ErrorListener errorListener) {
        login(new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                Nucom.super.getConfigWifi(listener, errorListener);
            }
        }, errorListener);
    }

    @Override
    public void setWifiSsid(final String newSsid, final Response.Listener listener, final Response.ErrorListener errorListener) {
        super.setWifiSsid(newSsid,
                new Response.Listener() {
                    @Override
                    public void onResponse(Object response) {
                        getSessionKey(eUrl.WIFI_SET_SSID_TO_GET_SESSIONKEY,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String sessionKey) {
                                        setValueWithSessionKey(newSsid, sessionKey,
                                                _routerConstants.get(eUrl.WIFI_SET_SSID),
                                                new Response.Listener() {
                                                    @Override
                                                    public void onResponse(Object response) {
                                                        Utils.connectToNetwork(newSsid, _context, listener);
                                                    }
                                                },
                                                errorListener);

                                    }
                                }, errorListener);
                        ///////////////////////////////////////////////
                    }
                }, errorListener);
    }

    @Override
    public void setWifiPassword(final String newPassword, final Response.Listener listener, final Response.ErrorListener errorListener) {
        super.setWifiPassword(newPassword,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String ssid) {
                        getSessionKey(eUrl.WIFI_SET_PASSWORD_TO_GET_SESSIONKEY,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String sessionKey) {
                                        final UrlRouter urlset;
                                        urlset = _routerConstants.get(eUrl.WIFI_SET_PASSWORD);
                                        setValueWithSessionKey(newPassword, sessionKey, urlset,
                                                new Response.Listener() {
                                                    @Override
                                                    public void onResponse(Object response) {
                                                        Utils.connectToNetwork(ssid, _context, listener);
                                                    }
                                                },
                                                errorListener);
                                    }
                                }, errorListener);
                    }
                }, errorListener);
    }
    @Override
    public String getName(){
        return "NucomR500UNv2";
    }

    @Override
    public void addBlockByMac(final String mac, final Response.Listener listener, final Response.ErrorListener errorListener){

        getWifiSsid(
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String ssid) {
                        getSessionKey(eUrl.ADD_BLOCK_BY_MAC_TO_GET_SESSIONKEY,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String sessionKey) {
                                        final UrlRouter urlset;
                                        urlset = _routerConstants.get(eUrl.ADD_BLOCK_BY_MAC);
                                        setValueWithSessionKey(mac, sessionKey, urlset,
                                                new Response.Listener() {
                                                    @Override
                                                    public void onResponse(Object response) {
                                                        logout(
                                                                new Response.Listener() {
                                                                    @Override
                                                                    public void onResponse(Object response) {
                                                                        Utils.connectToNetwork(ssid, _context, listener);
                                                                    }
                                                                },
                                                                new Response.ErrorListener() {
                                                                    @Override
                                                                    public void onErrorResponse(VolleyError error) {
                                                                        errorListener.onErrorResponse(error);
                                                                    }
                                                                }
                                                        );
                                                    }
                                                },
                                                errorListener);
                                    }
                                }, errorListener);
                    }
                }, errorListener
        );


    }


    @Override
    public void removeBlockByMac(final String mac, final Response.Listener listener, final Response.ErrorListener errorListener){
        getWifiSsid(
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String ssid) {
                        getSessionKey(eUrl.REMOVE_BLOCK_BY_MAC_TO_GET_SESSIONKEY,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String sessionKey) {
                                        final UrlRouter urlset;
                                        urlset = _routerConstants.get(eUrl.REMOVE_BLOCK_BY_MAC);
                                        setValueWithSessionKey(mac, sessionKey, urlset,
                                                new Response.Listener() {
                                                    @Override
                                                    public void onResponse(Object response) {
                                                        Utils.connectToNetwork(ssid, _context, listener);
                                                        /*logout(
                                                                new Response.Listener() {
                                                                    @Override
                                                                    public void onResponse(Object response) {
                                                                        Utils.connectToNetwork(ssid, _context, listener);
                                                                    }
                                                                }, errorListener
                                                        );*/
                                                    }
                                                },
                                                errorListener);

                                    }
                                }, errorListener);
                    }
                }, errorListener
        );
    }


    @Override
    protected List<Device> parseHtmlListDevices(String html) {
        String deviceString;

        List<String> listDeviceString = new ArrayList<String>();

        int p1,p2 ;
        String t1, t2, t;
        String beginDevice = "<tr><td>";
        String endDevice = "</td></tr>";

        t = html;
        boolean rs;
        do{
            p1 = t.indexOf(beginDevice);
            if(p1!=-1){
                t1 = t.substring(p1 + beginDevice.length());
                p2 = t1.indexOf(endDevice);
                t2 = t1.substring(0, p2);

                t = t.substring(t.indexOf(t2) + t2.length() + endDevice.length());
                listDeviceString.add(t2);
                rs = true;
            }else
                rs = false;

        }while(rs);

        List<Device> listDevice = new ArrayList<Device>();
        Device device;
        String[] devicesString;
        for (String aDeviceString :listDeviceString
                ) {
            device = new Device();
            devicesString = aDeviceString.split("</td><td>");

            device.setName(devicesString [0]);
            device.setMac(devicesString [1]);
            device.setIp(devicesString [2]);

            listDevice.add(device);
        }


        if(listDevice.size() == 0){
            device = new Device();
            device.setIp("0.0.0.0");
            device.setMac("00.00.00.00.00.00");
            device.setName("Ningun dispositivo conectado");
            listDevice.add(device);
        }

        return listDevice;
    }

    public void listDevicesConnected(final Response.Listener listener, final Response.ErrorListener errorListener) {

        login(new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                Nucom.super.listDevicesConnected(listener, errorListener);
            }
        }, errorListener);

    }
}
