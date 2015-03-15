package com.melnykov.callmeback.queries;

import static android.provider.ContactsContract.CommonDataKinds.Phone;

/**
 * The query for the contacts table.
 */
public class ContactWithPhoneQuery {

    public static final String[] PROJECTION = new String[]{
        Phone._ID,
        Phone.DISPLAY_NAME,
        Phone.NUMBER,
        Phone.LOOKUP_KEY
    };

    public static final int CONTACT_ID = 0;
    public static final int DISPLAY_NAME = 1;
    public static final int NUMBER = 2;
    public static final int LOOKUP_KEY = 3;
}