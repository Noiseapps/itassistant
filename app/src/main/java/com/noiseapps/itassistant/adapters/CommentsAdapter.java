package com.noiseapps.itassistant.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.noiseapps.itassistant.R;
import com.noiseapps.itassistant.model.jira.issues.comments.Comment;
import com.noiseapps.itassistant.utils.Consts;

import org.joda.time.DateTime;

import java.util.Collections;
import java.util.List;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ViewHolder> {
    private final Context context;
    private final List<Comment> comments;

    public CommentsAdapter(Context context, List<Comment> comments) {
        this.context = context;
        this.comments = comments;
        setHasStableIds(true);
        sort();
    }

    public void sort() {
        Collections.sort(comments, this::compare);
        notifyDataSetChanged();
    }

    private int compare(Comment lhs, Comment rhs) {
        return DateTime.parse(lhs.getCreated()).compareTo(DateTime.parse(rhs.getCreated()));
    }

    public Comment getItem(int position) {
        return comments.get(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View convertView = LayoutInflater.from(context).inflate(R.layout.item_comment, parent, false);
        return new ViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public void addItem(Comment comment) {
        comments.add(comment);
        sort();
        notifyDataSetChanged();
    }

    public void addItems(List<Comment> commentList) {
        comments.addAll(commentList);
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

        public void bind(Comment item) {
            body.setText(item.getBody());
            creator.setText(item.getAuthor().getDisplayName());
            final String dateString = DateTime.parse(item.getCreated()).toString(Consts.DATE_TIME_FORMAT);
            date.setText(dateString);
        }
    }
}
