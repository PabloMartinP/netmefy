package ar.com.netmefy.netmefy;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Response;

import ar.com.netmefy.netmefy.adapters.MySimplePasosArrayAdapter;
import ar.com.netmefy.netmefy.adapters.elements.OtItem;
import ar.com.netmefy.netmefy.adapters.elements.PasoItem;
import ar.com.netmefy.netmefy.services.NMF;
import ar.com.netmefy.netmefy.services.api.Api;
import ar.com.netmefy.netmefy.services.login.Session;

public class PasosOTActivity extends AppCompatActivity {
    private String tipoGestion;
    private Session session;
    private Api api;
    ListView listPasos;
    int ot_id;
    OtItem ot;
    PasoItem[] pasosARealizar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pasos_ot);
        Bundle inBundle = getIntent().getExtras();
        tipoGestion =  inBundle.getString("tipoGestion");
        ot_id =  inBundle.getInt("ot_id", -1);
        session = new Session(getApplicationContext());

        ot = NMF.tecnico.buscarOt(ot_id);

        api = Api.getInstance(getApplicationContext());

        pasosARealizar = crearPasosSegunGestion(ot.getTipo_ot());
        listPasos = (ListView) findViewById(R.id.listPasos);



        MySimplePasosArrayAdapter adapter = new MySimplePasosArrayAdapter(this, pasosARealizar);
        listPasos.setAdapter(adapter);

        listPasos.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(pasosARealizar[position].getPruebasDeRed()) {
                    Intent pruebas = new Intent(PasosOTActivity.this, PruebasActivity.class);
                    pruebas.putExtra("ot_id", ot.getOt_id());
                    pruebas.putExtra("ot_cliente_sk", ot.getCliente_sk());
                    startActivity(pruebas);

                }

                pasosARealizar[position].setDone(Boolean.TRUE);
                if (pasosARealizar[position].getDone()) { //ESTADO realizado
                    view.setBackgroundColor(Color.parseColor("#ff99cc00"));
                } else { //ESTADO no hecho aun
                    view.setBackgroundColor(Color.parseColor("#ffffbb33"));
                }
                //view.refreshDrawableState();
                //parent.refreshDrawableState();
                if (terminoTodosLosPasos()){
                    //si la ot no esta cerrada la cierrto
                    if(!ot.estaCerrado()){
                        api.setEstadoOt(ot.getOt_id(), 3, new Response.Listener() {
                            @Override
                            public void onResponse(Object response) {
                                Toast.makeText(getApplicationContext(), "Orden de trabajo cerrada", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });
                    }else{
                        finish();
                    }


                }
            }
        });
        ////////////////////////
        //si esta abierto lo paso a esta enCUrso();
        if(ot.estaAbierto()){
            api.setEstadoOt(ot_id, 2, new Response.Listener() {
                @Override
                public void onResponse(Object response) {
                    Toast.makeText(getApplicationContext(), "Orden de trabajo En Curso", Toast.LENGTH_SHORT).show();
                    //TODO. nose si haria falta
                    ot.setEstado(2);
                    ot.setEstado_desc("En curso");
                }
            });
        }
    }
    private boolean terminoTodosLosPasos(){
        for (PasoItem paso : pasosARealizar) {
            if(!paso.getDone())
                return false;
        }
        return true;
    }

    private PasoItem[] crearPasosSegunGestion(String tipoGestion) {

        if (tipoGestion.equalsIgnoreCase("instalacion")) {
            return new PasoItem[]{
                    new PasoItem("1 Definir lugar para el Router WIFI", "", "", Boolean.FALSE, Boolean.FALSE, R.drawable.location_128),
                    new PasoItem("2 Realizar conexión de bajada hasta el router", "", "", Boolean.FALSE, Boolean.FALSE, R.drawable.flecha_128),
                    new PasoItem("3 Cablear con UTP dispositivos cercanos", "(tv, consolas, desktops)", "", Boolean.FALSE, Boolean.FALSE, R.drawable.utp_128),
                    new PasoItem("4 Realizar configuraciones de acceso", "(DSL o DOCSIS)", "", Boolean.FALSE, Boolean.FALSE, R.drawable.access_128),
                    new PasoItem("5 Configuración de parámetros wifi", "", "", Boolean.FALSE, Boolean.FALSE, R.drawable.data_configuration_128),
                    new PasoItem("6 Realizar pruebas de velocidad, potencia y navegación", "", "", Boolean.FALSE, Boolean.TRUE, R.drawable.wrench_128),
                    new PasoItem("7 Cerificar navegación por parte del cliente", "", "", Boolean.FALSE, Boolean.FALSE, R.drawable.aceptar_128)
            };
        }else{
            return new PasoItem[]{
                    new PasoItem("1 Consultar falla o síntomas \n al cliente", "", "", Boolean.FALSE, Boolean.FALSE, R.drawable.cliente_hablar_128),
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


}
