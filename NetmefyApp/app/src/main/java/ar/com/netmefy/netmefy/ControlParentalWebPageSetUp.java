package ar.com.netmefy.netmefy;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import ar.com.netmefy.netmefy.router.Router;
import ar.com.netmefy.netmefy.services.Utils;
import ar.com.netmefy.netmefy.services.api.Api;

public class ControlParentalWebPageSetUp extends AppCompatActivity {

    de.hdodenhof.circleimageview.CircleImageView tvPageImage;
    Router router;
    EditText etPageConfigApodo;
    EditText etPageUrl;
    Button btPageDelete;
    Api api ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_parental_web_page_set_up);
        router = Router.getInstance(getApplicationContext());

        api = Api.getInstance(getApplicationContext());
        String url = getIntent().getStringExtra("url");

        tvPageImage = (de.hdodenhof.circleimageview.CircleImageView) findViewById(R.id.tvPageImage);
        etPageConfigApodo = (EditText) findViewById(R.id.et_page_config_apodo);
        etPageUrl = (EditText) findViewById(R.id.et_page_url);
        btPageDelete = (Button) findViewById(R.id.btPageDelete);
    }

    public void deletePage(View view){

    }

    public void saveAndExit(View view){

        final String url = etPageUrl.getText().toString();
        final String nombre = etPageConfigApodo.getText().toString();
        final ProgressDialog progressDialog = Utils.getProgressBar(this, "Agregando nueva página ...");

        progressDialog.show();
        router.addUrlToTargetListBlocked(url, new Response.Listener() {
            @Override
            public void onResponse(Object response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.hide();
            }
        }, new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                api.addPageToBlock(url, nombre, new Response.Listener() {
                    @Override
                    public void onResponse(Object response) {
                        progressDialog.hide();
                        Utils.newToast(getApplicationContext(), "Página agregada");
                        finish();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.hide();
                        Utils.newToast(getApplicationContext(), "Error al agregar Página");

                    }
                });
            }
        });


    }
}
