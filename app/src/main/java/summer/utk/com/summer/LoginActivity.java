package summer.utk.com.summer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.design.widget.TextInputEditText;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.varun.baasbox.utility;

public class LoginActivity extends AppCompatActivity {

    private Button login_but;
    private TextInputEditText password, username;
    private ProgressDialog pdia;
    private SharedPreferences prefs;
    private String saved_username,saved_password,entered_username,entered_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        /*// Prepare the View for the animation
        View view = findViewById(R.id.login_form_container);

// Start the animation
        int ANIM_DURATION = 2500;
        view.setTranslationY(-50f);
        view.animate()
                .setDuration(ANIM_DURATION + 100)
                .translationY(0)
                .alpha(1.0f);*/




        password = (TextInputEditText)findViewById(R.id.password);
        username = (TextInputEditText)findViewById(R.id.username);


        login_but = (Button)findViewById(R.id.login_but);
        login_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AttemptLogin at = new AttemptLogin();
                entered_username= username.getText().toString();
                entered_password = password.getText().toString();
                at.execute();
            }
        });

        prefs = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
        saved_username = prefs.getString("username",null);
        saved_password = prefs.getString("password",null);

        if(saved_username !=null && saved_password != null){
            username.setText(saved_username);
            password.setText(saved_password);
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        if(pdia!= null)
            pdia.dismiss();
    }

    class AttemptLogin extends AsyncTask<String,Void,Boolean> {

        //three methods get called, first preExecute, then do in background, and once do
        //in back ground is completed, the onPost execute method will be called.//TODO:incorporate on progresssdialog cancelled method in all asynctask

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdia = new ProgressDialog(LoginActivity.this);
            pdia.setMessage("Logging In ...");
            pdia.show();


        }

        @Override
        protected Boolean doInBackground(String... str) {

            utility u = new utility();
            try {
                return u.login(entered_username,entered_password);
            } catch (Exception e) {
                Log.e("LoginActivity", "Trying to login", e);
                return false;
            }

        }

        protected void onPostExecute(Boolean t) {

            pdia.dismiss();
            if(t){
                prefs.edit().putString("username",entered_username).apply();
                prefs.edit().putString("password",entered_password).apply();
                Intent i = new Intent(LoginActivity.this,AddProdActivity.class);
                startActivity(i);
                finish();
            }
            else{
                Toast.makeText(LoginActivity.this,"Login failed",Toast.LENGTH_SHORT).show();
            }
        }


    }
}
