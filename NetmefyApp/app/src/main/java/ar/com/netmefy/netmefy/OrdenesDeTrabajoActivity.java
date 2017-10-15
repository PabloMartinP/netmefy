package ar.com.netmefy.netmefy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.firebase.iid.FirebaseInstanceId;

import ar.com.netmefy.netmefy.adapters.MySimpleOrdenesArrayAdapter;
import ar.com.netmefy.netmefy.services.NMF_Info;
import ar.com.netmefy.netmefy.services.api.Api;
import ar.com.netmefy.netmefy.services.api.entity.Tecnico;
import ar.com.netmefy.netmefy.services.login.Session;

public class OrdenesDeTrabajoActivity extends AppCompatActivity {
    ListView lvOrdenesDeTrabajo;
    private Tecnico tecnico;
    private Session session;
    private Api api;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ordenes_de_trabajo);

        session = new Session(getApplicationContext());

        api = Api.getInstance(getApplicationContext());


        tecnico = new Tecnico();
        lvOrdenesDeTrabajo = (ListView) findViewById(R.id.listOTFull);



        MySimpleOrdenesArrayAdapter adapter = new MySimpleOrdenesArrayAdapter(this, tecnico.getOts());
        lvOrdenesDeTrabajo.setAdapter(adapter);

        lvOrdenesDeTrabajo.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent ot = new Intent(OrdenesDeTrabajoActivity.this, PasosOTActivity.class);
                ot.putExtra("tipoGestion", tecnico.getOts()[position].getTipo_ot());
                startActivity(ot);
            }
        });
    }

    public void refreshOtFull(View view){

    }

}
