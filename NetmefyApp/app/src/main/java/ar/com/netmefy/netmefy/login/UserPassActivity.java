package ar.com.netmefy.netmefy.login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import ar.com.netmefy.netmefy.R;

public class UserPassActivity extends AppCompatActivity {

    TextView tvRepeatPass, tvPassError;
    Button btSendPassword;
    EditText etPassword, etRepeatPassword;
    ProgressBar pbPasword;
    Boolean isFirstLogin;
    String userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_pass);
        Bundle inBundle = getIntent().getExtras();
        isFirstLogin = (Boolean) inBundle.get("isFirstLogin");
        userId =  inBundle.get("userId").toString();
        tvRepeatPass = (TextView) findViewById(R.id.tv_repeat_pass);
        etRepeatPassword = (EditText) findViewById(R.id.et_repeat_password);
        tvPassError = (TextView) findViewById(R.id.tv_pass_error);
        btSendPassword = (Button) findViewById(R.id.bt_send_password);
        etPassword = (EditText) findViewById(R.id.et_password);
        pbPasword = (ProgressBar) findViewById(R.id.pb_password);
        /*if (isFirstLogin){
            tvRepeatPass.setVisibility(View.VISIBLE);
            etRepeatPassword.setVisibility(View.VISIBLE);
        }*/
    }

    public void sendPass(View view){
        pbPasword.setVisibility(View.VISIBLE);
        tvPassError.setVisibility(View.GONE);
        etPassword.setEnabled(false);
        etRepeatPassword.setEnabled(false);
        btSendPassword.setEnabled(false);
        btSendPassword.setVisibility(View.GONE);
        /*if (isFirstLogin){
            if(etPassword.getText().toString().equalsIgnoreCase(etRepeatPassword.getText().toString())){
                sendNewPasswordIdToISP();
            }else {
                tvPassError.setText(R.string.error_creating_password);
                tvPassError.setVisibility(View.VISIBLE);
                pbPasword.setVisibility(View.GONE);
                etPassword.setEnabled(true);
                etRepeatPassword.setEnabled(true);
                btSendPassword.setEnabled(true);
                btSendPassword.setVisibility(View.VISIBLE);
            }
        }else {*/
            sendPasswordIdToISP();
        //}
    }

    private void sendPasswordIdToISP() {
        String url = "https://www.reddit.com/.json" ;//+ etUserId.getText().toString();
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    //Boolean isFirstLogin = Boolean.valueOf(response.getString("isFirstLogin"));
                    String kind = response.getString("kind");
                    Boolean isFirstLogin = true; //TODO: remove this and confirm the line before this one.
                    Intent login = new Intent(UserPassActivity.this,LoginActivity.class); //TODO: Check if already connect to facebook;
                    login.putExtra("userId",userId);
                    startActivity(login);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                pbPasword.setVisibility(View.GONE);
                tvPassError.setText(R.string.error_password);
                tvPassError.setVisibility(View.VISIBLE);
                etPassword.setEnabled(true);
                etRepeatPassword.setEnabled(true);
                btSendPassword.setEnabled(true);
                btSendPassword.setVisibility(View.VISIBLE);
            }
        });
        queue.add(jsObjRequest);
    }

    private void sendNewPasswordIdToISP() {
        String url = "https://www.reddit.com/.json" ;//+ etUserId.getText().toString();
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    //Boolean isFirstLogin = Boolean.valueOf(response.getString("isFirstLogin"));
                    String kind = response.getString("kind");
                    Boolean isFirstLogin = true; //TODO: remove this and confirm the line before this one.
                    Intent rate = new Intent(UserPassActivity.this,RateSupportActivity.class);
                    rate.putExtra("userId",userId);
                    startActivity(rate);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                pbPasword.setVisibility(View.GONE);
                tvPassError.setText(R.string.error_password);
                tvPassError.setVisibility(View.VISIBLE);
                etPassword.setEnabled(true);
                etRepeatPassword.setEnabled(true);
                btSendPassword.setEnabled(true);
                btSendPassword.setVisibility(View.VISIBLE);
            }
        });
        queue.add(jsObjRequest);
    }


}
