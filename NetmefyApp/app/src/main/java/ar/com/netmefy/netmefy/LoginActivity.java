package ar.com.netmefy.netmefy;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import ar.com.netmefy.netmefy.cliente.ClienteActivity;
import ar.com.netmefy.netmefy.tecnico.TecnicoActivity;


public class LoginActivity extends AppCompatActivity {
    private UserLoginTask _authTask = null;

    private EditText _idView;
    private EditText _passwordView;
    private ProgressDialog _progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        _idView = (EditText) findViewById(R.id.id);
        _passwordView = (EditText) findViewById(R.id.password);

        //evento enter
        _passwordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    login();
                    handled = true;
                }
                return handled;
            }
        });


        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

        inicializarProgressBar(this);


    }

    private void login() {
        // Store values at the time of the login attempt.
        String email = _idView.getText().toString();
        String password = _passwordView.getText().toString();

        boolean cancel = false;
        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            _authTask = new UserLoginTask(email, password, this.getApplicationContext());
            _authTask.execute((Void) null);

        }
    }

    private void showProgress(boolean b){
        if(b) {
            _progressBar.show();
            //Toast.makeText(this.getApplicationContext(), "entrando toast", Toast.LENGTH_LONG).show();
        }
        else {
            _progressBar.dismiss();
        }
    }

    private void inicializarProgressBar(LoginActivity loginActivity) {
        _progressBar = new ProgressDialog(this);
        _progressBar.setMessage(getString(R.string.login_ingresando));
        _progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        _progressBar.setCancelable(false);
    }

    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private Boolean _esCliente = true;
        private final String _id;
        private final String _password;
        private  final Context _context;
        @Override
        protected void onPreExecute(){
            showProgress(true);
        }


        UserLoginTask(String email, String password, Context context ) {
            _id = email;
            _password = password;
            _context = context;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                return false;
            }

            _esCliente = _id.length() == 0;

            // TODO: register the new account here.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            _authTask = null;

            if (success) {
                Intent intent;
                if(_esCliente){
                    intent = new Intent( LoginActivity.this, ClienteActivity.class);
                    Toast.makeText(_context, "Alto cliente", Toast.LENGTH_SHORT).show();
                }else{
                    intent = new Intent( LoginActivity.this, TecnicoActivity.class);
                    Toast.makeText(_context, "Tecnico de la cablera", Toast.LENGTH_SHORT).show();
                }

                showProgress(false);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(_context, R.string.error_login, Toast.LENGTH_SHORT).show();
                //_passwordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            _authTask = null;
            showProgress(false);
        }
    }
}
