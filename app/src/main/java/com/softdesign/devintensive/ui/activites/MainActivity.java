package com.softdesign.devintensive.ui.activites;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.utils.ConstantManager;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG= ConstantManager.TAG_PREFIX+"Main Activity";

    private EditText mEditText;
    private Button mRedButton,mGreenButton;
    private int mColorMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "Create");

        mRedButton = (Button) findViewById(R.id.red_btn);
        mGreenButton = (Button) findViewById(R.id.green_btn);
        mEditText = (EditText) findViewById(R.id.editText);

        mRedButton.setOnClickListener(this);
        mGreenButton.setOnClickListener(this);

        if (savedInstanceState == null) {
            // актифить прервый раз
        } else {
            // последующие запуски
            mColorMode = savedInstanceState.getInt(ConstantManager.COLOR_MODE_KEY);
            mEditText.setBackgroundColor(mColorMode);
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

        outState.putInt(ConstantManager.COLOR_MODE_KEY,mColorMode);
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
            case R.id.red_btn:
                mEditText.setBackgroundColor(Color.RED);
                mColorMode = Color.RED;
                break;
            case R.id.green_btn:
                mEditText.setBackgroundColor(Color.GREEN);
                mColorMode = Color.GREEN;
                break;
        }

    }
}
