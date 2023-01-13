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

import it.unipd.dei.hyperu.utils.StatusType;

import java.sql.Timestamp;

/**
 * Represents the data about a request from a user to join a specific team.
 *
 * @author Marco Alecci (marco.alecci@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public class JoinRequest {

    /**
     * The Team {@code Team} the user applied for
     */
    private final Team team;

    /**
     * The User {@code User} who made the request
     */
    private final User user;

    /**
     * The status of the join request
     */
    private final StatusType status;

    /**
     * the message attached to the request
     */
    private final String message;

    /**
     * when the request was sent
     */
    private final Timestamp requestedTime;


    /**
     * Creates a new join request
     *
     * @param team          The Team the user applied for
     * @param user          the user who made the request
     * @param status        the status of the request
     * @param message       the message attached to the request
     * @param requestedTime when the request was sent
     */
    public JoinRequest(final Team team, final User user, final StatusType status, final String message, final Timestamp requestedTime) {
        this.team = team;
        this.user = user;
        this.status = status;
        this.message = message;
        this.requestedTime = requestedTime;
    }

    /**
     * Returns the team associated to the join request.
     *
     * @return the team associated to the join request.
     */
    public final Team getTeam() {
        return team;
    }

    /**
     * Returns the user that made the join request.
     *
     * @return the user that made the  join request.
     */
    public final User getUser() {
        return user;
    }

    /**
     * Returns the status of the join request.
     *
     * @return the status of the join request.
     */
    public final StatusType getStatus() {
        return status;
    }

    /**
     * Returns the message attached to the join request.
     *
     * @return the message attached to the join request.
     */
    public final String getMessage() {
        return message;
    }

    /**
     * Returns the requestTime of the join request.
     *
     * @return the requestTime of the join request.
     */
    public final Timestamp getRequestedTime() {
        return requestedTime;
    }


}