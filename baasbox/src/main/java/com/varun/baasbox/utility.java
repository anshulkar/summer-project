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
    static String prodName="",prodDetails="";

    boolean executor=false;

    //Call this in onCreate method of the very first activity of the app
    public void init(Context appContext){
        BaasBox.Builder b =
                new BaasBox.Builder(appContext);
        client = b.setApiDomain("40.121.94.107")
                .setAppCode("1234567890")
                .init();
        login("admin","admin");

        Log.d("Init Method","Called");
    }

    //For checking whether the barcode exists in the db
    public boolean checkProduct(String s)
    {

        final BaasQuery PREPARED_QUERY =
                BaasQuery.builder()
                        .collection("sample")
                        .where("Barcode ='"+s+"' and _author='admin'")
                        .build();
        PREPARED_QUERY.query(new BaasHandler<List<JsonObject>>(){
            @Override
            public void handle(BaasResult<List<JsonObject>> res){
                if(res.isSuccess())
                {
                    //Toast.makeText(getApplicationContext(),res.value().toString(),Toast.LENGTH_LONG).show();
                    //JsonObject obj=res.value();
                    List<JsonObject> objlist=res.value();
                    if(objlist.size()>0) {
                        result=true;
                        JsonObject obj = objlist.get(0);
                        prodName=obj.getString("Product_Name");
                        prodDetails=obj.getString("Product_Details");
                    }
                    else
                    {
                        prodName="";
                        prodDetails="";
                    }

                    Log.d("Product Check","No. of items "+objlist.size());
                    executor=true;
                }
                else {
                    result=false;
                    Log.d("Product Check","Error:: "+res.value().toString());
                    executor=true;
                }


            }
        });

//        while(!executor){
//
//        }
        executor=false;
        return result;
    }


    //To insert new Product
    public boolean addProd(String barcode,String name,String details,String longitude,String latitude)
    {


        BaasDocument doc = new BaasDocument("sample");


        doc.put("Barcode",barcode).put("Product_Name",name).put("Product_Details",details).put("Latitude",latitude).put("Longitude",longitude);
        Log.d("Insert Function",doc.toJson().toString());
        doc.save(new BaasHandler<BaasDocument>() {
            @Override
            public void handle(BaasResult<BaasDocument> res) {
                if(res.isSuccess()) {
                    Log.d("Insert Function","Reached here");
                    flag=true;
                    executor=true;
                } else {
                    try {
                        Log.d("Insert Function",res.get().toString());
                        flag=false;
                        executor=true;
                    } catch (BaasException e) {
                        e.printStackTrace();
                    }

                }
            }
        });

        while(!executor){

        }
        executor=false;
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

        return prodName;
    }

    public String getProdDetails(String barcode){
        return prodDetails;
    }

    boolean executor2=false;
    public ArrayList<String> getAllProdNames(){
        /**Fetch list of all names of products in DB*/
        final ArrayList<String> str_arr = new ArrayList<>();

        BaasDocument.fetchAll("sample",
                new BaasHandler<List<BaasDocument>>() {
                    @Override
                    public void handle(BaasResult<List<BaasDocument>> res) {

                        if (res.isSuccess()) {
                            for (BaasDocument doc:res.value()) {
                                str_arr.add(doc.getString("Product_Name"));
                                Log.d("LOG","Doc: "+doc.getString("Product_Name"));
                            }
                            executor2 = true;
                        } else {
                            executor2=true;
                            Log.e("LOG","Error",res.error());
                        }
                    }
                });

        while(!executor2){}
        return str_arr;
    }
}
