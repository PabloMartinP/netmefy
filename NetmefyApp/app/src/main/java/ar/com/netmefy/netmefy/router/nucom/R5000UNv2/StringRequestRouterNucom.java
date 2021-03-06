package ar.com.netmefy.netmefy.router.nucom.R5000UNv2;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;

import java.util.HashMap;
import java.util.Map;

import ar.com.netmefy.netmefy.router.UrlRouter;

/**
 * Created by fiok on 09/07/2017.
 */

public class StringRequestRouterNucom extends com.android.volley.toolbox.StringRequest {

    static Map<String, String> _params = null;
    static String _referrer;
    static String _cookies = "Name=0admin; path=/";
    public StringRequestRouterNucom( UrlRouter urlRouter, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Request.Method.GET, urlRouter.get_url(), listener, errorListener);
        _referrer = urlRouter.get_referrer();
    }


    public Map<String, String> getHeadersGetSsid() {
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");

        //Accept-Encoding: gzip, deflate, sdch
        headerMap.put("Accept-Encoding", "gzip, deflate, sdch");

        //Accept-Language: es-ES,es;q=0.8,en;q=0.6
        headerMap.put("Accept-Language", "es-ES,es;q=0.8,en;q=0.6");

        //User-Agent: Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36
        headerMap.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36");

        //Cookie: Name=0admin
        //headerMap.put("Cookie", "Name=0admin");
        headerMap.put("Cookie", _cookies);

        //Referer: http://192.168.1.1/wlcfg.html
        //String referrer = "http://192.168.1.1/menu.html";
        headerMap.put("Referer", _referrer);

        //Upgrade-Insecure-Requests: 1
        headerMap.put("Upgrade-Insecure-Requests", "1");

        //Connection: keep-alive
        headerMap.put("Connection", "keep-alive");

        headerMap.put("Host", "192.168.1.1");

        return headerMap;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return getHeadersGetSsid();
    }



    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        // since we don't know which of the two underlying network vehicles
        // will Volley use, we have to handle and store session cookies manually

        Map<String, String> responseHeaders = response.headers;

        if(responseHeaders.get("Set-Cookie")!= null)
            _cookies = responseHeaders.get("Set-Cookie");

        _params = response.headers;

        return super.parseNetworkResponse(response);

    }

}
