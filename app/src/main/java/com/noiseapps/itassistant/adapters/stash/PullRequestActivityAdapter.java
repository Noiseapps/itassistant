package com.noiseapps.itassistant.adapters.stash;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.noiseapps.itassistant.R;
import com.noiseapps.itassistant.model.stash.pullrequests.activities.PullRequestActivity;
import com.noiseapps.itassistant.utils.AuthenticatedPicasso;
import com.noiseapps.itassistant.utils.Consts;
import com.squareup.picasso.Picasso;

import org.joda.time.DateTime;

import java.util.List;

public class PullRequestActivityAdapter extends RecyclerView.Adapter<PullRequestActivityAdapter.ViewHolder> {

    private final List<PullRequestActivity> activities;
    private Picasso picasso;
    private final LayoutInflater inflater;

    public PullRequestActivityAdapter(Context context, List<PullRequestActivity> activities, Picasso picasso) {
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

        private TextView activityAuthor, activityAction, activityTimestamp;
        private ImageView activityAuthorAvatar;

        public ViewHolder(View itemView) {
            super(itemView);
            this.activityAuthor = (TextView) itemView.findViewById(R.id.activityAuthor);
            this.activityAction = (TextView) itemView.findViewById(R.id.activityBadge);
            this.activityTimestamp = (TextView) itemView.findViewById(R.id.activityTimestamp);
            this.activityAuthorAvatar = (ImageView) itemView.findViewById(R.id.activityAuthorAvatar);
        }

        public void bind(PullRequestActivity pullRequestActivity) {
            final String timestamp = new DateTime(pullRequestActivity.getCreatedDate()).toString(Consts.DATE_TIME_FORMAT);
            this.activityAuthor.setText(pullRequestActivity.getUser().getDisplayName());
            this.activityAction.setText(pullRequestActivity.getAction());
            this.activityTimestamp.setText(timestamp);
            picasso.load(pullRequestActivity.getUser().getAvatarUrl()).into(activityAuthorAvatar);
        }
    }
}
