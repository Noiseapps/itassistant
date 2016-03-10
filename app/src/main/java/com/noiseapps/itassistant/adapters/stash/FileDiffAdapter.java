package com.noiseapps.itassistant.adapters.stash;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.sectionedrecyclerview.SectionedRecyclerViewAdapter;
import com.noiseapps.itassistant.R;
import com.noiseapps.itassistant.model.stash.pullrequests.details.Diff;
import com.noiseapps.itassistant.model.stash.pullrequests.details.Hunk;
import com.noiseapps.itassistant.model.stash.pullrequests.details.Line;
import com.noiseapps.itassistant.model.stash.pullrequests.details.Segment;

import java.util.List;

public class FileDiffAdapter extends SectionedRecyclerViewAdapter<FileDiffAdapter.ViewHolder> {

    private final LayoutInflater layoutInflater;
    private final List<Hunk> hunks;
    private Context context;

    public FileDiffAdapter(Context context, List<Hunk> hunks) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
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
            diffLineView.removeAllViews();
            final boolean isAdded = "ADDED".equalsIgnoreCase(segment.getType());
            final boolean isRemoved = "REMOVED".equalsIgnoreCase(segment.getType());
            sectionHeader.setVisibility(View.GONE);
            diffLineView.setVisibility(View.VISIBLE);
            for (Line line : segment.getLines()) {
                final View view = layoutInflater.inflate(R.layout.item_diff_line, diffLineView, false);
                TextView sourceLine = (TextView) view.findViewById(R.id.sourceLine);
                TextView destinationLine = (TextView) view.findViewById(R.id.destinationLine);
                TextView lineAction = (TextView) view.findViewById(R.id.lineAction);
                TextView diffLine = (TextView) view.findViewById(R.id.diffLine);

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
                diffLineView.addView(view);
            }
        }

        public void bindHeader() {
            sectionHeader.setVisibility(View.VISIBLE);
            diffLineView.setVisibility(View.GONE);
        }
    }
}
