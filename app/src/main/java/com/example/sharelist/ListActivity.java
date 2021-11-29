package com.example.sharelist;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
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
    ListView listView2;
    FloatingActionButton addNewItemButton;
    FloatingActionButton shareListButton;
    ArrayAdapter<String> adapter;
    ArrayAdapter<String> adapter2;
    DatabaseReference db;
    FirebaseItemHelper helper;
    List<ItemOfList> boughtitemListt= new ArrayList<>();
    List<ItemOfList> itemListt= new ArrayList<>();
    List<String> itemNamesList = new ArrayList<>();
    List<String> itemBoughtNamesList = new ArrayList<>();
    List<String> usersOfList = new ArrayList<>();

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
                itemNamesList.clear();
                boughtitemListt.clear();
                for (DataSnapshot ds : snapshot.getChildren())
                {
                    String name=ds.getValue(ItemOfList.class).getName();
                    if(!ds.getValue(ItemOfList.class).bought){
                        itemListt.add(new ItemOfList(name, ds.getKey()));
                    }else {
                        boughtitemListt.add(new ItemOfList(name));
                    }
                }

                for (ItemOfList list : itemListt){
                    itemNamesList.add(list.getName());
                }
                for (ItemOfList list : boughtitemListt){
                    itemBoughtNamesList.add(list.getName());
                }

                adapter.notifyDataSetChanged();
                adapter2.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        DatabaseReference dbb = FirebaseDatabase.getInstance().getReference().child("AppList").child(listId);
        dbb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usersOfList = snapshot.getValue(AppList.class).getUsers();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        helper = new FirebaseItemHelper(db);

        listView = view.findViewById(R.id.selected_listView);
        addNewItemButton = view.findViewById(R.id.add_new_item_floatingButton);

        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1,itemNamesList);
        listView.setAdapter(adapter);

        listView2 = view.findViewById(R.id.selected_listView_bought_items);
        adapter2 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1,itemBoughtNamesList);
        listView2.setAdapter(adapter2);


        addNewItemButton = view.findViewById(R.id.add_new_item_floatingButton);
        addNewItemButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                displayInputDialog();
            }
        });

        shareListButton = view.findViewById(R.id.add_new_user_to_list_floatingButton);
        shareListButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                displayInputDialogShareList(listId);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(getActivity(), listttt.get(i), Toast.LENGTH_SHORT).show();
                String name = itemNamesList.get(i);
                String itemId = null;
                for(ItemOfList item : itemListt){
                    if(item.name.equals(name)){
                        itemId = item.itemId;
                    }
                }
                db.child(itemId).child("bought").setValue(true);
                //adapter2.notifyDataSetChanged();
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


    private void displayInputDialogShareList(String listId)
    {
        Dialog d=new Dialog(getActivity());
        d.setTitle("Save To Firebase");
        d.setContentView(R.layout.input_dialog_user);

        Dialog successDialog = new Dialog(getActivity());
        successDialog.setContentView(R.layout.dialog_success);
        TextView successMessage = successDialog.findViewById(R.id.successMessageTextView);

        final EditText nameEditTxt= (EditText) d.findViewById(R.id.userNameEditText);
        Button saveBtn= (Button) d.findViewById(R.id.shareListWithUserButton);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //GET DATA
                String userEmail=nameEditTxt.getText().toString();

                //SET DATA



                //VALIDATE
                if(userEmail.length()>0 && userEmail != null)
                {
                    usersOfList.add(userEmail);
                    db.getParent().child("users").setValue(usersOfList);
                    d.dismiss();
                    successMessage.setText(userEmail + " sucessfully added to list! ");
                    successDialog.show();
                }else
                {
                    Toast.makeText(getActivity(), "Email cannot be empty", Toast.LENGTH_SHORT).show();
                }


            }
        });

        d.show();
    }


}
