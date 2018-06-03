package com.learnexo.fragments;


import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.learnexo.main.R;
import com.learnexo.model.connect.ConnectOptions;
import com.learnexo.util.DummyData;

import java.util.List;

import static com.learnexo.model.connect.ConnectOptions.LINE_TYPE;
import static com.learnexo.model.connect.ConnectOptions.OPTION_TYPE;

public class MentorsFragment extends Fragment {


    public MentorsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mentors, container, false);

        RecyclerView learnersRecycler = view.findViewById(R.id.mentorsRecyclerView);
        learnersRecycler.setHasFixedSize(true);
        DifferentRowAdapter differentRowAdapter = new DifferentRowAdapter(DummyData.getMentorOptions());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        learnersRecycler.setLayoutManager(layoutManager);
        learnersRecycler.setAdapter(differentRowAdapter);

        // Inflate the layout for this fragment
        return view;
    }

    public class DifferentRowAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private List<ConnectOptions> mList;
        public DifferentRowAdapter(List<ConnectOptions> list) {
            this.mList = list;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view;
            switch (viewType) {
                case OPTION_TYPE:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.learners_items, parent, false);
                    return new LearnersViewHolder(view);
                case LINE_TYPE:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.line_separator, parent, false);
                    return new LineViewHolder(view);
            }
            return null;
        }
        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

            ConnectOptions object = mList.get(position);
            if (object != null) {
                switch (object.getmOptionType()) {
                    case OPTION_TYPE:
                        ((LearnersViewHolder) holder).mTitle.setText(object.getmOption());

                        String uri="@drawable/connect_icon";
                        if(null != object.getmOptionIcon())
                            uri = "@drawable/"+object.getmOptionIcon();  // where myresource (without the extension) is the file

                        int imageResource = getResources().getIdentifier(uri, "drawable", getActivity().getPackageName());
                        Drawable res = getResources().getDrawable(imageResource);
                        ((LearnersViewHolder) holder).imageView.setImageDrawable(res);
                        break;
                    case LINE_TYPE:
                        ((LineViewHolder) holder).mLineView.setBackgroundColor(Color.parseColor("#1da1f2"));
                        break;
                }
            }
        }
        @Override
        public int getItemCount() {
            if (mList == null)
                return 0;
            return mList.size();
        }
        @Override
        public int getItemViewType(int position) {
            if (mList != null) {
                ConnectOptions object = mList.get(position);
                if (object != null) {
                    return object.getmOptionType();
                }
            }
            return 0;
        }
        public class LearnersViewHolder extends RecyclerView.ViewHolder {
            private TextView mTitle;
            private ImageView imageView;
            // private View mLineView;
            public LearnersViewHolder(View itemView) {
                super(itemView);
                mTitle = itemView.findViewById(R.id.titleTextView);
                imageView = itemView.findViewById(R.id.iconImage);
                // mLineView = itemView.findViewById(R.id.lineSeparator);
            }
        }
        public class LineViewHolder extends RecyclerView.ViewHolder {
            private View mLineView;
            public LineViewHolder(View itemView) {
                super(itemView);
                mLineView = itemView.findViewById(R.id.lineSeparator);
            }
        }
    }

}
