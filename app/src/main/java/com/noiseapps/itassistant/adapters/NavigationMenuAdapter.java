package com.noiseapps.itassistant.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractExpandableItemAdapter;
import com.noiseapps.itassistant.database.dao.AccountsDao;
import com.noiseapps.itassistant.database.dao.AccountsDao_;
import com.noiseapps.itassistant.model.NavigationModel;

import java.util.List;

public class NavigationMenuAdapter extends AbstractExpandableItemAdapter {

    private final Context context;
    private List<NavigationModel> navigationModels;
    private final AccountsDao accountsDao;

    public interface AdapterCallbacks {
        void onItemClicked();
    }

    public NavigationMenuAdapter(Context context, List<NavigationModel> navigationModels) {
        this.context = context;
        this.navigationModels = navigationModels;
        accountsDao = AccountsDao_.getInstance_(context);
    }

    @Override
    public int getGroupCount() {
        return navigationModels.size();
    }

    @Override
    public int getChildCount(int groupPosition) {
        return navigationModels.get(groupPosition).getJiraProjects().size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return navigationModels.get(groupPosition).getUser().hashCode();
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return navigationModels.get(groupPosition).getJiraProjects().get(childPosition).hashCode();
    }

    @Override
    public int getGroupItemViewType(int groupPosition) {
        return 0;
    }

    @Override
    public int getChildItemViewType(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public RecyclerView.ViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindGroupViewHolder(RecyclerView.ViewHolder holder, int groupPosition, int viewType) {

    }

    @Override
    public void onBindChildViewHolder(RecyclerView.ViewHolder holder, int groupPosition, int childPosition, int viewType) {

    }

    @Override
    public boolean onCheckCanExpandOrCollapseGroup(RecyclerView.ViewHolder holder, int groupPosition, int x, int y, boolean expand) {
        return false;
    }
}
