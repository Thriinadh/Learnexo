package com.learnexo.main;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;

public class EditDetailsActivity extends AppCompatActivity {

    Toolbar setupToolbar;

    private RelativeLayout eduRelate;
    private RelativeLayout empRelate;
    private RelativeLayout locRelate;

    private TextView editEduTView;
    private TextView editEmpTView;
    private TextView editLocTView;

    private TextView noDetailsYet;
    private ConstraintLayout topConstraint;
    private RelativeLayout addDetails;

    String studiedAt;
    String position;
    String company;
    String location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_details);

        setupToolbar();

        eduRelate = findViewById(R.id.eduRelate);
        empRelate = findViewById(R.id.empRelate);
        locRelate = findViewById(R.id.locRelate);

        noDetailsYet = findViewById(R.id.noDetailsYet);
        topConstraint = findViewById(R.id.topConstraint);
        addDetails = findViewById(R.id.addDetails);

        editEduTView = findViewById(R.id.editEdu);
        editEmpTView = findViewById(R.id.editEmp);
        editLocTView = findViewById(R.id.editLoc);

        Intent intent = getIntent();
        studiedAt = intent.getStringExtra("EXTRA_STUDIED_AT");
        position = intent.getStringExtra("EXTRA_POSITION");
        location = intent.getStringExtra("EXTRA_LOCATION");
        company = intent.getStringExtra("EXTRA_COMPANY");

        editEduTView.setText(studiedAt);
        editEmpTView.setText(position+" at "+company);
        editLocTView.setText(location);

        eduRelate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditDetailsActivity.this, EduDetailsActivity.class);
                startActivity(intent);
            }
        });

        empRelate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditDetailsActivity.this, EmpDetailsActivity.class);
                startActivity(intent);
            }
        });

        locRelate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditDetailsActivity.this, LocationDetailsActivity.class);
                startActivity(intent);
            }
        });

        if (studiedAt == null && position == null && location == null && company == null) {

            topConstraint.setVisibility(View.INVISIBLE);
            noDetailsYet.setVisibility(View.VISIBLE);
            addDetails.setVisibility(View.VISIBLE);

        }

        addDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(EditDetailsActivity.this);
                String list[] = {"Add Education Details", "Add Employment Details", "Add Location Details"};
                builder.setTitle("Add Details")
                        .setItems(list, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // The 'which' argument contains the index position
                                // of the selected item
                            }
                        });

                builder.show();

            }
        });

    }

    private void setupToolbar() {
        setupToolbar = findViewById(R.id.include);
        setSupportActionBar(setupToolbar);
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Edit Details");
        }
    }
}
