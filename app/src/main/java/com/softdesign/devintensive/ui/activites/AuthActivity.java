package com.softdesign.devintensive.ui.activites;


import android.support.design.widget.CoordinatorLayout;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.softdesign.devintensive.R;

public class AuthActivity extends BaseActivity implements View.OnClickListener {

    private Button mSignIn;
    private TextView mRemembderPassword;
    private EditText mLogin,mPassword;
    private CoordinatorLayout mCoordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        mSignIn = (Button) findViewById(R.id.login_btn);
        mRemembderPassword = (TextView) findViewById(R.id.remember_txt);
        mLogin = (EditText) findViewById(R.id.login_email_et);
        mPassword = (EditText) findViewById(R.id.login_passw_et);

        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.login_coordinator_container);

        mRemembderPassword.setOnClickListener(this);
        mSignIn.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.login_btn:
                break;
            case R.id.remember_txt:
                break;

        }

    }
}
