package com.melnykov.callmeback.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

import com.melnykov.callmeback.Operator;
import com.melnykov.callmeback.R;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (savedInstanceState == null) {
            if (isOperatorSelected()) {
                getFragmentManager().beginTransaction()
                    .add(R.id.container, new RecentContactsFragment())
                    .commit();
            } else {
                getFragmentManager().beginTransaction()
                    .add(R.id.container, new OperatorsFragment())
                    .commit();
            }
        }
    }

    private boolean isOperatorSelected() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        return prefs.getLong("operator", Long.MIN_VALUE) != Long.MIN_VALUE;
    }

    public void onOperatorSelected(Operator operator) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.edit().putLong("operator", operator.getId()).apply();

        getSupportActionBar().setTitle(operator.getNameResId());

        getFragmentManager().beginTransaction()
            .setCustomAnimations(R.animator.fragment_enter_left, R.animator.fragment_exit_left)
            .replace(R.id.container, new RecentContactsFragment())
            .addToBackStack(null)
            .commit();
    }

    public void onOperatorChange() {
        getFragmentManager().beginTransaction()
            .setCustomAnimations(R.animator.fragment_enter_right, R.animator.fragment_exit_right)
            .replace(R.id.container, new OperatorsFragment())
            .commit();
    }
}