package com.noiseapps.itassistant.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.noiseapps.itassistant.R;
import com.noiseapps.itassistant.model.jira.issues.worklog.WorkLogItem;
import com.noiseapps.itassistant.utils.Consts;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WorkLogAdapter extends RecyclerView.Adapter<WorkLogAdapter.ViewHolder> {

    private final Context context;
    private final ArrayList<WorkLogItem> workLogItems;

    public WorkLogAdapter(Context context, ArrayList<WorkLogItem> workLogItems) {
        this.context = context;
        this.workLogItems = workLogItems;
        sort();
        setHasStableIds(true);
    }

    public void sort() {
        Collections.sort(workLogItems, this::compare);
        notifyDataSetChanged();
    }

    private int compare(WorkLogItem lhs, WorkLogItem rhs) {
        return DateTime.parse(lhs.getStarted()).compareTo(DateTime.parse(rhs.getStarted()));
    }

    public WorkLogItem getItem(int position) {
        return workLogItems.get(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = LayoutInflater.from(context).inflate(R.layout.item_comment, parent, false);
        return new ViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    @Override
    public int getItemCount() {
        return workLogItems.size();
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

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView body, creator, date;

        public ViewHolder(View root) {
            super(root);
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
