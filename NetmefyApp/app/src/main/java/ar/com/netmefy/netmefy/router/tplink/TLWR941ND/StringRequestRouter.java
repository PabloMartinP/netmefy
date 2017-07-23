package ar.com.netmefy.netmefy.router.tplink.TLWR941ND;

import android.util.Base64;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;

import java.util.HashMap;
import java.util.Map;

import ar.com.netmefy.netmefy.router.UrlRouter;

/**
 * Created by fiok on 24/06/2017.
 */

public class StringRequestRouter extends com.android.volley.toolbox.StringRequest {
    //private final String _username = "admin";
    //private final String _password = "admin";
    //private final Map<String, String> _params = null;
    private  final String _referrer;

    public StringRequestRouter(int method, UrlRouter urlRouter,
                               Response.Listener<String> listener,
                               Response.ErrorListener errorListener) {
        super(method, urlRouter.get_url(), listener, errorListener);
        _referrer = urlRouter.get_referrer();
    }
    public StringRequestRouter(int method, String url, String referrer,
                               Response.Listener<String> listener,
                               Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);
        _referrer = referrer;
    }

    /*@Override
    protected Map<String, String> getParams() {
        return _params;
    }*/

    /* (non-Javadoc)
     * @see com.android.volley.Request#getHeaders()
     */
    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {

        Map<String, String> headerMap = new HashMap<>();
        //Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8
        headerMap.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");

        //Accept-Encoding: gzip, deflate, sdch
        headerMap.put("Accept-Encoding", "gzip, deflate, sdch");

        //Accept-Language: es-ES,es;q=0.8,en;q=0.6
        headerMap.put("Accept-Language", "es-ES,es;q=0.8,en;q=0.6");

        //User-Agent: Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36
        headerMap.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36");

        String credentials = "admin:admin";
        String auth = "Basic "+ Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
        headerMap.put("Content-Type", "application/json");
        headerMap.put("Authorization", auth);

        //Referer: http://192.168.1.1/wlcfg.html
        headerMap.put("Referer", _referrer);

        //Upgrade-Insecure-Requests: 1
        headerMap.put("Upgrade-Insecure-Requests", "1");

        //Connection: keep-alive
        headerMap.put("Connection", "keep-alive");

        headerMap.put("Host", "192.168.0.1");

        return headerMap;
    }

}
