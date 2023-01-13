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
 * Represents the data about a skill.
 *
 * @author Luca Martinelli (luca.martinelli.1@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public class Skill {

    /**
     * The ID (identifier) of the skill
     */
    private final int id;

    /**
     * The name of the skill
     */
    private final String name;

    /**
     * The description of the skill
     */
    private final String description;

    /**
     * The level of a skill for a specific user
     */
    private final Integer level;

    /**
     * Creates a new skill
     *
     * @param id          the ID of the skill
     * @param name        the name of the skill.
     * @param description the description of the skill
     */
    public Skill(final int id, final String name, final String description) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.level = null;
    }

    public Skill(final int id, final String name, final String description, final Integer level) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.level = level;
    }

    /**
     * Returns the ID of the skill.
     *
     * @return the ID of the skill.
     */
    public final int getID() {
        return id;
    }

    /**
     * Returns the name of the skill.
     *
     * @return the name of the skill.
     */
    public final String getName() {
        return name;
    }

    /**
     * Returns the description of the skill.
     *
     * @return the description of the skill.
     */
    public final String getDescription() {
        return description;
    }

    /**
     * Returns the level of the skill.
     *
     * @return the level of the skill.
     */
    public final Integer getLevel() {
        return level;
    }

}