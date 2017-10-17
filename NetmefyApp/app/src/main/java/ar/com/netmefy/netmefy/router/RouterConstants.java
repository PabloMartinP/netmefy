package ar.com.netmefy.netmefy.router;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by fiok on 22/07/2017.
 */

public class RouterConstants {

    public enum eRouter{
        TPLink,
        Nucom,
        none
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
        _urlRouters.put(eUrl.LOGIN, UrlRouter.create("login.cgi?username=admin&psd=1234", ""));
        _urlRouters.put(eUrl.WIFI_GET_SSID, UrlRouter.createWithFinder("wlcfg.html", "menu.html", "var ssid = '", "';", "Error SSID!"));
        _urlRouters.put(eUrl.WIFI_GET_PASSWORD, UrlRouter.createWithFinder("wlsecurity.html", "menu.html", "var wpaPskKey = '", "';", "Error Password!"));

        _urlRouters.put(eUrl.LIST_CONNECTED, UrlRouter.create("dhcpinfo.html", "menu.html"));

        //"wlcfg.wl?wlSsidIdx=0&enblewlautogeneration=0&wlEnbl=1&wlButtonEnable=0&wlHide=0&wlAPIsolation=0&wlSsid=NMFNUCOM5&wlCountry=AR&wlMaxAssoc=32&wlDisableWme=0&wlEnableWmf=0&wlEnbl_wl0v1=0&wlSsid_wl0v1=fiok-guest&wlHide_wl0v1=0&wlAPIsolation_wl0v1=0&wlDisableWme_wl0v1=0&wlEnableWmf_wl0v1=0&wlMaxAssoc_wl0v1=32&wlEnbl_wl0v2=0&wlSsid_wl0v2=Broadcom3&wlHide_wl0v2=0&wlAPIsolation_wl0v2=0&wlDisableWme_wl0v2=0&wlEnableWmf_wl0v2=0&wlMaxAssoc_wl0v2=32&wlEnbl_wl0v3=0&wlSsid_wl0v3=Broadcom4&wlHide_wl0v3=0&wlAPIsolation_wl0v3=0&wlDisableWme_wl0v3=0&wlEnableWmf_wl0v3=0&wlMaxAssoc_wl0v3=32&wlSyncNvram=1&sessionKey=531981182"
        String URL_WIFI_SET_SSID_PARAM = "[_ssid_]";
        String URL_WIFI_SET_SSID = "wlcfg.wl?wlSsidIdx=0&enblewlautogeneration=0&wlEnbl=1&wlButtonEnable=0&wlHide=0&wlAPIsolation=0&wlSsid="+URL_WIFI_SET_SSID_PARAM+"&wlCountry=AR&wlMaxAssoc=32&wlDisableWme=0&wlEnableWmf=0&wlEnbl_wl0v1=0&wlSsid_wl0v1=fiok-guest&wlHide_wl0v1=0&wlAPIsolation_wl0v1=0&wlDisableWme_wl0v1=0&wlEnableWmf_wl0v1=0&wlMaxAssoc_wl0v1=32&wlEnbl_wl0v2=0&wlSsid_wl0v2=Broadcom3&wlHide_wl0v2=0&wlAPIsolation_wl0v2=0&wlDisableWme_wl0v2=0&wlEnableWmf_wl0v2=0&wlMaxAssoc_wl0v2=32&wlEnbl_wl0v3=0&wlSsid_wl0v3=Broadcom4&wlHide_wl0v3=0&wlAPIsolation_wl0v3=0&wlDisableWme_wl0v3=0&wlEnableWmf_wl0v3=0&wlMaxAssoc_wl0v3=32&wlSyncNvram=1&";
        _urlRouters.put(eUrl.WIFI_SET_SSID, UrlRouter.createWithReplace(URL_WIFI_SET_SSID, "wlcfg.html", URL_WIFI_SET_SSID_PARAM));
        _urlRouters.put(eUrl.WIFI_SET_SSID_TO_GET_SESSIONKEY, UrlRouter.createWithFinder("wlcfg.html", "menu.html", "loc += '&sessionKey=", "';", "Error sessionKey SET SSID"));

        String URL_WIFI_SET_PASSWORD_PARAM = "[_ssid_]";
        String URL_WIFI_SET_PASSWORD = "wlsecurity.wl?wl_wsc_mode=disabled&wlWscVer2=disabled&wl_wsc_reg=enabled&wsc_config_state=1&wlAuthMode=psk%20psk2&wlAuth=0&wlWpaPsk="+URL_WIFI_SET_PASSWORD_PARAM+"&wlWpaGtkRekey=0&wlNetReauth=36000&wlWep=disabled&wlWpa=tkip+aes&wlKeyBit=1&wlPreauth=0&wlSsidIdx=0&wlSyncNvram=1&";
        _urlRouters.put(eUrl.WIFI_SET_PASSWORD, UrlRouter.createWithReplace(URL_WIFI_SET_PASSWORD, "wlsecurity.html", URL_WIFI_SET_PASSWORD_PARAM));
        _urlRouters.put(eUrl.WIFI_SET_PASSWORD_TO_GET_SESSIONKEY, UrlRouter.createWithFinder("wlsecurity.html ", "menu.html", "var sessionKey='", "';", "Error sessionKey SET PASSWORD"));

        String URL_RESTART = "rebootinfo.cgi?";
        //String URL_RESTART = "rebootinfo.cgi";
        _urlRouters.put(eUrl.RESTART, UrlRouter.create(URL_RESTART, "resetrouter.html"));
        _urlRouters.put(eUrl.RESTART_TO_GET_SESSIONKEY, UrlRouter.createWithFinder("resetrouter.html", "menu.html", "var sessionKey='", "';", "Error sessionKey RESTART"));




        String URL_ADD_BLOCK_BY_MAC_PARAM = "[_mac_]";
        //String URL_ADD_BLOCK_BY_MAC = "wlmacflt.cmd?action=add&wlFltMacAddr=44:78:3e:27:d0:4e&wlSyncNvram=1&";
        String URL_ADD_BLOCK_BY_MAC = "wlmacflt.cmd?action=add&wlFltMacAddr="+URL_ADD_BLOCK_BY_MAC_PARAM+"&wlSyncNvram=1&";
        _urlRouters.put(eUrl.ADD_BLOCK_BY_MAC, UrlRouter.createWithReplace(URL_ADD_BLOCK_BY_MAC, "wlmacflt.html", URL_ADD_BLOCK_BY_MAC_PARAM));
        _urlRouters.put(eUrl.ADD_BLOCK_BY_MAC_TO_GET_SESSIONKEY, UrlRouter.createWithFinder("wlmacflt.html", "menu.html", "loc += '&sessionKey=", "';", "Error ADD_BLOCK_BY_MAC_TO_GET_SESSIONKEY"));



        String URL_REMOVE_BLOCK_BY_MAC_PARAM = "[_mac_]";//AA:BB:CC:00:11:06
        //String URL_REMOVE_BLOCK_BY_MAC = "wlmacflt.cmd?action=remove&rmLst="+URL_REMOVE_BLOCK_BY_MAC_PARAM+",%20&";
        //                               /wlmacflt.cmd?action=remove&rmLst=AA:BB:CC:00:11:06,%20&sessionKey=1602707458
        String URL_REMOVE_BLOCK_BY_MAC = "wlmacflt.cmd?action=remove&rmLst="+URL_REMOVE_BLOCK_BY_MAC_PARAM+"&";
        _urlRouters.put(eUrl.REMOVE_BLOCK_BY_MAC, UrlRouter.createWithReplace(URL_REMOVE_BLOCK_BY_MAC, "wlmacflt.cmd?action=view", URL_REMOVE_BLOCK_BY_MAC_PARAM));
        _urlRouters.put(eUrl.REMOVE_BLOCK_BY_MAC_TO_GET_SESSIONKEY, UrlRouter.createWithFinder("wlmacflt.cmd?action=view", "menu.html", "wlmacflt.cmd?action=refresh&sessionKey=", "\"'", "Error REMOVE_BLOCK_BY_MAC_TO_GET_SESSIONKEY"));

        _urlRouters.put(eUrl.LOGOUT, UrlRouter.create("logout.cgi ", "menu.html"));

        String HTML_BEGIN = "<tr>\n" +
                "      <td class='hd' id='wlmacfltview7'>MAC Address</td>\n" +
                "      <td class='hd' id='wlmacfltview8'>Remove</td>\n" +
                "   </tr>\n";
        _urlRouters.put(eUrl.GET_MAC_LIST_BLOCKED, UrlRouter.createWithFinder("wlmacflt.cmd?action=view", "menu.html", HTML_BEGIN, "</table><br><br>", "ERROR GET_MAC_LIST_BLOCKED"));


        String HTML_URL_BEGIN = "var UrlFilterListObjectItems = '";
        _urlRouters.put(eUrl.GET_URL_LIST_BLOCKED, UrlRouter.createWithFinder("urlfilter.cmd?action=view", "menu.html", HTML_URL_BEGIN, "';", "ERROR GET_URL_LIST_BLOCKED"));


        String URL_ADD_BLOCK_BY_URL_PARAM = "[_URL_]";
        String URL_ADD_BLOCK_BY_URL = "urlfilter.cmd?action=set_url&TodUrlAdd="+URL_ADD_BLOCK_BY_URL_PARAM+"&port_num=80&";
        _urlRouters.put(eUrl.ADD_BLOCK_BY_URL, UrlRouter.createWithReplace(URL_ADD_BLOCK_BY_URL, "url_add.html", URL_ADD_BLOCK_BY_URL_PARAM));
        _urlRouters.put(eUrl.ADD_BLOCK_BY_URL_TO_GET_SESSIONKEY, UrlRouter.createWithFinder("url_add.html", "menu.html", "loc += '&sessionKey=", "';", "Error ADD_BLOCK_BY_MAC_TO_GET_SESSIONKEY"));


        String URL_REMOVE_BLOCK_BY_URL_PARAM = "[_URL_]";//AA:BB:CC:00:11:06
        //urlfilter.cmd?action=remove_url&rmLst=www.a3.com.ar%20&sessionKey=265785373
        //String URL_REMOVE_BLOCK_BY_URL = "wlmacflt.cmd?action=remove&rmLst="+URL_REMOVE_BLOCK_BY_MAC_PARAM+"&";
        String URL_REMOVE_BLOCK_BY_URL = "urlfilter.cmd?action=remove_url&rmLst="+URL_REMOVE_BLOCK_BY_URL_PARAM+"&";
        _urlRouters.put(eUrl.REMOVE_BLOCK_BY_URL, UrlRouter.createWithReplace(URL_REMOVE_BLOCK_BY_URL, "urlfilter.cmd?action=view", URL_REMOVE_BLOCK_BY_URL_PARAM));
        _urlRouters.put(eUrl.REMOVE_BLOCK_BY_URL_TO_GET_SESSIONKEY, UrlRouter.createWithFinder("urlfilter.cmd?action=view", "menu.html", "var locationQuery=\"sessionKey=", "\";", "Error REMOVE_BLOCK_BY_URL_TO_GET_SESSIONKEY"));

    }

    private void InitTPLink(){
        UrlRouter.set_urlRoot("http://192.168.2.1/");
        //UrlRouter.set_urlRoot("http://192.168.55.1/");

        _urlRouters.put(eUrl.WIFI_GET_SSID, UrlRouter.createWithFinder("userRpm/WlanNetworkRpm.htm", "userRpm/MenuRpm.htm", "0, 8, 0, \"", "\", 108, ", "Error SSID!!"));
        //_urlRouters.put(eUrl.WIFI_GET_PASSWORD, UrlRouter.createWithFinder("userRpm/WlanSecurityRpm.htm", "userRpm/MenuRpm.htm",   "8, 1, 3, \"132\", 1, 0, \"\", 1812, \"\", \""     ,           "\", 1, 0, 0, 1", "Error Password!!" ));
        //_urlRouters.put(eUrl.WIFI_GET_PASSWORD, UrlRouter.createWithFinder("userRpm/WlanSecurityRpm.htm", "userRpm/MenuRpm.htm",   "8, 1, 3, \"333\", 1, 0, \"\", 1812, \"\", \""     ,           "\", 1, 0, 0, 1", "Error Password!!" ));
        _urlRouters.put(eUrl.WIFI_GET_PASSWORD, UrlRouter.createWithFinder("userRpm/WlanSecurityRpm.htm", "userRpm/MenuRpm.htm",   "8, 1, 3, \"132\", 1, 0, \"\", 1812, \"\", \""     ,           "\", 1, 0, 0, 1", "Error Password!!" ));

        _urlRouters.put(eUrl.LIST_CONNECTED, UrlRouter.create("userRpm/AssignedIpAddrListRpm.htm", "userRpm/MenuRpm.htm"));

        _urlRouters.put(eUrl.LIST_CONNECTED_WIRELESS_STATICS, UrlRouter.create("userRpm/WlanStationRpm.htm", "userRpm/MenuRpm.htm"));


        String URL_WIFI_SET_SSID_PARAM = "[SSID]";
        String URL_WIFI_SET_SSID = "userRpm/WlanNetworkRpm.htm?ssid1="+URL_WIFI_SET_SSID_PARAM+"&region=2&channel=11&mode=5&chanWidth=2&rate=71&ap=1&broadcast=2&brlssid=&brlbssid=&keytype=1&wepindex=1&authtype=1&keytext=&Save=Save";
        String URL_WIFI_SET_SSID_REFERRER = "userRpm/WlanNetworkRpm.htm";
        _urlRouters.put(eUrl.WIFI_SET_SSID, UrlRouter.createWithReplace(URL_WIFI_SET_SSID, URL_WIFI_SET_SSID_REFERRER, URL_WIFI_SET_SSID_PARAM));

        String URL_WIFI_SET_PASSWORD_PARAM = "[PASSWORD]";
        String URL_WIFI_SET_PASSWORD = "userRpm/WlanSecurityRpm.htm?wepSecOpt=1&keytype=1&keynum=1&key1=&length1=0&key2=&length2=0&key3=&length3=0&key4=&length4=0&wpaSecOpt=3&wpaCipher=1&radiusIp=&radiusPort=1812&radiusSecret=&intervalWpa=0&secType=3&pskSecOpt=2&pskCipher=3&pskSecret="+URL_WIFI_SET_PASSWORD_PARAM+"&interval=0&Save=Save";
        String URL_WIFI_SET_PASSWORD_REFERRER = "userRpm/WlanSecurityRpm.htm";
        _urlRouters.put(eUrl.WIFI_SET_PASSWORD, UrlRouter.createWithReplace(URL_WIFI_SET_PASSWORD, URL_WIFI_SET_PASSWORD_REFERRER, URL_WIFI_SET_PASSWORD_PARAM));

        _urlRouters.put(eUrl.RESTART, UrlRouter.create("userRpm/SysRebootRpm.htm?Reboot=Reboot", "userRpm/SysRebootRpm.htm"));


        String HTML_BEGIN = "var wlanFilterList = new Array(\n";
        _urlRouters.put(eUrl.GET_MAC_LIST_BLOCKED, UrlRouter.createWithFinder("userRpm/WlanMacFilterRpm.htm", "userRpm/MenuRpm.htm", HTML_BEGIN, "0,0 );", "ERROR GET_MAC_LIST_BLOCKED"));

        String URL_ADD_BLOCK_BY_MAC_PARAM = "[_mac_]";
        //String URL_ADD_BLOCK_BY_MAC = "/userRpm/WlanMacFilterRpm.htm?Mac=A8-9F-BA-60-B9-B7&Desc=Test2&Type=1&entryEnabled=1&Changed=0&SelIndex=0&Page=1&Save=Save";
        //String URL_ADD_BLOCK_BY_MAC = "userRpm/WlanMacFilterRpm.htm?Mac="+URL_ADD_BLOCK_BY_MAC_PARAM+"&Desc=NMF-"+URL_ADD_BLOCK_BY_MAC_PARAM+"&Type=1&entryEnabled=1&Changed=0&SelIndex=0&Page=1&Save=Save";
        String URL_ADD_BLOCK_BY_MAC = "userRpm/WlanMacFilterRpm.htm?Mac="+URL_ADD_BLOCK_BY_MAC_PARAM+"&Desc=NMFok&Type=1&entryEnabled=1&Changed=0&SelIndex=0&Page=1&Save=Save";
        _urlRouters.put(eUrl.ADD_BLOCK_BY_MAC, UrlRouter.createWithReplace(URL_ADD_BLOCK_BY_MAC, "userRpm/WlanMacFilterRpm.htm?Add=Add&Page=1", URL_ADD_BLOCK_BY_MAC_PARAM));

        String URL_REMOVE_BLOCK_BY_MAC_PARAM = "[_mac_]";//
        String URL_REMOVE_BLOCK_BY_MAC = "userRpm/WlanMacFilterRpm.htm?Del="+URL_REMOVE_BLOCK_BY_MAC_PARAM+"&Page=1";
        _urlRouters.put(eUrl.REMOVE_BLOCK_BY_MAC, UrlRouter.createWithReplace(URL_REMOVE_BLOCK_BY_MAC, "wlmacflt.cmd?action=view", URL_REMOVE_BLOCK_BY_MAC_PARAM));

        String HTML_URL_BEGIN = "var access_targets_data_param = new Array(\n";
        _urlRouters.put(eUrl.GET_URL_LIST_BLOCKED, UrlRouter.createWithFinder("userRpm/AccessCtrlAccessTargetsRpm.htm", "userRpm/MenuRpm.htm", HTML_URL_BEGIN, ",0 );", "ERROR GET_URL_LIST_BLOCKED"));

        String URL_ADD_BLOCK_BY_URL_PARAM = "[_URL_]";
        String URL_ADD_BLOCK_BY_URL = "userRpm/AccessCtrlAccessTargetsRpm.htm?target_type=0&targets_lists_name=NMF-"+URL_ADD_BLOCK_BY_URL_PARAM+"&dst_ip_start=&dst_ip_end=&dst_port_start=&dst_port_end=&proto=0&Commonport=0&url_0="+URL_ADD_BLOCK_BY_URL_PARAM+"&url_1=&url_2=&url_3=&Changed=0&SelIndex=0&fromAdd=0&Page=1&Save=Save";
        _urlRouters.put(eUrl.ADD_BLOCK_BY_URL, UrlRouter.createWithReplace(URL_ADD_BLOCK_BY_URL, "userRpm/MenuRpm.htm", URL_ADD_BLOCK_BY_URL_PARAM));

        String URL_REMOVE_BLOCK_BY_URL_PARAM = "[_URL_]";
        String URL_REMOVE_BLOCK_BY_URL = "userRpm/AccessCtrlAccessTargetsRpm.htm?Del="+URL_REMOVE_BLOCK_BY_URL_PARAM+"&Page=1";
        _urlRouters.put(eUrl.REMOVE_BLOCK_BY_URL, UrlRouter.createWithReplace(URL_REMOVE_BLOCK_BY_URL, "userRpm/MenuRpm.htm", URL_REMOVE_BLOCK_BY_URL_PARAM));

        String URL_ADD_BLOCK_BY_URL_RULE_PARAM = "[_URL_]";
        String URL_ADD_BLOCK_BY_URL_RULE = "userRpm/AccessCtrlAccessRulesRpm.htm?rule_name=NMF-TARGET-ID-"+URL_ADD_BLOCK_BY_URL_RULE_PARAM+"&hosts_lists=0&targets_lists="+URL_ADD_BLOCK_BY_URL_RULE_PARAM+"&scheds_lists=255&allow=0&enable=1&Changed=0&SelIndex=0&Page=1&Save=Save";
        _urlRouters.put(eUrl.ADD_BLOCK_BY_URL_RULE, UrlRouter.createWithReplace(URL_ADD_BLOCK_BY_URL_RULE, "userRpm/MenuRpm.htm", URL_ADD_BLOCK_BY_URL_RULE_PARAM));

        String URL_REMOVE_BLOCK_BY_URL_PARAM_RULE = "[_URL_]";
        //String URL_REMOVE_BLOCK_BY_URL_RULE = "userRpm/AccessCtrlAccessTargetsRpm.htm?Del="+URL_REMOVE_BLOCK_BY_URL_PARAM_RULE+"&Page=1";
        String URL_REMOVE_BLOCK_BY_URL_RULE = "userRpm/AccessCtrlAccessRulesRpm.htm?Del="+URL_REMOVE_BLOCK_BY_URL_PARAM_RULE+"&Page=1";
        _urlRouters.put(eUrl.REMOVE_BLOCK_BY_URL_RULE, UrlRouter.createWithReplace(URL_REMOVE_BLOCK_BY_URL_RULE, "userRpm/MenuRpm.htm", URL_REMOVE_BLOCK_BY_URL_PARAM_RULE));


        String HTML_URL_BEGIN_RULE = "var access_rules_data_param = new Array(\n";
        _urlRouters.put(eUrl.GET_URL_LIST_BLOCKED_RULE, UrlRouter.createWithFinder("userRpm/AccessCtrlAccessRulesRpm.htm", "userRpm/MenuRpm.htm", HTML_URL_BEGIN_RULE, "0,0 );", "ERROR HTML_URL_BEGIN_RULE"));



    }

    public UrlRouter get(eUrl eUrl){
        return _urlRouters.get(eUrl);
    }


}
