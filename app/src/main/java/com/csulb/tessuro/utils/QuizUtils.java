package com.csulb.tessuro.utils;

public class QuizUtils {

    public boolean isTextLengthValid(String text, int minLength, int maxLength) {
        return (text.length() >= minLength && text.length() <= maxLength);
    }

    public boolean isNumberValid(int number, int minValue, int maxValue) {
        return (number >= minValue && number <= maxValue);
    }
}
