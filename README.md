# Campus Companion

A simple student helper app for Android, built with **Java + XML** for Assignment 2 (Android Application Development). It covers the common screens every app needs: splash, login, registration, password recovery, a dashboard and a profile. Authentication is simulated locally, so no backend is required.

## Demo account

| Email / username | Password |
|---|---|
| `student@uni.edu` (or `student`) | `123456` |

You can also register a new account; it replaces the demo account on the device.

## Screens and features

| Screen | What it does | Layout used |
|---|---|---|
| **Splash** | Shows the logo, app name and tagline for 2 seconds, then opens Login | ConstraintLayout |
| **Login** | Email/username and password fields, Login, Register and Forgot Password buttons, with validation and an error for wrong credentials | ConstraintLayout (packed chain for the Register row) |
| **Register** | Full name, email, password and confirm password, with validation (required fields, valid email, min 3-char name, min 6-char password, passwords match) | LinearLayout |
| **Forgot Password** | Accepts an email, checks it exists, and simulates sending a recovery link | LinearLayout |
| **Reset Password** | New password and confirm password with validation, then saves the new password | LinearLayout |
| **Dashboard** | Greets the user and shows six modules (Timetable, Assignments, Attendance, Results, Notices, Profile) as cards with an icon and text | ConstraintLayout + **GridLayout** (2 columns) |
| **Profile** | Shows name, email, username, student ID and program, with a Logout button (with confirmation) | ConstraintLayout |

## Navigation flow

```
Splash ──► Login ──► Dashboard ──► Profile ──► (Logout) ──► Login
             │                        ▲
             │                        └── profile icon / Profile card
             ├──► Register ──► Login (email pre-filled)
             └──► Forgot Password ──► Reset Password ──► Login (email pre-filled)
```

All navigation uses **explicit Intents** (`new Intent(this, TargetActivity.class)`). The email is passed between screens with an Intent extra. Logout clears the back stack with `FLAG_ACTIVITY_NEW_TASK | FLAG_ACTIVITY_CLEAR_TASK`, so pressing Back cannot return to the Dashboard.

## How the technical requirements are met

- **Default and alternative resources:** all text is in `res/values/strings.xml`; colors are in `res/values/colors.xml` with dark-mode overrides in `res/values-night/colors.xml`. `values/bools.xml` and `values-night/bools.xml` switch the status-bar icon color. The theme extends `Theme.MaterialComponents.DayNight`, so switching the phone to dark mode changes the whole app automatically.
- **Layouts:** LinearLayout (Register, Forgot, Reset), GridLayout (Dashboard module grid), ConstraintLayout (Splash, Login, Dashboard root, Profile).
- **Input controls:** `TextInputLayout` + `TextInputEditText` with `inputType` set to `textPersonName`, `textEmailAddress` and `textPassword`, plus a show/hide password toggle.
- **Button clicks and validation:** every button has a click listener; `Validator.java` checks required fields, email format (`Patterns.EMAIL_ADDRESS`), minimum lengths and matching passwords, and shows errors inline on the field.
- **Consistent UI:** shared styles in `themes.xml`, a shared toolbar (`view_toolbar.xml`), a reusable module card (`item_module.xml`) and common dimensions in `dimens.xml`.

## Project structure

```
app/src/main/
├── java/com/taimur/campuscompanion/
│   ├── SplashActivity.java
│   ├── LoginActivity.java
│   ├── RegisterActivity.java
│   ├── ForgotPasswordActivity.java
│   ├── ResetPasswordActivity.java
│   ├── DashboardActivity.java
│   ├── ProfileActivity.java
│   ├── BaseActivity.java       # shared toolbar setup
│   ├── UserManager.java        # simulated local auth (SharedPreferences)
│   └── Validator.java          # input validation helpers
└── res/
    ├── layout/                 # one XML layout per screen + reusable items
    ├── drawable/               # vector icons and shapes
    ├── values/                 # strings, colors, dimens, themes (light)
    └── values-night/           # colors and bools for dark mode
```

## How to run

1. Open the project in Android Studio (Koala or newer).
2. Let Gradle sync (Android Gradle Plugin 8.5.2, Gradle 8.7, JDK 17).
3. Run on an emulator or device with Android 8.0 (API 26) or higher.
4. To see the night-mode resources, turn on Dark theme in the device settings.
