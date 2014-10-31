package com.melnykov.callmeback.ui;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.melnykov.callmeback.Operator;
import com.melnykov.callmeback.R;
import com.melnykov.callmeback.adapters.OperatorsAdapter;

public class OperatorsFragment extends ListFragment {

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setListAdapter(new OperatorsAdapter(getActivity()));

        getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        getListView().setItemChecked(0 ,true);

        Button next = (Button) view.findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Operator operator = getListAdapter().getItem(getListView().getCheckedItemPosition());
                ((MainActivity) getActivity()).onOperatorSelected(operator);
            }
        });
    }

    @Override
    public OperatorsAdapter getListAdapter() {
        return (OperatorsAdapter) super.getListAdapter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_operators, container, false);
    }
}