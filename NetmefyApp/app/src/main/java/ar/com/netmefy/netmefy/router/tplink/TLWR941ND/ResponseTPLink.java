package ar.com.netmefy.netmefy.router.tplink.TLWR941ND;

import java.util.ArrayList;
import java.util.List;

import ar.com.netmefy.netmefy.router.Device;

/**
 * Created by fiok on 24/06/2017.
 */

public  class ResponseTPLink {

    private  static List<Device>  listDevices = null;
    private ResponseTPLink(){

    }
    public  static List<Device> getListDevices(){
        return listDevices;
    }
    public  static List<String> getListDevicesString(){
        List<String> list = new ArrayList<String>();


        for (final Device device: ResponseTPLink.getListDevices()) {
            String deviceString = "Nombre: " + device.getName() + ", Ip: " + device.getIp() + ", Mac: " + device.getMac();
            list.add(deviceString);
        }
        return list;
    }

    public  static List<Device> setListDevices(String response){
        String result = response;
        String find = "var DHCPDynList = new Array(\n";
        int pinit;
        pinit = result.indexOf(find) + find.length();
        int pend;
        pend = result.indexOf("</SCRIPT>");

        String aux ;
        aux = result.substring(pinit, pend);

        String[] devicesString = aux.split("\n");
        List<Device> listDevicesaux = new ArrayList<Device>();
        Device device;
        for (int i=0;i<devicesString.length-1;i++){
            device = Device.newFromString(devicesString[i]);
            listDevicesaux.add(device);
        }

        listDevices = listDevicesaux;
        return listDevices;
    }




}
