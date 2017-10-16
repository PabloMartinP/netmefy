package ar.com.netmefy.netmefy.adapters;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
    private Handler _onCompleteFinish;
    private boolean _primeraVez;
    public MySimpleWebPageArrayAdapter(Context context, WebPageToBlockItem[] values, Handler onComplete) {
        super(context, R.layout.user_layout, values);
        this.context = context;
        this.values = values;
        _onCompleteFinish = onComplete;
        _primeraVez = true;
    }

    public void toOnlyRead(){
        //setBackgroundColor(Color.parseColor("#ff33b5e5"));
        //webPageListView.getChildAt(i).findViewById(R.id.checkBox).setEnabled(true);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
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

        boolean _readOnly = values[position].getReadOnly();

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                values[position].setBlocked(isChecked);
            }
        });

        if (position == values.length - 1 && _primeraVez) {
            _primeraVez = false;
            _onCompleteFinish.sendEmptyMessage(0);
        }

        if(_readOnly){
            rowView.setBackgroundColor(Color.LTGRAY);
            rowView.findViewById(R.id.checkBox).setEnabled(false);

        }else{
            rowView.setBackgroundColor(Color.parseColor("#ff33b5e5"));
            rowView.findViewById(R.id.checkBox).setEnabled(true);
        }


        return rowView;
    }
}
