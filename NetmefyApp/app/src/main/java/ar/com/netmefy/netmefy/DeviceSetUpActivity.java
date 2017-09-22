package ar.com.netmefy.netmefy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class DeviceSetUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_set_up);
        int position= getIntent().getIntExtra("position",0);
    }

    public void saveAndExit(View view){
        this.finish();
    }
}
