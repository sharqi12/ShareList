package com.example.sharelist;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
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
    private static final String TAG = "listActivity";
    SwipeMenuListView listView;
    SwipeMenuListView listView2;
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
    TextView listNameTextView;

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
                        if(!itemNamesList.contains(ds.getValue(ItemOfList.class).name))
                        itemNamesList.add(ds.getValue(ItemOfList.class).getName());
                    }else {
                        boughtitemListt.add(new ItemOfList(name, ds.getKey()));
                        if(!itemBoughtNamesList.contains(ds.getValue(ItemOfList.class).name))
                            itemBoughtNamesList.add(ds.getValue(ItemOfList.class).getName());
                    }
                }


                adapter.notifyDataSetChanged();
                adapter2.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        listNameTextView = view.findViewById(R.id.listName);
        DatabaseReference dbb = FirebaseDatabase.getInstance().getReference().child("AppList").child(listId);
        dbb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue() != null) {
                    usersOfList = snapshot.getValue(AppList.class).getUsers();
                    listNameTextView.setText(snapshot.getValue(AppList.class).getName());
                }
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

        //HERE SWIPE1
        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(250);
                // set a icon
                deleteItem.setIcon(getContext().getDrawable(R.drawable.ic_delete));
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };
        listView.setMenuCreator(creator);


        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        db.child(itemListt.get(position).itemId).removeValue();
                        itemNamesList.remove(position);
                        return true;

                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });



        listView2 = view.findViewById(R.id.selected_listView_bought_items);
        adapter2 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1,itemBoughtNamesList){
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position,convertView, parent);

                TextView tv = view.findViewById(android.R.id.text1);
                tv.setPaintFlags(tv.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                return view;
            }
        };
        listView2.setAdapter(adapter2);

        //HERE SWIPE2
        listView2.setMenuCreator(creator);
        listView2.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        db.child(boughtitemListt.get(position).itemId).removeValue();
                        itemBoughtNamesList.remove(position);
                        return true;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });


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
                itemNamesList.clear();
                itemBoughtNamesList.clear();
                for(ItemOfList item : itemListt){
                    if(item.bought) {
                        itemBoughtNamesList.add(item.name);
                    }else itemNamesList.add(item.name);
                }
            }
        });

        listView2.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(getActivity(), listttt.get(i), Toast.LENGTH_SHORT).show();
                String name = itemBoughtNamesList.get(i);
                String itemId = null;
                for(ItemOfList item : boughtitemListt){
                    if(item.name.equals(name)){
                        itemId = item.itemId;
                    }
                }
                db.child(itemId).child("bought").setValue(false);
                itemBoughtNamesList.remove(i);
                itemNamesList.clear();
                itemBoughtNamesList.clear();
                for(ItemOfList item : itemListt){
                    if(item.bought) {
                        itemBoughtNamesList.add(item.name);
                    }else itemNamesList.add(item.name);
                }

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
                if(name.length()>0 && name != null && !doesItemNameExistsInList(name))
                {
                    if(helper.save(s))
                    {
                        nameEditTxt.setText("");
//                        adapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,helper.retrieve());
//                        listView.setAdapter(adapter);

                    }
                }else if(name.length()<=0)
                {
                    Toast.makeText(getActivity(), "Name of item cannot be empty", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getActivity(), "item already exist on list", Toast.LENGTH_SHORT).show();
                }


            }
        });

        d.show();
    }

    private boolean doesItemNameExistsInList(String name){
        if(itemNamesList.contains(name) || itemBoughtNamesList.contains(name)) return true; else return false;
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
