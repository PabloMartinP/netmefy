package ar.com.netmefy.netmefy;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;

import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

import ar.com.netmefy.netmefy.login.LoginActivity;
import ar.com.netmefy.netmefy.login.UserIdActivity;
import ar.com.netmefy.netmefy.router.activities.TPLinkTestsActivity;
import ar.com.netmefy.netmefy.services.login.Session;
import ar.com.netmefy.netmefy.tecnico.TecnicoActivity;

public class SplashScreen extends Activity {

    private Session session;
    private GoogleApiClient mGoogleApiClient;
    private static final String TAGGOOGLE = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        session = new Session(getApplicationContext());

        /*
        Intent intent = new Intent(SplashScreen.this, TPLinkTestsActivity.class);
        startActivity(intent);
        finish();*/


        if(session.getUserId().isEmpty()) {
            //no se registro con nada o esta totalmente deslogeado
            goToActivity(UserIdActivity.class);
        } else{
            FacebookSdk.sdkInitialize(getApplicationContext());
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
