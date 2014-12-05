package com.melnykov.callmeback;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Prefs {

    private static String KEY_OPERATOR_ID = "operator_id";

    private Prefs() {
        // Prevent instantiation
    }

    public static boolean isOperatorSelected(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getInt(KEY_OPERATOR_ID, Integer.MIN_VALUE) != Integer.MIN_VALUE;
    }

    public static void saveOperatorId(Context context, int id) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putInt(KEY_OPERATOR_ID, id).apply();
    }

    public static int getOperatorId(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getInt(KEY_OPERATOR_ID, Integer.MIN_VALUE);
    }
}