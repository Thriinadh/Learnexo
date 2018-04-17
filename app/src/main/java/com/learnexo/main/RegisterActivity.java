package com.learnexo.main;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private EditText mNameField;
    private EditText mEmailField;
    private EditText mPassField;
    private ProgressBar progressBar;

    private android.support.v7.widget.Toolbar setupToolbar;

    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mNameField = findViewById(R.id.regName);
        mEmailField = findViewById(R.id.regEmail);
        mPassField = findViewById(R.id.regPassword);

        progressBar = findViewById(R.id.progressBar);

        setupToolbar = findViewById(R.id.include);
        setSupportActionBar(setupToolbar);
        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle("Sign up");
        }

        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        progressBar.setVisibility(View.INVISIBLE);
    }

        public void registerButtonClicked(View view) {

        final String name_field = mNameField.getText().toString();
        final String email_field = mEmailField.getText().toString();
        String pass_field = mPassField.getText().toString();

        if(!TextUtils.isEmpty(name_field) && !TextUtils.isEmpty(email_field) && !TextUtils.isEmpty(pass_field)) {
            mAuth.createUserWithEmailAndPassword(email_field, pass_field).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    progressBar.setVisibility(View.VISIBLE);

                    FirebaseUser user = mAuth.getCurrentUser();
                    if(user != null) {
                        String user_id = user.getUid();

                        Map<String, String> userMap = new HashMap<>();
                        userMap.put("Name", name_field);
                        userMap.put("Email Id", email_field);

                        firebaseFirestore.collection("Users").document(user_id).collection("Registration Details")
                                .document("Register Fields").set(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if(task.isSuccessful()) {

                                    Intent interestsIntent = new Intent(RegisterActivity.this, TabsActivity.class);
                                    startActivity(interestsIntent);
                                    finish();

                                } else {

                                    progressBar.setVisibility(View.INVISIBLE);
                                    String error = task.getException().getMessage();
                                    Toast.makeText(RegisterActivity.this, "Firestore error : " + error,
                                            Toast.LENGTH_LONG).show();

                                }

                            }
                        });


                    } else {

                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(RegisterActivity.this, "Email already exists", Toast.LENGTH_LONG).show();

                    }
                }
            });
        }

    }

}