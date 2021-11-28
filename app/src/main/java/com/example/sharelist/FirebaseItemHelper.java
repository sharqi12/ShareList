package com.example.sharelist;

import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;

public class FirebaseItemHelper {

    DatabaseReference db;
    Boolean saved=null;

    public FirebaseItemHelper(DatabaseReference db) {
        this.db = db;
    }

    //WRITE
    public Boolean save(ItemOfList item)
    {
        if(item==null)
        {
            saved=false;
        }else
        {
            try
            {
                db.push().setValue(item);
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