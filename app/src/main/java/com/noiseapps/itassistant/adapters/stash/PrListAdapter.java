package com.noiseapps.itassistant.adapters.stash;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.noiseapps.itassistant.R;
import com.noiseapps.itassistant.model.stash.pullrequests.PullRequest;
import com.noiseapps.itassistant.utils.Consts;

import org.joda.time.DateTime;

import java.util.ArrayList;

public class PrListAdapter extends RecyclerView.Adapter<PrListAdapter.PrViewHolder> {


    private Context context;
    private final ArrayList<PullRequest> pullRequests;
    private PrListCallbacks callbacks;
    private final LayoutInflater layoutInflater;

    public PrListAdapter(Context context, ArrayList<PullRequest> pullRequests, PrListCallbacks callbacks) {
        this.context = context;
        this.pullRequests = pullRequests;
        this.callbacks = callbacks;
        layoutInflater = LayoutInflater.from(context);
    }

    public interface PrListCallbacks {
        void onPrSelected(PullRequest pullRequest);
    }

    @Override
    public PrViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = layoutInflater.inflate(R.layout.item_pull_request, parent, false);
        return new PrViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PrViewHolder holder, int position) {
        holder.bind(pullRequests.get(position));
    }

    @Override
    public int getItemCount() {
        return pullRequests.size();
    }

    class PrViewHolder extends RecyclerView.ViewHolder {

        private final TextView prId, prTitle, prToFrom, prUpdated;
        private PullRequest pullRequest;

        public PrViewHolder(View itemView) {
            super(itemView);
            prId = (TextView) itemView.findViewById(R.id.prId);
            prTitle = (TextView) itemView.findViewById(R.id.prTitle);
            prToFrom = (TextView) itemView.findViewById(R.id.prFromTo);
            prUpdated = (TextView) itemView.findViewById(R.id.prUpdated);
            itemView.setOnClickListener(v -> callbacks.onPrSelected(pullRequest));
        }

        public void bind(PullRequest pullRequest) {
            this.pullRequest = pullRequest;
            prId.setText(context.getString(R.string.prId, pullRequest.getId()));
            prTitle.setText(pullRequest.getTitle());
            prToFrom.setText(context.getString(R.string.prFromTo, pullRequest.getFromRef(), pullRequest.getToRef()));
            prUpdated.setText(new DateTime(pullRequest.getUpdatedDate()).toString(Consts.DATE_FORMAT));
        }
    }
}
