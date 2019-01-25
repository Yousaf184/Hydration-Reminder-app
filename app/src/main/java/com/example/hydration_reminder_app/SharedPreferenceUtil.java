package com.example.hydration_reminder_app;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SharedPreferenceUtil {

    private static final String KEY_WATER_COUNT = "water-count";
    private static final int DEFAULT_WATER_COUNT = 1;
    private static final int RESET_WATER_COUNT = -1;

    public static void incrementWaterCount(Context context) {
        int savedWaterCount = SharedPreferenceUtil.getWaterCountFromSharedPrefs(context);

        if(savedWaterCount == -1) {
            SharedPreferenceUtil.saveWaterCount(context, DEFAULT_WATER_COUNT);
        } else {
            savedWaterCount += 1;
            SharedPreferenceUtil.saveWaterCount(context, savedWaterCount);
        }
    }

    public static int getWaterCountFromSharedPrefs(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getInt(KEY_WATER_COUNT, -1);
    }

    private static void saveWaterCount(Context context, int waterCount) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putInt(KEY_WATER_COUNT, waterCount);
        editor.apply();
    }

    public static void resetWaterCount(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putInt(KEY_WATER_COUNT, RESET_WATER_COUNT);
        editor.apply();
    }
}
