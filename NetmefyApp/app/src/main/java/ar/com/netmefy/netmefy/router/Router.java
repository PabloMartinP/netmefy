package ar.com.netmefy.netmefy.router;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.List;

import ar.com.netmefy.netmefy.router.tplink.TLWR941ND.ResponseTPLink;
import ar.com.netmefy.netmefy.router.tplink.TLWR941ND.StringRequestRouter;
import ar.com.netmefy.netmefy.router.tplink.TLWR941ND.TPLinkConstants;
import ar.com.netmefy.netmefy.services.Utils;

/**
 * Created by fiok on 24/06/2017.
 */

public abstract class Router {


    protected RouterConstants _routerConstants;
    protected  Context _context;
    protected RequestQueue _queue ;

    public void execute(StringRequest stringRequest){
        _queue.add(stringRequest);

    }

    public  abstract void restart(Response.Listener listener, Response.ErrorListener errorListener);

    public  abstract void restartAndWaitUntilConnected(Response.Listener listener, Response.ErrorListener errorListener);


    public abstract StringRequest newStringRequest(int method, UrlRouter urlRouter,
                               Response.Listener listener,
                               Response.ErrorListener errorListener);

    //public abstract void getWifiSsid(Response.Listener<String> listener, Response.ErrorListener errorListener);
    public void getWifiSsid(final Response.Listener<String> listener, final Response.ErrorListener errorListener){
        final UrlRouter constants = _routerConstants.get(eUrl.WIFI_GET_SSID);
        //StringRequestRouter stringRequest = new StringRequestRouter(Request.Method.GET,
        StringRequest stringRequest = newStringRequest(Request.Method.GET,
                constants,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String result) {
                        //String ssid = Utils.getTextBetween(result, "0, 8, 0, \"", "\", 108, ", "Error ssid");
                        String ssid = Utils.getTextBetween(result, constants.get_htmlBefore(), constants.get_htmlAfter(), "Error ssid");
                        listener.onResponse(ssid);
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
    //public abstract void getWifiPassword(Response.Listener<String> listener, Response.ErrorListener errorListener);
    public void getWifiPassword(final Response.Listener listener, final Response.ErrorListener errorListener) {
        final UrlRouter constants = _routerConstants.get(eUrl.WIFI_GET_PASSWORD);
        StringRequest stringRequest = newStringRequest(Request.Method.GET,
                constants,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String result) {
                        /*String textBefore = "8, 1, 3, \"132\", 1, 0, \"\", 1812, \"\", ";
                        int pinit = result.indexOf(textBefore) + textBefore.length();
                        int pend = result.indexOf(", 1, 0, 0, 1, ");
                        String aux ;
                        aux = result.substring(pinit, pend);
                        String password;
                        password= aux.replace("\"", "");
                        */
                        String password;
                        //password = Utils.getTextBetween(result, "8, 1, 3, \"132\", 1, 0, \"\", 1812, \"\", \"",     "\", 1, 0, 0, 1", "Error passwd");
                        password = Utils.getTextBetween(result, constants.get_htmlBefore(), constants.get_htmlAfter(), "Error password");
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

    public abstract void setWifiSsid(String newSsid, Response.Listener listener, Response.ErrorListener errorListener);
    public abstract void setWifiPassword(String newPassword, Response.Listener listener, Response.ErrorListener errorListener);


    public abstract void setConfigWifiAndRestart(ConfigWifi configWifi, Response.Listener listener, Response.ErrorListener errorListener) ;
    public abstract void setConfigWifi(ConfigWifi configWifi, Response.Listener listener, Response.ErrorListener errorListener);
    //public abstract void getConfigWifi(Response.Listener<ConfigWifi> listener, Response.ErrorListener errorListener);

    public abstract void saveWifiChanges(ConfigWifi configWifi );

    protected abstract List<Device> parseHtmlListDevices(String html);

    //public abstract void listDevicesConnected(Response.Listener<List<Device>> listener, Response.ErrorListener errorListener) ;
    public void listDevicesConnected(final Response.Listener listener, Response.ErrorListener errorListener ) {
        final UrlRouter constants = _routerConstants.get(eUrl.LIST_CONNECTED);
        //StringRequestRouter stringRequest = new StringRequestRouter(Request.Method.GET,
        StringRequest stringRequest = newStringRequest(Request.Method.GET,
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

}
