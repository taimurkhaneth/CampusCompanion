package com.taimur.campuscompanion;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.DrawableRes;
import androidx.annotation.IdRes;
import androidx.annotation.StringRes;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class ProfileActivity extends BaseActivity {

    private UserManager userManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setupToolbar(R.string.profile_title);

        userManager = new UserManager(this);

        TextView tvName = findViewById(R.id.tvName);
        TextView tvEmail = findViewById(R.id.tvEmail);
        tvName.setText(userManager.getName());
        tvEmail.setText(userManager.getEmail());

        setupRow(R.id.rowUsername, R.drawable.ic_person, R.string.label_username,
                userManager.getUsername());
        setupRow(R.id.rowStudentId, R.drawable.ic_badge, R.string.label_student_id,
                userManager.getStudentId());
        setupRow(R.id.rowProgram, R.drawable.ic_school, R.string.label_program,
                getString(R.string.value_program));

        MaterialButton btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(v -> confirmLogout());
    }

    private void setupRow(@IdRes int rowId, @DrawableRes int iconRes,
                          @StringRes int labelRes, String value) {
        View row = findViewById(rowId);
        ImageView icon = row.findViewById(R.id.ivRowIcon);
        TextView label = row.findViewById(R.id.tvRowLabel);
        TextView text = row.findViewById(R.id.tvRowValue);
        icon.setImageResource(iconRes);
        label.setText(labelRes);
        text.setText(value);
    }

    private void confirmLogout() {
        new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.logout_confirm_title)
                .setMessage(R.string.logout_confirm_message)
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(R.string.btn_logout, (dialog, which) -> logout())
                .show();
    }

    private void logout() {
        userManager.logout();
        Toast.makeText(this, R.string.msg_logged_out, Toast.LENGTH_SHORT).show();

        // Explicit Intent: Profile -> Login, clearing the back stack so Back can't return here
        Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
