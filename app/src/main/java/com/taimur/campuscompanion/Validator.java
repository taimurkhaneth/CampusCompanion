package com.taimur.campuscompanion;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.widget.EditText;

import androidx.annotation.StringRes;

import com.google.android.material.textfield.TextInputLayout;

/**
 * Input validation helpers shared by every form.
 * Each check shows its error directly on the TextInputLayout and returns true when the input is valid.
 */
public final class Validator {

    public static final int MIN_PASSWORD_LENGTH = 6;
    public static final int MIN_NAME_LENGTH = 3;

    private Validator() {
    }

    public static String textOf(TextInputLayout layout) {
        EditText editText = layout.getEditText();
        return editText == null ? "" : editText.getText().toString().trim();
    }

    public static boolean required(TextInputLayout layout) {
        if (TextUtils.isEmpty(textOf(layout))) {
            return fail(layout, R.string.error_required);
        }
        return pass(layout);
    }

    public static boolean email(TextInputLayout layout) {
        String value = textOf(layout);
        if (TextUtils.isEmpty(value)) {
            return fail(layout, R.string.error_required);
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(value).matches()) {
            return fail(layout, R.string.error_invalid_email);
        }
        return pass(layout);
    }

    public static boolean minLength(TextInputLayout layout, int min, @StringRes int errorRes) {
        String value = textOf(layout);
        if (TextUtils.isEmpty(value)) {
            return fail(layout, R.string.error_required);
        }
        if (value.length() < min) {
            return fail(layout, errorRes);
        }
        return pass(layout);
    }

    public static boolean password(TextInputLayout layout) {
        return minLength(layout, MIN_PASSWORD_LENGTH, R.string.error_password_short);
    }

    public static boolean matches(TextInputLayout confirm, TextInputLayout original) {
        String value = textOf(confirm);
        if (TextUtils.isEmpty(value)) {
            return fail(confirm, R.string.error_required);
        }
        if (!value.equals(textOf(original))) {
            return fail(confirm, R.string.error_password_mismatch);
        }
        return pass(confirm);
    }

    /** Removes a field's error as soon as the user starts typing again. */
    public static void clearErrorOnType(TextInputLayout... layouts) {
        for (final TextInputLayout layout : layouts) {
            EditText editText = layout.getEditText();
            if (editText == null) {
                continue;
            }
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    layout.setError(null);
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
        }
    }

    private static boolean fail(TextInputLayout layout, @StringRes int errorRes) {
        layout.setError(layout.getContext().getString(errorRes));
        return false;
    }

    private static boolean pass(TextInputLayout layout) {
        layout.setError(null);
        return true;
    }
}
