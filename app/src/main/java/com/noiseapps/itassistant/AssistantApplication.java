package com.noiseapps.itassistant;

import android.support.multidex.MultiDexApplication;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import com.crashlytics.android.answers.LoginEvent;
import com.orhanobut.logger.Logger;
import com.squareup.leakcanary.LeakCanary;

import io.fabric.sdk.android.Fabric;
import net.danlew.android.joda.JodaTimeAndroid;

import org.acra.ACRA;
import org.acra.annotation.ReportsCrashes;
import org.acra.sender.HttpSender;

import jonathanfinerty.once.Once;

@ReportsCrashes(
        httpMethod = HttpSender.Method.PUT,
        reportType = HttpSender.Type.JSON,
        formUri = "http://vps232308.ovh.net:5984/acra-itassistant/_design/acra-storage/_update/report",
        formUriBasicAuthLogin = "itassistant",
        formUriBasicAuthPassword = "itassistant12"
)
public class AssistantApplication extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics(), new Answers());
        Answers.getInstance().logCustom(new CustomEvent("AppStarted"));
        AnalyticsTrackers_.getInstance_(this);
        JodaTimeAndroid.init(this);
        Logger.init(this.getClass().getSimpleName());
        Once.initialise(this);
        ACRA.init(this);
        LeakCanary.install(this);
    }
}
