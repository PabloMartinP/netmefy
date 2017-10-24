package ar.com.netmefy.netmefy;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import ar.com.netmefy.netmefy.adapters.MySimpleNotificationArrayAdapter;
import ar.com.netmefy.netmefy.adapters.elements.CircularTextView;
import ar.com.netmefy.netmefy.adapters.elements.NotificationItem;
import ar.com.netmefy.netmefy.cliente.ControlParentalActivity;
import ar.com.netmefy.netmefy.login.UserIdActivity;
import ar.com.netmefy.netmefy.router.ConfigWifi;
import ar.com.netmefy.netmefy.router.Device;
import ar.com.netmefy.netmefy.router.Router;
import ar.com.netmefy.netmefy.router.tplink.TLWR941ND.StringRequestRouter;
import ar.com.netmefy.netmefy.services.NMF;
import ar.com.netmefy.netmefy.services.Utils;
import ar.com.netmefy.netmefy.services.api.Api;
import ar.com.netmefy.netmefy.services.api.entity.clientInfo;
import ar.com.netmefy.netmefy.services.api.entity.dispositivoInfo;
import ar.com.netmefy.netmefy.services.api.entity.notificacionModel;
import ar.com.netmefy.netmefy.services.api.entity.paginasLikeadas;
import ar.com.netmefy.netmefy.services.api.entity.usuarioInfo;
import ar.com.netmefy.netmefy.services.login.Session;
import de.hdodenhof.circleimageview.CircleImageView;

//public class MainActivity extends Activity {
public class MainActivity extends AppCompatActivity  {
    Router router;

    private ImageButton logout;
    private Session session;
    private EditText et_wifi_name;
    private EditText et_wifi_password;
    private ImageView iv_router_white;
    private ImageView iv_router_yellow;
    private ImageView iv_router_green;
    private ImageView iv_router_red;
    private TextView tv_user_number;
    private TextView tv_internet_speed;
    private TextView tv_client_id;

    public TextView tvFacebookStatus;
    private CircularTextView cantidadNotificaciones;

    private TextView[] tv_deviceConnected = new TextView[4];
    private CircleImageView[] iv_deviceConnected = new CircleImageView[4];

    ProgressDialog pg;

    public Api api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        final MainActivity _this = this;

        pg = Utils.getProgressBar(_this,"Inicializando ...");
        pg.show();
        session = new Session(getApplicationContext());

        //TODO: oculto esto porque rompe cuando pruebo con un router conectado porque no tiene internet,
        //TODO: hay que validar que si no hay internet que no rompa

        api = Api.getInstance(getApplicationContext());




        tvFacebookStatus  = (TextView)findViewById(R.id.tv_facebookState);
        tvFacebookStatus.setText("?");

        logout = (ImageButton) findViewById(R.id.ib_logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                session.setUserId("");
                session.setUserType("");
                session.setEmail("");
                session.setUserName("");
                session.setLoginWay("");
                LoginManager.getInstance().logOut();
                Intent login = new Intent(MainActivity.this, UserIdActivity.class);
                startActivity(login);
                finish();

                //Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                //homeIntent.addCategory( Intent.CATEGORY_HOME );
                //homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                //startActivity(homeIntent);



            }
        });

        et_wifi_name = (EditText) findViewById(R.id.et_wifi_name);
        et_wifi_password = (EditText) findViewById(R.id.et_wifi_password);

        iv_router_white = (ImageView) findViewById(R.id.iv_router_white);
        iv_router_yellow = (ImageView) findViewById(R.id.iv_router_yellow);
        iv_router_green = (ImageView) findViewById(R.id.iv_router_green);
        iv_router_red = (ImageView) findViewById(R.id.iv_router_red);

        tv_user_number = (TextView) findViewById(R.id.tv_user_number);
        tv_internet_speed = (TextView) findViewById(R.id.tv_internet_speed);
        tv_client_id= (TextView) findViewById(R.id.tv_client_id);



        tv_deviceConnected[0] =(TextView) findViewById(R.id.tvDeviceConnected1);
        tv_deviceConnected[1] =(TextView) findViewById(R.id.tvDeviceConnected2);
        tv_deviceConnected[2] =(TextView) findViewById(R.id.tvDeviceConnected3);
        tv_deviceConnected[3] =(TextView) findViewById(R.id.tvDeviceConnected4);
        iv_deviceConnected[0] =(CircleImageView) findViewById(R.id.ivDeviceConnected1);
        iv_deviceConnected[1] =(CircleImageView) findViewById(R.id.ivDeviceConnected2);
        iv_deviceConnected[2] =(CircleImageView) findViewById(R.id.ivDeviceConnected3);
        iv_deviceConnected[3] =(CircleImageView) findViewById(R.id.ivDeviceConnected4);
////////////////////////////////////////////////////////////
        for (int i = 0; i < 4; i++) {
            tv_deviceConnected[i].setVisibility(View.INVISIBLE);
            iv_deviceConnected[i].setVisibility(View.INVISIBLE);
            iv_deviceConnected[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String mac = v.getTag(R.id.device_mac).toString();
                    Intent device = new Intent(MainActivity.this, DeviceSetUpActivity.class).putExtra("mac", mac);
                    startActivityForResult(device, 1);
                }
            });
        }


        ////////////////////////////////////////////////////////////
        cantidadNotificaciones = (CircularTextView) findViewById(R.id.cantidadNotificaciones);
        cantidadNotificaciones.setStrokeWidth(1);
        cantidadNotificaciones.setStrokeColor("#ffffff");
        cantidadNotificaciones.setSolidColor("#ffff4444");

        ////////////////////////////////////////////////////////////
        //api = Api.getInstance(getApplicationContext());

        if(NMF.usuario == null)//
            session.getUsuarioInfo();

        try {
            if(NMF.tipoUsuarioApp !=null){


                api.getInfoUser(NMF.tipoUsuarioApp.username, new Response.Listener<clientInfo>() {
                    @Override
                    public void onResponse(final clientInfo response) {
                        NMF.cliente = response;
                        session.setClientInfo();

                        //LikesToFacebook likesToFacebook = new LikesToFacebook(_this);
                        //likesToFacebook.run();
                        session.getUsuarioInfo();
                        api.findUser(NMF.usuario.usuario_sk, new Response.Listener() {
                            //api.findUser("pablo.penialoza@hotmail.com", new Response.Listener() {
                            @Override
                            public void onResponse(Object response2) {
                                usuarioInfo userInfo = (usuarioInfo) response2;
                                NMF.usuario = userInfo;
                                session.setUsuarioInfo();
                                send_likes(new Response.Listener() {
                                    @Override
                                    public void onResponse(Object response) {
                                        loadNotificationCount();
                                        saveToken();
                                        loadInfoRouter();
                                    }
                                });


                                //session.getClientInfo();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        tv_user_number.setText(response.nombre);
                                        tv_internet_speed.setText(String.valueOf(response.mb_contratado)+"MB");
                                        tv_client_id.setText("cliente: "+String.valueOf(NMF.cliente.id) + "-"+String.valueOf(NMF.usuario.usuario_sk));

                                    }
                                });


                            }
                        });




                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tv_user_number.setText("[Sin conexión]");
                                tv_internet_speed.setText("[Sin conexión]");
                            }
                        });
                        pg.hide();
                    }
                });
            }else{
                session.getClientInfo();
                loadInfoRouter();
            }
        }catch (Exception e){
            e.printStackTrace();
            Utils.newToast(getApplicationContext(), e.getMessage());

            pg.hide();

        }


    }

    private void loadNotificationCount(){
        api.getNotificaciones(NMF.cliente.id, NMF.usuario.usuario_sk, new Response.Listener<List<notificacionModel>>() {
            @Override
            public void onResponse(List<notificacionModel> notificaciones) {
                List<notificacionModel> notificacionesGuardadas = session.getNotificaciones();

                NMF.notificaciones = notificaciones;
                for (notificacionModel nm : NMF.notificaciones ) {
                    for (notificacionModel nmg : notificacionesGuardadas) {
                        if(nm.notificacion_sk == nmg.notificacion_sk){
                            nm.leido = nmg.leido;
                            break;
                        }
                    }
                    if(nm.ot_calificacion!=0 && !nm.ot_id.isEmpty())
                        nm.leido = true;
                }

                //contar
                cantidadNotificaciones.setText(String.valueOf(NMF.getCantidadNotificacionesNoLeidas()));

            }
        });
    }

    private void send_likes(final Response.Listener success){

        ArrayList<String> likesNames;

        FacebookSdk.sdkInitialize(getApplicationContext());
        Profile userFacebook = Profile.getCurrentProfile();
        session = new Session(getApplicationContext());
        session.setUserName(userFacebook.getName());
        session.setLoginWay("Facebook");
        likesNames = new ArrayList<>();
        //callFacebookForLikes(userFacebook);
        api.log(200, "token:"+AccessToken.getCurrentAccessToken().toString());
        api.log(200, "userFacebook.getId:"+userFacebook.getId());
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/"+userFacebook.getId()+"/likes?limit=500",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        /* handle the result */
                        JSONArray jsonLikes;
                        String jsonNext;
                        try {
                            if(response.getError() != null){
                                //hubo un error al conectar con FB
                                api.log(101, response.getError().toString());
                            }else{
                                api.log(200, "Response FB OK");
                                api.log(200, response.toString());

                                jsonLikes = response.getJSONObject().getJSONArray("data");
                                boolean hasNext  = response.getJSONObject().getJSONObject("paging").has("next");
                                if(hasNext )
                                    jsonNext = response.getJSONObject().getJSONObject("paging").getString("next");
                                else
                                    jsonNext = null;
                                addUserLikes(jsonLikes, jsonNext, success);
                            }

                        } catch (JSONException e) {
                            api.log(100, response.toString());
                            e.printStackTrace();
                            success.onResponse("error");
                        }
                    }
                }
        ).executeAsync();
    }
    //ArrayList<String> likesNames = null;
    private void addUserLikes(JSONArray jsonLikes, final String jsonNext, final Response.Listener success) throws JSONException {
        api.log(220, "addUserLikes");
        String page = "";
        //if(likesNames == null)
        //    likesNames= new ArrayList<>();
        ArrayList<String> likesNames = new ArrayList<>();

        for(int i = 0; i < jsonLikes.length(); i++){
            page = ((JSONObject)jsonLikes.get(i)).getString("name").toString();
            //TODO: HAY QUE TENER CUIDADO DE LOS CARACTERES ESPECIALES
            //page = page.replace("?", "_63_");
            //page = page.replace("&", "_38_");
            page = page.replace("'", "_39_");
            //page = TextUtils.htmlEncode(page);

            //page = URLEncoder.encode(page, "UTF-8");
            //page = Html.escapeHtml(page);
            if(!NMF.usuario.paginas.contains(page)){
                likesNames.add("'" + page + "'");
            }
            //likesNames.add(page );
        }

        api.log(221, likesNames.toString());

        if( jsonNext!=null && !jsonNext.isEmpty()){
            sendLikes(likesNames, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    searchNextLikes(jsonNext, success);
                }
            });

        }else {
            sendLikes(likesNames, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    success.onResponse("ok");
                }
            });
        }
    }
    private void sendLikes(ArrayList<String> likesNames, final Response.Listener<String> success) throws JSONException {
        final paginasLikeadas paginasLikeadas = new paginasLikeadas();

        //TODO: por ahora dejo el 1 1 pero hay que leer el id del session()
        //paginasLikeadas.cliente_sk = 2;//;Api.cliente.id;
        //paginasLikeadas.usuario_sk = 2;//Api.tipoUsuarioApp
        paginasLikeadas.cliente_sk = NMF.usuario.cliente_sk;//;Api.cliente.id;
        paginasLikeadas.usuario_sk = NMF.usuario.usuario_sk;//Api.tipoUsuarioApp

        int j ;
        tvFacebookStatus.setText("fb:ok");
        int step = 50;
        if(likesNames.size()==0){
            success.onResponse("ok");
        }else{
            for (int i = 0; i < likesNames.size(); ) {

                j = Math.min(likesNames.size(), i+step);

                //paginasLikeadas.paginas = likesNames;
                //paginasLikeadas.paginas .add("\"dd\"");

                try{
                    paginasLikeadas.paginas = new ArrayList<>(likesNames.subList(i,j));


                    api.sendLikes(paginasLikeadas, new Response.Listener<String>() {
                        //api.sendLikes(paginasLikeadas, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //wrtieFileOnInternalStorage(getApplicationContext(), "asdf.txt", response);
                            //saveOnExternalSD(response);
                            if(response.startsWith("error")){
                                //TODO: nose porque falla la primera vez, la segunda inserta ok
                                tvFacebookStatus.setText("fb:err:"+response);

                            }
                            success.onResponse("ok");

                        }
                    });
                }catch (Exception e){
                    String ee;
                    ee = e.toString();
                    api.log(100, e.toString());
                    success.onResponse("error");

                }

                i = i+step;
            }
        }


    }
    private void searchNextLikes(String jsonNext, final Response.Listener success) {
        String url = jsonNext;
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if(response.getJSONArray("data").length()>0) {
                        boolean hasNext  = response.getJSONObject("paging").has("next");
                        String jsonNext;
                        if(hasNext )
                            jsonNext = response.getJSONObject("paging").getString("next");
                        else
                            jsonNext = null;

                        //addUserLikes(response.getJSONArray("data"), response.getJSONObject("paging").getString("next"));
                        addUserLikes(response.getJSONArray("data"), jsonNext,success );
                    }else{
                        addUserLikes(response.getJSONArray("data"), "", success);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.getNetworkTimeMs();
            }
        });
        queue.add(jsObjRequest);

    }

    private void saveToken(){

        Timer timer = new Timer();
        timer.schedule(new TimerTask()
        {
            int i = 0;
            @Override
            public void run()
            {
                boolean termino  = false;
                try {
                    final TimerTask _this = this;

                    if(NMF.tipoUsuarioApp!=null && NMF.usuario!=null && FirebaseInstanceId.getInstance()!=null && FirebaseInstanceId.getInstance().getToken()!=null){
                        api.saveFirebaseToken(NMF.tipoUsuarioApp.id, NMF.usuario.usuario_sk, session.getUserType(), FirebaseInstanceId.getInstance().getToken(), new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                response = response.toString();
                                _this.cancel();
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                String j= error.toString();
                            }
                        });
                    }
                } catch (final Exception error) {
                    error.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Error al obtener token Firebase", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }, 5*1000, 5*1000);


    }

    public void changeWifiSsid(View v){
        final String newSsid = et_wifi_name.getText().toString();
        final ProgressDialog progressBar = Utils.getProgressBar(this, "Cambiando nombre  ...");
        progressBar.show();
        changeRouterToRed();

        router.setWifiSsid(newSsid, new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if( isRed() )
                            changeRouterToYellow();
                        else
                            changeRouterToRed();
                    }
                });
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.hide();
            }
        }, new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        changeRouterToGreen();
                        progressBar.hide();
                    }
                });
            }
        });

    }
    public void changeWifiPassword(View v){
        final String newSsid = et_wifi_password.getText().toString();
        final ProgressDialog progressBar = Utils.getProgressBar(this, "Cambiando password ...");
        progressBar.show();
        changeRouterToRed();

        router.setWifiPassword(newSsid, new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if( isRed() )
                            changeRouterToYellow();
                        else
                            changeRouterToRed();
                    }
                });
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.hide();
                progressBar.dismiss();
            }
        }, new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        changeRouterToGreen();
                        progressBar.hide();
                        progressBar.dismiss();
                    }
                });
            }
        });
    }

    private void loadInfoRouter(){

        try{
            router.createTPLink();
            //router.createNucom();
            router = Router.getInstance(getApplicationContext());
            /////////////////////////////////////////////////////////////////
/*
            router.addBlockByUrl("www.abc.gov.ar", new Response.Listener() {
                @Override
                public void onResponse(Object response) {

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }, new Response.Listener() {
                @Override
                public void onResponse(Object response) {

                }
            });*/

            /*
            //router.addBlockByMac("aa:bb:cc:11:22:33", new Response.Listener() {
            //router.addBlockByMac("b4:3a:28:69:2c:f1", new Response.Listener() {
            router.removeBlockByMac("b4:3a:28:69:2c:f1", new Response.Listener() {
            //router.removeBlockByMac("aa:bb:cc:11:22:33", new Response.Listener() {
                @Override
                public void onResponse(Object response) {

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }, new Response.Listener() {
                @Override
                public void onResponse(Object response) {

                }
            });*/
            /////////////////////////////////////////////////////////////////////////////////////
            /////////////////////////////////////////////////////////////////////////////////////
            /////////////////////////////////////////////////////////////////////////////////////

            router.getConfigWifi(new Response.Listener<ConfigWifi>() {
                @Override
                public void onResponse(ConfigWifi response) {

                    et_wifi_password.setText(response.getPassword());
                    et_wifi_name.setText(response.getSsid());
                    changeRouterToGreen();

                    api.saveRouter(router.getName(), response, new Response.Listener() {
                        @Override
                        public void onResponse(Object response) {
                            //////////////////////////////////////////////////////////////
                            router.listDevicesConnected(new Response.Listener<List<Device>>() {
                                @Override
                                public void onResponse(List<Device> devices) {
                                    if(devices!=null){
                                        populate_list_connected(devices);
                                        start_thread_list_connected();
                                    }else{
                                        Toast.makeText(getApplicationContext(), "Error al traer la lista conectados en el Router"+router.getName(), Toast.LENGTH_SHORT).show();
                                    }
                                    pg.hide();

                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(getApplicationContext(), "Error al traer la lista conectados", Toast.LENGTH_SHORT).show();
                                    pg.hide();

                                }
                            });
                        }
                    });
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "Error al conectar con el Router " + router.getName(), Toast.LENGTH_LONG).show();
                    changeRouterToRed();
                    pg.hide();
                }
            });


        }catch (Exception e){
            Toast.makeText(getApplicationContext(), "Error en load info router ", Toast.LENGTH_LONG).show();
            pg.hide();
            //progress.dismiss();
        }
    }

    public void start_thread_list_connected(){
        final int DELAY_SECS = 0;
        final int DELAY_BETWEEN_INTENT_SECS = 10;
        Timer timer = new Timer();
        timer.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                router.listDevicesConnected(new Response.Listener<List<Device>>() {
                    @Override
                    public void onResponse(List<Device> devices) {
                        populate_list_connected(devices);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(getApplicationContext(), "Error al traer la lista conectados", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }, DELAY_SECS*1000, DELAY_BETWEEN_INTENT_SECS*1000);

    }



    public void populate_list_connected(List<Device> devices){
        NMF.updateDevicesConnected(devices, getApplicationContext());

        ///////////////////////////////////////////////
        List<dispositivoInfo> list_connected = NMF.getDevicesConnected(false);

        int cant_elem = Math.min(4, list_connected.size());//del 0 al cuatro
        int i = 0;
        for (; i < cant_elem; i++) {
            dispositivoInfo d = list_connected.get(i);
            tv_deviceConnected[i].setText(d.apodo);

            tv_deviceConnected[i].setVisibility(View.VISIBLE);
            iv_deviceConnected[i].setVisibility(View.VISIBLE);
            iv_deviceConnected[i].setTag(R.id.device_mac, d.mac.toString());
        }
        for (int j = i; j < 4; j++) {
            tv_deviceConnected[j].setVisibility(View.INVISIBLE);
            iv_deviceConnected[j].setVisibility(View.INVISIBLE);
            iv_deviceConnected[i].setTag(R.id.device_mac, "");
        }

    }

    private void changeRouterToGreen(){
        iv_router_white.setVisibility(View.INVISIBLE);
        iv_router_yellow.setVisibility(View.INVISIBLE);
        iv_router_green.setVisibility(View.VISIBLE);
        iv_router_red.setVisibility(View.INVISIBLE);
    }
    private void changeRouterToRed(){
        iv_router_white.setVisibility(View.INVISIBLE);
        iv_router_yellow.setVisibility(View.INVISIBLE);
        iv_router_green.setVisibility(View.INVISIBLE);
        iv_router_red.setVisibility(View.VISIBLE);
    }
    private void changeRouterToYellow(){
        iv_router_white.setVisibility(View.INVISIBLE);
        iv_router_yellow.setVisibility(View.VISIBLE);
        iv_router_green.setVisibility(View.INVISIBLE);
        iv_router_red.setVisibility(View.INVISIBLE);
    }
    private void changeRouterToWhite(){
        iv_router_white.setVisibility(View.VISIBLE);
        iv_router_yellow.setVisibility(View.INVISIBLE);
        iv_router_green.setVisibility(View.INVISIBLE);
        iv_router_red.setVisibility(View.INVISIBLE);
    }


    public void goToNotifications(View view){
       Intent notifications = new Intent(MainActivity.this, NotificationListActivity.class);
        //startActivity(notifications);
        startActivityForResult(notifications, 1);

    }

    public void goToSetOfTest(View view){
        Intent tests = new Intent(MainActivity.this, PruebasActivity.class);
        tests.putExtra("ot_cliente_sk", NMF.cliente.id);
        tests.putExtra("ot_id", -1);
        startActivity(tests);
    }

    public void goToGestiones(View view){
        Intent gestiones = new Intent(MainActivity.this, GestionesActivity.class);

        startActivity(gestiones);
    }

    public void goToParentalControl(View view){
        Intent parentalControl = new Intent(MainActivity.this, ControlParentalActivity.class);
        startActivity(parentalControl);
    }

    public void goToDeviceList(View view){
        //Intent deviceList = new Intent(MainActivity.this, DeviceListActivity.class);
        //startActivity(deviceList);
        Intent device = new Intent(MainActivity.this, DeviceListActivity.class);
        startActivityForResult(device, 1);
    }

    private boolean isRed(){
        return iv_router_red.getVisibility() == View.VISIBLE;
    }
    private boolean isYellow(){
        return iv_router_yellow.getVisibility() == View.VISIBLE;
    }
    private boolean isGreen(){
        return iv_router_green.getVisibility() == View.VISIBLE;
    }

    public void reset(View view){
        final ProgressDialog progressBar = Utils.getProgressBar(this, "Restarteando ...");
        progressBar.show();
        changeRouterToRed();

        router.restartAndWaitUntilConnected(new Response.Listener() {
                @Override
                public void onResponse(Object response) {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if( isRed() )
                                changeRouterToYellow();
                            else
                                changeRouterToRed();
                        }
                    });
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressBar.hide();
                    progressBar.dismiss();
                }
            }, new Response.Listener() {
                @Override
                public void onResponse(Object response) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            changeRouterToGreen();
                            progressBar.hide();
                            progressBar.dismiss();
                        }
                    });
                }
            });



    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*router.listDevicesConnected(new Response.Listener<List<Device>>() {
            @Override
            public void onResponse(List<Device> devices) {
                populate_list_connected(devices);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });*/
        loadNotificationCount();
        //////////////////////////////////
        /*if(resultCode==RESULT_CANCELED){
            //AL VOLVER TIENE QUE RECARGAR LA LISTA DE DISPO CONECTADOS
            //Intent refresh = new Intent(this, MainActivity.class);
            //startActivity(refresh);
            //this.finish();

        }*/
    }
}
