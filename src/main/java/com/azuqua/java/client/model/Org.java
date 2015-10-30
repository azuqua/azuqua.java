package com.azuqua.java.client.model;
import java.util.ArrayList;

public class Org {
    private String name;
    private String accessKey;
    private String accessSecret;
    private ArrayList<Flo> flos;

    public Org(String name, String accessKey, String accessSecret, ArrayList<Flo> flos){
        this.name = name;
        this.accessKey = accessKey;
        this.accessSecret = accessSecret;
        this.flos = flos;
    }


    public void setName(String name) {
        this.name = name;
    }

    //Get Name
    public String getName(){
        return this.name;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public void setAccessSecret(String accessSecret) {
        this.accessSecret = accessSecret;
    }

    public void setFlos(ArrayList<Flo> flos) {
        this.flos = flos;
    }

    //Get AccessKey
    public String getAccessKey(){
        return this.accessKey;
    }

    //Get AccessSecret
    public String getAccessSecret(){
        return this.accessSecret;
    }

    //Get Flos
    public ArrayList<Flo> getFlos(){
        return this.flos;
    }

    //Add Flo
    public void addFlo(Flo flo){
        this.flos.add(flo);
    }
}