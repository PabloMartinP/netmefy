package ar.com.netmefy.netmefy.services.api.entity;

import java.util.List;

import ar.com.netmefy.netmefy.adapters.elements.OtItem;

/**
 * Created by ignac on 11/10/2017.
 */

public class Tecnico {
    private  Integer sk ;
    private String id ;
    private String nombre ;
    private String email;
    private Float calificacion ;
    private OtItem[] ots ;

    public OtItem buscarOt(int ot_id){
        for (OtItem ot : ots) {
            if(ot.getOt_id() == ot_id)
                return ot;
        }
        return null;
    }

    public Tecnico() {
    }

    public Tecnico(Integer sk, String id, String nombre, String email, Float calificacion, OtItem[] ots) {
        this.sk = sk;
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.calificacion = calificacion;
        this.ots = ots;
    }

    public Integer getSk() {
        return sk;
    }

    public void setSk(Integer sk) {
        this.sk = sk;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Float getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(Float calificacion) {
        this.calificacion = calificacion;
    }

    public OtItem[] getOts() {
        return ots;
    }

    public void setOts(OtItem[] ots) {
        this.ots = ots;
    }
}
