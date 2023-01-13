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
 * Gets the info messages.
 *
 * @author Luca Martinelli (luca.martinelli.1@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public enum InfoMessage {
    GENERAL_SUCCESS("Operation completed."),

    LOGGED_IN("Logged in successfully."),
    LOGGED_OUT("Logged out successfully."),

    USER_REGISTERED("User registered."),
    USER_UPDATED("User information updated."),
    USER_PASSWORD_UPDATED("Password updated."),
    USER_IMAGE_UPDATED("Profile picture updated."),
    USER_IMAGE_REMOVED("Profile picture removed."),
    USER_SKILL_ADDED("Skill added to the user."),
    USER_SKILL_UPDATED("Skill of the user updated."),
    USER_SKILL_REMOVED("Skill removed from the user."),
    USER_TOPIC_ADDED("Topic added to the user."),
    USER_TOPIC_REMOVED("Topic removed from the user."),
    USER_INFO_RETRIEVED("User information retrieved."),
    USER_LISTED("Users listed."),

    ADMINISTRATORS_LISTED("Administrators listed."),
    MODERATORS_LISTED("Moderators listed."),
    USER_PROMOTED("User promoted to moderator."),
    USER_DOWNGRADED("Moderator downgraded to user."),

    BANNED_USER_LISTED("Banned users listed."),
    USER_BANNED("User banned."),
    USER_READMITTED("User readmitted."),

    SKILLS_LISTED("Skills listed."),
    SKILL_CREATED("Skill created."),
    SKILL_UPDATED("Skill updated."),
    SKILL_DELETED("Skill deleted."),

    TOPICS_LISTED("Topics listed."),
    TOPIC_CREATED("Topic created."),
    TOPIC_UPDATED("Topic updated."),
    TOPIC_DELETED("Topic deleted."),

    IDEA_LISTED("Ideas listed."),
    IDEA_INFO_RETRIEVED("Idea information retrieved."),
    IDEA_CREATED("Idea created."),
    IDEA_UPDATED("Idea updated."),
    IDEA_DELETED("Idea deleted."),
    IDEA_IMAGE_UPDATED("Idea's image updated."),
    IDEA_IMAGE_REMOVED("Idea's image removed."),
    IDEA_SKILL_ADDED("Skill added to the idea."),
    IDEA_SKILL_REMOVED("Skill removed from the idea."),
    IDEA_TOPIC_ADDED("Topic added to the idea"),
    IDEA_TOPIC_REMOVED("Topic removed from the idea."),
    IDEA_LIKED("Idea liked."),
    IDEA_UNLIKED("Idea unliked."),

    COMMENT_LISTED("Comments listed."),
    COMMENT_SENT("Comment sent."),
    COMMENT_UPDATED("Comment updated."),
    COMMENT_DELETED("Comment deleted."),

    TEAM_LISTED("Teams listed."),
    TEAM_INFO_RETRIEVED("Team information retrieved."),
    TEAM_CREATED("Team created."),
    TEAM_UPDATED("Team updated."),
    TEAM_DELETED("Team deleted."),
    TEAM_IMAGE_UPDATED("Team's image updated."),
    TEAM_IMAGE_REMOVED("Team's image removed."),
    TEAM_MEMBERS_LISTED("Team members listed."),
    TEAM_MEMBER_REMOVED("Member removed from the team."),

    MESSAGE_LISTED("Messages listed."),
    MESSAGE_SENT("Message sent."),
    MESSAGE_UPDATED("Message updated."),
    MESSAGE_DELETED("Message deleted."),

    JOIN_REQUEST_LISTED("Requests listed."),
    JOIN_REQUEST_SENT("Request sent."),
    JOIN_REQUEST_ACCEPTED("Request accepted."),
    JOIN_REQUEST_DELETED("Request refused.");

    /**
     * The message to send.
     */
    private final String infoMessage;

    /**
     * Initialize the {@code InfoMessage}.
     *
     * @param infoMessage Message of information.
     */
    InfoMessage(String infoMessage) {
        this.infoMessage = infoMessage;
    }

    /**
     * Get the info message.
     *
     * @return the info message.
     */
    public String getInfoMessage() {
        return infoMessage;
    }
}