package ar.com.netmefy.netmefy;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.Response;

import java.util.Date;
import java.util.List;

import ar.com.netmefy.netmefy.adapters.MySimpleGestionArrayAdapter;
import ar.com.netmefy.netmefy.adapters.elements.DeviceItem;
import ar.com.netmefy.netmefy.adapters.elements.GestionItem;
import ar.com.netmefy.netmefy.services.NMF_Info;
import ar.com.netmefy.netmefy.services.api.Api;
import ar.com.netmefy.netmefy.services.api.entity.dispositivoInfo;
import ar.com.netmefy.netmefy.services.api.entity.solicitudListItemModel;
import ar.com.netmefy.netmefy.services.api.entity.tipoOsModel;

public class SolicitudesListActivity extends AppCompatActivity {
    ListView solisitudesHistorialListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solicitudes_list);

        solisitudesHistorialListView = (ListView) findViewById(R.id.lv_solicitudes_historial);


        Api api = Api.getInstance(getApplicationContext());
        final Activity _this = this;
        api.getSolicitudes(new Response.Listener<List<solicitudListItemModel>>() {
            @Override
            public void onResponse(List<solicitudListItemModel> response) {
                NMF_Info.solicitudes = response;


                final GestionItem[] values ;
                values = new GestionItem[NMF_Info.solicitudes.size()];
                int i = 0;
                for (solicitudListItemModel d : NMF_Info.solicitudes) {
                    values[i] = d.toGestionItem();
                    i++;
                }

                //TODO:Remplazar por el array que se saca de la api.
                /*final GestionItem[] values = new GestionItem[]{new GestionItem(1L,"Upgrade Velocidad",new Date(),"Abierto", "Se solicita el upgrade a 10 mb"),
                        new GestionItem(1L,"Upgrade TV HD",new Date(),"Pendiente", "Se solicita el upgrade a 10 mb"),
                        new GestionItem(1L,"Upgrade Velocidad",new Date(),"Cerrado", "Se solicita el upgrade a 30 mb")};*/


                MySimpleGestionArrayAdapter adapter = new MySimpleGestionArrayAdapter(_this, values);
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
        });


    }
}
