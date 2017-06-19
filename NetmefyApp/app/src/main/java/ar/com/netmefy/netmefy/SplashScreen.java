package ar.com.netmefy.netmefy;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;

import ar.com.netmefy.netmefy.login.LoginActivity;
import ar.com.netmefy.netmefy.login.UserIdActivity;

public class SplashScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                Intent intent = new Intent(SplashScreen.this, UserIdActivity.class);
                startActivity(intent);
                finish();

            }
        }, 3000);

    }
}
