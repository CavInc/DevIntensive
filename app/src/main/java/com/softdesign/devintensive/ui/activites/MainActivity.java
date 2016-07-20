package com.softdesign.devintensive.ui.activites;


import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.support.v7.widget.Toolbar;


import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.utils.ConstantManager;
import com.softdesign.devintensive.utils.RoundedBitmap;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG= ConstantManager.TAG_PREFIX+"Main Activity";

    private int mCurrentEditMode = 0 ;
    private DataManager mDataManager;

    private ImageView mImageView;
    private CoordinatorLayout mCoordinatorLayout;
    private Toolbar mToolbar;
    private DrawerLayout mNavigationDrawer;
    private FloatingActionButton mFab;
    private EditText mUserPhone,mUserMail,mUserVk,mUserGit,mUserBio;

    private List<EditText> mUserInfoViews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "Create");

        mDataManager = DataManager.getInstance();

        mImageView = (ImageView) findViewById(R.id.call_img);
        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.main_coordinator_container);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mNavigationDrawer = (DrawerLayout) findViewById(R.id.navigation_drawer);
        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mUserPhone = (EditText) findViewById(R.id.phone_et);
        mUserMail = (EditText) findViewById(R.id.email_et);
        mUserVk = (EditText) findViewById(R.id.profile_et);
        mUserGit = (EditText) findViewById(R.id.github_et);
        mUserBio = (EditText) findViewById(R.id.about_et);

        mUserInfoViews = new ArrayList<>();
        mUserInfoViews.add(mUserPhone);
        mUserInfoViews.add(mUserMail);
        mUserInfoViews.add(mUserVk);
        mUserInfoViews.add(mUserGit);
        mUserInfoViews.add(mUserBio);


        mFab.setOnClickListener(this);
        setupToolbar();
        setupDrower();
        loadUserInfoValue();


        if (savedInstanceState == null) {
            // актифить прервый раз
        } else {
            // последующие запуски
            mCurrentEditMode = savedInstanceState.getInt(ConstantManager.EDIT_MODE_KEY,0);
            changeEditMode(mCurrentEditMode);
        }
    }

    /**
     *
     */
    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG,"Stop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    /**
     * Сохраняет состояние активити
     * @param outState
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSaveInstanceState");
        outState.putInt(ConstantManager.EDIT_MODE_KEY,mCurrentEditMode);

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "Pause");
        saveUserInfoValue();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "Resume");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "Start");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "Restart");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            mNavigationDrawer.openDrawer(GravityCompat.START);
            setNavigationDrawerIcon();
        }
        return super.onOptionsItemSelected(item);
    }

    // перехватываем кнопку назад и закрываем NavigationDrawer если
    // он открыт
    @Override
    public void onBackPressed() {
        Log.d(TAG,"BACK BUTTON");
        if (mNavigationDrawer.isDrawerOpen(GravityCompat.START)){
            mNavigationDrawer.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab:
                if (mCurrentEditMode==0) {
                    changeEditMode(1);
                    mCurrentEditMode=1;
                } else {
                    changeEditMode(0);
                    mCurrentEditMode=0;
                }
              //  showProgress();
              //  runWihtDelay();
                break;
        }

    }

    private void runWihtDelay(){
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                hideProgress();
            }
        },3000);
    }

    private void showSnackbar(String message) {
        Snackbar.make(mCoordinatorLayout,message,Snackbar.LENGTH_LONG).show();
    }

    private void setupToolbar(){
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar!=null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);

        }
    }
    private void setupDrower(){
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                showSnackbar(item.getTitle().toString());
                item.setChecked(true);
                mNavigationDrawer.closeDrawer(GravityCompat.START);
                return false;
            }
        });
    }

    private void changeEditMode (int mode){
        if (mode==1){
            mFab.setImageResource(R.drawable.ic_done_black_24dp); // меняем иконку
            for (EditText userValue:mUserInfoViews){
                userValue.setEnabled(true);
                userValue.setFocusable(true);
                userValue.setFocusableInTouchMode(true); // фокус по косанию
            }
        }else {
            mFab.setImageResource(R.drawable.ic_mode_edit_black_24dp); // меняем иконку
            for (EditText userValue:mUserInfoViews){
                userValue.setEnabled(false);
                userValue.setFocusable(false);
                userValue.setFocusableInTouchMode(false);
                saveUserInfoValue();
            }
        }

    }

    private void loadUserInfoValue(){
        List<String> userData = mDataManager.getPreferensManager().loadUserProfileData();
        for (int i=0;i<userData.size();i++){
            mUserInfoViews.get(i).setText(userData.get(i));
        }
    }

    private void saveUserInfoValue(){
        List<String> userData = new ArrayList<>();
        for (EditText userFieldView : mUserInfoViews) {
            userData.add(userFieldView.getText().toString());
        }
        mDataManager.getPreferensManager().saveUserProfileData(userData);
    }

    private void setNavigationDrawerIcon(){
        ImageView imageView=(ImageView) findViewById(R.id.user_photo_drawer_igm);
        //imageView.setImageResource(R.drawable.userphoto);
        imageView.setImageBitmap(new RoundedBitmap(
                BitmapFactory.decodeResource(this.getResources(), R.drawable.userphoto))
                .getRoundedBitmap());

    }
}
