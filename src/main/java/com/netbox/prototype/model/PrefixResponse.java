package com.netbox.prototype.model;

import java.util.List;

public class PrefixResponse {
    private int count;
    private String next;
    private String previous;
    private List<PrefixResult> results;

    // Getters and setters
    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public String getPrevious() {
        return previous;
    }

    public void setPrevious(String previous) {
        this.previous = previous;
    }

    public List<PrefixResult> getResults() {
        return results;
    }

    public void setResults(List<PrefixResult> results) {
        this.results = results;
    }
}
