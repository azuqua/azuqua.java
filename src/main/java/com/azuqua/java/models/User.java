package com.azuqua.java.models;

/**
 * Created by SASi on 14-Jul-16.
 */

import java.util.List;

public class User {
    private long id;
    private String email;
    private String company;
    private String first_name;
    private String middle_name;
    private String last_name;
    private String access_key;
    private String access_secret;
    private List<Org> orgs;

    public User() {
    }

    public User(long id, String email, String company, String first_name, String middle_name, String last_name, String access_secret, String access_key, List<Org> orgs) {
        this.id = id;
        this.email = email;
        this.company = company;
        this.first_name = first_name;
        this.middle_name = middle_name;
        this.last_name = last_name;
        this.access_secret = access_secret;
        this.access_key = access_key;
        this.orgs = orgs;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getMiddle_name() {
        return middle_name;
    }

    public void setMiddle_name(String middle_name) {
        this.middle_name = middle_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getAccess_secret() {
        return access_secret;
    }

    public void setAccess_secret(String access_secret) {
        this.access_secret = access_secret;
    }

    public List<Org> getOrgs() {
        return orgs;
    }

    public void setOrgs(List<Org> orgs) {
        this.orgs = orgs;
    }

    public void setAccess_key(String access_key) {
        this.access_key = access_key;
    }

    public String getAccess_key() {
        return access_key;
    }
}
