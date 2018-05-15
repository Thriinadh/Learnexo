package com.learnexo.main;

import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.learnexo.fragments.ExpandableListAdapter;
import com.learnexo.util.FirebaseUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

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
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    private NestedScrollView nestedScroll;

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
            supportActionBar.setTitle("");
        }

        nestedScroll = findViewById(R.id.nestedScroll);
        nestedScroll.setNestedScrollingEnabled(true);

        // get the listview
        expListView = findViewById(R.id.ExpListView);

        // preparing list data
        prepareListData();

        listAdapter = new ExpandableListAdapter(PlayVideoActivity.this, listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);

        for(int i=0; i < listAdapter.getGroupCount(); i++)
            expListView.expandGroup(i);

//        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
//
//            @Override
//            public boolean onGroupClick(ExpandableListView parent, View v,
//                                        int groupPosition, long id) {
//                setListViewHeight(parent, groupPosition);
//                return false;
//            }
//        });

//
//        ViewCompat.setNestedScrollingEnabled(expListView, true);

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

    private void setListViewHeight(ExpandableListView listView,
                                   int group) {
        ExpandableListAdapter listAdapter = (ExpandableListAdapter) listView.getExpandableListAdapter();
        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(),
                View.MeasureSpec.EXACTLY);
        for (int i = 0; i < listAdapter.getGroupCount(); i++) {
            View groupItem = listAdapter.getGroupView(i, false, null, listView);
            groupItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);

            totalHeight += groupItem.getMeasuredHeight();

            if (((listView.isGroupExpanded(i)) && (i != group))
                    || ((!listView.isGroupExpanded(i)) && (i == group))) {
                for (int j = 0; j < listAdapter.getChildrenCount(i); j++) {
                    View listItem = listAdapter.getChildView(i, j, false, null,
                            listView);
                    listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);

                    totalHeight += listItem.getMeasuredHeight();

                }
            }
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        int height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getGroupCount() - 1));
        if (height < 10)
            height = 200;
        params.height = height;
        listView.setLayoutParams(params);
        listView.requestLayout();
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

    private void prepareListData() {
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();

        // Adding child data
        listDataHeader.add("1. Introduction");
        listDataHeader.add("2. Logic Gates");
        listDataHeader.add("3. Computer Circuits");
        listDataHeader.add("4. Random Access Memory");
        listDataHeader.add("5. Decoders");

        // Adding child data
        List<String> fundamentals = new ArrayList<>();
        fundamentals.add("How does a computer work..?");
        fundamentals.add("What do you want");
        fundamentals.add("Current is wrong");


        List<String> programming = new ArrayList<>();
        programming.add("Ruby on Rails");
        programming.add("Design Patterns");
        programming.add("Scala");

        List<String> databases = new ArrayList<>();
        databases.add("Relational Databases");
        databases.add("Oracle Database");
        databases.add("Microsoft Azure");

        List<String> networking = new ArrayList<>();
        networking.add("Wireless Networking");
        networking.add("CCNA Networking");
        networking.add("Firewalls protection");

        List<String> artificial = new ArrayList<>();
        artificial.add("Machine Learning");
        artificial.add("Game Theory");
        artificial.add("Speech Recognition");
        artificial.add("Robotics");
        artificial.add("Machine Algorithms");

        // Header, Child data
        listDataChild.put(listDataHeader.get(0), fundamentals);
        listDataChild.put(listDataHeader.get(1), programming);
        listDataChild.put(listDataHeader.get(2), databases);
        listDataChild.put(listDataHeader.get(3), networking);
        listDataChild.put(listDataHeader.get(4), artificial);
    }

}

