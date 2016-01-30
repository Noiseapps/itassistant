package com.noiseapps.itassistant.adapters.stash;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.noiseapps.itassistant.R;
import com.noiseapps.itassistant.model.stash.pullrequests.PullRequest;

import java.util.ArrayList;

public class PrListAdapter extends RecyclerView.Adapter<PrListAdapter.PrViewHolder> {


    private final Context context;
    private final ArrayList<PullRequest> pullRequests;
    private final LayoutInflater layoutInflater;

    public PrListAdapter(Context context, ArrayList<PullRequest> pullRequests) {
        this.context = context;
        this.pullRequests = pullRequests;
        layoutInflater = LayoutInflater.from(context);
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

        private final TextView prData;

        public PrViewHolder(View itemView) {
            super(itemView);
            prData = (TextView) itemView.findViewById(R.id.prData);
        }

        public void bind(PullRequest pullRequest) {
            prData.setText(pullRequest.toString());
        }
    }
}
