package com.taimur.campuscompanion;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

public class ResetPasswordActivity extends BaseActivity {

    private TextInputLayout tilNewPassword;
    private TextInputLayout tilConfirmNewPassword;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        setupToolbar(R.string.reset_title);

        email = getIntent().getStringExtra(EXTRA_EMAIL);
        if (email == null) {
            // This screen only makes sense after the Forgot Password step
            finish();
            return;
        }

        TextView tvSubtitle = findViewById(R.id.tvResetSubtitle);
        tvSubtitle.setText(getString(R.string.reset_subtitle, email));

        tilNewPassword = findViewById(R.id.tilNewPassword);
        tilConfirmNewPassword = findViewById(R.id.tilConfirmNewPassword);
        MaterialButton btnResetPassword = findViewById(R.id.btnResetPassword);

        Validator.clearErrorOnType(tilNewPassword, tilConfirmNewPassword);
        btnResetPassword.setOnClickListener(v -> resetPassword());
    }

    private void resetPassword() {
        boolean valid = Validator.password(tilNewPassword)
                & Validator.matches(tilConfirmNewPassword, tilNewPassword);
        if (!valid) {
            return;
        }

        new UserManager(this).resetPassword(email, Validator.textOf(tilNewPassword));
        Toast.makeText(this, R.string.msg_password_reset, Toast.LENGTH_SHORT).show();

        // Explicit Intent: Reset Password -> Login (reuses the existing Login screen)
        Intent intent = new Intent(ResetPasswordActivity.this, LoginActivity.class);
        intent.putExtra(EXTRA_EMAIL, email);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }
}
