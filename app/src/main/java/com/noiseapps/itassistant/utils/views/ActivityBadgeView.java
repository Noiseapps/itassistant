package com.noiseapps.itassistant.utils.views;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.TextView;

import com.noiseapps.itassistant.R;
import com.noiseapps.itassistant.model.stash.pullrequests.activities.PullRequestActivity;

public class ActivityBadgeView extends TextView {
    public ActivityBadgeView(Context context) {
        super(context);
    }

    public ActivityBadgeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ActivityBadgeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        getBackgroundColor(text);
        super.setText(text, type);
    }

    private void getBackgroundColor(CharSequence text) {
        switch (text.toString()) {
            case PullRequestActivity.ACTION_MERGED:
            case PullRequestActivity.ACTION_APPROVED:
                getDrawable(R.color.prApproved);
                break;
            case PullRequestActivity.ACTION_DECLINED:
                getDrawable(R.color.prDeclined);
                break;
            case PullRequestActivity.ACTION_UPDATE:
                getDrawable(R.color.prUpdated);
                break;
            case PullRequestActivity.ACTION_OPEN:
            case PullRequestActivity.ACTION_REOPENED:
            default:
                getDrawable(R.color.prOpened);
                break;
        }
    }

    private void getDrawable(@ColorRes int colorRes) {
        final GradientDrawable drawable = (GradientDrawable) ContextCompat.getDrawable(getContext(), R.drawable.activity_badge);
        drawable.setColor(ContextCompat.getColor(getContext(), colorRes));
        setBackground(drawable);
    }
}
