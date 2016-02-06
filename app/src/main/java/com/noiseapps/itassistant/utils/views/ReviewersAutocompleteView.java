package com.noiseapps.itassistant.utils.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.noiseapps.itassistant.R;
import com.noiseapps.itassistant.model.stash.general.StashUser;
import com.squareup.picasso.Picasso;
import com.tokenautocomplete.TokenCompleteTextView;

import de.hdodenhof.circleimageview.CircleImageView;

public class ReviewersAutocompleteView extends TokenCompleteTextView<StashUser> {
    private LayoutInflater inflater;
    private Picasso picasso;

    public ReviewersAutocompleteView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        inflater = LayoutInflater.from(context);
    }

    public ReviewersAutocompleteView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public void setPicasso(Picasso picasso) {
        this.picasso = picasso;
    }

    @Override
    protected View getViewForObject(StashUser object) {
        final View view = inflater.inflate(R.layout.item_reviewers, (ViewGroup) getParent(), false);
        ((TextView) view.findViewById(R.id.title)).setText(object.getDisplayName());
        final CircleImageView imageView = (CircleImageView) view.findViewById(R.id.avatar);
        picasso.load(object.getAvatarUrl()).into(imageView);
        return view;
    }

    @Override
    protected StashUser defaultObject(String completionText) {
        return null;
    }
}
