package ar.com.netmefy.netmefy;

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

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.facebook.login.LoginManager;
import com.fasterxml.jackson.databind.deser.std.DateDeserializers;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.List;

import ar.com.netmefy.netmefy.cliente.ControlParentalActivity;
import ar.com.netmefy.netmefy.login.UserIdActivity;
import ar.com.netmefy.netmefy.router.ConfigWifi;
import ar.com.netmefy.netmefy.router.Device;
import ar.com.netmefy.netmefy.router.Router;
import ar.com.netmefy.netmefy.services.Utils;
import ar.com.netmefy.netmefy.services.WifiUtils;
import ar.com.netmefy.netmefy.services.api.Api;
import ar.com.netmefy.netmefy.services.api.entity.clientInfo;
import ar.com.netmefy.netmefy.services.api.entity.paginasLikeadas;
import ar.com.netmefy.netmefy.services.api.entity.tipoUsuarioApp;
import ar.com.netmefy.netmefy.services.login.LikesToFacebook;
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

        /*paginasLikeadas paginasLikeadas = new paginasLikeadas();
        paginasLikeadas.cliente_sk = 1;
        paginasLikeadas.usuario_sk = 1;
        paginasLikeadas.paginas = new ArrayList<>();
        paginasLikeadas.paginas.add("'mtlslig'");
        paginasLikeadas.paginas.add("'123456'");

        api.sendLikes(paginasLikeadas, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        });*/

        /*api.getTypeOfUser("101", new Response.Listener<tipoUsuarioApp>() {
            @Override
            public void onResponse(tipoUsuarioApp response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });*/

        tvFacebookStatus  = (TextView)findViewById(R.id.tv_facebookState);
        tvFacebookStatus.setText("?");
        LikesToFacebook likesToFacebook = new LikesToFacebook(this);
        likesToFacebook.run();
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
        //api = Api.getInstance(getApplicationContext());

        if(Api.tipoUsuarioApp !=null){
            api.getInfoUser(Api.tipoUsuarioApp.username, new Response.Listener<clientInfo>() {
                @Override
                public void onResponse(final clientInfo response) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tv_user_number.setText(response.nombre);
                            tv_internet_speed.setText(String.valueOf(response.mb_contratado)+"MB");
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
        }

        loadInfoRouter();
        //saveToken();
    }


    private void saveToken(){
        try {
            api.saveFirebaseToken(session.getUserId(), session.getUserType(), FirebaseInstanceId.getInstance().getToken(), new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    response = response.toString();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    String j= error.toString();
                }
            });
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
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
            router.createTPLink();
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
                    Toast.makeText(getApplicationContext(), "Error:"+error.getMessage(), Toast.LENGTH_LONG).show();
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

    private void populate_list_connected(List<Device> devices){

        int cant_elem = Math.min(4, devices.size());//del 0 al cuatro
        int i = 0;
        for (; i < cant_elem; i++) {
            Device d = devices.get(i);
            tv_deviceConnected[i].setText(d.getName());
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
       /* Intent notifications = new Intent(MainActivity.this, NotificationsActivity.class);
        startActivity(notifications);*/
    }

    public void goToSetOfTest(View view){
       /* Intent tests = new Intent(MainActivity.this, TestConnectivityActivity.class);
        startActivity(tests);*/
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
        Intent deviceList = new Intent(MainActivity.this, DeviceListActivity.class);
        startActivity(deviceList);
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
