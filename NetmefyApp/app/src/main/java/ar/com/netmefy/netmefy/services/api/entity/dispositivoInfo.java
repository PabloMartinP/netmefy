package ar.com.netmefy.netmefy.services.api.entity;

import android.support.annotation.DrawableRes;

import ar.com.netmefy.netmefy.adapters.elements.DeviceItem;

public class dispositivoInfo{
    public int dispositivo_sk;
    public String mac;
    public String ip;
    public String tipo;
    public boolean bloqueado;
    public String foto;
    public String apodo;

    public@DrawableRes
    int resId = 2130837670;//para la imagen del tipo

    public boolean is_conectado() {
        return _conectado;
    }

    public void set_conectado(boolean _conectado) {
        this._conectado = _conectado;
    }

    private boolean _conectado;


    public DeviceModel toDeviceModel() {
        DeviceModel dm = new DeviceModel();
        if(this.bloqueado)
            dm.dispositivo_bloq = 1;
        else
            dm.dispositivo_bloq = 0;
        dm.dispositivo_mac = this.mac;
        dm.dispositivo_ip = this.ip;
        dm.dispositivo_tipo = this.tipo;
        dm.dispositivo_apodo = this.apodo;
        dm.dispositivo_sk = this.dispositivo_sk;
        return dm;

    }

    public DeviceItem toDeviceItem() {
        DeviceItem d = new DeviceItem();
        d.setName(this.apodo);
        d.setBlocked(this.bloqueado);
        d.setMac(this.mac);
        d.setTipoDeDispostivo(this.tipo);
        d.setResId(this.resId);
        return  d;
    }
}
