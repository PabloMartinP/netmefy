package ar.com.netmefy.netmefy.services.api.entity;

import ar.com.netmefy.netmefy.adapters.elements.GestionItem;

/**
 * Created by fiok on 07/10/2017.
 */

public class reclamoListItemModel {
    public int ot_id;
    public int cliente_sk;
    public int tecnico_sk;
    public String fh_creacion;
    public String fh_cierre;
    public int calificacion;
    public String tipo;
    public String descripcion;

    public GestionItem toGestionItem() {
        GestionItem item = new GestionItem(ot_id,tipo,fh_creacion,"EstadoOk", descripcion);
        return  item;
    }
}
