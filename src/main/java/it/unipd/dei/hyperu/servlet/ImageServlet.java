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

package it.unipd.dei.hyperu.servlet;

import it.unipd.dei.hyperu.database.IdeaDAO;
import it.unipd.dei.hyperu.database.TeamDAO;
import it.unipd.dei.hyperu.database.UserDAO;
import it.unipd.dei.hyperu.resource.Idea;
import it.unipd.dei.hyperu.resource.Profile;
import it.unipd.dei.hyperu.resource.Team;
import it.unipd.dei.hyperu.utils.ImageGenerator;
import it.unipd.dei.hyperu.resource.ResultMessage;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;

import javax.naming.NamingException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.unipd.dei.hyperu.resource.User;
import it.unipd.dei.hyperu.utils.ErrorCode;

/**
 * Get images from idea and profiles.
 *
 * @author Luca Martinelli (luca.martinelli.1@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public final class ImageServlet extends AbstractServlet {

    /**
     * Get images from idea and profiles.
     *
     * @param req the HTTP request from the client.
     * @param res the HTTP response from the server.
     * @throws IOException if any error occurs in the client/server communication.
     */
    public void doGet(HttpServletRequest req, HttpServletResponse res)
            throws IOException {
        String[] tokens = getTokens(req, "image");

        String imageTable = tokens[0];

        switch (imageTable) {
            case "profile":
                showProfileImage(req, res, tokens);
                break;
            case "idea":
                showIdeaImage(req, res, tokens);
                break;
            case "team":
                showIdeaTeam(req, res, tokens);
                break;
            default:
                writeResult(res, new ResultMessage(ErrorCode.OPERATION_UNKNOWN));
                break;
        }
    }

    private void showProfileImage(HttpServletRequest req, HttpServletResponse res, String[] tokens) throws IOException {
        User user;
        ResultMessage resultMessage = null;

        try {
            int userID = Integer.parseInt(tokens[1]);
            // Get user by username
            user = UserDAO.getUserByID(userID);
            // there is a user with that username
            if (user != null) {
                Profile userProfile = UserDAO.getUserProfile(user, true);
                byte[] profilePicture = userProfile.getProfilePicture();

                if (profilePicture != null) {
                    showImage(res, profilePicture, user.getUsername());
                } else {
                    showImage(res, Files.readAllBytes(Paths.get(req.getSession().getServletContext().getRealPath("/") + "media/user.png")), user.getUsername());
                }
            } else {
                resultMessage = new ResultMessage(ErrorCode.NO_USER_FOUND);
            }
        } catch (SQLException | NamingException ex) {
            resultMessage = new ResultMessage(ErrorCode.CANNOT_ACCESS_DATABASE, ex.getMessage());
        } catch (NumberFormatException | IndexOutOfBoundsException ex) {
            resultMessage = new ResultMessage(ErrorCode.BAD_INPUT, ex.getMessage());
        }

        if (resultMessage != null && resultMessage.isError()) {
            writeResult(res, resultMessage);
        }
    }

    private void showIdeaImage(HttpServletRequest req, HttpServletResponse res, String[] tokens) throws IOException {
        Idea idea;
        ResultMessage resultMessage = null;

        try {
            int ideaID = Integer.parseInt(tokens[1]);
            // Get idea by ID
            idea = IdeaDAO.getIdeaByID(ideaID, true);
            // there is a idea with that ID
            if (idea != null) {
                byte[] ideaImage = idea.getImage();

                if (ideaImage != null) {
                    showImage(res, ideaImage, idea.getTitle());
                } else {
                    showImage(res, ImageGenerator.generateImageWithText(idea.getTitle()), idea.getTitle());
                }
            } else {
                resultMessage = new ResultMessage(ErrorCode.NO_IDEA_FOUND);
            }
        } catch (SQLException | NamingException ex) {
            resultMessage = new ResultMessage(ErrorCode.CANNOT_ACCESS_DATABASE, ex.getMessage());
        } catch (NumberFormatException | IndexOutOfBoundsException ex) {
            resultMessage = new ResultMessage(ErrorCode.BAD_INPUT, ex.getMessage());
        }

        if (resultMessage != null && resultMessage.isError()) {
            writeResult(res, resultMessage);
        }
    }

    private void showIdeaTeam(HttpServletRequest req, HttpServletResponse res, String[] tokens) throws IOException {
        Team team;
        ResultMessage resultMessage = null;

        try {
            int teamID = Integer.parseInt(tokens[1]);
            // Get idea by ID
            team = TeamDAO.getTeamByID(teamID, true);
            // there is a idea with that ID
            if (team != null) {
                byte[] teamImage = team.getImage();

                if (teamImage != null) {
                    showImage(res, teamImage, team.getName());
                } else {
                    byte[] ideaImage = team.getIdea() != null ? team.getIdea().getImage() : null;
                    if (ideaImage != null) {
                        showImage(res, ideaImage, team.getName());
                    } else {
                        showImage(res, ImageGenerator.generateImageWithText(team.getName()), team.getName());
                    }
                }
            } else {
                resultMessage = new ResultMessage(ErrorCode.NO_TEAM_FOUND);
            }
        } catch (SQLException | NamingException ex) {
            resultMessage = new ResultMessage(ErrorCode.CANNOT_ACCESS_DATABASE, ex.getMessage());
        } catch (NumberFormatException | IndexOutOfBoundsException ex) {
            resultMessage = new ResultMessage(ErrorCode.BAD_INPUT, ex.getMessage());
        }

        if (resultMessage != null && resultMessage.isError()) {
            writeResult(res, resultMessage);
        }
    }

    private void showImage(HttpServletResponse res, byte[] data, String filename) throws IOException {
        res.setContentType("image/jpeg");
        res.setHeader("Content-Disposition", "inline; filename=\"" + filename + ".jpg\"");
        res.setContentLength(data.length);

        ServletOutputStream outputStream = res.getOutputStream();
        outputStream.write(data);

        outputStream.flush();
        outputStream.close();
    }

}