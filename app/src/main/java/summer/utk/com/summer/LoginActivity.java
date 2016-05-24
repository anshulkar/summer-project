package summer.utk.com.summer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONObject;

import jp.wasabeef.blurry.Blurry;

public class LoginActivity extends AppCompatActivity {

    private Button login_but;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Prepare the View for the animation
        View view = findViewById(R.id.login_form_container);
        View owlogo = findViewById(R.id.ow_logo);
        View overlay = findViewById(R.id.overlay);
        view.setAlpha(0.0f);
        overlay.setAlpha(0.0f);

// Start the animation
        int ANIM_DURATION = 2500;
        owlogo.setTranslationY(-500f);
        overlay.animate()
                .setDuration(ANIM_DURATION / 2)
                .alpha(0.7f);
        owlogo.animate()
                .setDuration(ANIM_DURATION + 100)
                .translationY(0);
        view.animate()
                .setDuration(ANIM_DURATION)
                .alpha(1.0f);



        login_but = (Button)findViewById(R.id.login_but);//jj
        login_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AttemptLogin at = new AttemptLogin();
                at.execute("put some fuking strings here or watever datatype");
            }
        });

    }


    class AttemptLogin extends AsyncTask<String,Void,Integer> {

        //three methods get called, first preExecute, then do in background, and once do
        //in back ground is completed, the onPost execute method will be called.//TODO:incorporate on progresssdialog cancelled method in all asynctask

        ProgressDialog pdia;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdia = new ProgressDialog(LoginActivity.this);
            pdia.setMessage("Logging In ...");
            pdia.show();


        }

        @Override
        protected Integer doInBackground(String... str) {
            return null;
        }

        protected void onPostExecute(Integer t) {

            pdia.dismiss();
            Intent i = new Intent(LoginActivity.this,MainActivity.class);
            startActivity(i);
        }


    }
}
