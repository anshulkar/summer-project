package com.example.varun.kinveyapp;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;

/**
 * Created by varun on 08/05/16.
 */
public class sampleDb extends GenericJson{

    public sampleDb()
    {

    }

    @Key("_id")
    private String id;
    @Key
    private String name;

    @Key
    private String value;

    public void setName(String s)
    {
        name=s;
    }

    public void setValue(String s)
    {
        value=s;
    }


}
