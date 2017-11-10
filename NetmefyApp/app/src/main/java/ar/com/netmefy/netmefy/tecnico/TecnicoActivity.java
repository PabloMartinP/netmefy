package ar.com.netmefy.netmefy.tecnico;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
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

import java.util.List;

import ar.com.netmefy.netmefy.OrdenesDeTrabajoActivity;
import ar.com.netmefy.netmefy.PasosOTActivity;
import ar.com.netmefy.netmefy.PruebasActivity;
import ar.com.netmefy.netmefy.R;
import ar.com.netmefy.netmefy.adapters.MySimpleOrdenesArrayAdapter;
import ar.com.netmefy.netmefy.login.UserIdActivity;
import ar.com.netmefy.netmefy.router.Device;
import ar.com.netmefy.netmefy.services.NMF;
import ar.com.netmefy.netmefy.services.Utils;
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
    Activity _this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tecnico);

        legajoTecnico = (TextView) findViewById(R.id.legajoTecnico);
        mailTecnico = (TextView) findViewById(R.id.mailTecnico);
        nombreDeTecnico = (TextView) findViewById(R.id.nombreDeTecnico);
        calificacionTecnico = (RatingBar) findViewById(R.id.calificacionTecnicoOk);
        lvOrdenesDeTrabajo = (ListView) findViewById(R.id.lvOrdenesDeTrabajo);

        calificacionTecnico.setIsIndicator(true);
        session = new Session(getApplicationContext());

        api = Api.getInstance(getApplicationContext());
        _this = this;

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
                cargarInfoTecnico();
                cargarOts();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.hide();
                pd.dismiss();
                Toast.makeText(getApplicationContext(), "Error al traer el tecnico", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void cargarOts(){
        MySimpleOrdenesArrayAdapter adapter = new MySimpleOrdenesArrayAdapter(getApplicationContext(), NMF.tecnico.getOts());
        lvOrdenesDeTrabajo.setAdapter(adapter);

        lvOrdenesDeTrabajo.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent ot = new Intent(TecnicoActivity.this, PasosOTActivity.class);
                //ot.putExtra("tipoGestion", NMF.tecnico.getOts()[position].getTipo_ot());
                int ot_id = NMF.tecnico.getOts()[position].getOt_id();
                ot.putExtra("ot_id", ot_id);

                //startActivity(ot);
                startActivityForResult(ot, 1);


                /*
                int statusOt = NMF.tecnico.getOts()[position].getEstado();
                if (statusOt == 1) { //ESTADO ABIERTO
                    view.setBackgroundColor(Color.parseColor("#aaaaaa00"));
                    //rowView.setBackgroundColor(Color.BLUE);
                } else if (statusOt == 2) { //ESTADO EN CURSO
                    view.setBackgroundColor(Color.parseColor("#ffff8800"));
                    //rowView.setBackgroundColor(Color.YELLOW);
                } else if (statusOt==3){//ESTADO CERRADO
                    view.setBackgroundColor(Color.parseColor("#ff99cc00"));
                    //rowView.setBackgroundColor(Color.GREEN);
                }*/
            }
        });
    }
    private void cargarInfoTecnico(){
        legajoTecnico.setText(NMF.tecnico.getId());
        mailTecnico.setText(NMF.tecnico.getEmail());
        nombreDeTecnico.setText(NMF.tecnico.getNombre());
        calificacionTecnico.setRating(NMF.tecnico.getCalificacion());
    }

    public void goToSetOfTest(View view){
        Intent tests = new Intent(TecnicoActivity.this, PruebasActivity.class);
        tests.putExtra("ot_cliente_sk", -1);
        tests.putExtra("ot_id", -1);

        //startActivity(tests);
        startActivityForResult(tests, 1);

    }



    public void refreshOt (View view){
        getTecnicoFromApi();
    }

    public void goToFullOt(View view){
        Intent intent = new Intent(TecnicoActivity.this, OrdenesDeTrabajoActivity.class);
        //startActivity(tests);
        startActivityForResult(intent, 1);
    }


    public void logOut(View view){
        session.setUserId("");
        session.setUserType("");
        Intent login = new Intent(TecnicoActivity.this, UserIdActivity.class);
        startActivity(login);
        finish();
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
