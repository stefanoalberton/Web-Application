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

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import it.unipd.dei.hyperu.database.IdeaDAO;
import it.unipd.dei.hyperu.database.SkillDAO;
import it.unipd.dei.hyperu.database.TopicDAO;
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
import java.util.HashMap;
import java.util.List;

/**
 * Manages the REST API for the {@link Idea} resource.
 *
 * @author Marco Alecci (marco.alecci@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public class IdeaRestResource extends AbstractRestResource {
    /**
     * Creates a new REST resource for managing {@code Idea} resources.
     *
     * @param req the HTTP request.
     * @param res the HTTP response.
     */
    public IdeaRestResource(HttpServletRequest req, HttpServletResponse res) {
        super(req, res);
    }

    /**
     * Return the idea
     *
     * @throws IOException if any error occurs in the client/server communication.
     */
    public void getIdea() throws IOException {
        String[] tokens = getTokens("idea");
        ResultMessage resultMessage;

        Idea idea = null;

        try {
            int ideaID = Integer.parseInt(tokens[0]);

            idea = IdeaDAO.getIdeaByID(ideaID);

            if (idea != null) {
                List<Skill> ideaSkills = IdeaDAO.listIdeaSkills(idea);
                List<Topic> ideaTopics = IdeaDAO.listIdeaTopics(idea);
                List<Team> ideaTeams = IdeaDAO.listIdeaTeams(idea);

                User loggedUser = getLoggedUser();
                boolean liked = loggedUser != null && IdeaDAO.ideaLiked(idea, loggedUser);

                idea.setSkills(ideaSkills);
                idea.setTopics(ideaTopics);
                idea.setTeams(ideaTeams);
                idea.setLiked(liked);

                resultMessage = new ResultMessage(InfoMessage.IDEA_INFO_RETRIEVED);
            } else {
                resultMessage = new ResultMessage(ErrorCode.NO_IDEA_FOUND);
            }
        } catch (SQLException | NamingException ex) {
            resultMessage = new ResultMessage(ErrorCode.CANNOT_ACCESS_DATABASE, ex.getMessage());
        } catch (NumberFormatException | IndexOutOfBoundsException ex) {
            resultMessage = new ResultMessage(ErrorCode.BAD_INPUT, ex.getMessage());
        }

        writeResult(resultMessage, idea);
    }

    /**
     * List all the ideas related to a specific topic
     *
     * @throws IOException if any error occurs in the client/server communication.
     */
    public void listIdeasByTopic() throws IOException {
        String[] tokens = getTokens("topic");
        ResultMessage resultMessage;

        List<Idea> ideas = null;
        Topic topic = null;

        try {
            int topicID = Integer.parseInt(tokens[0]);
            int page = req.getParameter("page") != null ? Integer.parseInt(req.getParameter("page")) : 0;

            topic = TopicDAO.getTopicByID(topicID);
            if (topic != null) {
                ideas = IdeaDAO.listIdeasByTopic(new Topic(topicID, null, null), page);

                User loggedUser = getLoggedUser();
                for (Idea idea : ideas) {
                    boolean liked = loggedUser != null && IdeaDAO.ideaLiked(idea, loggedUser);
                    idea.setLiked(liked);
                }

                resultMessage = new ResultMessage(InfoMessage.IDEA_LISTED);
            } else {
                resultMessage = new ResultMessage(ErrorCode.NO_TOPIC_FOUND);
            }
        } catch (SQLException | NamingException ex) {
            resultMessage = new ResultMessage(ErrorCode.CANNOT_ACCESS_DATABASE, ex.getMessage());
        } catch (NumberFormatException | IndexOutOfBoundsException ex) {
            resultMessage = new ResultMessage(ErrorCode.BAD_INPUT, ex.getMessage());
        }

        HashMap<String, Object> res = new HashMap<>();
        res.put("topic", topic);
        res.put("ideas", ideas);
        writeResult(resultMessage, res);
    }

    /**
     * List all the ideas related to a specific skill
     *
     * @throws IOException if any error occurs in the client/server communication.
     */
    public void listIdeasBySkill() throws IOException {
        String[] tokens = getTokens("skill");
        ResultMessage resultMessage;

        List<Idea> ideas = null;
        Skill skill = null;

        try {
            int skillID = Integer.parseInt(tokens[0]);
            int page = req.getParameter("page") != null ? Integer.parseInt(req.getParameter("page")) : 0;

            skill = SkillDAO.getSkillByID(skillID);
            if (skill != null) {
                ideas = IdeaDAO.listIdeasBySkill(new Skill(skillID, null, null), page);

                User loggedUser = getLoggedUser();
                for (Idea idea : ideas) {
                    boolean liked = loggedUser != null && IdeaDAO.ideaLiked(idea, loggedUser);
                    idea.setLiked(liked);
                }

                resultMessage = new ResultMessage(InfoMessage.IDEA_LISTED);
            } else {
                resultMessage = new ResultMessage(ErrorCode.NO_SKILL_FOUND);
            }
        } catch (SQLException | NamingException ex) {
            resultMessage = new ResultMessage(ErrorCode.CANNOT_ACCESS_DATABASE, ex.getMessage());
        } catch (NumberFormatException | IndexOutOfBoundsException ex) {
            resultMessage = new ResultMessage(ErrorCode.BAD_INPUT, ex.getMessage());
        }

        HashMap<String, Object> res = new HashMap<>();
        res.put("skill", skill);
        res.put("ideas", ideas);
        writeResult(resultMessage, res);
    }

    /**
     * Create a new Idea.
     *
     * @throws IOException if any error occurs in the client/server communication.
     */
    public void createIdea() throws IOException {
        ResultMessage resultMessage;

        int ideaID = -1;

        try {
            Idea idea = getJSON(Idea.class);

            if (idea.getTitle() == null || idea.getTitle().isBlank()) {
                resultMessage = new ResultMessage(ErrorCode.IDEA_TITLE_MISSING);
            } else if (idea.getDescription() == null || idea.getDescription().isBlank()) {
                resultMessage = new ResultMessage(ErrorCode.IDEA_DESCRIPTION_MISSING);
            } else {
                User loggedUser = getLoggedUser();

                Idea ideaToInsert = new Idea(ideaID, idea.getTitle().trim(), idea.getDescription().trim(), null, null, 0, loggedUser);
                ideaToInsert.setTopics(idea.getTopics());
                ideaToInsert.setSkills(idea.getSkills());

                ideaID = IdeaDAO.createIdea(ideaToInsert);

                if (ideaID > 0) {
                    resultMessage = new ResultMessage(InfoMessage.IDEA_CREATED);
                } else {
                    resultMessage = new ResultMessage(ErrorCode.NO_ELEMENT_ADDED);
                }
            }
        } catch (SQLException | NamingException ex) {
            resultMessage = new ResultMessage(ErrorCode.CANNOT_ACCESS_DATABASE, ex.getMessage());
        } catch (NumberFormatException | IndexOutOfBoundsException | JsonParseException | NullPointerException ex) {
            resultMessage = new ResultMessage(ErrorCode.BAD_INPUT, ex.getMessage());
        }

        writeResult(resultMessage, ideaID);
    }

    /**
     * Update the title and description of an existing Idea.
     *
     * @throws IOException if any error occurs in the client/server communication.
     */
    public void updateIdea() throws IOException {
        String[] tokens = getTokens("idea");
        ResultMessage resultMessage;

        try {
            int ideaID = Integer.parseInt(tokens[0]);
            Idea idea = getJSON(Idea.class);

            if (idea.getTitle() == null || idea.getTitle().isBlank()) {
                resultMessage = new ResultMessage(ErrorCode.IDEA_TITLE_MISSING);
            } else if (idea.getDescription() == null || idea.getDescription().isBlank()) {
                resultMessage = new ResultMessage(ErrorCode.IDEA_DESCRIPTION_MISSING);
            } else {
                Idea ideaToUpdate = new Idea(ideaID, idea.getTitle().trim(), idea.getDescription().trim(), null, null, 0, null);

                boolean updated = IdeaDAO.updateIdea(ideaToUpdate);
                if (updated) {
                    resultMessage = new ResultMessage(InfoMessage.IDEA_UPDATED);
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
     * Update the image of an existing Idea.
     *
     * @throws IOException if any error occurs in the client/server communication.
     */
    public void updateImageIdea(ServletConfig servletConfig) throws IOException {
        String[] tokens = getTokens("idea");
        ResultMessage resultMessage = new ResultMessage(ErrorCode.NO_CHANGES);

        try {
            int ideaID = Integer.parseInt(tokens[0]);

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

                        Idea idea = new Idea(ideaID);
                        idea.setImage(image);

                        boolean updated = IdeaDAO.updateImageIdea(idea);
                        if (updated) {
                            resultMessage = new ResultMessage(InfoMessage.IDEA_IMAGE_UPDATED);
                        } else {
                            resultMessage = new ResultMessage(ErrorCode.NO_CHANGES);
                        }
                        break;
                    } else {
                        resultMessage = new ResultMessage(ErrorCode.IMAGE_TOO_LARGE);
                    }
                } else {
                    resultMessage = new ResultMessage(ErrorCode.NOT_IMAGE);
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
     * Remove an image from an Idea.
     *
     * @throws IOException if any error occurs in the client/server communication.
     */
    public void removeImageIdea() throws IOException {
        String[] tokens = getTokens("idea");
        ResultMessage resultMessage;

        try {
            int ideaID = Integer.parseInt(tokens[0]);

            boolean updated = IdeaDAO.removeImageIdea(new Idea(ideaID));
            if (updated) {
                resultMessage = new ResultMessage(InfoMessage.IDEA_IMAGE_REMOVED);
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
     * Delete an existing Idea.
     *
     * @throws IOException if any error occurs in the client/server communication.
     */
    public void deleteIdea() throws IOException {
        String[] tokens = getTokens("idea");
        ResultMessage resultMessage;

        try {
            int ideaID = Integer.parseInt(tokens[0]);

            boolean deleted = IdeaDAO.deleteIdea(new Idea(ideaID));
            if (deleted) {
                resultMessage = new ResultMessage(InfoMessage.IDEA_DELETED);
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
     * Update an existing Idea adding a skill.
     *
     * @throws IOException if any error occurs in the client/server communication.
     */
    public void addSkillToIdea() throws IOException {
        String[] tokens = getTokens("idea");
        ResultMessage resultMessage;

        try {
            int ideaID = Integer.parseInt(tokens[0]);
            int skillID = Integer.parseInt(tokens[2]);

            boolean added = IdeaDAO.addSkillToIdea(new Idea(ideaID), new Skill(skillID, null, null));
            if (added) {
                resultMessage = new ResultMessage(InfoMessage.IDEA_SKILL_ADDED);
            } else {
                resultMessage = new ResultMessage(ErrorCode.NO_ELEMENT_ADDED);
            }
        } catch (SQLException | NamingException ex) {
            if (ex instanceof SQLException && ((SQLException) ex).getSQLState().equals(DUPLICATE_ERROR_CODE)) {
                resultMessage = new ResultMessage(ErrorCode.DUPLICATE_SKILL_IDEA, ex.getMessage());
            } else {
                resultMessage = new ResultMessage(ErrorCode.CANNOT_ACCESS_DATABASE, ex.getMessage());
            }
        } catch (NumberFormatException | IndexOutOfBoundsException ex) {
            resultMessage = new ResultMessage(ErrorCode.BAD_INPUT, ex.getMessage());
        }

        writeResult(resultMessage);
    }

    /**
     * Update an existing Idea removing a skill.
     *
     * @throws IOException if any error occurs in the client/server communication.
     */
    public void removeSkillFromIdea() throws IOException {
        String[] tokens = getTokens("idea");
        ResultMessage resultMessage;

        try {
            int ideaID = Integer.parseInt(tokens[0]);
            int skillID = Integer.parseInt(tokens[2]);

            boolean updated = IdeaDAO.removeSkillFromIdea(new Idea(ideaID), new Skill(skillID, null, null));
            if (updated) {
                resultMessage = new ResultMessage(InfoMessage.IDEA_SKILL_REMOVED);
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
     * Update an existing Idea adding a topic.
     *
     * @throws IOException if any error occurs in the client/server communication.
     */
    public void addTopicToIdea() throws IOException {
        String[] tokens = getTokens("idea");
        ResultMessage resultMessage;

        try {
            int ideaID = Integer.parseInt(tokens[0]);
            int topicID = Integer.parseInt(tokens[2]);

            boolean added = IdeaDAO.addTopicToIdea(new Idea(ideaID), new Topic(topicID, null, null));
            if (added) {
                resultMessage = new ResultMessage(InfoMessage.IDEA_TOPIC_ADDED);
            } else {
                resultMessage = new ResultMessage(ErrorCode.NO_ELEMENT_ADDED);
            }
        } catch (SQLException | NamingException ex) {
            if (ex instanceof SQLException && ((SQLException) ex).getSQLState().equals(DUPLICATE_ERROR_CODE)) {
                resultMessage = new ResultMessage(ErrorCode.DUPLICATE_TOPIC_IDEA, ex.getMessage());
            } else {
                resultMessage = new ResultMessage(ErrorCode.CANNOT_ACCESS_DATABASE, ex.getMessage());
            }
        } catch (NumberFormatException | IndexOutOfBoundsException ex) {
            resultMessage = new ResultMessage(ErrorCode.BAD_INPUT, ex.getMessage());
        }

        writeResult(resultMessage);
    }

    /**
     * Update an existing Idea removing a topic.
     *
     * @throws IOException if any error occurs in the client/server communication.
     */
    public void removeTopicFromIdea() throws IOException {
        String[] tokens = getTokens("idea");
        ResultMessage resultMessage;

        try {
            int ideaID = Integer.parseInt(tokens[0]);
            int topicID = Integer.parseInt(tokens[2]);

            boolean updated = IdeaDAO.removeTopicFromIdea(new Idea(ideaID), new Topic(topicID, null, null));
            if (updated) {
                resultMessage = new ResultMessage(InfoMessage.IDEA_TOPIC_REMOVED);
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
     * Like an Idea by a user
     *
     * @throws IOException if any error occurs in the client/server communication.
     */
    public void likeIdea() throws IOException {
        String[] tokens = getTokens("idea");
        ResultMessage resultMessage;

        try {
            int ideaID = Integer.parseInt(tokens[0]);
            User loggedUser = getLoggedUser();

            boolean updated = IdeaDAO.likeIdea(new Idea(ideaID), loggedUser);
            if (updated) {
                resultMessage = new ResultMessage(InfoMessage.IDEA_LIKED);
            } else {
                resultMessage = new ResultMessage(ErrorCode.NO_CHANGES);
            }
        } catch (SQLException | NamingException ex) {
            if (ex instanceof SQLException && ((SQLException) ex).getSQLState().equals(DUPLICATE_ERROR_CODE)) {
                resultMessage = new ResultMessage(ErrorCode.IDEA_ALREADY_LIKED, ex.getMessage());
            } else {
                resultMessage = new ResultMessage(ErrorCode.CANNOT_ACCESS_DATABASE, ex.getMessage());
            }
        } catch (NumberFormatException | IndexOutOfBoundsException ex) {
            resultMessage = new ResultMessage(ErrorCode.BAD_INPUT, ex.getMessage());
        }

        writeResult(resultMessage);
    }

    /**
     * Unlike an Idea by a user
     *
     * @throws IOException if any error occurs in the client/server communication.
     */
    public void unlikeIdea() throws IOException {
        String[] tokens = getTokens("idea");
        ResultMessage resultMessage;

        try {
            int ideaID = Integer.parseInt(tokens[0]);
            User loggedUser = getLoggedUser();

            boolean updated = IdeaDAO.unlikeIdea(new Idea(ideaID), loggedUser);
            if (updated) {
                resultMessage = new ResultMessage(InfoMessage.IDEA_UNLIKED);
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
