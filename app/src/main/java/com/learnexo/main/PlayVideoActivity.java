package com.learnexo.main;

import android.app.ProgressDialog;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.Toast;
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

    private LinearLayout layoutBottomSheet;
    private RelativeLayout btmSheetHideRelative;
    BottomSheetBehavior sheetBehavior;
    private CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video);

        videoView = findViewById(R.id.videoView);
        layoutBottomSheet = findViewById(R.id.bottom_sheet);
        sheetBehavior = BottomSheetBehavior.from(layoutBottomSheet);
        btmSheetHideRelative = findViewById(R.id.btmSheetHideRelative);

        mFirebaseUtil.mFirestore.collection("subjects").document("compilerDesign").collection("chap1")
                .document("introduction").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
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

        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = null;
        if (wm != null) {
            display = wm.getDefaultDisplay();
        }
        DisplayMetrics metrics = new DisplayMetrics();
        if (display != null) {
            display.getMetrics(metrics);
        }
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

//        coordinatorLayout = findViewById(R.id.coordinator_layout);
//
//        int videoVieww = videoView.getHeight();
//
//        layoutBottomSheet.getLayoutParams().height = layoutBottomSheet.getHeight() - videoVieww;
//        layoutBottomSheet.requestLayout();
//        sheetBehavior.onLayoutChild(coordinatorLayout, layoutBottomSheet, ViewCompat.LAYOUT_DIRECTION_LTR);


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


        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED: {
                       // btnBottomSheet.setText("Close Sheet");
                    }
                    break;
                    case BottomSheetBehavior.STATE_COLLAPSED: {
                      //  btnBottomSheet.setText("Expand Sheet");
                    }
                    break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        }, 2000);

        btmSheetHideRelative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

                } else {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

                }
            }
        });

    }

//    private static String getScreenResolution(Context context)
//    {
//        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
//        Display display = wm.getDefaultDisplay();
//        DisplayMetrics metrics = new DisplayMetrics();
//        display.getMetrics(metrics);
//        int width = metrics.widthPixels;
//        int height = metrics.heightPixels;
//
//        return "{" + width + "," + height + "}";
//    }

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

