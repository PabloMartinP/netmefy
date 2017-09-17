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
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import ar.com.netmefy.netmefy.MainActivity;
import ar.com.netmefy.netmefy.R;
import ar.com.netmefy.netmefy.router.activities.TPLinkTestsActivity;
import ar.com.netmefy.netmefy.services.api.Api;
import ar.com.netmefy.netmefy.services.api.entity.tipoUsuarioApp;
import ar.com.netmefy.netmefy.services.login.Session;
import ar.com.netmefy.netmefy.tecnico.TecnicoActivity;

public class UserIdActivity extends AppCompatActivity {

    private EditText etUserId, etPassword;
    private TextView tvErrorDni;
    private Button btSendUserId;
    private ProgressBar pbUserId;
    private Session session;
    Api api;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_id);
        etUserId = (EditText) findViewById(R.id.et_user_id);
        etPassword = (EditText) findViewById(R.id.et_password);
        tvErrorDni = (TextView) findViewById(R.id.tv_error_dni);
        btSendUserId = (Button) findViewById(R.id.bt_send_id);
        pbUserId = (ProgressBar) findViewById(R.id.pb_user_id);
        session = new Session(getApplicationContext());

        api = Api.getInstance(getApplicationContext());

    }


    public void sendUserId (View view){
        tvErrorDni.setVisibility(View.GONE);
        etUserId.setEnabled(false);
        etPassword.setEnabled(false);
        btSendUserId.setVisibility(View.GONE);
        pbUserId.setVisibility(View.VISIBLE);
        sendUserIdToISP();


    }

    private void sendUserIdToISP() {

        final String username =etUserId.getText().toString();
        String password = etPassword.getText().toString();

        api.LogIn(username, password, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equalsIgnoreCase("ok")){
                    session.setUserId(etUserId.getText().toString());
                    session.setPassword(etPassword.getText().toString());

                    //response = response.toLowerCase();
                    api.getTypeOfUser(username, new Response.Listener<tipoUsuarioApp>() {
                        @Override
                        public void onResponse(tipoUsuarioApp tipoUsuarioApp) {
                            Api.tipoUsuarioApp = tipoUsuarioApp;

                            if(Api.tipoUsuarioApp.esCliente()){
                                session.setUserType("client");
                                redirectToUser("");
                            }else{
                                session.setUserType("tech");
                                redirectToTech();
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            tvErrorDni.setVisibility(View.VISIBLE);
                            etUserId.setEnabled(true);
                            etPassword.setEnabled(true);
                            btSendUserId.setVisibility(View.VISIBLE);
                            pbUserId.setVisibility(View.GONE);
                            tvErrorDni.setText("Error al conectar con el servidor");
                        }
                    });

                }else {
                    tvErrorDni.setVisibility(View.VISIBLE);
                    etUserId.setEnabled(true);
                    etPassword.setEnabled(true);
                    btSendUserId.setVisibility(View.VISIBLE);
                    pbUserId.setVisibility(View.GONE);
                    tvErrorDni.setText("El usuario o clave son incorrectos");
                }
            }
        });
        /*String url = getResources().getString(R.string.baseUrl) + getResources().getString(R.string.urlLogin) + etUserId.getText().toString() + "/" + etPassword.getText().toString();
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    //String kind = response.getString("kind");
                    String typeOfUser = response.getString("typeOfUser");
                    String supportNumber = response.getString("supportNumber");
                    session.setUserType(typeOfUser);
                    if(typeOfUser.equalsIgnoreCase("user")){
                        redirectToUser(supportNumber);
                    }else{
                        redirectToTech();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                tvErrorDni.setVisibility(View.VISIBLE);
                etUserId.setEnabled(true);
                etPassword.setEnabled(true);
                btSendUserId.setVisibility(View.VISIBLE);
                pbUserId.setVisibility(View.GONE);
            }
        });
        queue.add(jsObjRequest);
        */
    }

    private void redirectToTech() {
        Intent userPass = new Intent(UserIdActivity.this,TecnicoActivity.class);
        startActivity(userPass);
    }

    private void redirectToUser(String supportNumber) {
        session.setUserType("user");

        if (!supportNumber.isEmpty()){
            Intent userPass = new Intent(UserIdActivity.this,RateSupportActivity.class);

            userPass.putExtra("userId",etUserId.getText());
            userPass.putExtra("supportNumber",supportNumber);
            startActivity(userPass);
            finish();
        }else{
            Intent userPass = new Intent(UserIdActivity.this, LoginActivity.class);
            session.setUserId(etUserId.getText().toString());
            userPass.putExtra("userId",etUserId.getText());
            startActivity(userPass);
            finish();
        }
    }

}
