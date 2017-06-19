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

import java.io.IOException;

import ar.com.netmefy.netmefy.MainActivity;
import ar.com.netmefy.netmefy.R;

public class UserIdActivity extends AppCompatActivity {

    private EditText etUserId;
    private TextView tvErrorUserId;
    private Button btSendUserId;
    private ProgressBar pbUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_id);
        etUserId = (EditText) findViewById(R.id.et_user_id);
        tvErrorUserId = (TextView) findViewById(R.id.tv_error_user_id);
        btSendUserId = (Button) findViewById(R.id.bt_send_id);
        pbUserId = (ProgressBar) findViewById(R.id.pb_user_id);
    }


    public void sendUserId (View view){
        tvErrorUserId.setVisibility(View.GONE);
        tvErrorUserId.setText("");
        etUserId.setEnabled(false);
        btSendUserId.setVisibility(View.GONE);
        pbUserId.setVisibility(View.VISIBLE);
        String url = "https://www.reddit.com/.json" ;//+ etUserId.getText().toString();
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    //Boolean isFirstLogin = Boolean.valueOf(response.getString("isFirstLogin"));
                    String kind = response.getString("kind");
                    Boolean isFirstLogin = true; //TODO: remove this and confirm the line before this one.
                    Intent userPass = new Intent(UserIdActivity.this,UserPassActivity.class);
                    userPass.putExtra("isFirstLogin",isFirstLogin);
                    startActivity(userPass);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                tvErrorUserId.setVisibility(View.VISIBLE);
                tvErrorUserId.setText(R.string.error_login);
                etUserId.setEnabled(true);
                btSendUserId.setVisibility(View.VISIBLE);
                pbUserId.setVisibility(View.GONE);
            }
        });
        queue.add(jsObjRequest);


    }


}
