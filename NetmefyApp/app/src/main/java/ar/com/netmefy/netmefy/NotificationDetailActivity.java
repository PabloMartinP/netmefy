package ar.com.netmefy.netmefy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import ar.com.netmefy.netmefy.login.RateSupportActivity;

public class NotificationDetailActivity extends AppCompatActivity {

    Button calificar;
    TextView tituloNotificacion;
    private String supportNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_detail);
        Bundle inBundle = getIntent().getExtras();
        supportNumber =  inBundle.getString("supportNumber");
        Long notificationId =  inBundle.getLong("notifcation");//TODO: se puede usar para saber el id de la notificacion.

        tituloNotificacion = (TextView) findViewById(R.id.tituloNotificacion);
        tituloNotificacion.setText("Aca va el titulo de la notificacion"); //TODO: con el titulo de la notificacion setear bien el titulo del activity aca.

        calificar = (Button) findViewById(R.id.botonCalificar);
        if (supportNumber.isEmpty()){
            calificar.setVisibility(View.INVISIBLE);
        }else{
            calificar.setVisibility(View.VISIBLE);
        }
    }

    public void irAClificar (View view){
        Intent calificacion = new Intent(NotificationDetailActivity.this, RateSupportActivity.class);
        calificacion.putExtra("userId", "ACA VA EL USER"); //TODO: Poner el user que va
        calificacion.putExtra("supportNumber", supportNumber);
        startActivity(calificacion);
    }
}
