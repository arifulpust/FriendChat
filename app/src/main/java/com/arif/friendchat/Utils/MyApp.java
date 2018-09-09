package com.arif.friendchat.Utils;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.util.Log;


import com.arif.friendchat.R;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

//import dream71.com.fmcg_nigeria.requestHandler.LruBitmapCache;


public class MyApp extends Application {
//    private RequestQueue mRequestQueue;
//    private ImageLoader mImageLoader;
    private static MyApp mInstance;

    private static MyApp _instance;

    //private RequestQueue mRequestQueue;
//    private ImageLoader mImageLoader;



    @Override
    public void onCreate() {
        super.onCreate();


        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/AvenirNextMedium.otf")
                // .setDefaultFontPath("fonts/RobotoCondensed-BoldItalic.ttf")
                // .setDefaultFontPath("fonts/RobotoCondensed-Italic.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
        mInstance = this;
        _instance = (MyApp) getApplicationContext();


       // MyVolley.init(this);
        MultiDex.install(this);
        Log.e("install","call");

    }
//    synchronized public Tracker getDefaultTracker() {
//        // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
//        if (sTracker == null) {
//            sTracker = sAnalytics.newTracker(R.xml.global_tracker);
//        }
//
//        return sTracker;
//    }

    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);

    }
    public static synchronized MyApp getInstance() {
        return _instance;
    }



}
