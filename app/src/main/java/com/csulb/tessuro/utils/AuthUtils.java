package com.csulb.tessuro.utils;

import android.util.Patterns;

import java.util.regex.Pattern;

public class AuthUtils {

    private final int FULLNAME_MIN_BOUND = 5;
    private final int FULLNAME_MAX_BOUND = 16;
    private final int PW_MIN_BOUND = 6;
    private final int PW_MAX_BOUND = 20;
    private final Pattern USERNAME_PATTERN = Pattern.compile("[a-zA-Z0-9_]+");

    public AuthUtils() {}

    /**
     * Verifies that the username is valid.
     *
     * @param username is the input for the username field.
     * @return true if the username is valid.
     */
    public boolean isUsernameValid(String username) {
        return (USERNAME_PATTERN.matcher(username).matches());
    }

    /**
     * Verifies that the username is valid between the character length bounds.
     *
     * @param username is the input for the username field.
     * @return true if the username is valid between the bounds.
     */
    public boolean isFullnameLengthValid(String username) {
        return (username.length() >= FULLNAME_MIN_BOUND && username.length() <= FULLNAME_MAX_BOUND);
    }

    /**
     * Verifies that the password is valid between the character length bounds.
     *
     * @param password is the input for the password field.
     * @return true if the password is valid between the bounds.
     */
    public boolean isPasswordValid(String password) {
        return (password.length() >= PW_MIN_BOUND && password.length() <= PW_MAX_BOUND);
    }

    /**
     * Verifies that the two passwords entered match each other.
     *
     * @param password         is the input for the password field.
     * @param retyped_password is the input for the retype password field.
     * @return true if the passwords match each other.
     */
    public boolean doPasswordsMatch(String password, String retyped_password) {
        return (password.equals(retyped_password));
    }


    /**
     * Checks to see if the email is a valid entry.
     *
     * @param email is the input for the email field.
     * @return true if the email is valid.
     */
    public boolean isEmailValid(CharSequence email) {
        return (Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    public boolean isRoleValid(String role) {
        return (role.equals("Admin") || role.equals("Standard"));
    }
}
