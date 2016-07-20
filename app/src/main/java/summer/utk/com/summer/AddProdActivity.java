package summer.utk.com.summer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.varun.baasbox.utility;

import java.util.ArrayList;
import java.util.List;

import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.OnReverseGeocodingListener;
import io.nlopez.smartlocation.SmartLocation;
import io.nlopez.smartlocation.location.providers.LocationGooglePlayServicesProvider;

public class AddProdActivity extends AppCompatActivity implements AddProdContentFragment.OnAddProdDataListener ,OnLocationUpdatedListener {


    private Fragment frag;
    private Toolbar toolbar;
    private FragmentTransaction fragTrans;
    private LocationGooglePlayServicesProvider provider;
    private String revGeocodededLocation,longitude,latitude;
    private String barcode,prod_name,prod_details=null;
    private ProgressDialog pdia;
    private String TITLE_ADD_PROD="Add product",TITLE_PROD_DETAILS="Product details";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_prod);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(TITLE_ADD_PROD);
        setSupportActionBar(toolbar);

        startLocation();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*AddProdContentFragment prodContentFragment = (AddProdContentFragment)
                        getSupportFragmentManager().findFragmentById(R.id.add_prod_activity_fragment);*/

                if (frag instanceof AddProdContentFragment) {
                    if(!((AddProdContentFragment)frag).submitButtonPressed()){
                        Snackbar.make(view, "Please provide required data", Snackbar.LENGTH_SHORT)
                                .setAction("Action", null).show();
                    }
                    else{

                        frag = new ProdDetailsFragment();
                        toolbar.setTitle(TITLE_PROD_DETAILS);
                        Bundle args = new Bundle();
                        args.putString(ProdDetailsFragment.BARCODE, barcode );
                        args.putString(ProdDetailsFragment.PROD_NAME, prod_name );
                        args.putString(ProdDetailsFragment.PROD_DETAILS, prod_details );
                        args.putString(ProdDetailsFragment.LOCATION, revGeocodededLocation );
                        args.putString(ProdDetailsFragment.LATITUDE, latitude );
                        args.putString(ProdDetailsFragment.LONGITUDE, longitude );
                        frag.setArguments(args);
                        fragTrans = getSupportFragmentManager().beginTransaction().replace(R.id.add_prod_activity_fragment,frag);
                        fragTrans.commit();
                    }
                } else {

                    AddProduct add = new AddProduct();
                    add.execute();

                }

            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        frag = new AddProdContentFragment();
        fragTrans = getSupportFragmentManager().beginTransaction().replace(R.id.add_prod_activity_fragment,frag);
        fragTrans.commit();
    }

    @Override
    public void onStop() {
        super.onStop();
        stopLocation();
        if(pdia!= null)
            pdia.dismiss();
    }
    @Override
    public void onBackPressed(){


        if (frag instanceof AddProdContentFragment) {
            Intent i = new Intent(AddProdActivity.this,MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            finish();
            super.onBackPressed();
        } else {

            getSupportFragmentManager().popBackStack();
            frag = new AddProdContentFragment();
            toolbar.setTitle(TITLE_ADD_PROD);
            fragTrans = getSupportFragmentManager().beginTransaction().replace(R.id.add_prod_activity_fragment,frag);
            fragTrans.commit();

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (frag instanceof AddProdContentFragment)frag.onActivityResult(requestCode, resultCode, intent);
    }

    @Override
    public void onAddProdData(String barcode, String prodName, String prodDetails) {

        this.barcode=barcode;
        prod_name = prodName;
        prod_details = prodDetails;
    }

    /***Location stuff**/

    private void startLocation() {

        provider = new LocationGooglePlayServicesProvider();
        provider.setCheckLocationSettings(true);

        SmartLocation smartLocation = new SmartLocation.Builder(AddProdActivity.this).logging(true).build();

        smartLocation.location(provider).start(this);

    }

    private void stopLocation() {
        SmartLocation.with(AddProdActivity.this).location().stop();
    }

    private void showLocation(Location location) {
        if (location != null) {
            latitude = String.format("%.6f",location.getLatitude());
            longitude = String.format("%.6f",location.getLongitude());
            final String text = "Latitude "+latitude + ", Longitude "+ longitude;
            revGeocodededLocation = text;

            // We are going to get the address for the current position
            SmartLocation.with(AddProdActivity.this).geocoding().reverse(location, new OnReverseGeocodingListener() {
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
            Toast.makeText(AddProdActivity.this,"Can't get location data!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onLocationUpdated(Location location) {
        showLocation(location);
        stopLocation();
    }

    private class AddProduct extends AsyncTask<String,Void,Boolean> {

        //three methods get called, first preExecute, then do in background, and once do
        //in back ground is completed, the onPost execute method will be called.//TODO:incorporate on progresssdialog cancelled method in all asynctask

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdia = new ProgressDialog(AddProdActivity.this);
            pdia.setMessage("Adding Product...");
            pdia.show();
        }

        @Override
        protected Boolean doInBackground(String... str) {
            utility u = new utility();
            return u.addProd(barcode,prod_name,prod_details,longitude,latitude);
        }

        protected void onPostExecute(Boolean t) {
            pdia.dismiss();
            if(t){
                Toast.makeText(AddProdActivity.this,"Product added to DB",Toast.LENGTH_SHORT).show();
                getSupportFragmentManager().popBackStack();
                frag = new AddProdContentFragment();
                toolbar.setTitle(TITLE_ADD_PROD);
                fragTrans = getSupportFragmentManager().beginTransaction().replace(R.id.add_prod_activity_fragment,frag);
                fragTrans.commit();
            }
            else{
                Toast.makeText(AddProdActivity.this,"Product can't be added",Toast.LENGTH_SHORT).show();//TODO: replace toast wid success and unsuccessful addition frags
                getSupportFragmentManager().popBackStack();
                frag = new AddProdContentFragment();
                toolbar.setTitle(TITLE_ADD_PROD);
                fragTrans = getSupportFragmentManager().beginTransaction().replace(R.id.add_prod_activity_fragment,frag);
                fragTrans.commit();
            }
        }


    }

}
