package com.noiseapps.itassistant.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractExpandableItemAdapter;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractExpandableItemViewHolder;
import com.noiseapps.itassistant.R;
import com.noiseapps.itassistant.model.NavigationModel;
import com.noiseapps.itassistant.model.account.BaseAccount;
import com.noiseapps.itassistant.model.atlassian.AbstractBaseProject;
import com.noiseapps.itassistant.utils.AuthenticatedPicasso;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import de.hdodenhof.circleimageview.CircleImageView;

public class NavigationMenuAdapter extends AbstractExpandableItemAdapter<NavigationMenuAdapter.ParentViewHolder, NavigationMenuAdapter.ChildViewHolder> {

    private final Context context;
    private List<NavigationModel> navigationModels;
    private final AdapterCallbacks callbacks;
    private final LayoutInflater layoutInflater;

    public NavigationMenuAdapter(Context context, List<NavigationModel> navigationModels, AdapterCallbacks callbacks) {
        this.context = context;
        this.navigationModels = navigationModels;
        this.callbacks = callbacks;
        layoutInflater = LayoutInflater.from(context);
        setHasStableIds(true);
    }

    @Override
    public int getGroupCount() {
        return navigationModels.size();
    }

    @Override
    public int getChildCount(int groupPosition) {
        return navigationModels.get(groupPosition).getJiraProjects().length;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return navigationModels.get(groupPosition).getBaseAccount().getId();
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return navigationModels.get(groupPosition).getJiraProjects()[childPosition].hashCode();
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
    public ParentViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        final View root = layoutInflater.inflate(R.layout.item_account, parent, false);
        return new ParentViewHolder(root);
    }

    @Override
    public ChildViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        final View root = layoutInflater.inflate(R.layout.item_project, parent, false);
        return new ChildViewHolder(root);
    }

    @Override
    public void onBindGroupViewHolder(ParentViewHolder holder, int groupPosition, int viewType) {
        final NavigationModel navigationModel = navigationModels.get(groupPosition);
        holder.bind(navigationModel.getBaseAccount());
    }

    @Override
    public void onBindChildViewHolder(ChildViewHolder holder, int groupPosition, int childPosition, int viewType) {
        final NavigationModel navigationModel = navigationModels.get(groupPosition);
        holder.bind(navigationModel.getJiraProjects()[childPosition], navigationModel.getBaseAccount());
    }

    @Override
    public boolean onCheckCanExpandOrCollapseGroup(ParentViewHolder holder, int groupPosition, int x, int y, boolean expand) {
        return true;
    }

    public boolean isEmpty() {
        return navigationModels.isEmpty();
    }

    public interface AdapterCallbacks {
        void onItemClicked(AbstractBaseProject jiraProject, BaseAccount baseAccount);
    }

    abstract class AbstractViewHolder extends AbstractExpandableItemViewHolder {

        public AbstractViewHolder(View itemView) {
            super(itemView);
        }
    }

    class ParentViewHolder extends AbstractViewHolder {

        private final TextView accountName;
        private final CircleImageView avatarImage;
        Bitmap avatarBitmap;

        public ParentViewHolder(View itemView) {
            super(itemView);
            accountName = (TextView) itemView.findViewById(R.id.accountName);
            avatarImage = (CircleImageView) itemView.findViewById(R.id.avatar);
            itemView.callOnClick();
        }
        public void bind(BaseAccount baseAccount) {
            Picasso.with(context).cancelRequest(avatarImage);
            accountName.setText(baseAccount.getName());
            if(avatarBitmap == null) {
                if(baseAccount.getAvatarPath().isEmpty()) {
                    AuthenticatedPicasso.getAuthPicasso(context, baseAccount).
                            load(baseAccount.getAvatarPath()).
                            placeholder(R.drawable.ic_action_account_circle).
                            error(R.drawable.ic_action_account_circle).into(new LoadTarget());
                } else {
                    Picasso.with(context).load("file:" + baseAccount.getAvatarPath()).
                            placeholder(R.drawable.ic_action_account_circle).
                            error(R.drawable.ic_action_account_circle).into(new LoadTarget());
                }
            } else {
                avatarImage.setImageBitmap(avatarBitmap);
            }
        }

        private class LoadTarget implements Target {

            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                avatarBitmap = bitmap;
                avatarImage.setImageBitmap(bitmap);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        }
    }

    class ChildViewHolder extends AbstractViewHolder implements View.OnClickListener {
        private final TextView projectName;
        private final TextView projectKey;
        private AbstractBaseProject jiraProject;
        private BaseAccount baseAccount;

        public ChildViewHolder(View itemView) {
            super(itemView);
            projectName = (TextView) itemView.findViewById(R.id.projectName);
            projectKey = (TextView) itemView.findViewById(R.id.projectKey);
            itemView.setOnClickListener(this);
        }

        public void bind(AbstractBaseProject jiraProject, BaseAccount baseAccount) {
            this.jiraProject = jiraProject;
            this.baseAccount = baseAccount;
            projectKey.setText(jiraProject.getKey());
            projectName.setText(jiraProject.getName());
        }

        @Override
        public void onClick(View v) {
            callbacks.onItemClicked(jiraProject, baseAccount);
        }
    }
}
