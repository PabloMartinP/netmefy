package ar.com.netmefy.netmefy.tecnico;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.firebase.iid.FirebaseInstanceId;

import ar.com.netmefy.netmefy.GestionesActivity;
import ar.com.netmefy.netmefy.MainActivity;
import ar.com.netmefy.netmefy.OrdenesDeTrabajoActivity;
import ar.com.netmefy.netmefy.PasosOTActivity;
import ar.com.netmefy.netmefy.PruebasActivity;
import ar.com.netmefy.netmefy.R;
import ar.com.netmefy.netmefy.adapters.MySimpleOrdenesArrayAdapter;
import ar.com.netmefy.netmefy.adapters.elements.OtItem;
import ar.com.netmefy.netmefy.login.UserIdActivity;
import ar.com.netmefy.netmefy.services.NMF_Info;
import ar.com.netmefy.netmefy.services.api.Api;
import ar.com.netmefy.netmefy.services.api.entity.Tecnico;
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
        api.LogIn("netmefy", "yfemten", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                saveToken();
            }
        });
        api = Api.getInstance(getApplicationContext());

        saveToken();
        //TODO: REMPLAZAR POR LO QUE SE TRAE DE LA API
        tecnico = new Tecnico();
        legajoTecnico.setText(tecnico.getId());
        mailTecnico.setText(tecnico.getEmail());
        nombreDeTecnico.setText(tecnico.getNombre());
        calificacionTecnico.setRating(tecnico.getCalificacion());


        MySimpleOrdenesArrayAdapter adapter = new MySimpleOrdenesArrayAdapter(this, tecnico.getOts());
        lvOrdenesDeTrabajo.setAdapter(adapter);

        lvOrdenesDeTrabajo.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent ot = new Intent(TecnicoActivity.this, PasosOTActivity.class);
                ot.putExtra("tipoGestion", tecnico.getOts()[position].getTipoDeGestion());
                startActivity(ot);
            }
        });

    }

    private void saveToken(){
       try {
            session.getClientInfo();
            if(NMF_Info.clientInfo!=null){
                api.saveFirebaseToken(NMF_Info.clientInfo.id, session.getUserType(), FirebaseInstanceId.getInstance().getToken(), new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        response = response.toString();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String j= error.toString();
                    }
                });
            }

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void goToSetOfTest(View view){
        Intent tests = new Intent(TecnicoActivity.this, PruebasActivity.class);
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
