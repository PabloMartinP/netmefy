package ar.com.netmefy.netmefy.adapters.elements;

/**
 * Created by ignac on 22/9/2017.
 */

public class DeviceItem {
    private String mac;
    private String name;
    private int resId;
    private Boolean blocked;

    public DeviceItem() {
    }

    public DeviceItem(String mac, String name, int resId, Boolean blocked) {
        this.mac = mac;
        this.name = name;
        this.resId = resId;
        this.blocked = blocked;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public Boolean getBlocked() {
        return blocked;
    }

    public void setBlocked(Boolean blocked) {
        this.blocked = blocked;
    }
}
