package com.twwm.trafficviolationreport;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class AdminLogin extends AppCompatActivity {

    private EditText code;
    private Button Btn;

    private String stringCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        code = findViewById(R.id.Pass);
        Btn = findViewById(R.id.loginButton);

        Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("Admin").document("Secret").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                            stringCode = documentSnapshot.getString("Code");

                            if (code.getText().toString().equals(stringCode))
                            {
                                Toast.makeText(AdminLogin.this, "Correct Code.\nAccess Granted.", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(AdminLogin.this, AdminSearch.class));
                            }

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            Toast.makeText(AdminLogin.this, e.toString(), Toast.LENGTH_LONG).show();

                        }
                    });
            }
        });

    }
}
