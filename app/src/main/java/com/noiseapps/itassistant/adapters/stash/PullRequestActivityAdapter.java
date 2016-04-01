package com.noiseapps.itassistant.adapters.stash;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.noiseapps.itassistant.R;
import com.noiseapps.itassistant.model.stash.commits.Commit;
import com.noiseapps.itassistant.model.stash.pullrequests.activities.Comment;
import com.noiseapps.itassistant.model.stash.pullrequests.activities.PullRequestActivity;
import com.noiseapps.itassistant.utils.Consts;
import com.squareup.picasso.Picasso;

import org.joda.time.DateTime;
import org.w3c.dom.Text;

import java.util.Collections;
import java.util.List;

public class PullRequestActivityAdapter extends RecyclerView.Adapter<PullRequestActivityAdapter.ViewHolder> {

    private final List<PullRequestActivity> activities;
    private final LayoutInflater inflater;
    private Context context;
    private Picasso picasso;

    public PullRequestActivityAdapter(Context context, List<PullRequestActivity> activities, Picasso picasso) {
        this.context = context;
        this.activities = activities;
        this.picasso = picasso;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = inflater.inflate(R.layout.item_pull_request_activity, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(activities.get(position));
    }

    @Override
    public int getItemCount() {
        return activities.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private final FrameLayout otherContainer;
        private final TextView activityAuthor, activityAction, activityTimestamp;
        private final ImageView activityAuthorAvatar;
        private PullRequestActivity pullRequestActivity;

        public ViewHolder(View itemView) {
            super(itemView);
            this.activityAuthor = (TextView) itemView.findViewById(R.id.activityAuthor);
            this.activityAction = (TextView) itemView.findViewById(R.id.activityBadge);
            this.activityTimestamp = (TextView) itemView.findViewById(R.id.activityTimestamp);
            this.activityAuthorAvatar = (ImageView) itemView.findViewById(R.id.activityAuthorAvatar);
            this.otherContainer = (FrameLayout) itemView.findViewById(R.id.activityOtherContainer);
        }

        public void bind(PullRequestActivity pullRequestActivity) {
            otherContainer.removeAllViews();
            picasso.cancelRequest(activityAuthorAvatar);
            this.pullRequestActivity = pullRequestActivity;
            final String timestamp = new DateTime(pullRequestActivity.getCreatedDate()).toString(Consts.DATE_TIME_FORMAT);
            this.activityAuthor.setText(pullRequestActivity.getUser().getDisplayName());
            this.activityAction.setText(pullRequestActivity.getAction());
            this.activityTimestamp.setText(timestamp);
            picasso.load(pullRequestActivity.getUser().getAvatarUrl()).into(activityAuthorAvatar);

            createExtraInfoView();
        }

        private void createExtraInfoView() {
            switch (pullRequestActivity.getAction()) {
                case PullRequestActivity.ACTION_UPDATE:
                    createRescopedView();
                    break;
                case PullRequestActivity.ACTION_COMMENT:
                    if (pullRequestActivity.getDiff() != null) {
                        createCommentView();
                    } else if (pullRequestActivity.getCommentAnchor() != null) {
                        createFileCommentView();
                    } else {
                        createPullRequestCommentView();
                    }
                    break;
            }
        }

        private void createPullRequestCommentView() {
            final View view = inflater.inflate(R.layout.item_spinner_textonly, otherContainer);
            final TextView textView = (TextView) view.findViewById(R.id.title);
            textView.setText(pullRequestActivity.getComment().getText());
        }

        private void createFileCommentView() {
            final View fileComment = inflater.inflate(R.layout.item_file_comment, otherContainer);

            final TextView fileText = (TextView) fileComment.findViewById(R.id.commentedOnFile);
            final TextView commentText = (TextView) fileComment.findViewById(R.id.commentText);
            final TextView commentAuthor = (TextView) fileComment.findViewById(R.id.commentAuthor);
            final TextView commentTimestamp = (TextView) fileComment.findViewById(R.id.commentTimestamp);
            final ImageView commentAuthorImage = (ImageView) fileComment.findViewById(R.id.commentAuthorImage);

            final Comment comment = pullRequestActivity.getComment();
            fileText.setText(pullRequestActivity.getCommentAnchor().getPath());
            commentText.setText(comment.getText());
            commentAuthor.setText(comment.getAuthor().getDisplayName());
            commentTimestamp.setText(new DateTime(comment.getCreatedDate()).toString(Consts.DATE_TIME_FORMAT));
            picasso.load(comment.getAuthor().getAvatarUrl()).into(commentAuthorImage);
        }

        private void createRescopedView() {
            final LinearLayout layout = new LinearLayout(context);
            layout.setOrientation(LinearLayout.VERTICAL);
            final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            layout.setLayoutParams(params);
            for (Commit commit : pullRequestActivity.getAdded().getChangesets()) {
                final View view = inflater.inflate(R.layout.item_commit, layout);
                ((TextView) view.findViewById(R.id.commitMessage)).setText(commit.getMessage());
                ((TextView) view.findViewById(R.id.commitDigest)).setText(commit.getDisplayId());
                ((TextView) view.findViewById(R.id.commitAuthor)).setText(commit.getAuthorMetadata().getDisplayName());
                ((TextView) view.findViewById(R.id.commitTimestamp)).setText(new DateTime(commit.getAuthorTimestamp()).toString(Consts.DATE_TIME_FORMAT));

                final ImageView imageView = (ImageView) view.findViewById(R.id.commitAuthorAvatar);
                picasso.load(commit.getAuthorMetadata().getAvatarUrl()).into(imageView);
            }
            otherContainer.addView(layout);
        }

        private void createCommentView() {
            final RecyclerView recyclerView = new RecyclerView(context);
            final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            recyclerView.setLayoutParams(params);
            final LinearLayoutManager layoutManager = new LinearLayoutManager(context);
            layoutManager.setAutoMeasureEnabled(true);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(new FileDiffAdapter(context, pullRequestActivity.getDiff().getHunks(), Collections.singletonList(pullRequestActivity), picasso));
            otherContainer.addView(recyclerView);
        }
    }
}
