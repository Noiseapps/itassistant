package com.noiseapps.itassistant.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import com.noiseapps.itassistant.R;
import com.noiseapps.itassistant.model.account.AccountTypes;
import com.noiseapps.itassistant.model.account.BaseAccount;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import de.hdodenhof.circleimageview.CircleImageView;

public class AccountListAdapter extends RecyclerView.Adapter<AccountListAdapter.ViewHolder> {

    private final List<BaseAccount> dataSet;
    private final Context context;

    public AccountListAdapter(Context context, List<BaseAccount> dataSet) {
        this.dataSet = dataSet;
        this.context = context;
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

    class ViewHolder extends RecyclerView.ViewHolder {
        final TextView username;
        final TextView url;
        final CircleImageView avatarView;

        public ViewHolder(View convertView) {
            super(convertView);
            username = (TextView) convertView.findViewById(R.id.listItemTitle);
            url = (TextView) convertView.findViewById(R.id.listItemSubTitle);
            avatarView = (CircleImageView) convertView.findViewById(R.id.avatarView);
        }

        void build(BaseAccount item) {
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
    }
}
