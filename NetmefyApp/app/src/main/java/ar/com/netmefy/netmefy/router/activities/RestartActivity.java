package ar.com.netmefy.netmefy.router.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import ar.com.netmefy.netmefy.R;
import ar.com.netmefy.netmefy.router.Router;
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

        Router routerNucom  = new TPLink(this.getApplicationContext());
        routerNucom.restartAndWaitUntilConnected(new Response.Listener() {
            @Override
            public void onResponse(final Object response) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        btnrestart.setText("Restarteado OK");
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
        });
    }
}
