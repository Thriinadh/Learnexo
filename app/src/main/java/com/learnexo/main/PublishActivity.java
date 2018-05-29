package com.learnexo.main;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.learnexo.fragments.FeedFragment;
import com.learnexo.model.feed.FeedItem;
import com.learnexo.model.feed.post.Post;
import com.learnexo.model.feed.question.Question;
import com.learnexo.util.FirebaseUtil;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

public class PublishActivity extends AppCompatActivity {

    private static final String TAG = PublishActivity.class.getSimpleName();

    private Uri mPublishedImageUri;
    private ProgressBar mProgressBar;

    private ConstraintLayout constraintKeyboardIn;
    private ConstraintLayout constraintKeyboard;

    private EditText content;
    private ImageView loadImage;

    private TextView postBtn;
    private CircleImageView imageView;
    private TextView username;
    private TextView timeofPost;

    private ImageView galleryBtn;
    private Spinner spinner;
    private String tag;
    private String mPublisherName =FeedFragment.sName;

    private Bitmap mBitmap;

    private String mUserId = FirebaseUtil.getCurrentUserId();
    private FirebaseUtil mFirebaseUtil = new FirebaseUtil();

    Post post;
    Question question;
    String publishType;

    String coontent;

    ArrayAdapter<String> staticAdapter;
    List<String> subjectsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish);
        publishType = getIntent().getStringExtra(TabsActivity.EXTRA_PUBLISH_TYPE);

        wiringViews();
        setupToolbar();

        getDPandNameAndSet();

        setupDropDownSpinner();
        spinnerOnselect();
        galleryBtnListener();
        enablePublishBtn();

        postBtnListener();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        if(publishType.equals(getString(R.string.shareInfo))) {
            final String contentt = content.getText().toString();
            final String tagg = tag;

            if (content.getText().toString().length() > 5) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Save Or Not");
                builder.setMessage("Do you want to save this? ");
                builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        Map<String, Object> userMap = new HashMap<>();
                        userMap.put("content", contentt);
                        userMap.put("tag", tagg);
                        // saveResult();
                        mFirebaseUtil.mFirestore.collection("users").document(mUserId).collection("drafts").add(userMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentReference> task) {
                               if(mPublishedImageUri != null)
                                Toast.makeText(PublishActivity.this, "Images won't be saved", Toast.LENGTH_SHORT).show();
                                PublishActivity.super.onBackPressed();
                            }
                        });

                    }
                });
                builder.setNegativeButton("Discard", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        PublishActivity.super.onBackPressed();
                    }
                });
                builder.show();
            } else PublishActivity.super.onBackPressed();
        } else PublishActivity.super.onBackPressed();
    }

    private void postBtnListener() {
        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String content = PublishActivity.this.content.getText().toString();

                if(!TextUtils.isEmpty(content) && tag != null && !tag.equals("others")) {
                    mProgressBar.setVisibility(View.VISIBLE);

                    if(publishType.equals(getString(R.string.shareInfo))) {
                        post = new Post();
                        prepareFeedItem(content,publishType);
                    } else {
                        question = new Question();
                        if(publishType.equals(getString(R.string.postChallenge))){
                            question.setChallenge(true);
                        }
                        //for both questions and challenges
                        prepareFeedItem(content,publishType);

                    }
                } else if(!TextUtils.isEmpty(content) && tag == null)
                    Toast.makeText(PublishActivity.this, "Tag must not be empty", Toast.LENGTH_SHORT).show();
                else if(!TextUtils.isEmpty(content) && tag.equals("others"))
                    Toast.makeText(PublishActivity.this, "Tag a subject", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void prepareFeedItem(String content, String publishType) {
        if(publishType.equals(getString(R.string.shareInfo))){
            post.setContent(content);
            post.setTags(Collections.singletonList(tag));
            post.setUserId(mUserId);
            post.setUserName(mPublisherName);
            post.setType(FeedItem.POST);

            if(mPublishedImageUri != null) {
                saveImagetoStorage(getString(R.string.post_images));
            }else {
                saveFeedItem(post);
            }

        }else{
            question.setContent(content);
            question.setTags(Collections.singletonList(tag));
            question.setUserId(mUserId);
            question.setUserName(mPublisherName);

            if(publishType.equals(getString(R.string.askYourQuestion))){
                question.setType(FeedItem.QUESTION);
            }else
                question.setType(FeedItem.CHALLENGE);

            if(mPublishedImageUri != null) {
                saveImagetoStorage(getString(R.string.question_images));
            }else {
                saveFeedItem(question);
            }
        }

    }

    private void saveImagetoStorage(final String path) {
        final String randomName = UUID.randomUUID().toString();

        StorageReference filepath = FirebaseUtil.sStorageReference.child(path).child(randomName + ".jpg");

        filepath.putFile(mPublishedImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull final Task<UploadTask.TaskSnapshot> task) {

                final String downloadUri = task.getResult().getDownloadUrl().toString();

                if (task.isSuccessful()) {
                    File newImageFile = new File(mPublishedImageUri.getPath());
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

                    String thumbs= path.concat("/thumbs");
                    UploadTask uploadTask = FirebaseUtil.sStorageReference.child(thumbs)
                            .child(randomName + ".jpg").putBytes(thumbData);

                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            String downloadthumbUri = taskSnapshot.getDownloadUrl().toString();

                            if(path.equals(getString(R.string.post_images))){
                                post.setImgUrl(downloadUri);
                                post.setImgThmb(downloadthumbUri);
                                saveFeedItem(post);
                            }else{
                                question.setImgUrl(downloadUri);
                                question.setImgThmb(downloadthumbUri);
                                saveFeedItem(question);
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            String error = task.getException().getMessage();
                            Toast.makeText(PublishActivity.this,
                                    "Firestore Retrieve Error : " + error, Toast.LENGTH_LONG).show();
                        }
                    });

                } else {
                    mProgressBar.setVisibility(View.INVISIBLE);
                }
            }
        });
    }


    public void saveFeedItem(final FeedItem mFeedItem) {
        final String path;
        if(mFeedItem.getClass()==Post.class){
            path="posts";
        }else{
            path="questions";
        }
        final String interestFeedPath="interest_feed";

        final Task<DocumentReference> documentReferenceTask = mFirebaseUtil.mFirestore.collection("users")
                .document(mUserId).collection(path).add(mFeedItem);

        documentReferenceTask.addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if (task.isSuccessful()) {
                    if(path.equals("questions"))
                        mFirebaseUtil.mFirestore.collection("questions")
                            .document(task.getResult().getId()).set(mFeedItem);

                    mFirebaseUtil.saveInterestFeedItem(mFeedItem, documentReferenceTask, interestFeedPath);


                    mFirebaseUtil.pushFeed(mFeedItem, documentReferenceTask, mUserId);

                    gotoFeed();

                } else {
                    String error = task.getException().getMessage();
                    Toast.makeText(PublishActivity.this, "Firestore Retrieve Error : " + error, Toast.LENGTH_LONG).show();
                }
                mProgressBar.setVisibility(View.INVISIBLE);
            }
            private void gotoFeed() {
                Intent intent = TabsActivity.newIntent(PublishActivity.this);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
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
                mPublishedImageUri = result.getUri();
                loadImage.setImageURI(mPublishedImageUri);

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
                Toast.makeText(PublishActivity.this, "ImagePicker " +error, Toast.LENGTH_LONG).show();
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
                    postBtn.setBackgroundColor(Color.parseColor("#bfbfbf"));
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

                if(!TextUtils.isEmpty(tag) && tag.equals("others")) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(PublishActivity.this);
                    builder.setTitle("Enter subject");
                    builder.setMessage("Do you want to tag this?");

                    final EditText input = new EditText(PublishActivity.this);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);
                    input.setLayoutParams(lp);
                    builder.setView(input);

                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            tag = input.getText().toString();

                            subjectsList.add(tag);
                            staticAdapter.notifyDataSetChanged();
                            spinner.setSelection(subjectsList.size());
                                }
                            });

                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            dialog.cancel();
                        }
                    });
                    builder.show();

                        }

                }


            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //...
            }
        });
    }

    private void setupDropDownSpinner() {

        String subjects[] = {"Department0 Java 0","Relational Database", "Java","Mongo DB","Scala","Python","Ruby", "C sharp","Android", "others"};

        subjectsList = new ArrayList<>(Arrays.asList(subjects));

        staticAdapter = new ArrayAdapter<>
                (this, android.R.layout.simple_spinner_item,
                        subjectsList);

        staticAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setPrompt("Select any subject");


        spinner.setAdapter(new NothingSelectedSpinnerAdapter(staticAdapter,
                        R.layout.contact_spinner_row_nothing_selected, this));


    }

    private void getDPandNameAndSet() {

                        username.setText(FeedFragment.sName);

                        RequestOptions placeholderRequest = new RequestOptions();
                        placeholderRequest.apply(placeholderRequest).placeholder(R.drawable.empty_profilee);

                        Glide.with(getApplicationContext()).load(FeedFragment.sDpUrl).apply(placeholderRequest).into(imageView);

    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(publishType);
        }
    }

    private void wiringViews() {
        mProgressBar = findViewById(R.id.shareProgressBar);
        content = findViewById(R.id.enterContent);
        setHint(content);

        username = findViewById(R.id.userNameTView);
        imageView = findViewById(R.id.smallCircleImageView);

        timeofPost = findViewById(R.id.timeofPost);
        timeofPost.setText("Now");

        galleryBtn = findViewById(R.id.gallery_btn);
        loadImage = findViewById(R.id.loadImage);
        constraintKeyboardIn = findViewById(R.id.constraintKeyboardIn);
        constraintKeyboard = findViewById(R.id.constraintKeyboard);

        postBtn = findViewById(R.id.post_button);
        postBtn.setEnabled(false);
        spinner = findViewById(R.id.spinner);
    }

    private void setHint(EditText content) {

        if(publishType.equals(getString(R.string.shareInfo))) {
            content.setHint(getString(R.string.share_info_hint));

        }else if(publishType.equals(getString(R.string.askYourQuestion))) {
            content.setHint(getString(R.string.ask_question_hint));

        } else {
            content.setHint(getString(R.string.post_challenge_hint));
        }
    }


}
