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

public class DashboardActivity extends BaseActivity {

    private UserManager userManager;
    private TextView tvGreeting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        userManager = new UserManager(this);
        tvGreeting = findViewById(R.id.tvGreeting);

        ImageView ivProfile = findViewById(R.id.ivProfile);
        ivProfile.setOnClickListener(v -> openProfile());

        setupModule(R.id.cardTimetable, R.drawable.ic_event,
                R.string.module_timetable, R.string.module_timetable_desc, null);
        setupModule(R.id.cardAssignments, R.drawable.ic_assignment,
                R.string.module_assignments, R.string.module_assignments_desc, null);
        setupModule(R.id.cardAttendance, R.drawable.ic_check_circle,
                R.string.module_attendance, R.string.module_attendance_desc, null);
        setupModule(R.id.cardResults, R.drawable.ic_star,
                R.string.module_results, R.string.module_results_desc, null);
        setupModule(R.id.cardNotices, R.drawable.ic_notifications,
                R.string.module_notices, R.string.module_notices_desc, null);
        setupModule(R.id.cardProfile, R.drawable.ic_person,
                R.string.module_profile, R.string.module_profile_desc, v -> openProfile());
    }

    @Override
    protected void onResume() {
        super.onResume();
        tvGreeting.setText(getString(R.string.dashboard_greeting, userManager.getFirstName()));
    }

    /**
     * Fills one included item_module card with its icon and text.
     * Modules without their own screen yet show a "coming soon" message.
     */
    private void setupModule(@IdRes int cardId, @DrawableRes int iconRes,
                             @StringRes int titleRes, @StringRes int descRes,
                             View.OnClickListener onClick) {
        View card = findViewById(cardId);
        ImageView icon = card.findViewById(R.id.ivModuleIcon);
        TextView title = card.findViewById(R.id.tvModuleTitle);
        TextView desc = card.findViewById(R.id.tvModuleDesc);

        icon.setImageResource(iconRes);
        title.setText(titleRes);
        desc.setText(descRes);

        if (onClick != null) {
            card.setOnClickListener(onClick);
        } else {
            card.setOnClickListener(v -> Toast.makeText(this,
                    getString(R.string.module_coming_soon, getString(titleRes)),
                    Toast.LENGTH_SHORT).show());
        }
    }

    private void openProfile() {
        // Explicit Intent: Dashboard -> Profile
        startActivity(new Intent(DashboardActivity.this, ProfileActivity.class));
    }
}
