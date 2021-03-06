package com.ibrahimyousre.ama.util;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class ActivityUtils {

    public static void hideKeyboard(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputMethodManager =
                    ((InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE));
            if (inputMethodManager != null)
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
