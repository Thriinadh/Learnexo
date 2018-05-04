package com.learnexo.main;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class OldInterestsActivity extends AppCompatActivity {

    private FloatingActionButton saveButton;

    private CheckBox mFirstCheckBox;
    private CheckBox mSecondCheckBox;
    private CheckBox mThirdCheckBox;
    private CheckBox mFourthCheckBox;
    private CheckBox mFifthCheckBox;
    private CheckBox mSixthCheckBox;
    private CheckBox mSeventhCheckBox;
    private CheckBox mEighthCheckBox;

    private CheckBox mFirstCheckBox2;
    private CheckBox mSecondCheckBox2;
    private CheckBox mThirdCheckBox2;
    private CheckBox mFourthCheckBox2;
    private CheckBox mFifthCheckBox2;
    private CheckBox mSixthCheckBox2;
    private CheckBox mSeventhCheckBox2;
    private CheckBox mEighthCheckBox2;

    private CheckBox mFirstCheckBox3;
    private CheckBox mSecondCheckBox3;
    private CheckBox mThirdCheckBox3;
    private CheckBox mFourthCheckBox3;
    private CheckBox mFifthCheckBox3;
    private CheckBox mSixthCheckBox3;
    private CheckBox mSeventhCheckBox3;
    private CheckBox mEighthCheckBox3;

    private CheckBox mFirstCheckBox4;
    private CheckBox mSecondCheckBox4;
    private CheckBox mThirdCheckBox4;
    private CheckBox mFourthCheckBox4;
    private CheckBox mFifthCheckBox4;
    private CheckBox mSixthCheckBox4;
    private CheckBox mSeventhCheckBox4;
    private CheckBox mEighthCheckBox4;

    private CheckBox mFirstCheckBox5;
    private CheckBox mSecondCheckBox5;
    private CheckBox mThirdCheckBox5;
    private CheckBox mFourthCheckBox5;
    private CheckBox mFifthCheckBox5;
    private CheckBox mSixthCheckBox5;
    private CheckBox mSeventhCheckBox5;
    private CheckBox mEighthCheckBox5;

    private FirebaseAuth mAuth;

    private FirebaseFirestore firebaseFirestore;

    private DatabaseReference mDatabase;

    private Toolbar setupToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interests);

        setupToolbar = findViewById(R.id.include);
        setSupportActionBar(setupToolbar);
        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle("Choose Interests");
        }

        mFirstCheckBox = findViewById(R.id.firstCheckBox);
        mSecondCheckBox = findViewById(R.id.secondCheckBox);
        mThirdCheckBox = findViewById(R.id.thirdCheckBox);
        mFourthCheckBox = findViewById(R.id.fourthCheckBox);
        mFifthCheckBox = findViewById(R.id.fifthCheckBox);
        mSixthCheckBox = findViewById(R.id.sixthCheckBox);
        mSeventhCheckBox = findViewById(R.id.seventhCheckBox);
        mEighthCheckBox = findViewById(R.id.eighthCheckBox);

        mFirstCheckBox2 = findViewById(R.id.firstCheckBox2);
        mSecondCheckBox2 = findViewById(R.id.secondCheckBox2);
        mThirdCheckBox2 = findViewById(R.id.thirdCheckBox2);
        mFourthCheckBox2 = findViewById(R.id.fourthCheckBox2);
        mFifthCheckBox2 = findViewById(R.id.fifthCheckBox2);
        mSixthCheckBox2 = findViewById(R.id.sixthCheckBox2);
        mSeventhCheckBox2 = findViewById(R.id.seventhCheckBox2);
        mEighthCheckBox2 = findViewById(R.id.eighthCheckBox2);

        mFirstCheckBox3 = findViewById(R.id.firstCheckBox3);
        mSecondCheckBox3 = findViewById(R.id.secondCheckBox3);
        mThirdCheckBox3 = findViewById(R.id.thirdCheckBox3);
        mFourthCheckBox3 = findViewById(R.id.fourthCheckBox3);
        mFifthCheckBox3 = findViewById(R.id.fifthCheckBox3);
        mSixthCheckBox3 = findViewById(R.id.sixthCheckBox3);
        mSeventhCheckBox3 = findViewById(R.id.seventhCheckBox3);
        mEighthCheckBox3 = findViewById(R.id.eighthCheckBox3);

        mFirstCheckBox4 = findViewById(R.id.firstCheckBox4);
        mSecondCheckBox4 = findViewById(R.id.secondCheckBox4);
        mThirdCheckBox4 = findViewById(R.id.thirdCheckBox4);
        mFourthCheckBox4 = findViewById(R.id.fourthCheckBox4);
        mFifthCheckBox4 = findViewById(R.id.fifthCheckBox4);
        mSixthCheckBox4 = findViewById(R.id.sixthCheckBox4);
        mSeventhCheckBox4 = findViewById(R.id.seventhCheckBox4);
        mEighthCheckBox4 = findViewById(R.id.eighthCheckBox4);

        mFirstCheckBox5 = findViewById(R.id.firstCheckBox5);
        mSecondCheckBox5 = findViewById(R.id.secondCheckBox5);
        mThirdCheckBox5 = findViewById(R.id.thirdCheckBox5);
        mFourthCheckBox5 = findViewById(R.id.fourthCheckBox5);
        mFifthCheckBox5 = findViewById(R.id.fifthCheckBox5);
        mSixthCheckBox5 = findViewById(R.id.sixthCheckBox5);
        mSeventhCheckBox5 = findViewById(R.id.seventhCheckBox5);
        mEighthCheckBox5 = findViewById(R.id.eighthCheckBox5);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        firebaseFirestore = FirebaseFirestore.getInstance();

        mAuth = FirebaseAuth.getInstance();

        saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user_id = mAuth.getCurrentUser().getUid();
                DatabaseReference current_user_db = mDatabase.child(user_id);

                Map<String, String> userMap = new HashMap<>();

                if(mFirstCheckBox.isChecked()) {
                    current_user_db.child("F1").setValue("Compiler design");
                    userMap.put("F1", "Compiler Design");
                    settingInterestsToFirebase(user_id, userMap);
                }

                if(mSecondCheckBox.isChecked()) {
                    current_user_db.child("F2").setValue("Operating Systems");
                    userMap.put("F2", "Operating Systems");
                    settingInterestsToFirebase(user_id, userMap);
                }

                if(mThirdCheckBox.isChecked()) {
                    current_user_db.child("F3").setValue("Software Engineering");
                    userMap.put("F3", "Software Engineering");
                    settingInterestsToFirebase(user_id, userMap);
                }

                if(mFourthCheckBox.isChecked()) {
                    current_user_db.child("F4").setValue("Mathematical foundations of computer science");
                    userMap.put("F4", "MFCS");
                    settingInterestsToFirebase(user_id, userMap);
                }

                if(mFifthCheckBox.isChecked()) {
                    current_user_db.child("F5").setValue("Digital Logic Design");
                    userMap.put("F5", "Digital Logic Design");
                    settingInterestsToFirebase(user_id, userMap);
                }

                if(mSixthCheckBox.isChecked()) {
                    current_user_db.child("F6").setValue("Operations Research");
                    userMap.put("F6", "Operations Research");
                    settingInterestsToFirebase(user_id, userMap);
                }

                if(mSeventhCheckBox.isChecked()) {
                    current_user_db.child("F7").setValue("Microprocessors");
                    userMap.put("F7", "Microprocessors");
                    settingInterestsToFirebase(user_id, userMap);
                }

                if(mEighthCheckBox.isChecked()) {
                    current_user_db.child("F8").setValue("Distributed Systems");
                    userMap.put("F8", "Distributed Systems");
                    settingInterestsToFirebase(user_id, userMap);
                }

                if(mFirstCheckBox2.isChecked()) {
                    current_user_db.child("P1").setValue("Java");
                    userMap.put("P1", "Java");
                    settingInterestsToFirebase(user_id, userMap);
                }

                if(mSecondCheckBox2.isChecked()) {
                    current_user_db.child("P2").setValue("Python");
                    userMap.put("P2", "Python");
                    settingInterestsToFirebase(user_id, userMap);
                }

                if(mThirdCheckBox2.isChecked()) {
                    current_user_db.child("P3").setValue("Scala");
                    userMap.put("P3", "Scala");
                    settingInterestsToFirebase(user_id, userMap);
                }

                if(mFourthCheckBox2.isChecked()) {
                    current_user_db.child("P4").setValue("C language");
                    userMap.put("P4", "C language");
                    settingInterestsToFirebase(user_id, userMap);
                }

                if(mFifthCheckBox2.isChecked()) {
                    current_user_db.child("P5").setValue("Ruby");
                    userMap.put("P5", "Ruby");
                    settingInterestsToFirebase(user_id, userMap);
                }

                if(mSixthCheckBox2.isChecked()) {
                    current_user_db.child("P6").setValue("Android");
                    userMap.put("P6", "Android");
                    settingInterestsToFirebase(user_id, userMap);
                }

                if(mSeventhCheckBox2.isChecked()) {
                    current_user_db.child("P7").setValue("Objective C");
                    userMap.put("P7", "Objective C");
                    settingInterestsToFirebase(user_id, userMap);
                }

                if(mEighthCheckBox2.isChecked()) {
                    current_user_db.child("P8").setValue("Design Patterns");
                    userMap.put("P8", "Design Patterns");
                    settingInterestsToFirebase(user_id, userMap);
                }

                if(mFirstCheckBox3.isChecked()) {
                    current_user_db.child("D1").setValue("DB Fundamentals");
                    userMap.put("D1", "DB Fundamentals");
                    settingInterestsToFirebase(user_id, userMap);
                }

                if(mSecondCheckBox3.isChecked()) {
                    current_user_db.child("D2").setValue("SQL");
                    userMap.put("D2", "SQL");
                    settingInterestsToFirebase(user_id, userMap);
                }

                if(mThirdCheckBox3.isChecked()) {
                    current_user_db.child("D3").setValue("NoSQL");
                    userMap.put("D3", "NoSQL");
                    settingInterestsToFirebase(user_id, userMap);
                }

                if(mFourthCheckBox3.isChecked()) {
                    current_user_db.child("D4").setValue("Hadoop");
                    userMap.put("D4", "Hadoop");
                    settingInterestsToFirebase(user_id, userMap);
                }

                if(mFifthCheckBox3.isChecked()) {
                    current_user_db.child("D5").setValue("MongoDB");
                    userMap.put("D5", "MongoDB");
                    settingInterestsToFirebase(user_id, userMap);
                }

                if(mSixthCheckBox3.isChecked()) {
                    current_user_db.child("D6").setValue("Cassandra");
                    userMap.put("D6", "Cassandra");
                    settingInterestsToFirebase(user_id, userMap);
                }

                if(mSeventhCheckBox3.isChecked()) {
                    current_user_db.child("D7").setValue("Big Data");
                    userMap.put("D7", "Big Data");
                    settingInterestsToFirebase(user_id, userMap);
                }

                if(mEighthCheckBox3.isChecked()) {
                    current_user_db.child("D8").setValue("Firebase");
                    userMap.put("D8", "Firebase");
                    settingInterestsToFirebase(user_id, userMap);
                }

                if(mFirstCheckBox4.isChecked()) {
                    current_user_db.child("N1").setValue("Computer Networks");
                    userMap.put("N1", "Computer Networks");
                    settingInterestsToFirebase(user_id, userMap);
                }

                if(mSecondCheckBox4.isChecked()) {
                    current_user_db.child("N2").setValue("Internet Protocols");
                    userMap.put("N2", "Internet Protocols");
                    settingInterestsToFirebase(user_id, userMap);
                }

                if(mThirdCheckBox4.isChecked()) {
                    current_user_db.child("N3").setValue("Introduction to Wireless Networks");
                    userMap.put("N3", "Introduction to Wireless Networks");
                    settingInterestsToFirebase(user_id, userMap);
                }

                if(mFourthCheckBox4.isChecked()) {
                    current_user_db.child("N4").setValue("Networking Services");
                    userMap.put("N4", "Networking Services");
                    settingInterestsToFirebase(user_id, userMap);
                }

                if(mFifthCheckBox4.isChecked()) {
                    current_user_db.child("N5").setValue("Graph Theory");
                    userMap.put("N5", "Graph Theory");
                    settingInterestsToFirebase(user_id, userMap);
                }

                if(mSixthCheckBox4.isChecked()) {
                    current_user_db.child("N6").setValue("Switched Network Management");
                    userMap.put("N6", "Switched Network Management");
                    settingInterestsToFirebase(user_id, userMap);
                }

                if(mSeventhCheckBox4.isChecked()) {
                    current_user_db.child("N7").setValue("Survival Networks");
                    userMap.put("N7", "Survival Networks");
                    settingInterestsToFirebase(user_id, userMap);
                }

                if(mEighthCheckBox4.isChecked()) {
                    current_user_db.child("N8").setValue("Telecommunication Networks");
                    userMap.put("N8", "Telecommunication Networks");
                    settingInterestsToFirebase(user_id, userMap);
                }

                if(mFirstCheckBox5.isChecked()) {
                    current_user_db.child("A1").setValue("Machine Learning");
                    userMap.put("A1", "Machine Learning");
                    settingInterestsToFirebase(user_id, userMap);
                }

                if(mSecondCheckBox5.isChecked()) {
                    current_user_db.child("A2").setValue("Neural Networks");
                    userMap.put("A2", "Neural Networks");
                    settingInterestsToFirebase(user_id, userMap);
                }

                if(mThirdCheckBox5.isChecked()) {
                    current_user_db.child("A3").setValue("Deep Learning");
                    userMap.put("A3", "Deep Learning");
                    settingInterestsToFirebase(user_id, userMap);
                }

                if(mFourthCheckBox5.isChecked()) {
                    current_user_db.child("A4").setValue("Computer Vision");
                    userMap.put("A4", "Computer Vision");
                    settingInterestsToFirebase(user_id, userMap);
                }

                if(mFifthCheckBox5.isChecked()) {
                    current_user_db.child("A5").setValue("Robotics");
                    userMap.put("A5", "Robotics");
                    settingInterestsToFirebase(user_id, userMap);
                }

                if(mSixthCheckBox5.isChecked()) {
                    current_user_db.child("A6").setValue("Speech Recognition");
                    userMap.put("A6", "Speech Recognition");
                    settingInterestsToFirebase(user_id, userMap);
                }

                if(mSeventhCheckBox5.isChecked()) {
                    current_user_db.child("A7").setValue("Image Processing");
                    userMap.put("A7", "Image Processing");
                    settingInterestsToFirebase(user_id, userMap);
                }

                if(mEighthCheckBox5.isChecked()) {
                    current_user_db.child("A8").setValue("Evolutionary Computation");
                    userMap.put("A8", "Evolutionary Computation");
                    settingInterestsToFirebase(user_id, userMap);
                }

                Intent setupIntent = new Intent(OldInterestsActivity.this, SetupActivity.class);
                startActivity(setupIntent);
                finish();
            }
        });

    }

    private void settingInterestsToFirebase(String user_id, Map<String, String> userMap) {
        firebaseFirestore.collection("Users").document(user_id).collection("Interests")
                .document("Inter").set(userMap);
    }

    public void didTapBox(View view) {
        final Animation myAnim = AnimationUtils.loadAnimation(OldInterestsActivity.this, R.anim.bounce);

        // Use bounce interpolator with amplitude 0.2 and frequency 20
        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.1, 20);
        myAnim.setInterpolator(interpolator);

        switch (view.getId()) {

            case R.id.firstCheckBox:
                mFirstCheckBox.startAnimation(myAnim);
                break;

            case R.id.secondCheckBox:
                mSecondCheckBox.startAnimation(myAnim);
                break;

            case R.id.thirdCheckBox:
                mThirdCheckBox.startAnimation(myAnim);
                break;

            case R.id.fourthCheckBox:
                mFourthCheckBox.startAnimation(myAnim);
                break;

            case R.id.fifthCheckBox:
                mFifthCheckBox.startAnimation(myAnim);
                break;

            case R.id.sixthCheckBox:
                mSixthCheckBox.startAnimation(myAnim);
                break;

            case R.id.seventhCheckBox:
                mSeventhCheckBox.startAnimation(myAnim);
                break;

            case R.id.eighthCheckBox:
                mEighthCheckBox.startAnimation(myAnim);
                break;

            case R.id.firstCheckBox2:
                mFirstCheckBox2.startAnimation(myAnim);
                break;

            case R.id.secondCheckBox2:
                mSecondCheckBox2.startAnimation(myAnim);
                break;

            case R.id.thirdCheckBox2:
                mThirdCheckBox2.startAnimation(myAnim);
                break;

            case R.id.fourthCheckBox2:
                mFourthCheckBox2.startAnimation(myAnim);
                break;

            case R.id.fifthCheckBox2:
                mFifthCheckBox2.startAnimation(myAnim);
                break;

            case R.id.sixthCheckBox2:
                mSixthCheckBox2.startAnimation(myAnim);
                break;

            case R.id.seventhCheckBox2:
                mSeventhCheckBox2.startAnimation(myAnim);
                break;

            case R.id.eighthCheckBox2:
                mEighthCheckBox2.startAnimation(myAnim);
                break;

            case R.id.firstCheckBox3:
                mFirstCheckBox3.startAnimation(myAnim);
                break;

            case R.id.secondCheckBox3:
                mSecondCheckBox3.startAnimation(myAnim);
                break;

            case R.id.thirdCheckBox3:
                mThirdCheckBox3.startAnimation(myAnim);
                break;

            case R.id.fourthCheckBox3:
                mFourthCheckBox3.startAnimation(myAnim);
                break;

            case R.id.fifthCheckBox3:
                mFifthCheckBox3.startAnimation(myAnim);
                break;

            case R.id.sixthCheckBox3:
                mSixthCheckBox3.startAnimation(myAnim);
                break;

            case R.id.seventhCheckBox3:
                mSeventhCheckBox3.startAnimation(myAnim);
                break;

            case R.id.eighthCheckBox3:
                mEighthCheckBox3.startAnimation(myAnim);
                break;

            case R.id.firstCheckBox4:
                mFirstCheckBox4.startAnimation(myAnim);
                break;

            case R.id.secondCheckBox4:
                mSecondCheckBox4.startAnimation(myAnim);
                break;

            case R.id.thirdCheckBox4:
                mThirdCheckBox4.startAnimation(myAnim);
                break;

            case R.id.fourthCheckBox4:
                mFourthCheckBox4.startAnimation(myAnim);
                break;

            case R.id.fifthCheckBox4:
                mFifthCheckBox4.startAnimation(myAnim);
                break;

            case R.id.sixthCheckBox4:
                mSixthCheckBox4.startAnimation(myAnim);
                break;

            case R.id.seventhCheckBox4:
                mSeventhCheckBox4.startAnimation(myAnim);
                break;

            case R.id.eighthCheckBox4:
                mEighthCheckBox4.startAnimation(myAnim);
                break;

            case R.id.firstCheckBox5:
                mFirstCheckBox5.startAnimation(myAnim);
                break;

            case R.id.secondCheckBox5:
                mSecondCheckBox5.startAnimation(myAnim);
                break;

            case R.id.thirdCheckBox5:
                mThirdCheckBox5.startAnimation(myAnim);
                break;

            case R.id.fourthCheckBox5:
                mFourthCheckBox5.startAnimation(myAnim);
                break;

            case R.id.fifthCheckBox5:
                mFifthCheckBox5.startAnimation(myAnim);
                break;

            case R.id.sixthCheckBox5:
                mSixthCheckBox5.startAnimation(myAnim);
                break;

            case R.id.seventhCheckBox5:
                mSeventhCheckBox5.startAnimation(myAnim);
                break;

            case R.id.eighthCheckBox5:
                mEighthCheckBox5.startAnimation(myAnim);
                break;

        }

    }

    }
