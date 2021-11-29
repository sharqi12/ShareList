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

public class ListsActivity extends Fragment {

    ListView listView;
    FloatingActionButton addNewListButton;
    ArrayAdapter<String> adapter;
    DatabaseReference db;
    FirebaseHelper helper;
    List<AppList> listttt= new ArrayList<>();
    List<String> listsNames = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean dialog = getArguments().getBoolean("dialog");
        if(dialog){
            displayInputDialog();
        }
        String userId = (String)FirebaseAuth.getInstance().getCurrentUser().getEmail();
        View view = inflater.inflate(R.layout.activity_lists, container , false);
        db = FirebaseDatabase.getInstance().getReference("AppList");
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listttt.clear();
                listsNames.clear();
                for (DataSnapshot ds : snapshot.getChildren())
                {

                    //po0AppList appList = ds.getValue(AppList.class);
                    if(ds.getValue(AppList.class).getUsers().contains(userId)){
                        String name=ds.getValue(AppList.class).getName();
                        String listId = ds.getKey();
                        listttt.add(new AppList(name, listId));
                    }
                }

                for (AppList lists : listttt){
                    listsNames.add(lists.getName());
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
        helper = new FirebaseHelper(db);
        listView = view.findViewById(R.id.main_listView);
        addNewListButton = view.findViewById(R.id.add_new_list_floatingButton);

        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1,listsNames);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(getActivity(), listttt.get(i), Toast.LENGTH_SHORT).show();
                Bundle bundle = new Bundle();
                ListActivity listActivity = new ListActivity();




                bundle.putString("listId",listttt.get(i).getListId());
                listActivity.setArguments(bundle);
                getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        listActivity).commit();

            }
        });

        addNewListButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                displayInputDialog();
            }
        });
        return view;
        //return inflater.inflate(R.layout.activity_lists, container , false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){


    }




    private void displayInputDialog()
    {
        Dialog d=new Dialog(getActivity());
        d.setTitle("Save To Firebase");
        d.setContentView(R.layout.input_dialog);

        final EditText nameEditTxt= (EditText) d.findViewById(R.id.createListNameEditText);
        Button saveBtn= (Button) d.findViewById(R.id.saveNewListButton);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //GET DATA
                String name=nameEditTxt.getText().toString();

                //SET DATA
                AppList s=new AppList();
                s.setName(name);
                s.setUsers(addFirstUserIDToList(FirebaseAuth.getInstance().getCurrentUser().getEmail()));
                //s.addNewUserToList(FirebaseAuth.getInstance().getCurrentUser().getUid());

                //VALIDATE
                if(name.length()>0 && name != null)
                {
                    if(helper.save(s))
                    {
                        nameEditTxt.setText("");
//                        adapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,helper.retrieve());
//                        listView.setAdapter(adapter);
                        d.dismiss();

                    }
                }else
                {
                    Toast.makeText(getActivity(), "Name cannot be empty", Toast.LENGTH_SHORT).show();
                }


            }
        });

        d.show();
    }

    public List<String> addFirstUserIDToList(String userId){
        List<String> templist = new ArrayList<String>();
        templist.add(userId);
        return templist;
    }

}
