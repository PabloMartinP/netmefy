package ar.com.netmefy.netmefy.router;

/**
 * Created by fiok on 22/07/2017.
 */

public class UrlRouter {
    private static String _urlRoot;

    private String _url;
    private String _referrer;
    private String _htmlBefore;
    private String _htmlAfter;
    private String _valueToReplace;
    private String _valueToReplace2 = "";

    private String _textOnError;

    private String _sessionKey = "";

    public String get_textOnError() {
        return _textOnError;
    }

    public void set_textOnError(String _textOnError) {
        this._textOnError = _textOnError;
    }

    public String get_newValue() {
        return _newValue;
    }

    public void set_newValue(String _newValue) {
        this._newValue = _newValue;
    }

    public void set_newValue2(String _newValue) {
        this._newValue2 = _newValue;
    }
    public String get_newValue2() {
        return _newValue2;
    }

    private String _newValue;
    private String _newValue2 = "";


    public String get_valueToReplace() {
        return _valueToReplace;
    }
    public String get_valueToReplace2() {
        return _valueToReplace2;
    }

    public void set_valueToReplace(String _valueToReplace) {
        this._valueToReplace = _valueToReplace;
    }
    public void set_valueToReplace2(String _valueToReplace) {
        this._valueToReplace2 = _valueToReplace;
    }

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
        UrlRouter aThis = new UrlRouter();
        aThis.set_url(url);
        aThis.set_referrer(referrer);
        return aThis;
    }

    public static UrlRouter createWithFinder(String url, String referrer, String htmlBefore, String htmlAfter, String textOnError){
        UrlRouter aThis = UrlRouter.create(url, referrer);

        aThis.set_htmlAfter(htmlAfter);
        aThis.set_htmlBefore(htmlBefore);
        aThis.set_textOnError(textOnError);
        return aThis;
    }
    public static UrlRouter createWithFinderAndReplace(String url, String referrer, String htmlBefore, String htmlAfter, String textOnError, String valueToReplace){
        UrlRouter aThis = UrlRouter.createWithFinder(url, referrer, htmlBefore, htmlAfter, textOnError);
        aThis.set_valueToReplace(valueToReplace);
        return aThis;
    }

    /*
    * Usado para hacer un SET*/
    public static UrlRouter createWithReplace(String url, String referrer, String valueToReplace){
        UrlRouter aThis = UrlRouter.create(url, referrer);
        aThis.set_valueToReplace(valueToReplace);
        //aThis.set_newValue(newValue);
        return aThis;
    }
    public static UrlRouter createWithReplace2(String url, String referrer, String valueToReplace, String valueToReplace2){
        UrlRouter aThis = UrlRouter.create(url, referrer);
        aThis.set_valueToReplace(valueToReplace);
        aThis.set_valueToReplace2(valueToReplace2);
        //aThis.set_newValue(newValue);
        return aThis;
    }

    private  boolean needReplace(){
        return _valueToReplace !=null && !_valueToReplace.isEmpty() && this._newValue!=null && !this._newValue.isEmpty();
    }

    private boolean needSessionKey(){
        return !_sessionKey.isEmpty();
    }
    private String appendSessionKey(String url1){
        if(needSessionKey()){
            if(!url1.substring(url1.length()-1).equals("&") && !url1.substring(url1.length()-1).equals("?"))
                url1 = url1.concat("&");


            url1 = url1.concat("sessionKey=" + _sessionKey);
            return url1;
        }else
            return url1 ;

    }
    public String get_url() {
        if(!needReplace() )
            return _urlRoot.concat(appendSessionKey(_url));
        else{
            String url;
            url = _url.replace(this.get_valueToReplace(), this.get_newValue() );
            url = url.replace(this.get_valueToReplace2(), this.get_newValue2() );
            String url1 = "";

            if(needSessionKey()){
                url1 = _urlRoot.concat(url);//.concat("sessionKey=" + _sessionKey);
                url1= appendSessionKey(url1);

            }
            else
                url1 = _urlRoot.concat(url);

            return url1;
        }
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

    public void addSessionKey(String sessionKey){
        //String sessionKeyQueryString = "sessionKey=" + sessionKey;
        //this.appendToUrl(sessionKeyQueryString);
        _sessionKey = sessionKey;
    }
    /*public void appendToUrl(String sessionKeyQueryString) {
        set_url( _url.concat(sessionKeyQueryString));
    }*/
}
