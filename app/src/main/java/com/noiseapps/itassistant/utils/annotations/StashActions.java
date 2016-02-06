package com.noiseapps.itassistant.utils.annotations;

import android.support.annotation.IntDef;

import com.noiseapps.itassistant.StashDetailsActivity;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@IntDef({StashDetailsActivity.ACTION_BRANCHES,
        StashDetailsActivity.ACTION_SOURCE,
        StashDetailsActivity.ACTION_COMMITS,
        StashDetailsActivity.ACTION_PULL_REQUESTS})
public @interface StashActions {
}
