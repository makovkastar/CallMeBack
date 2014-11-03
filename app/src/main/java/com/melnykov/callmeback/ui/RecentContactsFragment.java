package com.melnykov.callmeback.ui;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
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
import android.view.animation.AccelerateInterpolator;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import com.melnykov.callmeback.Operators;
import com.melnykov.callmeback.Prefs;
import com.melnykov.callmeback.R;
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
    private static final int MAX_RECENT_CONTACTS = 20;

    private static final int LOADER_CALL_LOG = 0;
    private static final int LOADER_PHONE_NUMBER = 1;

    private CallLogAdapter mAdapter;
    private Uri mContactUri;
    private Operator mOperator;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_recent_contacts, container, false);

        TextView emptyView = (TextView) root.findViewById(android.R.id.empty);
        emptyView.setText(Html.fromHtml(getString(R.string.no_recent_contacts)));

        Button allContacts = (Button) root.findViewById(R.id.all_contacts);
        allContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickContact();
            }
        });

        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getListView().setOnItemClickListener(this);

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).onShowDialpad();
            }
        });
        animateFab(fab);
    }

    private void animateFab(final FloatingActionButton fab) {
        fab.setScaleX(0);
        fab.setScaleY(0);
        fab.animate()
            .scaleX(1)
            .scaleY(1)
            .setDuration(getResources().getInteger(android.R.integer.config_mediumAnimTime))
            .setInterpolator(new AccelerateInterpolator())
            .setStartDelay(500)
            .start();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        int operatorId = Prefs.getOperatorId(getActivity().getApplicationContext());
        mOperator = Operators.getById(operatorId);

        ActionBar actionBar = ((MainActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(mOperator.getNameResId());
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mAdapter = new CallLogAdapter(getActivity());
        mAdapter.setLoading(true);
        setListAdapter(mAdapter);

        getLoaderManager().initLoader(LOADER_CALL_LOG, null, this);
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
    public void onPause() {
        super.onPause();
        mAdapter.stopRequestProcessing();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_change_operator:
                ((MainActivity) getActivity()).onChangeOperator();
                return true;
            case R.id.action_about:
                new AboutDialog().show(getFragmentManager(), AboutDialog.TAG);
            default:
                return super.onOptionsItemSelected(item);
        }
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
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + Uri.encode(String.format(mOperator.getRecallPattern(),
            phoneNumber))));
        startActivity(intent);
    }

    private void pickContact() {
        Intent pickContactIntent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        // Show user only contacts w/ phone numbers
        pickContactIntent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
        startActivityForResult(pickContactIntent, PICK_CONTACT_REQUEST);
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