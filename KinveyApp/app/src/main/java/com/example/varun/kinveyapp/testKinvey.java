package com.example.varun.kinveyapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.kinvey.android.AsyncAppData;
import com.kinvey.android.Client;
import com.kinvey.android.callback.KinveyListCallback;
import com.kinvey.android.callback.KinveyPingCallback;
import com.kinvey.android.callback.KinveyUserCallback;
import com.kinvey.java.Query;
import com.kinvey.java.User;
import com.kinvey.java.core.KinveyClientCallback;


public class testKinvey extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_kinvey);
        final Client mKinveyClient = new Client.Builder("kid_bk8OMTuHGb", "fb58705deeed45378bfc555a47c1bdc0"
                , this.getApplicationContext()).build();

        final EditText Username=(EditText)findViewById(R.id.uname);
        final EditText Pwd=(EditText)findViewById(R.id.password);
        Button btn=(Button)findViewById(R.id.button);
        //Button insertBtn=(Button)findViewById(R.id.sendData);
        //Button queryBtn=(Button)findViewById(R.id.queryData);


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                mKinveyClient.user().login(Username.getText().toString(), Pwd.getText().toString(), new KinveyUserCallback() {
                    @Override
                    public void onFailure(Throwable t) {
                        CharSequence text = "Wrong username or password.";
                        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onSuccess(User u) {
                        CharSequence text = "Welcome back," + u.getUsername() + ".";
                        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });


        /*insertBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getApplicationContext(),"Clicked",Toast.LENGTH_LONG);
                sampleDb obj=new sampleDb();
                obj.setName(Username.getText().toString());
                obj.setValue(Pwd.getText().toString());

                AsyncAppData<sampleDb> myevents = mKinveyClient.appData("Objects", sampleDb.class);
                myevents.save(obj, new KinveyClientCallback<sampleDb>() {
                    @Override
                    public void onFailure(Throwable e) {
                        Log.e("TAG", "failed to save event data", e);
                    }
                    @Override
                    public void onSuccess(sampleDb r) {
                        Log.d("TAG", "saved data for entity ");
                    }
                });

            }
        });

        queryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Clicked",Toast.LENGTH_LONG).show();
                sampleDb obj=new sampleDb();
                Query q=mKinveyClient.query();
                q.equals("value",Pwd.getText().toString());

                AsyncAppData<sampleDb> myEvents = mKinveyClient.appData("Objects", sampleDb.class);
                myEvents.get(q, new KinveyListCallback<sampleDb>() {
                    @Override
                    public void onSuccess(sampleDb[] results) {
                        Toast.makeText(getApplicationContext(), "received "+ results.length + " objects",Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onFailure(Throwable error) {
                        Toast.makeText(getApplicationContext(), "Failed",Toast.LENGTH_SHORT).show();
                        Log.e("TAG", "failed to fetchByFilterCriteria", error);
                    }
                });

            }
        });
*/

    }
}
