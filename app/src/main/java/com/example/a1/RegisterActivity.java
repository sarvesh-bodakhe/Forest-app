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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private Button register;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        email = findViewById(R.id.myTextViewEmail);
        password = findViewById(R.id.myTextViewPassword);
        register = findViewById(R.id.myButtonRegister);
        mAuth = FirebaseAuth.getInstance();
        register.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 String txtEmail = email.getText().toString();
                 String txtPasswor = password.getText().toString();

                 if(TextUtils.isEmpty(txtEmail) || TextUtils.isEmpty(txtPasswor)){
                     Toast.makeText(RegisterActivity.this, "Empty Input", Toast.LENGTH_SHORT).show();
                 }else if(txtPasswor.length() < 6){
                     Toast.makeText(RegisterActivity.this, "Password too short", Toast.LENGTH_SHORT).show();
                 }else{
                     registerUser(txtEmail, txtPasswor);
                 }
             }
         });
    }

    private void registerUser(String txtEmail, String txtPasswor) {
        Log.d("tag", "in registerUser()" + txtEmail + txtPasswor);
        mAuth.createUserWithEmailAndPassword(txtEmail, txtPasswor)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            Log.d("tag", "User Created");
                            startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                            finish();
                        }else{
                            Log.d("tag", "User Failed");
                        }
                    }
                });
        Log.d("tag", "Done");
    }

}
