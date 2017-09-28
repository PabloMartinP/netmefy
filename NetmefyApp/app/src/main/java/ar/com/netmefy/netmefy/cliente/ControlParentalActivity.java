package ar.com.netmefy.netmefy.cliente;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;

import ar.com.netmefy.netmefy.R;
import ar.com.netmefy.netmefy.adapters.MySimpleWebPageArrayAdapter;
import ar.com.netmefy.netmefy.adapters.elements.WebPageToBlockItem;

public class ControlParentalActivity extends AppCompatActivity {

    ListView webPageListView;
    Switch switchParentalControl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_parental);
        webPageListView = (ListView) findViewById(R.id.lv_web_pages_list);
        WebPageToBlockItem[] values = new WebPageToBlockItem[] {
                new WebPageToBlockItem("Whatapp","11:22:33:44:55:66",R.drawable.whatsapp,  Boolean.FALSE),
                new WebPageToBlockItem("Facebook","11:22:33:44:55:66",R.drawable.facebook, Boolean.FALSE),
                new WebPageToBlockItem("Instagram","11:22:33:44:55:66",R.drawable.instagram, Boolean.TRUE),
                new WebPageToBlockItem("Netflix","11:22:33:44:55:66",R.drawable.netflix,  Boolean.FALSE),
                new WebPageToBlockItem("Twitter","11:22:33:44:55:66",R.drawable.twitter, Boolean.TRUE),
                new WebPageToBlockItem("Youtube","11:22:33:44:55:66",R.drawable.youtube, Boolean.TRUE),
                new WebPageToBlockItem("Snapchat","11:22:33:44:55:66",R.drawable.snapchat, Boolean.FALSE),
                new WebPageToBlockItem("Spotify","11:22:33:44:55:66",R.drawable.spotify, Boolean.TRUE),
                new WebPageToBlockItem("Gmail","11:22:33:44:55:66",R.drawable.gmail, Boolean.FALSE),
                new WebPageToBlockItem("Telegram","11:22:33:44:55:66",R.drawable.telegram, Boolean.TRUE)};
        MySimpleWebPageArrayAdapter adapter = new MySimpleWebPageArrayAdapter(this, values);
        webPageListView.setAdapter(adapter);

        switchParentalControl = (Switch) findViewById(R.id.switch_parent_control) ;

        switchParentalControl.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    ArrayList<WebPageToBlockItem> webpagesBlocked = new ArrayList<>();
                    for (int i = 0; i < webPageListView.getCount()-1; i++) {
                        webPageListView.getChildAt(i).findViewById(R.id.checkBox).setEnabled(false);
                        WebPageToBlockItem v = (WebPageToBlockItem) webPageListView.getItemAtPosition(i);
                        if (v.getBlocked()) {
                            webpagesBlocked.add(new WebPageToBlockItem(v.getName(), v.getUrl(), v.getResId(), v.getBlocked()));
                            //TODO ACA TEENES QUE GUARDAR LA LISTA DE PAGINAS BLOQUEADAS Y DESPUES BLOQUEARLAS EN EL ROUTER
                        }else{
                            webPageListView.getChildAt(i).setBackgroundColor(Color.LTGRAY);
                        }
                    }
                }else{
                    for (int i = 0; i < webPageListView.getCount()-1; i++) {
                        webPageListView.getChildAt(i).setBackgroundColor(Color.parseColor("#ff33b5e5"));
                        webPageListView.getChildAt(i).findViewById(R.id.checkBox).setEnabled(true);
                    }
                }
            }
        });


    }
}
