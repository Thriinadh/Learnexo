package com.learnexo.main;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class EduDetailsActivity extends AppCompatActivity {

    private Spinner yearPicker;
    private String tag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edu_details);

        yearPicker = findViewById(R.id.spinnerYearPicker);

        setupToolbar();

        setupDropDownSpinner();
        spinnerOnselect();
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
