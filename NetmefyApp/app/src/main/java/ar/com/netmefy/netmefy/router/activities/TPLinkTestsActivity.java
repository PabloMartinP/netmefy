package ar.com.netmefy.netmefy.router.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

import ar.com.netmefy.netmefy.MainActivity;
import ar.com.netmefy.netmefy.R;
import ar.com.netmefy.netmefy.SplashScreen;
import ar.com.netmefy.netmefy.router.Router;
import ar.com.netmefy.netmefy.router.tplink.TLWR941ND.TPLink;

public class TPLinkTestsActivity extends AppCompatActivity {

    TextView tvTypeRouter ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tplink_tests);

        tvTypeRouter = (TextView) findViewById(R.id.tvTypeRouter);
        tvTypeRouter.setText("TPLInk TL-WR941ND");

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

    public void ConnectToSSID(View view){
        //Router router = new TPLink(this.getApplicationContext());

    }



}
