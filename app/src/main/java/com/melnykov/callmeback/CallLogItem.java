package com.melnykov.callmeback;

import android.telephony.PhoneNumberUtils;

public class CallLogItem {

    private final String name;
    private final String number;
    private final String photoUri;

    public CallLogItem(String name, String number, String photoUri) {
        this.name = name;
        this.number = number;
        this.photoUri = photoUri;
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CallLogItem that = (CallLogItem) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        return PhoneNumberUtils.compare(number, that.number);
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + PhoneNumberUtils.extractPostDialPortion(number).hashCode();
        return result;
    }
}