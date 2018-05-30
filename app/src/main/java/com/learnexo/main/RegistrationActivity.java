package com.learnexo.main;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
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

    private ConstraintLayout mConstraintLayout;
    private Snackbar snackbar;

    Toolbar setupToolbar;

    private ScrollView scrollView;

   FirebaseUtil mFirebaseUtil=new FirebaseUtil();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        wireViews();

        setupToolbar();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void registerBtnListener(View view) {
        final String first_name = mFirstName.getText().toString().trim();
        final String last_name = mLastName.getText().toString().trim();
        final String email = mEmail.getText().toString().trim();
        final String pass = mPass.getText().toString();

        if((!TextUtils.isEmpty(first_name) || !TextUtils.isEmpty(last_name)) && !TextUtils.isEmpty(email) && !email.contains(" ") && !TextUtils.isEmpty(pass)) {
            progressBar.setVisibility(View.VISIBLE);
            createUserinFirestore(first_name, last_name, email, pass);
        }
        else if((!TextUtils.isEmpty(first_name) || !TextUtils.isEmpty(last_name)) && email.contains(" ") && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(pass))
        {
           // Toast.makeText(RegistrationActivity.this, "Give proper email address", Toast.LENGTH_SHORT).show();

            AlertDialog.Builder builder = new AlertDialog.Builder(RegistrationActivity.this);

            builder.setMessage("Entered email must not contain spaces");
            builder.setPositiveButton("OKAY", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    snackbar = Snackbar.make(mConstraintLayout, "No spaces in email", Snackbar.LENGTH_SHORT);
                    View sbView = snackbar.getView();
                    TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(Color.YELLOW);
                    snackbar.show();
                }
            });
            builder.show();

        }
        else if((TextUtils.isEmpty(first_name) || TextUtils.isEmpty(last_name)) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(pass))
        {
           // Toast.makeText(RegistrationActivity.this, "Enter your name", Toast.LENGTH_SHORT).show();

            AlertDialog.Builder builder = new AlertDialog.Builder(RegistrationActivity.this);

            builder.setMessage("Enter your First or Last name");
            builder.setPositiveButton("OKAY", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    snackbar = Snackbar.make(mConstraintLayout, "Give your name", Snackbar.LENGTH_SHORT);
                    View sbView = snackbar.getView();
                    TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(Color.YELLOW);
                    snackbar.show();
                }
            });
            builder.show();

        }
        else if((!TextUtils.isEmpty(first_name) || !TextUtils.isEmpty(last_name)) && TextUtils.isEmpty(email) && !TextUtils.isEmpty(pass)) {
            //Toast.makeText(RegistrationActivity.this, "Enter your email address", Toast.LENGTH_SHORT).show();

            AlertDialog.Builder builder = new AlertDialog.Builder(RegistrationActivity.this);

            builder.setMessage("Enter your email address");
            builder.setPositiveButton("OKAY", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    snackbar = Snackbar.make(mConstraintLayout, "Give your email", Snackbar.LENGTH_SHORT);
                    View sbView = snackbar.getView();
                    TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(Color.YELLOW);
                    snackbar.show();
                }
            });
            builder.show();

        }
        else if((!TextUtils.isEmpty(first_name) || !TextUtils.isEmpty(last_name)) && !TextUtils.isEmpty(email) && TextUtils.isEmpty(pass)) {
           // Toast.makeText(RegistrationActivity.this, "Enter password", Toast.LENGTH_SHORT).show();

            AlertDialog.Builder builder = new AlertDialog.Builder(RegistrationActivity.this);

            builder.setMessage("Set password to register");
            builder.setPositiveButton("OKAY", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    snackbar = Snackbar.make(mConstraintLayout, "Give any password", Snackbar.LENGTH_SHORT);
                    View sbView = snackbar.getView();
                    TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(Color.YELLOW);
                    snackbar.show();
                }
            });
            builder.show();

        }
        else {
           // Toast.makeText(RegistrationActivity.this, "Fields can't be empty", Toast.LENGTH_SHORT).show();

            AlertDialog.Builder builder = new AlertDialog.Builder(RegistrationActivity.this);

            builder.setMessage("Must give Email and Password");
            builder.setPositiveButton("OKAY", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    snackbar = Snackbar.make(mConstraintLayout, "You can't skip them", Snackbar.LENGTH_SHORT);
                    View sbView = snackbar.getView();
                    TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(Color.YELLOW);
                    snackbar.show();
                }
            });
            builder.show();

        }
    }

    private void createUserinFirestore(final String first_name, final String last_name, final String email, String pass) {
        FirebaseUtil.sAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

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
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Sign up");
        }
    }

    private void wireViews() {
        scrollView = findViewById(R.id.scrollView);
        mFirstName = findViewById(R.id.regFirstName);
        mLastName = findViewById(R.id.regLastName);
        mEmail = findViewById(R.id.regEmail);
        mPass = findViewById(R.id.regPassword);

        mConstraintLayout = findViewById(R.id.constraintLayout);

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
    }

}