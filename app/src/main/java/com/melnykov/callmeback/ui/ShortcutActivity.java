package com.melnykov.callmeback.ui;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

import com.melnykov.callmeback.Dialer;
import com.melnykov.callmeback.Operators;
import com.melnykov.callmeback.Prefs;
import com.melnykov.callmeback.R;
import com.melnykov.callmeback.model.Operator;
import com.melnykov.callmeback.queries.ContactWithPhoneQuery;
import com.melnykov.callmeback.utils.ShortcutIconTransformation;
import com.melnykov.callmeback.utils.Utils;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.squareup.picasso.Transformation;

public class ShortcutActivity extends ActionBarActivity implements LoaderManager.LoaderCallbacks<Cursor>,
    OperatorsFragment.Callback {

    private static final String BUNDLE_KEY_CONTACT_URI = "contact_uri";
    private static final String STATE_KEY_OPERATOR_ID = "operator_id";

    private static final int LOADER_CONTACT = 0;
    private static final int PICK_CONTACT_REQUEST = 1;

    private int mOperatorId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        if (intent != null && Intent.ACTION_CREATE_SHORTCUT.equals(intent.getAction())) {
            if (Prefs.isOperatorSelected(this)) {
                setTheme(android.R.style.Theme_NoDisplay);
                super.onCreate(savedInstanceState);

                mOperatorId = Prefs.getOperatorId(getApplicationContext());
                pickContact();
            } else {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_shortcut);

                Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
                setSupportActionBar(toolbar);

                if (savedInstanceState != null) {
                    mOperatorId = savedInstanceState.getInt(STATE_KEY_OPERATOR_ID);
                }
            }
        } else {
            throw new RuntimeException("The activity can be started only with Intent.ACTION_CREATE_SHORTCUT");
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_KEY_OPERATOR_ID, mOperatorId);
    }

    private void pickContact() {
        Intent pickContactIntent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        // Show user only contacts w/ phone numbers
        pickContactIntent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
        if (Utils.isIntentResolvable(getApplicationContext(), pickContactIntent)) {
            startActivityForResult(pickContactIntent, PICK_CONTACT_REQUEST);
        } else {
            Utils.showPickContactNotInstalledDialog(this);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_CONTACT_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                Bundle args = new Bundle();
                args.putParcelable(BUNDLE_KEY_CONTACT_URI, data.getData());
                getSupportLoaderManager().restartLoader(LOADER_CONTACT, args, this);
            } else {
                setResult(RESULT_CANCELED);
                finish();
            }
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case LOADER_CONTACT:
                return new CursorLoader(this,
                    (Uri) args.getParcelable(BUNDLE_KEY_CONTACT_URI),
                    ContactWithPhoneQuery.PROJECTION,
                    null,
                    null,
                    null);
            default:
                throw new IllegalArgumentException("Invalid loader id: " + id);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        switch (loader.getId()) {
            case LOADER_CONTACT:
                if (cursor != null && cursor.moveToFirst()) {
                    createContactShortcut(cursor);
                } else {
                    setResult(RESULT_CANCELED);
                    finish();
                }
                break;
            default:
                throw new IllegalArgumentException("Invalid loader id: " + loader.getId());
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // NOP
    }

    private void createContactShortcut(Cursor cursor) {
        long contactId = cursor.getLong(ContactWithPhoneQuery.CONTACT_ID);
        String contactName = cursor.getString(ContactWithPhoneQuery.DISPLAY_NAME);
        String phoneNumber = cursor.getString(ContactWithPhoneQuery.NUMBER);
        String lookupKey = cursor.getString(ContactWithPhoneQuery.LOOKUP_KEY);

        Operator operator = Operators.getById(mOperatorId);
        String encodedNumber = Uri.encode(Dialer.getRecallNumber(operator, phoneNumber));

        Intent shortcutIntent = new Intent(Intent.ACTION_DIAL);
        shortcutIntent.setData(Uri.parse("tel:" + encodedNumber));

        final Intent resultIntent = new Intent()
            .putExtra(Intent.EXTRA_SHORTCUT_NAME, contactName)
            .putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);

        Uri contactUri = ContactsContract.Contacts.getLookupUri(contactId, lookupKey);

        final Transformation iconTransformation = new ShortcutIconTransformation(getApplicationContext());
        Picasso.with(this)
            .load(contactUri)
            .transform(iconTransformation)
            .into(new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    resultIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON, bitmap);

                    setResult(RESULT_OK, resultIntent);
                    finish();
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {
                    Picasso.with(ShortcutActivity.this)
                        .load(R.drawable.contact_photo_placeholder_light)
                        .transform(iconTransformation)
                        .into(new Target() {
                            @Override
                            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                resultIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON, bitmap);

                                setResult(RESULT_OK, resultIntent);
                                finish();
                            }

                            @Override
                            public void onBitmapFailed(Drawable errorDrawable) {
                            }

                            @Override
                            public void onPrepareLoad(Drawable placeHolderDrawable) {
                            }
                        });
                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {
                }
            });
    }

    @Override
    public void onOperatorSelected(int id) {
        mOperatorId = id;
        pickContact();
    }
}