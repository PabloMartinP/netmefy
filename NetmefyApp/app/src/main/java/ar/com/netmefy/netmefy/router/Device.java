package ar.com.netmefy.netmefy.router;

/**
 * Created by fiok on 24/06/2017.
 */

public class Device {
    private String name;
    private String ip;
    private String mac;

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
        this.mac = mac;
    }
}
