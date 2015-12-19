package com.noiseapps.itassistant;

import android.content.Context;
import android.support.annotation.IntDef;
import android.support.annotation.StringDef;
import android.util.SparseArray;
import android.util.SparseIntArray;

import java.util.HashMap;
import java.util.Map;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Logger;
import com.google.android.gms.analytics.Tracker;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

@EBean(scope = EBean.Scope.Singleton)
public class AnalyticsTrackers {

    public static final int APP_TARGET = 0;

    public static final String SCREEN_ACCOUNTS = "Accounts";
    public static final String SCREEN_ACCOUNT_EDIT= "AccountForm";
    public static final String SCREEN_ISSUE_LIST = "IssueList";
    public static final String SCREEN_ISSUE_DETAILS = "IssueDetails";
    public static final String SCREEN_ISSUE_EDIT = "IssueForm";

    public static final String CATEGORY_ACCOUNTS = "Accounts";
    public static final String CATEGORY_APP = "App";
    public static final String CATEGORY_MENU = "Menu";
    public static final String CATEGORY_ISSUES = "Issues";
    public static final String CATEGORY_TIME_TRACKER = "TimeTracking";

    @RootContext
    Context context;
    private GoogleAnalytics googleAnalytics;

    @IntDef({APP_TARGET})
    public @interface TargetTypes {}

    @StringDef({SCREEN_ACCOUNTS, SCREEN_ACCOUNT_EDIT, SCREEN_ISSUE_LIST, SCREEN_ISSUE_DETAILS, SCREEN_ISSUE_EDIT})
    public @interface ScreenNames {}

    @StringDef({CATEGORY_ACCOUNTS, CATEGORY_APP, CATEGORY_MENU, CATEGORY_ISSUES, CATEGORY_TIME_TRACKER})
    public @interface Categories {}

    private final SparseArray<Tracker> mTrackers = new SparseArray<>();

    @AfterInject
    void init() {
        googleAnalytics = GoogleAnalytics.getInstance(context);
//        googleAnalytics.setDryRun(BuildConfig.DEBUG);
        googleAnalytics.getLogger().setLogLevel(Logger.LogLevel.VERBOSE);
    }

    public synchronized Tracker get(@TargetTypes int targetType) {
        if (mTrackers.get(targetType) == null) {
            Tracker tracker;
            switch (targetType) {
                case APP_TARGET:
                    tracker = googleAnalytics.newTracker(R.xml.app_tracker);
                    tracker.enableAdvertisingIdCollection(true);
                    break;
                default:
                    throw new IllegalArgumentException("Unhandled analytics target " + targetType);
            }
            mTrackers.put(targetType, tracker);
        }

        return mTrackers.get(targetType);
    }

    public void sendScreenVisit(@ScreenNames String screenName) {
        final Tracker tracker = getDefault();
        tracker.setScreenName(screenName);
        tracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    public void sendEvent(@ScreenNames String screenName, @Categories  String category, String action) {
        final Tracker tracker = getDefault();
        tracker.setScreenName(screenName);
        HitBuilders.EventBuilder eventBuilder = new HitBuilders.EventBuilder(category, action);
        tracker.send(eventBuilder.build());
    }

    public void sendEvent(@ScreenNames String screenName, @Categories  String category, String action, String label) {
        final Tracker tracker = getDefault();
        tracker.setScreenName(screenName);
        HitBuilders.EventBuilder eventBuilder = new HitBuilders.EventBuilder(category, action);
        eventBuilder.setLabel(label);
        tracker.send(eventBuilder.build());
    }

    public synchronized Tracker getDefault() {
        return get(APP_TARGET);
    }
}
