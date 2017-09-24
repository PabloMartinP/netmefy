package ar.com.netmefy.netmefy.services.api;

import android.content.Context;
import android.util.ArrayMap;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONObject;
import org.jsoup.Connection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ar.com.netmefy.netmefy.router.Device;
import ar.com.netmefy.netmefy.services.Utils;
import ar.com.netmefy.netmefy.services.api.entity.DeviceModel;
import ar.com.netmefy.netmefy.services.api.entity.SaveToken;
import ar.com.netmefy.netmefy.services.api.entity.Token;
import ar.com.netmefy.netmefy.services.api.entity.clientInfo;
import ar.com.netmefy.netmefy.services.api.entity.paginasLikeadas;
import ar.com.netmefy.netmefy.services.api.entity.tipoUsuarioApp;
import ar.com.netmefy.netmefy.services.api.stringRequests.JsonRequestApi;
import ar.com.netmefy.netmefy.services.api.stringRequests.RequestQueueSingletonApi;

/**
 * Created by fiok on 21/08/2017.
 */


public  class Api {
    public static Token token;
    public static tipoUsuarioApp tipoUsuarioApp ;
    public  static int cliente_sk;
    public static int usuario_sk;
    public  static clientInfo clientInfo;
    /////////////////////////////////////////////////////////////////////
    private RequestQueue _queue;
    private Api(Context context){
        _queue = RequestQueueSingletonApi.getInstance(context).getRequestQueue();
        //_queue = Volley.newRequestQueue(context);

    }


    private static Api api = null;
    public static Api getInstance(Context context){
        if(api == null)
            api  = new Api(context);


        return api;
    }

    private  void execute(Request rq){
        _queue.add(rq);
    }
    public  void LogIn(String username, String password, final Response.Listener<String> success){
        String url = "http://200.82.0.24/oauth2/token";

        Map<String,String> params = new HashMap<String, String>();
        params.put("grant_type","password");
        params.put("UserName",username);
        params.put("Password",password);
        params.put("client_id","Utn.Ba$");

        JsonRequestApi rq = new JsonRequestApi(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Gson gson = new Gson();
                token = gson.fromJson(response.toString(), Token.class);
                success.onResponse("ok");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                success.onResponse("error:"+error.toString());
            }
        });
        execute(rq);

    }

    public void getTypeOfUser(String username, final Response.Listener<tipoUsuarioApp> success, Response.ErrorListener error ){

        String url = "http://200.82.0.24/api/tipoUsuarioApp?username=" +username;
        JsonRequestApi rq = new JsonRequestApi(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //String rr = response.toString();
                tipoUsuarioApp tipo ;
                Gson gson = new Gson();
                tipo= gson.fromJson(response.toString(), tipoUsuarioApp.class);
                success.onResponse(tipo);
            }
        }, error);
        execute(rq);
    }

    public void getInfoUser(String username, final Response.Listener<clientInfo> success, Response.ErrorListener error ){
        String url = "http://200.82.0.24/api/clientes?username=" +username;
        JsonRequestApi rq = new JsonRequestApi(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                clientInfo client ;
                Gson gson = new Gson();
                client = gson.fromJson(response.toString(), clientInfo.class);
                String jj;

                jj = client.nombre;
                success.onResponse(client);
            }
        }, error);
        execute(rq);
        /*String url = "http://200.82.0.24/api/usuarios" + "/" + String.valueOf(id);
        JsonRequestApi rq = new JsonRequestApi(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String rr;
                rr = response.toString();
            }
        }, error);
        execute(rq);*/
    }


    public void addDevice(final DeviceModel device, final Response.Listener success){
        String url = "http://200.82.0.24/api/dispositivos";
        Map<String, String> data  = null;
        try {
            data = Utils.toMap(device);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        final String dataok = data.toString();

        JsonRequestApi rq = new JsonRequestApi(Request.Method.POST, url, data, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //success.onResponse("ok:"+dataok);
                success.onResponse(device);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Map<String, String> data1 = data    ;
                //success.onResponse("error:"+dataok);
                success.onResponse(null);
            }
        });
        execute(rq);

    }

    public void sendLikes(paginasLikeadas paginasLikeadas, final Response.Listener<String> success){
        String url = "http://200.82.0.24/api/paginas";

        /*paginasLikeadas paginasLikeadas = new paginasLikeadas();
        paginasLikeadas.cliente_sk = 1;
        paginasLikeadas.usuario_sk = 1;
        paginasLikeadas.paginas = likes;*/

        Map<String, String> data  = null;
        try {
            data = paginasLikeadas.toMap();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        final String dataok = data.toString();

        JsonRequestApi rq = new JsonRequestApi(Request.Method.POST, url, data, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                success.onResponse("ok:"+dataok);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Map<String, String> data1 = data    ;
                success.onResponse("error:"+dataok);
            }
        });
        execute(rq);
    }

    public void saveFirebaseToken(String userId, String userType, String token,final Response.Listener<String> success, Response.ErrorListener error) throws IllegalAccessException {
        SaveToken st = new SaveToken();

        st.es_cliente = userType.equalsIgnoreCase("user");
        st.es_tecnico= !userType.equalsIgnoreCase("tech");

        st.setId_entidad(Integer.valueOf(userId));
        st.setTokenid(token);

        String url = UrlApi.SaveToken;
        //String url = "http://200.82.0.24/api/tokens";

        JsonRequestApi rq = new JsonRequestApi(Request.Method.POST, url, st.toMap(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String j;
                j = response.toString();
                success.onResponse("ok");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        execute(rq);
    }
}
