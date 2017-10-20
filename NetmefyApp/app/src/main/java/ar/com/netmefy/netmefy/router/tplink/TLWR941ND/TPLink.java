package ar.com.netmefy.netmefy.router.tplink.TLWR941ND;


import android.content.Context;


import com.android.volley.Response;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import ar.com.netmefy.netmefy.router.ConfigWifi;
import ar.com.netmefy.netmefy.router.Device;
import ar.com.netmefy.netmefy.router.RequestQueueSingleton;

import ar.com.netmefy.netmefy.router.Router;
import ar.com.netmefy.netmefy.router.RouterConstants;
import ar.com.netmefy.netmefy.router.UrlRouter;
import ar.com.netmefy.netmefy.router.eUrl;
import ar.com.netmefy.netmefy.services.Utils;
import ar.com.netmefy.netmefy.services.WifiUtils;


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
    /*public void login(final Response.Listener listener, final Response.ErrorListener errorListener){
        listener.onResponse("");
    }*/


    @Override
    public String getName(){
        return "TPLink(inPark) TL-WR941ND";
    }

    @Override
    public void _setWifiSsid(final ConfigWifi newSsid, final Response.Listener progressListener, final Response.ErrorListener errorListener, final Response.Listener successListener){
        setValue(newSsid.getSsid(), _routerConstants.get(eUrl.WIFI_SET_SSID), new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                _ssid = newSsid.getSsid();
                restartAndWaitUntilConnected(progressListener, errorListener, successListener);
            }
        }, errorListener);
    }

    @Override
    public void _setWifiPassword(final ConfigWifi newPassword, final Response.Listener progressListener, final Response.ErrorListener errorListener, final Response.Listener successListener){
        setValue(newPassword.getPassword(), _routerConstants.get(eUrl.WIFI_SET_PASSWORD), new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                _password = newPassword.getPassword();
                restartAndWaitUntilConnected(progressListener, errorListener, successListener);
            }
        }, errorListener);
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

    @Override
    protected List<Device> parseHtmlMacListBlocked(String html){
        List<Device> list = new ArrayList<Device>();
        if(html.isEmpty())
            return  list;

        String[] splitted = html.split("\n");
        Device device;
        for (int i = 0; i < splitted.length; i++) {
            device  = new Device();
            device.setMac(Utils.getTextBetween(splitted[i], "\"", "\"", ""));
            device.setId(i);
            list.add(device);
        }
        //Utils.getTextBetween(html.split("\n")[0], "\"", "\"", "")
        return list;
    }

    @Override
    public void addBlockByUrl(final String url_to_block, final Response.Listener progress, final Response.ErrorListener error,final Response.Listener success) {
        getUrlListBlocked(new Response.Listener<List<String>>() {
            @Override
            public void onResponse(List<String> urls) {
                int number = -1;
                int i = 0;//EMPIEZA EN UNO EL CONTADOR DE LA TABLA DE LA INTERFAZ WEB
                for (String url : urls) {
                    if (url.equalsIgnoreCase(url_to_block)) {
                        number = i;
                        break;
                    }
                    i++;
                }

                if(number!=-1)
                    setValueAndReconnect(String.valueOf(number),
                            _routerConstants.get(eUrl.ADD_BLOCK_BY_URL_RULE),
                            progress,
                            error,
                            success);
            }
        }, error);
        /*setValueAndReconnect(url,
                _routerConstants.get(eUrl.ADD_BLOCK_BY_URL_RULE),
                progress,
                error,
                success);*/
    }
    @Override
    public void addUrlToTargetListBlocked(String url, Response.Listener progress, Response.ErrorListener error, Response.Listener success) {
        setValueAndReconnect(url,
                _routerConstants.get(eUrl.ADD_BLOCK_BY_URL),
                progress,
                error,
                success);
    }

    public void getUrlListBlockedFromRule(final  Response.Listener success, final Response.ErrorListener error){
        getValueFromHtmlResponse(_routerConstants.get(eUrl.GET_URL_LIST_BLOCKED_RULE), new Response.Listener<String>() {
            @Override
            public void onResponse(final String htmlListBlocked) {
                success.onResponse(parseHtmlUrlListBlockedRule(htmlListBlocked));
            }
        }, error);
    }

    @Override
    public void removeBlockByUrl(final String urlToRemove, final Response.Listener progress, final Response.ErrorListener error, final Response.Listener success) {
        getUrlListBlockedFromRule(new Response.Listener<List<String>>() {
            @Override
            public void onResponse(List<String> urls) {
                int number = -1;
                int i = 0;
                for (String url : urls) {
                    if (url.equalsIgnoreCase(urlToRemove)) {
                        number = i;
                        break;
                    }
                    i++;
                }

                if(number!=-1)
                    setValueAndReconnect(String.valueOf(number),
                            _routerConstants.get(eUrl.REMOVE_BLOCK_BY_URL_RULE),
                            progress,
                            error,
                            success);
                else
                    success.onResponse("ok");
            }
        }, error);
    }


    public void logout(final Response.Listener listener,final Response.ErrorListener errorListener){

    }

    private List<Device> parseHtmlListDevicesFromWifiStatics(String html){
        String result = html;
        String find = "var hostList = new Array(\n";
        int pinit;
        pinit = result.indexOf(find) + find.length();
        result = result.substring(pinit);//para que empiece desde el find
        int pend;
        //pend = result.indexOf("0,0 );");
        pend = result.indexOf("</SCRIPT>");

        String aux ;
        aux = result.substring(0, pend);

        String[] devicesString = aux.split("\n");
        List<Device> listDevicesAux = new ArrayList<Device>();
        Device device;
        for (int i=0;i<devicesString.length-1;i++){
            device = new Device();
            device.setMac(devicesString[i].split("\"")[1]);
            listDevicesAux.add(device);
        }

        List<Device>  listDevices;
        listDevices = listDevicesAux;
        return listDevices;
    }

    @Override
    public void listDevicesConnected(final Response.Listener listener, final Response.ErrorListener errorListener ) {
        final UrlRouter urlRouter =  _routerConstants.get(eUrl.LIST_CONNECTED_WIRELESS_STATICS);
        executeRequest(urlRouter, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String htmlResponse = response;
                //String value= Utils.getTextBetween(response, urlRouter.get_htmlBefore(), urlRouter.get_htmlAfter(), urlRouter.get_textOnError());
                final List<Device> listFromWifiStatics = parseHtmlListDevicesFromWifiStatics(response);

                //hasta aca tengnola listsa de conectados posta
                //como la lista de dhcp no se actuailza rapido, tengo que joinerarla a esa para cargar los datos que le faltan
                final List<Device> listFinal = new ArrayList<Device>();
                TPLink.super.listDevicesConnected(new Response.Listener<List<Device>>() {
                    @Override
                    public void onResponse(List<Device> listFromDhcp) {
                        for (Device deviceFromDhcp : listFromDhcp) {

                            for (Device deviceFromWifi : listFromWifiStatics) {
                                if(deviceFromDhcp.getMac().equalsIgnoreCase(deviceFromWifi.getMac())){
                                    listFinal.add(deviceFromDhcp);
                                }
                            }
                        }

                        listener.onResponse(listFinal);
                    }
                }, errorListener);


            }
        }, errorListener);

        /*super.listDevicesConnected(new Response.Listener() {
            @Override
            public void onResponse(Object response) {


            }
        }, errorListener);*/
    }



    private String formatMac(String mac){
        return mac.replace(":", "-").toUpperCase();
    }

    @Override
    public void addBlockByMac(final String mac, final Response.Listener progress, final Response.ErrorListener error, final Response.Listener success) {
        this.getConfigWifi(new Response.Listener<ConfigWifi>() {
            @Override
            public void onResponse(final ConfigWifi configWifi) {
                final String macWithFormat ;
                macWithFormat = formatMac(mac);
                getMacListBlocked(new Response.Listener<List<Device>>() {
                    @Override
                    public void onResponse(List<Device> listBlocked) {
                        boolean yaEstaBloqueda = false;
                        for (Device device : listBlocked) {
                            if(device.getMac().equalsIgnoreCase(macWithFormat)){
                                yaEstaBloqueda = true;
                                break;
                            }
                        }
                        if(!yaEstaBloqueda){
                            setValueAndReconnect(macWithFormat,
                                    _routerConstants.get(eUrl.ADD_BLOCK_BY_MAC),
                                    progress,
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            //NOSE PORQUE FALLA AL INSERTAR UNA MAC BLOQUEDA
                                            //PER AUNQUE FALLE INSERTA OK
                                            WifiUtils.connectToNetwork(
                                                    configWifi,
                                                    _context,
                                                    progress, success);
                                        }
                                    },
                                    success);
                        }else{
                            success.onResponse("ok");
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
            }
        }, error);
    }

    @Override
    public void removeBlockByMac(String mac,final Response.Listener progress, final Response.ErrorListener error, final Response.Listener success) {
        final String macWithFormat = formatMac(mac);
        getConfigWifi(new Response.Listener<ConfigWifi>() {
            @Override
            public void onResponse(final ConfigWifi configWifi) {
                getMacListBlocked(new Response.Listener<List<Device>>() {
                    @Override
                    public void onResponse(List<Device> devicesBlocked) {
                        Device deviceFound = null   ;
                        for (Device device : devicesBlocked) {
                            if(device.getMac().equalsIgnoreCase(macWithFormat)){
                                deviceFound = device;
                                break;
                            }
                        }
                        ////////////////////////////////////
                        if(deviceFound !=null){
                            setValueAndReconnect(String.valueOf(deviceFound.getId()),
                                    _routerConstants.get(eUrl.REMOVE_BLOCK_BY_MAC),
                                    progress,
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            WifiUtils.connectToNetwork(
                                                    configWifi,
                                                    _context,
                                                    progress, success);
                                        }
                                    },
                                    success);
                        }else{
                            success.onResponse("ok");
                        }


                    }
                }, error);
            }
        }, error);
    }

    protected List<String> parseHtmlUrlListBlockedRule(String html){

        List<String> list = new ArrayList<String>();
        if(html.equalsIgnoreCase("ERROR HTML_URL_BEGIN_RULE"))
            return  list;

        String url ;

        //String[] splitted = html.split(Pattern.quote("1}-"));
        String[] splitted = html.split("\n");

        //RESTO UNO PORQUE SIEMPRE TRAE EL ULTIMO CON CERO
        for (int i=0;i<splitted.length;i++){
            //url  = splitted[i].replace("\"", "").split(",")[3];
            //url  = splitted[i].split("\"")[1];
            url  = splitted[i].split("\"")[5].replace("NMF-", "");

            //splitted[i].split("\"")

            if(!url.isEmpty()){
                list.add(url.trim());
            }

        }

        return list;
    }


    @Override
    protected List<String> parseHtmlUrlListBlocked(String html){
        List<String> list = new ArrayList<String>();

        String url ;

        //String[] splitted = html.split(Pattern.quote("1}-"));
        String[] splitted = html.split("\n");

        //RESTO UNO PORQUE SIEMPRE TRAE EL ULTIMO CON CERO
        for (int i=0;i<splitted.length-1;i++){
            url  = splitted[i].replace("\"", "").split(",")[3];

            if(!url.isEmpty()){
                list.add(url.trim());
            }

        }

        return list;
    }

}
