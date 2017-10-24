package ar.com.netmefy.netmefy;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import ar.com.netmefy.netmefy.router.Router;
import ar.com.netmefy.netmefy.services.NMF;
import ar.com.netmefy.netmefy.services.Utils;
import ar.com.netmefy.netmefy.services.WifiUtils;
import ar.com.netmefy.netmefy.services.api.Api;
import ar.com.netmefy.netmefy.services.api.entity.DeviceModel;
import ar.com.netmefy.netmefy.services.api.entity.dispositivoInfo;

public class DeviceSetUpActivity extends AppCompatActivity {

    de.hdodenhof.circleimageview.CircleImageView circleImageView2;
    EditText et_apodo;
    TextView et_tipo;
    TextView tv_mac;
    Button btn_bloquear;
    boolean cambioElBloqueo;
    Router router;
    dispositivoInfo device_selected;
    ProgressDialog progressBar;
    public Activity _this;
    Api api;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_set_up);
        _this = this;
        router = Router.getInstance(getApplicationContext());

        String mac= getIntent().getStringExtra("mac");

        device_selected = NMF.findDeviceByMac(mac);


        circleImageView2 = (de.hdodenhof.circleimageview.CircleImageView) findViewById(R.id.tvDeviceConnected2);
        et_tipo = (TextView) findViewById(R.id.et_device_config_tipo);
        tv_mac = (TextView) findViewById(R.id.tv_device_config_mac);
        et_apodo = (EditText) findViewById(R.id.et_device_config_apodo);
        btn_bloquear = (Button) findViewById(R.id.button2);

        int resId = Utils.getResIdFromImageName(this, "guest_w_128");
        circleImageView2.setImageResource(resId);
        tv_mac.setText(device_selected.mac);
        et_tipo.setText(device_selected.tipo);
        et_apodo.setText(device_selected.apodo);

        _set_blocked(device_selected.bloqueado);

        api = Api.getInstance(getApplicationContext());

    }

    private void _set_blocked(boolean blocked){
        if(blocked){
            btn_bloquear.setText("Desbloquear");
            btn_bloquear.setBackgroundColor(Color.parseColor("#ff99cc00"));
        }else{
            btn_bloquear.setText("Bloquear");
            btn_bloquear.setBackgroundColor(Color.parseColor("#ffff4444"));
        }
    }

    private boolean esta_bloqueado(){
        return btn_bloquear.getText().toString().toLowerCase().startsWith("bloq");
    }

    public void block(View view){
        _set_blocked(esta_bloqueado());
    }

    public void saveAndExit(View view){

        progressBar = Utils.getProgressBar(this, "Actualizando ...");
        progressBar.show();

        String apodo;
        String tipo;
        String mac;
        boolean bloqueado;
        apodo = et_apodo.getText().toString();
        tipo = et_tipo.getText().toString();
        bloqueado = !esta_bloqueado();
        mac = tv_mac.getText().toString();

        /////////////////////////////////////////////////
        final dispositivoInfo di = NMF.findDeviceByMac(mac);

        //para saber si cambio el bloqueo o no, si cambio tengo que actualiar en el router, sino nop
        cambioElBloqueo =di.bloqueado != bloqueado;

        di.apodo = apodo;
        di.tipo  = tipo;
        di.bloqueado = bloqueado;
        /*DeviceModel dm = di.toDeviceModel();
        dm.cliente_sk = NMF.cliente.id;
        dm.router_sk = NMF.cliente.router.router_sk;*/

        String myMac = WifiUtils.getMacAddress(getApplicationContext());

        if(!myMac.equalsIgnoreCase(mac)){
            blockOnRouter(di);
            /*
            final DeviceModel dm = di.toDeviceModel();
            dm.cliente_sk = NMF.cliente.id;
            dm.router_sk = NMF.cliente.router.router_sk;
            //saveOnApi(dm, di);

            api.updateDevice(dm, new Response.Listener() {
                @Override
                public void onResponse(Object response) {
                    if(!response.toString().equals("error")){
                        NMF.setDevice(di);

                        progressBar.hide();
                        progressBar.dismiss();
                        _this.setResult(RESULT_OK, null);
                        _this.finish();
                    }else{
                        progressBar.hide();
                        progressBar.dismiss();
                    }
                }
            });*/
        }else{
            Toast.makeText(getApplicationContext(), "No puede autobloquearse!", Toast.LENGTH_SHORT ).show();
            progressBar.hide();
        }
    }

    private void saveOnApi(DeviceModel dm, final dispositivoInfo di) {
        final Activity _this = this;

        api.updateDevice(dm, new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                if(!response.toString().equals("error")){
                    NMF.setDevice(di);
                    //_this.finish();
                    progressBar.hide();
                    progressBar.dismiss();
                    _this.setResult(RESULT_OK, null);

                    _this.finish();
                }else{
                    progressBar.hide();
                    progressBar.dismiss();
                }
            }
        });
    }

    public void blockOnRouter(final dispositivoInfo di){
        if(true){
            //final Api api = Api.getInstance(getApplicationContext());
            final DeviceModel dm = di.toDeviceModel();
            dm.cliente_sk = NMF.cliente.id;
            dm.router_sk = NMF.cliente.router.router_sk;
            if(di.bloqueado){
                router.addBlockByMac(di.mac, new Response.Listener() {
                    @Override
                    public void onResponse(final Object response) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setMessage(response.toString());
                            }
                        });

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //progressBar.setMessage(response.toString());
                        progressBar.dismiss();
                        Toast.makeText(getApplicationContext(), "Err: "+error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }, new Response.Listener() {
                    @Override
                    public void onResponse(Object response) {
                        saveOnApi(dm, di);
                    }
                });
            }else{
                router.removeBlockByMac(di.mac, new Response.Listener() {
                    @Override
                    public void onResponse(Object response) {

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }, new Response.Listener() {
                    @Override
                    public void onResponse(Object response) {

                        saveOnApi(dm, di);

                    }
                });

            }
        }//fin if di.bloquedo

    }

}
