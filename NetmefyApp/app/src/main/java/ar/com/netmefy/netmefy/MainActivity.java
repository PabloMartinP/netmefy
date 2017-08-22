package ar.com.netmefy.netmefy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
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

import ar.com.netmefy.netmefy.cliente.ControlParentalActivity;
import ar.com.netmefy.netmefy.login.UserIdActivity;
import ar.com.netmefy.netmefy.services.login.LikesToFacebook;
import ar.com.netmefy.netmefy.services.login.Session;

//public class MainActivity extends Activity {
public class MainActivity extends AppCompatActivity {

    private ImageButton logout;
    private Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        session = new Session(getApplicationContext());
        LikesToFacebook likesToFacebook = new LikesToFacebook();
        likesToFacebook.run();
        logout = (ImageButton) findViewById(R.id.ib_logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                session.setUserId("");
                session.setUserType("");
                session.setEmail("");
                session.setUserName("");
                session.setLoginWay("");
                LoginManager.getInstance().logOut();
                Intent login = new Intent(MainActivity.this, UserIdActivity.class);
                startActivity(login);
                finish();
            }
        });
    }

    public void goToNotifications(View view){
       /* Intent notifications = new Intent(MainActivity.this, NotificationsActivity.class);
        startActivity(notifications);*/
    }

    public void goToSetOfTest(View view){
       /* Intent tests = new Intent(MainActivity.this, TestConnectivityActivity.class);
        startActivity(tests);*/
    }

    public void goToGestiones(View view){
        Intent gestiones = new Intent(MainActivity.this, GestionesActivity.class);
        startActivity(gestiones);
    }

    public void goToParentalControl(View view){
        Intent parentalControl = new Intent(MainActivity.this, ControlParentalActivity.class);
        startActivity(parentalControl);
    }
}
