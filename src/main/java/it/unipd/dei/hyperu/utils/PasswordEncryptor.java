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

/**
 * Convert {@code String} to MD5 encrypted password
 *
 * @author Luca Martinelli (luca.martinelli.1@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public class PasswordEncryptor {

    /**
     * The salt to add
     */
    private static final String SALT = "C7H8N4O2";

    /**
     * Generate the encrypted password
     *
     * @param rawPassword The password to be encrypted
     * @return a {@code String} with the encrypted password.
     */
    public static String addSalt(String rawPassword) {
        return rawPassword + SALT;
    }

}