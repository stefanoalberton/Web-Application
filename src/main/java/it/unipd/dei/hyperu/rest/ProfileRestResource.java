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
import it.unipd.dei.hyperu.resource.*;
import it.unipd.dei.hyperu.utils.ErrorCode;
import it.unipd.dei.hyperu.utils.InfoMessage;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Manages the REST API for the {@link Comment} resource.
 *
 * @author Marco Alecci (marco.alecci@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */


public class ProfileRestResource extends AbstractRestResource {
    /**
     * Creates a new REST resource for managing {@code Comment} resources.
     *
     * @param req the HTTP request.
     * @param res the HTTP response.
     */
    public ProfileRestResource(HttpServletRequest req, HttpServletResponse res) {
        super(req, res);
    }

    /**
     * Return personal information about a specific User.
     *
     * @throws IOException if any error occurs in the client/server communication.
     */
    public void getUserProfile() throws IOException {
        String[] tokens = getTokens("profile");

        try {
            getUserProfile(Integer.parseInt(tokens[0]));
        } catch (NumberFormatException | IndexOutOfBoundsException ex) {
            writeResult(new ResultMessage(ErrorCode.BAD_INPUT, ex.getMessage()));
        }
    }

    /**
     * Get user profile by its username.
     *
     * @throws IOException if any error occurs in the client/server communication.
     */
    public void getUserProfileByUsername() throws IOException {
        String[] tokens = getTokens("profile");

        try {
            getUserProfile(tokens[0]);
        } catch (NumberFormatException | IndexOutOfBoundsException ex) {
            writeResult(new ResultMessage(ErrorCode.BAD_INPUT, ex.getMessage()));
        }
    }

    /**
     * Get the profile of the current user
     *
     * @throws IOException if any error occurs in the client/server communication.
     */
    public void getMyProfile() throws IOException {
        User loggedUser = getLoggedUser();
        getUserProfile(loggedUser.getID());
    }

    /**
     * Get user profile by its ID
     *
     * @param userID the ID of the user
     * @throws IOException if any error occurs in the client/server communication.
     */
    private void getUserProfile(int userID) throws IOException {
        User user = null;
        ResultMessage resultMessage;

        try {
            user = UserDAO.getUserByID(userID);

            if (user != null) {
                Profile profile = UserDAO.getUserProfile(user);
                List<Skill> userSkills = UserDAO.listUserSkills(user);
                List<Topic> userTopics = UserDAO.listUserTopics(user);
                int totalLikes = UserDAO.getTotalLikesOfUser(user);
                int totalIdeas = UserDAO.getNumberIdeasOfUser(user);

                profile.setSkills(userSkills);
                profile.setTopics(userTopics);
                profile.setTotalLikes(totalLikes);
                profile.setTotalIdeas(totalIdeas);

                user.setProfile(profile);

                resultMessage = new ResultMessage(InfoMessage.USER_INFO_RETRIEVED);
            } else {
                resultMessage = new ResultMessage(ErrorCode.NO_USER_FOUND);
            }
        } catch (SQLException | NamingException ex) {
            resultMessage = new ResultMessage(ErrorCode.CANNOT_ACCESS_DATABASE, ex.getMessage());
        }

        writeResult(resultMessage, user);
    }

    /**
     * Get user profile by its username
     *
     * @param username the username of the user
     * @throws IOException if any error occurs in the client/server communication.
     */
    private void getUserProfile(String username) throws IOException {
        User user = null;
        ResultMessage resultMessage;

        try {
            user = UserDAO.getUserByUsername(username);

            if (user != null) {
                Profile profile = UserDAO.getUserProfile(user);
                List<Skill> userSkills = UserDAO.listUserSkills(user);
                List<Topic> userTopics = UserDAO.listUserTopics(user);
                int totalLikes = UserDAO.getTotalLikesOfUser(user);
                int totalIdeas = UserDAO.getNumberIdeasOfUser(user);

                profile.setSkills(userSkills);
                profile.setTopics(userTopics);
                profile.setTotalLikes(totalLikes);
                profile.setTotalIdeas(totalIdeas);

                user.setProfile(profile);

                resultMessage = new ResultMessage(InfoMessage.USER_INFO_RETRIEVED);
            } else {
                resultMessage = new ResultMessage(ErrorCode.NO_USER_FOUND);
            }
        } catch (SQLException | NamingException ex) {
            resultMessage = new ResultMessage(ErrorCode.CANNOT_ACCESS_DATABASE, ex.getMessage());
        }

        writeResult(resultMessage, user);
    }

    /**
     * List the idea where the user is part of.
     *
     * @throws IOException if any error occurs in the client/server communication.
     */
    public void listUserIdeas() throws IOException {
        String[] tokens = getTokens("profile");

        try {
            int userID = Integer.parseInt(tokens[0]);
            listUserIdeasByID(new User(userID));
        } catch (NumberFormatException | IndexOutOfBoundsException ex) {
            writeResult(new ResultMessage(ErrorCode.BAD_INPUT, ex.getMessage()));
        }
    }

    /**
     * List the ideas of the current user
     *
     * @throws IOException if any error occurs in the client/server communication.
     */
    public void listMyIdeas() throws IOException {
        User loggedUser = getLoggedUser();
        listUserIdeasByID(loggedUser);
    }

    /**
     * List the idea where the user is part of.
     *
     * @param user the ID of the user
     * @throws IOException if any error occurs in the client/server communication.
     */
    private void listUserIdeasByID(User user) throws IOException {
        List<Idea> listIdeas = null;
        ResultMessage resultMessage;

        try {
            int page = req.getParameter("page") != null ? Integer.parseInt(req.getParameter("page")) : 0;

            listIdeas = UserDAO.listUserCreatedIdeas(user, page);

            User loggedUser = getLoggedUser();
            for (Idea idea : listIdeas) {
                boolean liked = loggedUser != null && IdeaDAO.ideaLiked(idea, loggedUser);
                idea.setLiked(liked);
            }

            resultMessage = new ResultMessage(InfoMessage.IDEA_LISTED);
        } catch (SQLException | NamingException ex) {
            resultMessage = new ResultMessage(ErrorCode.CANNOT_ACCESS_DATABASE, ex.getMessage());
        } catch (NumberFormatException ex) {
            resultMessage = new ResultMessage(ErrorCode.BAD_INPUT, ex.getMessage());
        }

        writeResult(resultMessage, listIdeas);
    }
}
