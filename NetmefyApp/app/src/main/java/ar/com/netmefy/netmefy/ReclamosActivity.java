package ar.com.netmefy.netmefy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Response;

import java.util.HashMap;
import java.util.List;

import ar.com.netmefy.netmefy.services.NMF;
import ar.com.netmefy.netmefy.services.api.Api;
import ar.com.netmefy.netmefy.services.api.entity.gestionAddModel;
import ar.com.netmefy.netmefy.services.api.entity.tipoOtModel;

public class ReclamosActivity extends AppCompatActivity {
    Spinner spn_tipo;
    EditText et_desc;
    Button btn_aceptar;
    Api api;
    HashMap<Integer,String> spinnerMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reclamos);
        spn_tipo = (Spinner) findViewById(R.id.spn_reclamo_tipo);
        et_desc = (EditText) findViewById(R.id.et_reclamo_desc);
        btn_aceptar = (Button) findViewById(R.id.btn_reclamo_aceptar);

        api = Api.getInstance(getApplicationContext()  );

        populate_tipo();
    }
    private void populate_tipo(){
        ////////////////////////////////////////////////
        //preparar
        /*String[] spinnerArray = new String[3];
        HashMap<Integer,String> spinnerMap = new HashMap<Integer, String>();

        spinnerMap.put(0,"0");
        spinnerArray[0] = "desc 0" ;

        spinnerMap.put(1,"1");
        spinnerArray[1] = "desc 1";

        spinnerMap.put(2,"2");
        spinnerArray[2] = "desc 2" ;*/

        spinnerMap = new HashMap<Integer, String>();
        api.getTiposDeReclamos(new Response.Listener<List<tipoOtModel>>() {
            @Override
            public void onResponse(List<tipoOtModel> lista) {
                String[] spinnerArray;
                spinnerArray = new String[lista.size()];
                tipoOtModel tipo = null;
                for (int i = 0; i < lista.size(); i++) {
                    tipo = lista.get(i);
                    spinnerMap.put(i,String.valueOf(tipo.tipo_ot_sk));
                    spinnerArray[i] = tipo.tipo_ot_desc;
                }

                //////////////////////////////////
                ArrayAdapter<String> adapter =new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_item, spinnerArray);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spn_tipo.setAdapter(adapter);

            }
        });

        ////////////////////////////////////////////////////////////////////////////
        //set value to spinner

        /////////////////////////////////////////////////////////////
    }

    public void aceptar(View view){
        //get value from spinner
        //String name = spinner.getSelectedItem().toString();
        //String id = spinnerMap.get(spinner.getSelectedItemPosition());
//get value from spinner
        //String name = spinner.getSelectedItem().toString();
        //String id = spinnerMap.get(spinner.getSelectedItemPosition());
        gestionAddModel g = new gestionAddModel();
        g.cliente_sk = NMF.cliente.id;
        g.descripcion = et_desc.getText().toString();
        g.tipo_id = Integer.parseInt(spinnerMap.get(spn_tipo.getSelectedItemPosition()));

        api.addReclamo(g, new Response.Listener() {
            @Override
            public void onResponse(Object response) {

                if(response.toString().equals("ok")){
                    Toast.makeText(getApplicationContext(), "Reclamo enviado correctamente. ", Toast.LENGTH_SHORT).show();
                    finish();
                }else
                    Toast.makeText(getApplicationContext(), "Hubo un error al iniciar el reclamo. ", Toast.LENGTH_SHORT).show();


            }
        });
    }
}
