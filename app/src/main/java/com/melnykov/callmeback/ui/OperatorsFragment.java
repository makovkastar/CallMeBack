package com.melnykov.callmeback.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v7.app.ActionBar;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.melnykov.callmeback.Prefs;
import com.melnykov.callmeback.R;
import com.melnykov.callmeback.adapters.OperatorsAdapter;

public class OperatorsFragment extends ListFragment {

    public static final String TAG = "OperatorsFragment";

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ActionBar actionBar = ((MainActivity) activity).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.select_operator);
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Context context = getActivity().getApplicationContext();

        setListAdapter(new OperatorsAdapter(getActivity()));

        getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        int operatorIndex = Prefs.isOperatorSelected(context) ? Prefs.getOperatorIndex(context) : 0;
        // ListView has a header view, so positions are shifted by 1.
        getListView().setItemChecked(operatorIndex + 1, true);

        Button next = (Button) view.findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).onOperatorSelected(getListView().getCheckedItemPosition() - 1);
            }
        });
    }

    @Override
    public OperatorsAdapter getListAdapter() {
        return (OperatorsAdapter) super.getListAdapter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_operators, container, false);

        TextView headerView = (TextView) inflater.inflate(R.layout.operators_header, null);
        headerView.setText(Html.fromHtml(getString(R.string.welcome_text)));

        ListView listView = (ListView) root.findViewById(android.R.id.list);
        listView.addHeaderView(headerView);

        return root;
    }
}