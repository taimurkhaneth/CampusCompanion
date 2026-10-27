package com.taimur.campuscompanion;

import android.content.Intent;
import android.os.Bundle;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends BaseActivity {

    private TextInputLayout tilIdentifier;
    private TextInputLayout tilPassword;
    private UserManager userManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userManager = new UserManager(this);

        tilIdentifier = findViewById(R.id.tilIdentifier);
        tilPassword = findViewById(R.id.tilPassword);
        MaterialButton btnLogin = findViewById(R.id.btnLogin);
        MaterialButton btnRegister = findViewById(R.id.btnRegister);
        MaterialButton btnForgotPassword = findViewById(R.id.btnForgotPassword);

        Validator.clearErrorOnType(tilIdentifier, tilPassword);

        btnLogin.setOnClickListener(v -> attemptLogin());

        EditText etPassword = findViewById(R.id.etPassword);
        etPassword.setOnEditorActionListener((view, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                attemptLogin();
                return true;
            }
            return false;
        });

        // Explicit Intent: Login -> Register
        btnRegister.setOnClickListener(v ->
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class)));

        // Explicit Intent: Login -> Forgot Password
        btnForgotPassword.setOnClickListener(v ->
                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class)));

        prefillEmail(getIntent());
    }

    /** Called when Register or Reset Password sends the user back here. */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        prefillEmail(intent);
    }

    private void prefillEmail(Intent intent) {
        String email = intent.getStringExtra(EXTRA_EMAIL);
        EditText etIdentifier = tilIdentifier.getEditText();
        EditText etPassword = tilPassword.getEditText();
        if (email != null && etIdentifier != null && etPassword != null) {
            etIdentifier.setText(email);
            etPassword.setText("");
            tilIdentifier.setError(null);
            tilPassword.setError(null);
            etPassword.requestFocus();
        }
    }

    private void attemptLogin() {
        // Use & (not &&) so every field shows its error at once
        boolean valid = Validator.required(tilIdentifier) & Validator.password(tilPassword);
        if (!valid) {
            return;
        }

        String identifier = Validator.textOf(tilIdentifier);
        String password = Validator.textOf(tilPassword);

        if (userManager.login(identifier, password)) {
            Toast.makeText(this, getString(R.string.msg_login_success, userManager.getFirstName()),
                    Toast.LENGTH_SHORT).show();

            // Explicit Intent: Login -> Dashboard (Login is closed so Back exits the app)
            Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
            startActivity(intent);
            finish();
        } else {
            tilPassword.setError(getString(R.string.error_invalid_login));
        }
    }
}
