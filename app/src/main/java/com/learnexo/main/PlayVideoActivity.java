package com.learnexo.main;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.learnexo.fragments.ExpandableListAdapter;
import com.learnexo.model.video.Subject;
import com.learnexo.model.video.VideoLesson;
import com.learnexo.model.video.chapter.Chapter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PlayVideoActivity extends AppCompatActivity {

    private VideoView videoView;
    private Toolbar mToolbar;

    private ImageView playBtn;
    private TextView currentDuration;
    private ProgressBar videoProgress;
    private ProgressBar progressBarCir;
    private TextView totalDuration;
    private RelativeLayout mediaControlsRelative;
    private RelativeLayout progressRelative;

    private Uri videoUri;

    private boolean isPlaying = false;

    private int current = 0;
    private int duration = 1;

    Handler handler;

    ExpandableListAdapter listAdapter;
    NonScrollExpandableListView expListView;
    List<String> listDataHeader=new ArrayList<>();
    Map<String, List<String>> listDataChild=new LinkedHashMap<>();

    private NestedScrollView nestedScroll;
    private boolean isDestroyed;
    private boolean isPaused;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video);

        mToolbar = findViewById(R.id.video_toolbar);
        setSupportActionBar(mToolbar);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setDisplayShowHomeEnabled(true);
            supportActionBar.setTitle("");
        }

        videoView = findViewById(R.id.videoView);
        playBtn = findViewById(R.id.pause);
        currentDuration = findViewById(R.id.currentDuration);
        videoProgress = findViewById(R.id.videoProgress);
        progressBarCir = findViewById(R.id.progressBarHor);
        totalDuration = findViewById(R.id.totalDuration);
        mediaControlsRelative = findViewById(R.id.mediaControlsRelative);
        progressRelative = findViewById(R.id.progressRelative);
        videoProgress.setMax(100);

        playBtn.setImageResource(R.drawable.ic_outline_pause_circle_outline);
        playBtn.setColorFilter(Color.WHITE);

        videoUri = Uri.parse("https://firebasestorage.googleapis.com/v0/b/authentication-5ca13.appspot.com/o/Subject_videos%2FVID-20160108-WA0001.mp4?alt=media&token=5eb183ee-eaac-49c4-987d-76837b95e719");

        videoView.setVideoURI(videoUri);
        videoView.requestFocus();

        mediaControlsRelative.setVisibility(View.INVISIBLE);
        progressRelative.setVisibility(View.INVISIBLE);
        mToolbar.setVisibility(View.INVISIBLE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            videoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                @Override
                public boolean onInfo(MediaPlayer mediaPlayer, int i, int i1) {

                    if (i == MediaPlayer.MEDIA_INFO_BUFFERING_START) {

                        progressBarCir.setVisibility(View.VISIBLE);
                        mediaControlsRelative.setVisibility(View.INVISIBLE);
                        progressRelative.setVisibility(View.INVISIBLE);

                    } else if (i == MediaPlayer.MEDIA_INFO_BUFFERING_END) {

                        progressBarCir.setVisibility(View.INVISIBLE);
                        mediaControlsRelative.setVisibility(View.VISIBLE);
                        progressRelative.setVisibility(View.VISIBLE);
                        mToolbar.setVisibility(View.VISIBLE);

                        handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mediaControlsRelative.setVisibility(View.INVISIBLE);
                                progressRelative.setVisibility(View.INVISIBLE);
                                mToolbar.setVisibility(View.INVISIBLE);
                            }
                        }, 2000);

                    }

                    return false;
                }
            });
        }

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                duration = mediaPlayer.getDuration()/1000;
                String durationString = String.format("%02d:%02d", duration/60, duration%60);
                totalDuration.setText(durationString);
            }
        });

        videoView.start();
        isPlaying = true; //set it only after video starts

        new VideoProgress().execute();

        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPlaying) {
                    videoView.pause();
                    isPlaying = false;
                    isPaused=true;
                    playBtn.setImageResource(R.drawable.ic_outline_play_circle_outline_24px);
                    playBtn.setColorFilter(Color.WHITE);
                    mediaControlsRelative.setVisibility(View.VISIBLE);
                    progressRelative.setVisibility(View.INVISIBLE);

                } else {
                    isPaused=false;
                    videoView.start();
                    isPlaying = true;
                    playBtn.setImageResource(R.drawable.ic_outline_pause_circle_outline);
                    playBtn.setColorFilter(Color.WHITE);
                    progressRelative.setVisibility(View.VISIBLE);

                    handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mediaControlsRelative.setVisibility(View.INVISIBLE);
                            progressRelative.setVisibility(View.INVISIBLE);
                        }
                    }, 2000);

                }
            }
        });

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mediaControlsRelative.setVisibility(View.VISIBLE);
                progressRelative.setVisibility(View.VISIBLE);
                mToolbar.setVisibility(View.VISIBLE);
            }
        });


        videoView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if (!isPlaying) {
                    mediaControlsRelative.setVisibility(View.VISIBLE);
                    progressRelative.setVisibility(View.VISIBLE);
                    handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressRelative.setVisibility(View.INVISIBLE);
                        }
                    }, 2000);
                } else {
                    progressRelative.setVisibility(View.VISIBLE);
                    mediaControlsRelative.setVisibility(View.VISIBLE);
                    mToolbar.setVisibility(View.VISIBLE);
                    handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() { mediaControlsRelative.setVisibility(View.VISIBLE);
                            progressRelative.setVisibility(View.INVISIBLE);
                            mediaControlsRelative.setVisibility(View.INVISIBLE);
                            mToolbar.setVisibility(View.INVISIBLE);
                        }
                    }, 2000);
                }

                return false;
            }
        });



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

    }

    @Override
    protected void onStop() {
        super.onStop();
        isPlaying = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isDestroyed=true;
    }

    public class VideoProgress extends AsyncTask<Void, Object, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            while (true){
                if(!isPaused&&!isDestroyed&&isPlaying)
                    current = videoView.getCurrentPosition() / 1000;
                    int currentPercent = current*100/duration;
                    String currentString = String.format("%02d:%02d", current / 60, current % 60);
                    publishProgress(currentPercent, currentString);
                    if(videoProgress.getProgress() ==100 ||isDestroyed)
                        break;
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Object... values) {
            super.onProgressUpdate(values);

            try {
                videoProgress.setProgress((Integer) values[0]);
                currentDuration.setText((String)values[1]);
            } catch (Exception e) {
                //Error Handling here...
                Toast.makeText(PlayVideoActivity.this, "Bazinga", Toast.LENGTH_SHORT).show();
            }

        }
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
        inflater.inflate(R.menu.menu_other_profile, menu);
        return super.onCreateOptionsMenu(menu);
    }

}
