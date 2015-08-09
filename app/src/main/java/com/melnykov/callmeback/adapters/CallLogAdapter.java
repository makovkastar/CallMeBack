package com.melnykov.callmeback.adapters;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.makeramen.RoundedTransformationBuilder;
import com.melnykov.callmeback.Prefs;
import com.melnykov.callmeback.R;
import com.melnykov.callmeback.model.CallLogItem;
import com.melnykov.callmeback.queries.ContactUriQuery;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CallLogAdapter extends RecyclerView.Adapter<CallLogAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(CallLogItem callLogItem);
    }

    private final Activity mActivity;
    private final OnItemClickListener mOnItemClickListener;
    private final Map<String, Uri> mContactUriCache;
    private final ExecutorService mExecutorService;
    private final Handler mHandler;
    private final Transformation mAvatarTransformation;

    private List<CallLogItem> mItems;

    public CallLogAdapter(Activity activity, OnItemClickListener onItemClickListener) {
        mActivity = activity;
        mOnItemClickListener = onItemClickListener;
        mContactUriCache = new HashMap<>();
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
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.contact_list_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        final CallLogItem item = mItems.get(position);
        viewHolder.name.setText(TextUtils.isEmpty(item.getName()) ? item.getNumber() : item.getName());
        viewHolder.number.setText(item.getNumber());

        int placeholderDrawableResId = Prefs.isDarkTheme(mActivity) ? R.drawable.contact_photo_placeholder_dark
                : R.drawable.contact_photo_placeholder_light;
        if (mContactUriCache.containsKey(item.getNumber())) {
            Uri contactUri = mContactUriCache.get(item.getNumber());
            Picasso.with(mActivity)
                    .load(contactUri)
                    .placeholder(placeholderDrawableResId)
                    .fit()
                    .centerCrop()
                    .transform(mAvatarTransformation)
                    .into(viewHolder.avatar);
        } else {
            if (!mExecutorService.isShutdown()) {
                // Request the contact details immediately since they are currently missing.
                mExecutorService.submit(new ContactUriFetcher(item.getNumber()));
            }
            Picasso.with(mActivity)
                    .load(placeholderDrawableResId)
                    .fit()
                    .into(viewHolder.avatar);
        }

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnItemClickListener.onItemClick(item);
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mItems == null ? 0 : mItems.size();
    }

    public void swapItems(List<CallLogItem> items) {
        if (items == mItems) {
            return;
        }
        mItems = items;
        if (items != null) {
            notifyDataSetChanged();
        }
    }

    public void stopRequestProcessing() {
        mHandler.removeMessages(0);
        mExecutorService.shutdownNow();
    }

    private Uri queryContactUriForPhoneNumber(String number) {
        Cursor cursor = mActivity.getContentResolver().query(
                Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number)),
                ContactUriQuery.PROJECTION,
                null,
                null,
                null);

        Uri contactUri;
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                long contactId = cursor.getLong(ContactUriQuery.CONTACT_ID);
                String lookupKey = cursor.getString(ContactUriQuery.LOOKUP_KEY);
                contactUri = ContactsContract.Contacts.getLookupUri(contactId, lookupKey);
            } else {
                contactUri = null;
            }
            cursor.close();
        } else {
            // Failed to fetch the data, ignore this request.
            contactUri = null;
        }
        return contactUri;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        View itemView;
        ImageView avatar;
        TextView name;
        TextView number;

        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            this.name = (TextView) itemView.findViewById(R.id.name);
            this.number = (TextView) itemView.findViewById(R.id.number);
            this.avatar = (ImageView) itemView.findViewById(R.id.avatar);
        }
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
}