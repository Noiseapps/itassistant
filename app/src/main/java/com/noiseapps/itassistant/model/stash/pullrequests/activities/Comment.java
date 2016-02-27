package com.noiseapps.itassistant.model.stash.pullrequests.activities;

import com.noiseapps.itassistant.model.stash.commits.AuthorMetadata;
import com.noiseapps.itassistant.model.stash.general.StashUser;

public class Comment {
    int id;
    int version;
    String text;
    StashUser author;
    long createdDate;
    long updatedDate;

    // TODO: 26.02.2016 Comment comments;  - todo bo na razie nie będę tego umieszczał i bo nie wiem czy to będzie to :)


    public class PermittedOperations {
        boolean deletable;
        boolean editable;
    }
}


