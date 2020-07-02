package com.godnedy.bank.user;

import org.apache.tomcat.jni.Local;

import java.time.LocalDate;
import java.util.Calendar;

public class PeselValidator {

    private static final long LEGAL_AGE = 18L;

    public static boolean validate(String peselNumber) {
        if (peselNumber.length() != 11) {
            return false;
        }
        int[] peselNumberArray = new int[11];
        for (int i = 0; i < 11; i++) {
            peselNumberArray[i] = Integer.parseInt(peselNumber.substring(i, i + 1));
        }
        return hasReachedLegalAge(getBirthDate(peselNumberArray));
    }

    //FIXME month dla osob po 2000 r. jest np. 25
    private static LocalDate getBirthDate(int[] peselNumber) {
        int year;
        int month;
        int day;
        year = 10 * peselNumber[0];
        year += peselNumber[1];
        month = 10 * peselNumber[2];
        month += peselNumber[3];
        day = 10 * peselNumber[4];
        day += peselNumber[5];
        if (month > 0 && month < 13) {
            year += 1900;
        } else if (month > 20 && month < 33) {
            month -= 20;
            year += 2000;
        }
        return LocalDate.of(year, month, day);
    }

    private static boolean hasReachedLegalAge(LocalDate birthDate) {
        return !birthDate.plusYears(LEGAL_AGE).isAfter(LocalDate.now());
    }
}
