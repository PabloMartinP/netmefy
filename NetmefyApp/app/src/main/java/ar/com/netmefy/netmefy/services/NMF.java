package ar.com.netmefy.netmefy.services;

import android.content.Context;

import com.android.volley.Response;

import java.util.ArrayList;
import java.util.List;

import ar.com.netmefy.netmefy.router.Device;
import ar.com.netmefy.netmefy.services.api.Api;
import ar.com.netmefy.netmefy.services.api.entity.DeviceModel;
import ar.com.netmefy.netmefy.services.api.entity.Tecnico;
import ar.com.netmefy.netmefy.services.api.entity.dispositivoInfo;
import ar.com.netmefy.netmefy.services.api.entity.notificacionModel;
import ar.com.netmefy.netmefy.services.api.entity.reclamoListItemModel;
import ar.com.netmefy.netmefy.services.api.entity.solicitudListItemModel;
import ar.com.netmefy.netmefy.services.login.Session;

/**
 * Created by fiok on 24/09/2017.
 */

public class NMF {

    public  static ar.com.netmefy.netmefy.services.api.entity.clientInfo cliente;
    public static ar.com.netmefy.netmefy.services.api.entity.usuarioInfo usuario;
    public static ar.com.netmefy.netmefy.services.api.entity.tipoUsuarioApp tipoUsuarioApp ;
    public static List<notificacionModel> notificaciones;
    public static List<solicitudListItemModel> solicitudes;
    public static List<reclamoListItemModel> reclamos;

    public static Tecnico tecnico;

    public static void setDevice_sk(String mac, int sk){
        for (dispositivoInfo disp : NMF.cliente.router.devices ) {
            if(disp.mac == mac)
                disp.dispositivo_sk = sk;
        }
    }
    public static void setDevice(dispositivoInfo dispo){
        for (dispositivoInfo di : cliente.router.devices ) {
            if(di.mac.toLowerCase().equals(dispo.mac.toLowerCase())){
                di.tipo = dispo.tipo;
                di.apodo = dispo.apodo;
                di.bloqueado = dispo.bloqueado;
            }
        }
    }
    public static boolean existsDevice(String mac){
        return findDeviceByMac(mac) !=null;
    }
    public static dispositivoInfo findDeviceByMac(String mac){
        for (dispositivoInfo di : cliente.router.devices ) {
            if(di.mac.toLowerCase().equals(mac.toLowerCase()))
                return di;
        }
        return null;
    }

    public static void setDevice_connected(String mac) {
        for (dispositivoInfo di : cliente.router.devices ) {
            if(di.mac.toLowerCase().equals(mac.toLowerCase()))
                di.set_conectado(true);
        }
    }

    public static List<dispositivoInfo> getDevicesConnected(){
        dispositivoInfo d;
        List<dispositivoInfo> list = new ArrayList<>();
        for (dispositivoInfo di : cliente.router.devices ) {
            if(di.is_conectado())
                list.add(di);
        }
        return list;
    }

    public static void updateDevicesConnected(List<Device> devices, Context context) {
        if(NMF.cliente.router.devices == null)
            NMF.cliente.router.devices = new ArrayList<>();

        ///////////////////////////////////////////////////
        for (Device dev : devices) {
            dispositivoInfo di = dev.toDispositivoInfo();
            if(!NMF.existsDevice(di.mac)){
                Api api = Api.getInstance(context);
                //inserto el nuevo dispositivo
                DeviceModel dm  = dev.toDeviceModel();
                dm.cliente_sk = NMF.cliente.id;
                dm.router_sk = NMF.cliente.router.router_sk;
                dm.dispositivo_tipo = "un tipo 123";

                dispositivoInfo dispo_info = dm.toDispositivoInfo();
                dispo_info.set_conectado(true);
                cliente.router.devices.add(dispo_info);

                api.addDevice(dm, new Response.Listener() {
                    @Override
                    public void onResponse(Object response) {
                        DeviceModel n = (DeviceModel) response;
                        //dispositivoInfo di = n.toDispositivoInfo();
                        //cliente.router.devices.add(di);
                        NMF.setDevice_sk(n.dispositivo_mac, n.dispositivo_sk);
                    /*for (dispositivoInfo disp : NMF.cliente.router.devices ) {
                        if(disp.mac == n.dispositivo_mac)
                            disp.dispositivo_sk = n.dispositivo_sk;
                    }*/
                    }
                });
            }else{
                //cargo el tipo, apodo, si esta blocked??creo que no
                NMF.setDevice_connected(dev.getMac());
            }
            //NMF.cliente.router.devices.add(di);
        }

        //session.setClientInfo();//guardo la info de los nuevos equipos de cliente

    }

    public static void marcarNotificacionComoLeida(int notificationId, Context context) {
        for (notificacionModel nm : NMF.notificaciones) {
            if(nm.notificacion_sk == notificationId){
                nm.leido = true;
                break;
            }
        }
        Session session = new Session(context);
        session.setNotificaciones(NMF.notificaciones);

    }


    public static int getCantidadNotificacionesNoLeidas() {
        int cant = 0 ;
        for (notificacionModel n : NMF.notificaciones) {
            if(!n.leido )
                cant ++;
        }
        return cant;
    }
}
