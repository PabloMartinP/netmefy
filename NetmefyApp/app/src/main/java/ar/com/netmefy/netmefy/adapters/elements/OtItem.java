package ar.com.netmefy.netmefy.adapters.elements;

/**
 * Created by ignac on 11/10/2017.
 */

public class OtItem {
    private int ot_id;
    private Integer estado;
    private String estado_desc;
    private String fecha;

    public int getCliente_sk() {
        return cliente_sk;
    }

    public void setCliente_sk(int cliente_sk) {
        this.cliente_sk = cliente_sk;
    }

    private int cliente_sk;
    private String cliente_desc;
    private String cliente_direccion;
    private String cliente_tipo_casa;
    private String tipo_ot;

    public OtItem() {
    }

    public OtItem(int ot_id, Integer estado, String estado_desc, String fecha, String nombreCliente, String direccion, String tipoDeVivienda, String tipo_ot, int cliente_sk) {
        this.ot_id = ot_id;
        this.estado = estado;
        this.estado_desc = estado_desc;
        this.fecha = fecha;
        this.cliente_sk = cliente_sk;
        this.cliente_desc = nombreCliente;
        this.cliente_direccion = direccion;
        this.cliente_tipo_casa = tipoDeVivienda;
        this.tipo_ot = tipo_ot;
    }

    public String getTipo_ot() {
        return tipo_ot;
    }

    public void setTipo_ot(String tipo_ot) {
        this.tipo_ot = tipo_ot;
    }

    public String getCliente_tipo_casa() {
        return cliente_tipo_casa;
    }

    public void setCliente_tipo_casa(String cliente_tipo_casa) {
        this.cliente_tipo_casa = cliente_tipo_casa;
    }

    public String getCliente_desc() {
        return cliente_desc;
    }

    public void setCliente_desc(String cliente_desc) {
        this.cliente_desc = cliente_desc;
    }

    public String getCliente_direccion() {
        return cliente_direccion;
    }

    public void setCliente_direccion(String cliente_direccion) {
        this.cliente_direccion = cliente_direccion;
    }

    public int getOt_id() {
        return ot_id;
    }

    public void setOt_id(int ot_id) {
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

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}
