package ar.com.netmefy.netmefy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.Date;

import ar.com.netmefy.netmefy.adapters.MySimpleNotificationArrayAdapter;
import ar.com.netmefy.netmefy.adapters.elements.NotificationItem;
import ar.com.netmefy.netmefy.router.Router;

public class NotificationListActivity extends AppCompatActivity {

    ListView notificationsListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_list);
        notificationsListView = (ListView) findViewById(R.id.lv_notificaciones);

        final NotificationItem[] values = new NotificationItem[]{new NotificationItem(1L,"Promo FREDO 2x1","Este lunes mostrando la app tenes 2x1 en 1kg de helado Freddo",new Date(),R.drawable.appointment_128, Boolean.TRUE,""),
                new NotificationItem(2L,"Fallo en servicio solucionado","Su servicio a sido restaurado y el problema fue corregido. Por favor no olvide de calificar el servicio ofrecido",new Date(),R.drawable.appointment_128, Boolean.FALSE, "qwe123")};
        MySimpleNotificationArrayAdapter adapter = new MySimpleNotificationArrayAdapter(this, values);
        notificationsListView.setAdapter(adapter);
        notificationsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent notification = new Intent(NotificationListActivity.this, NotificationDetailActivity.class);
                notification.putExtra("notifcation", values[position].getId());
                notification.putExtra("supportNumber", values[position].getOrdenDeTrabajo());
                startActivity(notification);
                //Intent intent = new Intent(this, SyncActivity.class);
                //intent.putExtra("someData", "Here is some data");
                //startActivityForResult(device, 1);


            }
        });
    }
}
