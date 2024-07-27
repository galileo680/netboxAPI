package com.netbox.prototype.model;

public class Prefix {

    private String prefix;
    private String description;


    // nowe
    private int id;
    private int depth;
    private int children;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }



    public Prefix(){

    }

    public Prefix(String prefix, String description) {
        //this.id = id;
        this.prefix = prefix;
        this.description = description;
    }

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

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public int getChildren() {
        return children;
    }

    public void setChildren(int children) {
        this.children = children;
    }

    @Override
    public String toString() {
        return "Prefix{" +
                "prefix='" + prefix + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", depth=" + depth +
                ", children=" + children +
                '}';
    }
}
