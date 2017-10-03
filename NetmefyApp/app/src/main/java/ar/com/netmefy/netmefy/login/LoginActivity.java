package ar.com.netmefy.netmefy.login;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Response;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import ar.com.netmefy.netmefy.MainActivity;
import ar.com.netmefy.netmefy.R;
import ar.com.netmefy.netmefy.services.NMF_Info;
import ar.com.netmefy.netmefy.services.api.Api;
import ar.com.netmefy.netmefy.services.api.entity.usuarioAddModel;
import ar.com.netmefy.netmefy.services.api.entity.usuarioInfo;
import ar.com.netmefy.netmefy.services.login.Session;


public class LoginActivity extends AppCompatActivity {
    private CallbackManager callbackManager;
    private LoginButton loginButton;
    private String TAG = "LoginActivity";
    private Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        session = new Session(getApplicationContext());


        FacebookSdk.sdkInitialize(this);
        setContentView(R.layout.activity_login);
        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setHeight(100);
        loginButton.setTextColor(Color.WHITE);
        loginButton.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        loginButton.setCompoundDrawablePadding(0);
        final Api api = Api.getInstance(getApplicationContext());
        api.log(220, "Abre login FB");
        FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        api.log(200, "entro a onSucces()");
                        api.log(200, object.toString());
                        api.log(200, response.toString());

                        String fb_nombre = "";
                        String fb_apellido = "";
                        String fb_sexo = "";
                        String fb_nacimiento = "";
                        String fb_email = "";
                        String fb_id ;
                        try {
                            fb_id = object.getString("id");
                            fb_nombre = object.getString("first_name");
                            fb_apellido = object.getString("last_name");
                            fb_email = object.getString("email");
                            fb_nacimiento = object.getString("birthday");
                            fb_sexo = object.getString("gender");


                        } catch (JSONException e) {
                            e.printStackTrace();
                            api.log(100, e.toString());
                        }

                        final usuarioAddModel user = new usuarioAddModel();
                        user.cliente_sk = NMF_Info.tipoUsuarioApp.id;
                        user.usuario_email = fb_email;
                        user.usuario_nombre = fb_nombre;
                        user.usuario_sexo = fb_sexo;


                        api.addUser(user, new Response.Listener() {
                            @Override
                            public void onResponse(Object response) {
                                boolean b = (boolean)response;
                                if(b){
                                    api.findUser(user.usuario_email, new Response.Listener() {
                                        @Override
                                        public void onResponse(Object response) {
                                            usuarioInfo userInfo = (usuarioInfo) response;
                                            userInfo.usuario_email = user.usuario_email;
                                            NMF_Info.usuarioInfo = userInfo;
                                            session.setUsuarioInfo();
                                            callMainActivity();
                                        }
                                    });
                                }

                            }
                        });
                    }
                });

                try{
                    Bundle parameters = new Bundle();
                    parameters.putString("fields", "id, first_name, last_name, email, birthday, gender , location");
                    request.setParameters(parameters);
                    request.executeAsync();
                    //request.executeAndWait();
                }catch (Exception ex){
                    ex.printStackTrace();
                }


            }

            @Override
            public void onCancel() {
                api.log(210, "Cancelo el login");
            }

            @Override
            public void onError(FacebookException e) {
                //e.printStackTrace();
                api.log(200, e.getMessage());
                api.log(200, e.toString());
            }
        };
        loginButton.setReadPermissions("email", "user_birthday","user_posts", "user_likes");
        loginButton.registerCallback(callbackManager, callback);
    }

    private void callMainActivity( ) {
        Intent main = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(main);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int responseCode, Intent intent) {
        super.onActivityResult(requestCode, responseCode, intent);
        callbackManager.onActivityResult(requestCode, responseCode, intent);
    }
}
