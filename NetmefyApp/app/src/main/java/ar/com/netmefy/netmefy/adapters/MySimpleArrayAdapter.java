package ar.com.netmefy.netmefy.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import ar.com.netmefy.netmefy.R;
import ar.com.netmefy.netmefy.adapters.elements.DeviceItem;

/**
 * Created by ignac on 21/9/2017.
 */

public class MySimpleArrayAdapter extends ArrayAdapter<DeviceItem> {
    private final Context context;
    private final DeviceItem[] values;

    public MySimpleArrayAdapter(Context context, DeviceItem[] values) {
        super(context, R.layout.user_layout, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.user_layout, parent, false);
        TextView textView1 = (TextView) rowView.findViewById(R.id.firstLine);
        TextView textView2 = (TextView) rowView.findViewById(R.id.secondLine);
        de.hdodenhof.circleimageview.CircleImageView imageView = (de.hdodenhof.circleimageview.CircleImageView) rowView.findViewById(R.id.tvDeviceConnected2);
        textView1.setText(values[position].getName());
        textView2.setText(values[position].getMac());
        imageView.setImageResource(values[position].getResId());
        // Change the icon for Windows and iPhone
        Boolean blocked = values[position].getBlocked();
        if (blocked) {
            rowView.setBackgroundColor(Color.parseColor("#ffff4444"));
        } else {
            rowView.setBackgroundColor(Color.parseColor("#ff99cc00"));
        }

        return rowView;
    }
}
