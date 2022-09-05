package com.afba.imageplus.utilities;

import org.springframework.stereotype.Component;

@Component
public class StringHelper {

    public String addNumberToNumericString(String numericString, Long number) {
        Long numberFromString = Long.parseLong(numericString);
        numberFromString += number;
        return String.format("%09d", numberFromString);
    }

    public String convertNumberToString(Long number) {
        return String.format("%09d", number);
    }
}
