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
import it.unipd.dei.hyperu.database.TopicDAO;
import it.unipd.dei.hyperu.resource.Comment;
import it.unipd.dei.hyperu.resource.Topic;
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
 * Manages the REST API for the {@link Topic} resource.
 *
 * @author Marco Alecci (marco.alecci@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */

public class TopicRestResource extends AbstractRestResource {

    /**
     * Creates a new REST resource for managing {@code Topic} resources.
     *
     * @param req the HTTP request.
     * @param res the HTTP response.
     */
    public TopicRestResource(HttpServletRequest req, HttpServletResponse res) {
        super(req, res);
    }

    /**
     * List all the topics from database.
     *
     * @throws IOException
     *             if any error occurs in the client/server communication.
     */
    public void listAllTopics() throws IOException {
        List<Topic> listTopic = null;
        ResultMessage message;

        try {
            listTopic = TopicDAO.listTopics();

            message = new ResultMessage(InfoMessage.TOPICS_LISTED);

        } catch (SQLException | NamingException ex) {
            message = new ResultMessage(ErrorCode.CANNOT_ACCESS_DATABASE, ex.getMessage());
        }

        writeResult(message, listTopic);
    }

    /**
     * Add a new topic in the DB.
     *
     * @throws IOException
     *             if any error occurs in the client/server communication.
     */
    public void createTopic() throws IOException {
        ResultMessage resultMessage;

        int topicID = -1;

        try {
            Topic topic = getJSON(Topic.class);

            if (topic.getName() == null || topic.getName().isBlank()) {
                resultMessage = new ResultMessage(ErrorCode.TOPIC_NAME_MISSING);
            } else if (topic.getDescription() == null || topic.getDescription().isBlank()) {
                resultMessage = new ResultMessage(ErrorCode.TOPIC_DESCRIPTION_MISSING);
            } else {
                topicID = TopicDAO.createTopic(new Topic(topicID, topic.getName().trim(), topic.getDescription().trim()));

                if (topicID > 0) {
                    resultMessage = new ResultMessage(InfoMessage.TOPIC_CREATED);
                } else {
                    resultMessage = new ResultMessage(ErrorCode.NO_ELEMENT_ADDED);
                }
            }
        } catch (SQLException | NamingException ex) {
            resultMessage = new ResultMessage(ErrorCode.CANNOT_ACCESS_DATABASE, ex.getMessage());
        } catch (JsonParseException | NullPointerException ex) {
            resultMessage = new ResultMessage(ErrorCode.BAD_INPUT, ex.getMessage());
        }

        writeResult(resultMessage, topicID);
    }

    /**
     * Edit a topic.
     *
     * @throws IOException
     *             if any error occurs in the client/server communication.
     */
    public void updateTopic() throws IOException {
        String[] tokens = getTokens("topic");
        ResultMessage resultMessage;

        try {
            Topic topic = getJSON(Topic.class);

            if (topic.getName() == null || topic.getName().isBlank()) {
                resultMessage = new ResultMessage(ErrorCode.TOPIC_NAME_MISSING);
            } else if (topic.getDescription() == null || topic.getDescription().isBlank()) {
                resultMessage = new ResultMessage(ErrorCode.TOPIC_DESCRIPTION_MISSING);
            } else {
                int topicID = Integer.parseInt(tokens[0]);

                boolean updated = TopicDAO.updateTopic(new Topic(topicID, topic.getName().trim(), topic.getDescription().trim()));

                if (updated) {
                    resultMessage = new ResultMessage(InfoMessage.TOPIC_UPDATED);
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
     * Delete a topic from DB.
     *
     * @throws IOException
     *             if any error occurs in the client/server communication.
     */
    public void deleteTopic() throws IOException {
        String[] tokens = getTokens("topic");
        ResultMessage resultMessage;

        try {
            int topicID = Integer.parseInt(tokens[0]);

            boolean deleted = TopicDAO.deleteTopic(new Topic(topicID, null, null));

            if (deleted) {
                resultMessage = new ResultMessage(InfoMessage.TOPIC_DELETED);
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
