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

import it.unipd.dei.hyperu.database.UserDAO;
import it.unipd.dei.hyperu.resource.Comment;
import it.unipd.dei.hyperu.resource.User;
import it.unipd.dei.hyperu.utils.ErrorCode;
import it.unipd.dei.hyperu.utils.InfoMessage;
import it.unipd.dei.hyperu.resource.ResultMessage;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Manages the REST API for the {@link User} resource.
 *
 * @author Marco Alecci (marco.alecci@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */

public class AdminRestResource extends AbstractRestResource {

    /**
     * Creates a new REST resource for managing {@code User} resources.
     *
     * @param req the HTTP request.
     * @param res the HTTP response.
     */
    public AdminRestResource(HttpServletRequest req, HttpServletResponse res) {
        super(req, res);
    }

    /**
     * list all the administrator
     *
     * @throws IOException if any error occurs in the client/server communication.
     */
    public void listAdministrators() throws IOException {

        List<User> administrators = null;
        ResultMessage resultMessage;

        try {
            administrators = UserDAO.listAdministrators();

            resultMessage = new ResultMessage(InfoMessage.ADMINISTRATORS_LISTED);

        } catch (SQLException | NamingException ex) {
            resultMessage = new ResultMessage(ErrorCode.CANNOT_ACCESS_DATABASE, ex.getMessage());
        }

        writeResult(resultMessage, administrators);
    }

    /**
     * Promote a user to the role of moderator by its username
     *
     * @throws IOException if any error occurs in the client/server communication.
     */
    public void promoteToModerator() throws IOException {
        String[] tokens = getTokens("moderator");

        ResultMessage resultMessage;

        try {
            int userID = Integer.parseInt(tokens[0]);

            boolean updated = UserDAO.promoteToModerator(new User(userID));

            if (updated) {
                resultMessage = new ResultMessage(InfoMessage.USER_PROMOTED);
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
     * Promote a user to the role of moderator by its username
     *
     * @throws IOException if any error occurs in the client/server communication.
     */
    public void promoteToModeratorByUsername() throws IOException {
        String[] tokens = getTokens("moderator");

        ResultMessage resultMessage;

        try {
            String username = tokens[0];

            boolean updated = UserDAO.promoteToModerator(new User(-1, username, null, null, false));

            if (updated) {
                resultMessage = new ResultMessage(InfoMessage.USER_PROMOTED);
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
     * downgrade a user to the role of common user
     *
     * @throws IOException if any error occurs in the client/server communication.
     */
    public void downgradeToUser() throws IOException {
        String[] tokens = getTokens("moderator");

        ResultMessage resultMessage;
        int downgradedUserID;

        try {
            downgradedUserID = Integer.parseInt(tokens[0]);

            boolean updated = UserDAO.downgradeToUser(new User(downgradedUserID));

            if (updated) {
                resultMessage = new ResultMessage(InfoMessage.USER_DOWNGRADED);
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
