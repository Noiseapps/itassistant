package com.noiseapps.itassistant.utils.views;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.noiseapps.itassistant.R;
import com.noiseapps.itassistant.adapters.stash.DiffFileSpinnerAdapter;
import com.noiseapps.itassistant.model.stash.pullrequests.details.Diff;
import com.noiseapps.itassistant.model.stash.pullrequests.details.DiffBase;
import com.orhanobut.logger.Logger;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;


@EViewGroup(R.layout.view_diff_base)
public class DiffViewBase extends LinearLayout {

    @ViewById
    Spinner filesSpinner;

    @ViewById
    RecyclerView diffViewList;
    DiffBase diff;

    public DiffViewBase(Context context) {
        super(context);
        commonInit();
    }

    public DiffViewBase(Context context, AttributeSet attrs) {
        super(context, attrs);
        commonInit();
    }

    public DiffViewBase(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        commonInit();
    }

    public void setDiff(DiffBase diff) {
        this.diff = diff;
        setSpinnerAdapter();
        setRecyclerViewAdapter();
    }

    private void setSpinnerAdapter() {
        final DiffFileSpinnerAdapter adapter = new DiffFileSpinnerAdapter(getContext(), diff.getDiffs());
        filesSpinner.setAdapter(adapter);
        filesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                onFileItemSelected(diff.getDiffs().get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void onFileItemSelected(Diff diffBase) {
        Logger.d(diffBase.toString());
    }

    private void setRecyclerViewAdapter() {
        diffViewList.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void commonInit() {
        setOrientation(VERTICAL);


    }


}
