package ar.com.netmefy.netmefy.router;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.Response;

import ar.com.netmefy.netmefy.router.tplink.TLWR941ND.StringRequestRouter;

/**
 * Created by fiok on 24/06/2017.
 */

public abstract class Router {



    protected  Context _context;
    protected RequestQueue _queue ;

    public void execute(StringRequestRouter stringRequest){
        _queue.add(stringRequest);

    }

    public  abstract void restart(Response.Listener listener, Response.ErrorListener errorListener);

    public  abstract void restartAndWaitUntilConnected(Response.Listener listener, Response.ErrorListener errorListener);

    public abstract void getWifiSsid(Response.Listener listener, Response.ErrorListener errorListener);
    public abstract void getWifiPassword(Response.Listener listener, Response.ErrorListener errorListener);

    public abstract void setWifiSsid(String newSsid, Response.Listener listener, Response.ErrorListener errorListener);
    public abstract void setWifiPassword(String newPassword, Response.Listener listener, Response.ErrorListener errorListener);


    public abstract void setConfigWifiAndRestart(ConfigWifi configWifi, Response.Listener listener, Response.ErrorListener errorListener) ;
    public abstract void setConfigWifi(ConfigWifi configWifi, Response.Listener listener, Response.ErrorListener errorListener);
    public abstract void getConfigWifi(Response.Listener listener, Response.ErrorListener errorListener);


    public abstract void listDevicesConnected(Response.Listener listener, Response.ErrorListener errorListener) ;


}
