package com.godnedy.bank.user;

import java.time.LocalDate;

public class PersonalIdNumberValidator {

    private static final long LEGAL_AGE = 18L;

    public static boolean validate(String personalIdNumber) {
        if (personalIdNumber.length() != 11) {
            return false;
        }
        int[] personalIdNumberArray = new int[11];
        for (int i = 0; i < 11; i++) {
            personalIdNumberArray[i] = Integer.parseInt(personalIdNumber.substring(i, i + 1));
        }
        return hasReachedLegalAge(getBirthDate(personalIdNumberArray));
    }

    private static LocalDate getBirthDate(int[] personalIdNumber) {
        int year;
        int month;
        int day;
        year = 10 * personalIdNumber[0];
        year += personalIdNumber[1];
        month = 10 * personalIdNumber[2];
        month += personalIdNumber[3];
        day = 10 * personalIdNumber[4];
        day += personalIdNumber[5];
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
