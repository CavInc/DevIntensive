package com.softdesign.devintensive.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.network.res.UserListRes;
import com.softdesign.devintensive.ui.viewes.AspectRatioImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserViewHolder> {

    Context mContext;
    List<UserListRes.UserData> mUsers;

    public UsersAdapter(List<UserListRes.UserData> users) {
        mUsers = users;
    }

    @Override
    public UsersAdapter.UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_list,parent,false);
        return new UserViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(UsersAdapter.UserViewHolder holder, int position) {
        UserListRes.UserData user = mUsers.get(position);
        Picasso.with(mContext)
                .load(user.getPublicInfo().getPhoto())
                .placeholder(mContext.getResources().getDrawable(R.drawable.user_bg))
                .error(mContext.getResources().getDrawable(R.drawable.user_bg))
                .into(holder.userPhoto);

        holder.mFullname.setText(String.valueOf(user.getFullName()));
        holder.mRationg.setText(user.getProfileValues().getRaiting());
        holder.mCodeLines.setText(user.getProfileValues().getLineCodes());
        holder.mProject.setText(user.getProfileValues().getProjects());
        if (user.getPublicInfo().getBio()==null || user.getPublicInfo().getBio().isEmpty()){
            holder.mBio.setVisibility(View.GONE);
        }else {
            holder.mBio.setVisibility(View.VISIBLE);
            holder.mBio.setText(user.getPublicInfo().getBio());
        }
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
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
