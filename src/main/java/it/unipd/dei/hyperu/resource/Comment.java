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

/**
 * Represents the data about a comment.
 *
 * @author Marco Alecci (marco.alecci@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public class Comment {

    /**
     * The ID (identifier) of the comment
     */
    private final int id;

    /**
     * The text of the comment
     */
    private final String text;

    /**
     * When the comment was wrote
     */
    private final Timestamp sentTime;

    /**
     * the {@code Idea}'s post
     */
    private final Idea idea;

    /**
     * the {@code User} who post the comment
     */
    private final User user;

    public Comment(final int id) {
        this.id = id;
        this.text = null;
        this.sentTime = null;
        this.idea = null;
        this.user = null;
    }

    public Comment(final int id, final String text) {
        this.id = id;
        this.text = text;
        this.sentTime = null;
        this.idea = null;
        this.user = null;
    }

    /**
     * Creates a new comment
     *
     * @param id       the ID of the comment
     * @param text     the text of the comment
     * @param sentTime when the comment was wrote
     * @param idea     the {@code Idea}
     * @param user     the {@code User} who post the comment
     */
    public Comment(final int id, final String text, final Timestamp sentTime, final Idea idea, final User user) {
        this.id = id;
        this.text = text;
        this.sentTime = sentTime;
        this.idea = idea;
        this.user = user;
    }

    /**
     * Returns the ID of the comment.
     *
     * @return the ID of the comment.
     */
    public final int getID() {
        return id;
    }

    /**
     * Returns the text of the comment.
     *
     * @return the text of the comment.
     */
    public final String getText() {
        return text;
    }

    /**
     * Returns the sentTime of the comment.
     *
     * @return the sentTime of the comment.
     */
    public final Timestamp getSentTime() {
        return sentTime;
    }

    /**
     * Returns the {@code Idea} of the comment.
     *
     * @return the {@code Idea} of the comment.
     */
    public final Idea getIdea() {
        return idea;
    }

    /**
     * Returns the {@code User} of the comment.
     *
     * @return the {@code User} of the comment.
     */
    public final User getUser() {
        return user;
    }
}