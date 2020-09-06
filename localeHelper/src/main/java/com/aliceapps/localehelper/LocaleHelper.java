package com.aliceapps.localehelper;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class LocaleHelper {
    private static List<String> locales;
    private static String prefName;
    private static String baseLocale = "system";

    public static Context onAttach(Context context) {
        String locale = getPersistedLocale(context);
        return setLocale(context, locale);
    }

    public static void loadLocales(String[] selectedLocales, String preferenceName) {
        locales = Arrays.asList(selectedLocales);
        prefName = preferenceName;
    }

    public static void loadLocales(String[] selectedLocales, String preferenceName, String defaultLocale) {
        locales = Arrays.asList(selectedLocales);
        prefName = preferenceName;
        baseLocale = defaultLocale;
    }

    public static void loadLocales(int resourceId, @org.jetbrains.annotations.NotNull Context context, String preferenceName) {
        locales = Arrays.asList(context.getResources().getStringArray(resourceId));
        prefName = preferenceName;
    }

    public static void loadLocales(int resourceId, @NotNull Context context, String preferenceName, String defaultLocale) {
        locales = Arrays.asList(context.getResources().getStringArray(resourceId));
        prefName = preferenceName;
        baseLocale = defaultLocale;
    }

    public static String getPersistedLocale(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(prefName, baseLocale);
    }

    @SuppressLint("ObsoleteSdkInt")
    public static Context setLocale(Context context, @NonNull String localeSpec) {
        Locale locale;
        if (localeSpec.equals("system")) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                locale = Resources.getSystem().getConfiguration().getLocales().get(0);
            } else {
                //noinspection deprecation
                locale = Resources.getSystem().getConfiguration().locale;
            }
            //If selected locale isn't available for this app (relevant for system locale), use English
            if (!locales.contains(locale.getLanguage()))
                locale = new Locale("en");

            savePreference(context, locale.getLanguage());
        } else {
            locale = new Locale(localeSpec);
        }
        Locale.setDefault(locale);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return updateResources(context, locale);
        } else {
            return updateResourcesLegacy(context, locale);
        }
    }

    @TargetApi(Build.VERSION_CODES.N)
    private static Context updateResources(@NonNull Context context, Locale locale) {
        Configuration configuration = context.getResources().getConfiguration();
        configuration.setLocale(locale);
        configuration.setLayoutDirection(locale);
        return context.createConfigurationContext(configuration);
    }

    @NotNull
    @Contract("_, _ -> param1")
    @SuppressWarnings("all")
    private static Context updateResourcesLegacy(@NonNull Context context, Locale locale) {
        Resources resources = context.getResources();

        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLayoutDirection(locale);
        }

        resources.updateConfiguration(configuration, resources.getDisplayMetrics());

        return context;
    }

    private static void savePreference(Context context, String locale) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putString(prefName,locale).apply();
    }

    protected static List<String> getLocales() {
        return locales;
    }

}