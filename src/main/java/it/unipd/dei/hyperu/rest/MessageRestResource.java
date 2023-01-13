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
import it.unipd.dei.hyperu.database.MessageDAO;
import it.unipd.dei.hyperu.database.TeamDAO;
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
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;
/**
 * Manages the REST API for the {@link Message} resource.
 *
 * @author Marco Alecci (marco.alecci@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public class MessageRestResource extends AbstractRestResource {

    /**
     * Creates a new REST resource for managing {@code Message} resources.
     *
     * @param req the HTTP request.
     * @param res the HTTP response.
     */
    public MessageRestResource(HttpServletRequest req, HttpServletResponse res) {
        super(req, res);
    }

    /**
     * List all the messaged of the group
     *
     * @throws IOException
     *             if any error occurs in the client/server communication.
     */
    public void listGroupMessages() throws IOException {
        String[] tokens = getTokens("team");

        List<Message> groupMessages = null;
        ResultMessage resultMessage;

        try {
            int teamID = Integer.parseInt(tokens[0]);
            int page = req.getParameter("page") != null ? Integer.parseInt(req.getParameter("page")) : 0;

            groupMessages = TeamDAO.listGroupMessages(new Team(teamID), page);

            resultMessage = new ResultMessage(InfoMessage.MESSAGE_LISTED);
        } catch (SQLException | NamingException ex) {
            resultMessage = new ResultMessage(ErrorCode.CANNOT_ACCESS_DATABASE, ex.getMessage());
        } catch (NumberFormatException | IndexOutOfBoundsException ex) {
            resultMessage = new ResultMessage(ErrorCode.BAD_INPUT, ex.getMessage());
        }

        writeResult(resultMessage, groupMessages);
    }

    /**
     * Send a new message in the Team chat
     *
     * @throws IOException
     *             if any error occurs in the client/server communication.
     */
    public void sendMessage() throws IOException {
        String[] tokens = getTokens("team");
        ResultMessage resultMessage;

        int messageID = -1;

        try {
            Message message = getJSON(Message.class);

            if (message.getContent() == null || message.getContent().isBlank()) {
                resultMessage = new ResultMessage(ErrorCode.MESSAGE_CONTENT_MISSING);
            } else {
                int teamID = Integer.parseInt(tokens[0]);
                User loggedUser = getLoggedUser();

                messageID = MessageDAO.sendMessage(new Message(messageID, message.getContent().trim(), null, null, null, new Team(teamID), loggedUser));

                if (messageID > 0) {
                    resultMessage = new ResultMessage(InfoMessage.MESSAGE_SENT);
                } else {
                    resultMessage = new ResultMessage(ErrorCode.NO_ELEMENT_ADDED);
                }
            }
        } catch (SQLException | NamingException ex) {
            resultMessage = new ResultMessage(ErrorCode.CANNOT_ACCESS_DATABASE, ex.getMessage());
        } catch (NumberFormatException | IndexOutOfBoundsException | JsonParseException | NullPointerException ex) {
            resultMessage = new ResultMessage(ErrorCode.BAD_INPUT, ex.getMessage());
        }

        writeResult(resultMessage, messageID);
    }

    /**
     * Send a new file message in the Team chat
     *
     * @param servletConfig the servlet in use
     * @throws IOException
     *             if any error occurs in the client/server communication.
     */
    public void sendFileMessage(ServletConfig servletConfig) throws IOException {
        String[] tokens = getTokens("team");
        ResultMessage resultMessage = new ResultMessage(ErrorCode.NO_CHANGES);

        int messageID = -1;

        try {
            int teamID = Integer.parseInt(tokens[0]);

            DiskFileItemFactory factory = new DiskFileItemFactory();

            ServletContext servletContext = servletConfig.getServletContext();
            File repository = (File) servletContext.getAttribute("javax.servlet.context.tempdir");
            factory.setRepository(repository);

            ServletFileUpload upload = new ServletFileUpload(factory);

            List<FileItem> items = upload.parseRequest(req);

            for (FileItem item : items) {
                if (!item.isFormField() && item.getSize() <= MAX_FILE_UPLOAD) {
                    InputStream uploadedStream = item.getInputStream();
                    byte[] file = uploadedStream.readAllBytes();
                    uploadedStream.close();

                    FileInfo fileInfo = new FileInfo(item.getName(), item.getContentType(), item.getSize());

                    User loggedUser = getLoggedUser();
                    Message message = new Message(messageID, null, file, fileInfo, null, new Team(teamID), loggedUser);

                    messageID = MessageDAO.sendMessage(message);
                    if (messageID > 0) {
                        resultMessage = new ResultMessage(InfoMessage.MESSAGE_SENT);
                    } else {
                        resultMessage = new ResultMessage(ErrorCode.NO_ELEMENT_ADDED);
                    }
                    break;
                } else {
                    resultMessage = new ResultMessage(ErrorCode.FILE_TOO_LARGE);
                }
            }
        } catch (SQLException | NamingException ex) {
            resultMessage = new ResultMessage(ErrorCode.CANNOT_ACCESS_DATABASE, ex.getMessage());
        } catch (NumberFormatException | IndexOutOfBoundsException | NullPointerException ex) {
            resultMessage = new ResultMessage(ErrorCode.BAD_INPUT, ex.getMessage());
        } catch (FileUploadException ex) {
            resultMessage = new ResultMessage(ErrorCode.CANNOT_UPLOAD_FILE, ex.getMessage());
        }

        writeResult(resultMessage, messageID);
    }

    /**
     * update a message in the Team chat
     *
     * @throws IOException
     *             if any error occurs in the client/server communication.
     */
    public void updateMessage() throws IOException {
        String[] tokens = getTokens("message");

        ResultMessage resultMessage;

        try {
            Message message = getJSON(Message.class);

            if (message.getContent() == null || message.getContent().isBlank()) {
                resultMessage = new ResultMessage(ErrorCode.MESSAGE_CONTENT_MISSING);
            } else {
                int messageID = Integer.parseInt(tokens[0]);
                boolean updated = MessageDAO.updateMessage(new Message(messageID, message.getContent().trim(), null, null, null, null, null));

                if (updated) {
                    resultMessage = new ResultMessage(InfoMessage.MESSAGE_UPDATED);
                } else {
                    resultMessage = new ResultMessage(ErrorCode.NO_CHANGES);
                }
            }
        } catch (SQLException | NamingException ex) {
            resultMessage = new ResultMessage(ErrorCode.CANNOT_ACCESS_DATABASE, ex.getMessage());
        } catch (NumberFormatException | IndexOutOfBoundsException | JsonParseException | NullPointerException ex) {
            resultMessage = new ResultMessage(ErrorCode.BAD_INPUT, ex.getMessage());
        }

        writeResult(resultMessage, resultMessage);
    }

    /**
     * Delete a message in the Team chat
     *
     * @throws IOException
     *             if any error occurs in the client/server communication.
     */
    public void deleteMessage() throws IOException {
        String[] tokens = getTokens("message");

        ResultMessage resultMessage;

        try {
            int messageID = Integer.parseInt(tokens[0]);

            boolean deleted = MessageDAO.deleteMessage(new Message(messageID));

            if (deleted) {
                resultMessage = new ResultMessage(InfoMessage.MESSAGE_DELETED);
            } else {
                resultMessage = new ResultMessage(ErrorCode.NO_CHANGES);
            }

        } catch (SQLException | NamingException ex) {
            resultMessage = new ResultMessage(ErrorCode.CANNOT_ACCESS_DATABASE, ex.getMessage());
        } catch (NumberFormatException | IndexOutOfBoundsException ex) {
            resultMessage = new ResultMessage(ErrorCode.BAD_INPUT, ex.getMessage());
        }

        writeResult(resultMessage, resultMessage);
    }
}
