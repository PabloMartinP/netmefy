package ar.com.netmefy.netmefy;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.List;

import ar.com.netmefy.netmefy.adapters.MySimpleArrayAdapter;
import ar.com.netmefy.netmefy.adapters.elements.DeviceItem;
import ar.com.netmefy.netmefy.router.Device;
import ar.com.netmefy.netmefy.router.Router;
import ar.com.netmefy.netmefy.services.NMF;
import ar.com.netmefy.netmefy.services.api.entity.dispositivoInfo;

public class DeviceListActivity extends AppCompatActivity {

    ListView devicesListView;
    Router router;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list);
        devicesListView = (ListView) findViewById(R.id.lv_devices);
        router = Router.getInstance(getApplicationContext());

        final Activity _this = this;
        final Context _ctx= this.getApplicationContext();
        router.listDevicesConnected(new Response.Listener<List<Device>>() {
            @Override
            public void onResponse(List<Device> devices) {
                NMF.updateDevicesConnected(devices, getApplicationContext());

                List<dispositivoInfo> list_connected = NMF.getDevicesConnected(true);

                final DeviceItem[] values ;
                values = new DeviceItem[list_connected.size()];
                int i = 0;
                for (dispositivoInfo d : list_connected) {
                    values[i] = d.toDeviceItem();
                    i++;
                }


                MySimpleArrayAdapter adapter = new MySimpleArrayAdapter(_ctx, values, _this);
                devicesListView.setAdapter(adapter);
                devicesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent device = new Intent(DeviceListActivity.this, DeviceSetUpActivity.class).putExtra("mac", values[position].getMac());
                        startActivityForResult(device, 1);


                    }
                });

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });


    }

    public void block(View view){

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            Intent refresh = new Intent(this, DeviceListActivity.class);
            startActivity(refresh);
            this.finish();
        }
    }
}
