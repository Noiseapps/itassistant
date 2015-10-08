package com.noiseapps.itassistant.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import com.noiseapps.itassistant.R;
import com.noiseapps.itassistant.model.jira.issues.Assignee;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class AssigneeSpinnerAdapter extends BaseAdapter {

    private final Context context;
    private final List<Assignee> assignees;
    private final Picasso authPicasso;

    public AssigneeSpinnerAdapter(Context context, List<Assignee> assignees, Picasso authPicasso) {
        this.context = context;
        this.assignees = assignees;
        this.authPicasso = authPicasso;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_spinner_assignee, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.bind(getItem(position));
        return convertView;
    }

    private class ViewHolder {
        private final TextView assignee;
        private final CircleImageView avatar;

        public ViewHolder(View convertView) {
            assignee = (TextView) convertView.findViewById(R.id.assignee);
            avatar = (CircleImageView) convertView.findViewById(R.id.avatar);
        }

        public void bind(Assignee item) {
            assignee.setText(item.getDisplayName());
            authPicasso.load(item.getAvatarUrls().get48x48()).into(avatar);
        }
    }
}
