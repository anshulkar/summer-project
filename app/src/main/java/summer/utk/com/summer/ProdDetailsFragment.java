package summer.utk.com.summer;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProdDetailsFragment extends Fragment {

    public static String LOCATION="location",PROD_NAME="product name",PROD_DETAILS="product details",BARCODE="barcode",LONGITUDE="longitude",LATITUDE="latitude";

    EditText barcode,name,details,location,longitude,latitude;
    public ProdDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_prod_details, container, false);

        Bundle args = this.getArguments();

        barcode = (EditText) view.findViewById(R.id.barcode);
        name = (EditText) view.findViewById(R.id.name);
        details = (EditText) view.findViewById(R.id.details);
        location = (EditText) view.findViewById(R.id.location);
        longitude = (EditText) view.findViewById(R.id.longitude);
        latitude = (EditText) view.findViewById(R.id.latitude);


        barcode.setText(args.getString(BARCODE));
        name.setText(args.getString(PROD_NAME));
        details.setText(args.getString(PROD_DETAILS));
        location.setText(args.getString(LOCATION));
        if(args.containsKey(LONGITUDE) || args.containsKey(LATITUDE)){
            longitude.setText(args.getString(LONGITUDE));
            latitude.setText(args.getString(LATITUDE));
            longitude.setVisibility(View.VISIBLE);
            latitude.setVisibility(View.VISIBLE);
        }

        return view;
    }

}
