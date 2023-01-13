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

import javax.servlet.http.HttpServletResponse;

/**
 * Gets the error code and error details.
 *
 * @author Luca Martinelli (luca.martinelli.1@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public enum ErrorCode {
    CANNOT_ACCESS_DATABASE(-100, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Cannot access to the database."),
    GENERAL_ERROR(-101, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error has occurred."),
    OPERATION_UNKNOWN(-102, HttpServletResponse.SC_NOT_FOUND, "Operation unknown."),
    BAD_INPUT(-103, HttpServletResponse.SC_BAD_REQUEST, "Input not allowed."),
    NO_ELEMENT_ADDED(-104, HttpServletResponse.SC_NOT_MODIFIED, "No elements added."),
    NO_CHANGES(-105, HttpServletResponse.SC_NOT_MODIFIED, "No changes."),
    FILE_TOO_LARGE(-106, HttpServletResponse.SC_BAD_REQUEST, "File is too large."),
    IMAGE_TOO_LARGE(-107, HttpServletResponse.SC_BAD_REQUEST, "Image is too large."),
    NOT_IMAGE(-108, HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE, "Uploaded file is not an image."),
    CANNOT_UPLOAD_FILE(-109, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Cannot upload the file."),
    USER_NOT_LOGGED(-110, HttpServletResponse.SC_METHOD_NOT_ALLOWED, "You must be logged in."),
    USER_NOT_AUTHORIZED(-111, HttpServletResponse.SC_METHOD_NOT_ALLOWED, "You cannot access this page."),

    USERNAME_MISSING(-200, HttpServletResponse.SC_BAD_REQUEST, "Username is missing."),
    PASSWORD_MISSING(-201, HttpServletResponse.SC_BAD_REQUEST, "Password is missing."),
    WRONG_CREDENTIALS(-202, HttpServletResponse.SC_BAD_REQUEST, "Credentials are wrong."),
    EMAIL_MISSING(-203, HttpServletResponse.SC_BAD_REQUEST, "Email is missing."),
    PASSWORD_CHECK_MISSING(-204, HttpServletResponse.SC_BAD_REQUEST, "Password checker is missing."),
    DIFFERENT_PASSWORDS(-205, HttpServletResponse.SC_CONFLICT, "Passwords are different."),
    NAME_MISSING(-206, HttpServletResponse.SC_BAD_REQUEST, "Name is missing."),
    SURNAME_MISSING(-207, HttpServletResponse.SC_BAD_REQUEST, "Surname is missing."),
    BIRTHDATE_MISSING(-208, HttpServletResponse.SC_BAD_REQUEST, "Birthdate is missing."),
    AGE_NOT_ACCEPTABLE(-209, HttpServletResponse.SC_NOT_ACCEPTABLE, "You must be at least 16 years old."),
    GENDER_MISSING(-210, HttpServletResponse.SC_BAD_REQUEST, "Gender is missing."),
    DUPLICATE_USER(-211, HttpServletResponse.SC_NOT_ACCEPTABLE, "Username is already taken."),
    OLD_PASSWORD_MISSING(-212, HttpServletResponse.SC_BAD_REQUEST, "Old password is missing."),
    OLD_PASSWORD_WRONG(-213, HttpServletResponse.SC_BAD_REQUEST, "Password is wrong."),
    DUPLICATE_SKILL_USER(-214, HttpServletResponse.SC_NOT_ACCEPTABLE, "You already have this skill."),
    DUPLICATE_TOPIC_USER(-215, HttpServletResponse.SC_NOT_ACCEPTABLE, "You already follow this topic."),
    NO_USER_FOUND(-216, HttpServletResponse.SC_NOT_FOUND, "User doesn't exists."),

    QUERY_MISSING(-300, HttpServletResponse.SC_BAD_REQUEST, "Query is missing."),

    SKILL_NAME_MISSING(-400, HttpServletResponse.SC_BAD_REQUEST, "Name is missing."),
    SKILL_DESCRIPTION_MISSING(-401, HttpServletResponse.SC_BAD_REQUEST, "Description is missing."),
    NO_SKILL_FOUND(-402, HttpServletResponse.SC_NOT_FOUND, "Skill doesn't exists."),

    TOPIC_NAME_MISSING(-500, HttpServletResponse.SC_BAD_REQUEST, "Name is missing."),
    TOPIC_DESCRIPTION_MISSING(-501, HttpServletResponse.SC_BAD_REQUEST, "Description is missing."),
    NO_TOPIC_FOUND(-502, HttpServletResponse.SC_NOT_FOUND, "Topic doesn't exists."),

    IDEA_TITLE_MISSING(-600, HttpServletResponse.SC_BAD_REQUEST, "Title is missing."),
    IDEA_DESCRIPTION_MISSING(-601, HttpServletResponse.SC_BAD_REQUEST, "Description is missing."),
    DUPLICATE_SKILL_IDEA(-602, HttpServletResponse.SC_NOT_ACCEPTABLE, "The idea already has this skill."),
    DUPLICATE_TOPIC_IDEA(-603, HttpServletResponse.SC_NOT_ACCEPTABLE, "The idea already has this topic."),
    NO_IDEA_FOUND(-604, HttpServletResponse.SC_NOT_FOUND, "Idea doesn't exists."),
    IDEA_ALREADY_LIKED(-605, HttpServletResponse.SC_NOT_ACCEPTABLE, "You already liked this idea."),

    COMMENT_TEXT_MISSING(-700, HttpServletResponse.SC_BAD_REQUEST, "Text is missing."),
    NO_COMMENT_FOUND(-701, HttpServletResponse.SC_NOT_FOUND, "Comment doesn't exists."),

    TEAM_NAME_MISSING(-800, HttpServletResponse.SC_BAD_REQUEST, "Name is missing."),
    NO_TEAM_FOUND(-801, HttpServletResponse.SC_NOT_FOUND, "Team doesn't exists."),

    MESSAGE_CONTENT_MISSING(-900, HttpServletResponse.SC_BAD_REQUEST, "Text is missing."),
    NO_MESSAGE_FOUND(-901, HttpServletResponse.SC_NOT_FOUND, "Message doesn't exists."),
    MESSAGE_NOT_FILE(-902, HttpServletResponse.SC_NOT_FOUND, "The message is not a file."),

    REQUEST_ALREADY_EXISTS(-1000, HttpServletResponse.SC_NOT_ACCEPTABLE, "You already requested to join this team."),
    USER_IS_MEMBER(-1001, HttpServletResponse.SC_NOT_ACCEPTABLE, "You are already a member of the team."),
    TEAM_CLOSED(-1002, HttpServletResponse.SC_NOT_ACCEPTABLE, "Team doesn't accept new requests.");

    /**
     * Code (identifier) of the error.
     */
    private final int errorCode;
    /**
     * HTTP error code.
     */
    private final int httpCode;
    /**
     * Message of the error.
     */
    private final String errorMessage;

    /**
     * Initialize the {@code ErrorCode}.
     *
     * @param errorCode    Code (identifier) of the error.
     * @param httpCode     HTTP code of the error.
     * @param errorMessage Message of the error.
     */
    ErrorCode(int errorCode, int httpCode, String errorMessage) {
        this.errorCode = errorCode;
        this.httpCode = httpCode;
        this.errorMessage = errorMessage;
    }

    /**
     * Get the error code.
     *
     * @return the error code.
     */
    public int getErrorCode() {
        return errorCode;
    }

    /**
     * get the HTTP error code.
     *
     * @return the HTTP error code.
     */
    public int getHTTPCode() {
        return httpCode;
    }

    /**
     * Get the error message.
     *
     * @return the error message.
     */
    public String getErrorMessage() {
        return errorMessage;
    }
}