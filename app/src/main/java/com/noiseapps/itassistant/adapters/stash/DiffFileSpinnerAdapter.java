package com.noiseapps.itassistant.adapters.stash;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.noiseapps.itassistant.R;
import com.noiseapps.itassistant.model.stash.pullrequests.details.Diff;
import com.noiseapps.itassistant.model.stash.pullrequests.details.Ref;

import java.util.List;

public class DiffFileSpinnerAdapter extends BaseAdapter {
    private final List<Diff> refs;
    private final LayoutInflater layoutInflater;

    public DiffFileSpinnerAdapter(Context context, List<Diff> refs) {
        this.refs = refs;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return refs.size();
    }

    @Override
    public Diff getItem(int position) {
        return refs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_spinner_subtitle, parent, false);
            convertView.setTag(new ViewHolder(convertView));
        }
        final ViewHolder holder = (ViewHolder) convertView.getTag();
        holder.bind(getItem(position));
        return convertView;
    }

    class ViewHolder {

        private final TextView title, subtitle;
        private final ImageView avatar;

        public ViewHolder(View itemView) {
            avatar = (ImageView) itemView.findViewById(R.id.avatar);
            title = (TextView) itemView.findViewById(R.id.title);
            subtitle = (TextView) itemView.findViewById(R.id.subTitle);
            avatar.setVisibility(View.GONE);
        }

        public void bind(Diff diffBase) {
            Ref source = diffBase.getSource();
            if(source == null) {
                source = diffBase.getDestination();
            }
            title.setText(source.getName());
            subtitle.setText(source.toString());
        }
    }
}
