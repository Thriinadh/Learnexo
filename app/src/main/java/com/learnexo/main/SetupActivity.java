package com.learnexo.main;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.learnexo.util.FirebaseUtil;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.learnexo.util.FirebaseUtil.sStorageReference;

public class SetupActivity extends AppCompatActivity {

    private CircleImageView setupImage;
    private Uri mainImageURI = null;

    private EditText setup_nickName;
    private Button setupBtn;
    private ProgressBar setupProgerss;
    private String user_id;
    private Button edit_name_button;
    private TextView skipTView;
    //  private CardView pickImageCView;

    private boolean isChanged = false;
    private FirebaseUtil mFirebaseUtil=new FirebaseUtil();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        setupToolbar();
        setUserId();

        wireViews();

        skipNgotoFeed();

        getFromFirebaseAndSet();
        setupBtnListener();

        profileImageListener();
        enableNameField();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        setupDPOnIView(requestCode, resultCode, data);
    }

    private void enableNameField() {
        edit_name_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setup_nickName.setEnabled(true);
                setup_nickName.setSelection(setup_nickName.getText().length());

            }
        });
    }

    private void skipNgotoFeed() {
        skipTView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, Object> is_skipped_profile = new HashMap<>();
                is_skipped_profile.put("IS_SKIPPED_PROFILE", true);

                mFirebaseUtil.mFirestore.collection("users").document(user_id).
                        set(is_skipped_profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {
                            Intent tabIntent = new Intent(SetupActivity.this, TabsActivity.class);
                            tabIntent.putExtra("com.learnexo.main.IS_SKIPPED_PROFILE",true);
                            startActivity(tabIntent);
                            finish();
                        } else {
                            String error = null;
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                                error = Objects.requireNonNull(task.getException()).getMessage();
                            }
                            Toast.makeText(SetupActivity.this, "Firestore Error : " + error, Toast.LENGTH_LONG).show();
                        }

                        setupProgerss.setVisibility(View.INVISIBLE);
                    }
                });


            }
        });
    }

    private void profileImageListener() {
        setupImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                    if (ContextCompat.checkSelfPermission(SetupActivity.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                        ActivityCompat.requestPermissions(SetupActivity.this,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

                    } else {
                        BringImagePicker();
                    }

                } else {
                    BringImagePicker();
                }

            }
        });

//        pickImageCView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//
//                    if (ContextCompat.checkSelfPermission(SetupActivity.this,
//                            Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//
//                        ActivityCompat.requestPermissions(SetupActivity.this,
//                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
//
//                    } else {
//                        BringImagePicker();
//                    }
//
//                } else {
//                    BringImagePicker();
//                }
//
//            }
//        });
    }

    private void setupBtnListener() {
        setupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String nick_name = setup_nickName.getText().toString();

                if(TextUtils.isEmpty(nick_name) && mainImageURI == null) {
                    Toast.makeText(SetupActivity.this, "Fields can't be empty", Toast.LENGTH_LONG).show();
                }
                if(mainImageURI == null && !TextUtils.isEmpty(nick_name)) {
                    Toast.makeText(SetupActivity.this, "Select DP", Toast.LENGTH_LONG).show();
                }
                if(TextUtils.isEmpty(nick_name) && mainImageURI != null) {
                    Toast.makeText(SetupActivity.this, "Describe yourself", Toast.LENGTH_LONG).show();
                }
                if (!TextUtils.isEmpty(nick_name) && mainImageURI != null) {
                    setupProgerss.setVisibility(View.VISIBLE);

                    if (isChanged) {
                        user_id = FirebaseUtil.getCurrentUserId();
                        StorageReference image_path = sStorageReference.child("Profile_images").child(user_id + ".jpg");
                        image_path.putFile(mainImageURI).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                if (task.isSuccessful()) {
                                    storeFirestore(task, nick_name);
                                } else {
                                    String error = null;
                                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                                        error = Objects.requireNonNull(task.getException()).getMessage();
                                    }
                                    Toast.makeText(SetupActivity.this, "Image Error : " + error, Toast.LENGTH_LONG).show();
                                    setupProgerss.setVisibility(View.INVISIBLE);
                                }
                            }
                        });
                    } else {
                        storeFirestore(null, nick_name);
                    }
                }
            }
        });
    }

    private void getFromFirebaseAndSet() {
        mFirebaseUtil.mFirestore.collection("users").document(user_id).
                collection("setupDetails").document("setupFields").get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()) {
                    if (task.getResult().exists()) {
                        String name = task.getResult().getString("nickName");
                        String image = task.getResult().getString("image");
                        if (image != null)
                            mainImageURI = Uri.parse(image);

                        setup_nickName.setText(name);
                        setup_nickName.setEnabled(false);
                        setup_nickName.setTextColor(Color.BLACK);

                        RequestOptions placeholderRequest = new RequestOptions();
                        placeholderRequest.diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.default_photo);
                      //  setupImage.getDrawable().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);

                        Glide.with(SetupActivity.this)
                                .load(image).apply(placeholderRequest).into(setupImage);
                    }

                } else {
                    String errorMsg = null;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                        errorMsg = Objects.requireNonNull(task.getException()).getMessage();
                    }
                    Toast.makeText(SetupActivity.this, "Firestore Retrieve Error : " + errorMsg, Toast.LENGTH_LONG).show();
                }
                setupProgerss.setVisibility(View.INVISIBLE);
                setupBtn.setEnabled(true);
            }
        });
    }

    private void wireViews() {
        edit_name_button = findViewById(R.id.editNameBtn);
        setupImage = findViewById(R.id.setup_image);
        setup_nickName = findViewById(R.id.setup_nickName);
        skipTView = findViewById(R.id.skipTView);
        setupProgerss = findViewById(R.id.setup_progress);
        //    pickImageCView = findViewById(R.id.pickImageCView);

        setupBtn = findViewById(R.id.setup_btn);
        setupBtn.setEnabled(false);
    }


    private void setUserId() {
        user_id = FirebaseUtil.getCurrentUserId();
    }

    private void setupToolbar() {
        Toolbar setupToolbar = findViewById(R.id.include);
        setSupportActionBar(setupToolbar);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setTitle("Profile completion");
        }
    }

    private void storeFirestore(Task<UploadTask.TaskSnapshot> task, String nick_name) {

        Uri download_uri;

        if (task != null) {

            download_uri = task.getResult().getDownloadUrl();
        } else {

            download_uri = mainImageURI;

        }

        Map<String, String> userMap = new HashMap<>();

        userMap.put("nickName", nick_name);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            userMap.put("image", Objects.requireNonNull(download_uri).toString());
        }

        mFirebaseUtil.mFirestore.collection("users").document(user_id).collection("setupDetails")
                .document("setupFields").set(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {
                    Intent tabIntent = new Intent(SetupActivity.this, TabsActivity.class);
                    startActivity(tabIntent);
                    finish();
                } else {
                    String error = null;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                        error = Objects.requireNonNull(task.getException()).getMessage();
                    }
                    Toast.makeText(SetupActivity.this, "Firestore Error : " + error, Toast.LENGTH_LONG).show();
                }

                setupProgerss.setVisibility(View.INVISIBLE);
            }
        });

    }

    private void BringImagePicker() {
        // start picker to get image for cropping and then use the image in cropping activity
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1, 1)
                .start(SetupActivity.this);
    }

    private void setupDPOnIView(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                mainImageURI = result.getUri();
                setupImage.setImageURI(mainImageURI);
                isChanged = true;
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(SetupActivity.this, "Error" + error, Toast.LENGTH_LONG).show();
            }
        }
    }
}
