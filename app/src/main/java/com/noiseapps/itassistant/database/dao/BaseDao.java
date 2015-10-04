package com.noiseapps.itassistant.database.dao;

import com.google.gson.Gson;

import java.util.List;

public abstract class BaseDao<T> {

    final static Gson GSON = new Gson();

    public abstract void add(T object);

    public abstract boolean delete(T object);

    public abstract void update(T object);

    public abstract void save(List<T> objects);

    public abstract List<T> getAll();

    public abstract int getNextId();

}
