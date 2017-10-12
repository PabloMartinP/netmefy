package ar.com.netmefy.netmefy.services.api;

import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ar.com.netmefy.netmefy.services.NMF_Info;
import ar.com.netmefy.netmefy.services.Utils;
import ar.com.netmefy.netmefy.services.api.entity.DeviceModel;
import ar.com.netmefy.netmefy.services.api.entity.SaveToken;
import ar.com.netmefy.netmefy.services.api.entity.Token;
import ar.com.netmefy.netmefy.services.api.entity.clientInfo;
import ar.com.netmefy.netmefy.services.api.entity.dispositivoInfo;
import ar.com.netmefy.netmefy.services.api.entity.log;
import ar.com.netmefy.netmefy.services.api.entity.notificacionModel;
import ar.com.netmefy.netmefy.services.api.entity.osModel;
import ar.com.netmefy.netmefy.services.api.entity.otModel;
import ar.com.netmefy.netmefy.services.api.entity.paginaControlParentalModel;
import ar.com.netmefy.netmefy.services.api.entity.paginasLikeadas;
import ar.com.netmefy.netmefy.services.api.entity.reclamoListItemModel;
import ar.com.netmefy.netmefy.services.api.entity.routerInfo;
import ar.com.netmefy.netmefy.services.api.entity.gestionAddModel;
import ar.com.netmefy.netmefy.services.api.entity.solicitudListItemModel;
import ar.com.netmefy.netmefy.services.api.entity.tipoOsModel;
import ar.com.netmefy.netmefy.services.api.entity.tipoOtModel;
import ar.com.netmefy.netmefy.services.api.entity.tipoUsuarioApp;
import ar.com.netmefy.netmefy.services.api.entity.usuarioAddModel;
import ar.com.netmefy.netmefy.services.api.entity.usuarioInfo;
import ar.com.netmefy.netmefy.services.api.entity.webABloquearModel;
import ar.com.netmefy.netmefy.services.api.entity.webBloqModel;
import ar.com.netmefy.netmefy.services.api.stringRequests.JsonArrayRequestApi;
import ar.com.netmefy.netmefy.services.api.stringRequests.JsonRequestApi;
import ar.com.netmefy.netmefy.services.api.stringRequests.RequestQueueSingletonApi;

/**
 * Created by fiok on 21/08/2017.
 */


public  class Api {
    public static Token token;


    /////////////////////////////////////////////////////////////////////
    private RequestQueue _queue;
    public Context _context;
    private Api(Context context){
        _queue = RequestQueueSingletonApi.getInstance(context).getRequestQueue();
        _context = context;
        //_queue = Volley.newRequestQueue(context);

    }


    private static Api api = null;
    public static Api getInstance(Context context){
        if(api == null)
            api  = new Api(context);


        return api;
    }

    private  void execute(Request rq){
        int socketTimeout = 30000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        rq.setRetryPolicy(policy);
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


    public void updateDevice(final DeviceModel device, final Response.Listener success){
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
                dispositivoInfo dispositivoInfo ;
                Gson gson = new Gson();
                dispositivoInfo= gson.fromJson(response.toString(), dispositivoInfo.class);
                //SOLO ME INTERESA LA SK, LOS DEMAS CAMPOS NO SE CARGAN PORQUE SE LLAMAN DISTINTO

                device.dispositivo_sk    =dispositivoInfo.dispositivo_sk;
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
                dispositivoInfo dispositivoInfo ;
                Gson gson = new Gson();
                dispositivoInfo= gson.fromJson(response.toString(), dispositivoInfo.class);
                //SOLO ME INTERESA LA SK, LOS DEMAS CAMPOS NO SE CARGAN PORQUE SE LLAMAN DISTINTO

                device.dispositivo_sk    =dispositivoInfo.dispositivo_sk;
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

    public void saveFirebaseToken(int cliente_sk, String userType, String token,final Response.Listener<String> success, Response.ErrorListener error) throws IllegalAccessException {
        SaveToken st = new SaveToken();

        st.es_cliente = userType.equalsIgnoreCase("user");
        st.es_tecnico= !userType.equalsIgnoreCase("tech");

        st.setSk_entidad(cliente_sk);
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

    public void addUser(usuarioAddModel user, final Response.Listener success){
        String url = "http://200.82.0.24/api/usuarios";
        Map<String, String> data  = null;
        try {
            data = Utils.toMap(user);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        JsonRequestApi rq = new JsonRequestApi(Request.Method.POST, url, data, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String status="";
                usuarioAddModel actualUser = null;
                try {
                    status = response.get("status").toString();
                    Gson gson = new Gson();
                    actualUser = gson.fromJson(response.getString("usuario"), usuarioAddModel.class);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                success.onResponse(actualUser);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Map<String, String> data1 = data    ;
                success.onResponse(null);
            }
        });
        execute(rq);
    }

    public void findUser(int usuario_sk, final Response.Listener success) {
        String url = "http://200.82.0.24/api/usuarios/" + String.valueOf(usuario_sk);

        JsonRequestApi rq = new JsonRequestApi(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                usuarioInfo usuarioInfo ;
                Gson gson = new Gson();
                usuarioInfo = gson.fromJson(response.toString(), usuarioInfo.class);
                success.onResponse(usuarioInfo);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Map<String, String> data1 = data    ;
                success.onResponse(null);
            }
        });
        execute(rq);
    }

    public void addSolicitud(gestionAddModel sol, final Response.Listener success ){
        String url = "http://200.82.0.24/api/solicitudes";
        /*solicitudModel solicitudModel = new solicitudModel();
        solicitudModel.cliente_sk = client_sk;
        solicitudModel.os_id = 0;
        solicitudModel.fh_cierre = "";
        solicitudModel.fh_creacion = "";*/

        Map<String, String> data  = null;
        try {
            data = Utils.toMap(sol);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }


        JsonRequestApi rq = new JsonRequestApi(Request.Method.POST, url, data, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //Gson gson = new Gson();
                //usuarioInfo = gson.fromJson(response.toString(), usuarioInfo.class);
                success.onResponse("ok");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Map<String, String> data1 = data    ;
                success.onResponse(null);
            }
        });
        execute(rq);

    }

    public void addReclamo(gestionAddModel g, final Response.Listener success) {
        String url = "http://200.82.0.24/api/ot";
        /*osModel solicitudModel = new osModel();
        solicitudModel.cliente_sk = client_sk;
        solicitudModel.fh_cierre = "";
        solicitudModel.fh_creacion = "";*/

        Map<String, String> data  = null;
        try {
            data = Utils.toMap(g);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }


        JsonRequestApi rq = new JsonRequestApi(Request.Method.POST, url, data, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //Gson gson = new Gson();
                //usuarioInfo = gson.fromJson(response.toString(), usuarioInfo.class);
                success.onResponse("ok");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Map<String, String> data1 = data    ;
                success.onResponse(null);
            }
        });
        execute(rq);
    }

    public void getListBlockedPage(final Response.Listener success) {
        String url = "http://200.82.0.24/api/web";

        StringRequest sr = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONArray listJson = null;
                try {
                    listJson = new JSONArray(response);
                    List<paginaControlParentalModel> result = new ArrayList<>();
                    Gson gson = new Gson();
                    for (int i = 0; i < listJson.length(); i++) {
                        JSONObject jo = listJson.getJSONObject(i);
                        paginaControlParentalModel p = gson.fromJson(jo.toString(), paginaControlParentalModel.class);
                        //p.ip = p.ip + ",,";
                        result.add(p);
                    }
                    success.onResponse(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                success.onResponse(null);
            }
        });
        execute(sr);
        /*JsonRequestApi rq = new JsonRequestApi(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Gson gson = new Gson();
                List<paginaControlParentalModel> p = gson.fromJson(response.toString(), List.class);
                success.onResponse(p);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Map<String, String> data1 = data    ;
                success.onResponse(null);
            }
        });
        execute(rq);*/
    }

    public void BlockPages(int cliente_sk, int router_sk, List<paginaControlParentalModel> paginasModel, final Response.Listener success) {
        webBloqModel model = new webBloqModel();
        model.cliente_sk = cliente_sk;
        model.router_sk = router_sk;
        webABloquearModel w;
        model.webs = new ArrayList<>();
        for (paginaControlParentalModel p : paginasModel) {
            model.webs.add( p.toWebBlockModel());
        }
        Map<String, String> data = null;
        try {
            data = Utils.toMap(model);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        final Gson gson = new Gson();
        /*
        Map<String, String> data  = new HashMap<>();;
            //data = Utils.toMap(model);
            String json = gson.toJson(model); //"{\"k1\":\"v1\",\"k2\":\"v2\"}";
            data = (Map<String,String>) gson.fromJson(json, data.getClass());*/


        /////////////////////////////////////////////////////////////
        String url = "http://200.82.0.24/api/routers";
        JsonRequestApi rq = new JsonRequestApi(Request.Method.POST, url, data, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //List<paginaControlParentalModel> p = gson.fromJson(response.toString(), List.class);
                try {
                    if(response.get("status").equals("ok")) {
                        routerInfo router;
                        Gson gson = new Gson();
                        router = gson.fromJson(response.get("router").toString(), routerInfo.class);
                        NMF_Info.clientInfo.router = router;
                        success.onResponse("ok");
                    }
                    else
                        success.onResponse(null);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Map<String, String> data1 = data    ;
                success.onResponse(null);
            }
        });
        execute(rq);

    }

    public void log(int tipo, String descripcion) {

        int max = Math.min(3500, descripcion.length());
        int cliente_sk = 0;
        if(NMF_Info.tipoUsuarioApp!=null)
            cliente_sk = NMF_Info.tipoUsuarioApp.id;
        else
            cliente_sk = -1;
        log log = new log(tipo, cliente_sk, descripcion.substring(0, max));

        String url = "http://200.82.0.24/api/logs";
        Map<String, String> data = null;
        try {
            data = Utils.toMap(log);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        JsonRequestApi rq = new JsonRequestApi(Request.Method.POST, url, data, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Map<String, String> data1 = data    ;
                //success.onResponse(null);
            }
        });
        execute(rq);

    }

    public void getNotificaciones(int cliente_sk, int usuario_sk, final Response.Listener<List<notificacionModel>> success) {

        //cliente_sk = 1;
        //usuario_sk = 1;

        String url = "http://200.82.0.24/api/notificaciones?cliente_sk="+String.valueOf(cliente_sk)+"&usuario_sk="+String.valueOf(usuario_sk);
        //todo: ACA LO HAGO ASI PORQUE NOSE COMO HACER PARA TRAERME UN ARRAY,
        final Map<String, String> params = null;
        JsonArrayRequestApi sr = new JsonArrayRequestApi(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                List<notificacionModel> ressult  = new ArrayList<>();
                notificacionModel nm = null;
                for (int i = 0; i < response.length(); i++) {
                    JSONObject explrObject = null;
                    try {
                        explrObject = response.getJSONObject(i);
                        nm = new notificacionModel();
                        nm.usuario_sk = explrObject.getInt("usuario_sk");
                        nm.cliente_sk = explrObject.getInt("cliente_sk");
                        nm.notificacion_sk = explrObject.getInt("notificacion_sk");
                        nm.notificacion_desc = explrObject.getString("notificacion_desc");
                        nm.notificacion_texto = explrObject.getString("notificacion_texto");
                        nm.tiempo_sk = explrObject.getString("tiempo_sk").substring(0, 10);
                        nm.ot_calificacion = explrObject.getDouble("ot_calificacion");

                        String aux = explrObject.getString("ot_id");
                        if(aux.equalsIgnoreCase("null"))
                            nm.ot_id = "";
                        else
                            nm.ot_id = explrObject.getString("ot_id");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    ressult.add(nm);
                }
                success.onResponse(ressult);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                success.onResponse(null);
            }
        }) ;
        execute(sr);
        /*
        StringRequest sr = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                List<notificacionModel> ressult  = new ArrayList<>();
                notificacionModel nm ;
                try {
                    //JSONObject jsnobject = new JSONObject(response);
                    JSONArray jsonarray = new JSONArray(response);
                    //JSONArray jsonArray = jsnobject.getJSONArray("locations");
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject explrObject = jsonarray.getJSONObject(i);
                        nm = new notificacionModel();
                        nm.usuario_sk = explrObject.getInt("usuario_sk");
                        nm.cliente_sk = explrObject.getInt("cliente_sk");
                        nm.notificacion_sk = explrObject.getInt("notificacion_sk");
                        nm.notificacion_desc = explrObject.getString("notificacion_desc");
                        nm.notificacion_texto = explrObject.getString("notificacion_texto");
                        nm.tiempo_sk = explrObject.getString("tiempo_sk").substring(0, 10);

                        String aux = explrObject.getString("ot_id");
                        if(aux.equalsIgnoreCase("null"))
                            nm.ot_id = "";
                        else
                            nm.ot_id = explrObject.getString("ot_id");

                        ressult.add(nm);
                    }
                    success.onResponse(ressult);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                return params;
            };
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                if(Api.token!=null){
                    headers.put("Authorization", "Bearer " + Api.token.getAccess_token());
                }
                return headers ;
            }

        };
        execute(sr);
*/
    }

    public void calificarOt(int ot_id, int calificacion, final Response.Listener success ){

        String url = "http://200.82.0.24/api/ot/"+String.valueOf(ot_id) + "?calificacion="+ String.valueOf(calificacion);


        JsonRequestApi rq = new JsonRequestApi(Request.Method.PUT, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                otModel ot = null;
                Gson gson = new Gson();
                try {
                    ot = gson.fromJson(response.get("ot").toString(), otModel.class);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                success.onResponse(ot);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                success.onResponse(null);
            }
        });
        execute(rq);
    }

    public void getTiposDeSolicitudes(final Response.Listener<List<tipoOsModel>> success){
        final Gson gson = new Gson();
        String url = "http://200.82.0.24/api/tipo_os";
        //todo: ACA LO HAGO ASI PORQUE NOSE COMO HACER PARA TRAERME UN ARRAY,
        JsonArrayRequestApi sr = new JsonArrayRequestApi(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                List<tipoOsModel> ressult  = new ArrayList<>();
                tipoOsModel tipo = null;
                for (int i = 0; i < response.length(); i++) {
                    JSONObject explrObject = null;
                    try {
                        explrObject = response.getJSONObject(i);
                        tipo = gson.fromJson(explrObject.toString(), tipoOsModel.class);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    ressult.add(tipo);
                }
                success.onResponse(ressult);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                success.onResponse(null);
            }
        }) ;
        execute(sr);
    }

    public void getTiposDeReclamos(final Response.Listener<List<tipoOtModel>> success){
        final Gson gson = new Gson();
        String url = "http://200.82.0.24/api/tipo_ot";
        //todo: ACA LO HAGO ASI PORQUE NOSE COMO HACER PARA TRAERME UN ARRAY,
        JsonArrayRequestApi sr = new JsonArrayRequestApi(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                List<tipoOtModel> ressult  = new ArrayList<>();
                tipoOtModel tipo = null;
                for (int i = 0; i < response.length(); i++) {
                    JSONObject explrObject = null;
                    try {
                        explrObject = response.getJSONObject(i);
                        tipo = gson.fromJson(explrObject.toString(), tipoOtModel.class);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    ressult.add(tipo);
                }
                success.onResponse(ressult);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                success.onResponse(null);
            }
        }) ;
        execute(sr);
    }


    public void getSolicitudes(final Response.Listener<List<solicitudListItemModel>> success){

        //int cliente_sk = 2;//NMF_Info.clientInfo.id;//2;
        int cliente_sk = NMF_Info.clientInfo.id;

        final Gson gson = new Gson();
        String url = "http://200.82.0.24/api/solicitudes?cliente_sk=" + String.valueOf(cliente_sk);
        //todo: ACA LO HAGO ASI PORQUE NOSE COMO HACER PARA TRAERME UN ARRAY,
        JsonArrayRequestApi sr = new JsonArrayRequestApi(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                List<solicitudListItemModel> ressult  = new ArrayList<>();
                solicitudListItemModel tipo = null;
                for (int i = 0; i < response.length(); i++) {
                    JSONObject explrObject = null;
                    try {
                        explrObject = response.getJSONObject(i);
                        tipo = gson.fromJson(explrObject.toString(), solicitudListItemModel.class);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    ressult.add(tipo);
                }
                success.onResponse(ressult);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                success.onResponse(null);
            }
        }) ;
        execute(sr);
    }

    public void getReclamos(final Response.Listener<List<reclamoListItemModel>> success){

        //int cliente_sk = 1;//NMF_Info.clientInfo.id;//2;
        int cliente_sk = NMF_Info.clientInfo.id;
        final Gson gson = new Gson();
        String url = "http://200.82.0.24/api/ot?cliente_sk=" + String.valueOf(cliente_sk);
        //todo: ACA LO HAGO ASI PORQUE NOSE COMO HACER PARA TRAERME UN ARRAY,
        JsonArrayRequestApi sr = new JsonArrayRequestApi(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                List<reclamoListItemModel> ressult  = new ArrayList<>();
                reclamoListItemModel tipo = null;
                for (int i = 0; i < response.length(); i++) {
                    JSONObject explrObject = null;
                    try {
                        explrObject = response.getJSONObject(i);
                        tipo = gson.fromJson(explrObject.toString(), reclamoListItemModel.class);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    ressult.add(tipo);
                }
                success.onResponse(ressult);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                success.onResponse(null);
            }
        }) ;
        execute(sr);
    }

}
