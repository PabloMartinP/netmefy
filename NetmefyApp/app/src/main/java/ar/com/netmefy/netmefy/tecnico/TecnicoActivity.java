package ar.com.netmefy.netmefy.tecnico;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import ar.com.netmefy.netmefy.MainActivity;
import ar.com.netmefy.netmefy.R;
import ar.com.netmefy.netmefy.login.UserIdActivity;
import ar.com.netmefy.netmefy.services.login.Session;

public class TecnicoActivity extends AppCompatActivity {

    private Session session;
    private Button techLogOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tecnico);
        session = new Session(getApplicationContext());
        techLogOut = (Button) findViewById(R.id.tech_log_out);
    }

    public void logOut(View view){
        session.setUserId("");
        session.setUserType("");
        Intent login = new Intent(TecnicoActivity.this, UserIdActivity.class);
        startActivity(login);
    }
}
