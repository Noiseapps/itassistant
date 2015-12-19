package com.noiseapps.itassistant.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.noiseapps.itassistant.R;
import com.noiseapps.itassistant.model.account.AccountTypes;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class AccountTypeListAdapter extends ArrayAdapter<String> {

    private final Context context;
    private final AccountTypeListener callbacks;

    public AccountTypeListAdapter(Context context, String[] values, AccountTypeListener callbacks) {
        super(context, R.layout.account_type_list_item, R.id.accountType, values);
        this.context = context;
        this.callbacks = callbacks;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        @AccountTypes.AccountType final int accountType = position + 1;

        final View root = super.getView(position, convertView, parent);
        final CircleImageView imageView = (CircleImageView) root.findViewById(R.id.avatarView);
        Picasso.with(context).load(AccountTypes.getAccountImageDrawable(accountType)).noFade().into(imageView);
        root.setOnClickListener(v -> callbacks.onAccountTypeSelected(accountType));
        return root;
    }

    public interface AccountTypeListener {
        void onAccountTypeSelected(@AccountTypes.AccountType int accountType);
    }
}
