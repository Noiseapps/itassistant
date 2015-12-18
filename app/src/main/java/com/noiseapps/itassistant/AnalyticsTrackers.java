package com.noiseapps.itassistant;

import android.content.Context;
import android.support.annotation.IntDef;

import java.util.HashMap;
import java.util.Map;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import org.androidannotations.annotations.EBean;

@EBean(scope = EBean.Scope.Singleton)
public final class AnalyticsTrackers {



    public static final int APP_TARGET = 0;

    @IntDef({APP_TARGET})
    public @interface TargetTypes {}

    public enum Target {
        APP,
        // Add more trackers here if you need, and update the code in #get(Target) below
    }

    private final Map<Target, Tracker> mTrackers = new HashMap<Target, Tracker>();
    private final Context mContext;


    private AnalyticsTrackers(Context context) {
        mContext = context.getApplicationContext();
        GoogleAnalytics.getInstance(context).setLocalDispatchPeriod(15);
    }

    public synchronized Tracker get(Target target) {
        if (!mTrackers.containsKey(target)) {
            Tracker tracker;
            switch (target) {
                case APP:
                    tracker = GoogleAnalytics.getInstance(mContext).newTracker(R.xml.app_tracker);
                    tracker.enableAdvertisingIdCollection(true);
                    break;
                default:
                    throw new IllegalArgumentException("Unhandled analytics target " + target);
            }
            mTrackers.put(target, tracker);
        }

        return mTrackers.get(target);
    }

    public void sendScreenVisit(String screenName) {
        final Tracker tracker = getDefault();
        tracker.setScreenName(screenName);
        tracker.send(new HitBuilders.ScreenViewBuilder().build());
        GoogleAnalytics.getInstance(mContext).dispatchLocalHits();
    }

    public synchronized Tracker getDefault() {
        return get(Target.APP);
    }

}
