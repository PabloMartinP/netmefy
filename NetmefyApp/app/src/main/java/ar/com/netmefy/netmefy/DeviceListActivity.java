package ar.com.netmefy.netmefy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import ar.com.netmefy.netmefy.adapters.MySimpleArrayAdapter;
import ar.com.netmefy.netmefy.adapters.elements.DeviceItem;

public class DeviceListActivity extends AppCompatActivity {

    ListView devicesListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list);
        devicesListView = (ListView) findViewById(R.id.lv_devices);
        DeviceItem[] values = new DeviceItem[] { new DeviceItem("11:22:33:44:55:66","Pepe",R.drawable.guest_w_128, Boolean.FALSE),
                new DeviceItem("11:22:33:44:55:66","Android",R.drawable.download_128, Boolean.FALSE),
                new DeviceItem("11:22:33:44:55:66","iPhone",R.drawable.info_128, Boolean.TRUE),
                new DeviceItem("11:22:33:44:55:66","WindowsMobile",R.drawable.add_link_128, Boolean.FALSE),
                new DeviceItem("11:22:33:44:55:66","Blackberry",R.drawable.eye_128, Boolean.TRUE),
                new DeviceItem("11:22:33:44:55:66","WebOS",R.drawable.help_128, Boolean.TRUE),
                new DeviceItem("11:22:33:44:55:66","Ubuntu",R.drawable.ok_128, Boolean.FALSE),
                new DeviceItem("11:22:33:44:55:66","Windows7",R.drawable.lock_5_128, Boolean.TRUE),
                new DeviceItem("11:22:33:44:55:66","OS/2",R.drawable.save_128, Boolean.FALSE),
                new DeviceItem("11:22:33:44:55:66","Max OS X",R.drawable.search_128, Boolean.TRUE)};


        MySimpleArrayAdapter adapter = new MySimpleArrayAdapter(this, values);
        devicesListView.setAdapter(adapter);
        devicesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent device = new Intent(DeviceListActivity.this, DeviceSetUpActivity.class).putExtra("position", position);
                startActivity(device);
            }
        });

    }
}
