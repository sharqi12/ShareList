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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ListsActivity extends Fragment {

    ListView listView;
    FloatingActionButton addNewListButton;
    ArrayAdapter<String> adapter;
    DatabaseReference db;
    FirebaseHelper helper;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.activity_lists, container , false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        db = FirebaseDatabase.getInstance().getReference();
        helper = new FirebaseHelper(db);
        listView = getView().findViewById(R.id.main_listView);
        addNewListButton = getView().findViewById(R.id.add_new_list_floatingButton);
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1,helper.retrieve());
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getActivity(), helper.retrieve().get(i), Toast.LENGTH_SHORT).show();
            }
        });

        addNewListButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                displayInputDialog();
            }
        });
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

                //VALIDATE
                if(name.length()>0 && name != null)
                {
                    if(helper.save(s))
                    {
                        nameEditTxt.setText("");
                        adapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,helper.retrieve());
                        listView.setAdapter(adapter);

                    }
                }else
                {
                    Toast.makeText(getActivity(), "Name cannot be empty", Toast.LENGTH_SHORT).show();
                }


            }
        });

        d.show();
    }

}
