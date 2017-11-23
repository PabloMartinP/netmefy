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
import ar.com.netmefy.netmefy.adapters.elements.NotificationItem;

/**
 * Created by ignac on 21/9/2017.
 */

public class MySimpleNotificationArrayAdapter extends ArrayAdapter<NotificationItem> {
    private final Context context;
    private final NotificationItem[] values;

    public MySimpleNotificationArrayAdapter(Context context, NotificationItem[] values) {
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
        textView1.setText(values[position].getTitle());
        textView2.setText(values[position].getFecha().toString());
        imageView.setImageResource(values[position].getResId());
        Boolean read = values[position].getRead();
        if (read) {
            rowView.setBackgroundColor(Color.parseColor("#f1f1f1"));
            textView1.setTextColor(Color.BLACK);
            textView2.setTextColor(Color.BLACK);
        } else {
            rowView.setBackgroundColor(Color.parseColor("#ffd600"));
            textView1.setTextColor(Color.BLACK);
            textView2.setTextColor(Color.BLACK);
        }

        return rowView;
    }
}
