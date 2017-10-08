package ar.com.netmefy.netmefy;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
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
import com.fasterxml.jackson.databind.deser.std.DateDeserializers;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import ar.com.netmefy.netmefy.cliente.ControlParentalActivity;
import ar.com.netmefy.netmefy.login.UserIdActivity;
import ar.com.netmefy.netmefy.router.ConfigWifi;
import ar.com.netmefy.netmefy.router.Device;
import ar.com.netmefy.netmefy.router.RestartTry;
import ar.com.netmefy.netmefy.router.Router;
import ar.com.netmefy.netmefy.services.NMF_Info;
import ar.com.netmefy.netmefy.services.Utils;
import ar.com.netmefy.netmefy.services.WifiUtils;
import ar.com.netmefy.netmefy.services.api.Api;
import ar.com.netmefy.netmefy.services.api.entity.DeviceModel;
import ar.com.netmefy.netmefy.services.api.entity.clientInfo;
import ar.com.netmefy.netmefy.services.api.entity.dispositivoInfo;
import ar.com.netmefy.netmefy.services.api.entity.paginasLikeadas;
import ar.com.netmefy.netmefy.services.api.entity.tipoUsuarioApp;
import ar.com.netmefy.netmefy.services.api.entity.usuarioInfo;
import ar.com.netmefy.netmefy.services.login.LikesToFacebook;
import ar.com.netmefy.netmefy.services.login.Session;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.facebook.FacebookSdk.getApplicationContext;

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
    public TextView tvFacebookStatus;

    private TextView[] tv_deviceConnected = new TextView[4];
    private CircleImageView[] iv_deviceConnected = new CircleImageView[4];



    public Api api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

        tv_deviceConnected[0] =(TextView) findViewById(R.id.tvDeviceConnected1);
        tv_deviceConnected[1] =(TextView) findViewById(R.id.tvDeviceConnected2);
        tv_deviceConnected[2] =(TextView) findViewById(R.id.tvDeviceConnected3);
        tv_deviceConnected[3] =(TextView) findViewById(R.id.tvDeviceConnected4);
        iv_deviceConnected[0] =(CircleImageView) findViewById(R.id.ivDeviceConnected1);
        iv_deviceConnected[1] =(CircleImageView) findViewById(R.id.ivDeviceConnected2);
        iv_deviceConnected[2] =(CircleImageView) findViewById(R.id.ivDeviceConnected3);
        iv_deviceConnected[3] =(CircleImageView) findViewById(R.id.ivDeviceConnected4);
////////////////////////////////////////////////////////////
        tv_deviceConnected[0].setVisibility(View.INVISIBLE);
        tv_deviceConnected[1].setVisibility(View.INVISIBLE);
        tv_deviceConnected[2].setVisibility(View.INVISIBLE);
        tv_deviceConnected[3].setVisibility(View.INVISIBLE);
        iv_deviceConnected[0].setVisibility(View.INVISIBLE);
        iv_deviceConnected[1].setVisibility(View.INVISIBLE);
        iv_deviceConnected[2].setVisibility(View.INVISIBLE);
        iv_deviceConnected[3].setVisibility(View.INVISIBLE);


        ////////////////////////////////////////////////////////////
        //api = Api.getInstance(getApplicationContext());

        if(NMF_Info.usuarioInfo == null)//
            session.getUsuarioInfo();

        final MainActivity _this = this;

        try {
            if(NMF_Info.tipoUsuarioApp !=null){


                api.getInfoUser(NMF_Info.tipoUsuarioApp.username, new Response.Listener<clientInfo>() {
                    @Override
                    public void onResponse(final clientInfo response) {
                        NMF_Info.clientInfo = response;
                        session.setClientInfo();

                        //LikesToFacebook likesToFacebook = new LikesToFacebook(_this);
                        //likesToFacebook.run();
                        session.getUsuarioInfo();
                        api.findUser(NMF_Info.usuarioInfo.usuario_email, new Response.Listener() {
                            //api.findUser("pablo.penialoza@hotmail.com", new Response.Listener() {
                            @Override
                            public void onResponse(Object response2) {
                                usuarioInfo userInfo = (usuarioInfo) response2;
                                NMF_Info.usuarioInfo = userInfo;
                                session.setUsuarioInfo();
                                send_likes();


                                //session.getClientInfo();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        tv_user_number.setText(response.nombre);
                                        tv_internet_speed.setText(String.valueOf(response.mb_contratado)+"MB");
                                    }
                                });


                                saveToken();
                                loadInfoRouter();
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
                    }
                });
            }else{
                session.getClientInfo();
                loadInfoRouter();
            }
        }catch (Exception e){
            e.printStackTrace();

        }
    }

    private void send_likes(){

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
                                addUserLikes(jsonLikes, jsonNext);
                            }

                        } catch (JSONException e) {
                            api.log(100, response.toString());
                            e.printStackTrace();
                        }
                    }
                }
        ).executeAsync();
    }
    //ArrayList<String> likesNames = null;
    private void addUserLikes(JSONArray jsonLikes, final String jsonNext) throws JSONException {
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
            if(!NMF_Info.usuarioInfo.paginas.contains(page)){
                likesNames.add("'" + page + "'");
            }
            //likesNames.add(page );
        }

        api.log(221, likesNames.toString());

        if( jsonNext!=null && !jsonNext.isEmpty()){
            sendLikes(likesNames, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    searchNextLikes(jsonNext);
                }
            });

        }else {
            sendLikes(likesNames, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    String ok;
                    ok = "OK;";
                }
            });
        }
    }
    private void sendLikes(ArrayList<String> likesNames, final Response.Listener<String> success) throws JSONException {
        final paginasLikeadas paginasLikeadas = new paginasLikeadas();

        //TODO: por ahora dejo el 1 1 pero hay que leer el id del session()
        //paginasLikeadas.cliente_sk = 2;//;Api.clientInfo.id;
        //paginasLikeadas.usuario_sk = 2;//Api.tipoUsuarioApp
        paginasLikeadas.cliente_sk = NMF_Info.usuarioInfo.cliente_sk;//;Api.clientInfo.id;
        paginasLikeadas.usuario_sk = NMF_Info.usuarioInfo.usuario_sk;//Api.tipoUsuarioApp

        int j ;
        tvFacebookStatus.setText("fb:ok");
        int step = 50;
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
    private void searchNextLikes(String jsonNext) {
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
                        addUserLikes(response.getJSONArray("data"), jsonNext);
                    }else{
                        addUserLikes(response.getJSONArray("data"), "");
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

                    if(NMF_Info.tipoUsuarioApp!=null && FirebaseInstanceId.getInstance()!=null && FirebaseInstanceId.getInstance().getToken()!=null){
                        api.saveFirebaseToken(NMF_Info.tipoUsuarioApp.id, session.getUserType(), FirebaseInstanceId.getInstance().getToken(), new Response.Listener<String>() {
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
                            Toast.makeText(getApplicationContext(), "Error al obtener token", Toast.LENGTH_SHORT).show();
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
            //router.createTPLink();


            router.createNucom();
            router = Router.getInstance(getApplicationContext());

            router.getConfigWifi(new Response.Listener<ConfigWifi>() {
                @Override
                public void onResponse(ConfigWifi response) {
                    et_wifi_password.setText(response.getPassword());
                    et_wifi_name.setText(response.getSsid());
                    changeRouterToGreen();
                    //progress.dismiss();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "Error al conectar con el Router", Toast.LENGTH_LONG).show();
                    changeRouterToRed();
                    //progress.dismiss();
                }
            });

            //////////////////////////////////////////////////////////////
            router.listDevicesConnected(new Response.Listener<List<Device>>() {
                @Override
                public void onResponse(List<Device> devices) {
                    populate_list_connected(devices);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
            /////////////////////////////////



        }catch (Exception e){
            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
            //progress.dismiss();

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_CANCELED){
            //AL VOLVER TIENE QUE RECARGAR LA LISTA DE DISPO CONECTADOS
            //Intent refresh = new Intent(this, MainActivity.class);
            //startActivity(refresh);
            //this.finish();
            router.listDevicesConnected(new Response.Listener<List<Device>>() {
                @Override
                public void onResponse(List<Device> devices) {
                    populate_list_connected(devices);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
        }
    }

    public void populate_list_connected(List<Device> devices){
        NMF_Info.updateDevicesConnected(devices, getApplicationContext());

        ///////////////////////////////////////////////
        List<dispositivoInfo> list_connected = NMF_Info.getDevicesConnected();

        int cant_elem = Math.min(4, list_connected.size());//del 0 al cuatro
        int i = 0;
        for (; i < cant_elem; i++) {
            dispositivoInfo d = list_connected.get(i);
            tv_deviceConnected[i].setText(d.apodo);

            tv_deviceConnected[i].setVisibility(View.VISIBLE);
            iv_deviceConnected[i].setVisibility(View.VISIBLE);
        }
        for (int j = i; j < 4; j++) {
            tv_deviceConnected[j].setVisibility(View.INVISIBLE);
            iv_deviceConnected[j].setVisibility(View.INVISIBLE);
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
        startActivity(notifications);
    }

    public void goToSetOfTest(View view){
        Intent tests = new Intent(MainActivity.this, PruebasActivity.class);
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
}
