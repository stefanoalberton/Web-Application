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

package it.unipd.dei.hyperu.rest;

import com.google.gson.JsonParseException;
import it.unipd.dei.hyperu.database.JoinRequestDAO;
import it.unipd.dei.hyperu.resource.*;
import it.unipd.dei.hyperu.utils.ErrorCode;
import it.unipd.dei.hyperu.utils.InfoMessage;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Manages the REST API for the {@link JoinRequest} resource.
 *
 * @author Marco Alecci (marco.alecci@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */


public class JoinRequestRestResource extends AbstractRestResource {

    /**
     * Creates a new REST resource for managing {@code JoinRequest} resources.
     *
     * @param req the HTTP request.
     * @param res the HTTP response.
     */

    public JoinRequestRestResource(HttpServletRequest req, HttpServletResponse res) {
        super(req, res);
    }

    /**
     * send a new request join
     *
     * @throws IOException
     *             if any error occurs in the client/server communication.
     */
    public void sendJoinRequest() throws IOException {
        String[] tokens = getTokens("team");
        ResultMessage resultMessage;

        try {
            int teamID = Integer.parseInt(tokens[0]);

            JoinRequest joinRequest = getJSON(JoinRequest.class);

            User loggedUser = getLoggedUser();

            boolean added = JoinRequestDAO.sendJoinRequest(new JoinRequest(new Team(teamID), loggedUser, null, joinRequest.getMessage().trim(), null));

            if (added) {
                resultMessage = new ResultMessage(InfoMessage.JOIN_REQUEST_SENT);
            } else {
                resultMessage = new ResultMessage(ErrorCode.NO_ELEMENT_ADDED);
            }
        } catch (SQLException | NamingException ex) {
            if (ex instanceof SQLException && ((SQLException) ex).getSQLState().equals(DUPLICATE_ERROR_CODE)) {
                resultMessage = new ResultMessage(ErrorCode.REQUEST_ALREADY_EXISTS, ex.getMessage());
            } else {
                resultMessage = new ResultMessage(ErrorCode.CANNOT_ACCESS_DATABASE, ex.getMessage());
            }
        } catch (NumberFormatException | IndexOutOfBoundsException | JsonParseException | NullPointerException ex) {
            resultMessage = new ResultMessage(ErrorCode.BAD_INPUT, ex.getMessage());
        }

        writeResult(resultMessage);

    }

    /**
     * accept the join request
     *
     * @throws IOException
     *             if any error occurs in the client/server communication.
     */
    public void acceptJoinRequest() throws IOException {
        String[] tokens = getTokens("team");

        ResultMessage resultMessage;

        try {
            int teamID = Integer.parseInt(tokens[0]);
            int userID = Integer.parseInt(tokens[2]);

            boolean updated = JoinRequestDAO.acceptJoinRequest(new JoinRequest(new Team(teamID), new User(userID), null, null, null));

            if (updated) {
                resultMessage = new ResultMessage(InfoMessage.JOIN_REQUEST_ACCEPTED);
            } else {
                resultMessage = new ResultMessage(ErrorCode.NO_CHANGES);
            }
        } catch (SQLException | NamingException ex) {
            resultMessage = new ResultMessage(ErrorCode.CANNOT_ACCESS_DATABASE, ex.getMessage());
        } catch (NumberFormatException | IndexOutOfBoundsException ex) {
            resultMessage = new ResultMessage(ErrorCode.BAD_INPUT, ex.getMessage());
        }

        writeResult(resultMessage);
    }

    /**
     * delete the join request
     *
     * @throws IOException
     *             if any error occurs in the client/server communication.
     */
    public void deleteJoinRequest() throws IOException {
        String[] tokens = getTokens("team");

        ResultMessage resultMessage;

        try {
            int teamID = Integer.parseInt(tokens[0]);
            int userID = Integer.parseInt(tokens[2]);

            boolean deleted = JoinRequestDAO.deleteJoinRequest(new JoinRequest(new Team(teamID), new User(userID), null, null, null));

            if (deleted) {
                resultMessage = new ResultMessage(InfoMessage.JOIN_REQUEST_DELETED);
            } else {
                resultMessage = new ResultMessage(ErrorCode.NO_CHANGES);
            }
        } catch (SQLException | NamingException ex) {
            resultMessage = new ResultMessage(ErrorCode.CANNOT_ACCESS_DATABASE, ex.getMessage());
        } catch (NumberFormatException | IndexOutOfBoundsException ex) {
            resultMessage = new ResultMessage(ErrorCode.BAD_INPUT, ex.getMessage());
        }

        writeResult(resultMessage);
    }

}
