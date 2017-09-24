package ar.com.netmefy.netmefy.adapters.elements;

/**
 * Created by ignac on 23/9/2017.
 */

public class WebPageToBlockItem {
    private String name;
    private String url;
    private Boolean blocked;
    private int resId;

    public WebPageToBlockItem() {
    }

    public WebPageToBlockItem(String name, String url, int resId, Boolean blocked) {
        this.name = name;
        this.url = url;
        this.blocked = blocked;
        this.resId = resId;
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
}
