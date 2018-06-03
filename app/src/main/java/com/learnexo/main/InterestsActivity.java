package com.learnexo.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.learnexo.model.video.Branch;
import com.learnexo.model.video.Subject;
import com.learnexo.util.FirebaseUtil;
import com.learnexo.util.MyBounceInterpolator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class InterestsActivity extends AppCompatActivity {

    private static final String TAG = InterestsActivity.class.getSimpleName();
    private Toolbar setupToolbar;
    private FirebaseUtil mFirebaseUtil=new FirebaseUtil();
    MenuItem nextBtn;
    Map<String,Boolean> interestMap=new LinkedHashMap<>();
    BranchAdapter mBranchAdapter;
    final List<Branch> mBranches = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_interests);

        setupToolBar();

        setupBranchRecycler();

        fetchNofityBranches();

    }

    private void setupBranchRecycler() {
        RecyclerView branchRecyclerview =  findViewById(R.id.sub_branch_recyclerview);
        branchRecyclerview.setHasFixedSize(true);
        mBranchAdapter = new BranchAdapter(this, mBranches);
        branchRecyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        branchRecyclerview.setAdapter(mBranchAdapter);
    }

    private void setupToolBar() {
        setupToolbar = findViewById(R.id.include);
        setSupportActionBar(setupToolbar);
        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle("Choose Interests");
        }
    }

    private void fetchNofityBranches() {
        mFirebaseUtil.mFirestore.collection("branches").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            Branch branch;
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();
                for (DocumentSnapshot documentSnapshot : documents) {
                    branch=documentSnapshot.toObject(Branch.class);
                    mBranches.add(branch);
                }
                mBranchAdapter.notifyDataSetChanged();
            }
        });
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
            nextBtn.setEnabled(false);
            mFirebaseUtil.mFirestore.collection("users").document(FirebaseUtil.getCurrentUserId()).collection("interests")
                    .document("doc1").set(interestMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    nextBtn.setEnabled(true);
                    Intent setupIntent = new Intent(InterestsActivity.this, SetupActivity.class);
                    startActivity(setupIntent);
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    nextBtn.setEnabled(true);
                }
            });
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    ////////////////
    public class BranchHolder extends RecyclerView.ViewHolder {
        protected TextView mSubBranchTView;
        protected RecyclerView mSubjectRecycler;

        public BranchHolder(View view) {
            super(view);

            mSubBranchTView = view.findViewById(R.id.sub_branch_tview);
            mSubjectRecycler = view.findViewById(R.id.subjectsRecyclerView);
        }
    }

    ////////////////
    public class BranchAdapter extends RecyclerView.Adapter<BranchHolder>{

        private List<Branch> mBranches;
        private Context context;

        public BranchAdapter(Context context, List<Branch> mBranches) {
            this.context = context;
            this.mBranches = mBranches;
        }

        @NonNull
        @Override
        public BranchHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_sub_branch_interests,parent, false);
            return new BranchHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final BranchHolder holder, int position) {

            Branch branch =  mBranches.get(position);

            Map<String,Subject> stringSubjectMap = branch.getSubjectMap();
            Collection<Subject> subjects = stringSubjectMap.values();

            SubjectAdapter subjectAdapter = new SubjectAdapter(context, new ArrayList(subjects));

            holder.mSubBranchTView.setText(branch.getBranchName());

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
            return (null != mBranches ? mBranches.size() : 0);
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
        private List<Subject> mSubjects;

        public SubjectAdapter(Context context, List<Subject> subjects) {
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

            final Subject subject = mSubjects.get(position);
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

                    String subjectName;
                    subjectName = subject.getSubjectName();

                    if (null != interestMap&&subjectName!=null) {
                        if (interestMap.containsKey(subjectName))
                            interestMap.remove(subjectName);
                        else
                            interestMap.put(subjectName, true);

                        if (interestMap.size() >= 2) {
                            nextBtn.setEnabled(true);
                           View view1 = findViewById(R.id.action_button);
                            if (view1 != null && view1 instanceof TextView) {
                                 view1.setBackgroundColor( Color.parseColor("#6699ff") );
                                 ((TextView) view1).setTextColor(Color.parseColor("#ffffff"));// Make text colour blue
                               // ((TextView) view1).setTextSize(TypedValue.COMPLEX_UNIT_SP, 24); // Increase font size
                            }
                        }else {
                            View view1 = findViewById(R.id.action_button);
                            if (view1 != null && view1 instanceof TextView) {
                                view1.setBackgroundColor(Color.WHITE);
                                ((TextView) view1).setTextColor(Color.LTGRAY);// Make text colour blue
                                // ((TextView) view1).setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
                                nextBtn.setEnabled(false);
                            }
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
