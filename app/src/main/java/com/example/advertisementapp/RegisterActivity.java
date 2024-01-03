package com.example.advertisementapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        EditText registerEmailTxt = findViewById(R.id.registerEmailTxt);
        EditText registerPasswordTxt = findViewById(R.id.registerPasswordTxt);
        EditText registerPasswordCnfTxt = findViewById(R.id.registerPasswordCnfTxt);
        Button registerBtn = findViewById(R.id.registerBtn);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = registerEmailTxt.getText().toString();
                String password = registerPasswordTxt.getText().toString();
                String passwordCnf = registerPasswordCnfTxt.getText().toString();

                if (!email.equals("") && !password.equals("") && password.equals(passwordCnf)) {
                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                registerEmailTxt.setText("");
                                registerPasswordTxt.setText("");
                                registerPasswordCnfTxt.setText("");
                                Toast.makeText(
                                        getApplicationContext(),
                                        "You have successfully created an account.",
                                        Toast.LENGTH_LONG
                                ).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(
                            getApplicationContext(),
                            "Input error",
                            Toast.LENGTH_LONG
                    ).show();
                }
            }
        });
    }
}
