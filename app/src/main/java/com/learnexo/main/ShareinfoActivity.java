package com.learnexo.main;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

public class ShareinfoActivity extends AppCompatActivity {

    private static final int FEED_FRAG_NO = 0;
    private Uri postedImageURI = null;

    private ProgressBar shareProgressBar;
    private ConstraintLayout constraintKeyboardIn;
    private ConstraintLayout constraintKeyboard;
    private EditText enterContent;
    private ImageView loadImage;
    private String user_id;
    private Button postBtn;
    private CircleImageView imageView;
    private TextView username;
    private TextView timeofPost;
    private Button galleryBtn;
    private AppCompatSpinner spinner;
    private Toolbar toolbar;
    private String selectedSub;

    private StorageReference storageReference;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;

    private Bitmap compressedImageFile;

    Map<String, Object> postMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shareinfo);

        wiringViews();
        postBtn.setEnabled(false);
        timeofPost.setText("Now");
        setupToolbar();
        setupFirebase();
        getDPandNameAndSet();
        setupDropDownSpinner();
        spinnerOnselect();
        galleryBtnOnclick();
        enablePostBtnAfterFiveCharsInEditText();
        postBtnOnclick();
    }

    private void postBtnOnclick() {
        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String enteredText = enterContent.getText().toString();

                if(!TextUtils.isEmpty(enteredText) && selectedSub != null) {

                    shareProgressBar.setVisibility(View.VISIBLE);

                    postMap = new HashMap<>();
                    postMap.put("postedContent", enteredText);
                    postMap.put("subject", selectedSub);
                    postMap.put("user_id", user_id);
                    postMap.put("timestamp", FieldValue.serverTimestamp());

                    if(postedImageURI != null) {

                        final String randomName = UUID.randomUUID().toString();

                        StorageReference filepath = storageReference.child("post_images").child(randomName + ".jpg");
                        filepath.putFile(postedImageURI).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull final Task<UploadTask.TaskSnapshot> task) {

                                final String downloadUri = task.getResult().getDownloadUrl().toString();

                                if (task.isSuccessful()) {

                                    File newImageFile = new File(postedImageURI.getPath());
                                    try {
                                        compressedImageFile = new Compressor(ShareinfoActivity.this)
                                                .setMaxHeight(100)
                                                .setMaxWidth(100)
                                                .setQuality(2)
                                                .compressToBitmap(newImageFile);

                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }

                                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                    compressedImageFile.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                                    byte[] thumbData = baos.toByteArray();

                                    UploadTask uploadTask = storageReference.child("post_images/thumbs")
                                            .child(randomName + ".jpg").putBytes(thumbData);

                                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                            String downloadthumbUri = taskSnapshot.getDownloadUrl().toString();

                                            postMap.put("image_url", downloadUri);
                                            postMap.put("image_thumb", downloadthumbUri);

                                            saveToFirebaseGotoFeed();

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            String error = task.getException().getMessage();
                                            Toast.makeText(ShareinfoActivity.this,
                                                    "Firestore Retrieve Error : " + error, Toast.LENGTH_LONG).show();
                                        }
                                    });

                                } else {
                                    shareProgressBar.setVisibility(View.INVISIBLE);
                                }

                            }
                        });

                    } else {
                        saveToFirebaseGotoFeed();
                    }
                }

            }
        });
    }

    private void enablePostBtnAfterFiveCharsInEditText() {
        enterContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //...
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //...
            }

            @Override
            public void afterTextChanged(Editable editable) {
                enableSubmitIfReady();
            }
        });
    }

    private void galleryBtnOnclick() {
        galleryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BringImagePicker();
            }
        });
    }

    private void spinnerOnselect() {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

               Object object = adapterView.getItemAtPosition(position);
                if(object != null)
                       selectedSub = object.toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //...
            }
        });
    }

    private void setupDropDownSpinner() {
        // Create an ArrayAdapter using the string array and a default spinner
        ArrayAdapter<CharSequence> staticAdapter = ArrayAdapter
                .createFromResource(this, R.array.subject_names,
                        android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        staticAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setPrompt("Select related subject");

        // Apply the adapter to the spinner
        spinner.setAdapter(
                new NothingSelectedSpinnerAdapter(
                        staticAdapter,
                        R.layout.contact_spinner_row_nothing_selected,
                        // R.layout.contact_spinner_nothing_selected_dropdown, // Optional
                        this));
    }

    private void getDPandNameAndSet() {
        firebaseFirestore.collection("Users").document(user_id).collection("Setup Details")
                .document("Setup Fields").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if(task.isSuccessful()) {

                    if(task.getResult().exists()) {

                        String name = task.getResult().getString("Nick name");
                        String image = task.getResult().getString("Image");

                        username.setText(name);

                        RequestOptions placeholderRequest = new RequestOptions();
                        placeholderRequest.apply(placeholderRequest).placeholder(R.drawable.default_photo);

                        Glide.with(ShareinfoActivity.this).load(image).apply(placeholderRequest).into(imageView);

                    }

                } else {

                    String error = task.getException().getMessage();
                    Toast.makeText(ShareinfoActivity.this, "Firestore Retrieve Error : " + error, Toast.LENGTH_LONG).show();

                }

            }
        });
    }

    private void setupFirebase() {
        storageReference = FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if(user != null)
            user_id = mAuth.getCurrentUser().getUid();
        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    private void setupToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle("Share info");
        }
    }

    private void wiringViews() {
        shareProgressBar = findViewById(R.id.shareProgressBar);
        enterContent = findViewById(R.id.enterContent);
        username = findViewById(R.id.userNameTView);
        imageView = findViewById(R.id.smallCircleImageView);
        timeofPost = findViewById(R.id.timeofPost);
        galleryBtn = findViewById(R.id.gallery_btn);
        loadImage = findViewById(R.id.loadImage);
        constraintKeyboardIn = findViewById(R.id.constraintKeyboardIn);
        constraintKeyboard = findViewById(R.id.constraintKeyboard);
        spinner = findViewById(R.id.spinner);
        postBtn = findViewById(R.id.post_button);
        spinner = findViewById(R.id.spinner);
    }

    public void saveToFirebaseGotoFeed() {

       final Task<DocumentReference> documentReferenceTask= firebaseFirestore.collection("Posts").add(postMap);

                documentReferenceTask.addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {

                if (task.isSuccessful()) {
                    gotoFeedTab();
                } else {

                    String error = task.getException().getMessage();
                    Toast.makeText(ShareinfoActivity.this, "Firestore Retrieve Error : " + error, Toast.LENGTH_LONG).show();

                }
                shareProgressBar.setVisibility(View.INVISIBLE);

            }

                    private void gotoFeedTab() {
                        //String postId = documentReferenceTask.getResult().getId();

                        Intent feedIntent = TabsActivity.newIntent(ShareinfoActivity.this, FEED_FRAG_NO);
                        startActivity(feedIntent);
//                        finish();
                    }
                });

    }

    public boolean onTouch(View view, MotionEvent event) {

        if(view.getId() == R.id.enterContent) {

            view.getParent().requestDisallowInterceptTouchEvent(true);
            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_UP:
                    view.getParent().requestDisallowInterceptTouchEvent(false);
                    break;
            }

        }
        return false;

    }

    private void BringImagePicker() {

        // start picker to get image for cropping and then use the image in cropping activity
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(ShareinfoActivity.this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        setImageToIview(requestCode, resultCode, data);
    }

    private void setImageToIview(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                constraintKeyboard.setVisibility(View.INVISIBLE);
                constraintKeyboardIn.setVisibility(View.VISIBLE);
                postedImageURI = result.getUri();
                loadImage.setImageURI(postedImageURI);

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ShareinfoActivity.this, "Uploaded", Toast.LENGTH_LONG).show();
                        constraintKeyboardIn.setVisibility(View.INVISIBLE);
                        constraintKeyboard.setVisibility(View.VISIBLE);
                    }
                }, 2000);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    public void enableSubmitIfReady() {

        boolean isReady = enterContent.getText().toString().length() > 5;
        postBtn.setEnabled(isReady);
        if(isReady) {
            postBtn.setBackgroundColor(Color.parseColor("#32CD32"));
            postBtn.setTextColor(Color.parseColor("#ffffff"));
        } else  {
            postBtn.setBackgroundColor(Color.parseColor("#D3D3D3"));
            postBtn.setTextColor(Color.parseColor("#000000"));
        }
    }

}
