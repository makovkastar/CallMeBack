package com.melnykov.callmeback.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;

import com.melnykov.callmeback.Operator;
import com.melnykov.callmeback.R;

public class OperatorsAdapter extends BaseAdapter {

    private static final Operator[] mOperators = new Operator[] {
        new Operator(0, R.string.kyivstar, R.drawable.kyivstar, null),
        new Operator(1, R.string.mts_ua, R.drawable.mts_ua, "*104*%s#"),
        new Operator(2, R.string.beeline_ua, R.drawable.beeline_ua, null),
        new Operator(3, R.string.djuice, 0, null),
        new Operator(4, R.string.life_ua, 0, null),
        new Operator(5, R.string.utel, 0, null)
    };

    private final Context mContext;

    public OperatorsAdapter(Context context) {
        mContext = context;
    }

    @Override
    public int getCount() {
        return mOperators.length;
    }

    @Override
    public Operator getItem(int position) {
        return mOperators[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.operator_list_item, parent, false);
        }

        CheckedTextView textView = (CheckedTextView) convertView.findViewById(android.R.id.text1);
        textView.setText(getItem(position).getNameResId());
        textView.setCompoundDrawablesWithIntrinsicBounds(getItem(position).getLogoResId(), 0, 0, 0);

        return convertView;
    }
}