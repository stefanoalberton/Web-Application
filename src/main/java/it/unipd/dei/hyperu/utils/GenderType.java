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

package it.unipd.dei.hyperu.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Enumeration data type for genders.
 *
 * @author Luca Martinelli (luca.martinelli.1@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public enum GenderType {
    NOT_DECLARED("Not Declared"),
    MALE("Male"),
    FEMALE("Female");

    /**
     * The label of each enum data.
     */
    public final String label;

    /**
     * @param label the label associated to the enum data.
     */
    GenderType(String label) {
        this.label = label;
    }

    /**
     * The map to store enums.
     */
    private static final Map<String, GenderType> BY_LABEL = new HashMap<>();

    static {
        for (GenderType e : values()) {
            BY_LABEL.put(e.label, e);
        }
    }

    /**
     * @param label the label of the enum to search.
     * @return {@code GenderType} enum.
     */
    public static GenderType valueOfLabel(String label) {
        return BY_LABEL.get(label);
    }

    public String toString() {
        return label;
    }
}