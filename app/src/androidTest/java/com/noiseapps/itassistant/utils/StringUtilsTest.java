package com.noiseapps.itassistant.utils;

import android.test.InstrumentationTestCase;

public class StringUtilsTest extends InstrumentationTestCase {

    public void testFailOnInvalidAddress() throws Exception {
        final boolean validUrl = StringUtils.validUrl("some.dumb.test");
        assertFalse(validUrl);
    }

    public void testPassOnValid() throws Exception {
        final boolean valid = StringUtils.validUrl("http://google.com");
        assertTrue(valid);
    }
}