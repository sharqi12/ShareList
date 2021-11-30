package com.example.sharelist;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;

public class ProfileActivity extends Fragment {

    TextView userEmail, changePasswordTV, userListsTV;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = inflater.inflate(R.layout.activity_profile, container , false);
        userEmail = view.findViewById(R.id.user_profile_name);
        userEmail.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());

        userListsTV = view.findViewById(R.id.user_profile_lists);
        userListsTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListsActivity listsActivity = new ListsActivity();
                Bundle bundle = new Bundle();
                bundle.putBoolean("dialog",false);
                listsActivity.setArguments(bundle);
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, listsActivity).commit();
            }
        });



        return view;
    }
}
