package com.netbox.prototype.model;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class MultiplePrefixes {

    @NotNull
    @Min(1)
    private Integer count;

    @NotNull
    @Min(1)
    private Integer length;

    @NotNull
    private String prefix;


    public @NotNull @Min(1) Integer getCount() {
        return count;
    }

    public void setCount(@NotNull @Min(1) Integer count) {
        this.count = count;
    }

    public @NotNull @Min(1) Integer getLength() {
        return length;
    }

    public void setLength(@NotNull @Min(1) Integer length) {
        this.length = length;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
}













