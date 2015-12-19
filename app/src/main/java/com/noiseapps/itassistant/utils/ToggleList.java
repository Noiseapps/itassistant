package com.noiseapps.itassistant.utils;

import java.util.ArrayList;

public class ToggleList<E> extends ArrayList<E> {

    public boolean toggle(E item) {
        if (contains(item)) {
            return remove(item);
        } else {
            return add(item);
        }
    }

}
