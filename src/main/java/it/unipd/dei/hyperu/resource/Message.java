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
 * Represents the data about a message.
 *
 * @author Marco Alecci (marco.alecci@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public class Message {

    /**
     * The ID (identifier) of the message
     */
    private final int id;

    /**
     * The content of the message
     */
    private final String content;

    /**
     * The file attached to the message
     */
    private final byte[] file;

    private final String fileUrl;

    /**
     * The file information
     */
    private final FileInfo fileInfo;


    /**
     * When the message was sent
     */
    private final Timestamp sentTime;

    /**
     * the {@code Team} chat
     */
    private final Team team;

    /**
     * the {@code User} who sent the message
     */
    private final User user;

    public Message(final int id) {
        this.id = id;
        this.content = null;
        this.file = null;
        this.fileUrl = null;
        this.fileInfo = null;
        this.sentTime = null;
        this.team = null;
        this.user = null;
    }

    /**
     * Creates a new message
     *
     * @param id       the ID of the message
     * @param content  the content of the message
     * @param file     the file attached to the message
     * @param fileInfo the {@code FileInfo} object
     * @param sentTime when the message was sent
     * @param team     the {@code Team} Chat
     * @param user     the {@code User} who sent the message
     */
    public Message(final int id, final String content, final byte[] file, final FileInfo fileInfo, final Timestamp sentTime, final Team team, final User user) {
        this.id = id;
        this.content = content;
        this.file = file;
        this.fileUrl = fileInfo != null ? "file/message/" + id : null;
        this.fileInfo = fileInfo;
        this.sentTime = sentTime;
        this.team = team;
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
     * Returns the content of the message.
     *
     * @return the content of the message.
     */
    public final String getContent() {
        return content;
    }

    /**
     * Returns the file of the message.
     *
     * @return the file of the message.
     */
    public final byte[] getFile() {
        return file;
    }

    /**
     * Returns the sent_time of the message.
     *
     * @return the sent_time of the message.
     */
    public final Timestamp getSentTime() {
        return sentTime;
    }

    /**
     * Returns the {@code Team} of the message.
     *
     * @return the {@code Team} of the message.
     */
    public final Team getTeam() {
        return team;
    }

    /**
     * Returns the {@code User} of the message.
     *
     * @return the {@code User} of the message.
     */
    public final User getUser() {
        return user;
    }

    /**
     * Returns the {@code FileInfo} of the file.
     *
     * @return the {@code FileInfo} of the file.
     */
    public final FileInfo getFileInfo() {
        return fileInfo;
    }
}