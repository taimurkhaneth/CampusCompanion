package com.taimur.campuscompanion;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Locale;

/**
 * Simulated local "backend". Stores a single account in SharedPreferences.
 * On first launch a demo account is created so the app can be tried immediately.
 * No real server is used: login, registration and password reset all happen on the device.
 */
public class UserManager {

    public static final String DEMO_NAME = "Demo Student";
    public static final String DEMO_EMAIL = "student@uni.edu";
    public static final String DEMO_PASSWORD = "123456";

    private static final String PREFS_NAME = "campus_companion_prefs";
    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_LOGGED_IN = "logged_in";

    private final SharedPreferences prefs;

    public UserManager(Context context) {
        prefs = context.getApplicationContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        if (!prefs.contains(KEY_EMAIL)) {
            saveAccount(DEMO_NAME, DEMO_EMAIL, DEMO_PASSWORD);
        }
    }

    /** Registers (or replaces) the account stored on this device. */
    public void saveAccount(String name, String email, String password) {
        prefs.edit()
                .putString(KEY_NAME, name.trim())
                .putString(KEY_EMAIL, email.trim().toLowerCase(Locale.ROOT))
                .putString(KEY_PASSWORD, password)
                .putBoolean(KEY_LOGGED_IN, false)
                .apply();
    }

    /** The identifier may be the email address or the username (the part before the @). */
    public boolean login(String identifier, String password) {
        String id = identifier.trim();
        boolean idMatches = id.equalsIgnoreCase(getEmail()) || id.equalsIgnoreCase(getUsername());
        if (idMatches && password.equals(prefs.getString(KEY_PASSWORD, ""))) {
            prefs.edit().putBoolean(KEY_LOGGED_IN, true).apply();
            return true;
        }
        return false;
    }

    public boolean emailExists(String email) {
        return email.trim().equalsIgnoreCase(getEmail());
    }

    /** Simulated password recovery: updates the stored password if the email matches. */
    public boolean resetPassword(String email, String newPassword) {
        if (!emailExists(email)) {
            return false;
        }
        prefs.edit().putString(KEY_PASSWORD, newPassword).apply();
        return true;
    }

    public void logout() {
        prefs.edit().putBoolean(KEY_LOGGED_IN, false).apply();
    }

    public boolean isLoggedIn() {
        return prefs.getBoolean(KEY_LOGGED_IN, false);
    }

    public String getName() {
        return prefs.getString(KEY_NAME, DEMO_NAME);
    }

    public String getEmail() {
        return prefs.getString(KEY_EMAIL, DEMO_EMAIL);
    }

    public String getUsername() {
        String email = getEmail();
        int at = email.indexOf('@');
        return at > 0 ? email.substring(0, at) : email;
    }

    public String getFirstName() {
        String name = getName();
        int space = name.indexOf(' ');
        return space > 0 ? name.substring(0, space) : name;
    }

    /** A stable, made-up student ID derived from the email. */
    public String getStudentId() {
        int number = (getEmail().hashCode() & 0x7fffffff) % 10000;
        return String.format(Locale.US, "CC-2026-%04d", number);
    }
}
