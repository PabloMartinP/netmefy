package ar.com.netmefy.netmefy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Response;

import ar.com.netmefy.netmefy.login.RateSupportActivity;
import ar.com.netmefy.netmefy.services.NMF;
import ar.com.netmefy.netmefy.services.api.Api;
import ar.com.netmefy.netmefy.services.api.entity.notificacionModel;
import ar.com.netmefy.netmefy.services.login.Session;

public class NotificationDetailActivity extends AppCompatActivity {

    Button calificar;
    TextView tituloNotificacion;
    TextView tv_notif_texto;
    private String ot_id;
    private Session session;
    int notificationId;
    Api api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_detail);
        session = new Session(getApplicationContext());

        api = Api.getInstance(getApplicationContext());
        Bundle inBundle = getIntent().getExtras();
        ot_id =  inBundle.getString("ot_id");
        notificationId =  inBundle.getInt("notifcation");
        notificacionModel notif = null;

        //NMF.marcarNotificacionComoLeida(notificationId, getApplicationContext());

        for (notificacionModel nm : NMF.notificaciones) {
            if(nm.notificacion_sk == notificationId){
                notif = nm;
                /*if(ot_id.isEmpty())//este codigo hace llorar al niño dios
                    notif.leido = true;
                break;*/
            }
        }



        tituloNotificacion = (TextView) findViewById(R.id.tituloNotificacion);
        tv_notif_texto = (TextView) findViewById(R.id.tv_notif_texto);

        tituloNotificacion.setText(notif.notificacion_desc); //TODO: con el titulo de la notificacion setear bien el titulo del activity aca.
        tv_notif_texto.setText(notif.notificacion_texto);

        calificar = (Button) findViewById(R.id.botonCalificar);
        if (ot_id.isEmpty() || notif.notificacion_desc.contains("en curso") ){
            calificar.setVisibility(View.INVISIBLE);
            session.setNotificaciones((NMF.notificaciones));
            api.marcarNotificacionComoLeida(notificationId, new Response.Listener() {
                @Override
                public void onResponse(Object response) {
                    if(response!=null)
                        NMF.marcarNotificacionComoLeida(notificationId, getApplicationContext());

                }
            });

        }else{
            calificar.setVisibility(View.VISIBLE);
            //si no es para calificar no marco como leido
        }
    }

    public void irAClificar (View view){
        Intent calificacion = new Intent(NotificationDetailActivity.this, RateSupportActivity.class);
        calificacion.putExtra("notificacionId", notificationId );
        calificacion.putExtra("ot_id", ot_id);
        calificacion.putExtra("ir_a_main", false);

        startActivityForResult(calificacion, 1);
        finish();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Intent refresh = new Intent(this, NotificationDetailActivity.class);
        //refresh.putExtra("notifcation", notificationId);
        //refresh.putExtra("ot_id", ot_id);
        //startActivity(refresh);
        this.finish();
    }


}
