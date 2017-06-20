package com.softdesign.devintensive.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.network.res.UserListRes;
import com.softdesign.devintensive.ui.viewes.AspectRatioImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserViewHolder> {

    private Context mContext;
    private List<UserListRes.UserData> mUsers;

    private UserViewHolder.CustomClickListener mCustomClickListener;

    public UsersAdapter(List<UserListRes.UserData> users,UserViewHolder.CustomClickListener customClickListener ) {
        mUsers = users;
        mCustomClickListener = customClickListener;
    }

    @Override
    public UsersAdapter.UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_list,parent,false);
        return new UserViewHolder(convertView,mCustomClickListener);
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
        holder.mRationg.setText(String.valueOf(user.getProfileValues().getRaiting()));
        holder.mCodeLines.setText(String.valueOf(user.getProfileValues().getLineCodes()));
        holder.mProject.setText(String.valueOf(user.getProfileValues().getProjects()));
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

    public static class UserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        protected AspectRatioImageView userPhoto;
        protected TextView mFullname,mRationg,mCodeLines,mProject,mBio;
        protected Button mShowMore;

        private CustomClickListener mListener;

        public UserViewHolder(View itemView,CustomClickListener customClickListener) {
            super(itemView);
            this.mListener = customClickListener;
            userPhoto = (AspectRatioImageView) itemView.findViewById(R.id.user_photo);
            mFullname = (TextView) itemView.findViewById(R.id.user_full_name);
            mRationg = (TextView) itemView.findViewById(R.id.rating_txt);
            mCodeLines = (TextView) itemView.findViewById(R.id.code_lines_txt);
            mProject = (TextView) itemView.findViewById(R.id.project_txt);
            mBio = (TextView) itemView.findViewById(R.id.bio_text);
            mShowMore = (Button) itemView.findViewById(R.id.more_info_btn);

            mShowMore.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if (mListener!=null) {
                mListener.onUserItemClickListener(getAdapterPosition());
            }

        }

        public interface CustomClickListener {
            void onUserItemClickListener(int adapterPosition) ;
        }
    }
}
