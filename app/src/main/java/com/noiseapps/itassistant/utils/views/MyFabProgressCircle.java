package com.noiseapps.itassistant.utils.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.github.jorgecastilloprz.FABProgressCircle;
import com.noiseapps.itassistant.R;

public class MyFabProgressCircle extends FABProgressCircle {
    private boolean collapsed;

    public MyFabProgressCircle(Context context) {
        super(context);
    }

    public MyFabProgressCircle(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyFabProgressCircle(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void collapse() {
        final Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.fab_scale_collapse);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                collapsed = true;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        startAnimation(animation);
    }

    public void expand() {
        final Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.fab_scale_expand);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                collapsed = false;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        startAnimation(animation);
    }

    public boolean isCollapsed() {
        return collapsed;
    }
}
