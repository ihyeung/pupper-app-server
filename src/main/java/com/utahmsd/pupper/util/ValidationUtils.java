package com.utahmsd.pupper.util;

import com.amazonaws.util.StringUtils;
import org.joda.time.DateTime;

public class ValidationUtils {

    public static boolean isValidEmail(String input) {
        return !StringUtils.isNullOrEmpty(input) &&
                input.matches("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
    }

    public static boolean isValidDate(String input) {
        if (StringUtils.isNullOrEmpty(input) || !input.matches("^[0-9]{4}-[0-9]{2}-[0-9]{2}$")) {
            return false;
        }
        String[] units = input.split("-");
        return isValidYear(units[0]) && isValidMonth(units[1]) && isValidDay(units[2]);
    }

    private static boolean isValidYear(String val) {
        return Integer.parseInt(val) >= 1900 && Integer.parseInt(val) <= DateTime.now().getYear();
    }

    private static boolean isValidMonth(String val) {
        return Integer.parseInt(val) >= 1 && Integer.parseInt(val) <= 12;
    }

    private static boolean isValidDay(String val) {
        return Integer.parseInt(val) >= 1 && Integer.parseInt(val) <= 31;
    }
}
