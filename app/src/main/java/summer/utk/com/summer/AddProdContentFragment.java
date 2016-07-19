package summer.utk.com.summer;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddProdContentFragment extends Fragment {

    private String codeFormat;
    private String codeContent="";

    EditText name,details;
    TextView barcode;
    private OnAddProdDataListener mListener;

    public AddProdContentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_prod_content, container, false);

        barcode = (TextView) view.findViewById(R.id.prod_barcode);

        name = (EditText) view.findViewById(R.id.prod_name);
        details = (EditText) view.findViewById(R.id.prod_details);


        barcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanNow(v);
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnAddProdDataListener) {
            mListener = (OnAddProdDataListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnAddProdDataListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;

    }

    public boolean submitButtonPressed(){
        TextInputLayout name_layout = (TextInputLayout)name.getParent();
        if(codeContent.equals("")){
            barcode.setText("Please scan a barcode");
            barcode.setTextColor(Color.parseColor("red"));
            return false;
        }
        if(name.getText().toString().isEmpty() || name.getText().equals("") || name.getText().equals(null)){
            name_layout.setError("Please enter a Product Name");
            return false;
        }
        sendProdDataToActivity(codeContent,name.getText().toString(),details.getText().toString());
        return true;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnAddProdDataListener {
        // TODO: Update argument type and name
        void onAddProdData(String barcode, String prodName, String prodDetails);
    }

    public void sendProdDataToActivity(String ssttrr, String ttrrss, String rrsstt) {
        if (mListener != null) {
            mListener.onAddProdData(ssttrr,ttrrss, rrsstt);
        }
    }

    public void scanNow(View view){
        IntentIntegrator integrator = new IntentIntegrator(getActivity());
        integrator.setCaptureActivity(ScannerActivity.class);
        integrator.setOrientationLocked(false);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
        integrator.setPrompt("Scan a QR code or Barcode");/*
        integrator.setResultDisplayDuration(0);
        integrator.setWide();  // Wide scanning rectangle, may work better for 1D barcodes*/
        integrator.setCameraId(0);  // Use a specific camera of the device
        integrator.initiateScan();
    }

    /**
     * function handle scan result
     * @param requestCode
     * @param resultCode
     * @param intent
     */
    //Move theactivity result in parent activity
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
//retrieve scan result
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);

        if (scanningResult != null) {
//we have a result
            codeContent = scanningResult.getContents();
            codeFormat = scanningResult.getFormatName();

// display it on screen
            barcode.setText("FORMAT: " + codeFormat);
            barcode.setTextColor(Color.parseColor("white"));
            //contentTxt.setText("CONTENT: " + codeContent);
            //HandleScan handlescanresults = new HandleScan();
            //handlescanresults.execute("noice");//the gui cant get updated when the async task is running

        }else{
            Toast toast = Toast.makeText(getActivity(),"No scan data received!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}
