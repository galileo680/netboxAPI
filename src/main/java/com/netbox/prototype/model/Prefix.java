package com.netbox.prototype.model;

public class Prefix {
    //private String id;
    private String prefix;
    private String description;

    public Prefix(String prefix, String description) {
        //this.id = id;
        this.prefix = prefix;
        this.description = description;
    }


    /*public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }*/

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
