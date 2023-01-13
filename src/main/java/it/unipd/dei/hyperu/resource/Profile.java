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

import java.util.Date;
import java.util.List;

import it.unipd.dei.hyperu.utils.GenderType;

/**
 * Represents the data about the profile.
 *
 * @author Marco Alecci (marco.alecci@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public class Profile {

    /**
     * The name of the user
     */
    private final String name;

    /**
     * The surname of the user
     */
    private final String surname;

    /**
     * the birth date of the user
     */
    private final Date birthDate;

    /**
     * the gender of the user
     */
    private final GenderType gender;

    /**
     * profile picture of the user
     */
    private transient byte[] profilePicture;

    /**
     * small biography of the user
     */
    private final String biography;

    private List<Topic> topics;
    private List<Skill> skills;

    private int totalLikes;
    private int totalIdeas;

    /**
     * Creates a new Profile object
     *
     * @param name           the name of the user
     * @param surname        the surname of the user
     * @param birthDate      the birth date of the user
     * @param gender         the gender of the user
     * @param profilePicture profile picture of the user
     * @param biography      small biography of the user
     */
    public Profile(final String name, final String surname, final Date birthDate, final GenderType gender, final String biography, final byte[] profilePicture) {
        this.name = name;
        this.surname = surname;
        this.birthDate = birthDate;
        this.gender = gender;
        this.profilePicture = profilePicture;
        this.biography = biography;
    }

    public Profile(final String name, final String surname, final Date birthDate, final GenderType gender, final String biography) {
        this.name = name;
        this.surname = surname;
        this.birthDate = birthDate;
        this.gender = gender;
        this.biography = biography;
        this.profilePicture = null;
    }

    public Profile() {
        this.name = null;
        this.surname = null;
        this.birthDate = null;
        this.gender = null;
        this.biography = null;
        this.profilePicture = null;
    }

    /**
     * Returns the name of the user.
     *
     * @return the name of the user.
     */
    public final String getName() {
        return name;
    }

    /**
     * Returns the surname of the user.
     *
     * @return the surname of the user.
     */
    public final String getSurname() {
        return surname;
    }

    /**
     * Returns the birth date of the user.
     *
     * @return the birth date {@code UserType} of the user.
     */
    public final Date getBirthDate() {
        return birthDate;
    }

    /**
     * Returns the gender of the user.
     *
     * @return the gender {@code GenderType} of the user.
     */
    public final GenderType getGender() {
        return gender;
    }

    /**
     * Returns the profile picture the user.
     *
     * @return the profile picture of the user.
     */
    public final byte[] getProfilePicture() {
        return profilePicture;
    }

    public final void setProfilePicture(byte[] profilePicture) {
        this.profilePicture = profilePicture;
    }

    /**
     * Returns the biography of the user.
     *
     * @return the biography of the user.
     */
    public final String getBiography() {
        return biography;
    }

    public final List<Skill> getSkills() {
        return skills;
    }

    public final void setSkills(List<Skill> skills) {
        this.skills = skills;
    }

    public final List<Topic> getTopics() {
        return topics;
    }

    public final void setTopics(List<Topic> topics) {
        this.topics = topics;
    }

    public final int getTotalLikes() {
        return totalLikes;
    }

    public final void setTotalLikes(int totalLikes) {
        this.totalLikes = totalLikes;
    }

    public final int getTotalIdeas() {
        return totalIdeas;
    }

    public final void setTotalIdeas(int totalIdeas) {
        this.totalIdeas = totalIdeas;
    }
}
