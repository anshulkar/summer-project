package summer.utk.com.summer;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.varun.baasbox.utility;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnBarcodeScanResultListener} interface
 * to handle interaction events.
 */
public class ScanFragment extends Fragment{


    private String codeFormat,codeContent;
    private TextView formatTxt, contentTxt,locationTxt;
    private Button scanButton,addProdButton;
    private OnBarcodeScanResultListener mListener;

    public ScanFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_scan, container, false);
        formatTxt = (TextView)view.findViewById(R.id.scan_format);
        contentTxt = (TextView)view.findViewById(R.id.scan_content);
        locationTxt = (TextView) view.findViewById(R.id.location_text);

        scanButton = (Button)view.findViewById(R.id.scan_button);
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanNow(v);
            }
        });

        addProdButton = (Button)view.findViewById(R.id.add_prod_button);
        addProdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(),LoginActivity.class);
                startActivity(i);
            }
        });


        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void sendBarcodeToActivity(String ssttrr, Boolean ttff) {
        if (mListener != null) {
            mListener.onScannedBarcode(ssttrr,ttff);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnBarcodeScanResultListener) {
            mListener = (OnBarcodeScanResultListener) context;
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
    public interface OnBarcodeScanResultListener {
        // TODO: Update argument type and name
        void onScannedBarcode(String barcode,Boolean DBmatch);
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
            formatTxt.setText("FORMAT: " + codeFormat);
            contentTxt.setText("CONTENT: " + codeContent);
            HandleScan handlescanresults = new HandleScan();
            handlescanresults.execute("noice");//the gui cant get updated when the async task is running

        }else{
            Toast toast = Toast.makeText(getActivity(),"No scan data received!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    class HandleScan extends AsyncTask<String,Void,Boolean> {

        //three methods get called, first preExecute, then do in background, and once do
        //in back ground is completed, the onPost execute method will be called.//TODO:incorporate on progresssdialog cancelled method in all asynctask

        ProgressDialog pdia;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdia = new ProgressDialog(getActivity());
            pdia.setMessage("Connecting to server ...");
            pdia.show();
        }

        @Override
        protected Boolean doInBackground(String... str) {
            utility u = new utility();
            return u.checkProduct(codeContent);
        }

        protected void onPostExecute(Boolean t) {
            pdia.dismiss();
            sendBarcodeToActivity(codeContent,t);

        }


    }
}
