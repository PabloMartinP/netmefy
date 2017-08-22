package ar.com.netmefy.netmefy.tecnico;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.firebase.iid.FirebaseInstanceId;

import ar.com.netmefy.netmefy.MainActivity;
import ar.com.netmefy.netmefy.R;
import ar.com.netmefy.netmefy.login.UserIdActivity;
import ar.com.netmefy.netmefy.services.api.Api;
import ar.com.netmefy.netmefy.services.login.Session;

public class TecnicoActivity extends AppCompatActivity {

    private Session session;
    private Button techLogOut;
    private Api api;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tecnico);
        session = new Session(getApplicationContext());
        techLogOut = (Button) findViewById(R.id.tech_log_out);

        api = Api.getInstance(getApplicationContext());
        api.LogIn("netmefy", "yfemten", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                saveToken();
            }
        });

    }

    private void saveToken(){
        try {
            api.saveFirebaseToken(session.getUserId(), session.getUserType(), FirebaseInstanceId.getInstance().getToken(), new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    response = response.toString();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    String j;
                    j = error.toString();

                }
            });
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void logOut(View view){
        session.setUserId("");
        session.setUserType("");
        Intent login = new Intent(TecnicoActivity.this, UserIdActivity.class);
        startActivity(login);
    }
}
