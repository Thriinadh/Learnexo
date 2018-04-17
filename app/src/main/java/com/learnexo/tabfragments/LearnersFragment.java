package com.learnexo.tabfragments;


import android.content.Context;
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

import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;
import static com.learnexo.tabfragments.LearnersModel.LINE_TYPE;
import static com.learnexo.tabfragments.LearnersModel.OPTION_TYPE;

/**
 * A simple {@link Fragment} subclass.
 */
public class LearnersFragment extends Fragment {

    public LearnersFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_learners, container, false);

        RecyclerView learnersRecycler = view.findViewById(R.id.learnersRecyclerView);
        learnersRecycler.setHasFixedSize(true);
        DifferentRowAdapter differentRowAdapter = new DifferentRowAdapter(DummyData.getData());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        learnersRecycler.setLayoutManager(layoutManager);
        learnersRecycler.setAdapter(differentRowAdapter);

        // Inflate the layout for this fragment
        return view;
    }

    public class DifferentRowAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private List<LearnersModel> mList;
        public DifferentRowAdapter(List<LearnersModel> list) {
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

            LearnersModel object = mList.get(position);
            if (object != null) {
                switch (object.getmType()) {
                    case OPTION_TYPE:
                        ((LearnersViewHolder) holder).mTitle.setText(object.getmName());

                        String uri="@drawable/connect_icon";
                        if(null != object.getImageName())
                        uri = "@drawable/"+object.getImageName();  // where myresource (without the extension) is the file

                        int imageResource = getResources().getIdentifier(uri, "drawable", getActivity().getPackageName());
                        Drawable res = getResources().getDrawable(imageResource);
                        ((LearnersViewHolder) holder).imageView.setImageDrawable(res);
                        break;
                    case LINE_TYPE:
                        ((LineViewHolder) holder).mLineView.setBackgroundColor(Color.RED);
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
                LearnersModel object = mList.get(position);
                if (object != null) {
                    return object.getmType();
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
