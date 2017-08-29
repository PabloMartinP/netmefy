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
import ar.com.netmefy.netmefy.services.WifiUtils;

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
    private static boolean _requiereLogin = false;

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
        _router = null;
        _requiereLogin = true;
    }

    /*
    * este create se ejecuta una vez sola, despues hay que llamar siempre a getInstance
    * */
    public static void createTPLink(){
        type = RouterConstants.eRouter.TPLink;
        _router = null;
        _requiereLogin = false;
    }

    public void set_context(Context context){
        _context = context;
    }

    public void execute(StringRequest stringRequest){

        _queue.add(stringRequest);
    }


    public void restart(Response.Listener listener, Response.ErrorListener errorListener){
        executeRequest(_routerConstants.get(eUrl.RESTART), listener, errorListener);
    }


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
                        try{
                            //WifiUtils.connectToNetwork(ssidtoconnect, _context, listener, listenerSuccess);
                            listener.onResponse(response);
                        }catch (Exception e){
                            String jjj;
                            jjj = e.toString();
                        }

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

    public abstract StringRequest newStringRequest(UrlRouter urlRouter,
                               Response.Listener listener,
                               Response.ErrorListener errorListener);

    public void getWifiSsid(final Response.Listener<String> listener, final Response.ErrorListener errorListener){
        if(_ssid.isEmpty()){
            getValueFromHtmlResponse(_routerConstants.get(eUrl.WIFI_GET_SSID), new Response.Listener() {
                @Override
                public void onResponse(Object response) {
                    _ssid = response.toString();
                    listener.onResponse(_ssid);
                }
            }, errorListener);
        }else{
            listener.onResponse(_ssid);
        }
    }


    protected void getValueFromHtmlResponse(final UrlRouter urlRouter,final Response.Listener listener, final Response.ErrorListener errorListener) {
        executeRequest(urlRouter, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String value= Utils.getTextBetween(response, urlRouter.get_htmlBefore(), urlRouter.get_htmlAfter(), urlRouter.get_textOnError());
                listener.onResponse(value);
            }
        }, errorListener);
    }

    public void getWifiPassword(final Response.Listener listener, final Response.ErrorListener errorListener) {
        //final UrlRouter constants = _routerConstants.get(eUrl.WIFI_GET_PASSWORD);
        getValueFromHtmlResponse(_routerConstants.get(eUrl.WIFI_GET_PASSWORD), listener, errorListener);
    }

    protected void setValue(String newValue, final UrlRouter urlRouter, final Response.Listener listener, final Response.ErrorListener errorListener){
        urlRouter.set_newValue(newValue);
        executeRequest(urlRouter, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                listener.onResponse("ok-"+response);
            }
        }, errorListener);
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
                _setWifiPassword(newPassword, progressListener, errorListener, successListener );
            }
        }, errorListener);
    }

    protected abstract List<Device> parseHtmlListDevices(String html);

    protected void executeRequest(final UrlRouter urlRouter, final Response.Listener<String> listener, final Response.ErrorListener errorListener ) {

        if(_requiereLogin){
            execute(newStringRequest(urlRouter, new Response.Listener<String>() {
                @Override
                public void onResponse(final String response) {
                    if(!response.contains("Login time out, please login again!") && !response.contains("parent.location='http://192.168.1.1/login.html'")) {
                        listener.onResponse(response);
                    }
                    else{
                        StringRequest srLogin ;
                        srLogin = newStringRequest(_routerConstants.get(eUrl.LOGIN), new Response.Listener() {
                            @Override
                            public void onResponse(Object response) {
                                //_needLogin = false;
                                StringRequest stringRequest = newStringRequest(urlRouter, listener, errorListener);
                                execute(stringRequest);
                            }
                        }, errorListener);
                        execute(srLogin);
                    }
                }
            }, errorListener));
        }else{
            execute(newStringRequest(urlRouter, listener, errorListener));
        }
    }

    public void listDevicesConnected(final Response.Listener listener, Response.ErrorListener errorListener ) {
        /*
        * ESTE TPLINK AL PARECER ACUMULA LA LISTA DE LOS DHCP,
        * Y CUANDO SE DESCONECTA UNO NO LO LIMPIA DE LA LISTA,
        * ASI QUE MUESTRA ALGUNOS DEVICES QUE YA SE DESCONECTARON
        * POR AHORA LA SOLUCION SERIA RESTART
        * */
        executeRequest(_routerConstants.get(eUrl.LIST_CONNECTED), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                List<Device> list = parseHtmlListDevices(response);
                listener.onResponse(list);
            }
        }, errorListener);
    }

    public void getConfigWifi(final Response.Listener<ConfigWifi> listener, final Response.ErrorListener errorListener) {
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

    protected abstract List<Device> parseHtmlMacListBlocked(String html);
    protected void setValueAndReconnect(final String newValue, final UrlRouter urlRouter, final Response.Listener progress, final Response.ErrorListener error, final Response.Listener success){
        setValue(newValue, urlRouter, new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                WifiUtils.connectToNetwork(
                        _ssid,
                        _context,
                        progress, success);
            }
        }, error);
    }

    public void getMacListBlocked(final Response.Listener<List<Device>> success, final Response.ErrorListener error){
        getValueFromHtmlResponse(_routerConstants.get(eUrl.GET_MAC_LIST_BLOCKED), new Response.Listener<String>() {
            @Override
            public void onResponse(final String htmlListBlocked) {
                success.onResponse(parseHtmlMacListBlocked(htmlListBlocked));
            }
        }, error);
    }

    public void saveWifiChanges(ConfigWifi configWifi ) {
        WifiUtils.addWifiConfig(configWifi.getSsid(), configWifi.getPassword(), _context);
    }

    protected abstract List<String> parseHtmlUrlListBlocked(String html);

    public void getUrlListBlocked(final Response.Listener<List<String>> success, Response.ErrorListener error) {
        getValueFromHtmlResponse(_routerConstants.get(eUrl.GET_URL_LIST_BLOCKED), new Response.Listener<String>() {
            @Override
            public void onResponse(final String htmlListBlocked) {
                success.onResponse(parseHtmlUrlListBlocked(htmlListBlocked));
            }
        }, error);
    }

    public abstract void addBlockByUrl(String url, Response.Listener progress, Response.ErrorListener error, Response.Listener success);

    public abstract void removeBlockByUrl(String url, Response.Listener progress, Response.ErrorListener error, Response.Listener success);
}
