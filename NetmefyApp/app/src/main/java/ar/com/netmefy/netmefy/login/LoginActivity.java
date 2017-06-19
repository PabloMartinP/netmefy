package ar.com.netmefy.netmefy.login;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import ar.com.netmefy.netmefy.MainActivity;
import ar.com.netmefy.netmefy.R;


public class LoginActivity extends AppCompatActivity {
    private CallbackManager callbackManager;
    private ProfileTracker profileTracker;
    private LoginButton loginButton;
    private String firstName,lastName, email,birthday,gender, next;
    private JSONObject likes, paging;
    private JSONArray likesNamesJson;
    private ArrayList<String>likesNames;
    private URL profilePicture;
    private String userId;
    private String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this);
        setContentView(R.layout.activity_login);

        callbackManager = CallbackManager.Factory.create();

        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setHeight(100);
        loginButton.setTextColor(Color.WHITE);
        loginButton.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        loginButton.setCompoundDrawablePadding(0);


        FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.e(TAG,object.toString());
                        Log.e(TAG,response.toString());

                        try {
                            getInfoFromUser(object);
                            callMainActivity();
                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }
                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id, first_name, last_name, email, birthday, gender , location, likes");
                parameters.putString("limit","400");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException e) {
                e.printStackTrace();
            }
        };

        loginButton.setReadPermissions("email", "user_birthday","user_posts", "user_likes");
        loginButton.registerCallback(callbackManager, callback);
    }

    private void callMainActivity() {
        Intent main = new Intent(LoginActivity.this,MainActivity.class);
        main.putExtra("name",firstName);
        main.putExtra("surname",lastName);
        main.putExtra("imageUrl",profilePicture.toString());
        main.putStringArrayListExtra("likesNames", likesNames);
        startActivity(main);
    }

    private void getInfoFromUser(JSONObject object) throws JSONException, MalformedURLException {
        userId = object.getString("id");
        profilePicture = new URL("https://graph.facebook.com/" + userId + "/picture?width=500&height=500");
        if(object.has("first_name"))
            firstName = object.getString("first_name");
        if(object.has("last_name"))
            lastName = object.getString("last_name");
        if (object.has("email"))
            email = object.getString("email");
        if (object.has("birthday"))
            birthday = object.getString("birthday");
        if (object.has("gender"))
            gender = object.getString("gender");
        if (object.has("likes")) {
            likes = (JSONObject)object.get("likes");
            likesNamesJson = (JSONArray)likes.get("data");
            likesNames = new ArrayList<String>();
            for(int i = 0; i < likesNamesJson.length(); i++){
                likesNames.add(((JSONObject)likesNamesJson.get(i)).getString("name"));
            }
            paging = (JSONObject)likes.get("paging");
            next = paging.getString("next");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int responseCode, Intent intent) {
        super.onActivityResult(requestCode, responseCode, intent);
        callbackManager.onActivityResult(requestCode, responseCode, intent);
    }

}
