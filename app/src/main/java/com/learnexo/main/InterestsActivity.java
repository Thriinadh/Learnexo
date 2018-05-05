package com.learnexo.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.learnexo.model.video.SubBranch;
import com.learnexo.model.video.Topic;
import com.learnexo.util.FirebaseUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InterestsActivity extends AppCompatActivity {

    private static final String TAG = InterestsActivity.class.getSimpleName();
    private Toolbar setupToolbar;
    private FirebaseUtil mFirebaseUtil=new FirebaseUtil();
    MenuItem nextBtn;
    Map<String,Boolean> interestMap=new HashMap<>();;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_interests);

        setupToolbar = findViewById(R.id.include);
        setSupportActionBar(setupToolbar);
        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle("Choose Interests");
        }

        List<SubBranch> mSubBranches = new ArrayList<>();
        SubBranch subBranch;

        for (int b = 0; b <= 5; b++) {

            List<Topic> subjects = new ArrayList<>();
            subBranch = new SubBranch();
            subBranch.setName("Programming "+b);

            for (int i = 0; i <= 7; i++) {
                Topic topic = new Topic();
                topic.setSubjectName("Branch"+b+" Java " + i);
                subjects.add(topic);
            }
            subBranch.setTopicList(subjects);
            mSubBranches.add(subBranch);
        }

        RecyclerView mSubBranchRecyclerview =  findViewById(R.id.sub_branch_recyclerview);
        mSubBranchRecyclerview.setHasFixedSize(true);
        SubBranchAdapter mSubBranchAdapter = new SubBranchAdapter(this,mSubBranches);
        mSubBranchRecyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mSubBranchRecyclerview.setAdapter(mSubBranchAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_interests_button, menu);
        nextBtn = menu.findItem(R.id.action_button);
        nextBtn.setEnabled(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_button) {
            mFirebaseUtil.mFirestore.collection("users").document(FirebaseUtil.getCurrentUserId()).collection("interests")
                    .document("doc1").set(interestMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Intent setupIntent = new Intent(InterestsActivity.this, SetupActivity.class);
                    startActivity(setupIntent);
                    finish();
                }
            });
            return true;
        }

        return super.onOptionsItemSelected(item);
    }





    ////////////////
    public class SubBranchHolder extends RecyclerView.ViewHolder {
        protected TextView mSubBranchTView;
        protected RecyclerView mSubjectRecycler;

        public SubBranchHolder(View view) {
            super(view);

            mSubBranchTView = view.findViewById(R.id.sub_branch_tview);
            mSubjectRecycler = view.findViewById(R.id.subjectsRecyclerView);
        }
    }

    ////////////////
    public class SubBranchAdapter extends RecyclerView.Adapter<SubBranchHolder>{

        private List<SubBranch> mSubBranches;
        private Context context;

        public SubBranchAdapter(Context context, List<SubBranch> mSubBranches) {
            this.context = context;
            this.mSubBranches = mSubBranches;
        }

        @NonNull
        @Override
        public SubBranchHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_sub_branch_interests,parent, false);
            return new SubBranchHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final SubBranchHolder holder, int position) {

            SubBranch subBranch =  mSubBranches.get(position);

            List<Topic> topicList = subBranch.getTopicList();
            SubjectAdapter subjectAdapter = new SubjectAdapter(context, topicList);

            holder.mSubBranchTView.setText(subBranch.getName());

            holder.mSubjectRecycler.setHasFixedSize(true);
            LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
            holder.mSubjectRecycler.setLayoutManager(layoutManager);
            holder.mSubjectRecycler.setAdapter(subjectAdapter);

            holder.mSubjectRecycler.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
                @Override
                public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                    int action = e.getAction();
                    switch (action) {
                        case MotionEvent.ACTION_MOVE:
                            rv.getParent().requestDisallowInterceptTouchEvent(true);
                            break;
                    }
                    return false;
                }

                @Override
                public void onTouchEvent(RecyclerView rv, MotionEvent e) {

                }

                @Override
                public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

                }
            });

        }

        @Override
        public int getItemCount() {
            return (null != mSubBranches ? mSubBranches.size() : 0);
        }
    }




    ////////////////
    public  class SubjectHolder extends RecyclerView.ViewHolder{

        protected CheckBox mSubjectCheckbox;

        public SubjectHolder(View view) {
            super(view);
            mSubjectCheckbox = view.findViewById(R.id.firstCheckBox);

        }

    }

    ////////////////
    public class SubjectAdapter extends RecyclerView.Adapter<SubjectHolder> {

        private Context mContext;
        private List<Topic> mSubjects;

        public SubjectAdapter(Context context, List<Topic> subjects) {
            mContext = context;
            mSubjects = subjects;
        }
        @NonNull
        @Override
        public SubjectHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_subject_interests, null);
            SubjectHolder subjectHolder = new SubjectHolder(view);
            return subjectHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull final SubjectHolder holder, int position) {

            final Topic subject = mSubjects.get(position);
            holder.mSubjectCheckbox.setText(subject.getSubjectName());
            holder.mSubjectCheckbox.setChecked(subject.isChecked());

            final Animation myAnim = AnimationUtils.loadAnimation(InterestsActivity.this, R.anim.bounce);
            // Use bounce interpolator with amplitude 0.2 and frequency 20
            MyBounceInterpolator interpolator = new MyBounceInterpolator(0.1, 20);
            myAnim.setInterpolator(interpolator);


            holder.mSubjectCheckbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (holder.mSubjectCheckbox.isChecked())
                        holder.mSubjectCheckbox.startAnimation(myAnim);

                    subject.setChecked(holder.mSubjectCheckbox.isChecked());

                    String subjectName = null;
                    if (null != subject)
                        subjectName = subject.getSubjectName();

                    if (null != interestMap&&subjectName!=null) {
                        if (interestMap.containsKey(subjectName))
                            interestMap.remove(subjectName);
                        else
                            interestMap.put(subjectName, true);

                        if (interestMap.size() >= 4) {
                            nextBtn.setEnabled(true);
                        }else{
                            nextBtn.setEnabled(false);
                        }

                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return(null != mSubjects ? mSubjects.size() : 0);
        }
    }


}
