package ar.com.netmefy.netmefy.router.activities;

import android.accounts.AccountManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.w3c.dom.Text;

import ar.com.netmefy.netmefy.MainActivity;
import ar.com.netmefy.netmefy.R;
import ar.com.netmefy.netmefy.SplashScreen;
import ar.com.netmefy.netmefy.router.Router;
import ar.com.netmefy.netmefy.router.nucom.R5000UNv2.Nucom;
import ar.com.netmefy.netmefy.router.tplink.TLWR941ND.TPLink;

public class TPLinkTestsActivity extends AppCompatActivity {
    Router router;
    TextView tvTypeRouter ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tplink_tests);

        //router = new Nucom(getApplicationContext());
        //este create se ejecuta una vez sola, despues hay que llamar siempre a getInstance
        Router.createNucom();
        //Router.createTPLink();

        router = Router.getInstance(getApplicationContext());


        tvTypeRouter = (TextView) findViewById(R.id.tvTypeRouter);
        tvTypeRouter.setText(router.getName());


    }

    public void restart(View view){
        startActivity(new Intent(TPLinkTestsActivity.this, RestartActivity.class));
    }
    public void devicesConnected(View view){
        startActivity(new Intent(TPLinkTestsActivity.this, DevicesConnectedActivity.class));
    }
    public void changeWifi(View view){
        startActivity(new Intent(TPLinkTestsActivity.this, ChangeWifiActivity.class));
    }

    public void blockByMac(View view){
        startActivity(new Intent(TPLinkTestsActivity.this, TestBlockByMacActivity.class));
    }
    public void blockByUrl(View view){
        startActivity(new Intent(TPLinkTestsActivity.this, TestBlockByUrlActivity.class));
    }

    public void logout(View v){
        router.logout(new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                Toast.makeText(getApplicationContext(), "onResponse!!!!!", Toast.LENGTH_LONG).show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "onErrorResponse!!!!!", Toast.LENGTH_LONG).show();

            }
        });
    }

}
