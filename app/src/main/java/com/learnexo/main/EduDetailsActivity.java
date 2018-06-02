package com.learnexo.main;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.learnexo.util.FirebaseUtil;

import java.util.HashMap;
import java.util.Map;

public class EduDetailsActivity extends AppCompatActivity {

    private Spinner yearPicker;
    private String tag;
    private EditText studiedAt;
    private EditText firstConcentration;
    private EditText secondConcentration;
    private EditText degreeType;

    private FirebaseUtil mFirebaseUtil=new FirebaseUtil();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edu_details);

        yearPicker = findViewById(R.id.spinnerYearPicker);
        studiedAt = findViewById(R.id.studiedAt);
        firstConcentration = findViewById(R.id.firstConcentration);
        secondConcentration = findViewById(R.id.secondConcentration);
        degreeType = findViewById(R.id.degreeType);

        setupToolbar();

        setupDropDownSpinner();
        spinnerOnselect();



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
            String studied = studiedAt.getText().toString().trim();
            String fstCon = firstConcentration.getText().toString().trim();
            String secCon = secondConcentration.getText().toString().trim();
            String degreeTypee = degreeType.getText().toString().trim();

            Map<String, Object> map = new HashMap<>();
            map.put("studiedAt", studied);
            map.put("firstCon", fstCon);
            map.put("secondCon", secCon);
            map.put("endYear", tag);
            map.put("degreeType", degreeTypee);

            if(!TextUtils.isEmpty(studied) || !TextUtils.isEmpty(fstCon) || !TextUtils.isEmpty(secCon) || !TextUtils.isEmpty(degreeTypee))
            mFirebaseUtil.mFirestore.collection("users").document(FirebaseUtil.getCurrentUserId()).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Intent intent = new Intent(EduDetailsActivity.this, TabsActivity.class);
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
            supportActionBar.setTitle("Education details");
        }
    }

    private void setupDropDownSpinner() {

        Integer years[]= new Integer[126];
        int year=2025;
        for(int i=0;i<126;i++){
            years[i]=year;
            year--;
        }

      //  String subjects[] = {"Department0 Java 0","Relational Database", "Java","Mongo DB","Scala","Python","Ruby", "C sharp","Android", "others"};

        ArrayAdapter<Integer> staticAdapter = new ArrayAdapter<>
                (this, android.R.layout.simple_spinner_item,
                        years);

        // Specify the layout to use when the list of choices appears
        staticAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearPicker.setPrompt("Select year");

        // Apply the adapter to the spinner
        yearPicker.setAdapter(new NothingSelectedSpinnerAdapter(staticAdapter,
                R.layout.contact_spinner_row_nothing_selected,
                // R.layout.contact_spinner_nothing_selected_dropdown, // Optional
                this));

    }

    private void spinnerOnselect() {
        yearPicker.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                Object object = adapterView.getItemAtPosition(position);
                if(object != null)
                    tag = object.toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //...
            }
        });
    }

}
