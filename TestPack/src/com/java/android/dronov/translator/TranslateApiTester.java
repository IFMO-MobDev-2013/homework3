package com.java.android.dronov.translator;

import android.test.InstrumentationTestCase;
import com.ifmo.gosugdrTeam.lesson3.background.WordTranslator;

/**
 * This is a simple framework for a test of an Application.  See
 * {@link android.test.ApplicationTestCase ApplicationTestCase} for more information on
 * how to write and extend Application tests.
 * <p/>
 * To run this test, you can type:
 * adb shell am instrument -w \
 * -e class com.java.android.dronov.translator.TranslateApiTester \
 * com.java.android.dronov.translator.tests/android.test.InstrumentationTestRunner
 */
public class TranslateApiTester extends InstrumentationTestCase {

    public void testTranslate() throws Exception {
        WordTranslator wordTranslator = new WordTranslator("mother");
        String result = wordTranslator.getAnswer();
        assertEquals(result, "мать");
    }
}
