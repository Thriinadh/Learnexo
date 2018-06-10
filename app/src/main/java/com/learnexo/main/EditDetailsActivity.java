package com.learnexo.main;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class EditDetailsActivity extends AppCompatActivity {

    private RelativeLayout eduRelate;
    private RelativeLayout empRelate;
    private RelativeLayout locRelate;

    private TextView editEduTView;
    private TextView editEmpTView;
    private TextView editLocTView;

    String studiedAt;
    String position;
    String company;
    String location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_details);

        eduRelate = findViewById(R.id.eduRelate);
        empRelate = findViewById(R.id.empRelate);
        locRelate = findViewById(R.id.locRelate);

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

            editEduTView.setVisibility(View.INVISIBLE);
            editEmpTView.setVisibility(View.INVISIBLE);
            editLocTView.setText("Add details");
            editLocTView.setTextSize(18);

        }

    }
}
