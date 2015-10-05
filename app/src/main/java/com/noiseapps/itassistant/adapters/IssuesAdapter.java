package com.noiseapps.itassistant.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import com.noiseapps.itassistant.R;
import com.noiseapps.itassistant.model.jira.issues.Assignee;
import com.noiseapps.itassistant.model.jira.issues.Issue;
import com.squareup.picasso.Picasso;


public class IssuesAdapter extends RecyclerView.Adapter<IssuesAdapter.IssueViewHolder>{

    private final Context context;
    private final List<Issue> issueList;
    private Picasso authPicasso;

    public IssuesAdapter(Context context, List<Issue> issueList) {
        this.context = context;
        this.issueList = issueList;
    }

    @Override
    public IssueViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(context).inflate(R.layout.item_issue, parent, false);
        return new IssueViewHolder(view);
    }

    @Override
    public void onBindViewHolder(IssueViewHolder holder, int position) {
        holder.build(issueList.get(position));
    }

    @Override
    public int getItemCount() {
        return issueList.size();
    }

    public void setPicasso(Picasso authPicasso) {
        this.authPicasso = authPicasso;
    }

    public class IssueViewHolder extends RecyclerView.ViewHolder {

        private final TextView title;
        private final TextView assignee;
        private final ImageView issuePriority;
        private final ImageView issueType;
        private Issue issue;
        private final TextView issueKey;

        public IssueViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.issueTitle);
            issueKey = (TextView) itemView.findViewById(R.id.issueKey);
            assignee = (TextView) itemView.findViewById(R.id.assignee);
            issuePriority = (ImageView) itemView.findViewById(R.id.issuePriority);
            issueType = (ImageView) itemView.findViewById(R.id.issueType);
        }

        public void build(Issue issue) {
            this.issue = issue;
            loadIssueType();
            loadIssuePriority();
            title.setText(issue.getFields().getSummary());
            issueKey.setText(issue.getKey());
            issue.getFields().getAggregateprogress().getPercent();
            final Assignee issueAssignee = issue.getFields().getAssignee();
            if(issueAssignee != null) {
                assignee.setText(context.getString(R.string.assignee, issueAssignee.getDisplayName()));
            } else {
                assignee.setVisibility(View.GONE);
            }
        }

        private void loadIssuePriority() {
            final String iconUrl = issue.getFields().getPriority().getIconUrl();
            Picasso.with(context).load(iconUrl).into(issuePriority);
        }

        private void loadIssueType() {
            final String iconUrl = issue.getFields().getIssuetype().getIconUrl();
            Picasso.with(context).load(iconUrl).noFade().into(issueType);
        }
    }
}
