package ar.com.netmefy.netmefy;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.facebook.FacebookSdk;
import com.facebook.Profile;

import ar.com.netmefy.netmefy.login.LoginActivity;
import ar.com.netmefy.netmefy.login.UserIdActivity;
import ar.com.netmefy.netmefy.services.NMF;
import ar.com.netmefy.netmefy.services.api.Api;
import ar.com.netmefy.netmefy.services.api.entity.tipoUsuarioApp;
import ar.com.netmefy.netmefy.services.login.Session;
import ar.com.netmefy.netmefy.tecnico.TecnicoActivity;

public class SplashScreen extends Activity {

    private Session session;
    Api api ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        session = new Session(getApplicationContext());

        api = Api.getInstance(getApplicationContext());

        if(session.getUserId().isEmpty()) {
            //no se registro con nada o esta totalmente deslogeado
            goToActivity(UserIdActivity.class);
        } else{
            FacebookSdk.sdkInitialize(getApplicationContext());
            api.LogIn(session.getUserId(), session.getPassword(), new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if(response.equals("ok")){
                        api.getTypeOfUser(session.getUserId(), new Response.Listener<tipoUsuarioApp>() {
                            @Override
                            public void onResponse(tipoUsuarioApp response) {
                                NMF.tipoUsuarioApp = response;

                                gotoNextActivity();
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        });
                    }else{
                        Toast.makeText(getApplicationContext(), "Error al conectar con el server", Toast.LENGTH_LONG).show();
                        gotoNextActivity();
                    }

                }
            });




        }
    }

    private void gotoNextActivity() {
        api.log(100, "Logeo ok");

        if(Profile.getCurrentProfile() != null) {
            //se logeo antes con facebook

            goToActivity(MainActivity.class);

        }else if (session.getUserType().equalsIgnoreCase("user")){
            //es usuario
            goToActivity(LoginActivity.class);
        }else{
            //es tecnico
            goToActivity(TecnicoActivity.class);
        }
    }

    private void goToActivity(final Class aClass) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                Intent intent = new Intent(SplashScreen.this, aClass);
                startActivity(intent);
                finish();

            }
        }, 2000);
    }
}
