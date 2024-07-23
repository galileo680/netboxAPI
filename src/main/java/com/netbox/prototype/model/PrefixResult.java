package com.netbox.prototype.model;

public class PrefixResult {
    private int id;
    private String prefix;
    private int _depth;
    private int children;

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public int getDepth() {
        return _depth;
    }

    public void setDepth(int depth) {
        this._depth = depth;
    }

    public int getChildren() {
        return children;
    }

    public void setChildren(int children) {
        this.children = children;
    }
}
