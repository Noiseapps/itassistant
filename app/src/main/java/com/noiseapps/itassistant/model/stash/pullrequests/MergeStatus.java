package com.noiseapps.itassistant.model.stash.pullrequests;

import java.util.List;

public class MergeStatus {
    boolean canMerge;
    boolean conflicted;
    List<Veto> vetoes;

    public boolean isCanMerge() {
        return canMerge;
    }

    public boolean isConflicted() {
        return conflicted;
    }

    public List<Veto> getVetoes() {
        return vetoes;
    }

    @Override
    public String toString() {
        return "MergeStatus{" +
                "canMerge=" + canMerge +
                ", conflicted=" + conflicted +
                ", vetoes=" + vetoes +
                '}';
    }
}
