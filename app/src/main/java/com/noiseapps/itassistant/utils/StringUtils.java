package com.noiseapps.itassistant.utils;

import java.util.regex.Pattern;

public class StringUtils {

    private static final Pattern URL_PATTERN = Pattern.compile("^((http|https)://).*", Pattern.CASE_INSENSITIVE);

    public static boolean validUrl(String query) {
        return URL_PATTERN.matcher(query).matches();
    }
}
