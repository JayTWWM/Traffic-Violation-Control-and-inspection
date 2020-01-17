package com.twwm.trafficviolationreport;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LogedIn extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private Button LogIn;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loged_in);

        email = (EditText)findViewById(R.id.Email1);
        password = (EditText)findViewById(R.id.Password1);
        LogIn = (Button)findViewById(R.id.loginButtonAdmin1);

        LogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String pEmail = email.getText().toString();
                String pPassword = password.getText().toString();

                if (pEmail.isEmpty()) {
                    Toast.makeText(LogedIn.this, "Please Enter Email.", Toast.LENGTH_LONG).show();
                    return;
                } else if (!Patterns.EMAIL_ADDRESS.matcher(pEmail).matches()) {
                    Toast.makeText(LogedIn.this, "Please Enter Valid Email.", Toast.LENGTH_LONG).show();
                    return;
                }
                if (pPassword.isEmpty()) {
                    Toast.makeText(LogedIn.this, "Please Enter Password.", Toast.LENGTH_LONG).show();
                    return;
                }
                signinUser(pEmail, pPassword);

            }
        });
    }

    private void signinUser(final String email, final String password) {

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Toast.makeText(LogedIn.this, "Log In Successful.", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(LogedIn.this, Main2Activity.class));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(LogedIn.this, "Log In Unsuccessful due to " + e, Toast.LENGTH_LONG).show();

                    }
                });
    }
}
