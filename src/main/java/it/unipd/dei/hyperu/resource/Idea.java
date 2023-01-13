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

import java.sql.Timestamp;
import java.util.List;

/**
 * Represents the data about a Idea.
 *
 * @author Luca Martinelli (luca.martinelli.1@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public class Idea {

    /**
     * The ID (identifier) of the idea.
     */
    private final int id;

    /**
     * The title of the idea.
     */
    private final String title;

    /**
     * The description of the idea.
     */
    private final String description;

    /**
     * The image attached to the idea.
     */
    private transient byte[] image;
    private final String imageUrl;

    /**
     * When the idea was posted.
     */
    private final Timestamp postedTime;

    /**
     * the number of likes to the idea.
     */
    private final int numLikes;

    /**
     * {@code true} if user likes the idea, {@code false} otherwise
     */
    private boolean liked = false;

    /**
     * the {@code User} who own the idea.
     */
    private final User user;

    /**
     * a list of {@code User}
     */
    private List<Topic> topics;

    /**
     * a list of {@code Skills}
     */
    private List<Skill> skills;

    /**
     * a list of {@code Skills}
     */
    private List<Team> teams;

    /**
     * Creates a new idea
     *
     * @param id the ID of the idea
     */
    public Idea(final int id) {
        this.id = id;
        this.title = null;
        this.description = null;
        this.image = null;
        this.imageUrl = null;
        this.postedTime = null;
        this.numLikes = 0;
        this.user = null;
    }

    /**
     * Creates a new idea
     *
     * @param id          the ID of the idea
     * @param title       the title of the idea
     * @param description the description of the idea
     * @param image       the image attached to the idea
     * @param postedTime  when the idea was sent
     * @param numLikes    the number of likes to the idea
     * @param user        the {@code User} who own the idea
     */
    public Idea(final int id, final String title, final String description, final byte[] image, final Timestamp postedTime, final int numLikes, final User user) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.image = image;
        this.imageUrl = "image/idea/" + id;
        this.postedTime = postedTime;
        this.numLikes = numLikes;
        this.user = user;
    }

    /**
     * Returns the ID of the message.
     *
     * @return the ID of the message.
     */
    public final int getID() {
        return id;
    }

    /**
     * Returns the title of the idea.
     *
     * @return the title of the idea.
     */
    public final String getTitle() {
        return title;
    }

    /**
     * Returns the description of the idea.
     *
     * @return the description of the idea.
     */
    public final String getDescription() {
        return description;
    }

    /**
     * Returns the image of the idea.
     *
     * @return the image of the idea.
     */
    public final byte[] getImage() {
        return image;
    }

    public final void setImage(byte[] image) {
        this.image = image;
    }

    /**
     * Returns the time when the idea was posted.
     *
     * @return the time when the idea was post.
     */
    public final Timestamp getPostedTime() {
        return postedTime;
    }

    /**
     * Returns the number of likes to the idea.
     *
     * @return the number of likes to the idea.
     */
    public final int getNumLikes() {
        return numLikes;
    }

    /**
     * Returns the {@code User} who own the idea.
     *
     * @return the {@code User} who own the message.
     */
    public final User getUser() {
        return user;
    }

    /**
     * Returns the {@code skill} required to the idea
     *
     * @return the {@code User} required to the idea
     */
    public final List<Skill> getSkills() {
        return skills;
    }

    /**
     * Set the the list of the {@code Skill} required for the realization of the idea
     *
     * @param skills the list of the {@code Skill} required for the realization of the idea
     */
    public final void setSkills(List<Skill> skills) {
        this.skills = skills;
    }

    /**
     * Returns the list of the {@code Topic}
     *
     * @return the list of the {@code Topic}
     */
    public final List<Topic> getTopics() {
        return topics;
    }

    /**
     * Set a new list of{@code Topic}
     */
    public final void setTopics(List<Topic> topics) {
        this.topics = topics;
    }

    /**
     * Returns the list of the {@code Teams}
     *
     * @return the list of the {@code Teams}
     */
    public final List<Team> getTeams() {
        return teams;
    }

    /**
     * Set a new list of{@code Teams}
     */
    public final void setTeams(List<Team> teams) {
        this.teams = teams;
    }

    /**
     * Returns {@code true} if the user likes the idea, {@code false} otherwise
     *
     * @return {@code true} if the user likes the idea, {@code false} otherwise
     */
    public final boolean getLiked() {
        return liked;
    }

    /**
     * Set liked
     */
    public final void setLiked(boolean liked) {
        this.liked = liked;
    }
}
