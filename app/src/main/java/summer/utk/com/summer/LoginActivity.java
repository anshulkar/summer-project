package summer.utk.com.summer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ButtonBarLayout;
import android.view.View;
import android.widget.Button;

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
                .alpha(1.0f);
        owlogo.animate()
                .setDuration(ANIM_DURATION + 100)
                .translationY(0);
        view.animate()
                .setDuration(ANIM_DURATION)
                .alpha(1.0f);

        login_but = (Button)findViewById(R.id.login_but);
        login_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this,MainActivity.class);
                startActivity(i);
            }
        });

    }
}
