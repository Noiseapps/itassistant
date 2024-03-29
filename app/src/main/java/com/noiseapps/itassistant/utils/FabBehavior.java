package com.noiseapps.itassistant.utils;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;

import com.noiseapps.itassistant.utils.views.MyFabProgressCircle;

public class FabBehavior extends CoordinatorLayout.Behavior<MyFabProgressCircle> {

    public FabBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, MyFabProgressCircle child, View dependency) {
        return dependency instanceof Snackbar.SnackbarLayout;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, MyFabProgressCircle child, View dependency) {
        float translationY = Math.min(0, dependency.getTranslationY() - dependency.getHeight());
        child.setTranslationY(translationY);
        return true;
    }


    @Override
    public boolean onStartNestedScroll(final CoordinatorLayout coordinatorLayout, final MyFabProgressCircle child,
                                       final View directTargetChild, final View target, final int nestedScrollAxes) {
        // Ensure we react to vertical scrolling
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL
                || super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, nestedScrollAxes);

    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout,
                               MyFabProgressCircle child,
                               View target,
                               int dxConsumed,
                               int dyConsumed,
                               int dxUnconsumed,
                               int dyUnconsumed) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
        if (child.getVisibility() != View.VISIBLE) {
            return;
        }
        if (dyConsumed > 0 && !child.isCollapsed()) {
            child.collapse();
        } else if (dyConsumed < 0 && child.isCollapsed()) {
            child.expand();
        }
    }
}
