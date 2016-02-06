package com.noiseapps.itassistant.adapters.stash;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.h6ah4i.android.widget.advrecyclerview.swipeable.RecyclerViewSwipeManager;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.SwipeableItemAdapter;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.SwipeableItemConstants;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.action.SwipeResultAction;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.action.SwipeResultActionDefault;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.action.SwipeResultActionRemoveItem;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractSwipeableItemViewHolder;
import com.noiseapps.itassistant.R;
import com.noiseapps.itassistant.model.stash.branches.BranchModel;
import com.noiseapps.itassistant.utils.Consts;

import org.joda.time.DateTime;

import java.util.List;

public class BranchListAdapter extends RecyclerView.Adapter<BranchListAdapter.BranchViewHolder>
        implements SwipeableItemAdapter<BranchListAdapter.BranchViewHolder> {

    private final Context context;
    private final List<BranchModel> branches;
    private final BranchListCallbacks callbacks;
    private final LayoutInflater inflater;
    private int maxBehind, maxAhead;

    public BranchListAdapter(@NonNull Context context, @NonNull List<BranchModel> branches, @NonNull BranchListCallbacks callbacks) {
        this.context = context;
        this.branches = branches;
        this.callbacks = callbacks;
        inflater = LayoutInflater.from(context);
        setHasStableIds(true);


        setMaxBehindAheadValues(branches);

    }

    private void setMaxBehindAheadValues(@NonNull List<BranchModel> branches) {
        for (BranchModel branch : branches) {
            BranchModel.Metadata metadata = branch.getMetadata();
            if(metadata != null && metadata.getBehindAheadMetadata() != null) {
                int ahead = metadata.getBehindAheadMetadata().getAhead();
                int behind = metadata.getBehindAheadMetadata().getBehind();
                if (ahead > maxAhead) maxAhead = ahead;
                if (behind > maxBehind) maxBehind = behind;
            }
        }
    }

    @Override
    public BranchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = inflater.inflate(R.layout.item_branch, parent, false);
        return new BranchViewHolder(view);
    }

    @Override
    public long getItemId(int position) {
        return branches.get(position).getId().hashCode();
    }

    @Override
    public void onBindViewHolder(BranchViewHolder holder, int position) {
        holder.bind(branches.get(position));
    }

    @Override
    public int getItemCount() {
        return branches.size();
    }


    @Override
    public int onGetSwipeReactionType(BranchViewHolder holder, int position, int x, int y) {
        return RecyclerViewSwipeManager.REACTION_CAN_SWIPE_BOTH_H;
    }

    @Override
    public void onSetSwipeBackground(BranchViewHolder holder, int position, int type) {
        int bgRes = 0;
        switch (type) {
            case SwipeableItemConstants.DRAWABLE_SWIPE_NEUTRAL_BACKGROUND:
                bgRes = R.color.white;
                break;
            case SwipeableItemConstants.DRAWABLE_SWIPE_LEFT_BACKGROUND:
            case SwipeableItemConstants.DRAWABLE_SWIPE_RIGHT_BACKGROUND:
                bgRes = R.drawable.item_swipe_delete;
                break;
        }

        holder.itemView.setBackgroundResource(bgRes);
    }

    @Override
    public SwipeResultAction onSwipeItem(BranchViewHolder holder, int position, int result) {
        if (result != RecyclerViewSwipeManager.RESULT_CANCELED) {
            return new SwipeAction(position);
        }
        return new DoNothingAction();
    }

    public interface BranchListCallbacks {
        void onItemClicked(BranchModel branchModel);

        void onItemRemoved(BranchModel branchModel);
    }


    private class SwipeAction extends SwipeResultActionRemoveItem {

        private final int position;

        public SwipeAction(int position) {
            this.position = position;
        }

        @Override
        protected void onPerformAction() {
            callbacks.onItemRemoved(branches.get(position));
            notifyItemRemoved(position);
        }
    }

    private class DoNothingAction extends SwipeResultActionDefault {
        @Override
        protected void onPerformAction() {
        }
    }

    public class BranchViewHolder extends AbstractSwipeableItemViewHolder {

        private final TextView branchName, branchCommit, branchLastUpdate, aheadValue, behindValue;
        private final ProgressBar progressBehind, progressAhead;
        private final View rootView, relationMetadata;
        private BranchModel branchModel;

        public BranchViewHolder(View itemView) {
            super(itemView);
            rootView = itemView.findViewById(R.id.listItemRoot);
            relationMetadata = itemView.findViewById(R.id.relationMetadata);
            branchName = (TextView) itemView.findViewById(R.id.branchName);
            branchCommit = (TextView) itemView.findViewById(R.id.branchLatestCommit);
            branchLastUpdate = (TextView) itemView.findViewById(R.id.branchLastUpdated);
            aheadValue = (TextView) itemView.findViewById(R.id.aheadValue);
            behindValue = (TextView) itemView.findViewById(R.id.behindValue);
            progressAhead = (ProgressBar) itemView.findViewById(R.id.progressAhead);
            progressBehind = (ProgressBar) itemView.findViewById(R.id.progressBehind);
            itemView.setOnClickListener(v -> {
                callbacks.onItemClicked(branchModel);
            });
        }

        public void bind(BranchModel branchModel) {
            branchLastUpdate.setVisibility(View.GONE);
            relationMetadata.setVisibility(View.GONE);
            this.branchModel = branchModel;
            branchName.setText(branchModel.getDisplayId());
            final String format = branchModel.getLatestChangeset().substring(0,7) + "…";
            branchCommit.setText(format);
            final BranchModel.Metadata metadata = branchModel.getMetadata();
            if (metadata != null) {
                setChangesetMetadata(metadata);
                setBehindAheadMetadata(metadata);
            }
        }

        private void setBehindAheadMetadata(BranchModel.Metadata metadata) {
            final BranchModel.BehindAheadMetadata relationMetadata = metadata.getBehindAheadMetadata();
            if(relationMetadata != null) {
                this.relationMetadata.setVisibility(View.VISIBLE);

                int ahead = relationMetadata.getAhead();
                progressAhead.setMax(maxAhead);
                progressAhead.setProgress(ahead);
                aheadValue.setText(String.valueOf(ahead));

                int behind = relationMetadata.getBehind();
                progressBehind.setMax(maxBehind);
                progressBehind.setProgress(behind);
                behindValue.setText(String.valueOf(behind));
            }
        }

        private void setChangesetMetadata(BranchModel.Metadata metadata) {
            final BranchModel.LatestChangesetMetadata changesetMetadata = metadata.getChangesetMetadata();
            if (changesetMetadata != null) {
                branchLastUpdate.setVisibility(View.VISIBLE);
                final long authorTimestamp = changesetMetadata.getAuthorTimestamp();
                final String updatedDate = new DateTime(authorTimestamp).toString(Consts.DATE_TIME_FORMAT);
                final String name = changesetMetadata.getAuthor().getName();
                final String updatedInfo = String.format("%s\n%s", updatedDate, name);
                branchLastUpdate.setText(updatedInfo);
            }
        }

        @Override
        public View getSwipeableContainerView() {
            return rootView;
        }
    }
}
