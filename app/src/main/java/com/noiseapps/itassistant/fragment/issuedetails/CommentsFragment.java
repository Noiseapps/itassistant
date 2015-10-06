package com.noiseapps.itassistant.fragment.issuedetails;

import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ListView;

import java.util.List;

import com.noiseapps.itassistant.R;
import com.noiseapps.itassistant.adapters.CommentsAdapter;
import com.noiseapps.itassistant.connector.JiraConnector;
import com.noiseapps.itassistant.model.jira.issues.Issue;
import com.noiseapps.itassistant.model.jira.issues.comments.Comment;
import com.noiseapps.itassistant.model.jira.issues.comments.Comments;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

@EFragment(R.layout.fragment_comment_list)
public class CommentsFragment extends Fragment{

    @Bean
    JiraConnector jiraConnector;
    @ViewById
    ListView commentsList;
    @FragmentArg
    Issue issue;
    @ViewById
    View noCommentsView, loadingComments;

    @AfterViews
    void init() {
        initComments();
    }

    private void hideComments() {
        noCommentsView.setVisibility(View.VISIBLE);
    }

    private void initComments() {
        noCommentsView.setVisibility(View.GONE);
        commentsList.setVisibility(View.GONE);
        loadingComments.setVisibility(View.VISIBLE);
        jiraConnector.getIssueComments(issue.getId(), new Callback<Comments>() {
            @Override
            public void success(Comments comments, Response response) {
                final List<Comment> commentList = comments.getComments();
                loadingComments.setVisibility(View.GONE);
                if (commentList.isEmpty()) {
                    hideComments();
                } else {
                    commentsList.setVisibility(View.VISIBLE);
                    commentsList.setAdapter(new CommentsAdapter(getContext(), commentList));
                }
            }

            @Override
            public void failure(RetrofitError error) {
                hideComments();
            }
        });
    }
}
