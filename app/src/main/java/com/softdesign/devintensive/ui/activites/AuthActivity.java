package com.softdesign.devintensive.ui.activites;


import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.CoordinatorLayout;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.data.network.req.UserLoginReq;
import com.softdesign.devintensive.data.network.res.UserListRes;
import com.softdesign.devintensive.data.network.res.UserModelRes;
import com.softdesign.devintensive.data.storage.models.Repository;
import com.softdesign.devintensive.data.storage.models.RepositoryDao;
import com.softdesign.devintensive.data.storage.models.User;
import com.softdesign.devintensive.data.storage.models.UserDao;
import com.softdesign.devintensive.utils.ConstantManager;
import com.softdesign.devintensive.utils.NetworkStatusChecker;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = ConstantManager.TAG_PREFIX + " AuthActivity";
    private Button mSignIn;
    private TextView mRemembderPassword;
    private EditText mLogin,mPassword;
    private CoordinatorLayout mCoordinatorLayout;

    private DataManager mDataManager;

    private RepositoryDao mRepositoryDao;
    private UserDao mUserDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        mDataManager = DataManager.getInstance();
        mUserDao = mDataManager.getDaoSession().getUserDao();
        mRepositoryDao = mDataManager.getDaoSession().getRepositoryDao();

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
        mDataManager.getPreferensManager().saveAuthToken(userModel.getData().getToken());
        mDataManager.getPreferensManager().saveUserId(userModel.getData().getUser().getId());
        saveUserValues(userModel);
        Intent loginIntent = new Intent(this,MainActivity.class);
        startActivity(loginIntent);
    }

    private void signIn(){
        if (NetworkStatusChecker.isNetworkAvailable(this)) {
            Call<UserModelRes> call = mDataManager.loginUser(new UserLoginReq(mLogin.getText().toString(), mPassword.getText().toString()));
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
                    Log.d("X-788","FAILURE "+t.getLocalizedMessage());

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
        mDataManager.getPreferensManager().saveUserProfileValues(userValues);

    }

    private void saveUserInDb(){
        Call<UserListRes> call = mDataManager.getUserListFromNetwork();
        call.enqueue(new Callback<UserListRes>() {
            @Override
            public void onResponse(Call<UserListRes> call, Response<UserListRes> response) {
                try {
                    if (response.code() == 200) {
                        List<Repository> allRepositories = new ArrayList<Repository>();
                        List<User> allUsers = new ArrayList<User>();

                        for (UserListRes.UserData userRes : response.body().getData()) {
                            allRepositories.addAll(getRepoListFromUserRes(userRes));
                            allUsers.add(new User(userRes));
                        }

                        mRepositoryDao.insertOrReplaceInTx(allRepositories);
                        mUserDao.insertOrReplaceInTx(allUsers);


                    }else {
                        showSnackbar("Список пользователей не может быть получен");
                        Log.e(TAG," onResponse : "+String.valueOf(response.errorBody().source()));
                    }

                }catch (Exception e){
                    e.printStackTrace();
                    showSnackbar("Что то пошло не так");
                }
            }

            @Override
            public void onFailure(Call<UserListRes> call, Throwable t) {

            }
        });
    }

    private List<Repository> getRepoListFromUserRes(UserListRes.UserData userData){
        final String userId = userData.getId();

        List<Repository> repositories = new ArrayList<>();
        for (UserModelRes.Repo repo : userData.getRepositories().getRepo()) {
            repositories.add(new Repository(repo,userId));
        }
        return  repositories;
    }
}
