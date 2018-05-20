package com.learnexo.main;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.learnexo.util.FirebaseUtil;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

import static com.learnexo.util.FirebaseUtil.sStorageReference;

public class SetupActivity extends AppCompatActivity {

    private static final String TAG = SetupActivity.class.getSimpleName();
    public static final String EXTRA_IS_SKIPPED="com.learnexo.main.IS_SKIPPED_PROFILE";

    private ConstraintLayout constaintLayout;
    private ScrollView scrollView;
    private CircleImageView setupImage;
    private Uri mainImageURI = null;

    private EditText description;
    private Button setupBtn;
    private ProgressBar setupProgerss;
    private String user_id;
    private Button edit_desc_Btn;
    private TextView skipTView;

    private boolean isChanged = false;
    private FirebaseUtil mFirebaseUtil=new FirebaseUtil();
    private  boolean is_profile_edit;

    private Bitmap mBitmap;
    Uri downloadUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
        is_profile_edit=getIntent().getBooleanExtra("IS_PROFILE_EDIT",false);

        setupToolbar();
        setUserId();

        wireViews();

        final View activityRootView = findViewById(R.id.constraintLayout);
        activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int heightDiff = activityRootView.getRootView().getHeight() - activityRootView.getHeight();
                if (heightDiff > dpToPx(SetupActivity.this, 200)) {
                    scrollView.scrollTo(0, 120);
                }
            }
        });

        googleNfbDpSetup();

        skipNgotoFeed();

        if(is_profile_edit)
            getFromFirebaseAndSet();

        profileImageListener();

        setupBtnListener();

    }

    public static float dpToPx(Context context, float valueInDp) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueInDp, metrics);
    }

    private void googleNfbDpSetup() {
        mFirebaseUtil.mFirestore.collection("users").document(user_id).
                collection("reg_details").document("doc").get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot snapshot = task.getResult();
                        if (task.isSuccessful()) {
                            if (snapshot.exists()) {
                                String googleImage = snapshot.getString("googleDpUri");
                                String fbImage = snapshot.getString("fbDpUri");
                                if (googleImage != null)
                                    mainImageURI = Uri.parse(googleImage);
                                else if(fbImage != null)
                                    mainImageURI = Uri.parse(fbImage);

                                RequestOptions placeholderRequest = new RequestOptions();
                                placeholderRequest.diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.empty_profilee);

                                Glide.with(getApplicationContext())
                                        .load(mainImageURI).apply(placeholderRequest).into(setupImage);
                            }

                        } else {
                            String errorMsg;
                                errorMsg = task.getException().getMessage();
                            Toast.makeText(SetupActivity.this, "Firestore Retrieve Error : " + errorMsg, Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        setupDPOnIView(requestCode, resultCode, data);
    }

    private void skipNgotoFeed() {
        skipTView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!is_profile_edit) {

                    Map<String, Object> is_skipped_profile = new HashMap<>();
                    is_skipped_profile.put("IS_SKIPPED_PROFILE", true);

                    mFirebaseUtil.mFirestore.collection("users").document(user_id).
                            set(is_skipped_profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()) {
                                Intent tabIntent = new Intent(SetupActivity.this, TabsActivity.class);
                                tabIntent.putExtra(EXTRA_IS_SKIPPED, true);
                                startActivity(tabIntent);
                                finish();
                            } else {
                                String errorMessage = task.getException().getMessage();
                                Toast.makeText(SetupActivity.this, "Firestore Error : " + errorMessage, Toast.LENGTH_LONG).show();
                            }

                            setupProgerss.setVisibility(View.INVISIBLE);
                        }
                    });


                }else {
                    Intent tabIntent = new Intent(SetupActivity.this, TabsActivity.class);
                    startActivity(tabIntent);
                    finish();
                }
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

    }

    private void setupBtnListener() {
        setupProgerss.setVisibility(View.INVISIBLE);
        setupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String description = SetupActivity.this.description.getText().toString();

                if(TextUtils.isEmpty(description) && mainImageURI == null) {
                    Toast.makeText(SetupActivity.this, "Fields can't be empty", Toast.LENGTH_LONG).show();
                }
                if(mainImageURI == null && !TextUtils.isEmpty(description)) {
                    Toast.makeText(SetupActivity.this, "Select DP", Toast.LENGTH_LONG).show();
                }
                if(TextUtils.isEmpty(description) && mainImageURI != null) {
                    Toast.makeText(SetupActivity.this, "Describe yourself", Toast.LENGTH_LONG).show();
                }
                if (!TextUtils.isEmpty(description) && mainImageURI != null) {
                    setupProgerss.setVisibility(View.VISIBLE);

                    if (isChanged) {
                        user_id = FirebaseUtil.getCurrentUserId();
                        final String randomName = UUID.randomUUID().toString();
                        StorageReference image_path = sStorageReference.child("Profile_images").child(user_id + ".jpg");
                        image_path.putFile(mainImageURI).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull final Task<UploadTask.TaskSnapshot> task) {

                                if (task.isSuccessful()) {
                                  //  storeFirestore(task, description);
                                    if (task != null) {
                                        downloadUri = task.getResult().getDownloadUrl();
                                    } else {
                                        downloadUri = mainImageURI;
                                    }

                                    File newImageFile = new File(mainImageURI.getPath());
                                    try {
                                        mBitmap = new Compressor(SetupActivity.this)
                                                .setMaxHeight(100)
                                                .setMaxWidth(100)
                                                .setQuality(2)
                                                .compressToBitmap(newImageFile);
                                    } catch (IOException e) {
                                        Log.e(TAG, Arrays.toString(e.getStackTrace()));
                                    }

                                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                    mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                                    byte[] thumbData = baos.toByteArray();

                                    String thumbs= "Profile_images".concat("/thumbs");
                                    UploadTask uploadTask = FirebaseUtil.sStorageReference.child(thumbs)
                                            .child(randomName + ".jpg").putBytes(thumbData);

                                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                            storeFirestore(taskSnapshot, description);

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            String error = task.getException().getMessage();
                                            Toast.makeText(SetupActivity.this,
                                                    "Firestore Retrieve Error : " + error, Toast.LENGTH_LONG).show();
                                        }
                                    });

                                } else {
                                    String errorMessage = task.getException().getMessage();
                                    Toast.makeText(SetupActivity.this, "Image Error : " + errorMessage, Toast.LENGTH_LONG).show();
                                    setupProgerss.setVisibility(View.INVISIBLE);
                                }
                            }
                        });
                    } else {
                        storeFirestore(null, description);
                    }
                }
            }
        });
    }

    private void getFromFirebaseAndSet() {
        mFirebaseUtil.mFirestore.collection("users").document(user_id).
                collection("reg_details").document("doc").get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot snapshot = task.getResult();
                if (task.isSuccessful()) {
                    if (snapshot.exists()) {
                        String description = snapshot.getString("description");
                        String image = snapshot.getString("dpUrl");
                        if (image != null)
                            mainImageURI = Uri.parse(image);

                        SetupActivity.this.description.setText(description);
                        SetupActivity.this.description.setTextColor(Color.BLACK);

                        RequestOptions placeholderRequest = new RequestOptions();
                        placeholderRequest.diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.empty_profilee);

                        Glide.with(getApplicationContext())
                                .load(image).apply(placeholderRequest).into(setupImage);
                    }

                } else {
                    String errorMessage = task.getException().getMessage();
                    Toast.makeText(SetupActivity.this, "Firestore Retrieve Error : " + errorMessage, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void wireViews() {
        constaintLayout = findViewById(R.id.constraintLayout);
        scrollView = findViewById(R.id.scrollView);
        setupImage = findViewById(R.id.setup_image);
        description = findViewById(R.id.setup_nickName);
        skipTView = findViewById(R.id.skipTView);
        setupProgerss = findViewById(R.id.setup_progress);
        // pickImageCView = findViewById(R.id.pickImageCView);

        setupBtn = findViewById(R.id.setup_btn);
    }


    private void setUserId() {
        user_id = FirebaseUtil.getCurrentUserId();
    }

    private void setupToolbar() {
        Toolbar setupToolbar = findViewById(R.id.include);
        setSupportActionBar(setupToolbar);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setDisplayShowHomeEnabled(true);
            supportActionBar.setTitle("Profile");
        }
    }

    private void storeFirestore(UploadTask.TaskSnapshot taskSnapshot, String description) {

        Uri downloadthumbUri;

        downloadthumbUri = taskSnapshot.getDownloadUrl();



        Map<String, Object> userMap = new HashMap<>();
        userMap.put("description", description);
        userMap.put("dpUrl", downloadUri.toString());
        userMap.put("dpThumb", downloadthumbUri.toString());

        mFirebaseUtil.mFirestore.collection("users").document(user_id).collection("reg_details")
                .document("doc").update(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {
                    Intent tabIntent = new Intent(SetupActivity.this, TabsActivity.class);
                    startActivity(tabIntent);
                    finish();
                } else {
                    String errorMessage = task.getException().getMessage();
                    Toast.makeText(SetupActivity.this, "Firestore Error : " + errorMessage, Toast.LENGTH_LONG).show();
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
