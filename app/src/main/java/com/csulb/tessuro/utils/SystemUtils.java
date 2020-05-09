package com.csulb.tessuro.utils;

import android.app.Activity;
import android.view.inputmethod.InputMethodManager;

import java.util.Objects;

public class SystemUtils {
    public SystemUtils() {

    }

    public void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        assert inputMethodManager != null;
        inputMethodManager.hideSoftInputFromWindow(Objects.requireNonNull(activity.getCurrentFocus()).getWindowToken(), 0);
    }
}
