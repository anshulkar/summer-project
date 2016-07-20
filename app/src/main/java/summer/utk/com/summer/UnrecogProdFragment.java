package summer.utk.com.summer;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.varun.baasbox.utility;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class UnrecogProdFragment extends Fragment {

    private MaterialSearchView searchView;
    private RecyclerView recyclerView;
    private StringsDataAdapter mAdapter;
    private ArrayList<String> prod_names;
    private ProgressDialog pdia;
    String[] search_suggestions;

    public UnrecogProdFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_unrecog_prod, container, false);
        searchView = (MaterialSearchView) ((AppCompatActivity)getActivity()).findViewById(R.id.search_view);
        searchView.showSearch();
        searchView.setVoiceSearch(false);



        prod_names = new ArrayList<>();

        FetchData fetchdata = new FetchData();
        fetchdata.execute();
        recyclerView = (RecyclerView) view.findViewById(R.id.unrecogProdRecyclerView);
        mAdapter = new StringsDataAdapter(prod_names);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        /*search_suggestions = prod_names.toArray(new String[0]);*///TODO:implement search suggestions
        //searchView.setSuggestions(search_suggestions);
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Do some magic
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                String[] words = new String[10];


                return false;
            }
        });

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                //Do some magic
            }

            @Override
            public void onSearchViewClosed() {
                //Do some magic
            }
        });

        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if(pdia!= null)
            pdia.dismiss();
    }

    class FetchData extends AsyncTask<String,Void,Boolean> {

        //three methods get called, first preExecute, then do in background, and once do
        //in back ground is completed, the onPost execute method will be called.//TODO:incorporate on progresssdialog cancelled method in all asynctask

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
            prod_names = u.getAllProdNames();
            return true;
        }

        protected void onPostExecute(Boolean t) {
            mAdapter.notifyDataSetChanged();
            pdia.dismiss();
        }


    }

}
