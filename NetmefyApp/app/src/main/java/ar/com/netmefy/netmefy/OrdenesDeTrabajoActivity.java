package ar.com.netmefy.netmefy;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import ar.com.netmefy.netmefy.adapters.MySimpleOrdenesArrayAdapter;
import ar.com.netmefy.netmefy.services.NMF;
import ar.com.netmefy.netmefy.services.Utils;
import ar.com.netmefy.netmefy.services.api.Api;
import ar.com.netmefy.netmefy.services.api.entity.Tecnico;
import ar.com.netmefy.netmefy.services.login.Session;

public class OrdenesDeTrabajoActivity extends AppCompatActivity {
    ListView lvOrdenesDeTrabajo;
    private Session session;
    private Api api;
    Activity _this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ordenes_de_trabajo);

        session = new Session(getApplicationContext());

        api = Api.getInstance(getApplicationContext());
        _this = this;

        lvOrdenesDeTrabajo = (ListView) findViewById(R.id.listOTFull);

        cargarOts();
    }

    private void cargarOts(){
        MySimpleOrdenesArrayAdapter adapter = new MySimpleOrdenesArrayAdapter(getApplicationContext(), NMF.tecnico.getOts());
        lvOrdenesDeTrabajo.setAdapter(adapter);

        lvOrdenesDeTrabajo.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent ot = new Intent(OrdenesDeTrabajoActivity.this, PasosOTActivity.class);
                //ot.putExtra("tipoGestion", NMF.tecnico.getOts()[position].getTipo_ot());
                ot.putExtra("ot_id", NMF.tecnico.getOts()[position].getOt_id());
                //startActivity(ot);
                startActivityForResult(ot, 1);

            }
        });
    }

    public void refreshOtFull(View view){
        getTecnicoFromApi();
    }
    private void getTecnicoFromApi(){
        final ProgressDialog pd = Utils.getProgressBar(_this, "Obteniendo información del técnico");

        pd.show();
        api.getTecnico(NMF.tipoUsuarioApp.username, new Response.Listener<Tecnico>() {
            @Override
            public void onResponse(Tecnico response) {
                pd.hide();
                pd.dismiss();
                NMF.tecnico = response;
                cargarOts();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.hide();
                pd.dismiss();
                Toast.makeText(getApplicationContext(), "Error al traer el técnico", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getTecnicoFromApi();
        /*Toast.makeText(getApplicationContext(), "recargar", Toast.LENGTH_SHORT).show();

        if(resultCode==RESULT_CANCELED){
            String jj;
            Toast.makeText(getApplicationContext(), "recargar", Toast.LENGTH_SHORT).show();
        }*/
    }

}
