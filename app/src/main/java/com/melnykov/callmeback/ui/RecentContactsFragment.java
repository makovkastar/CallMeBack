package com.melnykov.callmeback.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.melnykov.callmeback.Dialer;
import com.melnykov.callmeback.Operators;
import com.melnykov.callmeback.Prefs;
import com.melnykov.callmeback.R;
import com.melnykov.callmeback.Utils;
import com.melnykov.callmeback.adapters.CallLogAdapter;
import com.melnykov.callmeback.model.CallLogItem;
import com.melnykov.callmeback.model.Operator;
import com.melnykov.callmeback.queries.CallLogQuery;
import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class RecentContactsFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor>, AdapterView.OnItemClickListener {

    public static final String TAG = "RecentContactsFragment";

    private static final int PICK_CONTACT_REQUEST = 1;
    private static final int MAX_RECENT_CONTACTS = 40;

    private static final int LOADER_CALL_LOG = 0;
    private static final int LOADER_PHONE_NUMBER = 1;

    private static final String VERSION_UNAVAILABLE = "N/A";

    private CallLogAdapter mAdapter;
    private Uri mContactUri;
    private Operator mOperator;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_recent_contacts, container, false);

        ListView list = (ListView) root.findViewById(android.R.id.list);
        View listFooter = inflater.inflate(R.layout.recent_contacts_footer, null);

        list.addFooterView(listFooter, null, false);
        list.setFooterDividersEnabled(false);

        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getListView().setOnItemClickListener(this);

        TextView emptyView = (TextView) view.findViewById(android.R.id.empty);
        emptyView.setText(Html.fromHtml(getString(R.string.no_recent_contacts)));

        Button allContacts = (Button) view.findViewById(R.id.all_contacts);
        allContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickContact();
            }
        });

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).onShowDialpad();
            }
        });

        Utils.animateFab(fab, getResources().getInteger(android.R.integer.config_shortAnimTime));
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setListAdapter(mAdapter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        int operatorId = Prefs.getOperatorId(getActivity().getApplicationContext());
        mOperator = Operators.getById(operatorId);

        mAdapter = new CallLogAdapter(getActivity());
        mAdapter.setLoading(true);

        getLoaderManager().initLoader(LOADER_CALL_LOG, null, this);
    }

    @Override
    public void onResume() {
        super.onResume();
        ActionBar actionBar = ((MainActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(mOperator.getNameResId());
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
        switch (id) {
            case LOADER_CALL_LOG:
                return new CursorLoader(getActivity(),
                    CallLogQuery.CONTENT_URI,
                    CallLogQuery.PROJECTION,
                    CallLogQuery.SELECTION,
                    null,
                    CallLogQuery.SORT_ORDER);
            case LOADER_PHONE_NUMBER:
                // We only need the NUMBER column, because there will be only one row in the result
                String[] projection = {ContactsContract.CommonDataKinds.Phone.NUMBER};
                return new CursorLoader(getActivity(),
                    mContactUri,
                    projection,
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
            case LOADER_CALL_LOG:
                mAdapter.setLoading(false);
                mAdapter.swapItems(convertCursor(cursor));
                break;
            case LOADER_PHONE_NUMBER:
                cursor.moveToFirst();
                String number = cursor.getString(cursor.getColumnIndex(
                    ContactsContract.CommonDataKinds.Phone.NUMBER));
                dialSelectedNumber(number);
                getLoaderManager().destroyLoader(loader.getId());
                mContactUri = null;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        switch (loader.getId()) {
            case LOADER_CALL_LOG:
                mAdapter.setLoading(false);
                mAdapter.swapItems(null);
                break;
            case LOADER_PHONE_NUMBER:
                mContactUri = null;
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mAdapter.stopRequestProcessing();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_change_operator:
                ((MainActivity) getActivity()).onChangeOperator();
                return true;
            case R.id.action_about:
                showAboutDialog();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showAboutDialog() {
        new MaterialDialog.Builder(getActivity())
            .title(getString(R.string.about_title, getString(R.string.app_name), getVersionName()))
            .content(Html.fromHtml(getString(R.string.about_message)))
            .positiveText(R.string.rate)
            .positiveColorRes(R.color.primary)
            .negativeText(R.string.close)
            .negativeColorRes(R.color.font_button)
            .callback(new MaterialDialog.Callback() {
                @Override
                public void onPositive(MaterialDialog dialog) {
                    Utils.startPlayStore(getActivity());
                }

                @Override
                public void onNegative(MaterialDialog dialog) {
                    // NOP
                }
            })
            .build()
            .show();
    }

    public String getVersionName() {
        String versionName;
        try {
            PackageInfo info = getActivity().getPackageManager().getPackageInfo(getActivity()
                .getPackageName(), 0);
            versionName = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            versionName = VERSION_UNAVAILABLE;
        }
        return versionName;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main, menu);
    }

    private List<CallLogItem> convertCursor(Cursor cursor) {
        Set<CallLogItem> items = new LinkedHashSet<CallLogItem>(MAX_RECENT_CONTACTS);
        if (cursor != null) {
            cursor.moveToPosition(-1);
            while (cursor.moveToNext() && items.size() <= MAX_RECENT_CONTACTS) {
                String name = cursor.getString(CallLogQuery.CACHED_NAME);
                String number = cursor.getString(CallLogQuery.NUMBER);
                items.add(new CallLogItem(name, number));
            }
        }
        return new ArrayList<CallLogItem>(items);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        dialSelectedNumber(mAdapter.getItem(position).getNumber());
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

    private void pickContact() {
        Intent pickContactIntent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        // Show user only contacts w/ phone numbers
        pickContactIntent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
        if (Utils.isIntentResolvable(getActivity().getApplicationContext(), pickContactIntent)) {
            startActivityForResult(pickContactIntent, PICK_CONTACT_REQUEST);
        } else {
            Utils.showPickContactNotInstalledDialog(getActivity());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Check which request it is that we're responding to
        if (requestCode == PICK_CONTACT_REQUEST) {
            // Make sure the request was successful
            if (resultCode == Activity.RESULT_OK) {
                // Get the URI that points to the selected contact
                mContactUri = data.getData();
                getLoaderManager().restartLoader(LOADER_PHONE_NUMBER, null, this);
            }
        }
    }
}