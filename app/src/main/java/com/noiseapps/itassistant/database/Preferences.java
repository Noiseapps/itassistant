package com.noiseapps.itassistant.database;

import org.androidannotations.annotations.sharedpreferences.DefaultString;
import org.androidannotations.annotations.sharedpreferences.DefaultStringSet;
import org.androidannotations.annotations.sharedpreferences.SharedPref;

import java.util.Set;

@SharedPref(SharedPref.Scope.UNIQUE)
public interface Preferences {

    @DefaultString("{}")
    String jiraConfig();

    @DefaultString("[]")
    String accounts();

    @DefaultString("{}")
    String timeTracking();

    Set<String> shownProjects();
}
