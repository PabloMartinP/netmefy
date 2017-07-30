package ar.com.netmefy.netmefy.router.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.List;

import ar.com.netmefy.netmefy.R;
import ar.com.netmefy.netmefy.router.Router;

public class TestBlockByUrlActivity extends AppCompatActivity {
    EditText etUrl, etListBlocked;
    Router router;
    Button btnAdd, btnRemove;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_block_by_url);

        etUrl= (EditText) findViewById(R.id.etUrl);
        etUrl.setText("www.test10.com.ar");

        etListBlocked = (EditText) findViewById(R.id.etListBlockedUrl);

        btnAdd = (Button)findViewById(R.id.btnAddBlockUrl);
        btnRemove = (Button)findViewById(R.id.btnRemoveBlockUrl);

        router = Router.getInstance(getApplicationContext());

        showListBlocked();
/*
        router.getListBlocked(new Response.Listener() {
            @Override
            public void onResponse(Object response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });*/
/*
        ((Nucom)router).removeAllBlocked(new Response.Listener() {
            @Override
            public void onResponse(Object response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });*/

    }

    private void showListBlocked(){
        etListBlocked.setText("Getting list blocked ...");
        router.getUrlListBlocked(new Response.Listener<List<String>>() {
            @Override
            public void onResponse(List<String> urlBlocked) {
                etListBlocked.setText("");
                for (String url : urlBlocked) {
                    etListBlocked.setText(etListBlocked.getText().toString() +  url + "\n");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(final VolleyError error) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //etSsid.setText("SSID OK");
                        //etPassword.setText("PASSWORD OK");
                        etListBlocked.setText("error: "+error.getMessage());
                    }
                });
            }
        });
    }


    public void addBlock(View v){
        Toast.makeText(getApplicationContext(), "addBlock", Toast.LENGTH_LONG).show();
        String url ;

        url = etUrl.getText().toString();

        btnAdd.setText("addBlock ..." + url);

        router.addBlockByUrl(url,
                new Response.Listener() {
                    @Override
                    public void onResponse(final Object response) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //etSsid.setText("SSID OK");
                                //etPassword.setText("PASSWORD OK");
                                btnAdd.setText(response.toString());
                            }
                        });
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(final VolleyError error) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //etSsid.setText("SSID NO OK");
                                //etPassword.setText("PASSWORD NO OK");
                                btnAdd.setText("Error-" + error.getMessage());
                            }
                        });


                    }
                }, new Response.Listener() {
                    @Override
                    public void onResponse(Object response) {
                        //Toast.makeText(getApplicationContext(), "addBlockByMac OKKKK", Toast.LENGTH_LONG).show();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //etSsid.setText("SSID NO OK");
                                //etPassword.setText("PASSWORD NO OK");
                                btnAdd.setText("Termino OK al parecer" );
                            }
                        });

                    }
                });
    }


    public void removeBlock(View v){
        Toast.makeText(getApplicationContext(), "removeBlock", Toast.LENGTH_LONG).show();
        String url = etUrl.getText().toString();

        btnRemove.setText("removeBlock..." + url);


        router.removeBlockByUrl(url,
                new Response.Listener() {
                    @Override
                    public void onResponse(final Object response) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //etSsid.setText("SSID OK");
                                //etPassword.setText("PASSWORD OK");
                                btnRemove.setText(response.toString());
                            }
                        });
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(final VolleyError error) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //etSsid.setText("SSID NO OK");
                                //etPassword.setText("PASSWORD NO OK");
                                btnRemove.setText("Error-" + error.getMessage());
                            }
                        });


                    }
                }, new Response.Listener() {
                    @Override
                    public void onResponse(final Object response) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //etSsid.setText("SSID OK");
                                //etPassword.setText("PASSWORD OK");
                                btnRemove.setText("Fin "+response.toString());
                            }
                        });

                    }
                });

    }


}
