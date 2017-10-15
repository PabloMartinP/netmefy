package ar.com.netmefy.netmefy.cliente;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;

import com.android.volley.Response;

import java.util.ArrayList;
import java.util.List;

import ar.com.netmefy.netmefy.ControlParentalWebPageSetUp;
import ar.com.netmefy.netmefy.R;
import ar.com.netmefy.netmefy.adapters.MySimpleWebPageArrayAdapter;
import ar.com.netmefy.netmefy.adapters.elements.WebPageToBlockItem;
import ar.com.netmefy.netmefy.services.NMF;
import ar.com.netmefy.netmefy.services.api.Api;
import ar.com.netmefy.netmefy.services.api.entity.paginaControlParentalModel;
import ar.com.netmefy.netmefy.services.api.entity.webModel;

public class ControlParentalActivity extends AppCompatActivity {

    ListView webPageListView;
    Switch switchParentalControl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_parental);
        webPageListView = (ListView) findViewById(R.id.lv_web_pages_list);

        final List<paginaControlParentalModel> paginas;// = new ArrayList<paginaControlParentalModel>() ;
        final Activity _this = this;
        final Api api = Api.getInstance(getApplicationContext());
        api.getListBlockedPage(new Response.Listener<List<paginaControlParentalModel>>() {
            @Override
            public void onResponse(List<paginaControlParentalModel> paginas) {

                //setteo las paginasbloqueadas
                for (paginaControlParentalModel p : paginas) {
                    for (webModel webBloqueada: NMF.cliente.router.webs_bloqueadas ) {
                        if(webBloqueada.id == p.id){
                            p.bloqueado = true;
                            break;
                        }
                        else
                            p.bloqueado = false;//esto no hace falta porque siempre viene en false
                    }
                }

                //cargo la lista que necesita la lista del activity
                final WebPageToBlockItem[] values = new WebPageToBlockItem[paginas.size()];
                int i = 0;
                for (paginaControlParentalModel p : paginas) {
                    values[i] = p.toWebPageToBlockItem();
                    i++;
                }
                Handler onCompleteHandler = new Handler(new Handler.Callback() {
                    @Override
                    public boolean handleMessage(Message msg) {
                        //creo el evento del switch de arriba
                        switchParentalControl = (Switch) findViewById(R.id.switch_parent_control) ;
                        switchParentalControl.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                updateList(isChecked);
/*
                        if (isChecked){
                            for (int i = 0; i < webPageListView.getCount()-1; i++) {
                                webPageListView.getChildAt(i).findViewById(R.id.checkBox).setEnabled(false);
                                WebPageToBlockItem v = (WebPageToBlockItem) webPageListView.getItemAtPosition(i);
                                if (v.getBlocked()) {
                                    //webpagesBlocked.add(new WebPageToBlockItem(v.getName(), v.getUrl(), v.getResId(), v.getBlocked()));
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
                        }*/
                                ///////////////////////////////////////////////////////////////////
                                final List<paginaControlParentalModel> paginasModel= new ArrayList<paginaControlParentalModel>();
                                for (int i = 0; i < webPageListView.getCount()-1; i++) {
                                    WebPageToBlockItem v = (WebPageToBlockItem) webPageListView.getItemAtPosition(i);
                                    paginaControlParentalModel p = v.toPaginaControlParentalModel();

                                    if(!isChecked)//si deschequea marco tod0 como false
                                        p.bloqueado = false ;

                                    paginasModel.add(p);
                                }

                                api.BlockPages(NMF.cliente.id, NMF.cliente.router.router_sk, paginasModel, new Response.Listener() {
                                    @Override
                                    public void onResponse(Object response) {

                                    }
                                });
                                ///////////////////////////////////////////////////////////////////
                            }
                        });//fin setOnCheckedCHange();
                        if(NMF.cliente.router.webs_bloqueadas.size()>0){

                            switchParentalControl.setChecked(true);
                            //updateList(true);
                        }

                        return false;
                    }
                });
                MySimpleWebPageArrayAdapter adapter = new MySimpleWebPageArrayAdapter(_this, values, onCompleteHandler);
                webPageListView.setAdapter(adapter);

                ///////////////////////////////////////////////////////////////////
            }
        });

        ///////////////////////////////////////////////////
/*
        WebPageToBlockItem[] values = new WebPageToBlockItem[] {
                new WebPageToBlockItem("Whatapp","web.whatsapp.com",R.drawable.whatsapp,  Boolean.FALSE),
                new WebPageToBlockItem("Facebook","facebook.com",R.drawable.facebook, Boolean.FALSE),
                new WebPageToBlockItem("Instagram","www.instagram.com",R.drawable.instagram, Boolean.TRUE),
                new WebPageToBlockItem("Netflix","www.netflix.com",R.drawable.netflix,  Boolean.FALSE),
                new WebPageToBlockItem("Twitter","twitter.com",R.drawable.twitter, Boolean.TRUE),
                new WebPageToBlockItem("Youtube","www.youtube.com",R.drawable.youtube, Boolean.TRUE),
                new WebPageToBlockItem("Snapchat","www.snapchat.com",R.drawable.snapchat, Boolean.FALSE),
                new WebPageToBlockItem("Spotify","www.spotify.com",R.drawable.spotify, Boolean.TRUE),
                new WebPageToBlockItem("Gmail","www.google.com",R.drawable.gmail, Boolean.FALSE),
                new WebPageToBlockItem("Telegram","web.telegram.org",R.drawable.telegram, Boolean.TRUE)};

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
        */


    }
    public void updateList(boolean isChecked){
        if (isChecked){
            for (int i = 0; i < webPageListView.getCount()-1; i++) {
                webPageListView.getChildAt(i).findViewById(R.id.checkBox).setEnabled(false);
                WebPageToBlockItem v = (WebPageToBlockItem) webPageListView.getItemAtPosition(i);
                if (v.getBlocked()) {
                    //webpagesBlocked.add(new WebPageToBlockItem(v.getName(), v.getUrl(), v.getResId(), v.getBlocked()));
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

    public void newPage(View view){
        Intent page = new Intent(ControlParentalActivity.this, ControlParentalWebPageSetUp.class).putExtra("url", "");
        //startActivity(device);
        //Intent intent = new Intent(this, SyncActivity.class);
        //intent.putExtra("someData", "Here is some data");
        startActivityForResult(page, 1);
    }


}
