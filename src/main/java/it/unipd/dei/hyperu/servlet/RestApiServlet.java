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

import it.unipd.dei.hyperu.rest.*;
import it.unipd.dei.hyperu.resource.ResultMessage;

import java.io.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.unipd.dei.hyperu.utils.ErrorCode;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.servlet.ServletRequestContext;

/**
 * Get images from idea and profiles.
 *
 * @author Luca Martinelli (luca.martinelli.1@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public final class RestApiServlet extends AbstractServlet {

    private String[] tokens;
    private String method;

    private static final String GET = "GET";
    private static final String POST = "POST";
    private static final String PUT = "PUT";
    private static final String DELETE = "DELETE";

    /**
     * Get images from idea and profiles.
     *
     * @param req the HTTP request from the client.
     * @param res the HTTP response from the server.
     * @throws IOException if any error occurs in the client/server communication.
     */
    public void service(HttpServletRequest req, HttpServletResponse res)
            throws IOException {
        tokens = getTokens(req, req.getContextPath() + "/api");
        method = req.getMethod();

        ResultMessage resultMessage = new ResultMessage(ErrorCode.OPERATION_UNKNOWN);

        if (!checkMethodMediaType(req, res)) {
            resultMessage = new ResultMessage(ErrorCode.BAD_INPUT);
            writeResult(res, resultMessage);
            return;
        }

        if (tokens != null && tokens.length > 0) {
            if (processAdmin(req, res)) return;
            if (processModerator(req, res)) return;
            if (processTopic(req, res)) return;
            if (processSkill(req, res)) return;
            if (processProfile(req, res)) return;
            if (processUser(req, res)) return;
            if (processTeam(req, res)) return;
            if (processMessage(req, res)) return;
            if (processIdea(req, res)) return;
            if (processComment(req, res)) return;
            if (processSearch(req, res)) return;
        }

        writeResult(res, resultMessage);
    }

    /**
     * Checks that the request method and MIME media type are allowed.
     *
     * @param req the HTTP request.
     * @param res the HTTP response.
     * @return {@code true} if the request method and the MIME type are allowed; {@code false} otherwise.
     */
    private boolean checkMethodMediaType(final HttpServletRequest req, final HttpServletResponse res) {
        final String accept = req.getHeader("Accept");
        return true;
        // return accept == null || accept.contains("application/json") || accept.contains("*/*");
    }

    public boolean processSearch(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String resource = tokens[0];

        if (!resource.equals("search")) return false;

        if (tokens.length == 2) {
            resource = tokens[1];
            switch (resource) {
                case "users":
                    new SearchRestResource(req, res).searchUsers();
                    break;
                case "ideas":
                    new SearchRestResource(req, res).searchIdeas();
                    break;
                default:
                    return false;
            }
        }

        return true;
    }

    public boolean processAdmin(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String resource = tokens[0];

        if (!resource.equals("admin")) return false;

        switch (tokens.length) {
            case 1:
                if (method.equals(GET)) {
                    new AdminRestResource(req, res).listAdministrators();
                } else {
                    return false;
                }
                break;
            case 3:
                resource = tokens[1];
                if (resource.equals("moderator")) {
                    switch (method) {
                        case POST:
                            try {
                                Integer.parseInt(tokens[2]);
                                new AdminRestResource(req, res).promoteToModerator();
                            } catch (NumberFormatException ignore) {
                                new AdminRestResource(req, res).promoteToModeratorByUsername();;
                            }
                        case DELETE:
                            new AdminRestResource(req, res).downgradeToUser();
                            break;
                        default:
                            return false;
                    }
                } else {
                    return false;
                }
                break;
            default:
                return false;
        }

        return true;
    }

    public boolean processModerator(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String resource = tokens[0];

        if (!resource.equals("moderator")) return false;

        switch (tokens.length) {
            case 1:
                if (GET.equals(method)) {
                    new ModeratorRestResource(req, res).listModerators();
                } else {
                    return false;
                }
                break;
            case 2:
                resource = tokens[1];
                switch (resource) {
                    case "ban":
                        if (GET.equals(method)) {
                            new ModeratorRestResource(req, res).listBannedUsers();
                        } else {
                            return false;
                        }
                        break;
                    case "skill":
                        if (POST.equals(method)) {
                            new SkillRestResource(req, res).createSkill();
                        } else {
                            return false;
                        }
                        break;
                    case "topic":
                        if (POST.equals(method)) {
                            new TopicRestResource(req, res).createTopic();
                        } else {
                            return false;
                        }
                        break;
                    default:
                        return false;
                }
            case 3:
                resource = tokens[1];
                switch (resource) {
                    case "ban":
                        if (PUT.equals(method)) {
                            try {
                                Integer.parseInt(tokens[2]);
                                new ModeratorRestResource(req, res).banUser();
                            } catch (NumberFormatException ignore) {
                                new ModeratorRestResource(req, res).banUserByUsername();;
                            }
                        } else {
                            return false;
                        }
                        break;
                    case "unban":
                        if (method.equals(PUT)) {
                            new ModeratorRestResource(req, res).readmitUser();
                        } else {
                            return false;
                        }
                        break;
                    case "skill":
                        switch (method) {
                            case PUT:
                                new SkillRestResource(req, res).updateSkill();
                                break;
                            case DELETE:
                                new SkillRestResource(req, res).deleteSkill();
                                break;
                            default:
                                return false;
                        }
                        break;
                    case "topic":
                        switch (method) {
                            case PUT:
                                new TopicRestResource(req, res).updateTopic();
                                break;
                            case DELETE:
                                new TopicRestResource(req, res).deleteTopic();
                                break;
                            default:
                                return false;
                        }
                        break;
                    default:
                        return false;
                }
                break;
            default:
                return false;
        }

        return true;
    }

    public boolean processSkill(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String resource = tokens[0];

        if (!resource.equals("skill")) return false;

        switch (tokens.length) {
            case 1:
                if (GET.equals(method)) {
                    new SkillRestResource(req, res).listAllSkills();
                } else {
                    return false;
                }
                break;
            case 2:
                if (GET.equals(method)) {
                    new IdeaRestResource(req, res).listIdeasBySkill();
                } else {
                    return false;
                }
                break;
            default:
                return false;
        }

        return true;
    }

    public boolean processTopic(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String resource = tokens[0];

        if (!resource.equals("topic")) return false;

        switch (tokens.length) {
            case 1:
                if (GET.equals(method)) {
                    new TopicRestResource(req, res).listAllTopics();
                } else {
                    return false;
                }
                break;
            case 2:
                if (GET.equals(method)) {
                    new IdeaRestResource(req, res).listIdeasByTopic();
                } else {
                    return false;
                }
                break;
            default:
                return false;
        }

        return true;
    }

    public boolean processProfile(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String resource = tokens[0];

        if (!resource.equals("profile")) return false;

        switch (tokens.length) {
            case 2:
                if (GET.equals(method)) {
                    try {
                        Integer.parseInt(tokens[1]);
                        new ProfileRestResource(req, res).getUserProfile();
                    } catch (NumberFormatException ignore) {
                        new ProfileRestResource(req, res).getUserProfileByUsername();
                    }
                } else {
                    return false;
                }
                break;
            case 3:
                if (GET.equals(method)) {
                    new ProfileRestResource(req, res).listUserIdeas();
                } else {
                    return false;
                }
                break;
            default:
                return false;
        }

        return true;
    }

    public boolean processUser(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String resource = tokens[0];

        if (!resource.equals("user")) return false;

        switch (tokens.length) {
            case 2:
                resource = tokens[1];
                switch (resource) {
                    case "login":
                        switch (method) {
                            case GET:
                                new UserRestResource(req, res).getBasicLoggedUser();
                                break;
                            case POST:
                                new UserRestResource(req, res).loginUser();
                                break;
                            default:
                                return false;
                        }
                        break;
                    case "token":
                        if (POST.equals(method)) {
                            new UserRestResource(req, res).loginUserToken();
                        } else {
                            return false;
                        }
                        break;
                    case "register":
                        if (POST.equals(method)) {
                            new UserRestResource(req, res).registerUser();
                        } else {
                            return false;
                        }
                        break;
                    case "logout":
                        if (GET.equals(method)) {
                            new UserRestResource(req, res).logoutUser();
                        } else {
                            return false;
                        }
                        break;
                    case "me":
                        switch (method) {
                            case GET:
                                new ProfileRestResource(req, res).getMyProfile();
                                break;
                            case PUT:
                                new UserRestResource(req, res).updateUserProfile();
                                break;
                            default:
                                return false;
                        }
                        break;
                    default:
                        return false;
                }
                break;
            case 3:
                resource = tokens[1];
                if (resource.equals("me")) {
                    resource = tokens[2];
                    switch (resource) {
                        case "password":
                            if (POST.equals(method)) {
                                new UserRestResource(req, res).updateUserPassword();
                            } else {
                                return false;
                            }
                            break;
                        case "image":
                            switch (method) {
                                case PUT:
                                    if (!ServletFileUpload.isMultipartContent(new ServletRequestContext(req)))
                                        return false;
                                    new UserRestResource(req, res).updateProfilePictureUser(getServletConfig());
                                    break;
                                case DELETE:
                                    new UserRestResource(req, res).removeProfilePictureUser();
                                    break;
                                default:
                                    return false;
                            }
                            break;
                        case "ideas":
                            if (GET.equals(method)) {
                                new ProfileRestResource(req, res).listMyIdeas();
                            } else {
                                return false;
                            }
                            break;
                        case "requests":
                            if (GET.equals(method)) {
                                new UserRestResource(req, res).listRequests();
                            } else {
                                return false;
                            }
                            break;
                        case "teams":
                            if (GET.equals(method)) {
                                new UserRestResource(req, res).listUserTeams();
                            } else {
                                return false;
                            }
                            break;
                        case "feed":
                            if (GET.equals(method)) {
                                new UserRestResource(req, res).listFeed();
                            } else {
                                return false;
                            }
                            break;
                        default:
                            return false;
                    }
                } else {
                    return false;
                }
                break;
            case 4:
                resource = tokens[1];
                if (resource.equals("me")) {
                    resource = tokens[2];
                    switch (resource) {
                        case "skill":
                            switch (method) {
                                case POST:
                                    new UserRestResource(req, res).addSkillToUser();
                                    break;
                                case PUT:
                                    new UserRestResource(req, res).updateSkillOfUser();
                                    break;
                                case DELETE:
                                    new UserRestResource(req, res).removeSkillFromUser();
                                    break;
                                default:
                                    return false;
                            }
                            break;
                        case "topic":
                            switch (method) {
                                case POST:
                                    new UserRestResource(req, res).addTopicToUser();
                                    break;
                                case DELETE:
                                    new UserRestResource(req, res).removeTopicFromUser();
                                    break;
                                default:
                                    return false;
                            }
                            break;
                        default:
                            return false;
                    }
                } else {
                    return false;
                }
                break;
            default:
                return false;
        }

        return true;
    }

    public boolean processTeam(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String resource = tokens[0];

        if (!resource.equals("team")) return false;

        switch (tokens.length) {
            case 2:
                switch (method) {
                    case GET:
                        new TeamRestResource(req, res).getTeam();
                        break;
                    case PUT:
                        new TeamRestResource(req, res).updateTeam();
                        break;
                    case DELETE:
                        new TeamRestResource(req, res).deleteTeam();
                        break;
                    default:
                        return false;
                }
                break;
            case 3:
                resource = tokens[2];
                switch (resource) {
                    case "image":
                        switch (method) {
                            case PUT:
                                if (!ServletFileUpload.isMultipartContent(new ServletRequestContext(req))) return false;
                                new TeamRestResource(req, res).updateImageTeam(getServletConfig());
                                break;
                            case DELETE:
                                new TeamRestResource(req, res).removeImageTeam();
                                break;
                            default:
                                return false;
                        }
                        break;
                    case "member":
                        if (GET.equals(method)) {
                            new TeamRestResource(req, res).listGroupMembers();
                        } else {
                            return false;
                        }
                        break;
                    case "message":
                        switch (method) {
                            case GET:
                                new MessageRestResource(req, res).listGroupMessages();
                                break;
                            case POST:
                                if (ServletFileUpload.isMultipartContent(new ServletRequestContext(req))) {
                                    new MessageRestResource(req, res).sendFileMessage(getServletConfig());
                                } else {
                                    new MessageRestResource(req, res).sendMessage();
                                }
                                break;
                            default:
                                return false;
                        }
                        break;
                    case "request":
                        switch (method) {
                            case GET:
                                new TeamRestResource(req, res).listTeamRequests();
                                break;
                            case POST:
                                new JoinRequestRestResource(req, res).sendJoinRequest();
                                break;
                            default:
                                return false;
                        }
                        break;
                    default:
                        return false;
                }
                break;
            case 4:
                resource = tokens[2];

                switch (resource) {
                    case "member":
                        if (DELETE.equals(method)) {
                            new TeamRestResource(req, res).removeFromGroup();
                        } else {
                            return false;
                        }
                        break;
                    case "request":
                        switch (method) {
                            case PUT:
                                new JoinRequestRestResource(req, res).acceptJoinRequest();
                                break;
                            case DELETE:
                                new JoinRequestRestResource(req, res).deleteJoinRequest();
                                break;
                            default:
                                return false;
                        }
                        break;
                    default:
                        return false;
                }
                break;
            default:
                return false;
        }

        return true;
    }

    public boolean processMessage(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String resource = tokens[0];

        if (!resource.equals("message")) return false;

        if (tokens.length == 2) {
            switch (method) {
                case PUT:
                    new MessageRestResource(req, res).updateMessage();
                    break;
                case DELETE:
                    new MessageRestResource(req, res).deleteMessage();
                    break;
                default:
                    return false;
            }
        } else {
            return false;
        }

        return true;
    }

    public boolean processIdea(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String resource = tokens[0];

        if (!resource.equals("idea")) return false;

        switch (tokens.length) {
            case 1:
                if (POST.equals(method)) {
                    new IdeaRestResource(req, res).createIdea();
                } else {
                    return false;
                }
                break;
            case 2:
                switch (method) {
                    case GET:
                        new IdeaRestResource(req, res).getIdea();
                        break;
                    case PUT:
                        new IdeaRestResource(req, res).updateIdea();
                        break;
                    case DELETE:
                        new IdeaRestResource(req, res).deleteIdea();
                        break;
                    default:
                        return false;
                }
                break;
            case 3:
                resource = tokens[2];
                switch (resource) {
                    case "image":
                        switch (method) {
                            case PUT:
                                if (!ServletFileUpload.isMultipartContent(new ServletRequestContext(req))) return false;
                                new IdeaRestResource(req, res).updateImageIdea(getServletConfig());
                                break;
                            case DELETE:
                                new IdeaRestResource(req, res).removeImageIdea();
                                break;
                            default:
                                return false;
                        }
                        break;
                    case "like":
                        switch (method) {
                            case POST:
                                new IdeaRestResource(req, res).likeIdea();
                                break;
                            case DELETE:
                                new IdeaRestResource(req, res).unlikeIdea();
                                break;
                            default:
                                return false;
                        }
                        break;
                    case "comment":
                        switch (method) {
                            case GET:
                                new CommentRestResource(req, res).listIdeaComments();
                                break;
                            case POST:
                                new CommentRestResource(req, res).sendComment();
                                break;
                            default:
                                return false;
                        }
                        break;
                    case "team":
                        if (POST.equals(method)) {
                            new TeamRestResource(req, res).createTeam();
                        } else {
                            return false;
                        }
                        break;
                    default:
                        return false;
                }
                break;
            case 4:
                resource = tokens[2];

                switch (resource) {
                    case "skill":
                        switch (method) {
                            case POST:
                                new IdeaRestResource(req, res).addSkillToIdea();
                                break;
                            case DELETE:
                                new IdeaRestResource(req, res).removeSkillFromIdea();
                                break;
                            default:
                                return false;
                        }
                        break;
                    case "topic":
                        switch (method) {
                            case POST:
                                new IdeaRestResource(req, res).addTopicToIdea();
                                break;
                            case DELETE:
                                new IdeaRestResource(req, res).removeTopicFromIdea();
                                break;
                            default:
                                return false;
                        }
                        break;
                    default:
                        return false;
                }
                break;
            default:
                return false;
        }

        return true;
    }

    public boolean processComment(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String resource = tokens[0];

        if (!resource.equals("comment")) return false;

        if (tokens.length == 2) {
            switch (method) {
                case PUT:
                    new CommentRestResource(req, res).updateComment();
                    break;
                case DELETE:
                    new CommentRestResource(req, res).deleteComment();
                    break;
                default:
                    return false;
            }
        } else {
            return false;
        }

        return true;
    }

}