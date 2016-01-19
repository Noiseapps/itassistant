package com.noiseapps.itassistant.adapters.stash;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.noiseapps.itassistant.R;
import com.noiseapps.itassistant.model.stash.projects.Commit;
import com.noiseapps.itassistant.utils.Consts;

import org.joda.time.DateTime;

import java.util.List;

public class CommitListAdapter extends RecyclerView.Adapter<CommitListAdapter.ViewHolder> {

    private final Context context;
    private final List<Commit> commits;
    private final LayoutInflater layoutInflater;

    public CommitListAdapter(Context context, List<Commit> commits) {
        this.context = context;
        this.commits = commits;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = layoutInflater.inflate(R.layout.item_commit, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(commits.get(position));
    }

    @Override
    public int getItemCount() {
        return commits.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView commitMessage, commitAuthor, commitDigest, commitTimestamp;

        public ViewHolder(View itemView) {
            super(itemView);
            commitMessage = (TextView) itemView.findViewById(R.id.commitMessage);
            commitAuthor = (TextView) itemView.findViewById(R.id.commitAuthor);
            commitDigest = (TextView) itemView.findViewById(R.id.commitDigest);
            commitTimestamp = (TextView) itemView.findViewById(R.id.commitTimestamp);
        }

        public void bind(Commit commit) {
            commitMessage.setText(commit.getMessage());
            commitAuthor.setText(commit.getAuthorMetadata().getDisplayName());
            commitDigest.setText(commit.getDisplayId());
            commitTimestamp.setText(new DateTime(commit.getAuthorTimestamp()).toString(Consts.DATE_TIME_FORMAT));
        }
    }
}
