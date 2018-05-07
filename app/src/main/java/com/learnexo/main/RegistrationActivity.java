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
import com.google.firebase.auth.FirebaseUser;
import com.learnexo.util.FirebaseUtil;

import java.util.HashMap;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity {

    private EditText mFirstName;
    private EditText mLastName;
    private EditText mEmail;
    private EditText mPass;
    private ProgressBar progressBar;

    Toolbar setupToolbar;

   FirebaseUtil mFirebaseUtil=new FirebaseUtil();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        wireViews();
        setupToolbar();
    }

    public void registerBtnListener(View view) {
        final String first_name = mFirstName.getText().toString();
        final String last_name = mLastName.getText().toString();
        final String email = mEmail.getText().toString();
        final String pass = mPass.getText().toString();

        if((!TextUtils.isEmpty(first_name) || !TextUtils.isEmpty(last_name)) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(pass)&&!email.contains(" ")) {
            createUserinFirestore(first_name, last_name, email, pass);
        }
    }

    private void createUserinFirestore(final String first_name, final String last_name, final String email, String pass) {
        FirebaseUtil.sAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                progressBar.setVisibility(View.VISIBLE);
                FirebaseUser user = mFirebaseUtil.getCurrentUser();

                if(user != null) {
                    String user_id = user.getUid();

                    Map<String, String> userMap = new HashMap<>();
                    userMap.put("firstName", first_name);
                    userMap.put("lastName", last_name);
                    userMap.put("emailId", email);

                    mFirebaseUtil.mFirestore.collection("users").document(user_id).collection("reg_details")
                            .document("doc").set(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
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

    private void setupToolbar() {
        setupToolbar = findViewById(R.id.include);
        setSupportActionBar(setupToolbar);
        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle("Sign up");
        }
    }

    private void wireViews() {
        mFirstName = findViewById(R.id.regFirstName);
        mLastName = findViewById(R.id.regLastName);
        mEmail = findViewById(R.id.regEmail);
        mPass = findViewById(R.id.regPassword);

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
    }

}