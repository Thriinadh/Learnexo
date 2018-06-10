package com.learnexo.main;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.learnexo.util.FirebaseUtil;

import java.util.HashMap;
import java.util.Map;

public class EmpDetailsActivity extends AppCompatActivity {

    private EditText position;
    private EditText company;

    private FirebaseUtil mFirebaseUtil=new FirebaseUtil();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emp_details);

        position = findViewById(R.id.position);
        company = findViewById(R.id.company);

        setupToolbar();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edu_submit, menu);
        View view = findViewById(R.id.edu_submit);
        if (view != null && view instanceof TextView) {
            ((TextView) view).setTextColor(Color.RED);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.edu_submit) {
            String pos = position.getText().toString().trim();
            String com = company.getText().toString().trim();

            Map<String, Object> map = new HashMap<>();
            if(!TextUtils.isEmpty(pos))
                map.put("position", pos);
            if(!TextUtils.isEmpty(com))
                map.put("company", com);

            if(!TextUtils.isEmpty(pos) || !TextUtils.isEmpty(com))
                mFirebaseUtil.mFirestore.collection("users").document(FirebaseUtil.getCurrentUserId()).collection("details").document("empDetails").set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Intent intent = new Intent(EmpDetailsActivity.this, TabsActivity.class);
                        intent.putExtra("SAVE_PROFILE_FROM_PROFILE", true);
                        startActivity(intent);
                    }
                });

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupToolbar() {
        Toolbar setupToolbar = findViewById(R.id.include);
        setSupportActionBar(setupToolbar);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setDisplayShowHomeEnabled(true);
            supportActionBar.setTitle("Employment details");
        }
    }

}
