package ar.com.netmefy.netmefy.router;

/**
 * Created by fiok on 22/07/2017.
 */

public class UrlRouter {

    private String _url;
    private String _referrer;
    private static String _urlRoot;

    private String _htmlBefore;
    private String _htmlAfter;

    public String get_htmlBefore() {
        return _htmlBefore;
    }

    public String get_htmlAfter() {
        return _htmlAfter;
    }

    public void set_htmlAfter(String _htmlAfter) {
        this._htmlAfter = _htmlAfter;
    }

    public void set_htmlBefore(String _htmlBefore) {
        this._htmlBefore = _htmlBefore;
    }

    public  static void set_urlRoot(String urlRoot){
        _urlRoot = urlRoot;
    }

    public static UrlRouter create(String url, String referrer){
        UrlRouter athis = new UrlRouter();
        athis.set_url(url);
        athis.set_referrer(referrer);
        return athis;
    }

    public static UrlRouter createWithFinder(String url, String referrer, String htmlBefore, String htmlAfter){
        UrlRouter aThis = new UrlRouter();
        aThis.set_url(url);
        aThis.set_referrer(referrer);
        aThis.set_htmlAfter(htmlAfter);
        aThis.set_htmlBefore(htmlBefore);
        return aThis;
    }
    public String get_url() {
        return _urlRoot.concat(_url);
    }

    public void set_url(String _url) {
        this._url = _url;
    }

    public String get_referrer() {
        return _urlRoot.concat(_referrer);
    }

    public void set_referrer(String _referrer) {
        this._referrer = _referrer;
    }


}
