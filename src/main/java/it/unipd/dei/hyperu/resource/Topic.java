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

/**
 * Represents the data about a topic.
 *
 * @author Luca Martinelli (luca.martinelli.1@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public class Topic {

    /**
     * The ID (identifier) of the topic
     */
    private final int id;

    /**
     * The name of the topic
     */
    private final String name;

    /**
     * The description of the topic
     */
    private final String description;

    /**
     * Creates a new topic
     *
     * @param id          the ID of the topic
     * @param name        the name of the topic.
     * @param description the description of the topic
     */
    public Topic(final int id, final String name, final String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    /**
     * Returns the ID of the topic.
     *
     * @return the ID of the topic.
     */
    public final int getID() {
        return id;
    }

    /**
     * Returns the name of the topic.
     *
     * @return the name of the topic.
     */
    public final String getName() {
        return name;
    }

    /**
     * Returns the description of the topic.
     *
     * @return the description of the topic.
     */
    public final String getDescription() {
        return description;
    }

}