package com.noiseapps.itassistant.model.stash.pullrequests.activities;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Attributes {

    @SerializedName("jira-key")
    List<String> jiraKeys;
}
