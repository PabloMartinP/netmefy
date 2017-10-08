package ar.com.netmefy.netmefy.adapters.elements;

import java.util.Date;

/**
 * Created by ignac on 5/10/2017.
 */

public class GestionItem {
    private int idDeGestion;
    private String tipoDeGestion;
    private String fechaDeGestion;
    private String estadoDeGestion;
    private String descripcionDeGestion;

    public GestionItem() {
    }

    public GestionItem(int idDeGestion, String tipoDeGestion, String fechaDeGestion, String estadoDeGestion, String descripcionDeGestion) {
        this.idDeGestion = idDeGestion;
        this.tipoDeGestion = tipoDeGestion;
        this.fechaDeGestion = fechaDeGestion;
        this.estadoDeGestion = estadoDeGestion;
        this.descripcionDeGestion = descripcionDeGestion;
    }

    public int getIdDeGestion() {
        return idDeGestion;
    }

    public void setIdDeGestion(int idDeGestion) {
        this.idDeGestion = idDeGestion;
    }

    public String getTipoDeGestion() {
        return tipoDeGestion;
    }

    public void setTipoDeGestion(String tipoDeGestion) {
        this.tipoDeGestion = tipoDeGestion;
    }

    public String getFechaDeGestion() {
        return fechaDeGestion;
    }

    public void setFechaDeGestion(String fechaDeGestion) {
        this.fechaDeGestion = fechaDeGestion;
    }

    public String getEstadoDeGestion() {
        return estadoDeGestion;
    }

    public void setEstadoDeGestion(String estadoDeGestion) {
        this.estadoDeGestion = estadoDeGestion;
    }

    public String getDescripcionDeGestion() {
        return descripcionDeGestion;
    }

    public void setDescripcionDeGestion(String descripcionDeGestion) {
        this.descripcionDeGestion = descripcionDeGestion;
    }
}
