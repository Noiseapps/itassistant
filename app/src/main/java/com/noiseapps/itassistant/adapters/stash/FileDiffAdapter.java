package com.noiseapps.itassistant.adapters.stash;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.sectionedrecyclerview.SectionedRecyclerViewAdapter;
import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.annimon.stream.function.Function;
import com.noiseapps.itassistant.R;
import com.noiseapps.itassistant.model.stash.pullrequests.PullRequest;
import com.noiseapps.itassistant.model.stash.pullrequests.activities.Comment;
import com.noiseapps.itassistant.model.stash.pullrequests.activities.CommentAnchor;
import com.noiseapps.itassistant.model.stash.pullrequests.activities.PullRequestActivity;
import com.noiseapps.itassistant.model.stash.pullrequests.details.Diff;
import com.noiseapps.itassistant.model.stash.pullrequests.details.Hunk;
import com.noiseapps.itassistant.model.stash.pullrequests.details.Line;
import com.noiseapps.itassistant.model.stash.pullrequests.details.Segment;
import com.noiseapps.itassistant.utils.Consts;
import com.orhanobut.logger.Logger;
import com.squareup.picasso.Picasso;

import org.joda.time.DateTime;
import org.w3c.dom.Text;

import java.util.List;

public class FileDiffAdapter extends SectionedRecyclerViewAdapter<FileDiffAdapter.ViewHolder> {

    private final LayoutInflater layoutInflater;
    private final List<Hunk> hunks;
    private final List<PullRequestActivity> comments;
    private Context context;
    private Picasso picasso;

    public FileDiffAdapter(Context context, List<Hunk> hunks, List<PullRequestActivity> comments, Picasso picasso) {
        this.context = context;
        this.picasso = picasso;
        this.layoutInflater = LayoutInflater.from(context);
        this.comments = comments;
        this.hunks = hunks;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = layoutInflater.inflate(R.layout.item_diff_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public int getSectionCount() {
        return hunks.size();
    }

    @Override
    public int getItemCount(int i) {
        return hunks.get(i).getSegments().size();
    }

    @Override
    public void onBindHeaderViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.bindHeader();
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int section, int relativePosition, int absolutePosition) {
        final Hunk item = hunks.get(section);
        viewHolder.bindItem(item.getSegments().get(relativePosition));
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        final FrameLayout sectionHeader;
        final LinearLayout diffLineView;

        public ViewHolder(View itemView) {
            super(itemView);
            sectionHeader = (FrameLayout) itemView.findViewById(R.id.sectionHeader);
            diffLineView = (LinearLayout) itemView.findViewById(R.id.diffLineLayout);
        }

        public void bindItem(Segment segment) {
            final List<PullRequestActivity> typeComments = Stream.of(comments).
                    filter(value -> {
                        Logger.d(value.getCommentAnchor().getLineType() + " : " + segment.getType());
                        return value.getCommentAnchor().getLineType().equalsIgnoreCase(segment.getType());
                    }).collect(Collectors.toList());
            diffLineView.removeAllViews();
            final boolean isAdded = "ADDED".equalsIgnoreCase(segment.getType());
            final boolean isRemoved = "REMOVED".equalsIgnoreCase(segment.getType());
            sectionHeader.setVisibility(View.GONE);
            diffLineView.setVisibility(View.VISIBLE);
            for (Line line : segment.getLines()) {
                Logger.d(segment.getType() + " : " + typeComments.size());
                List<PullRequestActivity> activities = Stream.of(typeComments).filter(value -> {
                    final CommentAnchor commentAnchor = value.getCommentAnchor();
                    Logger.d(commentAnchor.getLineType());
                    if (isAdded) {
                        return commentAnchor.getLine() == line.getDestination();
                    } else {
                        return commentAnchor.getLine() == line.getSource();
                    }
                }).collect(Collectors.toList());

                final LinearLayout view = (LinearLayout) layoutInflater.inflate(R.layout.item_diff_line, diffLineView, false);
                final TextView sourceLine = (TextView) view.findViewById(R.id.sourceLine);
                final TextView destinationLine = (TextView) view.findViewById(R.id.destinationLine);
                final TextView lineAction = (TextView) view.findViewById(R.id.lineAction);
                final TextView diffLine = (TextView) view.findViewById(R.id.diffLine);
                final LinearLayout diffComments = (LinearLayout) view.findViewById(R.id.diffComments);

                sourceLine.setText(!isAdded ? String.valueOf(line.getSource()) : "");
                destinationLine.setText(!isRemoved ? String.valueOf(line.getDestination()) : "");
                final String action = isAdded ? "+" : isRemoved ? "-" : "";
                lineAction.setText(action);
                diffLine.setText(line.getLine());

                if(isAdded) {
                    view.setBackgroundColor(ContextCompat.getColor(context, R.color.diffAdded));
                } else if (isRemoved) {
                    view.setBackgroundColor(ContextCompat.getColor(context, R.color.diffRemoved));
                } else {
                    view.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
                }

                addComments(diffComments, activities);
                diffLineView.addView(view);
            }
        }

        private void addComments(LinearLayout view, List<PullRequestActivity> activities) {
            for (PullRequestActivity prActivity : activities) {
                final View diffView = layoutInflater.inflate(R.layout.item_diff_comment, view, false);
                final TextView commentText = (TextView) diffView.findViewById(R.id.commentText);
                final TextView commentAuthor = (TextView) diffView.findViewById(R.id.commentAuthor);
                final TextView commentTimestamp = (TextView) diffView.findViewById(R.id.commentTimestamp);
                final ImageView commentAuthorImage = (ImageView) diffView.findViewById(R.id.commentAuthorImage);

                final Comment comment = prActivity.getComment();
                commentText.setText(comment.getText());
                commentAuthor.setText(comment.getAuthor().getDisplayName());
                commentTimestamp.setText(new DateTime(comment.getCreatedDate()).toString(Consts.DATE_TIME_FORMAT));
                picasso.load(comment.getAuthor().getAvatarUrl()).into(commentAuthorImage);
                view.addView(diffView);
            }

        }

        public void bindHeader() {
            sectionHeader.setVisibility(View.VISIBLE);
            diffLineView.setVisibility(View.GONE);
        }
    }
}
