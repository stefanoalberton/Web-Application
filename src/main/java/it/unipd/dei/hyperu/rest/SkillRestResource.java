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
import it.unipd.dei.hyperu.database.SkillDAO;
import it.unipd.dei.hyperu.resource.Comment;
import it.unipd.dei.hyperu.resource.Skill;
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
 * Manages the REST API for the {@link Skill} resource.
 *
 * @author Marco Alecci (marco.alecci@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public class SkillRestResource extends AbstractRestResource {
    /**
     * Creates a new REST resource for managing {@code Skill} resources.
     *
     * @param req the HTTP request.
     * @param res the HTTP response.
     */

    public SkillRestResource(HttpServletRequest req, HttpServletResponse res) {
        super(req, res);
    }

    /**
     * List all the skills from database.
     *
     * @throws IOException
     *             if any error occurs in the client/server communication.
     */
    public void listAllSkills() throws IOException {
        List<Skill> listSkill = null;
        ResultMessage resultMessage;

        try {
            listSkill = SkillDAO.listSkills();

            resultMessage = new ResultMessage(InfoMessage.SKILLS_LISTED);
        } catch (SQLException | NamingException ex) {
            resultMessage = new ResultMessage(ErrorCode.CANNOT_ACCESS_DATABASE, ex.getMessage());
        }

        writeResult(resultMessage, listSkill);

    }

    /**
     * Add a new skill in the DB.
     *
     * @throws IOException
     *             if any error occurs in the client/server communication.
     */
    public void createSkill() throws IOException {
        ResultMessage resultMessage;

        int skillID = -1;

        try {
            Skill skill = getJSON(Skill.class);

            if (skill.getName() == null || skill.getName().isBlank()) {
                resultMessage = new ResultMessage(ErrorCode.SKILL_NAME_MISSING);
            } else if (skill.getDescription() == null || skill.getDescription().isBlank()) {
                resultMessage = new ResultMessage(ErrorCode.SKILL_DESCRIPTION_MISSING);
            } else {
                skillID = SkillDAO.createSkill(new Skill(skillID, skill.getName().trim(), skill.getDescription().trim()));

                if (skillID > 0) {
                    resultMessage = new ResultMessage(InfoMessage.SKILL_CREATED);
                } else {
                    resultMessage = new ResultMessage(ErrorCode.NO_ELEMENT_ADDED);
                }
            }
        } catch (SQLException | NamingException ex) {
            resultMessage = new ResultMessage(ErrorCode.CANNOT_ACCESS_DATABASE, ex.getMessage());
        } catch (JsonParseException | NullPointerException ex) {
            resultMessage = new ResultMessage(ErrorCode.BAD_INPUT, ex.getMessage());
        }

        writeResult(resultMessage, skillID);
    }

    /**
     * Edit a skill.
     *
     * @throws IOException
     *             if any error occurs in the client/server communication.
     */
    public void updateSkill() throws IOException {
        String[] tokens = getTokens("skill");
        ResultMessage resultMessage;

        try {
            Skill skill = getJSON(Skill.class);

            if (skill.getName() == null || skill.getName().isBlank()) {
                resultMessage = new ResultMessage(ErrorCode.SKILL_NAME_MISSING);
            } else if (skill.getDescription() == null || skill.getDescription().isBlank()) {
                resultMessage = new ResultMessage(ErrorCode.SKILL_DESCRIPTION_MISSING);
            } else {
                int skillID = Integer.parseInt(tokens[0]);
                boolean updated = SkillDAO.updateSkill(new Skill(skillID, skill.getName(), skill.getDescription()));

                if (updated) {
                    resultMessage = new ResultMessage(InfoMessage.SKILL_UPDATED);
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
     * Delete a new skill in the DB
     *
     * @throws IOException
     *             if any error occurs in the client/server communication.
     */
    public void deleteSkill() throws IOException {
        String[] tokens = getTokens("skill");
        ResultMessage resultMessage;

        try {
            int skillID = Integer.parseInt(tokens[0]);

            boolean deleted = SkillDAO.deleteSkill(new Skill(skillID, null, null));

            if (deleted) {
                resultMessage = new ResultMessage(InfoMessage.SKILL_DELETED);
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
