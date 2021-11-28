package com.example.sharelist;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends Fragment {
    ListView listView;
    FloatingActionButton addNewItemButton;
    ArrayAdapter<String> adapter;
    DatabaseReference db;
    FirebaseItemHelper helper;
    List<String> itemListt= new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = inflater.inflate(R.layout.activity_list, container , false);
        String listId = getArguments().getString("listId");
        db = FirebaseDatabase.getInstance().getReference().child("AppList").child(listId).child("items");

        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                itemListt.clear();
                for (DataSnapshot ds : snapshot.getChildren())
                {
                    String name=ds.getValue(ItemOfList.class).getName();
                    itemListt.add(name);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });

        helper = new FirebaseItemHelper(db);

        listView = view.findViewById(R.id.selected_listView);
        addNewItemButton = view.findViewById(R.id.add_new_item_floatingButton);
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1,itemListt);
        listView.setAdapter(adapter);


        addNewItemButton = view.findViewById(R.id.add_new_item_floatingButton);
        addNewItemButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                displayInputDialog();
            }
        });


        return view;
        //return super.onCreateView(inflater, container, savedInstanceState);

    }


    private void displayInputDialog()
    {
        Dialog d=new Dialog(getActivity());
        d.setTitle("Save To Firebase");
        d.setContentView(R.layout.input_dialog_item);

        final EditText nameEditTxt= (EditText) d.findViewById(R.id.createItemNameEditText);
        Button saveBtn= (Button) d.findViewById(R.id.saveNewItemButton);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //GET DATA
                String name=nameEditTxt.getText().toString();

                //SET DATA
                ItemOfList s=new ItemOfList();
                s.setName(name);

                //VALIDATE
                if(name.length()>0 && name != null)
                {
                    if(helper.save(s))
                    {
                        nameEditTxt.setText("");
//                        adapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,helper.retrieve());
//                        listView.setAdapter(adapter);

                    }
                }else
                {
                    Toast.makeText(getActivity(), "Name of item cannot be empty", Toast.LENGTH_SHORT).show();
                }


            }
        });

        d.show();
    }




}
