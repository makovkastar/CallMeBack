package com.melnykov.callmeback;

import android.content.Context;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.drawable.Drawable;

public class Utils {

    private Utils() {
        // Prevent instantiation
    }

    public static Drawable getColoredDrawable(Context context, int whiteDrawableResId, int targetColorResId) {
        Drawable drawable = context.getResources().getDrawable(whiteDrawableResId);
        int color = context.getResources().getColor(targetColorResId);
        ColorFilter filter = new LightingColorFilter(color, 0);
        drawable.mutate().setColorFilter(filter);
        return drawable;
    }
}