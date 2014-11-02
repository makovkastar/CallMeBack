package com.melnykov.callmeback.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.melnykov.callmeback.Operators;
import com.melnykov.callmeback.Prefs;
import com.melnykov.callmeback.R;
import com.melnykov.callmeback.Utils;
import com.melnykov.callmeback.model.Operator;

public class DialpadFragment extends Fragment {

    public static final String TAG = "DialpadFragment";

    private Operator mOperator;
    private EditText mPhoneNumber;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ActionBar actionBar = ((MainActivity) activity).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.manual_dial);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int index = Prefs.getOperatorIndex(getActivity().getApplicationContext());
        mOperator = Operators.list().get(index);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_dialpad, container, false);
        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mPhoneNumber = (EditText) view.findViewById(R.id.phone_number);
        mPhoneNumber.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        ImageButton backSpace = (ImageButton) view.findViewById(R.id.backspace);
        backSpace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence oldText = mPhoneNumber.getText();
                mPhoneNumber.setText(oldText.subSequence(0, Math.max(0, oldText.length() - 1)));
            }
        });

        ImageButton dial = (ImageButton) view.findViewById(R.id.dial);
        dial.setImageDrawable(Utils.getColoredDrawable(getActivity(), R.drawable.ic_phone_forwarded_white_36dp, R.color.primary));
        dial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialSelectedNumber(mPhoneNumber.getText().toString());
            }
        });
    }

    public void onDigitClick(Button button) {
        mPhoneNumber.setText(mPhoneNumber.getText().append(button.getText()));
    }

    private void dialSelectedNumber(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + Uri.encode(String.format(mOperator.getRecallPattern(),
            phoneNumber))));
        startActivity(intent);
    }
}