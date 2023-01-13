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
import it.unipd.dei.hyperu.database.*;
import it.unipd.dei.hyperu.resource.*;
import it.unipd.dei.hyperu.utils.ErrorCode;
import it.unipd.dei.hyperu.utils.InfoMessage;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.naming.NamingException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.SQLException;
import java.util.List;

/**
 * Manages the REST API for the {@link Team} resource.
 *
 * @author Marco Alecci (marco.alecci@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public class TeamRestResource extends AbstractRestResource {
    /**
     * Creates a new REST resource for managing {@code Team} resources.
     *
     * @param req the HTTP request.
     * @param res the HTTP response.
     */
    public TeamRestResource(HttpServletRequest req, HttpServletResponse res) {
        super(req, res);
    }

    /**
     * Create a new Team.
     *
     * @throws IOException
     *             if any error occurs in the client/server communication.
     */
    public void createTeam() throws IOException {
        String[] tokens = getTokens("idea");
        ResultMessage resultMessage;

        int teamID = -1;

        try {
            int ideaID = Integer.parseInt(tokens[0]);
            Team team = getJSON(Team.class);

            if (team.getName() == null || team.getName().isBlank()) {
                resultMessage = new ResultMessage(ErrorCode.TEAM_NAME_MISSING);
            } else {

                teamID = TeamDAO.createTeam(new Team(teamID, team.getName().trim(), null, new Idea(ideaID), team.canAcceptRequests(), null,
                        team.getDescription() != null ? team.getDescription().trim() : null));
                if (teamID > 0) {
                    resultMessage = new ResultMessage(InfoMessage.TEAM_CREATED);
                } else {
                    resultMessage = new ResultMessage(ErrorCode.NO_ELEMENT_ADDED);
                }
            }
        } catch (SQLException | NamingException ex) {
            resultMessage = new ResultMessage(ErrorCode.CANNOT_ACCESS_DATABASE, ex.getMessage());
        } catch (NumberFormatException | IndexOutOfBoundsException | JsonParseException | NullPointerException ex) {
            resultMessage = new ResultMessage(ErrorCode.BAD_INPUT, ex.getMessage());
        }

        writeResult(resultMessage, teamID);
    }

    /**
     * Update team information.
     *
     * @throws IOException
     *             if any error occurs in the client/server communication.
     */
    public void updateTeam() throws IOException {
        String[] tokens = getTokens("team");
        ResultMessage resultMessage;

        try {
            int teamID = Integer.parseInt(tokens[0]);
            Team team = getJSON(Team.class);

            if (team.getName() == null || team.getName().isBlank()) {
                resultMessage = new ResultMessage(ErrorCode.TEAM_NAME_MISSING);
            } else {
                boolean updated = TeamDAO.updateTeam(new Team(teamID, team.getName().trim(), null, null, team.canAcceptRequests(), null, team.getDescription().trim()));

                if (updated) {
                    resultMessage = new ResultMessage(InfoMessage.TEAM_UPDATED);
                } else {
                    resultMessage = new ResultMessage(ErrorCode.NO_CHANGES);
                }
            }
        } catch (SQLException | NamingException ex) {
            resultMessage = new ResultMessage(ErrorCode.CANNOT_ACCESS_DATABASE, ex.getMessage());
        } catch (NumberFormatException | IndexOutOfBoundsException | JsonParseException | NullPointerException ex) {
            resultMessage = new ResultMessage(ErrorCode.BAD_INPUT, ex.getMessage());
        }

        writeResult(resultMessage);
    }

    /**
     * Delete a Team.
     *
     * @throws IOException
     *             if any error occurs in the client/server communication.
     */
    public void deleteTeam() throws IOException {
        String[] tokens = getTokens("team");
        ResultMessage resultMessage;

        try {
            int teamID = Integer.parseInt(tokens[0]);

            boolean deleted = TeamDAO.deleteTeam(new Team(teamID));
            if (deleted) {
                resultMessage = new ResultMessage(InfoMessage.TEAM_DELETED);
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
     * Updated the image of the group chat
     *
     * @throws IOException
     *             if any error occurs in the client/server communication.
     */
    public void updateImageTeam(ServletConfig servletConfig) throws IOException {
        String[] tokens = getTokens("team");
        ResultMessage resultMessage = new ResultMessage(ErrorCode.NO_CHANGES);

        try {
            int teamID = Integer.parseInt(tokens[0]);

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

                        Team team = new Team(teamID);
                        team.setImage(image);

                        boolean updated = TeamDAO.updateImageTeam(team);
                        if (updated) {
                            resultMessage = new ResultMessage(InfoMessage.TEAM_IMAGE_UPDATED);
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
        } catch (NumberFormatException | IndexOutOfBoundsException | NullPointerException ex) {
            resultMessage = new ResultMessage(ErrorCode.BAD_INPUT, ex.getMessage());
        } catch (FileUploadException ex) {
            resultMessage = new ResultMessage(ErrorCode.CANNOT_UPLOAD_FILE, ex.getMessage());
        }

        writeResult(resultMessage);
    }

    /**
     * Updated the image of the group chat
     *
     * @throws IOException
     *             if any error occurs in the client/server communication.
     */
    public void removeImageTeam() throws IOException {
        String[] tokens = getTokens("team");
        ResultMessage resultMessage;

        try {
            int teamID = Integer.parseInt(tokens[0]);

            boolean updated = TeamDAO.removeImageTeam(new Team(teamID));
            if (updated) {
                resultMessage = new ResultMessage(InfoMessage.TEAM_IMAGE_REMOVED);
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
     * Get all team inforamtions.
     *
     * @throws IOException
     *             if any error occurs in the client/server communication.
     */
    public void getTeam() throws IOException {
        String[] tokens = getTokens("team");
        ResultMessage resultMessage;

        Team teamInfo = null;

        try {
            int teamID = Integer.parseInt(tokens[0]);

            teamInfo = TeamDAO.getTeamByID(teamID);

            if (teamInfo != null) {
                resultMessage = new ResultMessage(InfoMessage.TEAM_INFO_RETRIEVED);
            } else {
                resultMessage = new ResultMessage(ErrorCode.NO_TEAM_FOUND);
            }
        } catch (SQLException | NamingException ex) {
            resultMessage = new ResultMessage(ErrorCode.CANNOT_ACCESS_DATABASE, ex.getMessage());
        } catch (NumberFormatException | IndexOutOfBoundsException ex) {
            resultMessage = new ResultMessage(ErrorCode.BAD_INPUT, ex.getMessage());
        }

        writeResult(resultMessage, teamInfo);
    }

    /**
     * Get Team Members of a specific Team.
     *
     * @throws IOException
     *             if any error occurs in the client/server communication.
     */
    public void listGroupMembers() throws IOException {
        String[] tokens = getTokens("team");
        ResultMessage resultMessage;

        List<User> listGroupMembers = null;

        try {
            int teamID = Integer.parseInt(tokens[0]);

            listGroupMembers = TeamDAO.listGroupMembers(new Team(teamID));

            resultMessage = new ResultMessage(InfoMessage.TEAM_MEMBERS_LISTED);

        } catch (SQLException | NamingException ex) {
            resultMessage = new ResultMessage(ErrorCode.CANNOT_ACCESS_DATABASE, ex.getMessage());
        } catch (NumberFormatException | IndexOutOfBoundsException ex) {
            resultMessage = new ResultMessage(ErrorCode.BAD_INPUT, ex.getMessage());
        }

        writeResult(resultMessage, listGroupMembers);
    }

    /**
     * List the join request of the team.
     *
     * @throws IOException
     *             if any error occurs in the client/server communication.
     */
    public void listTeamRequests() throws IOException {
        String[] tokens = getTokens("team");
        ResultMessage resultMessage;

        List<JoinRequest> listRequests = null;

        try {
            int teamID = Integer.parseInt(tokens[0]);

            listRequests = TeamDAO.listRequestsOfTeam(new Team(teamID));

            resultMessage = new ResultMessage(InfoMessage.JOIN_REQUEST_LISTED);

        } catch (SQLException | NamingException ex) {
            resultMessage = new ResultMessage(ErrorCode.CANNOT_ACCESS_DATABASE, ex.getMessage());
        } catch (NumberFormatException | IndexOutOfBoundsException ex) {
            resultMessage = new ResultMessage(ErrorCode.BAD_INPUT, ex.getMessage());
        }

        writeResult(resultMessage, listRequests);
    }

    /**
     * Remove a member from a group
     *
     * @throws IOException
     *             if any error occurs in the client/server communication.
     */
    public void removeFromGroup() throws IOException {
        String[] tokens = getTokens("team");
        ResultMessage resultMessage;

        try {
            int teamID = Integer.parseInt(tokens[0]);
            int userID = Integer.parseInt(tokens[2]);

            boolean deleted = JoinRequestDAO.deleteJoinRequest(new JoinRequest(new Team(teamID), new User(userID), null, null, null));

            if (deleted) {
                resultMessage = new ResultMessage(InfoMessage.TEAM_MEMBER_REMOVED);
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
