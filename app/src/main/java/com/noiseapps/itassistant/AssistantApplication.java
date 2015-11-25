package com.noiseapps.itassistant;

import android.support.multidex.MultiDexApplication;

import java.util.Map;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.noiseapps.itassistant.utils.Analytics;

import net.danlew.android.joda.JodaTimeAndroid;

import org.acra.ACRA;
import org.acra.annotation.ReportsCrashes;
import org.acra.sender.HttpSender;

import jonathanfinerty.once.Once;

@ReportsCrashes(
        httpMethod = HttpSender.Method.PUT,
        reportType = HttpSender.Type.JSON,
        formUri = "http://176.31.165.225:5984/acra-itassistant/_design/acra-storage/_update/report",
        formUriBasicAuthLogin = "itassistant",
        formUriBasicAuthPassword = "itassistant12"
)
public class AssistantApplication extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        JodaTimeAndroid.init(this);
        Once.initialise(this);
        ACRA.init(this);
        AnalyticsTrackers.initialize(this);
        final Map<String, String> event = new HitBuilders.EventBuilder(Analytics.CATEGORIES.APP, Analytics.ACTIONS.OPEN).build();
        AnalyticsTrackers.getTracker().send(event);
        GoogleAnalytics.getInstance(this).dispatchLocalHits();
    }
}
