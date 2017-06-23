package ar.com.netmefy.netmefy.login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RatingBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import ar.com.netmefy.netmefy.R;

public class RateSupportActivity extends AppCompatActivity {

    Button btSendRateSupport;
    RatingBar rbRateSupport;
    ProgressBar pbRate;
    String userId, supportNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_support);
        Bundle inBundle = getIntent().getExtras();
        userId =  inBundle.get("userId").toString();
        supportNumber =  inBundle.get("supportNumber").toString();
        rbRateSupport = (RatingBar) findViewById(R.id.rb_rate_support);
        btSendRateSupport = (Button) findViewById(R.id.bt_send_rate_suport);
        pbRate = (ProgressBar) findViewById(R.id.pb_rate);
        rbRateSupport.setRating(new Float(4));
    }

    public void sendRateOnClick(View view){
        btSendRateSupport.setVisibility(View.GONE);
        rbRateSupport.setEnabled(false);
        pbRate.setVisibility(View.VISIBLE);
        sendRate();
    }

    private void sendRate() {
        String url = getResources().getString(R.string.baseUrl) + getResources().getString(R.string.urlRate) + supportNumber + "/" + rbRateSupport.getRating() ;
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Intent login = new Intent(RateSupportActivity.this,LoginActivity.class);
                login.putExtra("userId",userId);
                startActivity(login);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                btSendRateSupport.setVisibility(View.VISIBLE);
                rbRateSupport.setEnabled(true);
                pbRate.setVisibility(View.GONE);
            }
        });
        queue.add(jsObjRequest);
    }
}
