package com.taimur.campuscompanion;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;

/** Shared setup for inner screens so every toolbar looks and behaves the same. */
public abstract class BaseActivity extends AppCompatActivity {

    public static final String EXTRA_EMAIL = "com.taimur.campuscompanion.EXTRA_EMAIL";

    protected void setupToolbar(@StringRes int titleRes) {
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitle(titleRes);
            toolbar.setNavigationOnClickListener(v -> finish());
        }
    }
}
