package com.example.sharelist;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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

        changePasswordTV = view.findViewById(R.id.user_profile_change_password);
        changePasswordTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayInputDialog();
            }
        });


        return view;
    }

    private void displayInputDialog()
    {
        Dialog d=new Dialog(getActivity());
        d.setTitle("Save To Firebase");
        d.setContentView(R.layout.dialog_change_password);

        final EditText currentPasswordEditTxt= (EditText) d.findViewById(R.id.current_password_EditText);
        final EditText newPasswordEditTxt= (EditText) d.findViewById(R.id.new_password_EditText);
        Button saveBtn= (Button) d.findViewById(R.id.saveNewPasswordButton);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                final String email = user.getEmail();
                String currentPassword = currentPasswordEditTxt.getText().toString();
                String newPassword = newPasswordEditTxt.getText().toString();
                AuthCredential credential = EmailAuthProvider.getCredential(email,currentPassword);

                user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            user.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(getActivity(), "Password Successfully Modified", Toast.LENGTH_SHORT).show();
                                        d.dismiss();
                                    }else {
                                        if(newPassword.length() <6 ){
                                            Toast.makeText(getActivity(), "New password should be at least 6 characters", Toast.LENGTH_SHORT).show();
                                        }
                                        else
                                        Toast.makeText(getActivity(), "Something went wrong. Please try again later", Toast.LENGTH_SHORT).show();
                                        d.dismiss();
                                    }

                                }
                            });
                        }else {
                            Toast.makeText(getActivity(), "Authenticatation failed", Toast.LENGTH_SHORT).show();
                            d.dismiss();
                        }
                    }
                });


//                    Toast.makeText(getActivity(), "Name cannot be empty", Toast.LENGTH_SHORT).show();


            }
        });

        d.show();
    }

}


