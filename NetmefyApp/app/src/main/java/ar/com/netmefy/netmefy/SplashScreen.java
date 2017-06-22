package ar.com.netmefy.netmefy;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;

import com.facebook.FacebookSdk;
import com.facebook.Profile;

import ar.com.netmefy.netmefy.login.LoginActivity;
import ar.com.netmefy.netmefy.login.UserIdActivity;
import ar.com.netmefy.netmefy.services.login.Session;

public class SplashScreen extends Activity {

    private Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        session = new Session(getApplicationContext());


        if(session.getUserId().isEmpty()) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    //Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                    Intent intent = new Intent(SplashScreen.this, UserIdActivity.class);
                    startActivity(intent);
                    finish();

                }
            }, 2000);
        } else{
            FacebookSdk.sdkInitialize(getApplicationContext());
            if(Profile.getCurrentProfile() != null) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                        Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                        startActivity(intent);
                        finish();

                    }
                }, 2000);
            }else{
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                        Intent intent = new Intent(SplashScreen.this, LoginActivity.class);
                        startActivity(intent);
                        finish();

                    }
                }, 2000);
            }
        }
    }
}
