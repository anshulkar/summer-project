package com.varun.baasbox;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.baasbox.android.*;
import com.baasbox.android.json.JsonObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class utility {

    private BaasBox client;
    private boolean flag;
    private boolean loginFlag=false;
    private boolean signUpFlag=false;
    static String Username="",Pwd="";
    static BaasUser user;
    private boolean result=false;
    static String prodName="",prodDetails="";
    private Context context;

    boolean executor=false;
    boolean errorFlag=false;
    //Call this in onCreate method of the very first activity of the app
    public void init(Context context) throws Exception{
        this.context = context;
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if(activeNetworkInfo == null || !activeNetworkInfo.isConnected())
        {
            throw new Exception();
        }
        BaasBox.Builder b =
                new BaasBox.Builder(context);
        client = b.setApiDomain("40.121.94.107")
                .setAppCode("1234567890")
                .init();
        Thread t=new Thread(){
          public void run(){
              try{login("admin","admin");}
              catch (Exception e){}
          }
        };
        t.start();

        Log.d("Init Method","Called");
    }

    //For checking whether the barcode exists in the db
    public boolean checkProduct(String s)throws Exception
    {
        errorFlag=false;
        result=false;
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
                    try {
                        Log.d("Product Check", "Error:: " + res.value().toString());
                    }
                    catch (Exception e){
                        errorFlag=true;
                    }
                    executor=true;
                }


            }
        });
        if(errorFlag)
        {
            throw new Exception();
        }
        while(!executor){

        }
        executor=false;
        return result;
    }


    //To insert new Product
    public boolean addProd(String barcode,String name,String details,String longitude,String latitude)throws Exception
    {


        BaasDocument doc = new BaasDocument("sample");
        flag=false;
        errorFlag=false;
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
                        errorFlag=true;
                        e.printStackTrace();
                    }

                }
            }
        });
        if(errorFlag)
        {
            throw new Exception();
        }

        while(!executor){

        }
        executor=false;
        return flag;

    }

    public boolean signUp(String U,String P)
    {
        signUpFlag=false;
        user = BaasUser.withUserName(U)
                .setPassword(P);

        user.signup(new BaasHandler<BaasUser>(){
            @Override
            public void handle(BaasResult<BaasUser> result){
                if(result.isSuccess()) {
                    signUpFlag=true;
                    executor=true;
                } else {
                    executor=true;
                    Log.e("SIGNUP Function","Show error",result.error());
                }
            }
        });
        while(!executor)
        {

        }
        executor=false;
        return signUpFlag;
    }

    public boolean login(String U,String P)throws Exception
    {
        loginFlag=false;
        errorFlag=false;
        executor=false;
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
                    executor=true;

                    //Success code here
                } else {
                    Log.e("LOGIN","Show error",result.error());
                    errorFlag=true;
                    executor=true;
                }

            }
        });
        if(errorFlag){
            throw new Exception();
        }
        while (!executor){}
        executor=false;

        return loginFlag;

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
        errorFlag=false;
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
                            errorFlag=true;
                        }
                    }
                });
        if(errorFlag)
        {
            //throw new Exception();
        }

        while(!executor2){}
        return str_arr;
    }
}
