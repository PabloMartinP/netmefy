package ar.com.netmefy.netmefy.adapters.elements;

import java.util.Date;

/**
 * Created by ignac on 11/10/2017.
 */

public class OtItem {
    private Integer ot_id;
    private Integer estado;
    private String estado_desc;
    private Date fecha;
    private String nombreCliente;
    private String direccion;
    private String tipoDeVivienda;
    private String tipoDeGestion;

    public OtItem() {
    }

    public OtItem(Integer ot_id, Integer estado, String estado_desc, Date fecha, String nombreCliente, String direccion, String tipoDeVivienda, String tipoDeGestion) {
        this.ot_id = ot_id;
        this.estado = estado;
        this.estado_desc = estado_desc;
        this.fecha = fecha;
        this.nombreCliente = nombreCliente;
        this.direccion = direccion;
        this.tipoDeVivienda = tipoDeVivienda;
        this.tipoDeGestion = tipoDeGestion;
    }

    public String getTipoDeGestion() {
        return tipoDeGestion;
    }

    public void setTipoDeGestion(String tipoDeGestion) {
        this.tipoDeGestion = tipoDeGestion;
    }

    public String getTipoDeVivienda() {
        return tipoDeVivienda;
    }

    public void setTipoDeVivienda(String tipoDeVivienda) {
        this.tipoDeVivienda = tipoDeVivienda;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Integer getOt_id() {
        return ot_id;
    }

    public void setOt_id(Integer ot_id) {
        this.ot_id = ot_id;
    }

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }

    public String getEstado_desc() {
        return estado_desc;
    }

    public void setEstado_desc(String estado_desc) {
        this.estado_desc = estado_desc;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
}
