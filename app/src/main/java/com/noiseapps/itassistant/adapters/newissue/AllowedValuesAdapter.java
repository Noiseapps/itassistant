package com.noiseapps.itassistant.adapters.newissue;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.noiseapps.itassistant.R;
import com.noiseapps.itassistant.model.jira.projects.createmeta.AllowedValue;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AllowedValuesAdapter extends BaseAdapter {

    private final Context context;
    private final List<AllowedValue> allowedValues = new ArrayList<>();

    public AllowedValuesAdapter(Context context, List<AllowedValue> allowedValues) {
        this.context = context;
        this.allowedValues.addAll(allowedValues);
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public int getCount() {
        return allowedValues.size();
    }

    @Override
    public AllowedValue getItem(int position) {
        return allowedValues.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public int getPositionForValue(String value) {
        for (int i = 0; i < allowedValues.size(); i++) {
            final AllowedValue item = allowedValues.get(i);
            if (item.getName().equalsIgnoreCase(value)) {
                return i;
            }
        }
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_spinner_title, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.bind(getItem(position));
        return convertView;
    }

    public void setItems(List<AllowedValue> allowedValues) {
        this.allowedValues.clear();
        this.allowedValues.addAll(allowedValues);
        notifyDataSetChanged();
    }

    private class ViewHolder {
        private final TextView assignee;
        private final CircleImageView avatar;

        public ViewHolder(View convertView) {
            assignee = (TextView) convertView.findViewById(R.id.title);
            avatar = (CircleImageView) convertView.findViewById(R.id.avatar);
        }

        public void bind(AllowedValue item) {
            assignee.setText(item.getName());
            Picasso.with(context).load(item.getIconUrl()).into(avatar);
        }
    }
}
