package ar.com.netmefy.netmefy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class GestionesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestiones);
    }

    public void goToReclamos(View view){
        Intent reclamos = new Intent(GestionesActivity.this, ReclamosActivity.class);
        startActivity(reclamos);
    }

    public void goToSolicitudes(View view){
        Intent solicitudes = new Intent(GestionesActivity.this, SolicitudesActivity.class);
        startActivity(solicitudes);
    }
}
