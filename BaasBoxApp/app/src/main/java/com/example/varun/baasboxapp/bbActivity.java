package com.example.varun.baasboxapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.baasbox.android.*;
import com.baasbox.android.json.JsonObject;

import java.util.List;

public class bbActivity extends AppCompatActivity {
    private BaasBox client;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bb);

        BaasBox.Builder b =
                new BaasBox.Builder(this);
        client = b.setApiDomain("192.168.0.101")
                .setAppCode("1234567890")
                .init();

        final BaasQuery PREPARED_QUERY =
                BaasQuery.builder()
                        .collection("sample")
                        .where("key=1")
                        .build();
// then
//        BaasUser user = BaasUser.withUserName("admin")
//                .setPassword("admin");
//        user.login(new BaasHandler<BaasUser>() {
//            @Override
//            public void handle(BaasResult<BaasUser> result) {
//                if(result.isSuccess()) {
//                    Log.d("LOG", "The user is currently logged in: "+result.value());
//                } else {
//                    Log.e("LOG","Show error",result.error());
//                }
//            }
//        });
        Button btn=(Button)findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                PREPARED_QUERY.query(new BaasHandler<List<JsonObject>>(){
                    @Override
                    public void handle(BaasResult<List<JsonObject>> res){
                        if(res.isSuccess())
                        {
                            Toast.makeText(getApplicationContext(),""+res.value(),Toast.LENGTH_LONG).show();
                            //JsonObject obj=res.value();
                            List<JsonObject> objlist=res.value();
                            System.out.println(objlist.size());

                        }
                        else {
                            Toast.makeText(getApplicationContext(),"Failed",Toast.LENGTH_SHORT).show();
                        }
                        // handle result or failure

                    }
                });

            }
        });
    }
}
