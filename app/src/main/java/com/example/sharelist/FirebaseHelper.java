package com.example.sharelist;

import android.widget.ArrayAdapter;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;


public class FirebaseHelper {

    DatabaseReference db;
    Boolean saved=null;
    ArrayList<String> appListsNames =new ArrayList<>();


    public FirebaseHelper(DatabaseReference db) {
        this.db = db;
    }

    //WRITE
    public Boolean save(AppList appList)
    {
        if(appList==null)
        {
            saved=false;
        }else
        {
            try
            {
                db.push().setValue(appList);
                saved=true;

            }catch (DatabaseException e)
            {
                e.printStackTrace();
                saved=false;
            }
        }

        return saved;
    }

    //READ
    public ArrayList<String> retrieve()
    {
        db.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                fetchData(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                fetchData(dataSnapshot);

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return appListsNames;
    }

    private void fetchData(DataSnapshot dataSnapshot)
    {
        appListsNames.clear();

        for (DataSnapshot ds : dataSnapshot.getChildren())
        {
            String name=ds.getValue(AppList.class).getName();
            appListsNames.add(name);
        }
    }
}