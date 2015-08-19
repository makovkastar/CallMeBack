package com.melnykov.callmeback.ui;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.melnykov.callmeback.Dialer;
import com.melnykov.callmeback.Operators;
import com.melnykov.callmeback.Prefs;
import com.melnykov.callmeback.R;
import com.melnykov.callmeback.adapters.CallLogAdapter;
import com.melnykov.callmeback.model.CallLogItem;
import com.melnykov.callmeback.model.Operator;
import com.melnykov.callmeback.queries.CallLogQuery;
import com.melnykov.callmeback.utils.DividerItemDecoration;
import com.melnykov.callmeback.utils.Utils;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class RecentContactsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, CallLogAdapter.OnItemClickListener {

    public static final String TAG = "RecentContactsFragment";

    private static final int PICK_CONTACT_REQUEST = 1;
    private static final int MAX_RECENT_CONTACTS = 40;

    private static final int LOADER_CALL_LOG = 0;
    private static final int LOADER_PHONE_NUMBER = 1;

    private static final String VERSION_UNAVAILABLE = "N/A";

    private RecyclerView mRecyclerView;
    private TextView mEmptyText;
    private CallLogAdapter mAdapter;
    private Uri mContactUri;
    private Operator mOperator;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recent_contacts, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity()));

        mEmptyText = (TextView) view.findViewById(android.R.id.empty);
        mEmptyText.setText(Html.fromHtml(getString(R.string.no_recent_contacts)));

        Button allContacts = (Button) view.findViewById(R.id.all_contacts);
        allContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickContact();
            }
        });

        final FloatingActionButton dialpadFab = (FloatingActionButton) view.findViewById(R.id.fab_dialpad);
        dialpadFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialpadFab.hide();
                ((MainActivity) getActivity()).onShowDialpad();
            }
        });
        Utils.showFabWithAnimation(dialpadFab);

        setEmptyTextVisible(false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        int operatorId = Prefs.getOperatorId(getActivity().getApplicationContext());
        mOperator = Operators.getById(operatorId);

        mAdapter = new CallLogAdapter(getActivity(), this);

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
                List<CallLogItem> callLogItems = convertCursor(cursor);
                if (callLogItems.isEmpty()) {
                    setEmptyTextVisible(true);
                } else {
                    mAdapter.swapItems(callLogItems);
                    setEmptyTextVisible(false);
                }
                break;
            case LOADER_PHONE_NUMBER:
                if (cursor != null && cursor.moveToFirst()) {
                    String number = cursor.getString(cursor.getColumnIndex(
                            ContactsContract.CommonDataKinds.Phone.NUMBER));
                    dialSelectedNumber(number);
                }
                mContactUri = null;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        switch (loader.getId()) {
            case LOADER_CALL_LOG:
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
            case R.id.action_dark_theme:
                Prefs.setDarkTheme(getActivity(), !item.isChecked());
                ((MainActivity) getActivity()).onChangeTheme();
                return true;
            case R.id.action_about:
                showAboutDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (getActivity() != null) {
            menu.findItem(R.id.action_dark_theme).setChecked(Prefs.isDarkTheme(getActivity()));
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_CONTACT_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                mContactUri = data.getData();
                getLoaderManager().restartLoader(LOADER_PHONE_NUMBER, null, this);
            }
        }
    }

    @Override
    public void onItemClick(CallLogItem callLogItem) {
        dialSelectedNumber(callLogItem.getNumber());
    }

    private List<CallLogItem> convertCursor(Cursor cursor) {
        Set<CallLogItem> items = new LinkedHashSet<>(MAX_RECENT_CONTACTS);
        if (cursor != null) {
            cursor.moveToPosition(-1);
            while (cursor.moveToNext() && items.size() <= MAX_RECENT_CONTACTS) {
                String number = cursor.getString(CallLogQuery.NUMBER);
                if (Utils.canPlaceCallsTo(number)) {
                    String name = cursor.getString(CallLogQuery.CACHED_NAME);
                    items.add(new CallLogItem(name, number));
                }
            }
        }
        return new ArrayList<>(items);
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

    private void setEmptyTextVisible(boolean visible) {
        mRecyclerView.setVisibility(visible ? View.GONE : View.VISIBLE);
        mEmptyText.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    private void showAboutDialog() {
        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.about_title, getString(R.string.app_name), getVersionName()))
                .setMessage(Html.fromHtml(getString(R.string.about_message)))
                .setPositiveButton(R.string.rate, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Utils.startPlayStore(getActivity());
                    }
                })
                .setNegativeButton(R.string.close, null)
                .create();
        dialog.show();
        // Make links clickable inside this dialog.
        ((TextView) dialog.findViewById(android.R.id.message)).setMovementMethod(LinkMovementMethod.getInstance());
        ((TextView) dialog.findViewById(android.R.id.message)).setLinkTextColor(getResources().getColor(R.color.primary));
    }

    private String getVersionName() {
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
}