package ar.com.netmefy.netmefy;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import ar.com.netmefy.netmefy.adapters.elements.DeviceItem;
import ar.com.netmefy.netmefy.router.Router;

public class DeviceSetUpActivity extends AppCompatActivity {

    de.hdodenhof.circleimageview.CircleImageView circleImageView2;
    EditText editText2;
    TextView textView15;
    TextView textView16;
    Button button2;
    Router router;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_set_up);

        router = Router.getInstance(getApplicationContext());

        int position= getIntent().getIntExtra("position",0);
        DeviceItem[] values =router.getDeviceConnectedStored();
        /*DeviceItem[] values = new DeviceItem[] { new DeviceItem("11:22:33:44:55:66","Pepe",R.drawable.guest_w_128, "TV", Boolean.FALSE),
                new DeviceItem("11:22:33:44:55:66","Android",R.drawable.download_128, "TV",  Boolean.FALSE),
                new DeviceItem("11:22:33:44:55:66","iPhone",R.drawable.info_128, "TV",  Boolean.TRUE),
                new DeviceItem("11:22:33:44:55:66","WindowsMobile",R.drawable.add_link_128, "TV",  Boolean.FALSE),
                new DeviceItem("11:22:33:44:55:66","Blackberry",R.drawable.eye_128, "TV",  Boolean.TRUE),
                new DeviceItem("11:22:33:44:55:66","WebOS",R.drawable.help_128, "TV",  Boolean.TRUE),
                new DeviceItem("11:22:33:44:55:66","Ubuntu",R.drawable.ok_128, "TV",  Boolean.FALSE),
                new DeviceItem("11:22:33:44:55:66","Windows7",R.drawable.lock_5_128, "TV",  Boolean.TRUE),
                new DeviceItem("11:22:33:44:55:66","OS/2",R.drawable.save_128, "TV",  Boolean.FALSE),
                new DeviceItem("11:22:33:44:55:66","Max OS X",R.drawable.search_128, "TV",  Boolean.TRUE)};
        */

        circleImageView2 = (de.hdodenhof.circleimageview.CircleImageView) findViewById(R.id.tvDeviceConnected2);
        textView15 = (TextView) findViewById(R.id.textView15);
        textView16 = (TextView) findViewById(R.id.textView16);
        editText2 = (EditText) findViewById(R.id.editText2);
        button2 = (Button) findViewById(R.id.button2);

        circleImageView2.setImageResource(values[position].getResId());
        textView15.setText(values[position].getMac());
        textView15.setText(values[position].getTipoDeDispostivo());
        editText2.setText(values[position].getName());
        if (values[position].getBlocked()){
            button2.setText("Desbloquear");
            button2.setBackgroundColor(Color.parseColor("#ff99cc00"));
        }else{
            button2.setText("Bloquear");
            button2.setBackgroundColor(Color.parseColor("#ffff4444"));
        }

    }

    public void saveAndExit(View view){
        this.finish();
    }
}
