package com.example.sharelist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    EditText emailId, password;
    Button btnRegister;
    TextView tvlogin;
    FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mFirebaseAuth = FirebaseAuth.getInstance();
        emailId = findViewById(R.id.editTextTextPersonName);
        password = findViewById(R.id.editTextTextPassword);
        btnRegister = findViewById(R.id.button);
        tvlogin = findViewById(R.id.textView);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailId.getText().toString();
                String pwd = password.getText().toString();
                if(email.isEmpty()){
                    emailId.setError("Insert email here");
                    emailId.requestFocus();
                }
                else if(pwd.isEmpty()){
                    password.setError("Insert password here");
                    password.requestFocus();
                }
                else if(!(pwd.isEmpty() && email.isEmpty())){
                    mFirebaseAuth.createUserWithEmailAndPassword(email,pwd).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(RegisterActivity.this, "Register unsuccessful, try again",Toast.LENGTH_SHORT).show();
                            }else {
                                startActivity(new Intent(RegisterActivity.this,RegisterActivity.class));
                            }
                        }
                    });
                }else {
                    Toast.makeText(RegisterActivity.this, "Error occurred!",Toast.LENGTH_SHORT).show();
                }

            }
        });

        tvlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(i);
            }
        });
    }
}