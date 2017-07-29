package ar.com.netmefy.netmefy.router.nucom.R5000UNv2;

import android.content.Context;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

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
        executeRequest(_routerConstants.get(eUrl.LOGIN), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(!response.contains("Other user logined") && !response.contains("/login."))
                    listener.onResponse(response);
                else{
                    errorListener.onErrorResponse(new VolleyError("Error - usuario ya logueado"));
                }

            }
        }, errorListener);
    }

    public void logout(final Response.Listener listener,final Response.ErrorListener errorListener){

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


    protected void setValueWithSessionKeyAndReconnect(final String newValue, eUrl eUrlSessionKey, final UrlRouter urlRouter, final Response.Listener progresListener, final Response.ErrorListener errorListener, final Response.Listener successListener){
        getSessionKey(eUrlSessionKey, new Response.Listener<String>() {
            @Override
            public void onResponse(String sessionKey) {

                urlRouter.addSessionKey(sessionKey);
                setValue(newValue, urlRouter, new Response.Listener() {
                    @Override
                    public void onResponse(Object response) {
                        Utils.connectToNetwork(
                                _ssid,
                                _context,
                                progresListener, successListener);
                        }
                }, errorListener);

            }
        }, errorListener);
    }


    @Override
    public void _setWifiSsid(final String newSsid, final Response.Listener progressListener, final Response.ErrorListener errorListener, final Response.Listener successListener) {
        _ssid = newSsid;
        setValueWithSessionKeyAndReconnect(newSsid,
                eUrl.WIFI_SET_SSID_TO_GET_SESSIONKEY,
                _routerConstants.get(eUrl.WIFI_SET_SSID),
                progressListener, errorListener, successListener);
    }

    @Override
    public void _setWifiPassword(final String newPassword, final Response.Listener progressListener, final Response.ErrorListener errorListener, final Response.Listener successListener) {
        setValueWithSessionKeyAndReconnect(newPassword,
                eUrl.WIFI_SET_PASSWORD_TO_GET_SESSIONKEY,
                _routerConstants.get(eUrl.WIFI_SET_PASSWORD),
                progressListener, errorListener, successListener);

    }
    @Override
    public String getName(){
        return "NucomR500UNv2";
    }

    private void addBlockByMacAndReconnect(final String mac, final Response.Listener progressListener, final Response.ErrorListener errorListener, final Response.Listener successListener){
        setValueWithSessionKeyAndReconnect(mac,
                eUrl.ADD_BLOCK_BY_MAC_TO_GET_SESSIONKEY,
                _routerConstants.get(eUrl.ADD_BLOCK_BY_MAC),
                progressListener,
                errorListener,
                successListener);
    }

    @Override
    public void addBlockByMac(final String mac, final Response.Listener progressListener, final Response.ErrorListener errorListener, final Response.Listener successListener){

        getListBlocked(new Response.Listener<List<Device>>() {
            @Override
            public void onResponse(final List<Device> devicesBlockedBeforeAdd) {
                final List<String> macsString = new ArrayList<String>();
                //armo una lista de macs
                for (Device dev :devicesBlockedBeforeAdd) {
                    macsString.add(dev.getMac());
                }
                /*
                * agrego la mac y porque anda mal el router me vuelve a agregar todos los que habia eliminado!!!
                * para parchearlo al terminar de agregar borro tod0 excepto la mac que se agrego
                * */
                addBlockByMacAndReconnect(mac,
                        progressListener,
                        errorListener,
                        new Response.Listener() {
                            @Override
                            public void onResponse(Object response) {
                                //borra tod0 excepto la mac psada por param

                                macsString.add(mac.toUpperCase());
                                
                                ///////////////////////
                                removeAllBlockedAndReconnect(macsString,
                                        progressListener, errorListener, successListener);
                            }
                        });

            }
        }, errorListener);



    }

    public void removeAllBlockedAndReconnect(final List<String> macsToIgnore, final Response.Listener progressListener, final Response.ErrorListener errorListener, final Response.Listener successListener){
        getListBlocked(new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                List<Device> listBlocked = (List<Device>)response;

                //convierto todas las mac bloquedas a string e ignoro las del param
                String macsToBlock = "";
                String aux1;
                for (Device device :listBlocked) {
                    if(!macsToIgnore.contains(device.getMac()))
                        macsToBlock = macsToBlock.concat(device.getMac()).concat(",%20");

                }
                macsToBlock = macsToBlock.toUpperCase();

                removeBlockByMac(macsToBlock,
                        progressListener, errorListener, successListener);

            }
        }, errorListener);
    }


    @Override
    public void removeBlockByMac(final String mac, final Response.Listener progressListener, final Response.ErrorListener errorListener, final Response.Listener successListener){
        String macFormat;
        if(!mac.substring(mac.length()-1).equals(",%20"))
            macFormat = mac.concat(",%20").toUpperCase();
        else
            macFormat = mac;

        if(mac.isEmpty())
            successListener.onResponse("ok-empty");

        setValueWithSessionKeyAndReconnect(macFormat,
                eUrl.REMOVE_BLOCK_BY_MAC_TO_GET_SESSIONKEY,
                _routerConstants.get(eUrl.REMOVE_BLOCK_BY_MAC),
                progressListener,
                errorListener,
                successListener);
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

    private List<Device> parseHtmlListBlocked(String html){
        Device device ;
        List<Device> list = new ArrayList<Device>();
        if(!html.isEmpty()){
            Document doc ;
            doc = Jsoup.parse(html);

            Elements elements ;
            elements = doc.getElementsByAttribute("type");
            String mac ;
            for (Element element : elements) {
                mac = element.attr("value");
                device = new Device();
                device.setMac(mac);
                list.add(device);
            }





        }

        return list;
    }

    @Override
    public void getListBlocked(final Response.Listener<List<Device>> listener, final Response.ErrorListener errorListener){
        login(new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                getValueFromHtmlResponse(_routerConstants.get(eUrl.GET_LIST_BLOCKED), new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String htmlListBlocked) {
                        listener.onResponse(parseHtmlListBlocked(htmlListBlocked));
                    }
                }, errorListener);

            }
        }, errorListener);

    }
}
