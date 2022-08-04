package com.example.jetpackdemo;

import android.app.Application;

import com.facebook.ads.AudienceNetworkAds;

public class YourApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // Initialize the Audience Network SDK
        AudienceNetworkAds.initialize(this);
    }
}
