package com.azuqua.java.models;

/**
 * Created by SASi on 14-Jul-16.
 */
public class FLO {
    private int id;
    private int user_id;
    private int org_id;
    private String alias;
    private String version;
    private String name;
    private String module;
    private boolean active;
    private boolean published;
    private String security_level;
    private String client_token;
    private String description;
    private String created;
    private String updated;
    private String role_type;
    private User user;

    public FLO() {
    }

    public FLO(int id, int user_id, int org_id, String alias, String version, String name, String module, boolean active, boolean published, String security_level, String client_token, String description, String created, String updated, String role_type, User user) {
        this.id = id;
        this.user_id = user_id;
        this.org_id = org_id;
        this.alias = alias;
        this.version = version;
        this.name = name;
        this.module = module;
        this.active = active;
        this.published = published;
        this.security_level = security_level;
        this.client_token = client_token;
        this.description = description;
        this.created = created;
        this.updated = updated;
        this.role_type = role_type;
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getOrg_id() {
        return org_id;
    }

    public void setOrg_id(int org_id) {
        this.org_id = org_id;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isPublished() {
        return published;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }

    public String getSecurity_level() {
        return security_level;
    }

    public void setSecurity_level(String security_level) {
        this.security_level = security_level;
    }

    public String getClient_token() {
        return client_token;
    }

    public void setClient_token(String client_token) {
        this.client_token = client_token;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public String getRole_type() {
        return role_type;
    }

    public void setRole_type(String role_type) {
        this.role_type = role_type;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
