package com.taimur.campuscompanion;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

/** Simulated password recovery: checks the email locally, then moves on to Reset Password. */
public class ForgotPasswordActivity extends BaseActivity {

    private TextInputLayout tilEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        setupToolbar(R.string.forgot_title);

        tilEmail = findViewById(R.id.tilEmail);
        MaterialButton btnSendRecovery = findViewById(R.id.btnSendRecovery);

        Validator.clearErrorOnType(tilEmail);
        btnSendRecovery.setOnClickListener(v -> sendRecovery());
    }

    private void sendRecovery() {
        if (!Validator.email(tilEmail)) {
            return;
        }

        String email = Validator.textOf(tilEmail);
        if (!new UserManager(this).emailExists(email)) {
            tilEmail.setError(getString(R.string.error_email_not_found));
            return;
        }

        Toast.makeText(this, getString(R.string.msg_recovery_sent, email), Toast.LENGTH_LONG).show();

        // Explicit Intent: Forgot Password -> Reset Password (as if the user tapped the emailed link)
        Intent intent = new Intent(ForgotPasswordActivity.this, ResetPasswordActivity.class);
        intent.putExtra(EXTRA_EMAIL, email);
        startActivity(intent);
        finish();
    }
}
