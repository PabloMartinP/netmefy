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
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.facebook.login.LoginManager;

import ar.com.netmefy.netmefy.cliente.ControlParentalActivity;
import ar.com.netmefy.netmefy.login.UserIdActivity;
import ar.com.netmefy.netmefy.router.ConfigWifi;
import ar.com.netmefy.netmefy.router.Router;
import ar.com.netmefy.netmefy.services.WifiUtils;
import ar.com.netmefy.netmefy.services.login.LikesToFacebook;
import ar.com.netmefy.netmefy.services.login.Session;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        session = new Session(getApplicationContext());
        LikesToFacebook likesToFacebook = new LikesToFacebook();
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


        //loadInfoRouter();
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

        }catch (Exception e){
            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
            //progress.dismiss();

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
        //final ProgressDialog pd = initProgressbar();

        /*final ProgressDialog progressBar = new ProgressDialog(MainActivity.this);
        progressBar.setMessage("Restarteando");
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setCancelable(false);
        progressBar.show();*/

        try{
            final Context _context = getApplicationContext();
            //changeRouterToRed();
            router.restartAndWaitUntilConnected(new Response.Listener() {
                @Override
                public void onResponse(Object response) {

                    try{

                        WifiUtils.connectToNetwork("NMFTPLink", _context, new Response.Listener() {
                            @Override
                            public void onResponse(Object response) {

                                if( isRed() )
                                    changeRouterToYellow();
                                else if(isYellow())
                                    changeRouterToRed();
                                else
                                    changeRouterToGreen();
                            }
                        }, new Response.Listener() {
                            @Override
                            public void onResponse(Object response) {

                            }
                        });
                    }catch (Exception e){
                        int j =0;
                        j++;
                        String jj = String.valueOf(j);
                    }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }, new Response.Listener() {
                @Override
                public void onResponse(Object response) {
                    //pd.dismiss();
                    //progressBar.dismiss();
                    changeRouterToGreen();
                }
            });
        }catch (Exception e){
            String jj;
            jj = e.toString();
        }

    }
}
