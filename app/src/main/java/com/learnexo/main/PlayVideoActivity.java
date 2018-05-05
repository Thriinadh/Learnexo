package com.learnexo.main;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.learnexo.util.FirebaseUtil;

public class PlayVideoActivity extends AppCompatActivity {

    private VideoView videoView;
    private MediaController mediaControls;
    private String url;
    private FirebaseUtil mFirebaseUtil=new FirebaseUtil();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video);

        videoView = findViewById(R.id.videoView);


        mFirebaseUtil.mFirestore.collection("Subjects").document("Compiler Design").collection("chap1")
                .document("Introduction").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if(task.isSuccessful()) {

                    if(task.getResult().exists()) {

                          url = task.getResult().getString("url");
//                        videoView.setVideoURI(Uri.parse(url));
//                        videoView.start();

                        new BackgroundAsyncTask().execute(url);

                    }

                }

            }
        });



//        if (mediaControls == null) {
//            // create an object of media controller class
//            mediaControls = new MediaController(PlayVideoActivity.this);
//            mediaControls.setAnchorView(videoView);
//        }
//
//        // set the media controller for video view
//        videoView.setMediaController(mediaControls);

        //Firebase is here

//        // implement on completion listener on video view
//        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//            @Override
//            public void onCompletion(MediaPlayer mp) {
//                Toast.makeText(getApplicationContext(), "Thank You...!!!", Toast.LENGTH_LONG).show();
//                // display a toast when an video is completed
//            }
//        });
//        videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
//            @Override
//            public boolean onError(MediaPlayer mp, int what, int extra) {
//                Toast.makeText(getApplicationContext(), "Oops An Error Occur While Playing Video...!!!", Toast.LENGTH_LONG).show();
//                // display a toast when an error is occured while playing an video
//                return false;
//            }
//        });

    }

    public class BackgroundAsyncTask extends AsyncTask<String, Uri, Void> {
        Integer track = 0;
        ProgressDialog dialog;

        protected void onPreExecute() {
            dialog = new ProgressDialog(PlayVideoActivity.this);
            dialog.setMessage("Loading, Please Wait...");
            dialog.setCancelable(true);
            dialog.show();
        }

        protected void onProgressUpdate(final Uri... uri) {

            try {

                mediaControls = new MediaController(PlayVideoActivity.this);
                mediaControls.setPrevNextListeners(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // next button clicked
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        finish();
                    }
                });

                mediaControls.show(10000);
                videoView.setMediaController(mediaControls);
                videoView.setVideoURI(uri[0]);
                videoView.requestFocus();

                videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

                    @Override
                    public void onPrepared(MediaPlayer mediaPlayer) {
                        videoView.start();
                        dialog.dismiss();

                        mediaPlayer.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                            @Override
                            public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                                /*
                                 * and set its position on screen
                                 */
                                mediaControls.setAnchorView(videoView);
                            }
                        });
                    }
                });

            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                Uri uri = Uri.parse(params[0]);

                publishProgress(uri);
            } catch (Exception e) {
                e.printStackTrace();

            }

            return null;
        }


    }

}

