package com.melnykov.callmeback.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.crashlytics.android.Crashlytics;
import com.melnykov.callmeback.Prefs;
import com.melnykov.callmeback.R;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Crashlytics.start(this);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (savedInstanceState == null) {
            if (Prefs.isOperatorSelected(this)) {
                getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new RecentContactsFragment())
                    .commit();
            } else {
                getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new OperatorsFragment())
                    .commit();
            }
        }
    }

    public void onDigitClick(View view) {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container);
        if (fragment instanceof DialpadFragment) {
            ((DialpadFragment) fragment).onDigitClick((Button) view);
        }
    }

    public void onOperatorSelected(int id) {
        Prefs.saveOperatorId(getApplicationContext(), id);

        getSupportFragmentManager().beginTransaction()
            .setCustomAnimations(R.anim.fragment_enter_right, R.anim.fragment_exit_right)
            .replace(R.id.container, new RecentContactsFragment(), RecentContactsFragment.TAG)
            .commit();
    }

    public void onChangeOperator() {
        getSupportFragmentManager().beginTransaction()
            .setCustomAnimations(R.anim.fragment_enter_left, R.anim.fragment_exit_left)
            .replace(R.id.container, new OperatorsFragment(), OperatorsFragment.TAG)
            .commit();
    }

    public void onShowDialpad() {
        getSupportFragmentManager().beginTransaction()
            .setCustomAnimations(R.anim.fragment_enter_bottom, R.anim.fragment_exit_bottom,
                R.anim.fragment_enter_top, R.anim.fragment_exit_top)
            .replace(R.id.container, new DialpadFragment(), DialpadFragment.TAG)
            .addToBackStack(null)
            .commit();
    }

    public void onHideDialpad() {
        // Pop a back stack because currently transaction Recent contacts -> Dialpad is saved and must be removed.
        getSupportFragmentManager().popBackStack();

        getSupportFragmentManager().beginTransaction()
            .setCustomAnimations(R.anim.fragment_enter_top, R.anim.fragment_exit_top)
            .replace(R.id.container, new RecentContactsFragment(), RecentContactsFragment.TAG)
            .commit();
    }
}