package com.taimur.campuscompanion;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

public class RegisterActivity extends BaseActivity {

    private TextInputLayout tilName;
    private TextInputLayout tilEmail;
    private TextInputLayout tilPassword;
    private TextInputLayout tilConfirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setupToolbar(R.string.register_title);

        tilName = findViewById(R.id.tilName);
        tilEmail = findViewById(R.id.tilEmail);
        tilPassword = findViewById(R.id.tilPassword);
        tilConfirmPassword = findViewById(R.id.tilConfirmPassword);
        MaterialButton btnCreateAccount = findViewById(R.id.btnCreateAccount);
        MaterialButton btnBackToLogin = findViewById(R.id.btnBackToLogin);

        Validator.clearErrorOnType(tilName, tilEmail, tilPassword, tilConfirmPassword);

        btnCreateAccount.setOnClickListener(v -> attemptRegister());
        btnBackToLogin.setOnClickListener(v -> finish());
    }

    private void attemptRegister() {
        boolean valid = Validator.minLength(tilName, Validator.MIN_NAME_LENGTH, R.string.error_name_short)
                & Validator.email(tilEmail)
                & Validator.password(tilPassword)
                & Validator.matches(tilConfirmPassword, tilPassword);
        if (!valid) {
            return;
        }

        String email = Validator.textOf(tilEmail);
        new UserManager(this).saveAccount(
                Validator.textOf(tilName),
                email,
                Validator.textOf(tilPassword));

        Toast.makeText(this, R.string.msg_register_success, Toast.LENGTH_SHORT).show();

        // Explicit Intent: Register -> Login, passing the new email so it is pre-filled
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        intent.putExtra(EXTRA_EMAIL, email);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }
}
