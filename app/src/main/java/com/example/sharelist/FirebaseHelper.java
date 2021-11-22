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
}