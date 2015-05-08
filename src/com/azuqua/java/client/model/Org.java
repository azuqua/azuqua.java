package com.azuqua.java.client.model;
import java.util.ArrayList;
import java.util.Collection;

import com.azuqua.java.client.Azuqua;
import com.azuqua.java.client.AzuquaException;

public class Org {
    private String name;
    private String accessKey;
    private String accessSecret;
    private Azuqua azuqua;
    
	public Org(String name, String accessKey, String accessSecret, ArrayList<Flo> flos){
        this.name = name;
        this.accessKey = accessKey;
        this.accessSecret = accessSecret;
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
    
    //Get AccessKey
    public String getAccessKey(){
        return this.accessKey;
    }

    //Get AccessSecret
    public String getAccessSecret(){
        return this.accessSecret;
    }
    
    public Collection<Flo> getFlos() throws AzuquaException {
    	return azuqua.getFlos();
    }

	public void setAzuqua(Azuqua azuqua) {
		this.azuqua = azuqua;
	}

}