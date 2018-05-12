package com.learnexo.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.learnexo.fragments.FeedFragment;
import com.learnexo.model.feed.FeedItem;
import com.learnexo.model.feed.InterestFeed;
import com.learnexo.model.feed.answer.Answer;
import com.learnexo.model.feed.post.Post;
import com.learnexo.util.FirebaseUtil;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.UUID;

import id.zelory.compressor.Compressor;

public class QuestionAnswerActivity extends AppCompatActivity {

    private static final int FEED_FRAG_NO = 0;
    public static final String EXTRA_QUESTION = "com.learnexo.questiondata";

    private Uri mPublishedImageUri;
    private TextView askedQuestion;
    private EditText quesAns;
    private TextView submitTView;
    private ImageView galleryView;
    private RelativeLayout relativeLayoutHide;
    private RelativeLayout relativeLayout;
    private ImageView loadImage;

    private String mPublisherName = FeedFragment.sName;

    private String mUserId = FirebaseUtil.getCurrentUserId();
    private FirebaseUtil mFirebaseUtil = new FirebaseUtil();

    private Bitmap mBitmap;

    Answer answer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_answer);

        quesAns = findViewById(R.id.quesAns);
        submitTView = findViewById(R.id.submitTView);
        galleryView = findViewById(R.id.galleryView);
        relativeLayoutHide = findViewById(R.id.relativeLayoutHide);
        relativeLayout = findViewById(R.id.relativeLayout);
        loadImage = findViewById(R.id.loadImage);

        Intent intent = getIntent();
        String quesData = intent.getStringExtra(EXTRA_QUESTION);
        askedQuestion = findViewById(R.id.questionView);
        askedQuestion.setText(quesData);

        galleryBtnListener();
        enablePublishBtn();
        postBtnListener();

    }

    public static Intent newIntent(Context context, String question) {

        Intent intent = new Intent(context, QuestionAnswerActivity.class);
        intent.putExtra(EXTRA_QUESTION, question);
        return intent;
    }

    private void enablePublishBtn() {
        quesAns.addTextChangedListener(new TextWatcher() {
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
                boolean isReady = quesAns.getText().toString().length() > 3;
                submitTView.setEnabled(isReady);
                if(isReady) {
                    submitTView.setEnabled(true);
                } else  {
                    submitTView.setEnabled(false);
                }
            }
        });
    }

    private void galleryBtnListener() {
        galleryView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // start picker to get image for cropping and then use the image in cropping activity
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(QuestionAnswerActivity.this);
            }
        });
    }

    private void postBtnListener() {
        submitTView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String content = quesAns.getText().toString();

                if(!TextUtils.isEmpty(content)) {
                   // mProgressBar.setVisibility(View.VISIBLE);
                        answer = new Answer();
                        saveFeedItem(content);
                }
        }
    });
    }

    private void saveFeedItem(String content) {
            answer.setContent(content);
//            post.setTags(Collections.singletonList(tag));
            answer.setUserId(mUserId);
            answer.setUserName(mPublisherName);
         //   answer.setTags();

            if(mPublishedImageUri != null) {
                saveImagetoStorage();
            }else {
                saveFeedItem(answer);
            }

    }

    private void saveImagetoStorage() {
        final String randomName = UUID.randomUUID().toString();

        StorageReference filepath = FirebaseUtil.sStorageReference.child("answer_images").child(randomName + ".jpg");

        filepath.putFile(mPublishedImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull final Task<UploadTask.TaskSnapshot> task) {

                final String downloadUri = task.getResult().getDownloadUrl().toString();

                if (task.isSuccessful()) {
                    File newImageFile = new File(mPublishedImageUri.getPath());
                    try {
                        mBitmap = new Compressor(QuestionAnswerActivity.this)
                                .setMaxHeight(100)
                                .setMaxWidth(100)
                                .setQuality(2)
                                .compressToBitmap(newImageFile);
                    } catch (IOException e) {
                        Log.e("TAG", Arrays.toString(e.getStackTrace()));
                    }

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] thumbData = baos.toByteArray();

                    String thumbs= "answer_images".concat("/thumbs");
                    UploadTask uploadTask = FirebaseUtil.sStorageReference.child(thumbs)
                            .child(randomName + ".jpg").putBytes(thumbData);

                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            String downloadthumbUri = taskSnapshot.getDownloadUrl().toString();

                                answer.setImgUrl(downloadUri);
                                answer.setImgThmb(downloadthumbUri);
                                saveFeedItem(answer);

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            String error = task.getException().getMessage();
                            Toast.makeText(QuestionAnswerActivity.this,
                                    "Firestore Retrieve Error : " + error, Toast.LENGTH_LONG).show();
                        }
                    });

                } else {
                   // mProgressBar.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    public void saveFeedItem(final FeedItem mFeedItem) {

        final String interestFeedPath="interest_feed";


        final Task<DocumentReference> documentReferenceTask = mFirebaseUtil.mFirestore.collection("users")
                .document(mUserId).collection("answers").add(mFeedItem);
        documentReferenceTask.addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if (task.isSuccessful()) {
                    gotoFeed();

                    saveInterestFeedItem(mFeedItem, documentReferenceTask, interestFeedPath, mFeedItem.getType());

                } else {
                    String error = task.getException().getMessage();
                    Toast.makeText(QuestionAnswerActivity.this, "Firestore Retrieve Error : " + error, Toast.LENGTH_LONG).show();
                }
               // mProgressBar.setVisibility(View.INVISIBLE);
            }
            private void gotoFeed() {
                //postId = documentReferenceTask.getResult().getId();
                Intent intent = TabsActivity.newIntent(QuestionAnswerActivity.this, FEED_FRAG_NO);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
    }

    private void saveInterestFeedItem(FeedItem mFeedItem, Task<DocumentReference> documentReferenceTask, String interestFeedPath, int type) {
        InterestFeed interestFeed=new InterestFeed();

        interestFeed.setInterest(mFeedItem.getTags().get(0));
        interestFeed.setPublisherId(mUserId);
        interestFeed.setFeedType(type);
        interestFeed.setFeedItemId(documentReferenceTask.getResult().getId());

        mFirebaseUtil.mFirestore.collection(interestFeedPath).add(interestFeed);
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
                relativeLayout.setVisibility(View.INVISIBLE);
                relativeLayoutHide.setVisibility(View.VISIBLE);
                mPublishedImageUri = result.getUri();
                loadImage.setImageURI(mPublishedImageUri);

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(QuestionAnswerActivity.this, "Uploaded", Toast.LENGTH_LONG).show();
                        relativeLayoutHide.setVisibility(View.INVISIBLE);
                        relativeLayout.setVisibility(View.VISIBLE);
                    }
                }, 1000);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(QuestionAnswerActivity.this, "ImagePicker " +error, Toast.LENGTH_LONG).show();
            }
        }
    }

}
