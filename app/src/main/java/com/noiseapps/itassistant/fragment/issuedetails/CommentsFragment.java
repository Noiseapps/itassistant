package com.noiseapps.itassistant.fragment.issuedetails;

import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import com.github.jorgecastilloprz.FABProgressCircle;
import com.noiseapps.itassistant.R;
import com.noiseapps.itassistant.adapters.CommentsAdapter;
import com.noiseapps.itassistant.connector.JiraConnector;
import com.noiseapps.itassistant.model.jira.issues.Issue;
import com.noiseapps.itassistant.model.jira.issues.comments.Comment;
import com.noiseapps.itassistant.model.jira.issues.comments.Comments;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ItemLongClick;
import org.androidannotations.annotations.ViewById;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

@EFragment(R.layout.fragment_comment_list)
public class CommentsFragment extends Fragment {

    @Bean
    JiraConnector jiraConnector;
    @ViewById
    ListView commentsList;
    @ViewById
    FABProgressCircle fabProgressCircle;
    @FragmentArg
    Issue issue;
    @ViewById
    View noCommentsView, loadingComments, errorView;
    @ViewById
    FloatingActionButton addCommentFab;
    private CommentsAdapter adapter;

    @AfterViews
    void init() {
        initComments();
    }

    private void initComments() {
        adapter = new CommentsAdapter(getContext(), new ArrayList<Comment>());
        fabProgressCircle.setVisibility(View.GONE);
        noCommentsView.setVisibility(View.GONE);
        commentsList.setVisibility(View.GONE);
        loadingComments.setVisibility(View.VISIBLE);
        jiraConnector.getIssueComments(issue.getId(), new Callback<Comments>() {
            @Override
            public void success(Comments comments, Response response) {
                fabProgressCircle.setVisibility(View.VISIBLE);
                final List<Comment> commentList = comments.getComments();
                loadingComments.setVisibility(View.GONE);
                if (commentList.isEmpty()) {
                    hideComments();
                } else {
                    adapter.addItems(commentList);
                    commentsList.setVisibility(View.VISIBLE);
                    commentsList.setAdapter(adapter);
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

    @ItemLongClick(R.id.commentsList)
    void onItemLongClick(int position) {
        final Comment item = adapter.getItem(position);
        //todo remove item, undo snackbar

    }

    @Click(R.id.addCommentFab)
    void onAddCommentClick() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.postComment, issue.getKey()));
        builder.setView(R.layout.dialog_post_comment);
        builder.setPositiveButton(R.string.post, null);
        builder.setNegativeButton(R.string.cancel, null);
        final AlertDialog alertDialog = builder.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                onDialogShown(alertDialog);
            }
        });
        alertDialog.show();
    }

    private void onDialogShown(final AlertDialog alertDialog) {
        final View dialogRoot = alertDialog.findViewById(R.id.dialogRoot);
        final EditText bodyEditText = (EditText) alertDialog.findViewById(R.id.commentBody);
        final Button positiveButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        final Button negativeButton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPositiveButtonClicked(bodyEditText, alertDialog, dialogRoot);
            }
        });
        negativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }

    private void onPositiveButtonClicked(EditText bodyEditText, final AlertDialog alertDialog, final View dialogRoot) {
        fabProgressCircle.show();
        addCommentFab.setEnabled(false);
        final Comment comment = new Comment();
        comment.setBody(bodyEditText.getText().toString());
        jiraConnector.postIssueComment(issue.getId(), comment, new Callback<Comment>() {
            @Override
            public void success(Comment comment, Response response) {
                fabProgressCircle.beginFinalAnimation();
                Snackbar.make(fabProgressCircle, R.string.commentAdded, Snackbar.LENGTH_LONG).show();
                addCommentFab.setEnabled(true);
                adapter.addItem(comment);
                noCommentsView.setVisibility(View.GONE);
                commentsList.setVisibility(View.VISIBLE);
                commentsList.requestLayout();
                commentsList.invalidate();
            }

            @Override
            public void failure(RetrofitError error) {
                addCommentFab.setEnabled(true);
                fabProgressCircle.hide();
                Snackbar.make(fabProgressCircle, R.string.failedToAddComment, Snackbar.LENGTH_LONG).show();
            }
        });
        alertDialog.dismiss();
    }
}
