package com.learnexo.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity {

    private EditText mName;
    private EditText mEmail;
    private EditText mPass;
    private ProgressBar progressBar;

    Toolbar setupToolbar;

    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        wireViews();
        setupToolbar();
        setupFirebase();
    }

    public void registerBtnListener(View view) {
        final String name = mName.getText().toString();
        final String email = mEmail.getText().toString();
        final String pass = mPass.getText().toString();

        if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(pass)&&!email.contains(" ")) {
            createUserinFirestore(name, email, pass);
        }
    }

    private void createUserinFirestore(final String name, final String email, String pass) {
        mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                progressBar.setVisibility(View.VISIBLE);
                FirebaseUser user = mAuth.getCurrentUser();

                if(user != null) {
                    String user_id = user.getUid();

                    Map<String, String> userMap = new HashMap<>();
                    userMap.put("Name", name);
                    userMap.put("Email Id", email);

                    mFirestore.collection("Users").document(user_id).collection("Registration Details")
                            .document("Register Fields").set(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if(task.isSuccessful()) {
                                Intent intent = new Intent(RegistrationActivity.this, TabsActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                progressBar.setVisibility(View.INVISIBLE);
                                String errorMsg = task.getException().getMessage();
                                Toast.makeText(RegistrationActivity.this,
                                        "Firestore error : " + errorMsg,
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                } else {
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(RegistrationActivity.this,
                            "Email already in use.",
                            Toast.LENGTH_LONG).show();

                }
            }
        });
    }

    private void setupFirebase() {
        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
    }

    private void setupToolbar() {
        setupToolbar = findViewById(R.id.include);
        setSupportActionBar(setupToolbar);
        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle("Sign up");
        }
    }

    private void wireViews() {
        mName = findViewById(R.id.regName);
        mEmail = findViewById(R.id.regEmail);
        mPass = findViewById(R.id.regPassword);

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
    }



}