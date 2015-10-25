package com.noiseapps.itassistant.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.noiseapps.itassistant.R;
import com.noiseapps.itassistant.model.jira.issues.Assignee;
import com.noiseapps.itassistant.model.jira.issues.Issue;
import com.noiseapps.itassistant.utils.ToggleList;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class IssuesAdapter extends RecyclerView.Adapter<IssuesAdapter.IssueViewHolder> {

    private final Context context;
    private final Picasso authPicasso;
    private final boolean assignedToMe;
    private final List<Issue> issueList;
    private final IssueAdapterCallback callback;
    ToggleList<Issue> issueToggleList = new ToggleList<>();

    public IssuesAdapter(Context context, List<Issue> issueList, Picasso authPicasso, IssueAdapterCallback callback, boolean assignedToMe) {
        this.context = context;
        this.authPicasso = authPicasso;
        this.assignedToMe = assignedToMe;
        this.issueList = issueList == null ? new ArrayList<>() : issueList;
        this.callback = callback;
    }

    public void clearActionMode() {
        issueToggleList.clear();
        notifyDataSetChanged();
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

    public interface IssueAdapterCallback {
        void onItemClicked(Issue selectedIssue);

        void onItemLongPressed(ToggleList<Issue> issue);
    }

    public class IssueViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView title;
        //        private final TextView assignee;
        private final ImageView issuePriority;
        private final ImageView issueType;
        private final CircleImageView avatar;
        private final TextView issueKey;
        private Issue issue;

        public IssueViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.issueTitle);
            issueKey = (TextView) itemView.findViewById(R.id.issueKey);
            issuePriority = (ImageView) itemView.findViewById(R.id.issuePriority);
            issueType = (ImageView) itemView.findViewById(R.id.issueType);
            avatar = (CircleImageView) itemView.findViewById(R.id.avatar);
            itemView.setOnClickListener(this);
        }

        public void build(Issue issue) {
            this.issue = issue;
            itemView.setActivated(issueToggleList.contains(issue));
            loadIssueType();
            loadIssuePriority();
            title.setText(issue.getFields().getSummary());
            issueKey.setText(issue.getKey());
            issue.getFields().getAggregateProgress().getPercent();
            final Assignee issueAssignee = issue.getFields().getAssignee();
            if (issueAssignee != null) {
                final String avatarUrl = issueAssignee.getAvatarUrls().get48x48();
                authPicasso.load(avatarUrl).
                        placeholder(R.drawable.ic_account_circle).
                        error(R.drawable.ic_account_circle).
                        into(avatar);
            } else {
                avatar.setVisibility(View.INVISIBLE);
            }
            if(!assignedToMe) {
                itemView.setOnLongClickListener(v -> {
                    onLongPressed(issue);
                    return true;
                });
            }
        }

        private void onLongPressed(Issue issue) {
            issueToggleList.toggle(issue);
            notifyItemChanged(issueList.indexOf(issue));
            callback.onItemLongPressed(issueToggleList);
        }

        @Override
        public void onClick(View v) {
            if(issueToggleList.isEmpty()) {
                issueToggleList.clear();
                notifyDataSetChanged();
                callback.onItemClicked(issue);
            } else {
                onLongPressed(issue);
            }
        }

        private void loadIssuePriority() {
            final String iconUrl = issue.getFields().getPriority().getIconUrl();
            Picasso.with(context).load(iconUrl).into(issuePriority);
        }

        private void loadIssueType() {
            final String iconUrl = issue.getFields().getIssueType().getIconUrl();
            Picasso.with(context).load(iconUrl).noFade().into(issueType);
        }
    }
}
