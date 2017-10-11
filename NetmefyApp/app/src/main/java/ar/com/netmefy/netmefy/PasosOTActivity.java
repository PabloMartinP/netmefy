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

import ar.com.netmefy.netmefy.adapters.MySimplePasosArrayAdapter;
import ar.com.netmefy.netmefy.adapters.elements.PasoItem;
import ar.com.netmefy.netmefy.services.NMF_Info;
import ar.com.netmefy.netmefy.services.api.Api;
import ar.com.netmefy.netmefy.services.api.entity.Tecnico;
import ar.com.netmefy.netmefy.services.login.Session;

public class PasosOTActivity extends AppCompatActivity {
    private String tipoGestion;
    private Session session;
    private Api api;
    ListView listPasos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pasos_ot);
        Bundle inBundle = getIntent().getExtras();
        tipoGestion =  inBundle.getString("tipoGestion");
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
        final PasoItem[] pasosARealizar = crearPasosSegunGestion(tipoGestion);
        listPasos = (ListView) findViewById(R.id.listPasos);



        MySimplePasosArrayAdapter adapter = new MySimplePasosArrayAdapter(this, pasosARealizar);
        listPasos.setAdapter(adapter);

        listPasos.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(pasosARealizar[position].getPruebasDeRed()) {
                    Intent pruebas = new Intent(PasosOTActivity.this, PruebasActivity.class);
                    startActivity(pruebas);
                    //TODO: CUANDO VUELVE DE LAS PRUEVAS DEBERIA PONERSE EN DONE ESTA ACTIVIDAD
                }
                pasosARealizar[position].setDone(Boolean.TRUE);
                if (position == pasosARealizar.length-1){
                    finish();
                }
            }
        });
    }

    private PasoItem[] crearPasosSegunGestion(String tipoGestion) {

        if (tipoGestion.equalsIgnoreCase("instalacion")) {
            return new PasoItem[]{
                    new PasoItem("1 Definir lugar para el Router WIFI", "", "", Boolean.FALSE, Boolean.FALSE, R.drawable.location_128),
                    new PasoItem("2 Realizar conexión de bajada hasta el router", "(tv, consolas, desktops)", "", Boolean.FALSE, Boolean.FALSE, R.drawable.flecha_128),
                    new PasoItem("3 Cablear con UTP dispositivos cercanos", "(DSL o DOCSIS)", "", Boolean.FALSE, Boolean.FALSE, R.drawable.utp_128),
                    new PasoItem("4 Realizar configuraciones de acceso", "", "", Boolean.FALSE, Boolean.FALSE, R.drawable.access_128),
                    new PasoItem("5 Llamar a call center", "Recibir ok de instalación", "", Boolean.FALSE, Boolean.FALSE, R.drawable.assistant_128),
                    new PasoItem("6 Configuración de parámetros wifi", "", "", Boolean.FALSE, Boolean.FALSE, R.drawable.data_configuration_128),
                    new PasoItem("7 Realizar pruebas de velocidad, potencia y navegación", "", "", Boolean.FALSE, Boolean.TRUE, R.drawable.wrench_128),
                    new PasoItem("8 Cerificar navegación por parte del cliente", "", "", Boolean.FALSE, Boolean.FALSE, R.drawable.aceptar_128)
            };
        }else{
            return new PasoItem[]{
                    new PasoItem("1 Consultar falla o síntomas al cliente", "", "", Boolean.FALSE, Boolean.FALSE, R.drawable.cliente_hablar_128),
                    new PasoItem("2 Revisar cable de bajada hasta el router", "", "", Boolean.FALSE, Boolean.FALSE, R.drawable.flecha_128),
                    new PasoItem("3 Revisar cableados a dispositivos", "(tv, consolas, desktops)", "", Boolean.FALSE, Boolean.FALSE, R.drawable.utp_128),
                    new PasoItem("4 Verificar configuraciones de acceso", "(DSL o DOCSIS)", "", Boolean.FALSE, Boolean.FALSE, R.drawable.access_128),
                    new PasoItem("5 Verificar configuración de parámetros WiFi", "", "", Boolean.FALSE, Boolean.FALSE, R.drawable.data_configuration_128),
                    new PasoItem("6 Cambiar módem y reconfigurar de ser necesario", "", "", Boolean.FALSE, Boolean.FALSE, R.drawable.router_2_128_white),
                    new PasoItem("7 Realizar pruebas de velocidad, potencia y navegación", "", "", Boolean.FALSE, Boolean.TRUE, R.drawable.wrench_128),
                    new PasoItem("8 Cerificar navegación por parte del cliente", "", "", Boolean.FALSE, Boolean.FALSE, R.drawable.aceptar_128)
            };
        }

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
}
