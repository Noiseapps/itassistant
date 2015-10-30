package com.noiseapps.itassistant.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import com.h6ah4i.android.widget.advrecyclerview.swipeable.RecyclerViewSwipeManager;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.SwipeableItemAdapter;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.action.SwipeResultAction;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.action.SwipeResultActionRemoveItem;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractSwipeableItemViewHolder;
import com.noiseapps.itassistant.R;
import com.noiseapps.itassistant.model.account.AccountTypes;
import com.noiseapps.itassistant.model.account.BaseAccount;
import com.orhanobut.logger.Logger;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import de.hdodenhof.circleimageview.CircleImageView;

public class AccountListAdapter extends RecyclerView.Adapter<AccountListAdapter.ViewHolder> implements SwipeableItemAdapter<AccountListAdapter.ViewHolder> {

    private final List<BaseAccount> dataSet;
    private final Context context;
    private final AccountListCallbacks callbacks;

    public interface AccountListCallbacks {
        void onItemSelected(BaseAccount account);
        void onItemRemoved(BaseAccount account);
    }

    public AccountListAdapter(@NonNull Context context, @NonNull List<BaseAccount> dataSet, @NonNull AccountListCallbacks callbacks) {
        this.dataSet = dataSet;
        this.context = context;
        this.callbacks = callbacks;
    }

    @Override
    public int onGetSwipeReactionType(ViewHolder holder, int position, int x, int y) {
        Logger.d(String.valueOf(position));
        return RecyclerViewSwipeManager.REACTION_CAN_SWIPE_BOTH_H;
    }

    @Override
    public void onSetSwipeBackground(ViewHolder holder, int position, int type) {

    }

    @Override
    public SwipeResultAction onSwipeItem(ViewHolder holder, int position, int result) {
        if(result != RecyclerViewSwipeManager.RESULT_CANCELED) {
            Logger.d(String.valueOf(position));
            return new SwipeAction(position);
        }
        return null;
    }

    private class SwipeAction extends SwipeResultActionRemoveItem {

        private final int position;

        public SwipeAction(int position) {
            this.position = position;
        }

        @Override
        protected void onPerformAction() {
            super.onPerformAction();
            callbacks.onItemRemoved(dataSet.get(position));
            notifyItemRemoved(position);
        }
    }

    @Override
    public long getItemId(int position) {
        return dataSet.get(position).hashCode();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemLayout = LayoutInflater.from(context).inflate(R.layout.account_list_item, parent, false);
        return new ViewHolder(itemLayout);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.build(dataSet.get(position));
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    class ViewHolder extends AbstractSwipeableItemViewHolder {
        final TextView username;
        final TextView url;
        final CircleImageView avatarView;
        private BaseAccount item;

        public ViewHolder(View convertView) {
            super(convertView);
            username = (TextView) convertView.findViewById(R.id.listItemTitle);
            url = (TextView) convertView.findViewById(R.id.listItemSubTitle);
            avatarView = (CircleImageView) convertView.findViewById(R.id.avatarView);
            convertView.setOnClickListener(v -> callbacks.onItemSelected(item));
        }

        void build(BaseAccount item) {
            this.item = item;
            Picasso.with(context).cancelRequest(avatarView);
            username.setText(item.getUsername());
            url.setText(item.getUrl());
            final RequestCreator picassoLoad;
            if(item.getAccountType() == AccountTypes.ACC_JIRA || item.getAccountType() == AccountTypes.ACC_STASH) {
                picassoLoad = Picasso.with(context).load("file:" + item.getAvatarPath());
            } else {
                picassoLoad = Picasso.with(context).load(R.drawable.jenkins);
            }
            picassoLoad.noFade().
                    placeholder(R.drawable.ic_action_account_circle).
                    error(R.drawable.ic_action_account_circle).
                    into(avatarView);
        }

        @Override
        public View getSwipeableContainerView() {
            return itemView;
        }
    }
}
