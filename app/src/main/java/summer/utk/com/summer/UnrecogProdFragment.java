package summer.utk.com.summer;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.varun.baasbox.utility;

import java.util.ArrayList;


public class UnrecogProdFragment extends Fragment {

    private MaterialSearchView searchView;
    private RecyclerView recyclerView;
    private StringsDataAdapter mAdapter;
    private ArrayList<String> prod_names,shownList;
    private ProgressDialog pdia;
    private boolean searchViewSubmitPressed=false;/**It is a temporary workaround so that when submit is called at the end querytextchange doesnt clear the list*/

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
        /*searchView.showSearch();
        searchView.clearFocus();*/
        searchView.setVoiceSearch(false);



        shownList = new ArrayList<>();
        prod_names = new ArrayList<>();
        shownList.add("No products found");

        FetchData fetchdata = new FetchData();
        fetchdata.execute();
        recyclerView = (RecyclerView) view.findViewById(R.id.unrecogProdRecyclerView);
        mAdapter = new StringsDataAdapter(shownList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchViewSubmitPressed=true;
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                if(!searchViewSubmitPressed) {
                    shownList.clear();
                    //Log.d("change", query);
                    if (query == null || query.equals("") || query.isEmpty()) shownList.addAll(prod_names);
                    /**search algo*/
                    else for (String str : prod_names) if (str.contains(query)) shownList.add(str);
                    /**search algo end*/
                    //Log.d("change", shownList.size() + "");
                    if(shownList.size()==0)shownList.add("NO SEARCH RESULTS");
                    mAdapter.notifyDataSetChanged();
                }
                else searchViewSubmitPressed=false;
                return false;
            }
        });

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                shownList.clear();
                shownList.addAll(prod_names);
                mAdapter.notifyDataSetChanged();
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
            //prod_names = u.getAllProdNames(); This cant be done as this changes reference of prod_names causing the adapter to lose the reference.
            prod_names.addAll(u.getAllProdNames());
            Log.d("unrecProd",""+prod_names.size());
            return prod_names.size()!=0;
        }

        protected void onPostExecute(Boolean t) {
            if(!t)Toast.makeText(getActivity(),"Network unreachable or Internal server Error",Toast.LENGTH_LONG).show();
            else {
                shownList.clear();
                shownList.addAll(prod_names);
                searchView.setSuggestions(prod_names.toArray(new String[0]));
            }
            mAdapter.notifyDataSetChanged();
            pdia.dismiss();
        }


    }

}
