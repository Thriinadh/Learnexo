package com.learnexo.main;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.learnexo.fragments.ExpandableListAdapter;
import com.learnexo.model.video.Subject;
import com.learnexo.model.video.VideoLesson;
import com.learnexo.model.video.chapter.Chapter;
import com.learnexo.util.FirebaseUtil;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PlayVideoActivity extends AppCompatActivity {

    private VideoView videoView;
    private MediaController mediaControls;
    private String url;
    private FirebaseUtil mFirebaseUtil=new FirebaseUtil();
    private Toolbar mToolbar;
    private ImageView expandText;
    private TextView overviewText;

    ExpandableListAdapter listAdapter;
    NonScrollExpandableListView expListView;
    List<String> listDataHeader=new ArrayList<>();
    Map<String, List<String>> listDataChild=new LinkedHashMap<>();



    private NestedScrollView nestedScroll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video);
        Intent intent= getIntent();

        Subject subject = (Subject) intent.getSerializableExtra("EXTRA_EXTRA_SUBJECT");

        Map<String, Chapter> chapterMap = subject.getChapterMap();

        List<Chapter> chpterList=new ArrayList(chapterMap.values());

        List<String> chapterVideos;
        for (Chapter chapter : chpterList){
            String chapterName = chapter.getChapterName();
            listDataHeader.add(chapterName);

            Map<String, VideoLesson> videoLessonMap = chapter.getVideoLessonMap();
            List<VideoLesson> videoLessonList=null;
            if(videoLessonMap!=null) {
                videoLessonList = new ArrayList(videoLessonMap.values());
                chapterVideos = new ArrayList<>();

                for (VideoLesson videoLesson : videoLessonList) {
                    chapterVideos.add(videoLesson.getVideoName());
                }

                listDataChild.put(chapterName, chapterVideos);
            }
        }


        Toast.makeText(PlayVideoActivity.this, subject.getSubjectName(), Toast.LENGTH_SHORT).show();

        videoView = findViewById(R.id.videoView);
        mToolbar = findViewById(R.id.video_toolbar);
        setSupportActionBar(mToolbar);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setDisplayShowHomeEnabled(true);
            supportActionBar.setTitle("");
        }

        nestedScroll = findViewById(R.id.nestedScroll);
        if(nestedScroll!=null)
            nestedScroll.setNestedScrollingEnabled(true);

        expListView = findViewById(R.id.ExpListView);
        listAdapter = new ExpandableListAdapter(PlayVideoActivity.this, listDataHeader, listDataChild);

        if(expListView != null) {
            // setting list adapter
            expListView.setAdapter(listAdapter);

            for (int i = 0; i < listAdapter.getGroupCount(); i++)
                expListView.expandGroup(i);
        }


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
//                          videoView.setVideoURI(Uri.parse(url));
//                          videoView.start();
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

