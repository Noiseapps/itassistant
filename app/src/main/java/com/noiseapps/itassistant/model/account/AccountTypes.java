package com.noiseapps.itassistant.model.account;

import android.support.annotation.DrawableRes;
import android.support.annotation.IntDef;
import android.support.annotation.StringRes;

import com.noiseapps.itassistant.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


public class AccountTypes {

    public static final int ACC_JIRA = 1;
    public static final int ACC_STASH = 2;
    public static final int ACC_JENKINS = 3;

    @DrawableRes
    public static int getAccountImageDrawable(@AccountType int type) {
        switch (type) {
            case ACC_JIRA:
                return R.drawable.jira;
            case ACC_STASH:
                return R.drawable.jira;
            case ACC_JENKINS:
                return R.drawable.jenkins;
            default:
                final String format = "Typ konta : %1$d nie może zostać zmapowany";
                throw new TypeNotFoundException(String.format(format, type));
        }
    }

    public static boolean isAtlassian(@AccountType int type) {
        return type == ACC_JIRA || type == ACC_STASH;
    }

    @StringRes
    public static int getAccountName(@AccountType int type) {
        switch (type) {
            case ACC_JIRA:
                return R.string.jira;
            case ACC_STASH:
                return R.string.stash;
            case ACC_JENKINS:
                return R.string.jenkins;
            default:
                final String format = "Typ konta : %1$d nie może zostać zmapowany";
                throw new TypeNotFoundException(String.format(format, type));
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({ACC_JIRA, ACC_STASH, ACC_JENKINS})
    public @interface AccountType {
    }

    public static class TypeNotFoundException extends RuntimeException {

        public TypeNotFoundException(String message) {
            super(message);
        }
    }

}
