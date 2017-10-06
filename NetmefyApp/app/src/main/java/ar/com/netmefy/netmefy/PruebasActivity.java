package ar.com.netmefy.netmefy;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class PruebasActivity extends AppCompatActivity {
    private int progressStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pruebas);

        //TODO: esto es para manejar la parte de velocidad
        final ProgressBar pb = (ProgressBar) findViewById(R.id.progressBarVelocidad);
        TextView tvVelocidad = (TextView) findViewById(R.id.tvVelocidad) ;
        progressStatus = 45;
        //TODO: la siguiente linea es para setear el lugar donde aparece la bara de 0 a 100
        pb.setProgress(progressStatus);//TODO: El maximo valor de la barra es 100 por lo que si da mas mb de 100 ponerlo en 100
        tvVelocidad.setText(String.valueOf(progressStatus) + "mb");//TODO: usar el valor de la barra mas el string mb

        //TODO: esto es para manejar la parte de intensidad de senal
        ImageView ivSenal = (ImageView) findViewById(R.id.ivSenal);
        TextView tvSenal = (TextView) findViewById(R.id.tvSenal) ;
        ivSenal.setImageResource(R.drawable.wifi_2_128); //TODO: asi seteas las imagenes
        //TODO: las opciones de imagenes son:
        //TODO:     R.drawable.wifi_0_128
        //TODO:     R.drawable.wifi_1_128
        //TODO:     R.drawable.wifi_2_128
        //TODO:     R.drawable.wifi_3_128
        //TODO:     R.drawable.wifi_4_128
        //TODO:  El numero que cambia son las barras llenas, usar la division de Lean para saber cual mostrar
        tvSenal.setText("-5db"); //TODO: para setear el texto con el valor de decibeles.

        TextView tvPingAMostrar = (TextView) findViewById(R.id.tvPingAMostrar) ;
        tvPingAMostrar.setText("145ms"); //TODO: para setear el valor de ping

    }
}
