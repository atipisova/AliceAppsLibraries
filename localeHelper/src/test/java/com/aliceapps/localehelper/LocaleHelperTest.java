package com.aliceapps.localehelper;

import android.content.Context;
import android.content.res.Resources;

import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.annotation.LooperMode;

import java.util.List;
import java.util.Locale;

import static org.junit.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = {TestHelper.SDK_VERSION})
@LooperMode(LooperMode.Mode.PAUSED)
public class LocaleHelperTest {
    String[] locales = new String[2];
    Context mContext = InstrumentationRegistry.getInstrumentation().getContext();

    @Before
    public void SetUp() {
        locales[0] = "en";
        locales[1] = "ru";
    }

    @Test
    public void onAttach() {
        String defaultLocale = "ru";
        LocaleHelper.loadLocales(locales, "locale", defaultLocale);
        Context result = LocaleHelper.onAttach(mContext);
        Locale locale = result.getResources().getConfiguration().getLocales().get(0);
        assertEquals("ru",locale.getLanguage());
    }

    @Test
    public void loadLocales() {
        LocaleHelper.loadLocales(locales, "locale");
        List<String> savedLocales = LocaleHelper.getLocales();
        assertEquals(locales.length,savedLocales.size());
    }

    @Test
    public void testLoadLocales() {
        String defaultLocale = "ru";
        LocaleHelper.loadLocales(locales, "locale", defaultLocale);
        List<String> savedLocales = LocaleHelper.getLocales();
        assertEquals(locales.length,savedLocales.size());
    }

    @Test
    public void testLoadLocales1() {
        LocaleHelper.loadLocales(R.array.test_locales, mContext, "locale");
        List<String> savedLocales = LocaleHelper.getLocales();
        assertEquals(3,savedLocales.size());
    }

    @Test
    public void testLoadLocales2() {
        String defaultLocale = "ru";
        LocaleHelper.loadLocales(R.array.test_locales, mContext, "locale", defaultLocale);
        List<String> savedLocales = LocaleHelper.getLocales();
        assertEquals(3,savedLocales.size());
    }

    @Test
    public void getPersistedLocale() {
        String defaultLocale = "ru";
        LocaleHelper.loadLocales(R.array.test_locales, mContext, "locale", defaultLocale);
        String locale = LocaleHelper.getPersistedLocale(mContext);
        assertEquals(defaultLocale,locale);
    }

    @Test
    public void setLocaleCorrect() {
        String defaultLocale = "ru";
        LocaleHelper.loadLocales(R.array.test_locales, mContext, "locale", defaultLocale);
        LocaleHelper.setLocale(mContext,"en");
        Locale locale = Resources.getSystem().getConfiguration().getLocales().get(0);
        assertEquals("en",locale.getLanguage());
    }

    @Test
    public void setLocaleWrong() {
        String defaultLocale = "ru";
        LocaleHelper.loadLocales(R.array.test_locales, mContext, "locale", defaultLocale);
        LocaleHelper.setLocale(mContext,"fr");
        Locale locale = Resources.getSystem().getConfiguration().getLocales().get(0);
        assertEquals("en",locale.getLanguage());
    }

}