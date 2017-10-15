package ar.com.netmefy.netmefy;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.Response;

import java.util.List;

import ar.com.netmefy.netmefy.adapters.MySimpleNotificationArrayAdapter;
import ar.com.netmefy.netmefy.adapters.elements.NotificationItem;
import ar.com.netmefy.netmefy.services.NMF;
import ar.com.netmefy.netmefy.services.api.Api;
import ar.com.netmefy.netmefy.services.api.entity.notificacionModel;
import ar.com.netmefy.netmefy.services.login.Session;

public class NotificationListActivity extends AppCompatActivity {
    private Session session;

    ListView notificationsListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_list);
        notificationsListView = (ListView) findViewById(R.id.lv_notificaciones);
        session = new Session(getApplicationContext());

        final Api api = Api.getInstance(getApplicationContext());
        final Activity _this = this;
        api.getNotificaciones(NMF.cliente.id, NMF.usuario.usuario_sk, new Response.Listener<List<notificacionModel>>() {
            @Override
            public void onResponse(List<notificacionModel> notificaciones) {
                List<notificacionModel> notificacionesGuardadas = session.getNotificaciones();

                NMF.notificaciones = notificaciones;
                for (notificacionModel nm : NMF.notificaciones ) {
                    for (notificacionModel nmg : notificacionesGuardadas) {
                        if(nm.notificacion_sk == nmg.notificacion_sk){
                            nm.leido = nmg.leido;
                            break;
                        }
                    }
                }

                //session.setNotificaciones(notificaciones);


                //converto la lista en items
                final NotificationItem[] values ;
                values = new NotificationItem[NMF.notificaciones.size()];
                int i = 0;
                for (notificacionModel d : NMF.notificaciones) {
                    values[i] = d.toNotificacionItem();
                    i++;
                }

/*
                final NotificationItem[] values2 = new NotificationItem[]{new NotificationItem(1,"Promo FREDO 2x1","Este lunes mostrando la app tenes 2x1 en 1kg de helado Freddo","20170582",R.drawable.appointment_128, Boolean.TRUE,""),
                        new NotificationItem(2,"Fallo en servicio solucionado","Su servicio a sido restaurado y el problema fue corregido. Por favor no olvide de calificar el servicio ofrecido","20171121",R.drawable.appointment_128, Boolean.FALSE, "qwe123")};
*/
                MySimpleNotificationArrayAdapter adapter = new MySimpleNotificationArrayAdapter(_this, values);


                notificationsListView.setAdapter(adapter);
                notificationsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent notification = new Intent(NotificationListActivity.this, NotificationDetailActivity.class);
                        notification.putExtra("notifcation", values[position].getId());
                        notification.putExtra("ot_id", values[position].getOrdenDeTrabajo());
                        //startActivity(notification);
                        startActivityForResult(notification, 1);
                        //Intent intent = new Intent(this, SyncActivity.class);
                        //intent.putExtra("someData", "Here is some data");
                        //startActivityForResult(device, 1);


                    }
                });
            }
        });


    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_CANCELED){
            Intent refresh = new Intent(this, NotificationListActivity.class);
            startActivity(refresh);
            this.finish();
        }
    }
}
