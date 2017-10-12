package ar.com.netmefy.netmefy.services.api.entity;

import java.math.BigDecimal;

import ar.com.netmefy.netmefy.adapters.elements.NotificationItem;

/**
 * Created by fiok on 05/10/2017.
 */

public class notificacionModel {

    public int usuario_sk ;
    public int cliente_sk ;
    public int notificacion_sk ;
    //public System.DateTime tiempo_sk { get; set; }
    public String tiempo_sk;
    public String notificacion_desc ;
    public String notificacion_texto ;
    public boolean leido = false;
    public String ot_id ;
    public double    ot_calificacion ;

    public NotificationItem toNotificacionItem() {
        NotificationItem rs = new NotificationItem(this.notificacion_sk, this.notificacion_desc, this.notificacion_texto, this.tiempo_sk, 2130837588, leido, ot_id);
//(int id,String title, String descripcion, String fecha, int resId, Boolean read, String ordenDeTrabajo) {
/*        rs.setResId(2130837588);
        rs.setDescripcion(this.notificacion_desc);
        rs.setFecha(this.tiempo_sk);
        rs.setId( this.notificacion_sk);
        rs.setOrdenDeTrabajo("0");
        rs.setRead(false);*/
        return  rs;
    }
}
