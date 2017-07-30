package ar.com.netmefy.netmefy.router.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import ar.com.netmefy.netmefy.R;
import ar.com.netmefy.netmefy.router.Router;
import ar.com.netmefy.netmefy.router.nucom.R5000UNv2.Nucom;
import ar.com.netmefy.netmefy.router.tplink.TLWR941ND.TPLink;

public class RestartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restart);
    }


    public void restart(View view){
        final Button btnrestart =(Button) findViewById(R.id.btn);
        btnrestart.setText("Restarteando ...");

        //Router router  = new Nucom(this.getApplicationContext());
        //Router router  = new TPLink(this.getApplicationContext());
        Router router  = Router.getInstance(getApplicationContext());

        btnrestart.setText("Restarteado ...");
        router.restartAndWaitUntilConnected(new Response.Listener() {
            @Override
            public void onResponse(final Object response) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        btnrestart.setText(response.toString());
                    }
                });
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(final VolleyError error) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        btnrestart.setText("error " + error.getMessage());
                    }
                });
            }
        }, new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                //Toast.makeText(getApplicationContext(), "CONECTADO OK!!!!!", Toast.LENGTH_LONG).show();
            }
        });
    }
}
