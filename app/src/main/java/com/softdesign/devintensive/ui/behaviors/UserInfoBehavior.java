package com.softdesign.devintensive.ui.behaviors;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.utils.UIHelper;

public class UserInfoBehavior <V extends LinearLayout> extends AppBarLayout.ScrollingViewBehavior {
    private final int mMaxAppBarHeight;
    private final int mMinAppBarHeight;
    private final int mMaxUserInfoHeight;
    private final int mMinUserInfoHeight;

    public UserInfoBehavior(Context context, AttributeSet attrs){
        super(context,attrs);
        TypedArray a=context.obtainStyledAttributes(attrs, R.styleable.UserInfoBehavior);
        mMinUserInfoHeight = a.getDimensionPixelSize(R.styleable.UserInfoBehavior_begavior_min_height,56);
        a.recycle();

        mMinAppBarHeight = UIHelper.getActionBarHeight()*UIHelper.getStatusBarHeight();
        mMaxAppBarHeight = context.getResources().getDimensionPixelSize(R.dimen.profile_image_size); // 256dp
        mMaxUserInfoHeight = context.getResources().getDimensionPixelSize(R.dimen.user_info_size); // 112dp
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        float currentFriction = UIHelper.currentFriction(mMinAppBarHeight,mMaxAppBarHeight,dependency.getBottom());
        int currentHeight = UIHelper.lerp(mMinUserInfoHeight,mMinAppBarHeight,currentFriction);

        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
        lp.height = currentHeight;
        child.setLayoutParams(lp);

        return super.onDependentViewChanged(parent, child, dependency);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        return dependency instanceof AppBarLayout;
    }
}
