package com.learnexo.main;

import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.learnexo.util.FirebaseUtil;

public class PlayVideoActivity extends AppCompatActivity {

    private VideoView videoView;
    private MediaController mediaControls;
    private String url;
    private FirebaseUtil mFirebaseUtil=new FirebaseUtil();
    private Toolbar mToolbar;
    private ImageView expandText;
    private TextView overviewText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video);

        videoView = findViewById(R.id.videoView);
        mToolbar = findViewById(R.id.video_toolbar);
        setSupportActionBar(mToolbar);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setDisplayShowHomeEnabled(true);
        }

        overviewText = findViewById(R.id.overviewText);

        expandText = findViewById(R.id.expandText);

//        mFirebaseUtil.mFirestore.collection("subjects").document("compilerDesign").collection("chap1")
//                .document("introduction").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//
//                if(task.isSuccessful()) {
//
//                    if(task.getResult().exists()) {
//
//                          url = task.getResult().getString("url");
////                        videoView.setVideoURI(Uri.parse(url));
////                        videoView.start();
//
//                        new BackgroundAsyncTask().execute(url);
//
//                    }
//
//                }
//
//            }
//        });

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            View decorView = getWindow().getDecorView();
            int uiOptions = 0;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
                uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            }
            decorView.setSystemUiVisibility(uiOptions);
        } else {
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_VISIBLE;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public class BackgroundAsyncTask extends AsyncTask<String, Uri, Void> {
        Integer track = 0;
        ProgressDialog dialog;

//        protected void onPreExecute() {
//            dialog = new ProgressDialog(PlayVideoActivity.this);
//            dialog.setMessage("Loading, Please Wait...");
//            dialog.setCancelable(true);
//            dialog.show();
//        }

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
                      //  dialog.dismiss();

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

