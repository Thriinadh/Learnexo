package com.learnexo.main;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.learnexo.fragments.FeedFragment;
import com.learnexo.model.feed.FeedItem;
import com.learnexo.model.feed.InterestFeed;
import com.learnexo.model.feed.answer.Answer;
import com.learnexo.model.feed.question.Question;
import com.learnexo.util.FirebaseUtil;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import id.zelory.compressor.Compressor;

public class AnswerActivity extends AppCompatActivity {

    public static final String EXTRA_QUESTION_CONTENT = "com.learnexo.question_content";
    public static final String EXTRA_QUESTION_ID = "com.learnexo.question_id";
    public static final String EXTRA_QUESTION_TAG = "com.learnexo.question_tag";
    public static final String EXTRA_QUESTIONER_ID = "com.learnexo.questioner_id";
    public static final String EXTRA_QUESTION_FROM_ALL= "com.learnexo.question_from_all";


    private ProgressBar mProgressBar;
    private Uri mPublishedImageUri;
    private TextView askedQuestion;
    private TextView title;
    private EditText mAnswerContent;
    private TextView submitTView;
    private ImageView galleryView;
    private RelativeLayout relativeLayoutHide;
    private RelativeLayout relativeLayout;
    private ImageView loadImage;

    private String mPublisherName = FeedFragment.sName;
    String quesContent;
    String questionId;
    String questionTag;
    String questionerId;
    String question;
    int ansType;


    private String mUserId = FirebaseUtil.getCurrentUserId();
    private FirebaseUtil mFirebaseUtil = new FirebaseUtil();

    private Bitmap mBitmap;

    Answer answer=new Answer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);

        initQuesFromIntent();
        wireViews();

        //changeStatusbarColor();


        galleryBtnListener();
        enablePublishBtn();
        submitBtnListener();

    }

    @Override
    public void onBackPressed() {
            final String contentt = mAnswerContent.getText().toString();

            if (mAnswerContent.getText().toString().length() > 3) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Save Or Not");
                builder.setMessage("Do you want to save this? ");
                builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        Map<String, Object> userMap = new HashMap<>();
                        userMap.put("Answer", contentt);
                        // saveResult();
                        mFirebaseUtil.mFirestore.collection("users").document(mUserId).collection("drafts").add(userMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                if(mPublishedImageUri != null)
                                    Toast.makeText(AnswerActivity.this, "Images won't be saved", Toast.LENGTH_SHORT).show();
                                AnswerActivity.super.onBackPressed();
                            }
                        });

                    }
                });
                builder.setNegativeButton("Discard", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        AnswerActivity.super.onBackPressed();
                    }
                });
                builder.show();
            } else AnswerActivity.super.onBackPressed();
    }

    private void changeStatusbarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decor = getWindow().getDecorView();
                decor.setSystemUiVisibility(0);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        }
    }

    private void wireViews() {
        title=findViewById(R.id.textView6);
        if(ansType==FeedItem.CRACK)
            title.setText("Crack");

        mProgressBar = findViewById(R.id.progressbaar);
        mAnswerContent = findViewById(R.id.quesAns);
        submitTView = findViewById(R.id.submitTView);
        galleryView = findViewById(R.id.galleryView);
        relativeLayoutHide = findViewById(R.id.relativeLayoutHide);
        relativeLayout = findViewById(R.id.relativeLayout);
        loadImage = findViewById(R.id.loadImage);
        askedQuestion = findViewById(R.id.questionView);
        askedQuestion.setText(quesContent);
        if(question != null)
        askedQuestion.setText(question);
    }

    private void initQuesFromIntent() {
        Intent intent = getIntent();
        ansType=intent.getIntExtra("ANSWER_TYPE",-1);
        quesContent = intent.getStringExtra(EXTRA_QUESTION_CONTENT);
        questionId = intent.getStringExtra(EXTRA_QUESTION_ID);
        questionTag = intent.getStringExtra(EXTRA_QUESTION_TAG);
        questionerId = intent.getStringExtra(EXTRA_QUESTIONER_ID);
        question = intent.getStringExtra(EXTRA_QUESTION_FROM_ALL);
    }

    public static Intent newIntent(Context context, Question question) {

        Intent intent = new Intent(context, AnswerActivity.class);
        intent.putExtra(EXTRA_QUESTION_CONTENT, question.getContent());
        intent.putExtra(EXTRA_QUESTION_ID,question.getFeedItemId());
        intent.putExtra(EXTRA_QUESTION_TAG,question.getTags().get(0));
        intent.putExtra(EXTRA_QUESTIONER_ID,question.getUserId());
        return intent;
    }
    public static Intent newIntent(Context packageContext,String question) {
        Intent intent = new Intent(packageContext, AnswerActivity.class);
        intent.putExtra(EXTRA_QUESTION_FROM_ALL, question);
        return intent;
    }

    private void enablePublishBtn() {
        mAnswerContent.addTextChangedListener(new TextWatcher() {
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
                boolean isReady = mAnswerContent.getText().toString().length() > 3;
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
                        .start(AnswerActivity.this);
            }
        });
    }

    private void submitBtnListener() {
        submitTView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String answerContent = mAnswerContent.getText().toString();

                if(!TextUtils.isEmpty(answerContent)) {
                    mProgressBar.setVisibility(View.VISIBLE);
                        saveAnswer(answerContent);
                }
        }
    });
    }

    private void saveAnswer(String content) {
            answer.setType(ansType);
            answer.setContent(content);
            answer.setUserId(mUserId);
            answer.setUserName(mPublisherName);
            answer.setQuesId(questionId);
            answer.setQuesContent(quesContent);
            answer.setTags(Collections.singletonList(questionTag));

            if(mPublishedImageUri != null) {
                saveImagetoStorage();
            }else {
                saveAnswer(answer);
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
                        mBitmap = new Compressor(AnswerActivity.this)
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
                                saveAnswer(answer);

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            String error = task.getException().getMessage();
                            Toast.makeText(AnswerActivity.this,
                                    "Firestore Retrieve Error : " + error, Toast.LENGTH_LONG).show();
                        }
                    });

                } else {
                    mProgressBar.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    public void saveAnswer(final Answer answer) {

        final String interestFeedPath="interest_feed";


        final Task<DocumentReference> documentReferenceTask = mFirebaseUtil.mFirestore.collection("users")
                .document(mUserId).collection("answers").add(answer);
        documentReferenceTask.addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if (task.isSuccessful()) {

                    Task<DocumentReference> documentReferenceTask1 = mFirebaseUtil.mFirestore.collection("questions")
                            .document(answer.getQuesId()).collection("answers").add(answer);
                    documentReferenceTask1.addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if(task.isSuccessful()){

                                Task<DocumentSnapshot> documentSnapshotTask =  mFirebaseUtil.mFirestore.collection("users")
                                        .document(questionerId).collection("questions")
                                        .document(answer.getQuesId()).get();


                                documentSnapshotTask.addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if(task.isSuccessful()){
                                            DocumentSnapshot documentSnapshot = task.getResult();
                                            Object noOfAns = documentSnapshot.get("noOfAns");
                                            int noOfAnsw=0;
                                            if(noOfAns!=null)
                                                noOfAnsw = ((Long) noOfAns).intValue()+1;
                                            Map<String, Object> map= new HashMap();
                                            map.put("noOfAns",noOfAnsw);
                                            mFirebaseUtil.mFirestore.collection("questions").document(answer.getQuesId()).update(map);
                                            mFirebaseUtil.mFirestore.collection("users").document(questionerId).collection("questions").
                                                    document(answer.getQuesId()).update(map);

                                            mFirebaseUtil.saveInterestFeedItem(answer, documentReferenceTask, interestFeedPath, answer.getType(),mUserId);
                                            gotoFeed();
                                        }
                                    }
                                });


                            }
                            else {
                                String error = task.getException().getMessage();
                                Toast.makeText(AnswerActivity.this, "Firestore Retrieve Error : " + error, Toast.LENGTH_LONG).show();
                            }
                        }
                    });


                } else {
                    String error = task.getException().getMessage();
                    Toast.makeText(AnswerActivity.this, "Firestore Retrieve Error : " + error, Toast.LENGTH_LONG).show();
                }
                mProgressBar.setVisibility(View.INVISIBLE);
            }
            private void gotoFeed() {
                Intent intent = TabsActivity.newIntent(AnswerActivity.this);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
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
                        Toast.makeText(AnswerActivity.this, "Uploaded", Toast.LENGTH_LONG).show();
                        relativeLayoutHide.setVisibility(View.INVISIBLE);
                        relativeLayout.setVisibility(View.VISIBLE);
                    }
                }, 1000);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(AnswerActivity.this, "ImagePicker " +error, Toast.LENGTH_LONG).show();
            }
        }
    }

}
