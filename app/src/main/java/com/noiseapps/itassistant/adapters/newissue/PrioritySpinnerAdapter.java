package com.noiseapps.itassistant.adapters.newissue;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.noiseapps.itassistant.R;
import com.noiseapps.itassistant.model.jira.issues.Priority;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PrioritySpinnerAdapter extends BaseAdapter {

    private final Context context;
    private final List<Priority> assignees;

    public PrioritySpinnerAdapter(Context context, List<Priority> assignees) {
        this.context = context;
        this.assignees = assignees;
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
    public Priority getItem(int position) {
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

    private class ViewHolder {
        private final TextView assignee;
        private final CircleImageView avatar;

        public ViewHolder(View convertView) {
            assignee = (TextView) convertView.findViewById(R.id.title);
            avatar = (CircleImageView) convertView.findViewById(R.id.avatar);
        }

        public void bind(Priority item) {
            assignee.setText(item.getName());
            Picasso.with(context).load(item.getIconUrl()).into(avatar);
        }
    }
}
