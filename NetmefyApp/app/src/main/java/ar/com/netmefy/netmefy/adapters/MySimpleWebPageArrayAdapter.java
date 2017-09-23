package ar.com.netmefy.netmefy.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import ar.com.netmefy.netmefy.R;
import ar.com.netmefy.netmefy.adapters.elements.DeviceItem;
import ar.com.netmefy.netmefy.adapters.elements.WebPageToBlockItem;

/**
 * Created by ignac on 21/9/2017.
 */

public class MySimpleWebPageArrayAdapter extends ArrayAdapter<WebPageToBlockItem> {
    private final Context context;
    private final WebPageToBlockItem[] values;

    public MySimpleWebPageArrayAdapter(Context context, WebPageToBlockItem[] values) {
        super(context, R.layout.user_layout, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.webpage_layout, parent, false);
        TextView textView1 = (TextView) rowView.findViewById(R.id.firstLine);
        TextView textView2 = (TextView) rowView.findViewById(R.id.secondLine);
        CheckBox checkBox = (CheckBox) rowView.findViewById(R.id.checkBox);
        de.hdodenhof.circleimageview.CircleImageView imageView = (de.hdodenhof.circleimageview.CircleImageView) rowView.findViewById(R.id.circleImageView2);
        textView1.setText(values[position].getName());
        textView2.setText(values[position].getUrl());
        imageView.setImageResource(values[position].getResId());
        Boolean blocked = values[position].getBlocked();
        checkBox.setChecked(blocked);

        return rowView;
    }
}
