package ar.com.netmefy.netmefy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.login.LoginManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ar.com.netmefy.netmefy.login.UserIdActivity;
import ar.com.netmefy.netmefy.services.DownloadImage;
import ar.com.netmefy.netmefy.login.LoginActivity;
import ar.com.netmefy.netmefy.services.login.Session;

public class MainActivity extends AppCompatActivity {

    private TextView nameView;
    private Button logout;
    private ListView lvLikes;
    JSONArray jsonLikes;
    JSONObject jsonNext;
    ArrayList<String> likesNames;
    private Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bundle inBundle = getIntent().getExtras();
        FacebookSdk.sdkInitialize(getApplicationContext());
        Profile userFacebook = Profile.getCurrentProfile();
        session = new Session(getApplicationContext());
        nameView = (TextView)findViewById(R.id.nameAndSurname);
        lvLikes = (ListView) findViewById(R.id.lv_likes);
        likesNames = new ArrayList<>();
        callFacebookForLikes(userFacebook);
        String nameToShow = getNameToShow(userFacebook);
        nameView.setText(nameToShow);
        logout = (Button)findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                session.setUserId("");
                LoginManager.getInstance().logOut();
                Intent login = new Intent(MainActivity.this, UserIdActivity.class);
                startActivity(login);
                finish();
            }
        });
    }

    private String getNameToShow(Profile userFacebook) {
        String name = userFacebook.getFirstName();
        if(!userFacebook.getMiddleName().isEmpty()) {
            name = name.concat(" " + userFacebook.getMiddleName());
        }
        name = name.concat(" " + userFacebook.getLastName());
        return name;
    }

    private void callFacebookForLikes(Profile userFacebook) {
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/"+userFacebook.getId()+"/likes?limit=500",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        /* handle the result */
                        try {
                            jsonLikes = response.getJSONObject().getJSONArray("data");
                            addUserLikes(jsonLikes);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
        ).executeAsync();
    }

    private void addUserLikes(JSONArray jsonLikes) throws JSONException {
        for(int i = 0; i < jsonLikes.length(); i++){
            likesNames.add(((JSONObject)jsonLikes.get(i)).getString("name").toString());
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_1, likesNames );

        lvLikes.setAdapter(arrayAdapter);
    }


}
