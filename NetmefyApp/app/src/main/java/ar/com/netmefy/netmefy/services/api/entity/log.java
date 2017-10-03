package ar.com.netmefy.netmefy.services.api.entity;

/**
 * Created by fiok on 02/10/2017.
 */

public class log {
    public int log_sk;
    public int cliente_sk;
    public int log_tipo;
    public String log_desc;
    public log(int tipo, int cliente_sk, String desc){
        this.log_tipo = tipo;
        this.log_desc = desc;
        this.cliente_sk = cliente_sk;
    }
}
