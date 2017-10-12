package ar.com.netmefy.netmefy.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import ar.com.netmefy.netmefy.MainActivity;
import ar.com.netmefy.netmefy.R;
import ar.com.netmefy.netmefy.services.NMF_Info;
import ar.com.netmefy.netmefy.services.Utils;
import ar.com.netmefy.netmefy.services.api.Api;
import ar.com.netmefy.netmefy.services.api.entity.notificacionModel;

public class RateSupportActivity extends AppCompatActivity {

    Button btSendRateSupport;
    RatingBar rbRateSupport;
    ProgressBar pbRate;
    String ot_id;
    int notificacionId ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_support);
        Bundle inBundle = getIntent().getExtras();
        //userId =  inBundle.get("userId").toString();
        ot_id =  inBundle.get("ot_id").toString();
        notificacionId=  Integer.parseInt(inBundle.get("notificacionId").toString());
        rbRateSupport = (RatingBar) findViewById(R.id.rb_rate_support);
        btSendRateSupport = (Button) findViewById(R.id.bt_send_rate_suport);
        pbRate = (ProgressBar) findViewById(R.id.pb_rate);
        //rbRateSupport.setRating(new Float(4));


        for (notificacionModel nm : NMF_Info.notificaciones) {
            if(nm.notificacion_sk == notificacionId){
                if(nm.ot_calificacion!=0)
                    rbRateSupport.setRating((float)nm.ot_calificacion);
                break;
            }
        }


    }

    public void sendRateOnClick(View view){

        //sendRate();


        int calificacion = Math.round(rbRateSupport.getRating());

        int ot_id_int =  Integer.parseInt(ot_id);
        Api api = Api.getInstance(getApplicationContext());
        api.calificarOt(ot_id_int, calificacion, new Response.Listener() {
            @Override
            public void onResponse(Object response) {

                if(response == null){
                    Toast.makeText(getApplicationContext(), "Error al enviar la calificación", Toast.LENGTH_SHORT).show();

                }else{
                    NMF_Info.marcarNotificacionComoLeida(notificacionId, getApplicationContext());

                    startActivity(new Intent(RateSupportActivity.this,MainActivity.class));
                    Toast.makeText(getApplicationContext(), "calificación enviada", Toast.LENGTH_SHORT).show();
                    finish();
                }

            }
        });
    }

    /*private void sendRate() {
        String url = getResources().getString(R.string.baseUrl) + getResources().getString(R.string.urlRate) + ot_id + "/" + rbRateSupport.getRating() ;
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Intent login = new Intent(RateSupportActivity.this,LoginActivity.class);
                login.putExtra("userId",userId);
                startActivity(login);
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                btSendRateSupport.setVisibility(View.VISIBLE);
                rbRateSupport.setEnabled(true);
                pbRate.setVisibility(View.GONE);
            }
        });
        queue.add(jsObjRequest);
    }*/
}
