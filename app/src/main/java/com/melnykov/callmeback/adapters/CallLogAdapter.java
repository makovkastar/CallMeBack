package com.melnykov.callmeback.adapters;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.makeramen.RoundedTransformationBuilder;
import com.melnykov.callmeback.CallLogItem;
import com.melnykov.callmeback.PhoneQuery;
import com.melnykov.callmeback.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CallLogAdapter extends BaseAdapter {

    private final Context mContext;
    private List<CallLogItem> mItems;
    private boolean mLoading;
    private final Map<String, Uri> mContactUriCache;
    private final ExecutorService mExecutorService;
    private final Handler mHandler;
    private final Transformation mAvatarTransformation;

    public CallLogAdapter(Context context) {
        mContext = context;
        mContactUriCache = new HashMap<String, Uri>();
        mExecutorService = Executors.newCachedThreadPool();
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                notifyDataSetChanged();
            }
        };
        mAvatarTransformation = new RoundedTransformationBuilder()
            .oval(true)
            .build();
    }

    @Override
    public boolean isEmpty() {
        if (mLoading) {
            // We don't want the empty state to show when loading.
            return false;
        } else {
            return super.isEmpty();
        }
    }

    @Override
    public int getCount() {
        return mItems == null ? 0 : mItems.size();
    }

    @Override
    public CallLogItem getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.contact_list_item, parent, false);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.number = (TextView) convertView.findViewById(R.id.number);
            holder.avatar = (ImageView) convertView.findViewById(R.id.avatar);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        CallLogItem item = getItem(position);
        holder.name.setText(TextUtils.isEmpty(item.getName()) ? item.getNumber() : item.getName());
        holder.number.setText(item.getNumber());

        Uri contactUri = mContactUriCache.get(item.getNumber());
        if (contactUri == null) {
            if (!mExecutorService.isShutdown()) {
                // Request the contact details immediately since they are currently missing.
                mExecutorService.submit(new ContactUriFetcher(item.getNumber()));
            }
        }

        Picasso.with(mContext)
            .load(contactUri)
            .placeholder(R.drawable.contact_photo_placeholder)
            .fit()
            .centerCrop()
            .transform(mAvatarTransformation)
            .into(holder.avatar);

        return convertView;
    }

    public void setLoading(boolean loading) {
        mLoading = loading;
    }

    public void swapItems(List<CallLogItem> items) {
        if (items == mItems) {
            return;
        }
        mItems = items;
        if (items != null) {
            notifyDataSetChanged();
        } else {
            notifyDataSetInvalidated();
        }
    }

    public void stopRequestProcessing() {
        mHandler.removeMessages(0);
        mExecutorService.shutdownNow();
    }

    private Uri queryContactUriForPhoneNumber(String number) {
        Cursor phonesCursor =
            mContext.getContentResolver().query(
                Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number)),
                PhoneQuery.PROJECTION,
                null,
                null,
                null);

        Uri lookupUri;
        if (phonesCursor != null) {
            if (phonesCursor.moveToFirst()) {
                long contactId = phonesCursor.getLong(PhoneQuery.CONTACT_ID);
                String lookupKey = phonesCursor.getString(PhoneQuery.LOOKUP_KEY);
                lookupUri = ContactsContract.Contacts.getLookupUri(contactId, lookupKey);
            } else {
                lookupUri = null;
            }
            phonesCursor.close();
        } else {
            // Failed to fetch the data, ignore this request.
            lookupUri = null;
        }
        return lookupUri;
    }

    private class ContactUriFetcher implements Runnable {

        private final String phoneNumber;

        private ContactUriFetcher(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        @Override
        public void run() {
            Uri contactUri = queryContactUriForPhoneNumber(phoneNumber);
            mContactUriCache.put(phoneNumber, contactUri);
            mHandler.sendEmptyMessage(0);
        }
    }

    static class ViewHolder {
        ImageView avatar;
        TextView name;
        TextView number;
    }
}