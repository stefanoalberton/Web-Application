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

import it.unipd.dei.hyperu.database.IdeaDAO;
import it.unipd.dei.hyperu.database.UserDAO;
import it.unipd.dei.hyperu.resource.Comment;
import it.unipd.dei.hyperu.resource.Idea;
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
 * Manages the REST API for the {@link User} and {@link Idea} resource.
 *
 * @author Marco Alecci (marco.alecci@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public class SearchRestResource extends AbstractRestResource {

    /**
     * Creates a new REST resource for managing {@code User} and {@code Idea} resources.
     *
     * @param req the HTTP request.
     * @param res the HTTP response.
     */
    public SearchRestResource(HttpServletRequest req, HttpServletResponse res) {
        super(req, res);
    }

    /**
     * Get Team Members of a specific Team.
     *
     * @throws IOException
     *             if any error occurs in the client/server communication.
     */
    public void searchUsers() throws IOException {
        String query = req.getParameter("q");
        ResultMessage message;

        List<User> usersFound = null;

        try {
            int page = req.getParameter("page") != null ? Integer.parseInt(req.getParameter("page")) : 0;

            if (query == null || query.isBlank()) {
                message = new ResultMessage(ErrorCode.QUERY_MISSING);
            } else {
                message = new ResultMessage(InfoMessage.USER_LISTED);

                usersFound = UserDAO.searchUsers(query, page);
            }
        } catch (SQLException | NamingException ex) {
            message = new ResultMessage(ErrorCode.CANNOT_ACCESS_DATABASE, ex.getMessage());
        } catch (NumberFormatException ex) {
            message = new ResultMessage(ErrorCode.BAD_INPUT, ex.getMessage());
        }

        writeResult(message, usersFound);

    }

    /**
     * Search ideas by query.
     *
     * @throws IOException
     *             if any error occurs in the client/server communication.
     */
    public void searchIdeas() throws IOException {
        String query = req.getParameter("q");
        ResultMessage message;

        List<Idea> ideasFound = null;

        try {
            int page = req.getParameter("page") != null ? Integer.parseInt(req.getParameter("page")) : 0;

            if (query == null || query.isBlank()) {
                message = new ResultMessage(ErrorCode.QUERY_MISSING);
            } else {
                message = new ResultMessage(InfoMessage.IDEA_LISTED);

                ideasFound = IdeaDAO.searchIdeas(query, page);

                User loggedUser = getLoggedUser();
                for (Idea idea : ideasFound) {
                    boolean liked = loggedUser != null && IdeaDAO.ideaLiked(idea, loggedUser);
                    idea.setLiked(liked);
                }
            }
        } catch (SQLException | NamingException ex) {
            message = new ResultMessage(ErrorCode.CANNOT_ACCESS_DATABASE, ex.getMessage());
        } catch (NumberFormatException ex) {
            message = new ResultMessage(ErrorCode.BAD_INPUT, ex.getMessage());
        }

        writeResult(message, ideasFound);
    }

}
