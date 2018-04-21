package com.learnexo.tabfragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.learnexo.main.PlayVideoActivity;
import com.learnexo.main.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class VideoFragment extends Fragment {

    public VideoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video, container, false);

        List<SubBranch> mSubBranches = new ArrayList<>();
        SubBranch subBranch;

        for (int b = 0; b <= 5; b++) {

            List<Subject> subjects = new ArrayList<>();
            subBranch = new SubBranch();
            subBranch.setName("Programming "+b);

            for (int i = 0; i <= 10; i++) {
                Subject subject = new Subject();
                subject.setName("Java " + i);
                subjects.add(subject);
            }
            subBranch.setSubjectList(subjects);
            mSubBranches.add(subBranch);
        }

        RecyclerView mSubBranchRecyclerview =  view.findViewById(R.id.sub_branch_recyclerview);
        mSubBranchRecyclerview.setHasFixedSize(true);
        SubBranchAdapter mSubBranchAdapter = new SubBranchAdapter(getActivity(),mSubBranches);
        mSubBranchRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mSubBranchRecyclerview.setAdapter(mSubBranchAdapter);

        return view;
    }

    public class SubBranchHolder extends RecyclerView.ViewHolder {
        protected TextView mSubBranchTView;
        protected RecyclerView mSubjectRecycler;

        public SubBranchHolder(View view) {
            super(view);

           mSubBranchTView = view.findViewById(R.id.sub_branch_tview);
           mSubjectRecycler = view.findViewById(R.id.subjectsRecyclerView);
        }
    }

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
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_sub_branch,parent, false);
            return new SubBranchHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final SubBranchHolder holder, int position) {

            SubBranch subBranch =  mSubBranches.get(position);

            List<Subject> subjects = subBranch.getSubjectList();
            SubjectAdapter subjectAdapter = new SubjectAdapter(context, subjects);

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

    public  class SubjectHolder extends RecyclerView.ViewHolder{

        protected Button mSubjectBtn;

        public SubjectHolder(View view) {
            super(view);
            mSubjectBtn = view.findViewById(R.id.subject_btn);

        }

        public void bind(Subject subject) {
            mSubjectBtn.setText(subject.getName());
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

        private Context mContext;
        private List<Subject> mSubjects;

        public SubjectAdapter(Context context, List<Subject> subjects) {
            mContext = context;
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
