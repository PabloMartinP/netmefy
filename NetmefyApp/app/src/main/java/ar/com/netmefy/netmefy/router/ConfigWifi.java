package ar.com.netmefy.netmefy.router;

/**
 * Created by fiok on 24/06/2017.
 */

public class ConfigWifi {
    private String ssid;
    private String password;


    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ConfigWifi(String ssid, String password){
        this.ssid = ssid;
        this.password = password;
    }
    public ConfigWifi(){

    }
}
