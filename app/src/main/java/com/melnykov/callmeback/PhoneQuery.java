package com.melnykov.callmeback;


import android.provider.ContactsContract.PhoneLookup;

/**
 * The query to look up for a given number in the Call Log.
 */
public class PhoneQuery {

    public static final String[] PROJECTION = new String[] {
        PhoneLookup._ID,
        PhoneLookup.LOOKUP_KEY
    };

    public static final int CONTACT_ID = 0;
    public static final int LOOKUP_KEY = 1;
}