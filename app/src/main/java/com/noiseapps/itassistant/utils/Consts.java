package com.noiseapps.itassistant.utils;

import java.util.regex.Pattern;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class Consts {

    public static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormat.forPattern("YYYY-MM-dd HH:mm");
    public static final DateTimeFormatter DATE_FORMAT = DateTimeFormat.forPattern("YYYY-MM-dd");
    public static final Pattern PATTERN = Pattern.compile("([0-9]+w){0,1}( *[0-9]+d){0,1}( *[0-9]+h){0,1}( *[0-9]+m){0,1}( *[0-9]+s){0,1}", Pattern.CASE_INSENSITIVE);
}
