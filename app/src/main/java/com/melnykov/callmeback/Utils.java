package com.melnykov.callmeback;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.view.animation.AccelerateInterpolator;

import com.afollestad.materialdialogs.MaterialDialog;
import com.melnykov.fab.FloatingActionButton;

import java.util.List;

public class Utils {

    private static final String MARKET_APP_URI = "market://details?id=com.melnykov.callmeback";
    private static final String MARKET_BROWSER_URI = "https://play.google.com/store/apps/details?id=com.melnykov.callmeback";

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

    public static boolean isIntentResolvable(Context context, Intent intent) {
        final PackageManager mgr = context.getPackageManager();
        List<ResolveInfo> list = mgr.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    public static void startPlayStore(Context context) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(MARKET_APP_URI));
        if (!Utils.isIntentResolvable(context, intent)) {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(MARKET_BROWSER_URI));
        }
        context.startActivity(intent);
    }

    public static void showDialerNotInstalledDialog(Activity activity) {
        new MaterialDialog.Builder(activity)
            .title(R.string.title_error)
            .content(R.string.msg_dialer_not_installed)
            .positiveText(R.string.close)
            .positiveColorRes(R.color.primary)
            .callback(new MaterialDialog.SimpleCallback() {
                @Override
                public void onPositive(MaterialDialog dialog) {
                    // NOP
                }
            })
            .build()
            .show();
    }

    public static void showPickContactNotInstalledDialog(Activity activity) {
        new MaterialDialog.Builder(activity)
            .title(R.string.title_error)
            .content(R.string.msg_pick_contact_not_installed)
            .positiveText(R.string.close)
            .positiveColorRes(R.color.primary)
            .callback(new MaterialDialog.SimpleCallback() {
                @Override
                public void onPositive(MaterialDialog dialog) {
                    // NOP
                }
            })
            .build()
            .show();
    }

    public static void animateFab(FloatingActionButton fab, long duration) {
        fab.setScaleX(0);
        fab.setScaleY(0);
        fab.animate()
            .scaleX(1)
            .scaleY(1)
            .setDuration(duration)
            .setInterpolator(new AccelerateInterpolator())
            .setStartDelay(500)
            .start();
    }

    public static boolean isKitKat() {
        return Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT;
    }

    public static boolean isLollipop() {
        return Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP;
    }
}