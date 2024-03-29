package com.noiseapps.itassistant.fragment.issuedetails;

import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.github.jorgecastilloprz.FABProgressCircle;
import com.noiseapps.itassistant.AnalyticsTrackers;
import com.noiseapps.itassistant.R;
import com.noiseapps.itassistant.adapters.CommentsAdapter;
import com.noiseapps.itassistant.connector.JiraConnector;
import com.noiseapps.itassistant.fragment.IssueDetailFragment;
import com.noiseapps.itassistant.model.jira.issues.Issue;
import com.noiseapps.itassistant.model.jira.issues.comments.Comment;
import com.noiseapps.itassistant.model.jira.issues.comments.Comments;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@EFragment(R.layout.fragment_comment_list)
public class CommentsFragment extends Fragment implements IssueDetailFragment.DetailFragmentCallbacks {

    @Bean
    JiraConnector jiraConnector;
    @ViewById
    RecyclerView commentsList;
    FABProgressCircle fabProgressCircle;
    @FragmentArg
    Issue issue;
    @ViewById
    View noCommentsView, loadingComments, errorView;
    @Bean
    AnalyticsTrackers tracker;
    private CommentsAdapter adapter;

    @AfterViews
    void init() {
        initComments();
    }

    private void initComments() {
        adapter = new CommentsAdapter(getContext(), new ArrayList<>());
        commentsList.setAdapter(adapter);
        commentsList.setLayoutManager(new LinearLayoutManager(getContext()));
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
                    adapter.addItems(commentList);
                    commentsList.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                loadingComments.setVisibility(View.GONE);
                errorView.setVisibility(View.VISIBLE);
            }
        });
    }

    private void hideComments() {
        noCommentsView.setVisibility(View.VISIBLE);
    }

    void onAddCommentClick() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.postComment, issue.getKey()));
        builder.setView(R.layout.dialog_post_comment);
        builder.setPositiveButton(R.string.post, null);
        builder.setNegativeButton(R.string.cancel, null);
        final AlertDialog alertDialog = builder.create();
        alertDialog.setOnShowListener(dialog -> onDialogShown(alertDialog));
        alertDialog.show();
    }

    private void onDialogShown(final AlertDialog alertDialog) {
        final EditText bodyEditText = (EditText) alertDialog.findViewById(R.id.commentBody);
        final Button positiveButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        final Button negativeButton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        positiveButton.setOnClickListener(v -> onPositiveButtonClicked(bodyEditText, alertDialog));
        negativeButton.setOnClickListener(v -> alertDialog.dismiss());
    }

    private void onPositiveButtonClicked(EditText bodyEditText, final AlertDialog alertDialog) {
        fabProgressCircle.show();
        final Comment comment = new Comment();
        comment.setBody(bodyEditText.getText().toString());
        jiraConnector.postIssueComment(issue.getId(), comment).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(this::onCommentAdded, throwable -> onCommentAddFailed());
        alertDialog.dismiss();
    }

    private void onCommentAdded(Comment comment) {
        tracker.sendEvent(AnalyticsTrackers.SCREEN_ISSUE_DETAILS, AnalyticsTrackers.CATEGORY_ISSUES, "commentAdded");
        fabProgressCircle.beginFinalAnimation();
        Snackbar.make(fabProgressCircle, R.string.commentAdded, Snackbar.LENGTH_LONG).show();
        noCommentsView.setVisibility(View.GONE);
        commentsList.setVisibility(View.VISIBLE);
        adapter.addItem(comment);
    }

    private void onCommentAddFailed() {
        tracker.sendEvent(AnalyticsTrackers.SCREEN_ISSUE_DETAILS, AnalyticsTrackers.CATEGORY_ISSUES, "commentAddingFailed");
        fabProgressCircle.hide();
        Snackbar.make(fabProgressCircle, R.string.failedToAddComment, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onFabClicked(FABProgressCircle circle) {
        fabProgressCircle = circle;
        onAddCommentClick();
    }
}
