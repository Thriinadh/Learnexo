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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.learnexo.util.FirebaseUtil;

import java.util.HashMap;
import java.util.Map;

public class LocationDetailsActivity extends AppCompatActivity {

    private EditText location;
    private Spinner spinnerStartYearPicker;
    private Spinner spinnerEndYearPicker;
    private CheckBox currentLocationCheck;
    private int startYear;
    private int endYear;

    private FirebaseUtil mFirebaseUtil=new FirebaseUtil();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_details);

        location = findViewById(R.id.location);
        spinnerStartYearPicker = findViewById(R.id.spinnerStartYearPicker);
        spinnerEndYearPicker = findViewById(R.id.spinnerEndYearPicker);
        currentLocationCheck = findViewById(R.id.currentLocationCheck);

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
            String locationn = location.getText().toString().trim();

            Map<String, Object> map = new HashMap<>();
            if(!TextUtils.isEmpty(locationn))
                map.put("location", locationn);
            if(startYear != 0)
                map.put("startYear", startYear);
            if(endYear != 0)
                map.put("endYear", endYear);
            if(currentLocationCheck.isChecked())
                map.put("currentStatus", currentLocationCheck.isChecked());

            if(!TextUtils.isEmpty(locationn) && startYear != 0 && endYear != 0 && startYear > endYear)
                Toast.makeText(LocationDetailsActivity.this, "start year can't be greater", Toast.LENGTH_SHORT).show();
            else if(!TextUtils.isEmpty(locationn)) {
                mFirebaseUtil.mFirestore.collection("users").document(FirebaseUtil.getCurrentUserId()).collection("details").document("locationDetails").set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Intent intent = new Intent(LocationDetailsActivity.this, TabsActivity.class);
                        intent.putExtra("SAVE_PROFILE_FROM_PROFILE", true);
                        startActivity(intent);
                    }
                });
            } else
                Toast.makeText(LocationDetailsActivity.this, "Location Can't be empty", Toast.LENGTH_SHORT).show();

            return true;
        }

        return super.onOptionsItemSelected(item);
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
        spinnerStartYearPicker.setPrompt("Select year");

        // Apply the adapter to the spinner
        spinnerStartYearPicker.setAdapter(new NothingSelectedSpinnerAdapter(staticAdapter,
                R.layout.contact_spinner_row_nothing_selected,
                // R.layout.contact_spinner_nothing_selected_dropdown, // Optional
                this));

        spinnerEndYearPicker.setAdapter(new NothingSelectedSpinnerAdapter(staticAdapter,
                R.layout.contact_spinner_row_nothing_selected,
                // R.layout.contact_spinner_nothing_selected_dropdown, // Optional
                this));

    }

    private void spinnerOnselect() {
        spinnerStartYearPicker.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                Object object = adapterView.getItemAtPosition(position);
                if(object != null)
                    startYear = Integer.parseInt(object.toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //...
            }
        });

        spinnerEndYearPicker.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                Object object = adapterView.getItemAtPosition(position);
                if(object != null)
                    endYear = Integer.parseInt(object.toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //...
            }
        });
    }

    private void setupToolbar() {
        Toolbar setupToolbar = findViewById(R.id.include);
        setSupportActionBar(setupToolbar);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setDisplayShowHomeEnabled(true);
            supportActionBar.setTitle("Location details");
        }
    }

}
