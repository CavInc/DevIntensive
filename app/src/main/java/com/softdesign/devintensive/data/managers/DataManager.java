package com.softdesign.devintensive.data.managers;

import android.content.Context;
import android.support.annotation.NonNull;

import com.softdesign.devintensive.data.network.PicassoCache;
import com.softdesign.devintensive.data.network.RestService;

import com.softdesign.devintensive.data.network.ServiceGenerator;
import com.softdesign.devintensive.data.network.req.UserLoginReq;
import com.softdesign.devintensive.data.network.res.UploadPhotoRes;
import com.softdesign.devintensive.data.network.res.UserListRes;
import com.softdesign.devintensive.data.network.res.UserModelRes;
import com.softdesign.devintensive.data.storage.models.DaoSession;
import com.softdesign.devintensive.data.storage.models.User;
import com.softdesign.devintensive.utils.DevIntensiveApplication;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import okhttp3.RequestBody;
import retrofit2.Call;


public class DataManager {
    private static DataManager INSTANCE = null;
    private Picasso mPicasso;


    private Context mContext;
    private PreferensManager mPreferensManager;
    private RestService mRestService;

    private DaoSession mDaoSession;

    public DataManager(){
        this.mPreferensManager = new PreferensManager();
        this.mContext = DevIntensiveApplication.getContext();
        this.mRestService = ServiceGenerator.createService(RestService.class);
        this.mPicasso = new PicassoCache(mContext).getPicassoInstance();

        this.mDaoSession = DevIntensiveApplication.getDaoSession();
    }


    public static DataManager getInstance() {
        if (INSTANCE==null){
            INSTANCE = new DataManager();
        }
        return INSTANCE;
    }

    public PreferensManager getPreferensManager() {
        return mPreferensManager;
    }

    public Context getContext() {
        return mContext;
    }

    public Picasso getPicasso() {
        return mPicasso;
    }

    // region ====== network =================
    public Call<UserModelRes> loginUser (UserLoginReq userLoginReq){
        return mRestService.loginUser(userLoginReq);
    }
    public Call<UserListRes> getUserListFromNetwork(){
        return mRestService.getUserList();
    }

    public Call<UploadPhotoRes> uploadPhoto (String userId, RequestBody photoFile){
        //return mRestService.uploadPhoto(userId,photoFile); // гдето косяк в обявление
        return null;
    }
    // endregion

    //region ======== databse ==============

    public DaoSession getDaoSession() {
        return mDaoSession;
    }

    public List<User> getUserListFromDb(){
        List <User> temp = new ArrayList<>();
        return temp;
    }
    //endregion
}
