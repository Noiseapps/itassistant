package com.noiseapps.itassistant.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ListMultimap<K,V> extends HashMap<K, List<V>> {

    public static <K, V> ListMultimap<K,V> create() {
        return new ListMultimap<>();
    }

    private ListMultimap() {
    }

    public void putItem(K key, V value) {
        if (get(key) == null) {
            put(key, new ArrayList<>());
        }
        get(key).add(value);
    }
}
