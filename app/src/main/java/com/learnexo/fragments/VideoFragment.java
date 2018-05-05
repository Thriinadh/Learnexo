package com.learnexo.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.learnexo.main.PlayVideoActivity;
import com.learnexo.main.R;
import com.learnexo.model.video.Branch;
import com.learnexo.model.video.Subject;

import java.util.ArrayList;
import java.util.List;

public class VideoFragment extends Fragment {

    public VideoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video, container, false);

        List<Branch> branches = new ArrayList<>();
        Branch branch;

        for (int b = 0; b <= 5; b++) {

            List<Subject> subjects = new ArrayList<>();
            branch = new Branch();
            branch.setName("Programming "+b);

            for (int i = 0; i <= 10; i++) {
                Subject subject = new Subject();
                subject.setSubjectName("Java " + i);
                subjects.add(subject);
            }
            branch.setSubjects(subjects);
            branches.add(branch);
        }

        RecyclerView branchRecycler =  view.findViewById(R.id.sub_branch_recyclerview);
        branchRecycler.setHasFixedSize(true);

        BranchAdapter branchAdapter = new BranchAdapter(getActivity(), branches);
        branchRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        branchRecycler.setAdapter(branchAdapter);

        return view;
    }

    public class BranchHolder extends RecyclerView.ViewHolder {
        protected TextView mBranchTView;
        protected RecyclerView mSubjectRecycler;

        public BranchHolder(View view) {
            super(view);

           mBranchTView = view.findViewById(R.id.sub_branch_tview);
           mSubjectRecycler = view.findViewById(R.id.subjectsRecyclerView);
        }
    }

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
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_sub_branch,parent, false);
            return new BranchHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final BranchHolder holder, int position) {

            Branch branch =  mBranches.get(position);

            List<Subject> subjects = branch.getSubjects();
            SubjectAdapter subjectAdapter = new SubjectAdapter(subjects);

            holder.mBranchTView.setText(branch.getName());

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

    public  class SubjectHolder extends RecyclerView.ViewHolder{

        protected Button mSubjectBtn;

        public SubjectHolder(View view) {
            super(view);
            mSubjectBtn = view.findViewById(R.id.subject_btn);

        }

        public void bind(Subject subject) {
            mSubjectBtn.setText(subject.getSubjectName());
            mSubjectBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getActivity(), "ButtonClicked", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getActivity(), PlayVideoActivity.class);
                    startActivity(intent);
                }
            });
        }

    }

    public class SubjectAdapter extends RecyclerView.Adapter<SubjectHolder> {
        private List<Subject> mSubjects;

        public SubjectAdapter(List<Subject> subjects) {
            mSubjects = subjects;
        }
        @NonNull
        @Override
        public SubjectHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_subject, null);
            SubjectHolder subjectHolder = new SubjectHolder(view);
            return subjectHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull SubjectHolder holder, int position) {
            Subject subject = mSubjects.get(position);
            holder.bind(subject);
        }

        @Override
        public int getItemCount() {
            return(null != mSubjects ? mSubjects.size() : 0);
        }
    }

}
