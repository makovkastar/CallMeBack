package com.melnykov.callmeback.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Interpolator;

import com.melnykov.callmeback.Prefs;
import com.melnykov.callmeback.R;

import java.util.List;

public class Utils {

    private static final String UNKNOWN_NUMBER = "-1";
    private static final String PRIVATE_NUMBER = "-2";
    private static final String PAYPHONE_NUMBER = "-3";

    private static final Interpolator FAB_INTERPOLATOR = new FastOutSlowInInterpolator();

    private Utils() {
        // Prevent instantiation
    }

    public static boolean isIntentResolvable(Context context, Intent intent) {
        final PackageManager mgr = context.getPackageManager();
        List<ResolveInfo> list = mgr.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    public static void startPlayStore(Context context) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + context.getPackageName()));
        if (!Utils.isIntentResolvable(context, intent)) {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + context.getPackageName()));
        }
        context.startActivity(intent);
    }

    public static void showDialerNotInstalledDialog(Activity activity) {
        new AlertDialog.Builder(activity)
                .setTitle(R.string.title_error)
                .setMessage(R.string.msg_dialer_not_installed)
                .setPositiveButton(R.string.close, null)
                .create()
                .show();
    }

    public static void showPickContactNotInstalledDialog(Activity activity) {
        new AlertDialog.Builder(activity)
                .setTitle(R.string.title_error)
                .setMessage(R.string.msg_pick_contact_not_installed)
                .setPositiveButton(R.string.close, null)
                .create()
                .show();
    }

    public static boolean canPlaceCallsTo(String number) {
        return !(TextUtils.isEmpty(number)
                || number.equals(UNKNOWN_NUMBER)
                || number.equals(PRIVATE_NUMBER)
                || number.equals(PAYPHONE_NUMBER));
    }

    public static int getTheme(Context context) {
        if (Prefs.isDarkTheme(context)) {
            return R.style.AppThemeDark;
        } else {
            return R.style.AppThemeLight;
        }
    }

    public static void showFabWithAnimation(final FloatingActionButton fab) {
        fab.setVisibility(View.INVISIBLE);
        fab.setScaleX(0.0F);
        fab.setScaleY(0.0F);
        fab.setAlpha(0.0F);
        fab.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override public boolean onPreDraw() {
                fab.getViewTreeObserver().removeOnPreDrawListener(this);
                fab.postDelayed(new Runnable() {
                    @Override public void run() {
                        fab.show();
                    }
                }, 500);
                return true;
            }
        });
    }
}