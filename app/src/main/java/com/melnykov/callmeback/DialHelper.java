package com.melnykov.callmeback;

public class DialHelper {
    public static String getCallbackNumber(String originalNumber) {
        return "*104*" + originalNumber + "#";
    }
}
