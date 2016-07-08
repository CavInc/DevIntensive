package com.softdesign.devintensive.ui.activites;


import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;


import com.softdesign.devintensive.R;
import com.softdesign.devintensive.utils.ConstantManager;


public class MainActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG= ConstantManager.TAG_PREFIX+"Main Activity";

    private ImageView mImageView;
    private CoordinatorLayout mCoordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "Create");

        mImageView = (ImageView) findViewById(R.id.call_img);
        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.main_coordinator_container);
      //  mImageView.setOnClickListener(this);

        if (savedInstanceState == null) {
            // актифить прервый раз
        } else {
            // последующие запуски
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

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "Pause");
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
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.call_img:
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
}
