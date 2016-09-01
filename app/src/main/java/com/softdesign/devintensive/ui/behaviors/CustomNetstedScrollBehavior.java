package com.softdesign.devintensive.ui.behaviors;

import android.content.Context;
import android.provider.Contacts;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.utils.UIHelper;


public class CustomNetstedScrollBehavior extends AppBarLayout.ScrollingViewBehavior {
    private int mMaxAppBarHeight;
    private int mMinAppBarHeight;
    private int mMaxUserInfoHeight;

    public CustomNetstedScrollBehavior(Context context, AttributeSet attrs){
        super(context,attrs);
        mMinAppBarHeight = UIHelper.getActionBarHeight()* UIHelper.getStatusBarHeight();
        mMaxUserInfoHeight = 112; // 112dp
        mMaxAppBarHeight = context.getResources().getDimensionPixelSize(R.dimen.profile_image_size);//256dp
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        float friction = UIHelper.currentFriction(mMinAppBarHeight,mMaxAppBarHeight,dependency.getBottom());
        int transY = UIHelper.lerp(mMaxUserInfoHeight/2,mMaxUserInfoHeight,friction);

        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
        lp.topMargin = transY;
        child.setLayoutParams(lp);

        return super.onDependentViewChanged(parent, child, dependency);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        return dependency instanceof  AppBarLayout;
    }
}
