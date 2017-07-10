package ar.com.netmefy.netmefy.router.nucom.R5000UNv2;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.List;

import ar.com.netmefy.netmefy.router.ConfigWifi;
import ar.com.netmefy.netmefy.router.Device;
import ar.com.netmefy.netmefy.router.RequestQueueSingleton;
import ar.com.netmefy.netmefy.router.Router;
import ar.com.netmefy.netmefy.router.tplink.TLWR941ND.StringRequestRouter;
import ar.com.netmefy.netmefy.router.tplink.TLWR941ND.TPLinkConstants;

/**
 * Created by fiok on 09/07/2017.
 */

public class Nucom extends Router {

    public Nucom(Context context){
        _context = context;
        _queue = RequestQueueSingleton.getInstance(this._context).getRequestQueue();

    }
    @Override
    public void restart(Response.Listener listener, Response.ErrorListener errorListener) {

    }

    @Override
    public void restartAndWaitUntilConnected(Response.Listener listener, Response.ErrorListener errorListener) {

    }

    private void login(final Response.Listener<String> listener,final Response.ErrorListener errorListener){
        final String URL_LOGIN = "http://192.168.1.1/login.cgi?username=admin&psd=taller";
        StringRequestRouterNucom stringRequest = new StringRequestRouterNucom(Request.Method.GET,
                URL_LOGIN,
                "",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String result) {
                        listener.onResponse(result);
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
    public void getWifiSsid(final Response.Listener<String> listener,final Response.ErrorListener errorListener) {

        login(new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                final String URL_WIFI_GET_SSID = "http://192.168.1.1/wlcfg.html";
                StringRequestRouterNucom stringRequest = new StringRequestRouterNucom(Request.Method.GET,
                        URL_WIFI_GET_SSID,
                        "", new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        /*HASTA ACA LLEGA BIEN, AHORA HAY QUE PARSEAR EL TEXTO PARA BUSCAR EL SSID*/
                        response = response.toString();

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
                );

                //_queue.add(stringRequest);
                execute(stringRequest);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                errorListener.onErrorResponse(error);
            }
        });



    }

    @Override
    public void getWifiPassword(Response.Listener<String> listener, Response.ErrorListener errorListener) {

    }

    @Override
    public void setWifiSsid(String newSsid, Response.Listener listener, Response.ErrorListener errorListener) {

    }

    @Override
    public void setWifiPassword(String newPassword, Response.Listener listener, Response.ErrorListener errorListener) {

    }

    @Override
    public void setConfigWifiAndRestart(ConfigWifi configWifi, Response.Listener listener, Response.ErrorListener errorListener) {

    }

    @Override
    public void setConfigWifi(ConfigWifi configWifi, Response.Listener listener, Response.ErrorListener errorListener) {

    }

    @Override
    public void getConfigWifi(Response.Listener<ConfigWifi> listener, Response.ErrorListener errorListener) {

    }

    @Override
    public void saveWifiChanges(ConfigWifi configWifi) {

    }

    @Override
    public void listDevicesConnected(Response.Listener<List<Device>> listener, Response.ErrorListener errorListener) {

    }
}
