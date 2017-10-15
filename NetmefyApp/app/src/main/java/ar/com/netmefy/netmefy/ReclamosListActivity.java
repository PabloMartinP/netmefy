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

import ar.com.netmefy.netmefy.adapters.MySimpleGestionArrayAdapter;
import ar.com.netmefy.netmefy.adapters.elements.GestionItem;
import ar.com.netmefy.netmefy.services.NMF;
import ar.com.netmefy.netmefy.services.api.Api;
import ar.com.netmefy.netmefy.services.api.entity.reclamoListItemModel;

public class ReclamosListActivity extends AppCompatActivity {
    ListView reclamosHistorialListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reclamos_list);
        reclamosHistorialListView = (ListView) findViewById(R.id.lv_reclamos_historial);

        Api api = Api.getInstance(getApplicationContext());
        final Activity _this = this;

        api.getReclamos(new Response.Listener<List<reclamoListItemModel>>() {
            @Override
            public void onResponse(List<reclamoListItemModel> response) {
                NMF.reclamos = response;


                final GestionItem[] values ;
                values = new GestionItem[NMF.reclamos.size()];
                int i = 0;
                for (reclamoListItemModel d : NMF.reclamos) {
                    values[i] = d.toGestionItem();
                    i++;
                }
/*
                //TODO:Remplazar por el array que se saca de la api.
                final GestionItem[] values = new GestionItem[]{new GestionItem(1,"Upgrade Velocidad",new Date().toString(),"Abierto", "Se solicita el upgrade a 10 mb"),
                        new GestionItem(2,"Upgrade TV HD",new Date().toString(),"Pendiente", "Se solicita el upgrade a 10 mb"),
                        new GestionItem(3,"Upgrade Velocidad",new Date().toString(),"Cerrado", "Se solicita el upgrade a 30 mb")};
*/

                MySimpleGestionArrayAdapter adapter = new MySimpleGestionArrayAdapter(_this, values);
                reclamosHistorialListView.setAdapter(adapter);
                reclamosHistorialListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent reclamo = new Intent(ReclamosListActivity.this, VerGestionActivity.class);
                        reclamo.putExtra("idGestion", values[position].getIdDeGestion());
                        reclamo.putExtra("tipoGestion", 2);//1 para solicitud 2 para reclamo esto es para setear el color del titulo
                        startActivity(reclamo);
                    }
                });
            }
        });




    }
}
