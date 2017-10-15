package ar.com.netmefy.netmefy;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import ar.com.netmefy.netmefy.services.NMF;
import ar.com.netmefy.netmefy.services.api.entity.reclamoListItemModel;
import ar.com.netmefy.netmefy.services.api.entity.solicitudListItemModel;

public class VerGestionActivity extends AppCompatActivity {
    private int gestionId;
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
        gestionId =  inBundle.getInt("idGestion");
        tipoGestion =  inBundle.getInt("tipoGestion");
        toolbarGestion = (Toolbar) findViewById(R.id.toolbarGestion);
        imageViewGestion = (ImageView) findViewById(R.id.imageViewGestion);

        String titulo="", descripcion="", estado="";
        if(tipoGestion == 1){//solicitud
            for (solicitudListItemModel solicitud: NMF.solicitudes ) {
                if(solicitud.os_id == gestionId){
                    titulo = solicitud.tipo;
                    descripcion = solicitud.descripcion;
                    estado = solicitud.estado_desc;
                }
            }
        }else{
            //reclamo
            for (reclamoListItemModel reclamo   : NMF.reclamos ) {
                if(reclamo.ot_id == gestionId){
                    titulo = reclamo.tipo   ;
                    descripcion = reclamo.descripcion;
                    estado = reclamo.estado_desc;
                }
            }
        }

        tituloGestion = (TextView) findViewById(R.id.tituloGestion);
        descripcionGestion = (TextView) findViewById(R.id.descripcionGestion);
        estadoGestion = (TextView) findViewById(R.id.estadoGestion);


        tituloGestion.setText(titulo);//TODO poner aca el Titulo de la gestion con el valor de tipo de solicitud o gestion que se selecciono en el combo al crearla

        descripcionGestion.setText(descripcion); //TODO poner lo que se puso de descripcion de gestio en el item de la lista que se seleccion y luego de un espacio poner la fecha del item.

        estadoGestion.setText(estado); //TODO:poner el estado_sk de la gestion

        //TODO: Poner if de a cuerdo al estado_sk de la gestion y cambiar el color del texto a los que estan abajo segun corresponda
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
