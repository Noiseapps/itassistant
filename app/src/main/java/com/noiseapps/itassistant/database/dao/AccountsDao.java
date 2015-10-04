package com.noiseapps.itassistant.database.dao;

import com.google.gson.reflect.TypeToken;
import com.noiseapps.itassistant.database.Preferences_;
import com.noiseapps.itassistant.model.account.BaseAccount;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


@EBean(scope = EBean.Scope.Singleton)
public class AccountsDao extends BaseDao<BaseAccount> {

    private List<BaseAccount> allItems = new ArrayList<>();
    public static final Type ACCOUNT_TYPE = new TypeToken<List<BaseAccount>>() {
    }.getType();


    @Pref
    Preferences_ preferences;

    @AfterInject
    void init() {
        allItems = getAll();
    }

    @Override
    public void add(BaseAccount object) {
        allItems.add(object);
        save(allItems);
    }

    @Override
    public boolean delete(BaseAccount object) {
        final boolean deleted = allItems.remove(object);
        save(allItems);
        return deleted;
    }

    @Override
    public void update(BaseAccount object) {
        for (int i = 0; i < allItems.size(); i++) {
            if(allItems.get(i).getId() == object.getId()) {
                allItems.set(i, object);
                break;
            }
        }
        save(allItems);
    }

    @Override
    public void save(List<BaseAccount> objects) {
        final String json = GSON.toJson(objects);
        preferences.accounts().put(json);
    }

    @Override
    public List<BaseAccount> getAll() {
        if(allItems.isEmpty()) {
            allItems = parseGetAll();
        }
        return allItems;
    }

    private List<BaseAccount> parseGetAll() {
        final String json = preferences.accounts().get();
        return GSON.fromJson(json, ACCOUNT_TYPE);
    }

    @Override
    public int getNextId() {
        int maxId = 0;
        final List<BaseAccount> all = getAll();
        for (BaseAccount account : all) {
            if(account.getId() > maxId) {
                maxId = account.getId();
            }
        }

        return maxId + 1;
    }
}
