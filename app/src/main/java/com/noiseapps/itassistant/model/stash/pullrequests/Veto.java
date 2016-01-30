package com.noiseapps.itassistant.model.stash.pullrequests;

import com.noiseapps.itassistant.utils.StringUtils;

public class Veto {

    String summaryMessage;
    String detailedMessage;

    @Override
    public String toString() {
        return "Veto{" +
                "summaryMessage='" + summaryMessage + '\'' +
                ", detailedMessage='" + detailedMessage + '\'' +
                '}';
    }

    public String getSummaryMessage() {
        return summaryMessage;
    }

    public void setSummaryMessage(String summaryMessage) {
        this.summaryMessage = summaryMessage;
    }

    public String getDetailedMessage() {
        return detailedMessage;
    }

    public void setDetailedMessage(String detailedMessage) {
        this.detailedMessage = detailedMessage;
    }
}
