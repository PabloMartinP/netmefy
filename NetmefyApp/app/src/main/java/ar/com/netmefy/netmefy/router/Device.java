package ar.com.netmefy.netmefy.router;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import ar.com.netmefy.netmefy.adapters.elements.DeviceItem;
import ar.com.netmefy.netmefy.services.api.entity.DeviceModel;
import ar.com.netmefy.netmefy.services.api.entity.dispositivoInfo;

/**
 * Created by fiok on 24/06/2017.
 */

public class Device {
    private String name;
    private String ip;
    private String mac;
    private int id;
    private boolean blocked;

    public int getId() {
        return id;
    }


    /*
    * Usado para los TPLINK, al elimiar hay que pasar este ID*/
    public void setId(int id) {
        this.id = id;
    }

    /*
        example: "fiok-PC", "C4-6E-1F-22-49-D1", "192.168.0.100", "01:13:02"
         */
    public static Device newFromString(String stringdevice){
        String[] fields = stringdevice.split(", ");
        Device device = new Device();
        device.setName(fields[0].replace("\"", ""));
        device.setMac(fields[1].replace("\"", ""));
        device.setIp(fields[2].replace("\"", ""));
        return device;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac.toUpperCase();
    }

    public DeviceItem toDeviceItem() {
        DeviceItem d = new DeviceItem();
        d.setName(this.getName());
        d.setBlocked(this.isBlocked());
        d.setMac(this.getMac());
        d.setTipoDeDispostivo("un tipo 123");
        d.setResId(2130837670);
        return  d;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }


    public DeviceModel toDeviceModel() {
        DeviceModel model = new DeviceModel();
        model.dispositivo_apodo = this.getName();
        model.dispositivo_bloq = 0;
        model.dispositivo_ip = this.getIp();
        model.dispositivo_mac = this.getMac();

        return model;
    }

    public dispositivoInfo toDispositivoInfo() {
        dispositivoInfo di = new dispositivoInfo();
        di.apodo = this.getName();
        di.bloqueado = false;
        di.ip = this.getIp();
        di.mac   = this.getMac();
        di.tipo = "tipo 123";
        return di;
    }
}
