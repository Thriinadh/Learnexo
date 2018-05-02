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
import android.util.Log;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.learnexo.model.feed.FeedItem;
import com.learnexo.model.feed.post.Post;
import com.learnexo.model.feed.question.Question;
import com.learnexo.util.FirebaseUtil;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

public class PublishActivity extends AppCompatActivity {

    private static final int FEED_FRAG_NO = 0;
    private static final String TAG = PublishActivity.class.getSimpleName();

    private Uri postedImageURI;
    private ProgressBar mProgressBar;

    private ConstraintLayout constraintKeyboardIn;
    private ConstraintLayout constraintKeyboard;

    private EditText content;
    private ImageView loadImage;

    private Button postBtn;
    private CircleImageView imageView;
    private TextView username;
    private TextView timeofPost;

    private Button galleryBtn;
    private AppCompatSpinner spinner;
    private String tag;
    private String name;

    private Bitmap mBitmap;

    public String postId;
    private String mUserId = FirebaseUtil.getCurrentUserId();
    private FirebaseUtil mFirebaseUtil = new FirebaseUtil();



    FeedItem mFeedItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish);

        String publishType;
        publishType = getIntent().getStringExtra("PUBLISH_TYPE");

        wiringViews();
        setupToolbar(publishType);

        getDPandNameAndSet();
        setupDropDownSpinner();
        spinnerOnselect();
        galleryBtnListener();
        enablePublishBtn();

        postBtnListener(publishType);
    }

    private void postBtnListener(final String publishType) {
        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String content = PublishActivity.this.content.getText().toString();

                if(!TextUtils.isEmpty(content) && tag != null) {
                    mProgressBar.setVisibility(View.VISIBLE);

                    if(publishType.equals("Share Info")) {
                        mFeedItem = new Post();
                    } else {
                        mFeedItem = new Question();
                    }
                    mFeedItem.setContent(content);
                    mFeedItem.setTags(Collections.singletonList(tag));
                    mFeedItem.setUserId(mUserId);
                    mFeedItem.setUserName(name);
//                    mFeedItem.setPublishTime(new Date());

                    if(postedImageURI != null) {
                        saveImagetoStorage();
                    }
                    saveFeedItem(mFeedItem);

                }

            }
        });
    }

    private void saveImagetoStorage() {
        final String randomName = UUID.randomUUID().toString();

        StorageReference filepath = FirebaseUtil.sStorageReference.child("post_images").child(randomName + ".jpg");
        filepath.putFile(postedImageURI).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull final Task<UploadTask.TaskSnapshot> task) {

                final String downloadUri = task.getResult().getDownloadUrl().toString();

                if (task.isSuccessful()) {
                    File newImageFile = new File(postedImageURI.getPath());
                    try {
                        mBitmap = new Compressor(PublishActivity.this)
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

                    uploadImage(task, downloadUri, thumbData, randomName);

                } else {
                    mProgressBar.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    private void uploadImage(@NonNull final Task<UploadTask.TaskSnapshot> task, final String downloadUri, byte[] thumbData, String randomName) {
        UploadTask uploadTask = FirebaseUtil.sStorageReference.child("post_images/thumbs")
                .child(randomName + ".jpg").putBytes(thumbData);

        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                String downloadthumbUri = taskSnapshot.getDownloadUrl().toString();
                mFeedItem.setImgUrl(downloadUri);
                mFeedItem.setImgThmb(downloadthumbUri);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String error = task.getException().getMessage();
                Toast.makeText(PublishActivity.this,
                        "Firestore Retrieve Error : " + error, Toast.LENGTH_LONG).show();
            }
        });
    }

    public void saveFeedItem(FeedItem mFeedItem) {
        final Task<DocumentReference> documentReferenceTask = mFirebaseUtil.mFirestore.collection("users")
                .document(mUserId).collection("posts").add(mFeedItem);
        documentReferenceTask.addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if (task.isSuccessful()) {
                    gotoFeed();
                } else {
                    String error = task.getException().getMessage();
                    Toast.makeText(PublishActivity.this, "Firestore Retrieve Error : " + error, Toast.LENGTH_LONG).show();
                }
                mProgressBar.setVisibility(View.INVISIBLE);
            }
            private void gotoFeed() {
                //postId = documentReferenceTask.getResult().getId();
                Intent intent = TabsActivity.newIntent(PublishActivity.this, FEED_FRAG_NO);
                startActivity(intent);
                finish();
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
                        Toast.makeText(PublishActivity.this, "Uploaded", Toast.LENGTH_LONG).show();
                        constraintKeyboardIn.setVisibility(View.INVISIBLE);
                        constraintKeyboard.setVisibility(View.VISIBLE);
                    }
                }, 1000);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(PublishActivity.this, "ImgagePicker " +error, Toast.LENGTH_LONG).show();
            }
        }
    }


    private void enablePublishBtn() {
        content.addTextChangedListener(new TextWatcher() {
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
                boolean isReady = content.getText().toString().length() > 5;
                postBtn.setEnabled(isReady);
                if(isReady) {
                    postBtn.setBackgroundColor(Color.parseColor("#32CD32"));
                    postBtn.setTextColor(Color.parseColor("#ffffff"));
                } else  {
                    postBtn.setBackgroundColor(Color.parseColor("#D3D3D3"));
                    postBtn.setTextColor(Color.parseColor("#000000"));
                }
            }
        });
    }

    private void galleryBtnListener() {
        galleryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // start picker to get image for cropping and then use the image in cropping activity
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(PublishActivity.this);
            }
        });
    }

    private void spinnerOnselect() {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

    private void setupDropDownSpinner() {
        // Create an ArrayAdapter using the string array and a default spinner
        ArrayAdapter<CharSequence> staticAdapter = ArrayAdapter
                .createFromResource(this, R.array.subject_names,
                        android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        staticAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setPrompt("Select related subject");

        // Apply the adapter to the spinner
        spinner.setAdapter(new NothingSelectedSpinnerAdapter(staticAdapter,
                        R.layout.contact_spinner_row_nothing_selected,
                        // R.layout.contact_spinner_nothing_selected_dropdown, // Optional
                        this));
    }

    private void getDPandNameAndSet() {
        mFirebaseUtil.mFirestore.collection("Users").document(mUserId).collection("Setup Details")
                .document("Setup Fields").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    if(task.getResult().exists()) {
                        name = task.getResult().getString("Nick name");
                        username.setText(name);

                        String imageUrl = task.getResult().getString("Image");
                        RequestOptions placeholderRequest = new RequestOptions();
                        placeholderRequest.apply(placeholderRequest).placeholder(R.drawable.default_photo);

                        Glide.with(PublishActivity.this).load(imageUrl).apply(placeholderRequest).into(imageView);
                    }
                } else {
                    String error = task.getException().getMessage();
                    Toast.makeText(PublishActivity.this, "Firestore Retrieve Error : " + error, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void setupToolbar(String publishType) {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle(publishType);
        }
    }

    private void wiringViews() {
        mProgressBar = findViewById(R.id.shareProgressBar);
        content = findViewById(R.id.enterContent);
        username = findViewById(R.id.userNameTView);
        imageView = findViewById(R.id.smallCircleImageView);

        timeofPost = findViewById(R.id.timeofPost);
        timeofPost.setText("Now");

        galleryBtn = findViewById(R.id.gallery_btn);
        loadImage = findViewById(R.id.loadImage);
        constraintKeyboardIn = findViewById(R.id.constraintKeyboardIn);
        constraintKeyboard = findViewById(R.id.constraintKeyboard);
        spinner = findViewById(R.id.spinner);

        postBtn = findViewById(R.id.post_button);
        postBtn.setEnabled(false);
        spinner = findViewById(R.id.spinner);
    }



}
