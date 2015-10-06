package com.noiseapps.itassistant;

import android.app.Application;

import net.danlew.android.joda.JodaTimeAndroid;

import org.androidannotations.annotations.EApplication;

@EApplication
public class AssistantApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        JodaTimeAndroid.init(this);
    }
}
