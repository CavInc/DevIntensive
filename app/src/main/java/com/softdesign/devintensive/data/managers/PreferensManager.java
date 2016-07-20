package com.softdesign.devintensive.data.managers;


import android.content.SharedPreferences;
import android.net.Uri;

import com.softdesign.devintensive.utils.ConstantManager;
import com.softdesign.devintensive.utils.DevIntensiveApplication;

import java.util.ArrayList;
import java.util.List;

public class PreferensManager {
    private SharedPreferences mSharedPreferences;

    private static final String[] USER_FIELD = {ConstantManager.USER_PHONE_KEY,ConstantManager.USER_MAIL_KEY,
    ConstantManager.USER_VK_KEY,ConstantManager.USER_GIT_KEY,ConstantManager.USER_BIO_KEY};

    public PreferensManager(){
        this.mSharedPreferences = DevIntensiveApplication.getSharedPreferences();

    }
    public void saveUserProfileData(List<String> userFields){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        for (int i=0;i<USER_FIELD.length;i++) {
            editor.putString(USER_FIELD[i],userFields.get(i));

        }
        editor.apply();
    }
    public List<String> loadUserProfileData(){
        List<String> userFields = new ArrayList<>();
        userFields.add(mSharedPreferences.getString(ConstantManager.USER_PHONE_KEY,"null"));
        userFields.add(mSharedPreferences.getString(ConstantManager.USER_MAIL_KEY,"null"));
        userFields.add(mSharedPreferences.getString(ConstantManager.USER_VK_KEY,"null"));
        userFields.add(mSharedPreferences.getString(ConstantManager.USER_GIT_KEY,"null"));
        userFields.add(mSharedPreferences.getString(ConstantManager.USER_BIO_KEY,"null"));
        return userFields;
    }

    public void saveUserPhoto(Uri uri){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(ConstantManager.USER_PHOTO_KEY,uri.toString());
        editor.apply();
    }
    public Uri loadUserPhoto(){
        return Uri.parse(mSharedPreferences.getString(ConstantManager.USER_PHOTO_KEY,
                "android.resource://com.softdesign.devintensive/drawable/userphoto"));
    }

    public void saveAuthToken(String authToken){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(ConstantManager.AUTH_TOKEN,authToken);
        editor.apply();
    }

    public String getAuthToken(){
        return mSharedPreferences.getString(ConstantManager.AUTH_TOKEN,"null");
    }

    public void sateUserId(String userId){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(ConstantManager.USER_ID_KEY,userId);
        editor.apply();
    }

    public String getUserId(){
        return mSharedPreferences.getString(ConstantManager.USER_ID_KEY,"null");
    }

}
