package com.noiseapps.itassistant.adapters.newissue;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.noiseapps.itassistant.R;
import com.noiseapps.itassistant.model.jira.issues.Assignee;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class AssigneeSpinnerAdapter extends BaseAdapter {

    private final Context context;
    private final List<Assignee> assignees = new ArrayList<>();
    private final Picasso authPicasso;

    public AssigneeSpinnerAdapter(@NonNull Context context, @NonNull List<Assignee> assignees, @NonNull Picasso authPicasso) {
        this.context = context;
        addNoUser();
        this.assignees.addAll(assignees);
        this.authPicasso = authPicasso;
    }

    private void addNoUser() {
        final Assignee none = new Assignee();
        none.setActive(true);
        none.setDisplayName(context.getString(R.string.notAssigned));
        none.setName("");
        assignees.add(none);
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public int getCount() {
        return assignees.size();
    }

    @Override
    public Assignee getItem(int position) {
        return assignees.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_spinner_title, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.bind(getItem(position));
        return convertView;
    }

    public int getPositionForAssignee(@NonNull Assignee assignee) {
        for (int i = 0; i < assignees.size(); i++) {
            final Assignee item = assignees.get(i);
            if(item.getName().equalsIgnoreCase(assignee.getName())) {
                return i;
            }
        }
        return 0;
    }

    private class ViewHolder {
        private final TextView assignee;
        private final CircleImageView avatar;

        public ViewHolder(View convertView) {
            assignee = (TextView) convertView.findViewById(R.id.title);
            avatar = (CircleImageView) convertView.findViewById(R.id.avatar);
        }

        public void bind(Assignee item) {
            assignee.setText(item.getDisplayName());
            if(item.getAvatarUrls() != null) {
                authPicasso.load(item.getAvatarUrls().get48x48()).
                        placeholder(R.drawable.ic_action_account_circle).
                        error(R.drawable.ic_action_account_circle).into(avatar);
            }
        }
    }
}
