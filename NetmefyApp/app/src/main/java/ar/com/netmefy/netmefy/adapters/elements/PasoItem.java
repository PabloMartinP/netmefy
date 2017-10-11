package ar.com.netmefy.netmefy.adapters.elements;

/**
 * Created by ignac on 11/10/2017.
 */

public class PasoItem {
    private String desc1;
    private String desc2;
    private String desc3;
    private Boolean done;
    private Boolean isPruebasDeRed;
    private Integer resId;

    public PasoItem() {
    }

    public PasoItem(String desc1, String desc2, String desc3, Boolean done, Boolean isPruebasDeRed, Integer resId) {
        this.desc1 = desc1;
        this.desc2 = desc2;
        this.desc3 = desc3;
        this.done = done;
        this.isPruebasDeRed = isPruebasDeRed;
        this.resId = resId;
    }

    public Integer getResId() {
        return resId;
    }

    public void setResId(Integer resId) {
        this.resId = resId;
    }

    public String getDesc1() {
        return desc1;
    }

    public void setDesc1(String desc1) {
        this.desc1 = desc1;
    }

    public String getDesc2() {
        return desc2;
    }

    public void setDesc2(String desc2) {
        this.desc2 = desc2;
    }

    public String getDesc3() {
        return desc3;
    }

    public void setDesc3(String desc3) {
        this.desc3 = desc3;
    }

    public Boolean getDone() {
        return done;
    }

    public void setDone(Boolean done) {
        this.done = done;
    }

    public Boolean getPruebasDeRed() {
        return isPruebasDeRed;
    }

    public void setPruebasDeRed(Boolean pruebasDeRed) {
        isPruebasDeRed = pruebasDeRed;
    }
}
