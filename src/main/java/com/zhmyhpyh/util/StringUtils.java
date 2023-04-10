package com.zhmyhpyh.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class StringUtils {
    public static String getCorrectWordForm(long number, String form1, String form2, String form5) {
        long mod10 = number % 10;
        long mod100 = number % 100;
        if (mod100 >= 11 && mod100 <= 19) {
            return form5;
        } else if (mod10 == 1) {
            return form1;
        } else if (mod10 >= 2 && mod10 <= 4) {
            return form2;
        } else {
            return form5;
        }
    }
}
