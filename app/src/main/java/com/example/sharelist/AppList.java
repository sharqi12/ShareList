package com.example.sharelist;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AppList {
    String name;
    String listId;
    java.util.List<String> usersID;
    HashMap<String, ItemOfList> items;


    public AppList(String name, String listId) {
        this.name = name;
        this.listId = listId;
        items = new HashMap<String, ItemOfList>();
    }

    public AppList(String name) {
        this.name = name;
    }

    public AppList() {
        items = new HashMap<String, ItemOfList>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public java.util.List<String> getUsers() {
        return usersID;
    }

    public void setUsers(java.util.List<String> users) {
        this.usersID = users;
    }

//    public void addNewUserToList(String userId){
//        this.usersID.add(userId);
//    }


    public HashMap<String, ItemOfList> getItems() {
        return items;
    }

//    //public void setItems(List<ItemOfList> items) {
//        this.items = items;
//    }


    public void setItems(HashMap<String, ItemOfList> items) {
        this.items = items;
    }


    public String getListId() {
        return listId;
    }

    public void setListId(String listId) {
        this.listId = listId;
    }
}
