package ar.com.netmefy.netmefy.router.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.List;

import ar.com.netmefy.netmefy.R;
import ar.com.netmefy.netmefy.router.Device;
import ar.com.netmefy.netmefy.router.Router;
import ar.com.netmefy.netmefy.router.tplink.TLWR941ND.ResponseTPLink;
import ar.com.netmefy.netmefy.router.tplink.TLWR941ND.TPLink;

public class DevicesConnectedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devices_connected);
    }

    public void showDevicesConnected(View view){
        final Button btn =(Button) findViewById(R.id.btnDevicesConnected);
        btn.setText("showDevicesConnected ...");
        Router router= new TPLink(this.getApplicationContext());
        final Context context = this.getApplicationContext();
        router.listDevicesConnected(
                new Response.Listener<List<Device>>() {
                    @Override
                    public void onResponse(List<Device> devices) {

                        /*
                        si bien esta en devices en el arrayadapter necesita estar en list<string>
                        Al ejecutar este metodo carga la lista en una var static listDevices
                        TODO: Crear una clase que contenga a List<Device>
                         */

                        List<String> listDevices = ResponseTPLink.getListDevicesString();


                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                                context, android.R.layout.simple_list_item_1, listDevices);

                        ListView lvDevicesConnected = (ListView )findViewById(R.id.lvDevicesConnected);

                        lvDevicesConnected.setAdapter(arrayAdapter);

                        btn.setText("showDevicesConnected OKOK...");

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        btn.setText("Error " + error.getMessage());
                    }
                }
        );
    }
}
