package ar.com.netmefy.netmefy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.Date;

import ar.com.netmefy.netmefy.adapters.MySimpleGestionArrayAdapter;
import ar.com.netmefy.netmefy.adapters.elements.GestionItem;

public class SolicitudesListActivity extends AppCompatActivity {
    ListView solisitudesHistorialListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solicitudes_list);

        solisitudesHistorialListView = (ListView) findViewById(R.id.lv_solicitudes_historial);

        //TODO:Remplazar por el array que se saca de la api.
        final GestionItem[] values = new GestionItem[]{new GestionItem(1L,"Upgrade Velocidad",new Date(),"Abierto", "Se solicita el upgrade a 10 mb"),
                new GestionItem(1L,"Upgrade TV HD",new Date(),"Pendiente", "Se solicita el upgrade a 10 mb"),
                new GestionItem(1L,"Upgrade Velocidad",new Date(),"Cerrado", "Se solicita el upgrade a 30 mb")};


        MySimpleGestionArrayAdapter adapter = new MySimpleGestionArrayAdapter(this, values);
        solisitudesHistorialListView.setAdapter(adapter);
        solisitudesHistorialListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent solicitud = new Intent(SolicitudesListActivity.this, VerGestionActivity.class);
                solicitud.putExtra("idGestion", values[position].getIdDeGestion());
                solicitud.putExtra("tipoGestion", 1); //1 para solicitud 2 para reclamo esto es para setear el color del titulo
                startActivity(solicitud);
            }
        });
    }
}
