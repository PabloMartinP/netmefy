package ar.com.netmefy.netmefy.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import ar.com.netmefy.netmefy.R;
import ar.com.netmefy.netmefy.adapters.elements.GestionItem;
import ar.com.netmefy.netmefy.adapters.elements.NotificationItem;

/**
 * Created by ignac on 21/9/2017.
 */

public class MySimpleGestionArrayAdapter extends ArrayAdapter<GestionItem> {
    private final Context context;
    private final GestionItem[] values;

    public MySimpleGestionArrayAdapter(Context context, GestionItem[] values) {
        super(context, R.layout.gestion_layout, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.gestion_layout, parent, false);
        TextView titulo = (TextView) rowView.findViewById(R.id.firstLine);
        TextView subtitulo = (TextView) rowView.findViewById(R.id.secondLine);
        TextView estado = (TextView) rowView.findViewById(R.id.estadoGestionItem);
        titulo.setText(String.valueOf(values[position].getIdDeGestion()) + ": " + values[position].getTipoDeGestion());
        subtitulo.setText(values[position].getFechaDeGestion().toString());
        estado.setText(values[position].getEstadoDeGestion());

        String estadoGestion = values[position].getEstadoDeGestion();
        if (estadoGestion.equalsIgnoreCase("abierto")) {
            estado.setTextColor(Color.BLUE);
        } else if (estadoGestion.equalsIgnoreCase("en curso")){
            estado.setTextColor(Color.YELLOW);
        }else if (estadoGestion.equalsIgnoreCase("cerrado")){
            estado.setTextColor(Color.GREEN);
        }

        return rowView;
    }
}
