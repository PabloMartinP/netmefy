package ar.com.netmefy.netmefy.router.activities;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.android.volley.Response;

import java.util.Timer;
import java.util.TimerTask;

import ar.com.netmefy.netmefy.R;
import ar.com.netmefy.netmefy.router.RestartTry;
import ar.com.netmefy.netmefy.router.models.WifiSignalResult;
import ar.com.netmefy.netmefy.services.WifiUtils;

public class TestWifiSignalActivity extends AppCompatActivity {
    Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_wifi_signal);
        btn = (Button)findViewById(R.id.btnBeginSignalWifi);
        btn.setText( btn.getText().toString());
    }

    public void beginTest(View v){

        btn.setText("Start ...");
        Context context = getApplicationContext();

        WifiUtils.checkSignal(context, new Response.Listener<WifiSignalResult>() {
            @Override
            public void onResponse(final WifiSignalResult response) {
                try{
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            btn.setText(response.toString());
                        }
                    });
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });


    }

}
