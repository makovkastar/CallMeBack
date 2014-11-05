package com.melnykov.callmeback;

import com.melnykov.callmeback.model.Operator;

public class Dialer {

    public Dialer() {
        // Prevent instantiation
    }

    public static String getRecallNumber(Operator operator, String phoneNumber) {
        phoneNumber = stripNumber(phoneNumber);

        switch (operator.getCountry()) {
            case UKRAINE:
                // Phone numbers for Ukraine must be in the format XXXXXXXXXX
                if (phoneNumber.length() == 12 && phoneNumber.startsWith("38")) {
                    phoneNumber = phoneNumber.substring(2);
                }
                break;
            case BELARUS:
                // Phone numbers for Belarus must be in format 375XXXXXXXXX
                if (phoneNumber.length() == 15 && phoneNumber.startsWith("810375")) {
                    phoneNumber = phoneNumber.substring(3);
                } else if (phoneNumber.length() == 9) {
                    phoneNumber = "375" + phoneNumber;
                }

                // For Velcom phone number must be in format XXXXXXXXX
                if (operator.getId() == 200 && phoneNumber.startsWith("375")) {
                    phoneNumber = phoneNumber.substring(3);
                }
                break;
            case RUSSIA:
                // Phone numbers for Russia must be in format 9XXXXXXXXX
                if (phoneNumber.length() > 10) {
                    phoneNumber = phoneNumber.substring(phoneNumber.length() - 10);
                }
                break;
            case MOLDOVA:
                if (operator.getId() == 201 && phoneNumber.length() > 8) {
                    phoneNumber = phoneNumber.substring(phoneNumber.length() - 8);
                }
                break;
            case KAZAKHSTAN:
                // Phone numbers for Russia must be in format 7XXXXXXXXX
                if (phoneNumber.length() > 10) {
                    phoneNumber = phoneNumber.substring(phoneNumber.length() - 10);
                }
        }

        return String.format(operator.getRecallPattern(), phoneNumber);
    }

    public static boolean isNumberValid(Operator operator, String phoneNumber) {
        boolean isValid = false;
        phoneNumber = stripNumber(phoneNumber);

        switch (operator.getCountry()) {
            case UKRAINE:
                // [38]XXXXXXXXXX
                isValid = (phoneNumber.length() == 12 && phoneNumber.startsWith("38"))
                    || (phoneNumber.length() == 10);
                break;
            case BELARUS:
                // [810][375]XXXXXXXXX
                isValid = (phoneNumber.length() == 15 && phoneNumber.startsWith("810375"))
                    || (phoneNumber.length() == 12 && phoneNumber.startsWith("375"))
                    || (phoneNumber.length() == 9);
                break;
            case RUSSIA:
                // 9XXXXXXXXX
                if (phoneNumber.length() >= 10) {
                    phoneNumber = phoneNumber.substring(phoneNumber.length() - 10);
                    isValid = phoneNumber.startsWith("9");
                }
                break;
            case MOLDOVA:
                isValid = true;
                break;
            case KAZAKHSTAN:
                // 7XXXXXXXXXX
                if (phoneNumber.length() >= 10) {
                    phoneNumber = phoneNumber.substring(phoneNumber.length() - 10);
                    isValid = phoneNumber.startsWith("7");
                }
                break;
        }

        isValid &= phoneNumber.matches("[0-9]+");

        return isValid;
    }

    private static String stripNumber(String phoneNumber) {
        return phoneNumber.replace("-", "")
            .replace("+", "")
            .replace("(", "")
            .replace(")", "")
            .replace(" ", "");
    }
}