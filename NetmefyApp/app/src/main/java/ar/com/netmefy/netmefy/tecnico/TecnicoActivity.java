package ar.com.netmefy.netmefy.tecnico;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import ar.com.netmefy.netmefy.OrdenesDeTrabajoActivity;
import ar.com.netmefy.netmefy.PasosOTActivity;
import ar.com.netmefy.netmefy.PruebasActivity;
import ar.com.netmefy.netmefy.R;
import ar.com.netmefy.netmefy.adapters.MySimpleOrdenesArrayAdapter;
import ar.com.netmefy.netmefy.login.UserIdActivity;
import ar.com.netmefy.netmefy.services.NMF_Info;
import ar.com.netmefy.netmefy.services.api.Api;
import ar.com.netmefy.netmefy.services.api.entity.Tecnico;
import ar.com.netmefy.netmefy.services.api.entity.clientInfo;
import ar.com.netmefy.netmefy.services.login.Session;

public class TecnicoActivity extends AppCompatActivity {

    private Session session;
    private Button techLogOut;
    private Api api;
    private Tecnico tecnico;
    TextView legajoTecnico;
    TextView mailTecnico;
    TextView nombreDeTecnico;
    RatingBar calificacionTecnico;
    ListView lvOrdenesDeTrabajo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tecnico);

        legajoTecnico = (TextView) findViewById(R.id.legajoTecnico);
        mailTecnico = (TextView) findViewById(R.id.mailTecnico);
        nombreDeTecnico = (TextView) findViewById(R.id.nombreDeTecnico);
        calificacionTecnico = (RatingBar) findViewById(R.id.calificacionTecnico);
        lvOrdenesDeTrabajo = (ListView) findViewById(R.id.lvOrdenesDeTrabajo);


        session = new Session(getApplicationContext());

        api = Api.getInstance(getApplicationContext());
        /*api.LogIn("netmefy", "yfemten", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                saveToken();
            }
        });*/

        final Activity _this = this;
        //saveToken();
        //TODO: REMPLAZAR POR LO QUE SE TRAE DE LA API
        api.getTecnico(NMF_Info.tipoUsuarioApp.username, new Response.Listener<Tecnico>() {
            @Override
            public void onResponse(Tecnico response) {
                NMF_Info.tecnico = response;

                legajoTecnico.setText(NMF_Info.tecnico.getId());
                mailTecnico.setText(NMF_Info.tecnico.getEmail());
                nombreDeTecnico.setText(NMF_Info.tecnico.getNombre());
                calificacionTecnico.setRating(NMF_Info.tecnico.getCalificacion());

                ///////////////////////////////
                MySimpleOrdenesArrayAdapter adapter = new MySimpleOrdenesArrayAdapter(getApplicationContext(), NMF_Info.tecnico.getOts());
                lvOrdenesDeTrabajo.setAdapter(adapter);

                lvOrdenesDeTrabajo.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent ot = new Intent(TecnicoActivity.this, PasosOTActivity.class);
                        //ot.putExtra("tipoGestion", NMF_Info.tecnico.getOts()[position].getTipo_ot());
                        ot.putExtra("ot_id", NMF_Info.tecnico.getOts()[position].getOt_id());
                        startActivity(ot);
                    }
                });
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error al traer el tecnico", Toast.LENGTH_SHORT).show();
            }
        });



    }

    public void goToSetOfTest(View view){
        Intent tests = new Intent(TecnicoActivity.this, PruebasActivity.class);
        tests.putExtra("ot_cliente_sk", -1);
        tests.putExtra("ot_id", -1);

        startActivity(tests);
    }

    public void refreshOt (View view){
        //TODO REFRESCAR LISTA
    }

    public void goToFullOt(View view){
        Intent tests = new Intent(TecnicoActivity.this, OrdenesDeTrabajoActivity.class);
        startActivity(tests);
    }


    public void logOut(View view){
        session.setUserId("");
        session.setUserType("");
        Intent login = new Intent(TecnicoActivity.this, UserIdActivity.class);
        startActivity(login);
    }
}
