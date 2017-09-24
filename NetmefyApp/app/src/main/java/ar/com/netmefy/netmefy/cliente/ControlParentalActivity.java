package ar.com.netmefy.netmefy.cliente;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import ar.com.netmefy.netmefy.R;
import ar.com.netmefy.netmefy.adapters.MySimpleWebPageArrayAdapter;
import ar.com.netmefy.netmefy.adapters.elements.WebPageToBlockItem;

public class ControlParentalActivity extends AppCompatActivity {

    ListView webPageListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_parental);
        webPageListView = (ListView) findViewById(R.id.lv_web_pages_list);
        WebPageToBlockItem[] values = new WebPageToBlockItem[] { new WebPageToBlockItem("Whatapp","11:22:33:44:55:66",R.drawable.whatsapp,  Boolean.FALSE),
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

    }
}
