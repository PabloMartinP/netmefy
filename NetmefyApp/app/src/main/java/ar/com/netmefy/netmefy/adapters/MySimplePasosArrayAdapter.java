package ar.com.netmefy.netmefy.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import ar.com.netmefy.netmefy.R;
import ar.com.netmefy.netmefy.adapters.elements.PasoItem;

/**
 * Created by ignac on 21/9/2017.
 */

public class MySimplePasosArrayAdapter extends ArrayAdapter<PasoItem> {
    private final Context context;
    private final PasoItem[] values;

    public MySimplePasosArrayAdapter(Context context, PasoItem[] values) {
        super(context, R.layout.pasos_layout, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.pasos_layout, parent, false);
        TextView textView1 = (TextView) rowView.findViewById(R.id.firstLine);
        TextView textView2 = (TextView) rowView.findViewById(R.id.secondLine);
        TextView textView3 = (TextView) rowView.findViewById(R.id.thirdLine);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.circleImageView2);
        textView1.setText(values[position].getDesc1());
        textView2.setText(values[position].getDesc2());
        textView3.setText(values[position].getDesc3());
        imageView.setImageResource(values[position].getResId());
        Boolean statusPaso = values[position].getDone();

        if (statusPaso) { //ESTADO realizado
            rowView.setBackgroundColor(Color.parseColor("#ff99cc00"));
        } else { //ESTADO no hecho aun
            rowView.setBackgroundColor(Color.parseColor("#ffffbb33"));
        }

        return rowView;
    }
}
