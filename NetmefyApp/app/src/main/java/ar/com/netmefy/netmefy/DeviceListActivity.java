package ar.com.netmefy.netmefy;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.List;

import ar.com.netmefy.netmefy.adapters.MySimpleArrayAdapter;
import ar.com.netmefy.netmefy.adapters.elements.DeviceItem;
import ar.com.netmefy.netmefy.router.Device;
import ar.com.netmefy.netmefy.router.Router;

public class DeviceListActivity extends AppCompatActivity {

    ListView devicesListView;
    Router router;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list);
        devicesListView = (ListView) findViewById(R.id.lv_devices);
        router = Router.getInstance(getApplicationContext());


        /*DeviceItem[] values = new DeviceItem[] { new DeviceItem("11:22:33:44:55:66","Pepe",R.drawable.guest_w_128, "TV",  Boolean.FALSE),
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

        final Activity activity = this;
        router.listDevicesConnected(new Response.Listener<List<Device>>() {
            @Override
            public void onResponse(List<Device> devices) {

                DeviceItem[] values ;
                values = new DeviceItem[devices.size()];
                int i = 0;
                for (Device d : devices) {
                    values[i] = d.toDeviceItem();
                    i++;
                }

                router.setDeviceConnected(values);
                MySimpleArrayAdapter adapter = new MySimpleArrayAdapter(activity, values);
                devicesListView.setAdapter(adapter);
                devicesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent device = new Intent(DeviceListActivity.this, DeviceSetUpActivity.class).putExtra("position", position);
                        startActivity(device);
                    }
                });

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });




    }
}
