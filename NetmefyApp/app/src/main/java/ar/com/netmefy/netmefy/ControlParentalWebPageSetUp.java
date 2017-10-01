package ar.com.netmefy.netmefy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import ar.com.netmefy.netmefy.router.Router;
import ar.com.netmefy.netmefy.services.NMF_Info;

public class ControlParentalWebPageSetUp extends AppCompatActivity {

    de.hdodenhof.circleimageview.CircleImageView tvPageImage;
    Router router;
    EditText etPageConfigApodo;
    EditText etPageUrl;
    Button btPageDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_parental_web_page_set_up);
        router = Router.getInstance(getApplicationContext());

        String mac = getIntent().getStringExtra("mac");

        tvPageImage = (de.hdodenhof.circleimageview.CircleImageView) findViewById(R.id.tvPageImage);
        etPageConfigApodo = (EditText) findViewById(R.id.et_page_config_apodo);
        etPageUrl = (EditText) findViewById(R.id.et_page_url);
        btPageDelete = (Button) findViewById(R.id.btPageDelete);
    }

    public void deletePage(View view){

    }

    public void saveAndExit(View view){

    }
}
