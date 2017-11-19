package ar.com.netmefy.netmefy.adapters;

import android.app.Activity;
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
import ar.com.netmefy.netmefy.services.Utils;

/**
 * Created by ignac on 21/9/2017.
 */

public class MySimpleWebPageArrayAdapter extends ArrayAdapter<WebPageToBlockItem> {
    private final Context context;
    private final WebPageToBlockItem[] values;
    private Handler _onCompleteFinish;
    private boolean _primeraVez;
    private  final Activity activity;
    public MySimpleWebPageArrayAdapter(Context context, WebPageToBlockItem[] values, Handler onComplete, Activity activity) {
        super(context, R.layout.user_layout, values);
        this.context = context;
        this.values = values;
        _onCompleteFinish = onComplete;
        _primeraVez = true;
        this.activity = activity;
    }

    public void toOnlyRead(){
        //setBackgroundColor(Color.parseColor("#ff33b5e5"));
        //webPageListView.getChildAt(i).findViewById(R.id.checkBox).setEnabled(true);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.webpage_layout, parent, false);
        TextView textView1 = (TextView) rowView.findViewById(R.id.firstLine);
        TextView textView2 = (TextView) rowView.findViewById(R.id.secondLine);
        CheckBox checkBox = (CheckBox) rowView.findViewById(R.id.checkBox);
        de.hdodenhof.circleimageview.CircleImageView imageView = (de.hdodenhof.circleimageview.CircleImageView) rowView.findViewById(R.id.circleImageView2);
        textView1.setText(values[position].getName());
        textView2.setText(values[position].getUrl());

        int resIdImage = values[position].getResId();
        String nombreImagen = "";

        switch (resIdImage){
            case 2130837716:
                nombreImagen = "whatsapp";
                break;
            case 2130837669:
                nombreImagen = "facebook";
                break;
            case 2130837675:
                nombreImagen = "instagram";
                break;
            case 2130837691:
                nombreImagen = "netflix";
                break;
            case 2130837715:
                nombreImagen = "twitter";
                break;
            case 2130837718:
                nombreImagen = "youtube";
                break;
            case 2130837711:
                nombreImagen = "snapchat";
                break;
            case 2130837712:
                nombreImagen = "spotify";
                break;
            case 2130837670:
                nombreImagen = "gmail";
                break;
            case 2130837714:
                nombreImagen = "telegram";
                break;


        }




        int newResIdImage = Utils.getResIdFromImageName(activity, nombreImagen);
        if(newResIdImage == 0)
            newResIdImage = Utils.getResIdFromImageName(activity, "lock_5_128");

        imageView.setImageResource(newResIdImage);
        Boolean blocked = values[position].getBlocked();
        checkBox.setChecked(blocked);

        boolean _readOnly = values[position].getReadOnly();

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                values[position].setBlocked(isChecked);

                if(isChecked)
                    //rowView.setBackgroundColor(Color.LTGRAY);
                    rowView.setBackgroundColor(Color.parseColor("#ff33b5e5"));
                else
                    //rowView.setBackgroundColor(Color.parseColor("#ff33b5e5"));
                    rowView.setBackgroundColor(Color.LTGRAY);
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

        if(blocked)
            //rowView.setBackgroundColor(Color.LTGRAY);
            rowView.setBackgroundColor(Color.parseColor("#ff33b5e5"));
        else
            //rowView.setBackgroundColor(Color.parseColor("#ff33b5e5"));
            rowView.setBackgroundColor(Color.LTGRAY);




        return rowView;
    }
}
