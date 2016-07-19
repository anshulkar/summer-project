package com.varun.baasbox;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.baasbox.android.*;
import com.baasbox.android.json.JsonObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class utility {

    private BaasBox client;
    static boolean flag;
    static boolean loginFlag=false;
    static boolean signUpFlag=false;
    static String Username="",Pwd="";
    static BaasUser user;
    static boolean result=false;

    //Call this in onCreate method of the very first activity of the app
    public void init(Context appContext){
        BaasBox.Builder b =
                new BaasBox.Builder(appContext);
        client = b.setApiDomain("40.121.94.107")
                .setAppCode("1234567890")
                .init();
    }

    //For checking whether the barcode exists in the db
    public boolean checkProduct(String s)
    {

        final BaasQuery PREPARED_QUERY =
                BaasQuery.builder()
                        .collection("sample")
                        .where("jar ='"+s+"' and _author='admin'")
                        .build();
        PREPARED_QUERY.query(new BaasHandler<List<JsonObject>>(){
            @Override
            public void handle(BaasResult<List<JsonObject>> res){
                if(res.isSuccess())
                {
                    //Toast.makeText(getApplicationContext(),res.value().toString(),Toast.LENGTH_LONG).show();
                    //JsonObject obj=res.value();
                    List<JsonObject> objlist=res.value();
                    result=true;
                    Log.d("Product Check","No. of items "+objlist.size());
                }
                else {
                    result=false;
                    Log.d("Product Check","Error:: "+res.value().toString());

                }


            }
        });
        return result;
    }


    //To insert new Product
    public boolean insert(String name,String latitude,String longitude)
    {


        BaasDocument doc = new BaasDocument("sample");

        doc.put("Product_Name",name).put("Latitude",latitude).put("Longitude",longitude);
        Log.d("Insert Function",doc.toJson().toString());
        doc.save(new BaasHandler<BaasDocument>() {
            @Override
            public void handle(BaasResult<BaasDocument> res) {
                if(res.isSuccess()) {
                    Log.d("Insert Function","Reached here");
                    flag=true;
                } else {
                    try {
                        Log.d("Insert Function",res.get().toString());
                        flag=false;
                    } catch (BaasException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
        return flag;

    }

    public boolean signUp(String U,String P)
    {

        user = BaasUser.withUserName(U)
                .setPassword(P);

        user.signup(new BaasHandler<BaasUser>(){
            @Override
            public void handle(BaasResult<BaasUser> result){
                if(result.isSuccess()) {
                    signUpFlag=true;
                } else {
                    Log.e("SIGNUP Function","Show error",result.error());
                }
            }
        });
        return signUpFlag;
    }

    public void login(String U,String P)
    {
        Username=U;
        Pwd=P;
        BaasUser user = BaasUser.withUserName(U)
                .setPassword(P);

        user.login(new BaasHandler<BaasUser>() {
            @Override
            public void handle(BaasResult<BaasUser> result) {
                if(result.isSuccess()) {
                    Log.d("LOGIN",result.value().toString());
                    loginFlag=true;


                    //Success code here
                } else {
                    Log.e("LOGIN","Show error",result.error());

                }

            }
        });


    }

    /***Unimplemented methods TODO: implement them*/
    public String getProdName(String barcode){
        return "Product name";
    }

    public String getProdDetails(String barcode){
        return "Product details";
    }
    public Boolean addProd(String barcode,String prod_name, String prod_details, String longitude, String latitude){
        /**Add product to DB*/
        return false;
    }
    public ArrayList<String> getAllProdNames(){
        /**Fetch list of all names of products in DB*/
        ArrayList<String> str_arr = new ArrayList<>();
        str_arr.add("prod_1_name");
        return str_arr;
    }
}
