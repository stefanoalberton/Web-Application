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
import it.unipd.dei.hyperu.database.CommentDAO;
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
 * Manages the REST API for the {@link Comment} resource.
 *
 * @author Marco Alecci (marco.alecci@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */

public class CommentRestResource extends AbstractRestResource {

    /**
     * Creates a new REST resource for managing {@code Comment} resources.
     *
     * @param req the HTTP request.
     * @param res the HTTP response.
     */
    public CommentRestResource(HttpServletRequest req, HttpServletResponse res) {
        super(req, res);
    }

    /**
     * Get all the comments under an Idea.
     *
     * @throws IOException
     *             if any error occurs in the client/server communication.
     */
    public void listIdeaComments() throws IOException {
        String[] tokens = getTokens("idea");
        ResultMessage resultMessage;

        List<Comment> ideaComments = null;

        try {
            int ideaID = Integer.parseInt(tokens[0]);
            int page = req.getParameter("page") != null ? Integer.parseInt(req.getParameter("page")) : 0;

            ideaComments = CommentDAO.listIdeaComments(new Idea(ideaID), page);

            resultMessage = new ResultMessage(InfoMessage.COMMENT_LISTED);

        } catch (SQLException | NamingException ex) {
            resultMessage = new ResultMessage(ErrorCode.CANNOT_ACCESS_DATABASE, ex.getMessage());
        } catch (NumberFormatException | IndexOutOfBoundsException ex) {
            resultMessage = new ResultMessage(ErrorCode.BAD_INPUT, ex.getMessage());
        }

        writeResult(resultMessage, ideaComments);
    }
    /**
     * Send a new comment to an Idea
     *
     * @throws IOException
     *             if any error occurs in the client/server communication.
     */
    public void sendComment() throws IOException {
        String[] tokens = getTokens("idea");
        ResultMessage resultMessage;

        int commentID = -1;

        try {
            int ideaID = Integer.parseInt(tokens[0]);

            Comment comment = getJSON(Comment.class);

            if (comment.getText() == null || comment.getText().isBlank()) {
                resultMessage = new ResultMessage(ErrorCode.COMMENT_TEXT_MISSING);
            } else {
                User loggedUser = getLoggedUser();

                commentID = CommentDAO.sendComment(new Comment(commentID, comment.getText().trim(), null, new Idea(ideaID), loggedUser));

                if (commentID > 0) {
                    resultMessage = new ResultMessage(InfoMessage.COMMENT_SENT);
                } else {
                    resultMessage = new ResultMessage(ErrorCode.NO_ELEMENT_ADDED);
                }
            }
        } catch (SQLException | NamingException ex) {
            resultMessage = new ResultMessage(ErrorCode.CANNOT_ACCESS_DATABASE, ex.getMessage());
        } catch (NumberFormatException | IndexOutOfBoundsException | JsonParseException | NullPointerException ex) {
            resultMessage = new ResultMessage(ErrorCode.BAD_INPUT, ex.getMessage());
        }

        writeResult(resultMessage, commentID);
    }

    /**
     * Delete a comment
     *
     * @throws IOException
     *             if any error occurs in the client/server communication.
     */
    public void deleteComment() throws IOException {
        String[] tokens = getTokens("comment");
        ResultMessage resultMessage;

        try {
            int commentID = Integer.parseInt(tokens[0]);

            boolean deleted = CommentDAO.deleteComment(new Comment(commentID));

            if (deleted)
                resultMessage = new ResultMessage(InfoMessage.COMMENT_DELETED);
            else
                resultMessage = new ResultMessage(ErrorCode.NO_CHANGES);

        } catch (SQLException | NamingException ex) {
            resultMessage = new ResultMessage(ErrorCode.CANNOT_ACCESS_DATABASE, ex.getMessage());
        } catch (NumberFormatException | IndexOutOfBoundsException ex) {
            resultMessage = new ResultMessage(ErrorCode.BAD_INPUT, ex.getMessage());
        }

        writeResult(resultMessage);
    }

    /**
     * Modify a comment
     *
     * @throws IOException
     *             if any error occurs in the client/server communication.
     */
    public void updateComment() throws IOException {
        String[] tokens = getTokens("comment");
        ResultMessage resultMessage;

        try {
            int commentID = Integer.parseInt(tokens[0]);
            Comment comment = getJSON(Comment.class);

            if (comment.getText() == null || comment.getText().isBlank()) {
                resultMessage = new ResultMessage(ErrorCode.COMMENT_TEXT_MISSING);
            } else {

                boolean updated = CommentDAO.updateComment(new Comment(commentID, comment.getText().trim()));

                if (updated) {
                    resultMessage = new ResultMessage(InfoMessage.COMMENT_UPDATED);
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

}
