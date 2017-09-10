package ar.com.netmefy.netmefy.services.api.entity;

/**
 * Created by fiok on 10/09/2017.
 */

public class tipoUsuarioApp {
    public String tipo;
    public String username;
    public int id;

    public boolean esCliente(){
        return tipo.equals("c");
    }
    public boolean esTecnico(){
        return !esCliente();
    }
}
