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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import it.unipd.dei.hyperu.database.IdeaDAO;
import it.unipd.dei.hyperu.database.UserDAO;
import it.unipd.dei.hyperu.resource.*;
import it.unipd.dei.hyperu.utils.ErrorCode;
import it.unipd.dei.hyperu.utils.InfoMessage;
import it.unipd.dei.hyperu.utils.ServletFunctions;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.naming.NamingException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.*;

import static java.util.Calendar.*;

/**
 * Manages the REST API for the {@link User} resource.
 *
 * @author Marco Alecci (marco.alecci@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public class UserRestResource extends AbstractRestResource {
    /**
     * Creates a new REST resource for managing {@code User} resources.
     *
     * @param req the HTTP request.
     * @param res the HTTP response.
     */
    public UserRestResource(HttpServletRequest req, HttpServletResponse res) {
        super(req, res);
    }

    /**
     * Make the login for the current user
     *
     * @throws IOException if any error occurs in the client/server communication.
     */
    public void loginUser() throws IOException {
        User loggedUser = null;
        ResultMessage message;
        UUID token = null;

        try {
            String username = req.getParameter("username");
            String password = req.getParameter("password");

            if (username == null || username.isBlank()) {
                message = new ResultMessage(ErrorCode.USERNAME_MISSING);
            } else if (password == null || password.isBlank()) {
                message = new ResultMessage(ErrorCode.PASSWORD_MISSING);
            } else {
                int authenticatedUser = UserDAO.authenticateUser(username.trim(), password);

                if (authenticatedUser > -1) {
                    loggedUser = UserDAO.getUserByID(authenticatedUser);
                    if (req.getParameter("token") != null && Boolean.parseBoolean(req.getParameter("token"))) {
                        token = UserDAO.createToken(loggedUser);
                    }

                    setLoggedUser(loggedUser);

                    message = new ResultMessage(InfoMessage.LOGGED_IN);
                } else {
                    message = new ResultMessage(ErrorCode.WRONG_CREDENTIALS);
                }
            }
        } catch (SQLException | NamingException ex) {
            message = new ResultMessage(ErrorCode.CANNOT_ACCESS_DATABASE, ex.getMessage());
        }

        Gson gson = new GsonBuilder().create();
        JsonObject jsonObject = new JsonObject();

        jsonObject.add("user", gson.toJsonTree(loggedUser));
        jsonObject.add("token", gson.toJsonTree(token));
        if (loggedUser != null) {
            jsonObject.add("jwtoken", gson.toJsonTree(ServletFunctions.getJWTUser(loggedUser)));
        }

        writeResult(message, jsonObject);
    }

    /**
     * generate the token for the user.
     *
     * @throws IOException if any error occurs in the client/server communication.
     */
    public void loginUserToken() throws IOException {
        User loggedUser = null;
        ResultMessage message;
        UUID token = null;

        try {
            int loginID = Integer.parseInt(req.getParameter("id"));
            UUID loginToken = UUID.fromString(req.getParameter("token"));

            int authenticatedUser = UserDAO.authenticateToken(loginID, loginToken);

            if (authenticatedUser > -1) {
                loggedUser = UserDAO.getUserByID(authenticatedUser);
                token = UserDAO.createToken(loggedUser);

                setLoggedUser(loggedUser);

                message = new ResultMessage(InfoMessage.LOGGED_IN);
            } else {
                message = new ResultMessage(ErrorCode.WRONG_CREDENTIALS);
            }
        } catch (SQLException | NamingException ex) {
            message = new ResultMessage(ErrorCode.CANNOT_ACCESS_DATABASE, ex.getMessage());
        } catch (NumberFormatException ex) {
            message = new ResultMessage(ErrorCode.BAD_INPUT, ex.getMessage());
        }

        Gson gson = new GsonBuilder().create();
        JsonObject jsonObject = new JsonObject();

        jsonObject.add("user", gson.toJsonTree(loggedUser));
        jsonObject.add("token", gson.toJsonTree(token));
        if (loggedUser != null) {
            jsonObject.add("jwtoken", gson.toJsonTree(ServletFunctions.getJWTUser(loggedUser)));
        }

        writeResult(message, jsonObject);
    }

    /**
     * Get the basic information of the logged user
     *
     * @throws IOException if any error occurs in the client/server communication.
     */
    public void getBasicLoggedUser() throws IOException {
        User loggedUser;
        ResultMessage resultMessage;

        loggedUser = getLoggedUser();

        if (loggedUser != null) {
            resultMessage = new ResultMessage(InfoMessage.USER_INFO_RETRIEVED);
        } else {
            resultMessage = new ResultMessage(ErrorCode.NO_USER_FOUND);
        }

        writeResult(resultMessage, loggedUser);
    }

    /**
     * Make the logout for the current user
     *
     * @throws IOException if any error occurs in the client/server communication.
     */
    public void logoutUser() throws IOException {
        ResultMessage message;

        HttpSession session = req.getSession();
        session.invalidate();

        message = new ResultMessage(InfoMessage.LOGGED_OUT);

        writeResult(message);
    }

    /**
     * register a new user
     *
     * @throws IOException if any error occurs in the client/server communication.
     */
    public void registerUser() throws IOException {
        ResultMessage resultMessage;

        int userID = -1;

        try {
            UserRegister user = getJSON(UserRegister.class);
            Profile profile = user.getProfile();

            if (user.getUsername() == null || user.getUsername().isBlank()) {
                resultMessage = new ResultMessage(ErrorCode.USERNAME_MISSING);
            } else if (user.getEmail() == null || user.getEmail().isBlank()) {
                resultMessage = new ResultMessage(ErrorCode.EMAIL_MISSING);
            } else if (user.getPassword() == null || user.getPassword().isBlank()) {
                resultMessage = new ResultMessage(ErrorCode.PASSWORD_MISSING);
            } else if (user.getPasswordCheck() == null || user.getPasswordCheck().isBlank()) {
                resultMessage = new ResultMessage(ErrorCode.PASSWORD_CHECK_MISSING);
            } else if (!user.getPassword().equals(user.getPasswordCheck())) {
                resultMessage = new ResultMessage(ErrorCode.DIFFERENT_PASSWORDS);
            } else if (profile.getName() == null || profile.getName().isBlank()) {
                resultMessage = new ResultMessage(ErrorCode.NAME_MISSING);
            } else if (profile.getSurname() == null || profile.getSurname().isBlank()) {
                resultMessage = new ResultMessage(ErrorCode.SURNAME_MISSING);
            } else if (profile.getBirthDate() == null) {
                resultMessage = new ResultMessage(ErrorCode.BIRTHDATE_MISSING);
            } else if (getDiffYears(profile.getBirthDate(), new Date(System.currentTimeMillis())) < 16) {
                resultMessage = new ResultMessage(ErrorCode.AGE_NOT_ACCEPTABLE);
            } else if (profile.getGender() == null) {
                resultMessage = new ResultMessage(ErrorCode.GENDER_MISSING);
            } else {
                Profile insertProfile = new Profile(profile.getName().trim(), profile.getSurname().trim(), profile.getBirthDate(), profile.getGender(),
                        profile.getBiography() != null ? profile.getBiography().trim() : null);

                insertProfile.setTopics(profile.getTopics());
                insertProfile.setSkills(profile.getSkills());

                User insertUser = new User(userID, user.getUsername().trim(), user.getEmail().trim(), null, false, insertProfile);

                userID = UserDAO.registerUser(insertUser, user.getPassword());

                if (userID > 0) {
                    resultMessage = new ResultMessage(InfoMessage.USER_REGISTERED);
                } else {
                    resultMessage = new ResultMessage(ErrorCode.NO_ELEMENT_ADDED);
                }
            }
        } catch (SQLException | NamingException ex) {
            if (ex instanceof SQLException && ((SQLException) ex).getSQLState().equals(DUPLICATE_ERROR_CODE)) {
                resultMessage = new ResultMessage(ErrorCode.DUPLICATE_USER, ex.getMessage());
            } else {
                resultMessage = new ResultMessage(ErrorCode.CANNOT_ACCESS_DATABASE, ex.getMessage());
            }
        } catch (NumberFormatException | IndexOutOfBoundsException | JsonParseException | NullPointerException ex) {
            resultMessage = new ResultMessage(ErrorCode.BAD_INPUT, ex.getMessage());
        }

        writeResult(resultMessage, userID);
    }

    /**
     * Get the difference of years
     *
     * @param first the first date
     * @param last  the last date
     */
    private static int getDiffYears(Date first, Date last) {
        Calendar a = getCalendar(first);
        Calendar b = getCalendar(last);
        int diff = b.get(YEAR) - a.get(YEAR);
        if (a.get(MONTH) > b.get(MONTH) ||
                (a.get(MONTH) == b.get(MONTH) && a.get(DATE) > b.get(DATE))) {
            diff--;
        }
        return diff;
    }

    /**
     * get the date in the calendar
     *
     * @param date the date
     */
    private static Calendar getCalendar(Date date) {
        Calendar cal = Calendar.getInstance(Locale.ITALY);
        cal.setTime(date);
        return cal;
    }

    /**
     * update the password of a user.
     *
     * @throws IOException if any error occurs in the client/server communication.
     */
    public void updateUserPassword() throws IOException {
        ResultMessage resultMessage;

        try {
            String password = req.getParameter("password");
            String passwordCheck = req.getParameter("password_check");
            String oldPassword = req.getParameter("old_password");

            if (oldPassword == null || oldPassword.isBlank()) {
                resultMessage = new ResultMessage(ErrorCode.OLD_PASSWORD_MISSING);
            } else if (password == null || password.isBlank()) {
                resultMessage = new ResultMessage(ErrorCode.PASSWORD_MISSING);
            } else if (passwordCheck == null || passwordCheck.isBlank()) {
                resultMessage = new ResultMessage(ErrorCode.PASSWORD_CHECK_MISSING);
            } else if (!password.equals(passwordCheck)) {
                resultMessage = new ResultMessage(ErrorCode.DIFFERENT_PASSWORDS);
            } else {
                User loggedUser = getLoggedUser();

                boolean updated = UserDAO.updatePassword(loggedUser, password, oldPassword);

                if (updated) {
                    resultMessage = new ResultMessage(InfoMessage.USER_PASSWORD_UPDATED);
                } else {
                    resultMessage = new ResultMessage(ErrorCode.OLD_PASSWORD_WRONG);
                }
            }
        } catch (SQLException | NamingException ex) {
            resultMessage = new ResultMessage(ErrorCode.CANNOT_ACCESS_DATABASE, ex.getMessage());
        } catch (NumberFormatException | IndexOutOfBoundsException ex) {
            resultMessage = new ResultMessage(ErrorCode.BAD_INPUT, ex.getMessage());
        }

        writeResult(resultMessage);
    }

    /**
     * updated the profile of the user.
     *
     * @throws IOException if any error occurs in the client/server communication.
     */
    public void updateUserProfile() throws IOException {
        ResultMessage resultMessage;

        try {
            User user = getJSON(User.class);
            Profile profile = user.getProfile();

            if (user.getUsername() == null || user.getUsername().isBlank()) {
                resultMessage = new ResultMessage(ErrorCode.USERNAME_MISSING);
            } else if (user.getEmail() == null || user.getEmail().isBlank()) {
                resultMessage = new ResultMessage(ErrorCode.EMAIL_MISSING);
            } else if (profile.getName() == null || profile.getName().isBlank()) {
                resultMessage = new ResultMessage(ErrorCode.NAME_MISSING);
            } else if (profile.getSurname() == null || profile.getSurname().isBlank()) {
                resultMessage = new ResultMessage(ErrorCode.SURNAME_MISSING);
            } else if (profile.getBirthDate() == null) {
                resultMessage = new ResultMessage(ErrorCode.BIRTHDATE_MISSING);
            } else if (getDiffYears(profile.getBirthDate(), new Date(System.currentTimeMillis())) < 16) {
                resultMessage = new ResultMessage(ErrorCode.AGE_NOT_ACCEPTABLE);
            } else if (profile.getGender() == null) {
                resultMessage = new ResultMessage(ErrorCode.GENDER_MISSING);
            } else {
                User loggedUser = getLoggedUser();

                Profile updateProfile = new Profile(profile.getName().trim(), profile.getSurname().trim(), profile.getBirthDate(), profile.getGender(),
                        profile.getBiography() != null ? profile.getBiography().trim() : null);
                User updateUser = new User(loggedUser.getID(), user.getUsername().trim(), user.getEmail().trim(), null, false, updateProfile);

                boolean updated = UserDAO.updateUserProfile(updateUser);

                if (updated) {
                    resultMessage = new ResultMessage(InfoMessage.USER_UPDATED);
                } else {
                    resultMessage = new ResultMessage(ErrorCode.NO_CHANGES);
                }
            }
        } catch (SQLException | NamingException ex) {
            if (ex instanceof SQLException && ((SQLException) ex).getSQLState().equals(DUPLICATE_ERROR_CODE)) {
                resultMessage = new ResultMessage(ErrorCode.DUPLICATE_USER, ex.getMessage());
            } else {
                resultMessage = new ResultMessage(ErrorCode.CANNOT_ACCESS_DATABASE, ex.getMessage());
            }
        } catch (NumberFormatException | IndexOutOfBoundsException | JsonParseException | NullPointerException ex) {
            resultMessage = new ResultMessage(ErrorCode.BAD_INPUT, ex.getMessage());
        }

        writeResult(resultMessage);
    }

    /**
     * updated a profile picture of the user
     *
     * @throws IOException if any error occurs in the client/server communication.
     */
    public void updateProfilePictureUser(ServletConfig servletConfig) throws IOException {
        ResultMessage resultMessage = new ResultMessage(ErrorCode.NO_CHANGES);

        try {
            DiskFileItemFactory factory = new DiskFileItemFactory();

            ServletContext servletContext = servletConfig.getServletContext();
            File repository = (File) servletContext.getAttribute("javax.servlet.context.tempdir");
            factory.setRepository(repository);

            ServletFileUpload upload = new ServletFileUpload(factory);

            List<FileItem> items = upload.parseRequest(req);

            for (FileItem item : items) {
                if (!item.isFormField() && item.getSize() <= MAX_IMAGE_UPLOAD) {
                    if (IMAGE_SUPPORTED_TYPES.contains(item.getContentType().toLowerCase())) {
                        InputStream uploadedStream = item.getInputStream();
                        byte[] image = uploadedStream.readAllBytes();
                        uploadedStream.close();

                        User loggedUser = getLoggedUser();
                        User userToUpdate = new User(loggedUser.getID());
                        Profile profile = new Profile();
                        profile.setProfilePicture(image);
                        userToUpdate.setProfile(profile);

                        boolean updated = UserDAO.updateUserProfilePicture(userToUpdate);
                        if (updated) {
                            resultMessage = new ResultMessage(InfoMessage.USER_IMAGE_UPDATED);
                        } else {
                            resultMessage = new ResultMessage(ErrorCode.NO_CHANGES);
                        }
                        break;
                    } else {
                        resultMessage = new ResultMessage(ErrorCode.NOT_IMAGE);
                    }
                } else {
                    resultMessage = new ResultMessage(ErrorCode.IMAGE_TOO_LARGE);
                }
            }
        } catch (SQLException | NamingException ex) {
            resultMessage = new ResultMessage(ErrorCode.CANNOT_ACCESS_DATABASE, ex.getMessage());
        } catch (NumberFormatException | IndexOutOfBoundsException ex) {
            resultMessage = new ResultMessage(ErrorCode.BAD_INPUT, ex.getMessage());
        } catch (FileUploadException ex) {
            resultMessage = new ResultMessage(ErrorCode.CANNOT_UPLOAD_FILE, ex.getMessage());
        }

        writeResult(resultMessage);
    }

    /**
     * remove a profile picture of the user
     *
     * @throws IOException if any error occurs in the client/server communication.
     */
    public void removeProfilePictureUser() throws IOException {
        ResultMessage resultMessage;

        try {
            User loggedUser = getLoggedUser();

            boolean updated = UserDAO.removeUserProfilePicture(loggedUser);
            if (updated) {
                resultMessage = new ResultMessage(InfoMessage.USER_IMAGE_REMOVED);
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
     * create the feed for the user
     *
     * @throws IOException if any error occurs in the client/server communication.
     */
    public void listFeed() throws IOException {
        ResultMessage resultMessage;
        List<Idea> listIdeas = null;

        try {
            User loggedUser = getLoggedUser();
            int page = req.getParameter("page") != null ? Integer.parseInt(req.getParameter("page")) : 0;

            listIdeas = UserDAO.listFeed(loggedUser, page);

            for (Idea idea : listIdeas) {
                boolean liked = IdeaDAO.ideaLiked(idea, loggedUser);
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

    /**
     * list groups in which user is part
     *
     * @throws IOException if any error occurs in the client/server communication.
     */
    public void listUserTeams() throws IOException {
        ResultMessage resultMessage;
        List<Team> listTeams = null;

        try {
            User loggedUser = getLoggedUser();

            listTeams = UserDAO.listUserGroups(loggedUser);

            resultMessage = new ResultMessage(InfoMessage.TEAM_LISTED);
        } catch (SQLException | NamingException ex) {
            resultMessage = new ResultMessage(ErrorCode.CANNOT_ACCESS_DATABASE, ex.getMessage());
        }

        writeResult(resultMessage, listTeams);
    }

    /**
     * list the pending request for a specific user
     *
     * @throws IOException if any error occurs in the client/server communication.
     */
    public void listRequests() throws IOException {
        ResultMessage resultMessage;
        List<JoinRequest> joinRequests = null;

        try {
            User loggedUser = getLoggedUser();

            joinRequests = UserDAO.listPendingRequests(loggedUser);

            resultMessage = new ResultMessage(InfoMessage.JOIN_REQUEST_LISTED);
        } catch (SQLException | NamingException ex) {
            resultMessage = new ResultMessage(ErrorCode.CANNOT_ACCESS_DATABASE, ex.getMessage());
        } catch (NumberFormatException ex) {
            resultMessage = new ResultMessage(ErrorCode.BAD_INPUT, ex.getMessage());
        }

        writeResult(resultMessage, joinRequests);
    }

    /**
     * add a skill to the user
     *
     * @throws IOException if any error occurs in the client/server communication.
     */
    public void addSkillToUser() throws IOException {
        String[] tokens = getTokens("skill");
        ResultMessage resultMessage;

        try {
            int level = Integer.parseInt(req.getParameter("level"));
            User loggedUser = getLoggedUser();
            int skillID = Integer.parseInt(tokens[0]);

            boolean added = UserDAO.addSkillToUser(loggedUser, new Skill(skillID, null, null, level));
            if (added) {
                resultMessage = new ResultMessage(InfoMessage.USER_SKILL_ADDED);
            } else {
                resultMessage = new ResultMessage(ErrorCode.NO_ELEMENT_ADDED);
            }
        } catch (SQLException | NamingException ex) {
            if (ex instanceof SQLException && ((SQLException) ex).getSQLState().equals(DUPLICATE_ERROR_CODE)) {
                resultMessage = new ResultMessage(ErrorCode.DUPLICATE_SKILL_USER, ex.getMessage());
            } else {
                resultMessage = new ResultMessage(ErrorCode.CANNOT_ACCESS_DATABASE, ex.getMessage());
            }
        } catch (NumberFormatException | IndexOutOfBoundsException ex) {
            resultMessage = new ResultMessage(ErrorCode.BAD_INPUT, ex.getMessage());
        }

        writeResult(resultMessage);
    }

    /**
     * add a skill to the user
     *
     * @throws IOException if any error occurs in the client/server communication.
     */
    public void updateSkillOfUser() throws IOException {
        String[] tokens = getTokens("skill");
        ResultMessage resultMessage;

        try {
            int level = Integer.parseInt(req.getParameter("level"));
            User loggedUser = getLoggedUser();
            int skillID = Integer.parseInt(tokens[0]);

            boolean updated = UserDAO.updateSkillOfUser(loggedUser, new Skill(skillID, null, null, level));
            if (updated) {
                resultMessage = new ResultMessage(InfoMessage.USER_SKILL_UPDATED);
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
     * remove a skill from a user
     *
     * @throws IOException if any error occurs in the client/server communication.
     */
    public void removeSkillFromUser() throws IOException {
        String[] tokens = getTokens("skill");
        ResultMessage resultMessage;

        try {
            User loggedUser = getLoggedUser();
            int skillID = Integer.parseInt(tokens[0]);

            boolean updated = UserDAO.removeSkillFromUser(loggedUser, new Skill(skillID, null, null));
            if (updated) {
                resultMessage = new ResultMessage(InfoMessage.USER_SKILL_REMOVED);
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
     * Follow a topic by a specific user
     *
     * @throws IOException if any error occurs in the client/server communication.
     */
    public void addTopicToUser() throws IOException {
        String[] tokens = getTokens("topic");
        ResultMessage resultMessage;

        try {
            User loggedUser = getLoggedUser();
            int topicID = Integer.parseInt(tokens[0]);

            boolean added = UserDAO.addTopicToUser(loggedUser, new Topic(topicID, null, null));
            if (added) {
                resultMessage = new ResultMessage(InfoMessage.USER_TOPIC_ADDED);
            } else {
                resultMessage = new ResultMessage(ErrorCode.NO_ELEMENT_ADDED);
            }
        } catch (SQLException | NamingException ex) {
            if (ex instanceof SQLException && ((SQLException) ex).getSQLState().equals(DUPLICATE_ERROR_CODE)) {
                resultMessage = new ResultMessage(ErrorCode.DUPLICATE_TOPIC_USER, ex.getMessage());
            } else {
                resultMessage = new ResultMessage(ErrorCode.CANNOT_ACCESS_DATABASE, ex.getMessage());
            }
        } catch (NumberFormatException | IndexOutOfBoundsException ex) {
            resultMessage = new ResultMessage(ErrorCode.BAD_INPUT, ex.getMessage());
        }

        writeResult(resultMessage);
    }

    /**
     * Unfollow a topic bu a specific user.
     *
     * @throws IOException if any error occurs in the client/server communication.
     */
    public void removeTopicFromUser() throws IOException {
        String[] tokens = getTokens("topic");
        ResultMessage resultMessage;

        try {
            User loggedUser = getLoggedUser();
            int topicID = Integer.parseInt(tokens[0]);

            boolean updated = UserDAO.removeTopicFromUser(loggedUser, new Topic(topicID, null, null));
            if (updated) {
                resultMessage = new ResultMessage(InfoMessage.USER_TOPIC_REMOVED);
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
