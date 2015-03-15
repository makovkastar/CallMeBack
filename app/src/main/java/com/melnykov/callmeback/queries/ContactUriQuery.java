package com.melnykov.callmeback.queries;


import android.provider.ContactsContract.PhoneLookup;

/**
 * The query to look up for a contact uri in the contacts table.
 */
public class ContactUriQuery {

    public static final String[] PROJECTION = new String[]{
        PhoneLookup._ID,
        PhoneLookup.LOOKUP_KEY
    };

    public static final int CONTACT_ID = 0;
    public static final int LOOKUP_KEY = 1;
}