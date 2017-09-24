package ar.com.netmefy.netmefy.services.api.entity;

import java.util.List;

public  class routerInfo{
    public int router_sk;
    public String modelo;
    public String ssid;
    public String password;
    public List<dispositivoInfo> dispositivos;

    public List<webModel> webs_bloqueadas;
}
