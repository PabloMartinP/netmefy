package ar.com.netmefy.netmefy.adapters.elements;

import ar.com.netmefy.netmefy.services.api.entity.paginaControlParentalModel;

/**
 * Created by ignac on 23/9/2017.
 */

public class WebPageToBlockItem {
    private String name;
    private String url;
    private Boolean blocked;
    private int resId;
    private int id;
    private boolean readOnly = false;
    private int colorOk;

    public WebPageToBlockItem() {
    }

    public WebPageToBlockItem(String name, String url, int resId, Boolean blocked, int id, boolean readOnly) {
        this.name = name;
        this.url = url;
        this.blocked = blocked;
        this.resId = resId;
        this.id = id;
        this.readOnly = readOnly;
    }

    public void setId(int id){
        this.id = id;
    }
    public int getId(){
        return this.id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Boolean getBlocked() {
        return blocked;
    }

    public void setBlocked(Boolean blocked) {
        this.blocked = blocked;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public paginaControlParentalModel toPaginaControlParentalModel() {
        paginaControlParentalModel res = new paginaControlParentalModel();
        res.id = this.getId();
        res.nombre = this.getName();
        res.bloqueado = this.getBlocked();
        res.resid_imagen = this.getResId();
        res.url = this.getUrl();
        return res;
    }

    public boolean getReadOnly() {
        return readOnly;
    }

    public void setReadOnly(boolean value) {
        readOnly = value;
    }

    public void setColorOk(int colorOk) {
        this.colorOk = colorOk;
    }

    public int getColorOk() {
        return colorOk;
    }
}
