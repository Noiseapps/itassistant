package com.noiseapps.itassistant.adapters.stash;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.noiseapps.itassistant.R;
import com.noiseapps.itassistant.model.account.BaseAccount;
import com.noiseapps.itassistant.model.stash.general.StashUser;
import com.noiseapps.itassistant.utils.AuthenticatedPicasso;
import com.orhanobut.logger.Logger;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ReviewersAdapter extends BaseAdapter implements Filterable {

    private final Context context;

    private final List<StashUser> finalUsers;
    private final List<StashUser> users;
    private final LayoutInflater layoutInflater;
    private final Picasso authPicasso;

    public ReviewersAdapter(Context context, List<StashUser> users, BaseAccount baseAccount) {
        this.context = context;
        this.finalUsers = new ArrayList<>(users);
        this.users = users;
        layoutInflater = LayoutInflater.from(context);
        authPicasso = AuthenticatedPicasso.getAuthPicasso(context, baseAccount);
    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public StashUser getItem(int position) {
        return users.get(position);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_spinner_subtitle, parent, false);
            convertView.setTag(new ReviewersViewHolder(convertView));
        }

        final ReviewersViewHolder viewHolder = (ReviewersViewHolder) convertView.getTag();
        viewHolder.bind(getItem(position));
        return convertView;
    }

    @Override
    public Filter getFilter() {
        return new UserFilter();
    }

    private final class ReviewersViewHolder {
        private final ImageView avatar;
        private final TextView title, subTitle;

        ReviewersViewHolder(View view) {
            avatar = (ImageView) view.findViewById(R.id.avatar);
            title = (TextView) view.findViewById(R.id.title);
            subTitle = (TextView) view.findViewById(R.id.subTitle);
        }

        public void bind(StashUser item) {
            authPicasso.load(item.getAvatarUrl()).into(avatar);
            title.setText(item.getDisplayName());
            subTitle.setText(item.getEmailAddress());
        }
    }

    private class UserFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            final List<StashUser> filteredUsers = Stream.of(finalUsers).filter(stashUser -> {
                final String lowerConstraint = constraint.toString().toLowerCase();
                String name = stashUser.getName().toLowerCase();
                String displayName = stashUser.getDisplayName().toLowerCase();
                String emailAddress = stashUser.getEmailAddress().toLowerCase();
                return name.contains(lowerConstraint) || displayName.contains(lowerConstraint) || emailAddress.contains(lowerConstraint);
            }).collect(Collectors.toList());
            final FilterResults results = new FilterResults();
            results.values = filteredUsers;
            results.count = filteredUsers.size();
            return results;
        }

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            return ((StashUser) resultValue).getDisplayName();
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            users.clear();
            if(results.values != null) {
                //noinspection unchecked
                users.addAll((List<StashUser>) results.values);
            }
            notifyDataSetChanged();
        }
    }
}
