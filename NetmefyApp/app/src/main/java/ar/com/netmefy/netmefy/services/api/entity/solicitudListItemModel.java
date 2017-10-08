package ar.com.netmefy.netmefy.services.api.entity;

import java.util.Date;

import ar.com.netmefy.netmefy.adapters.elements.GestionItem;

/**
 * Created by fiok on 07/10/2017.
 */

public class solicitudListItemModel {
    public int os_id;
    public int cliente_sk;
    public  String fh_creacion;
    public String fh_cierre;
    public String descripcion;
    public String tipo;
    public int tipo_id;

    public GestionItem toGestionItem(){
        GestionItem item = new GestionItem(os_id,tipo,fh_creacion,"EstadoOk", descripcion);
        return  item;

    }
}
