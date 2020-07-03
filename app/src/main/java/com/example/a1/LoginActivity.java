package com.example.a1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private EditText email;
    private EditText password;
    private Button myButton;
    private FirebaseAuth myAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.myEditTextEmailLogIn);
        password = findViewById(R.id.myEditTextPasswordLogIn);
        myButton = findViewById(R.id.myButtonLogInLogIn);
        myAuth = FirebaseAuth.getInstance();

        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txtEmail = email.getText().toString();
                String txtPasswor = password.getText().toString();

                if(TextUtils.isEmpty(txtEmail) || TextUtils.isEmpty(txtPasswor)){
                    Toast.makeText(LoginActivity.this, "Empty Input", Toast.LENGTH_SHORT).show();
                }else if(txtPasswor.length() < 6){
                    Toast.makeText(LoginActivity.this, "Password too short", Toast.LENGTH_SHORT).show();
                }else{
                    Log.d("tag", "Valid Input");
                    logIn(txtEmail, txtPasswor);

                }
            }
        });

    }

    private void logIn(String txtEmail, String txtPasswor) {
        Log.d("tag", "in logIn() function");
        myAuth.signInWithEmailAndPassword(txtEmail, txtPasswor).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Log.d("tag", "Log in Successful. Going to main Screen");
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: user not registered");
                Toast.makeText(LoginActivity.this, "User Need To Register First", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
