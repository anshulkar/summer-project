package summer.utk.com.summer;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;

/**
 * Created by Utkarsh on 16-06-2016 with the help of SWAG.
 */
public class IntroActivity extends AppIntro {

    private SharedPreferences prefs;
    public static String FIRST_RUN="firstRun";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = PreferenceManager.getDefaultSharedPreferences(IntroActivity.this);
        if(!prefs.getBoolean(IntroActivity.FIRST_RUN,true)){
            Intent i = new Intent(IntroActivity.this,MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            finish();
        }

        // Add your slide's fragments here.
        // AppIntro will automatically generate the dots indicator and buttons.
        /*addSlide(first_fragment);
        addSlide(second_fragment);
        addSlide(third_fragment);
        addSlide(fourth_fragment);*/

        // Instead of fragments, you can also use our default slide
        // Just set a title, description, background and image. AppIntro will do the rest.
        addSlide(AppIntroFragment.newInstance("Camera", "We need camera permissions\n to scan barcodes and QRcodes", R.drawable.intro_frag_camera, Color.parseColor("#3F51B5")));
        addSlide(AppIntroFragment.newInstance("Location", "Location Permissions required/nto know where you are scanning from.", R.drawable.intro_frag_location, Color.parseColor("#3F51B5")));
        addSlide(AppIntroFragment.newInstance("Welcome", "", R.drawable.intro_frag_app_tut, Color.parseColor("#3F51B5")));

        askForPermissions(new String[]{Manifest.permission.CAMERA}, 1);
        askForPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 2);

        // OPTIONAL METHODS
        // Override bar/separator color.
        //setBarColor(Color.parseColor("#3F51B5"));
        //setSeparatorColor(Color.parseColor("#2196F3"));

        // Hide Skip/Done button.
        showSkipButton(false);
        setProgressButtonEnabled(true);

        // Turn vibration on and set intensity.
        // NOTE: you will probably need to ask VIBRATE permisssion in Manifest.
        //setVibrate(true);
        //setVibrateIntensity(30);
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        // Do something when users tap on Skip button.
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        prefs.edit().putBoolean(FIRST_RUN,false).apply();
        Intent i = new Intent(IntroActivity.this,MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
        // Do something when users tap on Done button.
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
        // Do something when the slide changes.
    }
}
