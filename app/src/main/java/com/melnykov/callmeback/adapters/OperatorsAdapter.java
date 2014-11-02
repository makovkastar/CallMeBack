package com.melnykov.callmeback.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;

import com.melnykov.callmeback.Operators;
import com.melnykov.callmeback.R;
import com.melnykov.callmeback.model.Operator;

public class OperatorsAdapter extends BaseAdapter {

    private final Context mContext;

    public OperatorsAdapter(Context context) {
        mContext = context;
    }

    @Override
    public int getCount() {
        return Operators.list().size();
    }

    @Override
    public Operator getItem(int position) {
        return Operators.list().get(position);
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