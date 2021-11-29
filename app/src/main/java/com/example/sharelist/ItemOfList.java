package com.example.sharelist;

public class ItemOfList {
    String name;
    String itemId;
    Integer count;
    String description;
    Boolean bought = false;

    public ItemOfList() {
    }

    public ItemOfList(String name) {
        this.name = name;
    }

    public ItemOfList(String name, boolean bought) {
        this.name =name;
        this.bought=bought;
    }

    public ItemOfList(String name, String id) {
        this.name = name;
        this.itemId = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getBought() {
        return bought;
    }

    public void setBought(Boolean bought) {
        this.bought = bought;
    }
}
