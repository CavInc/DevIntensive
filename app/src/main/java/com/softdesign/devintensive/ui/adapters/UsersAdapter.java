package com.softdesign.devintensive.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.ui.viewes.AspectRatioImageView;

import java.util.ArrayList;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserViewHolder> {

    ArrayList<Object> mUsers;

    @Override
    public UsersAdapter.UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_list,parent,false);

        return new UserViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(UsersAdapter.UserViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        protected AspectRatioImageView userPhoto;
        protected TextView mFullname,mRationg,mCodeLines,mProject,mBio;

        public UserViewHolder(View itemView) {
            super(itemView);
            userPhoto = (AspectRatioImageView) itemView.findViewById(R.id.user_photo);
            mFullname = (TextView) itemView.findViewById(R.id.user_full_name);
            mRationg = (TextView) itemView.findViewById(R.id.rating_txt);
            mCodeLines = (TextView) itemView.findViewById(R.id.code_lines_txt);
            mProject = (TextView) itemView.findViewById(R.id.project_txt);
            mBio = (TextView) itemView.findViewById(R.id.bio_text);
        }
    }
}
