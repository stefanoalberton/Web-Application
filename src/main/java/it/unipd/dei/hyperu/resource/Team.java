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

import it.unipd.dei.hyperu.resource.Idea;

/**
 * Represents the data about a group
 *
 * @author Marco Alecci (marco.alecci@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public class Team {

    /**
     * The ID (identifier) of the group
     */
    private final int id;

    /**
     * The name of the group
     */
    private final String name;

    /**
     * When the group was created
     */
    private final Timestamp creationTime;

    /**
     * the idea {@code Idea} associated to the group
     */
    private final Idea idea;

    /**
     * determine if the group is looking for other members
     */
    private final boolean acceptRequests;

    /**
     * Image of the group, will be displayed also in the chat
     */
    private transient byte[] image;
    private final String imageUrl;

    /**
     * Description of the group, will be displayed also in the chat
     */
    private final String description;

    public Team(final int id) {
        this.id = id;
        this.name = null;
        this.creationTime = null;
        this.idea = null;
        this.acceptRequests = true;
        this.image = null;
        this.imageUrl = null;
        this.description = null;
    }

    /**
     * Creates a new group
     *
     * @param id             the ID of the group
     * @param name           the name of the group
     * @param creationTime   when the group was created
     * @param idea           the idea associated to the group
     * @param acceptRequests determine if a group is looking for other members
     * @param image          image of the group
     * @param description    description of the group
     */
    public Team(final int id, final String name, final Timestamp creationTime, final Idea idea, final boolean acceptRequests, final byte[] image, final String description) {
        this.id = id;
        this.name = name;
        this.creationTime = creationTime;
        this.idea = idea;
        this.acceptRequests = acceptRequests;
        this.image = image;
        this.imageUrl = "image/team/" + id;
        this.description = description;
    }

    public Team(final int id, final String name, final Timestamp creationTime, final Idea idea, final boolean acceptRequests) {
        this.id = id;
        this.name = name;
        this.creationTime = creationTime;
        this.idea = idea;
        this.acceptRequests = acceptRequests;
        this.image = null;
        this.imageUrl = "image/team/" + id;
        this.description = null;
    }

    /**
     * Returns the ID of the group.
     *
     * @return the ID of the group.
     */
    public final int getID() {
        return id;
    }

    /**
     * Returns the name of the group.
     *
     * @return the name of the group.
     */
    public final String getName() {
        return name;
    }

    /**
     * Returns the creationTime of the group.
     *
     * @return the creationTime of the group.
     */
    public final Timestamp getCreationTime() {
        return creationTime;
    }

    /**
     * Returns the idea of the group.
     *
     * @return the idea {@code Idea} of the group.
     */
    public final Idea getIdea() {
        return idea;
    }

    /**
     * Returns the acceptRequest boolean indicator of the group
     *
     * @return {@code true} if the group is looking for new members, {@code false} otherwise
     */
    public final boolean canAcceptRequests() {
        return acceptRequests;
    }

    /**
     * Returns the image of the group.
     *
     * @return the image of the group.
     */
    public final byte[] getImage() {
        return image;
    }

    public final void setImage(byte[] image) {
        this.image = image;
    }

    /**
     * Returns the description of the group.
     *
     * @return the description of the group.
     */
    public final String getDescription() {
        return description;
    }

}
