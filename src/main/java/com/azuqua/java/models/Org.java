package com.azuqua.java.models;

/**
 * Created by SASi on 14-Jul-16.
 */
public class Org {
    private long id;
    private String name;
    private String display_name;
    private String namespace;
    private String trail_start;
    private String trail_end;
    private boolean trail_used;
    private boolean active;
    private boolean enabled;
    private String role_type;

    public Org() {
    }

    public Org(long id, String name, String display_name, String namespace, String trail_start, String trail_end, boolean trail_used, boolean active, boolean enabled, String role_type) {
        this.id = id;
        this.name = name;
        this.display_name = display_name;
        this.namespace = namespace;
        this.trail_start = trail_start;
        this.trail_end = trail_end;
        this.trail_used = trail_used;
        this.active = active;
        this.enabled = enabled;
        this.role_type = role_type;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getTrail_start() {
        return trail_start;
    }

    public void setTrail_start(String trail_start) {
        this.trail_start = trail_start;
    }

    public String getTrail_end() {
        return trail_end;
    }

    public void setTrail_end(String trail_end) {
        this.trail_end = trail_end;
    }

    public boolean isTrail_used() {
        return trail_used;
    }

    public void setTrail_used(boolean trail_used) {
        this.trail_used = trail_used;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    
    public String getRole_type() {
        return role_type;
    }

    public void setRole_type(String role_type) {
        this.role_type = role_type;
    }
}
