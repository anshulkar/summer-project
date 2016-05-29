package com.example.varun.baasboxapp;

import android.util.Log;
import android.widget.Toast;

import com.baasbox.android.BaasHandler;
import com.baasbox.android.BaasResult;
import com.baasbox.android.BaasUser;
import com.baasbox.android.json.JsonObject;

/**
 * Created by varun on 26/05/16.
 */
public class LoginSignup {
    String Username="",Pwd="";

    boolean loginFlag=false,signUpFlag=false;

    public boolean signUp(String U,String P)
    {
        Username=U;
        Pwd=P;
        BaasUser user = BaasUser.withUserName(U)
        .setPassword(P);

        user.signup(new BaasHandler<BaasUser>(){
            @Override
            public void handle(BaasResult<BaasUser> result){
                if(result.isSuccess()) {
                    signUpFlag=true;
                } else {
                    Log.e("LOG","Show error",result.error());
                }
            }
        });
        return signUpFlag;
    }

    public boolean login(String U,String P)
    {
        BaasUser user = BaasUser.withUserName(U)
                .setPassword(P);
        user.login(new BaasHandler<BaasUser>() {
            @Override
            public void handle(BaasResult<BaasUser> result) {
                if(result.isSuccess()) {
                    loginFlag=true;//Success code here
                } else {
                    Log.e("LOG","Show error",result.error());
                }
            }
        });
        return loginFlag;
    }

}
