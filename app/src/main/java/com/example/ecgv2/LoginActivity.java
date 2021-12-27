package com.example.ecgv2;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import androidx.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText nPasswordField;
    private EditText nEmailField;
    String TAG = "LoginActivity";
    private FirebaseAuth.AuthStateListener nAuthListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Log.e(TAG,"lOGIN aCTIVITY STARTED");
// Initialize Firebase Auth

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        nEmailField = (EditText) findViewById(R.id.email);
        nPasswordField = (EditText) findViewById(R.id.password);
        nAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()!=null){
                    //startActivity(new Intent(LoginActivity.this,UserDetails.class));
                }
            }
        };
        Button lBttn = (Button) findViewById(R.id.LoginBttn);
        lBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSignIn();
            }
        });
        Button SignUpBtn = (Button) findViewById(R.id.signUpBtn);
        SignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StartSignup();
            }
        });
    }
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(nAuthListener);
    }
    private void startSignIn(){
        String email = nEmailField.getText().toString();
        String password = nPasswordField.getText().toString();
        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
            Toast.makeText(LoginActivity.this, "Fields are Empty", Toast.LENGTH_LONG).show();
        }
        else {
            mAuth.signInWithEmailAndPassword(email,
                    password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser()
                        String login_email = user.getEmail().toString();
                        Toast.makeText(LoginActivity.this, "Welcome: "+login_email,Toast.LENGTH_LONG).show();

                        startActivity(new Intent(LoginActivity.this,MainActivity.class));
                    }
                    else{
                        Log.e(TAG,"sIGN iN FAILED");
                        Toast.makeText(LoginActivity.this, "Sign In Failed",

                                Toast.LENGTH_LONG).show();

                    }
                }
            });
        }
    }
    private void StartSignup(){
        String email = nEmailField.getText().toString();
        String password = nPasswordField.getText().toString();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {@Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
// Sign in success, update UI with the signed-in user's information
                        Log.e(TAG, "createUserWithEmail:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        String login_email = user.getEmail().toString();
                        Toast.makeText(LoginActivity.this, "Welcome: "+login_email, Toast.LENGTH_LONG).show();
                    } else {
// If sign in fails, display a message to the user.
                        Log.e(TAG, "createUserWithEmail:failure", task.getException());
                        Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                    }
                }
                });
    }

}