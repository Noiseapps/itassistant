package com.noiseapps.itassistant.adapters.newissue;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.noiseapps.itassistant.R;
import com.noiseapps.itassistant.model.jira.projects.createmeta.IssueType;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class TypeSpinnerAdapter extends BaseAdapter {

    private final Context context;
    private final List<IssueType> issueTypes;

    public TypeSpinnerAdapter(Context context, List<IssueType> issueTypes) {
        this.context = context;
        this.issueTypes = issueTypes;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public int getCount() {
        return issueTypes.size();
    }

    @Override
    public IssueType getItem(int position) {
        return issueTypes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_spinner_subtitle, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.bind(getItem(position));
        return convertView;
    }

    private class ViewHolder {
        private final TextView title;
        private final TextView subtitle;
        private final ImageView avatar;

        public ViewHolder(View convertView) {
            avatar = (ImageView) convertView.findViewById(R.id.avatar);
            title = (TextView) convertView.findViewById(R.id.title);
            subtitle = (TextView) convertView.findViewById(R.id.subTitle);
        }

        public void bind(IssueType item) {
            title.setText(item.getName());
            subtitle.setText(item.getDescription());
            Picasso.with(context).load(item.getIconUrl()).into(avatar);
        }
    }
}
