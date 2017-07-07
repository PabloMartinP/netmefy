package ar.com.netmefy.netmefy;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ar.com.netmefy.netmefy.login.UserIdActivity;
import ar.com.netmefy.netmefy.services.login.Session;

public class MainGoogleActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private TextView nameView, likesRecived, likesNotRecived;
    private Button logout;
    private ListView lvLikes;
    private ProgressBar progressBar2;
    ArrayList<String> likesNames;
    private Session session;
    private GoogleApiClient mGoogleApiClient;
    private static final String TAGGOOGLE = "SignInActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


        session = new Session(getApplicationContext());
        nameView = (TextView)findViewById(R.id.nameAndSurname);
        likesRecived = (TextView)findViewById(R.id.tv_likes_recived);
        likesNotRecived = (TextView)findViewById(R.id.tv_likes_not_recived);
        progressBar2 = (ProgressBar)findViewById(R.id.progressBar2);
        lvLikes = (ListView) findViewById(R.id.lv_likes);
        likesNames = new ArrayList<>();
        String nameToShow =session.getUserName();
        try {
            sendLikes(new ArrayList<String>());
        } catch (JSONException e) {
                e.printStackTrace();
        }

        nameView.setText(nameToShow);
        logout = (Button)findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                session.setUserId("");
                session.setUserType("");
                session.setEmail("");
                session.setUserName("");
                session.setLoginWay("");
                Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                        new ResultCallback<Status>() {
                            @Override
                            public void onResult(Status status) {
                                Log.d("google", "onResult: " + status);
                                Intent login = new Intent(MainGoogleActivity.this, UserIdActivity.class);
                                startActivity(login);
                                finish();
                            }
                        });

            }
        });
    }

    private void sendLikes(ArrayList<String> likesNames) throws JSONException {
        String url = getResources().getString(R.string.baseUrl)+getResources().getString(R.string.urlLikesFromId);
        RequestQueue queue = Volley.newRequestQueue(this);
        JSONObject requestToSend = new JSONObject();
        requestToSend.put("userId",session.getUserId());
        requestToSend.put("name",session.getUserName());
        requestToSend.put("email",session.getEmail());
        requestToSend.put("loginWay",session.getLoginWay());
        requestToSend.put("likesFromUser",likesNames);
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, url, requestToSend, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    progressBar2.setVisibility(View.GONE);
                    //String kind = response.getString("kind");
                    String status = response.getString("status");
                    if (status.equalsIgnoreCase("ok")){
                        likesNotRecived.setVisibility(View.GONE);
                        likesRecived.setVisibility(View.VISIBLE);
                    }else{
                        likesRecived.setVisibility(View.GONE);
                        likesNotRecived.setVisibility(View.VISIBLE);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar2.setVisibility(View.GONE);
                likesRecived.setVisibility(View.GONE);
                likesNotRecived.setVisibility(View.VISIBLE);
            }
        });
        queue.add(jsObjRequest);
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAGGOOGLE, "onConnectionFailed:" + connectionResult);
    }
}
