package ar.com.netmefy.netmefy;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

public class VerGestionActivity extends AppCompatActivity {
    private Long gestionId;
    private Integer tipoGestion;
    Toolbar toolbarGestion;
    ImageView imageViewGestion;
    TextView tituloGestion;
    TextView descripcionGestion;
    TextView estadoGestion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_gestion);
        Bundle inBundle = getIntent().getExtras();
        gestionId =  inBundle.getLong("idGestion");
        tipoGestion =  inBundle.getInt("tipoGestion");
        toolbarGestion = (Toolbar) findViewById(R.id.toolbarGestion);
        imageViewGestion = (ImageView) findViewById(R.id.imageViewGestion);
        tituloGestion = (TextView) findViewById(R.id.tituloGestion);
        descripcionGestion = (TextView) findViewById(R.id.descripcionGestion);
        estadoGestion = (TextView) findViewById(R.id.estadoGestion);


        tituloGestion.setText("(Ejemplo)Upgrade Velocidad");//TODO poner aca el Titulo de la gestion con el valor de tipo de solicitud o gestion que se selecciono en el combo al crearla

        descripcionGestion.setText("Lo que se puso en la descripcion"); //TODO poner lo que se puso de descripcion de gestio en el item de la lista que se seleccion y luego de un espacio poner la fecha del item.

        estadoGestion.setText("Abierta"); //TODO:poner el estado de la gestion

        //TODO: Poner if de a cuerdo al estado de la gestion y cambiar el color del texto a los que estan abajo segun corresponda
        //estadoGestion.setTextColor(Color.GREEN); //TODO: para cerrado
        //estadoGestion.setTextColor(Color.BLUE); //TODO: para abierto
        //estadoGestion.setTextColor(Color.YELLOW); //TODO: para pendiente



        if (tipoGestion == 1){
            toolbarGestion.setBackgroundColor(Color.parseColor("#ff99cc00"));
            imageViewGestion.setImageResource(R.drawable.request_128);
        }else if (tipoGestion == 2){
            toolbarGestion.setBackgroundColor(Color.parseColor("#ffff4444"));
            imageViewGestion.setImageResource(R.drawable.help_128);
        }
    }
}
