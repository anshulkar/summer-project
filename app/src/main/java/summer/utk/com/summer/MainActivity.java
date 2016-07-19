package summer.utk.com.summer;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.widget.Toast;

import com.varun.baasbox.utility;

import java.util.ArrayList;
import java.util.List;

import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.OnReverseGeocodingListener;
import io.nlopez.smartlocation.SmartLocation;
import io.nlopez.smartlocation.location.providers.LocationGooglePlayServicesProvider;

public class MainActivity extends AppCompatActivity implements ScanFragment.OnBarcodeScanResultListener , OnLocationUpdatedListener {

    private Fragment frag;
    private FragmentTransaction fragTrans;
    private static final int REQUEST_FINE_LOCATION=0;
    private LocationGooglePlayServicesProvider provider;
    private String revGeocodededLocation;
    private static long back_pressed;//keeps track of the time when back button was pressed required for double-tap-in-2sec back button to exit app

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadPermissions(Manifest.permission.ACCESS_FINE_LOCATION,REQUEST_FINE_LOCATION);

        startLocation();

        frag = new ScanFragment();
        fragTrans = getSupportFragmentManager().beginTransaction().replace(R.id.main_activity_fragment,frag);
        fragTrans.commit();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (frag instanceof ScanFragment) frag.onActivityResult(requestCode, resultCode, intent);
    }


    @Override
    public void onStop() {
        super.onStop();
        stopLocation();
    }

    @Override
    public void onBackPressed() {
        if(frag instanceof ScanFragment){
            if (back_pressed + 2000 > System.currentTimeMillis()){
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                startActivity(intent);
                finish();
                super.onBackPressed();
            }
            else
                Toast.makeText(getBaseContext(), "Press again to exit the Application!", Toast.LENGTH_SHORT).show();
            back_pressed = System.currentTimeMillis();
        }
        else{
            frag = new ScanFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.main_activity_fragment,frag).commit();

        }
    }

    @Override
    public void onScannedBarcode(String barcode, Boolean DBmatch) {

        if(DBmatch){
            utility u = new utility();

            Fragment frag = new ProdDetailsFragment();
            Bundle bundle =  new Bundle();
            bundle.putString(ProdDetailsFragment.BARCODE,barcode);
            bundle.putString(ProdDetailsFragment.LOCATION,revGeocodededLocation);
            bundle.putString(ProdDetailsFragment.PROD_NAME,u.getProdName(barcode));
            bundle.putString(ProdDetailsFragment.PROD_DETAILS,u.getProdDetails(barcode));
            frag.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.main_activity_fragment,frag).commit();

        }
        else {
            Fragment frag = new UnrecogProdFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.main_activity_fragment,frag).commit();
        }

    }


    /***Location stuff**/

    private void startLocation() {

        provider = new LocationGooglePlayServicesProvider();
        provider.setCheckLocationSettings(true);

        SmartLocation smartLocation = new SmartLocation.Builder(MainActivity.this).logging(true).build();

        smartLocation.location(provider).start(this);

    }

    private void stopLocation() {
        SmartLocation.with(MainActivity.this).location().stop();
    }

    private void showLocation(Location location) {
        if (location != null) {
            final String text = String.format("Latitude %.6f, Longitude %.6f",
                    location.getLatitude(),
                    location.getLongitude());
            revGeocodededLocation = text;

            // We are going to get the address for the current position
            SmartLocation.with(MainActivity.this).geocoding().reverse(location, new OnReverseGeocodingListener() {
                @Override
                public void onAddressResolved(Location original, List<Address> results) {
                    if (results.size() > 0) {
                        Address result = results.get(0);
                        StringBuilder builder = new StringBuilder(text);
                        builder.append("\n[Reverse Geocoding] ");
                        List<String> addressElements = new ArrayList<>();
                        for (int i = 0; i <= result.getMaxAddressLineIndex(); i++) {
                            addressElements.add(result.getAddressLine(i));
                        }
                        builder.append(TextUtils.join(", ", addressElements));
                        revGeocodededLocation = text;
                    }
                }
            });
        } else {
            Toast.makeText(MainActivity.this,"Can't get location data!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onLocationUpdated(Location location) {
        showLocation(location);
        stopLocation();
    }

    /**Permission stuff**/

    private void loadPermissions(String perm,int requestCode) {
        if (ContextCompat.checkSelfPermission(MainActivity.this, perm) != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, perm)) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{perm},requestCode);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // granted
                }
                else{
                    // no granted
                }
                return;
            }

        }

    }
}
