package ar.com.netmefy.netmefy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.HashMap;

public class ReclamosActivity extends AppCompatActivity {
    Spinner spn_tipo;
    EditText et_desc;
    Button btn_aceptar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reclamos);
        spn_tipo = (Spinner) findViewById(R.id.spn_reclamo_tipo);
        et_desc = (EditText) findViewById(R.id.et_reclamo_desc);
        btn_aceptar = (Button) findViewById(R.id.btn_reclamo_aceptar);


        populate_tipo();
    }
    private void populate_tipo(){
        ////////////////////////////////////////////////
        //preparar
        String[] spinnerArray = new String[3];
        HashMap<Integer,String> spinnerMap = new HashMap<Integer, String>();

        spinnerMap.put(0,"0");
        spinnerArray[0] = "desc 0" ;

        spinnerMap.put(1,"1");
        spinnerArray[1] = "desc 1";

        spinnerMap.put(2,"2");
        spinnerArray[2] = "desc 2" ;
        ////////////////////////////////////////////////////////////////////////////
        //set value to spinner
        ArrayAdapter<String> adapter =new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_item, spinnerArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_tipo.setAdapter(adapter);

        /////////////////////////////////////////////////////////////
    }

    public void aceptar(View view){
        //get value from spinner
        //String name = spinner.getSelectedItem().toString();
        //String id = spinnerMap.get(spinner.getSelectedItemPosition());

    }
}
