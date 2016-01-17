package com.gg.robot.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PrefUtils {

    private static SharedPreferences getSharedPreferences(final Context context) {
        // return context.getSharedPreferences("name", Context.MODE_PRIVATE);
        return PreferenceManager.getDefaultSharedPreferences(context);
    }


    public static boolean isFirstTime(Context context, String key) {
        if (getBoolean(context, key, false)) {
            return false;
        } else {
            putBoolean(context, key, true);
            return true;
        }
    }

    public static boolean contains(Context context, String key) {
        return PrefUtils.getSharedPreferences(context).contains(key);
    }

    public static int getInt(final Context context, final String key, final int defaultValue) {
        return PrefUtils.getSharedPreferences(context).getInt(key, defaultValue);
    }

    public static boolean putInt(final Context context, final String key, final int pValue) {
        final SharedPreferences.Editor editor = PrefUtils.getSharedPreferences(context).edit();

        editor.putInt(key, pValue);

        return editor.commit();
    }

    public static long getLong(final Context context, final String key, final long defaultValue) {
        return PrefUtils.getSharedPreferences(context).getLong(key, defaultValue);
    }

    public static Long getLong(final Context context, final String key, final Long defaultValue) {
        if (PrefUtils.getSharedPreferences(context).contains(key)) {
            return PrefUtils.getSharedPreferences(context).getLong(key, 0);
        } else {
            return null;
        }
    }

    public static boolean putLong(final Context context, final String key, final long pValue) {
        final SharedPreferences.Editor editor = PrefUtils.getSharedPreferences(context).edit();

        editor.putLong(key, pValue);

        return editor.commit();
    }

    public static boolean getBoolean(final Context context, final String key, final boolean defaultValue) {
        return PrefUtils.getSharedPreferences(context).getBoolean(key, defaultValue);
    }

    public static boolean putBoolean(final Context context, final String key, final boolean pValue) {
        final SharedPreferences.Editor editor = PrefUtils.getSharedPreferences(context).edit();

        editor.putBoolean(key, pValue);

        return editor.commit();
    }

    public static String getString(final Context context, final String key, final String defaultValue) {
        return PrefUtils.getSharedPreferences(context).getString(key, defaultValue);
    }

    public static boolean putString(final Context context, final String key, final String pValue) {
        final SharedPreferences.Editor editor = PrefUtils.getSharedPreferences(context).edit();

        editor.putString(key, pValue);

        return editor.commit();
    }

    public static boolean remove(final Context context, final String key) {
        final SharedPreferences.Editor editor = PrefUtils.getSharedPreferences(context).edit();

        editor.remove(key);

        return editor.commit();
    }
}
