package com.softdesign.devintensive.ui.activites;


import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.CoordinatorLayout;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.data.network.req.UserLoginReq;
import com.softdesign.devintensive.data.network.res.UserModelRes;
import com.softdesign.devintensive.utils.NetworkStatusChecker;

import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthActivity extends BaseActivity implements View.OnClickListener {

    private Button mSignIn;
    private TextView mRemembderPassword;
    private EditText mLogin,mPassword;
    private CoordinatorLayout mCoordinatorLayout;

    private DataManager mDatamanager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        mDatamanager = DataManager.getInstance();

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
                signIn();
                break;
            case R.id.remember_txt:
                rememberPassword();
                break;

        }

    }

    private void showSnackbar(String message) {
        Snackbar.make(mCoordinatorLayout,message,Snackbar.LENGTH_LONG).show();
    }

    private void rememberPassword(){
        Intent rememberIdent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://devintensive.softdesign-apps.ru/forgotpass"));
        startActivity(rememberIdent);

    }
    private void loginSuccess(UserModelRes userModel){
        showSnackbar("ВХОД "+userModel.getData().getToken());
        mDatamanager.getPreferensManager().saveAuthToken(userModel.getData().getToken());
        mDatamanager.getPreferensManager().saveUserId(userModel.getData().getUser().getId());
        saveUserValues(userModel);
        Intent loginIntent = new Intent(this,MainActivity.class);
        startActivity(loginIntent);
    }

    private void signIn(){
        if (NetworkStatusChecker.isNetworkAvailable(this)) {
            Call<UserModelRes> call = mDatamanager.loginUser(new UserLoginReq(mLogin.getText().toString(), mPassword.getText().toString()));
            call.enqueue(new Callback<UserModelRes>() {
                @Override
                public void onResponse(Call<UserModelRes> call, Response<UserModelRes> response) {
                    if (response.code() == 200) {
                        loginSuccess(response.body());
                    } else if (response.code() == 404) {
                        showSnackbar("Неверный логи или пароль");
                    } else {
                        showSnackbar("Ошибка с кодом " + Integer.toString(response.code()));
                    }
                }

                @Override
                public void onFailure(Call<UserModelRes> call, Throwable t) {

                }
            });
        }else {
            showSnackbar("Сеть не доступна");
        }

    }
    private void saveUserValues(UserModelRes userModel){
        int[] userValues = {
                userModel.getData().getUser().getProfileValues().getRaiting(),
                userModel.getData().getUser().getProfileValues().getLineCodes(),
                userModel.getData().getUser().getProfileValues().getProjects()
        };
        mDatamanager.getPreferensManager().saveUserProfileValues(userValues);

    }
}
