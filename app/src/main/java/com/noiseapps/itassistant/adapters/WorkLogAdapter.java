package com.noiseapps.itassistant.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.noiseapps.itassistant.R;
import com.noiseapps.itassistant.model.jira.issues.worklog.WorkLogItem;
import com.noiseapps.itassistant.utils.Consts;

import org.joda.time.DateTime;

public class WorkLogAdapter extends BaseAdapter {

    private final Context context;
    private final ArrayList<WorkLogItem> workLogItems;

    public WorkLogAdapter(Context context, ArrayList<WorkLogItem> workLogItems) {
        this.context = context;
        this.workLogItems = workLogItems;
        sort();
    }

    public void sort() {
        Collections.sort(workLogItems, new Comparator<WorkLogItem>() {
            @Override
            public int compare(WorkLogItem lhs, WorkLogItem rhs) {
                return DateTime.parse(lhs.getStarted()).compareTo(DateTime.parse(rhs.getStarted()));
            }
        });
        notifyDataSetChanged();
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public int getCount() {
        return workLogItems.size();
    }

    @Override
    public WorkLogItem getItem(int position) {
        return workLogItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_comment, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.bind(getItem(position));
        return convertView;
    }

    public void addItem(WorkLogItem workLogItem) {
        workLogItems.add(workLogItem);
        sort();
    }

    public void setItems(List<WorkLogItem> worklogs) {
        workLogItems.clear();
        workLogItems.addAll(worklogs);
        sort();
        notifyDataSetChanged();
    }

    private class ViewHolder {
        TextView body, creator, date;

        public ViewHolder(View root) {
            body = (TextView) root.findViewById(R.id.commentBody);
            creator = (TextView) root.findViewById(R.id.commentCreator);
            date = (TextView) root.findViewById(R.id.commentDate);
        }

        public void bind(WorkLogItem item) {
            body.setText(item.getComment());
            creator.setText(context.getString(R.string.logged, item.getAuthor().getDisplayName(), item.getTimeSpent()));
            final String dateString = DateTime.parse(item.getStarted()).toString(Consts.DATE_FORMAT);
            date.setText(dateString);
        }
    }
}
