package com.softdesign.devintensive.data.network.interseptors;


import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.data.managers.PreferensManager;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Заполняет заголовок запроса к серверу
 */

public class HeadInterseptor implements Interceptor{
    @Override
    public Response intercept(Chain chain) throws IOException {
        PreferensManager pm = DataManager.getInstance().getPreferensManager();

        Request original = chain.request();
        Request.Builder requestBuilder = original.newBuilder()
                .header("X-Access-Token",pm.getAuthToken())
                .header("Request-User-Id",pm.getUserId())
                .header("User-Agent","DevIntensiveApp")
                .header("Cache-Control","max-age="+(60*60*24));
        Request request = requestBuilder.build();

        return chain.proceed(request);
    }
}
