package com.noiseapps.itassistant.database;

import org.androidannotations.annotations.sharedpreferences.DefaultString;
import org.androidannotations.annotations.sharedpreferences.SharedPref;

@SharedPref(SharedPref.Scope.UNIQUE)
public interface Preferences {

    @DefaultString("{}")
    String jiraConfig();

    @DefaultString("[]")
    String accounts();
}
