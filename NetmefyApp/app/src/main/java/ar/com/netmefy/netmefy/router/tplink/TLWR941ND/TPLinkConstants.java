package ar.com.netmefy.netmefy.router.tplink.TLWR941ND;

/**
 * Created by fiok on 25/06/2017.
 */

public  final class TPLinkConstants {
    final static String URL_LIST_CONNECTED = "http://192.168.0.1/userRpm/AssignedIpAddrListRpm.htm";
    final static String URL_LIST_CONNECTED_REFERRER = "http://192.168.0.1/userRpm/MenuRpm.htm";

    final static String URL_WIFI_GET_SSID = "http://192.168.0.1/userRpm/WlanNetworkRpm.htm";
    final static String URL_WIFI_GET_SSID_REFERRER = "http://192.168.0.1/userRpm/MenuRpm.htm";

    final static String URL_WIFI_GET_PASSWORD = "http://192.168.0.1/userRpm/WlanSecurityRpm.htm";
    final static String URL_WIFI_GET_PASSWORD_REFERRER = "http://192.168.0.1/userRpm/MenuRpm.htm";

    final static String URL_RESTART = "http://192.168.0.1/userRpm/SysRebootRpm.htm?Reboot=Reboot";
    final static String URL_RESTART_REFERRER = "http://192.168.0.1/userRpm/SysRebootRpm.htm";


    final static String URL_WIFI_SET_SSID_PARAM = "[SSID]";
    final static String URL_WIFI_SET_SSID = "http://192.168.0.1/userRpm/WlanNetworkRpm.htm?ssid1="+URL_WIFI_SET_SSID_PARAM+"&region=2&channel=11&mode=5&chanWidth=2&rate=71&ap=1&broadcast=2&brlssid=&brlbssid=&keytype=1&wepindex=1&authtype=1&keytext=&Save=Save";
    final static String URL_WIFI_SET_SSID_REFERRER = "http://192.168.0.1/userRpm/WlanNetworkRpm.htm";

    final static String URL_WIFI_SET_PASSWORD_PARAM = "[PASSWORD]";
    final static String URL_WIFI_SET_PASSWORD = "http://192.168.0.1/userRpm/WlanSecurityRpm.htm?wepSecOpt=1&keytype=1&keynum=1&key1=&length1=0&key2=&length2=0&key3=&length3=0&key4=&length4=0&wpaSecOpt=3&wpaCipher=1&radiusIp=&radiusPort=1812&radiusSecret=&intervalWpa=0&secType=3&pskSecOpt=2&pskCipher=3&pskSecret="+URL_WIFI_SET_PASSWORD_PARAM+"&interval=0&Save=Save";
    final static String URL_WIFI_SET_PASSWORD_REFERRER = "http://192.168.0.1/userRpm/WlanSecurityRpm.htm";


}
