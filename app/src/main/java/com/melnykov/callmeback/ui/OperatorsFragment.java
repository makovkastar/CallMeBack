package com.melnykov.callmeback.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.melnykov.callmeback.Operators;
import com.melnykov.callmeback.Prefs;
import com.melnykov.callmeback.R;
import com.melnykov.callmeback.adapters.OperatorsAdapter;
import com.melnykov.callmeback.model.Operator;

public class OperatorsFragment extends ListFragment {

    public static final String TAG = "OperatorsFragment";

    private Callback mCallback;

    public interface Callback {
        void onOperatorSelected(int operatorId);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (Callback) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OperatorsFragment.Callback interface");
        }
        ActionBar actionBar = ((AppCompatActivity) activity).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.select_operator);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_operators, container, false);

        TextView headerView = (TextView) inflater.inflate(R.layout.operators_header, null);
        headerView.setText(Html.fromHtml(getString(R.string.welcome_text)));

        ListView listView = (ListView) root.findViewById(android.R.id.list);
        listView.addHeaderView(headerView, null, false);

        return root;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Context context = getActivity().getApplicationContext();

        setListAdapter(new OperatorsAdapter(getActivity()));

        int operatorId = Prefs.isOperatorSelected(context) ? Prefs.getOperatorId(context)
                : Operators.DEFAULT_OPERATOR_ID;
        // ListView has a header view, so positions are shifted by 1.
        getListView().setItemChecked(Operators.getPosition(operatorId) + 1, true);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        Button next = (Button) view.findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Operator operator = getListAdapter().getItem(getListView().getCheckedItemPosition() - 1);
                mCallback.onOperatorSelected(operator.getId());
            }
        });
    }

    @Override
    public OperatorsAdapter getListAdapter() {
        return (OperatorsAdapter) super.getListAdapter();
    }
}