package ar.com.netmefy.netmefy.services.api.entity;

import ar.com.netmefy.netmefy.adapters.elements.WebPageToBlockItem;

/**
 * Created by fiok on 30/09/2017.
 */

public class paginaControlParentalModel {
    public String ip;
    public int id;
    public String nombre;
    public String url;
    public int resId;
    public boolean bloqueado;

    public WebPageToBlockItem toWebPageToBlockItem() {
        WebPageToBlockItem res = new WebPageToBlockItem();
        res.setId(this.id);
        res.setName(this.nombre);
        res.setBlocked(this.bloqueado);
        res.setResId(this.resId);
        res.setUrl(this.url);
        res.setReadOnly(false);

        return res;
    }

    public webABloquearModel toWebBlockModel() {
        webABloquearModel res = new webABloquearModel();
        res.web_bloqueado = this.bloqueado;
        res.web_sk = this.id;
        return res;
    }
}
