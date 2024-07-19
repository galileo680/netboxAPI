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
    private String parentPrefix;


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

    public @NotNull String getParentPrefix() {
        return parentPrefix;
    }

    public void setParentPrefix(@NotNull String parentPrefix) {
        this.parentPrefix = parentPrefix;
    }
}













