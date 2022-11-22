package com.d138.wheere.controller.Guava;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;

public class CustomNamingStrategy extends PropertyNamingStrategies.NamingBase {
    @Override
    public String translate(String input) {

        if (input == null || input.isEmpty()) {
            return input;
        }

        char c = input.charAt(0);
        char lc = Character.toLowerCase(c);

        if (c == lc) {
            return input;
        }

        StringBuilder sb = new StringBuilder(input);
        sb.setCharAt(0, lc);

        return sb.toString();
    }
}