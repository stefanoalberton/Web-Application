package it.unipd.dei.hyperu.resource;

import it.unipd.dei.hyperu.utils.UserType;

/**
 * Represents the data about a user.
 *
 * @author Luca Martinelli (luca.martinelli.1@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public class UserRegister {

    /**
     * The username of the user
     */
    private String username;

    /**
     * The email of the user
     */
    private String email;

    /**
     * the role {@code UserType} of the user
     */
    private UserType role;

    /**
     * determine if the user is banned or not
     */
    private boolean banned;

    /**
     * profile Object associated to the user
     */
    private Profile profile;

    /**
     * The password of the user
     */
    private String password;

    /**
     * A check password of the user
     */
    private String passwordCheck;

    /**
     * Returns the username of the user.
     *
     * @return the username of the user.
     */
    public final String getUsername() {
        return username;
    }

    /**
     * Returns the email of the user.
     *
     * @return the email of the user.
     */
    public final String getEmail() {
        return email;
    }

    /**
     * Returns the profile indicator of the user.
     *
     * @return A {@code Profile} object of the user.
     */
    public final Profile getProfile() {
        return profile;
    }

    /**
     * Returns the password chosen by the user.
     *
     * @return the password chosen by the user.
     */
    public final String getPassword() {
        return password;
    }

    /**
     * Returns the password check.
     *
     * @return the password check.
     */
    public final String getPasswordCheck() {
        return passwordCheck;
    }
}