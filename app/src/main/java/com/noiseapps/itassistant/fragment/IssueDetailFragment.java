package com.noiseapps.itassistant.fragment;

import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import com.akexorcist.roundcornerprogressbar.TextRoundCornerProgressBar;
import com.noiseapps.itassistant.R;
import com.noiseapps.itassistant.connector.JiraConnector;
import com.noiseapps.itassistant.model.jira.issues.Assignee;
import com.noiseapps.itassistant.model.jira.issues.Fields;
import com.noiseapps.itassistant.model.jira.issues.Issue;
import com.noiseapps.itassistant.model.jira.issues.comments.Comment;
import com.noiseapps.itassistant.model.jira.issues.comments.Comments;
import com.noiseapps.itassistant.utils.Consts;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.joda.time.DateTime;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

@EFragment(R.layout.fragment_issue_detail)
@OptionsMenu(R.menu.menu_issue_details)
public class IssueDetailFragment extends Fragment {

    @ViewById
    TextView type, priority, labels, status, issueName, assignee, reporter, description, created, modified;
    @ViewById
    TextRoundCornerProgressBar estimated, left, logged;
    @ViewById
    LinearLayout commentsList;

    @ViewById
    View scrollView, loadingComments, comments;

    @FragmentArg
    Issue issue;
    @Bean
    JiraConnector jiraConnector;

    @AfterViews
    void init() {
        initComments();
        setHasOptionsMenu(true);
        final Fields issueFields = issue.getFields();
        setIssueDetailsData(issueFields);
        setProgressData(issueFields);
        setDatesData(issueFields);
        setPeopleData(issueFields);
    }

    private void initComments() {
        commentsList.setVisibility(View.GONE);
        loadingComments.setVisibility(View.VISIBLE);
        jiraConnector.getIssueComments(issue.getId(), new Callback<Comments>() {
            @Override
            public void success(Comments comments, Response response) {
                final List<Comment> commentList = comments.getComments();
                if (commentList.isEmpty()) {
                    hideComments();
                } else {
                    loadingComments.setVisibility(View.GONE);
                    commentsList.setVisibility(View.VISIBLE);
                    for (Comment comment : commentList) {
                        addComment(comment);
                    }
                    commentsList.invalidate();
                    commentsList.requestLayout();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                hideComments();
            }
        });
    }

    private void addComment(Comment comment) {
        final View root = LayoutInflater.from(getActivity()).inflate(R.layout.item_comment, commentsList, false);
        final TextView body = (TextView) root.findViewById(R.id.commentBody);
        final TextView creator = (TextView) root.findViewById(R.id.commentCreator);
        final TextView date = (TextView) root.findViewById(R.id.commentDate);

        body.setText(comment.getBody());
        creator.setText(comment.getAuthor().getDisplayName());
        final String dateString = DateTime.parse(comment.getCreated()).toString(Consts.DATE_TIME_FORMAT);
        date.setText(dateString);
        commentsList.addView(root);
    }

    private void hideComments() {
        comments.setVisibility(View.GONE);
    }

    private void setIssueDetailsData(Fields issueFields) {
        type.setText(issueFields.getIssuetype().getName());
        priority.setText(issueFields.getPriority().getName());
        status.setText(issueFields.getStatus().getName());
        labels.setText(StringUtils.join(issueFields.getLabels(), ", "));
        issueName.setText(String.format("%s (%s)", issueFields.getSummary(), issue.getKey()));
        description.setText(issueFields.getDescription());
    }

    private void setPeopleData(Fields issueFields) {
        final Assignee assignee = issueFields.getAssignee();
        if(assignee != null) {
            this.assignee.setText(assignee.getDisplayName());
        } else {
            this.assignee.setText(R.string.notAssigned);
        }
        reporter.setText(issueFields.getReporter().getDisplayName());
    }

    private void setDatesData(Fields issueFields) {
        final String createdTime = DateTime.parse(issueFields.getCreated()).toString(Consts.DATE_TIME_FORMAT);
        created.setText(createdTime);
        final String updatedTime = DateTime.parse(issueFields.getUpdated()).toString(Consts.DATE_TIME_FORMAT);
        modified.setText(updatedTime);
    }

    private void setProgressData(Fields issueFields) {
        final long originalEstimate = issueFields.getAggregatetimeoriginalestimate();
        final long timeSpent = issueFields.getTimespent();
        long max = Math.max(originalEstimate, timeSpent);
        final String estimateDuration = DurationFormatUtils.formatDuration(originalEstimate * 1000, getContext().getString(R.string.timeFormat));
        final String spentDuration = DurationFormatUtils.formatDuration(timeSpent * 1000, getContext().getString(R.string.timeFormat));

        estimated.setMax(max);
        estimated.setProgress(originalEstimate);
        estimated.setProgressText(estimateDuration);

        left.setMax(max);

        logged.setMax(max);
        logged.setProgress(timeSpent);
        logged.setProgressText(spentDuration);
    }

    @OptionsItem(R.id.action_edit)
    void onEditIssue() {
        Snackbar.make(scrollView, R.string.optionUnavailable, Snackbar.LENGTH_LONG).show();
    }

    @OptionsItem(android.R.id.home)
    void onHome() {
        getActivity().finish();
    }
}
