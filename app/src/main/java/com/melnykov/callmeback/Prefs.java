package com.melnykov.callmeback;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Prefs {

    private static String KEY_OPERATOR_INDEX = "operator_index";

    private Prefs() {
    } // Prevent instantiation

    public static boolean isOperatorSelected(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getInt(KEY_OPERATOR_INDEX, Integer.MIN_VALUE) != Integer.MIN_VALUE;
    }

    public static void saveOperatorIndex(Context context, int index) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putInt(KEY_OPERATOR_INDEX, index).apply();
    }

    public static int getOperatorIndex(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getInt(KEY_OPERATOR_INDEX, Integer.MIN_VALUE);
    }
}