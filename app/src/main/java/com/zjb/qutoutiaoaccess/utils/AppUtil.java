package com.zjb.qutoutiaoaccess.utils;

import android.content.Context;

import com.google.gson.Gson;

/**
 * @author LCM
 * @date 2019/6/10 14:34
 */
public class AppUtil {
    private volatile static Gson gson=null;

    //获取单例
    public static Gson getGson(Context context) {
        if (gson == null) {
            synchronized (context) {
                if (gson == null) {
                    gson = new Gson();
                }
            }
        }
        return gson;
    }

}
