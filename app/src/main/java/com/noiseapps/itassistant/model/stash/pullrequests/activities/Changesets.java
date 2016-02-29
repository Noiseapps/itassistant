package com.noiseapps.itassistant.model.stash.pullrequests.activities;

import com.noiseapps.itassistant.model.stash.commits.Commit;

import java.util.List;

public class Changesets {
    List<Commit> changesets;
    int total;

    public List<Commit> getChangesets() {
        return changesets;
    }

    public int getTotal() {
        return total;
    }
}
