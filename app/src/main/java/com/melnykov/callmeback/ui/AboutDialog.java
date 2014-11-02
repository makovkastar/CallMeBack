package com.melnykov.callmeback.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.melnykov.callmeback.R;

public class AboutDialog extends DialogFragment {

    public static final String TAG = "AboutDialog";

    private static final String VERSION_UNAVAILABLE = "N/A";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Context context = getActivity();

        View view = LayoutInflater.from(context).inflate(R.layout.dialog_about, null);
        TextView title = (TextView) view.findViewById(R.id.title);
        TextView message = (TextView) view.findViewById(R.id.message);

        message.setMovementMethod(new LinkMovementMethod());

        title.setText(Html.fromHtml(getString(R.string.about_title, getString(R.string.app_name),
            getVersionName())));
        message.setText(Html.fromHtml(getString(R.string.about_message)));

        return new AlertDialog.Builder(getActivity())
            .setView(view)
            .setNegativeButton(R.string.close, null)
            .create();
    }

    private void startPlayStore() {
    }

    public String getVersionName() {
        String versionName;
        try {
            PackageInfo info = getActivity().getPackageManager().getPackageInfo(getActivity()
                .getPackageName(), 0);
            versionName = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            versionName = VERSION_UNAVAILABLE;
        }
        return versionName;
    }
}