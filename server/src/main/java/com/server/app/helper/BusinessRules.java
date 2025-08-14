package com.server.app.helper;

import com.google.common.base.Strings;

public class BusinessRules {
    public static void validate(String... errorMessages) {
        for (String error : errorMessages) {
            if (!Strings.isNullOrEmpty(error)) {
                throw new BusinessException(error);
            }
        }
    }
}
