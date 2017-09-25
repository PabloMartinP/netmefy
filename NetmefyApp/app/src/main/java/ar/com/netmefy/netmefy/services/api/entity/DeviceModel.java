package ar.com.netmefy.netmefy.services.api.entity;

/**
 * Created by fiok on 23/09/2017.
 */

public class DeviceModel {
    public int cliente_sk;
    public int router_sk;
    public int dispositivo_sk ;
    public String dispositivo_mac;
    public String dispositivo_ip;
    public String dispositivo_apodo;
    public String dispositivo_tipo;
    public int dispositivo_bloq;

    public dispositivoInfo toDispositivoInfo() {
        dispositivoInfo dispositivoInfo = new dispositivoInfo();
        dispositivoInfo.bloqueado = this.dispositivo_bloq != 0;
        dispositivoInfo.ip = this.dispositivo_ip;
        dispositivoInfo.mac = this.dispositivo_mac;
        dispositivoInfo.apodo = this.dispositivo_apodo;
        dispositivoInfo.tipo = this.dispositivo_tipo;
        return dispositivoInfo;
    }
}
