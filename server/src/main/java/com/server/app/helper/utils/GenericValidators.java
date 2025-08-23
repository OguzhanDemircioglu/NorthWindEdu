package com.server.app.helper.utils;

public class GenericValidators {

    public static boolean isNotContainsNumbers(String value) {
        return !value.chars().allMatch(Character::isDigit);
    }

    public static boolean isInvalidPhoneNumber(String phone) {
        return !phone.matches("^0\\d{10}$");
    }

    public static boolean isNotValidCountry(String country) {
        return !country.matches("^[\\p{L} ]+$");
    }
}
