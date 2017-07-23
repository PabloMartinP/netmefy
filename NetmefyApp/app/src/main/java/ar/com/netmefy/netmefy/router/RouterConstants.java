package ar.com.netmefy.netmefy.router;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by fiok on 22/07/2017.
 */

public class RouterConstants {

    public enum eRouter{
        TPLink,
        Nucom
    }

    eUrl URL;
    Map<eUrl, UrlRouter> _urlRouters = new HashMap<eUrl, UrlRouter>();

    public RouterConstants(eRouter router){
        switch (router){
            case Nucom:
                InitNucom();
                break;
            case TPLink:
                InitTPLink();
                break;

        }

    }

    private void InitNucom(){
        UrlRouter.set_urlRoot("http://192.168.1.1/");
        _urlRouters.put(eUrl.LOGIN, UrlRouter.create("login.cgi?username=admin&psd=taller", ""));
        _urlRouters.put(eUrl.WIFI_GET_SSID, UrlRouter.createWithFinder("wlcfg.html", "menu.html", "var ssid = '", "';"));
        _urlRouters.put(eUrl.WIFI_GET_PASSWORD, UrlRouter.createWithFinder("wlsecurity.html", "menu.html", "var wpaPskKey = '", "';"));

        _urlRouters.put(eUrl.LIST_CONNECTED, UrlRouter.create("dhcpinfo.html", "menu.html"));

        //_urlRouters.put(eUrl.WIFI_SET_SSID, UrlRouter.create("userRpm/WlanNetworkRpm.htm", "userRpm/MenuRpm.htm"));
        //_urlRouters.put(eUrl.WIFI_SET_PASSWORD, UrlRouter.create("userRpm/WlanNetworkRpm.htm", "userRpm/MenuRpm.htm"));
    }


    private void InitTPLink(){
        UrlRouter.set_urlRoot("http://192.168.0.1/");
        //_urlRouters.put(eUrl.LOGIN, UrlRouter.create("login.cgi?username=admin&psd=taller", ""));
        _urlRouters.put(eUrl.WIFI_GET_SSID, UrlRouter.createWithFinder("userRpm/WlanNetworkRpm.htm", "userRpm/MenuRpm.htm", "0, 8, 0, \"", "\", 108, "));
        _urlRouters.put(eUrl.WIFI_GET_PASSWORD, UrlRouter.createWithFinder("userRpm/WlanSecurityRpm.htm", "userRpm/MenuRpm.htm",   "8, 1, 3, \"132\", 1, 0, \"\", 1812, \"\", \""     ,           "\", 1, 0, 0, 1" ));

        _urlRouters.put(eUrl.LIST_CONNECTED, UrlRouter.create("userRpm/AssignedIpAddrListRpm.htm", "userRpm/MenuRpm.htm"));


        _urlRouters.put(eUrl.RESTART, UrlRouter.create("userRpm/SysRebootRpm.htm?Reboot=Reboot", "userRpm/SysRebootRpm.htm"));



        //_urlRouters.put(eUrl.WIFI_SET_SSID, UrlRouter.create("userRpm/WlanNetworkRpm.htm", "userRpm/MenuRpm.htm"));
        //_urlRouters.put(eUrl.WIFI_SET_PASSWORD, UrlRouter.create("userRpm/WlanNetworkRpm.htm", "userRpm/MenuRpm.htm"));
    }

    public UrlRouter get(eUrl eUrl){
        return _urlRouters.get(eUrl);
    }


}
