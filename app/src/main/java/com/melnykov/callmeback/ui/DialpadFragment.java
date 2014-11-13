package com.melnykov.callmeback.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.melnykov.callmeback.Dialer;
import com.melnykov.callmeback.Operators;
import com.melnykov.callmeback.Prefs;
import com.melnykov.callmeback.R;
import com.melnykov.callmeback.Utils;
import com.melnykov.callmeback.model.Operator;
import com.melnykov.fab.FloatingActionButton;

public class DialpadFragment extends Fragment {

    public static final String TAG = "DialpadFragment";

    private Operator mOperator;
    private EditText mPhoneNumber;
    private ImageView mHeader;
    private boolean mIsAnimationRunning = false;
    private ValueAnimator mColorAnimation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        int operatorId = Prefs.getOperatorId(getActivity().getApplicationContext());
        mOperator = Operators.getById(operatorId);
    }

    @Override
    public void onResume() {
        super.onResume();
        ActionBar actionBar = ((MainActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.manual_dial);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dialpad, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mHeader = (ImageView) view.findViewById(R.id.header);

        mPhoneNumber = (EditText) view.findViewById(R.id.phone_number);
        mPhoneNumber.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        mPhoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String phoneNumber = editable.toString();
                if (Dialer.isNumberValid(mOperator, phoneNumber)) {
                    animateHeader(getResources().getColor(R.color.valid), 0, true);
                } else {
                    animateHeader(getResources().getColor(R.color.accent), 0, false);
                }
            }
        });

        ImageButton backSpace = (ImageButton) view.findViewById(R.id.backspace);
        backSpace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence oldText = mPhoneNumber.getText();
                mPhoneNumber.setText(oldText.subSequence(0, Math.max(0, oldText.length() - 1)));
            }
        });
        backSpace.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mPhoneNumber.setText("");
                return true;
            }
        });

        FloatingActionButton dial = (FloatingActionButton) view.findViewById(R.id.dial);
        dial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = mPhoneNumber.getText().toString();
                if (TextUtils.isEmpty(phoneNumber)) return;

                if (Dialer.isNumberValid(mOperator, phoneNumber)) {
                    dialSelectedNumber(phoneNumber);
                } else {
                    animateHeader(getResources().getColor(R.color.invalid), 3, false);
                    showPhoneNumberError(phoneNumber);
                }
            }
        });
        Utils.animateFab(dial, getResources().getInteger(android.R.integer.config_shortAnimTime));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.dialpad, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_recent_contacts:
                ((MainActivity) getActivity()).onHideDialpad();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onDigitClick(Button button) {
        button.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP);
        mPhoneNumber.append(button.getText());
    }

    private void dialSelectedNumber(String phoneNumber) {
        String encodedNumber = Uri.encode(Dialer.getRecallNumber(mOperator, phoneNumber));
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + encodedNumber));
        if (Utils.isIntentResolvable(getActivity().getApplicationContext(), intent)) {
            startActivity(intent);
        } else {
            Utils.showDialerNotInstalledDialog(getActivity());
        }
    }

    private void showPhoneNumberError(String phoneNumber) {
        new MaterialDialog.Builder(getActivity())
            .title(R.string.title_invalid_number)
            .content(Html.fromHtml(getString(R.string.msg_invalid_number, phoneNumber)))
            .positiveText(R.string.close)
            .positiveColorRes(R.color.primary)
            .callback(new MaterialDialog.SimpleCallback() {
                @Override
                public void onPositive(MaterialDialog dialog) {
                    // NOP
                }
            })
            .build()
            .show();
    }

    private void animateHeader(final int colorTo, final int repeatCount, boolean cancelPrevious) {
        final int colorFrom = ((ColorDrawable) mHeader.getBackground()).getColor();

        if (colorFrom == colorTo || (mIsAnimationRunning && !cancelPrevious)) return;

        if (mColorAnimation != null) {
            mColorAnimation.cancel();
        }
        mColorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        mColorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                mHeader.setBackgroundColor((Integer) animator.getAnimatedValue());
            }
        });
        mColorAnimation.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                mIsAnimationRunning = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mIsAnimationRunning = false;
            }
        });
        if (repeatCount > 0) {
            mColorAnimation.setRepeatCount(repeatCount);
            mColorAnimation.setRepeatMode(ValueAnimator.REVERSE);
        }
        mColorAnimation.setDuration(getResources().getInteger(android.R.integer.config_mediumAnimTime));
        mColorAnimation.start();
    }
}