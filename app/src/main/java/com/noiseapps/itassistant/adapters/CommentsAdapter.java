package com.noiseapps.itassistant.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.noiseapps.itassistant.R;
import com.noiseapps.itassistant.model.jira.issues.comments.Comment;
import com.noiseapps.itassistant.utils.Consts;

import org.joda.time.DateTime;

public class CommentsAdapter extends BaseAdapter {
    private final Context context;
    private final List<Comment> comments;

    public CommentsAdapter(Context context, List<Comment> comments) {
        this.context = context;
        this.comments = comments;
        sort();
    }

    public void sort() {
        Collections.sort(comments, new Comparator<Comment>() {
            @Override
            public int compare(Comment lhs, Comment rhs) {
                return DateTime.parse(lhs.getCreated()).compareTo(DateTime.parse(rhs.getCreated()));
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
        return comments.size();
    }

    @Override
    public Comment getItem(int position) {
        return comments.get(position);
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

    public void addItem(Comment comment) {
        comments.add(comment);
        sort();
    }

    public void addItems(List<Comment> commentList) {
        comments.addAll(commentList);
        sort();
    }

    private class ViewHolder {
        TextView body, creator, date;

        public ViewHolder(View root) {
            body = (TextView) root.findViewById(R.id.commentBody);
            creator = (TextView) root.findViewById(R.id.commentCreator);
            date = (TextView) root.findViewById(R.id.commentDate);
        }

        public void bind(Comment item) {
            body.setText(item.getBody());
            creator.setText(item.getAuthor().getDisplayName());
            final String dateString = DateTime.parse(item.getCreated()).toString(Consts.DATE_TIME_FORMAT);
            date.setText(dateString);
        }
    }
}
