package ar.com.netmefy.netmefy.router;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import com.android.volley.toolbox.StringRequest;

import java.util.List;

import ar.com.netmefy.netmefy.router.nucom.R5000UNv2.Nucom;
import ar.com.netmefy.netmefy.router.tplink.TLWR941ND.TPLink;
import ar.com.netmefy.netmefy.services.Utils;

/**
 * Created by fiok on 24/06/2017.
 */

public abstract class Router {

    protected RouterConstants _routerConstants;
    protected  Context _context;
    protected RequestQueue _queue ;

    protected String _ssid = "";

    private static RouterConstants.eRouter type = RouterConstants.eRouter.none;
    private static Router _router = null;

    public static Router getInstance(Context context){
        if(_router == null ){
            switch (type){
                case Nucom:
                    _router = new Nucom(context);
                    break;
                case TPLink:
                    _router = new TPLink(context);
                    break;
                case none:
                    _router = null;
                    break;
            }
            return _router;
        }else {
            _router.set_context(context);
            return _router;
        }
    }

    /*
    * este create se ejecuta una vez sola, despues hay que llamar siempre a getInstance
    * */
    public static void createNucom(){
        type = RouterConstants.eRouter.Nucom;
        _router = null  ;
    }

    /*
    * este create se ejecuta una vez sola, despues hay que llamar siempre a getInstance
    * */
    public static void createTPLink(){
        type = RouterConstants.eRouter.TPLink;
        _router = null  ;
    }

    public void set_context(Context context){
        _context = context;
    }

    public void execute(StringRequest stringRequest){
        _queue.add(stringRequest);
    }


    public  abstract void restart(Response.Listener listener, Response.ErrorListener errorListener);


    public void restartAndWaitUntilConnected(final Response.Listener listener, final Response.ErrorListener errorListener, final Response.Listener listenerSuccess) {
        //antes de hacer el restart obtengo el ssid,
        // para que al reconectar sepa a que ssid tengo que conectarme
        //esto es porque los cells por default al perder conexion con un AP
        //se conectan a otro que tenga configurado y este al alcance

        getWifiSsid(new Response.Listener<String>() {
            @Override
            public void onResponse(String ssid) {

                final String ssidtoconnect = ssid;
                restart(new Response.Listener() {
                    @Override
                    public void onResponse(Object response) {
                        Utils.connectToNetwork(ssidtoconnect, _context, listener, listenerSuccess);
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


    public void getWifiSsid(final Response.Listener<String> listener, final Response.ErrorListener errorListener){

        if(_ssid.isEmpty()){
            login(new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if(!response.contains("Other user logined"))
                        getValueFromHtmlResponse(_routerConstants.get(eUrl.WIFI_GET_SSID), new Response.Listener() {
                            @Override
                            public void onResponse(Object response) {
                                _ssid = response.toString();
                                listener.onResponse(_ssid);
                            }
                        }, errorListener);
                    else{
                        errorListener.onErrorResponse(new VolleyError("Error - usuario ya logueado"));
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    errorListener.onErrorResponse(error);
                }
            });
        }else{
            listener.onResponse(_ssid);
        }


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
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        errorListener.onErrorResponse(error);
                    }
                });
        execute(stringRequest);
    }



    public abstract void _setWifiSsid(final String newSsid, final Response.Listener progressListener, final Response.ErrorListener errorListener, final Response.Listener successListener) ;
    /*
    * Agrega a la lista de redes conocidas del android a la nueva red
    * */
    public void setWifiSsid(final String newSsid, final Response.Listener listener, final Response.ErrorListener errorListener, final Response.Listener listenerSuccess) {

        getWifiPassword(new Response.Listener<String>() {
            @Override
            public void onResponse(String password) {
                final ConfigWifi configWifi = new ConfigWifi(newSsid, password);
                saveWifiChanges(configWifi);
                //listener.onResponse("ok");
                //listener.onResponse(newSsid);
                 _setWifiSsid(newSsid, listener, errorListener, listenerSuccess);
            }
        }, errorListener);
    }

    public abstract void _setWifiPassword(final String newPassword, final Response.Listener progressListener, final Response.ErrorListener errorListener, final Response.Listener successListener) ;
    /*
    * Agrega a la lista de redes conocidas del android a la nueva red y devuelve el ssid
    * */
    public void setWifiPassword(final String newPassword, final Response.Listener progressListener, final Response.ErrorListener errorListener, final Response.Listener successListener) {
        getWifiSsid(new Response.Listener<String>() {
            @Override
            public void onResponse(String ssid) {
                final ConfigWifi configWifi = new ConfigWifi(ssid, newPassword);
                saveWifiChanges(configWifi);
                //listener.onResponse("ok");
                _setWifiPassword(newPassword,
                        progressListener, errorListener, successListener );

                //listener.onResponse(ssid);
            }
        }, errorListener);

    }


    //public abstract void setConfigWifiAndRestart(ConfigWifi configWifi, Response.Listener listener, Response.ErrorListener errorListener) ;
    //public abstract void setConfigWifi(ConfigWifi configWifi, Response.Listener listener, Response.ErrorListener errorListener);


    protected abstract List<Device> parseHtmlListDevices(String html);

    protected void executeRequest(UrlRouter urlRouter, final Response.Listener listener, Response.ErrorListener errorListener ) {
        StringRequest stringRequest = newStringRequest(urlRouter, listener, errorListener);
        execute(stringRequest);
    }
    public void listDevicesConnected(final Response.Listener listener, Response.ErrorListener errorListener ) {

    /*
    * ESTE TPLINK AL PARECER ACUMULA LA LISTA DE LOS DHCP,
    * Y CUANDO SE DESCONECTA UNO NO LO LIMPIA DE LA LISTA,
    * ASI QUE MUESTRA ALGUNOS DEVICES QUE YA SE DESCONECTARON
    * POR AHORA LA SOLUCION SERIA RESTART
    * */
        final UrlRouter constants = _routerConstants.get(eUrl.LIST_CONNECTED);
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

    public abstract String getName();

    public abstract void logout(final Response.Listener listener, final Response.ErrorListener errorListener);

    public abstract void addBlockByMac(final String mac, final Response.Listener progressListener, final Response.ErrorListener errorListener, final Response.Listener successListener);

    public abstract void removeBlockByMac(final String mac, final Response.Listener progressListener, final Response.ErrorListener errorListener, final Response.Listener successListener);

    public abstract void getListBlocked(final Response.Listener listener, final Response.ErrorListener errorListener);

    public void saveWifiChanges(ConfigWifi configWifi ) {
        Utils.addWifiConfig(configWifi.getSsid(), configWifi.getPassword(), _context);
    }

}
