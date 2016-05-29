package com.example.varun.baasboxapp;

import android.util.Log;

import com.baasbox.android.BaasDocument;
import com.baasbox.android.BaasHandler;
import com.baasbox.android.BaasResult;

/**
 * Created by varun on 29/05/16.
 */
public class insertInfo {
    boolean flag=false;

    public boolean insert(String name,String price)
    {
        BaasDocument doc = new BaasDocument("sample");
        doc.put("Product Name",name).put("Price",price);

        doc.save(new BaasHandler<BaasDocument>() {
            @Override
            public void handle(BaasResult<BaasDocument> res) {
                if(res.isSuccess()) {
                    flag=true;
                } else {

                }
            }
        });
        return flag;

    }
}
