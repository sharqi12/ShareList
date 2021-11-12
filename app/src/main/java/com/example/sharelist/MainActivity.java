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
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    EditText emailId, password;
    Button btnLogin;
    TextView tvRegister;
    FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFirebaseAuth = FirebaseAuth.getInstance();
        emailId = findViewById(R.id.editTextTextPersonName);
        password = findViewById(R.id.editTextTextPassword);
        btnLogin = findViewById(R.id.button);
        tvRegister = findViewById(R.id.textView);

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser mFireBaseUser = mFirebaseAuth.getCurrentUser();
                if( mFireBaseUser != null){
                    Toast.makeText(MainActivity.this, "You are logged in!",Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(i);
                }
                else {
                    //Toast.makeText(MainActivity.this, "Please login",Toast.LENGTH_SHORT).show();
                }
            }
        };

        btnLogin.setOnClickListener(new View.OnClickListener() {
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
                    mFirebaseAuth.signInWithEmailAndPassword(email,pwd).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                Toast.makeText(MainActivity.this, "Login Error, try again",Toast.LENGTH_SHORT).show();
                            }else{
                                Intent intToHome = new Intent(MainActivity.this, HomeActivity.class);
                                startActivity(intToHome);
                            }

                        }
                    });
                }else {
                    Toast.makeText(MainActivity.this, "Error occurred!",Toast.LENGTH_SHORT).show();

                }

            }
        });
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intToRegister = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intToRegister);
            }
        });
    }

    @Override
    protected void onStart(){
        super.onStart();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

}