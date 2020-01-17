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

public class SignUp extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private EditText confirmPassword;
    private Button signUp;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        email = (EditText)findViewById(R.id.Email);
        password = (EditText)findViewById(R.id.Password);
        confirmPassword = (EditText)findViewById(R.id.ConfirmPassword);
        signUp = (Button)findViewById(R.id.loginButtonAdmin);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String pEmail = email.getText().toString();
                String pPassword = password.getText().toString();
                String pConfirmPassword = confirmPassword.getText().toString();

                if (pEmail.isEmpty()) {
                    Toast.makeText(SignUp.this, "Please Enter Email.", Toast.LENGTH_LONG).show();
                    return;
                } else if (!Patterns.EMAIL_ADDRESS.matcher(pEmail).matches()) {
                    Toast.makeText(SignUp.this, "Please Enter Valid Email.", Toast.LENGTH_LONG).show();
                    return;
                }
                if (pPassword.isEmpty()) {
                    Toast.makeText(SignUp.this, "Please Enter Password.", Toast.LENGTH_LONG).show();
                    return;
                }
                if (pConfirmPassword.isEmpty()) {
                    Toast.makeText(SignUp.this, "Please Confirm Password.", Toast.LENGTH_LONG).show();
                    return;
                }
                if (!pPassword.equals(pConfirmPassword)) {
                    Toast.makeText(SignUp.this, "Password and Confirmed Password don't match.", Toast.LENGTH_LONG).show();
                    return;
                }
                signupUser(pEmail, pPassword);

            }
        });
    }

    private void signupUser(final String email, final String password) {

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Toast.makeText(SignUp.this, "Sign Up Successful.", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(SignUp.this, Main2Activity.class));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SignUp.this, "Sign Up Unsuccessful due to " + e, Toast.LENGTH_LONG).show();

                    }
                });
    }

}
