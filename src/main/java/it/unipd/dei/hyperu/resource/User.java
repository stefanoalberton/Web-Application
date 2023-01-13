/*
 * Copyright 2021 University of Padua, Italy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package it.unipd.dei.hyperu.resource;

import it.unipd.dei.hyperu.utils.UserType;

/**
 * Represents the data about a user.
 *
 * @author Marco Alecci (marco.alecci@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public class User {

    /**
     * The ID (identifier) of the user
     */
    private final int id;

    /**
     * The username of the user
     */
    private final String username;

    /**
     * The email of the user
     */
    private final String email;

    /**
     * the role {@code UserType} of the user
     */
    private final UserType role;

    /**
     * determine if the user is banned or not
     */
    private final boolean banned;

    private final String profilePictureUrl;

    /**
     * profile Object associated to the user
     */
    private Profile profile;

    /**
     * Creates a new user
     *
     * @param id the ID of the user
     */
    public User(final int id) {
        this.id = id;
        this.username = null;
        this.email = null;
        this.role = null;
        this.banned = false;
        this.profilePictureUrl = null;
        this.profile = null;
    }

    /**
     * Creates a new user
     *
     * @param id       the ID of the user
     * @param username the username of the user
     * @param email    the email of the user
     * @param role     the role of the user
     * @param banned   determine if a user is banned
     * @param profile  Profile Object associated to the user
     */
    public User(final int id, final String username, final String email, final UserType role, final boolean banned, final Profile profile) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.role = role;
        this.banned = banned;
        this.profilePictureUrl = "image/profile/" + id;
        this.profile = profile;
    }

    public User(final int id, final String username, final String email, final UserType role, final boolean banned) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.role = role;
        this.banned = banned;
        this.profilePictureUrl = "image/profile/" + id;
        this.profile = null;
    }

    /**
     * Returns the ID of the user.
     *
     * @return the ID of the user.
     */
    public final int getID() {
        return id;
    }

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
     * Returns the role of the user.
     *
     * @return the role {@code UserType} of the user.
     */
    public final UserType getRole() {
        return role;
    }

    /**
     * Returns the banned indicator of the user.
     *
     * @return {@code true} if user is banned, {@code false} otherwise
     */
    public final boolean isBanned() {
        return banned;
    }

    /**
     * Returns the profile indicator of the user.
     *
     * @return A {@code Profile} object of the user.
     */
    public final Profile getProfile() {
        return profile;
    }

    public final void setProfile(Profile profile) {
        this.profile = profile;
    }
}
