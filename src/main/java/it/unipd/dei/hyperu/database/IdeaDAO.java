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

package it.unipd.dei.hyperu.database;

import it.unipd.dei.hyperu.resource.*;
import it.unipd.dei.hyperu.utils.DataSourceProvider;
import it.unipd.dei.hyperu.utils.UserType;

import javax.naming.NamingException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Database Access Object for the ideas.
 *
 * @author Luca Martinelli (luca.martinelli.1@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public class IdeaDAO extends AbstractDAO {

    /**
     * Search ideas by query.
     *
     * @param query the query used to search ideas.
     * @param page  the offset of the ideas.
     * @return A list of {@code Idea} object.
     * @throws SQLException    If SQLException.
     * @throws NamingException If NamingException.
     */
    public static List<Idea> searchIdeas(String query, int page) throws SQLException, NamingException {
        final int offset = IDEAS_PER_PAGE * page;
        //SQL query
        final String STATEMENT = "SELECT Users.Username, Users.Email, Users.Role, Users.Banned, Idea.* " +
                "FROM Idea JOIN Users ON Idea.ID_Creator = Users.ID " +
                "WHERE (LOWER(Idea.Title) LIKE ? " +
                "OR LOWER(Idea.Description) LIKE ?) " +
                "AND Users.Banned != True " +
                "ORDER BY Idea.Posted_Time DESC " +
                "LIMIT ? OFFSET ?";
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Connection connection = null;

        // the results of the search
        final List<Idea> ideas = new ArrayList<>();

        try {
            //connection to the database and getting the statement
            connection = DataSourceProvider.getDataSource().getConnection();
            preparedStatement = connection.prepareStatement(STATEMENT);
            //setting the search
            preparedStatement.setString(1, "%" + query.toLowerCase() + "%");
            preparedStatement.setString(2, "%" + query.toLowerCase() + "%");
            preparedStatement.setInt(3, IDEAS_PER_PAGE);
            preparedStatement.setInt(4, offset);

            // execute query
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                //add ideas found to the list
                User ideaUser = new User(resultSet.getInt("ID_Creator"),
                        resultSet.getString("Username"),
                        resultSet.getString("Email"),
                        UserType.valueOfLabel(resultSet.getString("Role")),
                        resultSet.getBoolean("Banned"));

                Idea idea = new Idea(resultSet.getInt("ID"),
                        resultSet.getString("Title"),
                        resultSet.getString("Description"),
                        null,
                        resultSet.getTimestamp("Posted_Time"),
                        resultSet.getInt("Num_Likes"),
                        ideaUser);
                List<Skill> ideaSkills = IdeaDAO.listIdeaSkills(idea);
                List<Topic> ideaTopics = IdeaDAO.listIdeaTopics(idea);
                List<Team> ideaTeams = IdeaDAO.listIdeaTeams(idea);
                idea.setSkills(ideaSkills);
                idea.setTopics(ideaTopics);
                idea.setTeams(ideaTeams);

                ideas.add(idea);
            }
        } finally {
            cleaningOperations(preparedStatement, resultSet, connection);
        }

        return ideas;
    }

    /**
     * list all the ideas related to a specific topic
     *
     * @param topic the {@code Topic} in common for the ideas
     * @param page  the offset of the ideas.
     * @return A list of {@code Idea} object.
     * @throws SQLException    If SQLException.
     * @throws NamingException If NamingException.
     */
    public static List<Idea> listIdeasByTopic(Topic topic, int page) throws SQLException, NamingException {
        final int offset = IDEAS_PER_PAGE * page;
        PreparedStatement preparedStatement = null;
        Connection connection = null;
        //SQL query
        final String STATEMENT = "SELECT Users.Username, Users.Email, Users.Role, Users.Banned, Idea.* " +
                "FROM Idea JOIN Users ON Idea.ID_Creator = Users.ID " +
                "JOIN Is_Related ON Is_Related.ID_Idea = Idea.ID " +
                "WHERE Is_Related.ID_Topic = ? " +
                "AND Users.Banned != True " +
                "ORDER BY Idea.Posted_Time DESC " +
                "LIMIT ? OFFSET ?";

        ResultSet resultSet = null;
        final List<Idea> ideas = new ArrayList<>();

        try {
            //connection to the database and getting the statement
            connection = DataSourceProvider.getDataSource().getConnection();
            preparedStatement = connection.prepareStatement(STATEMENT);
            //setting the search
            preparedStatement.setInt(1, topic.getID());
            preparedStatement.setInt(2, IDEAS_PER_PAGE);
            preparedStatement.setInt(3, offset);
            // execute query
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                //add ideas found to the list
                User ideaUser = new User(resultSet.getInt("ID_Creator"),
                        resultSet.getString("Username"),
                        resultSet.getString("Email"),
                        UserType.valueOfLabel(resultSet.getString("Role")),
                        resultSet.getBoolean("Banned"));
                Idea idea = new Idea(resultSet.getInt("ID"),
                        resultSet.getString("Title"),
                        resultSet.getString("Description"),
                        null,
                        resultSet.getTimestamp("Posted_Time"),
                        resultSet.getInt("Num_Likes"),
                        ideaUser);
                List<Skill> ideaSkills = IdeaDAO.listIdeaSkills(idea);
                List<Topic> ideaTopics = IdeaDAO.listIdeaTopics(idea);
                List<Team> ideaTeams = IdeaDAO.listIdeaTeams(idea);
                idea.setSkills(ideaSkills);
                idea.setTopics(ideaTopics);
                idea.setTeams(ideaTeams);

                ideas.add(idea);
            }

        } finally {
            cleaningOperations(preparedStatement, resultSet, connection);
        }

        return ideas;
    }

    /**
     * list all the ideas related to a specific skill
     *
     * @param skill the {@code Skill} in common for the ideas
     * @param page  the offset of the ideas.
     * @return A list of {@code Idea} object.
     * @throws SQLException    If SQLException.
     * @throws NamingException If NamingException.
     */
    public static List<Idea> listIdeasBySkill(Skill skill, int page) throws SQLException, NamingException {
        final int offset = IDEAS_PER_PAGE * page;

        PreparedStatement preparedStatement = null;
        Connection connection = null;
        //SQL query
        final String STATEMENT = "SELECT Users.Username, Users.Email, Users.Role, Users.Banned, Idea.* " +
                "FROM Idea JOIN Users ON Idea.ID_Creator = Users.ID " +
                "JOIN Need ON Need.ID_Idea = Idea.ID " +
                "WHERE Need.ID_Skill = ? " +
                "AND Users.Banned != True " +
                "ORDER BY Idea.Posted_Time DESC " +
                "LIMIT ? OFFSET ?";

        ResultSet resultSet = null;
        final List<Idea> ideas = new ArrayList<>();

        try {
            //connection to the database and getting the statement
            connection = DataSourceProvider.getDataSource().getConnection();
            preparedStatement = connection.prepareStatement(STATEMENT);
            //put the parameters of the method in the query
            preparedStatement.setInt(1, skill.getID());
            preparedStatement.setInt(2, IDEAS_PER_PAGE);
            preparedStatement.setInt(3, offset);
            // execute query
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                //add ideas found to the list
                User ideaUser = new User(resultSet.getInt("ID_Creator"),
                        resultSet.getString("Username"),
                        resultSet.getString("Email"),
                        UserType.valueOfLabel(resultSet.getString("Role")),
                        resultSet.getBoolean("Banned"));
                Idea idea = new Idea(resultSet.getInt("ID"),
                        resultSet.getString("Title"),
                        resultSet.getString("Description"),
                        null,
                        resultSet.getTimestamp("Posted_Time"),
                        resultSet.getInt("Num_Likes"),
                        ideaUser);
                List<Skill> ideaSkills = IdeaDAO.listIdeaSkills(idea);
                List<Topic> ideaTopics = IdeaDAO.listIdeaTopics(idea);
                List<Team> ideaTeams = IdeaDAO.listIdeaTeams(idea);
                idea.setSkills(ideaSkills);
                idea.setTopics(ideaTopics);
                idea.setTeams(ideaTeams);

                ideas.add(idea);
            }

        } finally {
            cleaningOperations(preparedStatement, resultSet, connection);
        }

        return ideas;
    }

    /**
     * Get Idea from ID.
     *
     * @param ID        the ID (identification) of the {@code Idea}
     * @param withImage if there is the image of the idea
     * @return A {@code Idea} object.
     * @throws SQLException    If SQLException.
     * @throws NamingException If NamingException.
     */
    public static Idea getIdeaByID(int ID, boolean withImage) throws SQLException, NamingException {
        //SQL query
        final String STATEMENT = "SELECT Idea.* " +
                "FROM Idea JOIN Users ON Idea.ID_Creator = Users.ID " +
                "WHERE Idea.ID = ? " +
                "AND Users.Banned != True";
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Connection connection = null;

        Idea idea = null;

        try {
            //connection to the database and getting the statement
            connection = DataSourceProvider.getDataSource().getConnection();
            preparedStatement = connection.prepareStatement(STATEMENT);
            //put the parameter of the method in the query
            preparedStatement.setInt(1, ID);
            // execute query
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                //build the idea found
                User ideaUser = UserDAO.getUserByID(resultSet.getInt("ID_Creator"));

                idea = new Idea(resultSet.getInt("ID"),
                        resultSet.getString("Title"),
                        resultSet.getString("Description"),
                        (withImage) ? resultSet.getBytes("Image") : null,
                        resultSet.getTimestamp("Posted_Time"),
                        resultSet.getInt("Num_Likes"),
                        ideaUser);
            }
        } finally {
            cleaningOperations(preparedStatement, resultSet, connection);
        }

        return idea;
    }

    /**
     * Manage getIdeaByID without the image
     *
     * @param ID the ID (identification) of the {@code Idea}
     * @throws SQLException    If SQLException.
     * @throws NamingException If NamingException.
     */
    public static Idea getIdeaByID(int ID) throws SQLException, NamingException {
        return getIdeaByID(ID, false);
    }

    /**
     * Create a new Idea.
     *
     * @param idea the {@code Idea} object of the idea.
     * @return the ID of the inserted idea
     * @throws SQLException    If SQLException.
     * @throws NamingException If NamingException.
     */
    public static int createIdea(Idea idea) throws SQLException, NamingException {
        //SQL queries
        final String STATEMENT = "INSERT INTO Idea (Title, Description, ID_Creator) VALUES (?, ?, ?)";
        final String STATEMENT_TOPICS = "INSERT INTO Is_Related (ID_Idea, ID_Topic) VALUES (?, ?)";
        final String STATEMENT_SKILLS = "INSERT INTO Need (ID_Idea, ID_Skill) VALUES (?, ?)";

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Connection connection = null;

        int ideaID = -1;

        try {
            connection = DataSourceProvider.getDataSource().getConnection();
            // prepare the connection and return the generated keys after execution
            preparedStatement = connection.prepareStatement(STATEMENT, Statement.RETURN_GENERATED_KEYS);
            //put the parameters of the method in the queries
            preparedStatement.setString(1, idea.getTitle());
            preparedStatement.setString(2, idea.getDescription());
            preparedStatement.setInt(3, idea.getUser().getID());

            // execute query retrieving the generated keys (ID)
            preparedStatement.executeUpdate();
            resultSet = preparedStatement.getGeneratedKeys();

            if (resultSet.next()) {
                // get the ID of the inserted Idea
                ideaID = resultSet.getInt(1);
                // add skills and topics
                try {
                    if (idea.getSkills() != null && idea.getSkills().size() > 0) {
                        preparedStatement = connection.prepareStatement(STATEMENT_SKILLS);
                        for (Skill skill : idea.getSkills()) {
                            preparedStatement.setInt(1, ideaID);
                            preparedStatement.setInt(2, skill.getID());
                            preparedStatement.addBatch();
                        }
                        preparedStatement.executeBatch();
                    }
                    if (idea.getTopics() != null && idea.getTopics().size() > 0) {
                        preparedStatement = connection.prepareStatement(STATEMENT_TOPICS);
                        for (Topic topic : idea.getTopics()) {
                            preparedStatement.setInt(1, ideaID);
                            preparedStatement.setInt(2, topic.getID());
                            preparedStatement.addBatch();
                        }
                        preparedStatement.executeBatch();
                    }
                } catch (SQLException ignore) {
                }

                TeamDAO.createTeam(new Team(-1, idea.getTitle(), null, new Idea(ideaID), true));
            }

        } finally {
            cleaningOperations(preparedStatement, resultSet, connection);
        }

        return ideaID;
    }

    /**
     * Update the title and description of an existing Idea.
     *
     * @param idea the {@code Idea} object of the idea.
     * @return {@code true} if the idea is edited, {@code false} otherwise.
     * @throws SQLException    If SQLException.
     * @throws NamingException If NamingException.
     */
    public static boolean updateIdea(Idea idea) throws SQLException, NamingException {
        //SQL query
        final String STATEMENT = "UPDATE Idea SET Title = ?, Description = ? WHERE ID = ?";

        PreparedStatement preparedStatement = null;
        Connection connection = null;

        boolean updated;

        try {
            connection = DataSourceProvider.getDataSource().getConnection();
            // prepare the connection
            preparedStatement = connection.prepareStatement(STATEMENT);
            //put the parameters of the method in the query
            preparedStatement.setString(1, idea.getTitle());
            preparedStatement.setString(2, idea.getDescription());
            preparedStatement.setInt(3, idea.getID());

            // execute query retrieving the generated keys (ID)
            updated = preparedStatement.executeUpdate() > 0;
        } finally {
            cleaningOperations(preparedStatement, null, connection);
        }
        return updated;
    }

    /**
     * Update the image of an existing Idea.
     *
     * @param idea the {@code Idea} object of the idea.
     * @return {@code true} if the idea is edited, {@code false} otherwise.
     * @throws SQLException    If SQLException.
     * @throws NamingException If NamingException.
     */
    public static boolean updateImageIdea(Idea idea) throws SQLException, NamingException {
        //SQL query
        final String STATEMENT = "UPDATE Idea SET Image = ? WHERE ID = ?";

        PreparedStatement preparedStatement = null;
        Connection connection = null;

        boolean updated;

        try {
            // prepare the connection
            connection = DataSourceProvider.getDataSource().getConnection();
            preparedStatement = connection.prepareStatement(STATEMENT);
            //put the parameters of the method in the query
            preparedStatement.setBytes(1, idea.getImage());
            preparedStatement.setInt(2, idea.getID());

            // execute query retrieving the generated keys (ID)
            updated = preparedStatement.executeUpdate() > 0;
        } finally {
            cleaningOperations(preparedStatement, null, connection);
        }

        return updated;
    }

    /**
     * Remove an image from an Idea.
     *
     * @param idea the {@code Idea} object of the idea.
     * @return {@code true} if the idea is edited, {@code false} otherwise.
     * @throws SQLException    If SQLException.
     * @throws NamingException If NamingException.
     */
    public static boolean removeImageIdea(Idea idea) throws SQLException, NamingException {
        //SQL query
        final String STATEMENT = "UPDATE Idea SET Image = ? WHERE ID = ?";

        PreparedStatement preparedStatement = null;
        Connection connection = null;

        boolean updated;

        try {
            // prepare the connection
            connection = DataSourceProvider.getDataSource().getConnection();
            preparedStatement = connection.prepareStatement(STATEMENT);
            //put the parameters of the method in the query
            preparedStatement.setBytes(1, null);
            preparedStatement.setInt(2, idea.getID());

            // execute query retrieving the generated keys (ID)
            updated = preparedStatement.executeUpdate() > 0;
        } finally {
            cleaningOperations(preparedStatement, null, connection);
        }

        return updated;
    }

    /**
     * Delete an existing Idea.
     *
     * @param idea {@code Idea}.
     * @return {@code true} if idea deleted, {@code false} otherwise.
     * @throws SQLException    If SQLException.
     * @throws NamingException If NamingException.
     */
    public static boolean deleteIdea(Idea idea) throws SQLException, NamingException {
        //SQL query
        final String STATEMENT = "DELETE FROM Idea WHERE ID = ?";

        PreparedStatement preparedStatement = null;
        Connection connection = null;
        boolean deleted;

        try {
            // prepare the connection
            connection = DataSourceProvider.getDataSource().getConnection();
            preparedStatement = connection.prepareStatement(STATEMENT);
            //put the parameters of the method in the query
            preparedStatement.setInt(1, idea.getID());

            // execute query retrieving the generated keys (ID)
            deleted = preparedStatement.executeUpdate() > 0;

        } finally {
            cleaningOperations(preparedStatement, null, connection);
        }

        return deleted;
    }

    /**
     * List all the teams that are working on an idea
     *
     * @param idea {@code Idea}.
     * @return a list of {@code Team} object.
     * @throws SQLException    if any error occurs while listing Skills.
     * @throws NamingException if any error occurs while connecting to DB.
     */
    public static List<Team> listIdeaTeams(Idea idea) throws SQLException, NamingException {
        //SQL query
        final String STATEMENT = "SELECT Team.*, Group_Chat.Description " +
                "FROM Team JOIN Group_Chat ON Group_Chat.ID = Team.ID WHERE Team.ID_Idea = ? " +
                "ORDER BY Team.Name ASC";

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Connection connection = null;

        final List<Team> teams = new ArrayList<>();

        try {
            // prepare the connection
            connection = DataSourceProvider.getDataSource().getConnection();
            preparedStatement = connection.prepareStatement(STATEMENT);
            //put the parameters of the method in the query
            preparedStatement.setInt(1, idea.getID());

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                //add a team to the list
                teams.add(new Team(resultSet.getInt("ID"),
                        resultSet.getString("Name"),
                        resultSet.getTimestamp("Creation_Time"),
                        null,
                        resultSet.getBoolean("Accept_Requests"),
                        null,
                        resultSet.getString("Description")));
            }
        } finally {
            cleaningOperations(preparedStatement, resultSet, connection);
        }

        return teams;
    }

    /**
     * List the skills required for an idea
     *
     * @param idea {@code Idea}.
     * @return a list of {@code Skill} object.
     * @throws SQLException    if any error occurs while listing Skills.
     * @throws NamingException if any error occurs while connecting to DB.
     */
    public static List<Skill> listIdeaSkills(Idea idea) throws SQLException, NamingException {
        //SQL query
        final String STATEMENT = "SELECT Skill.ID, Skill.Name, Skill.Description " +
                "FROM Skill JOIN Need ON Skill.ID = Need.ID_Skill " +
                "WHERE Need.ID_Idea = ? " +
                "ORDER BY Skill.Name ASC ";

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Connection connection = null;

        // the results of the search
        final List<Skill> skills = new ArrayList<>();

        try {
            //connection to the database and getting the statement
            connection = DataSourceProvider.getDataSource().getConnection();
            preparedStatement = connection.prepareStatement(STATEMENT);
            //put the parameter of the method in the query
            preparedStatement.setInt(1, idea.getID());
            // execute query
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                //add a skill to the list
                skills.add(new Skill(resultSet.getInt("ID"),
                        resultSet.getString("Name"),
                        resultSet.getString("Description")));
            }
        } finally {
            cleaningOperations(preparedStatement, resultSet, connection);
        }

        return skills;
    }


    /**
     * List all the topics that regards an idea
     *
     * @param idea {@code Idea}.
     * @return a list of {@code Topic} object.
     * @throws SQLException    if any error occurs while listing Skills.
     * @throws NamingException if any error occurs while connecting to DB.
     */
    public static List<Topic> listIdeaTopics(Idea idea) throws SQLException, NamingException {
        //SQL query
        final String STATEMENT = "SELECT Topic.ID, Topic.Name, Topic.Description " +
                "FROM Topic JOIN Is_Related ON Topic.ID = Is_Related.ID_Topic " +
                "WHERE Is_Related.ID_Idea = ? " +
                "ORDER BY Topic.Name ASC ";

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Connection connection = null;

        final List<Topic> topics = new ArrayList<>();

        try {
            //connection to the database and getting the statement
            connection = DataSourceProvider.getDataSource().getConnection();
            preparedStatement = connection.prepareStatement(STATEMENT);
            //put the parameter of the method in the query
            preparedStatement.setInt(1, idea.getID());
            //execute query
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                //Add the topic to the list
                topics.add(new Topic(resultSet.getInt("ID"),
                        resultSet.getString("Name"),
                        resultSet.getString("Description")));
            }
        } finally {
            cleaningOperations(preparedStatement, resultSet, connection);
        }

        return topics;
    }

    /**
     * Update an existing Idea adding a skill.
     *
     * @param idea  the {@code Idea}.
     * @param skill the {@code Skill} to add to the idea.
     * @return {@code true} if skill added, {@code false} otherwise.
     * @throws SQLException    If SQLException.
     * @throws NamingException If NamingException.
     */
    public static boolean addSkillToIdea(Idea idea, Skill skill) throws SQLException, NamingException {

        //different statement for different type of updates
        final String STATEMENT = "INSERT INTO Need (ID_Idea, ID_Skill) VALUES (?, ?)";

        PreparedStatement preparedStatement = null;
        Connection connection = null;

        boolean updated;

        try {
            //connection to the database and getting the statement
            connection = DataSourceProvider.getDataSource().getConnection();
            preparedStatement = connection.prepareStatement(STATEMENT);
            //put the parameters of the method in the query
            preparedStatement.setInt(1, idea.getID());
            preparedStatement.setInt(2, skill.getID());

            // execute query retrieving the generated keys (ID)
            updated = preparedStatement.executeUpdate() > 0;
        } finally {
            cleaningOperations(preparedStatement, null, connection);
        }

        return updated;
    }

    /**
     * Update an existing Idea removing a skill.
     *
     * @param idea  the {@code Idea}.
     * @param skill the {@code Skill} to remove from the idea.
     * @return {@code true} if skill removed, {@code false} otherwise.
     * @throws SQLException    If SQLException.
     * @throws NamingException If NamingException.
     */
    public static boolean removeSkillFromIdea(Idea idea, Skill skill) throws SQLException, NamingException {
        //SQL query
        final String STATEMENT = "DELETE FROM Need WHERE ID_Idea = ? AND ID_Skill = ?";

        PreparedStatement preparedStatement = null;
        Connection connection = null;

        boolean updated;

        try {
            //connection to the database and getting the statement
            connection = DataSourceProvider.getDataSource().getConnection();
            preparedStatement = connection.prepareStatement(STATEMENT);
            //put the parameter of the method in the query
            preparedStatement.setInt(1, idea.getID());
            preparedStatement.setInt(2, skill.getID());

            // execute query retrieving the generated keys (ID)
            updated = preparedStatement.executeUpdate() > 0;
        } finally {
            cleaningOperations(preparedStatement, null, connection);
        }

        return updated;
    }

    /**
     * Update an existing Idea adding a topic.
     *
     * @param idea  the {@code Idea}.
     * @param topic the {@code Topic} to add to the idea.
     * @return {@code true} if topic added, {@code false} otherwise.
     * @throws SQLException    If SQLException.
     * @throws NamingException If NamingException.
     */
    public static boolean addTopicToIdea(Idea idea, Topic topic) throws SQLException, NamingException {
        //SQL query
        final String STATEMENT = "INSERT INTO Is_Related (ID_Idea, ID_Topic) VALUES (?, ?)";

        PreparedStatement preparedStatement = null;
        Connection connection = null;

        boolean updated;

        try {
            //connection to the database and getting the statement
            connection = DataSourceProvider.getDataSource().getConnection();
            preparedStatement = connection.prepareStatement(STATEMENT);
            //put the parameter of the method in the query
            preparedStatement.setInt(1, idea.getID());
            preparedStatement.setInt(2, topic.getID());

            // execute query retrieving the generated keys (ID)
            updated = preparedStatement.executeUpdate() > 0;
        } finally {
            cleaningOperations(preparedStatement, null, connection);
        }

        return updated;
    }

    /**
     * Update an existing Idea removing a topic.
     *
     * @param idea  the {@code Idea}.
     * @param topic the {@code Topic} to remove from the idea.
     * @return {@code true} if topic removed, {@code false} otherwise.
     * @throws SQLException    If SQLException.
     * @throws NamingException If NamingException.
     */
    public static boolean removeTopicFromIdea(Idea idea, Topic topic) throws SQLException, NamingException {
        //SQL query
        final String STATEMENT = "DELETE FROM Is_Related WHERE ID_Idea = ? AND ID_Topic = ?";

        PreparedStatement preparedStatement = null;
        Connection connection = null;

        boolean updated;

        try {
            // prepare the connection
            connection = DataSourceProvider.getDataSource().getConnection();
            preparedStatement = connection.prepareStatement(STATEMENT);
            //put the parameter of the method in the query
            preparedStatement.setInt(1, idea.getID());
            preparedStatement.setInt(2, topic.getID());

            // execute query retrieving the generated keys (ID)
            updated = preparedStatement.executeUpdate() > 0;
        } finally {
            cleaningOperations(preparedStatement, null, connection);
        }

        return updated;
    }

    /**
     * Unlike an Idea by a user
     *
     * @param idea the {@code Idea}.
     * @param user the {@code User} that unlike the idea
     * @return {@code true} if idea unliked, {@code false} otherwise.
     * @throws SQLException    If SQLException.
     * @throws NamingException If NamingException.
     */
    public static boolean unlikeIdea(Idea idea, User user) throws SQLException, NamingException {
        PreparedStatement preparedStatement = null;
        Connection connection = null;
        //SQL queries
        final String STATEMENT_DEL = "DELETE FROM likes where id_idea = ? and id_user = ?";
        final String STATEMENT_UPDATE = "UPDATE Idea SET Num_Likes = Num_Likes - 1 WHERE id = ?";

        boolean updated;

        try {
            //connection to the database and getting the statement
            connection = DataSourceProvider.getDataSource().getConnection();
            preparedStatement = connection.prepareStatement(STATEMENT_DEL);
            //put the parameters of the method in the queries
            preparedStatement.setInt(1, idea.getID());
            preparedStatement.setInt(2, user.getID());
            // execute query retrieving the generated keys (ID)
            updated = preparedStatement.executeUpdate() > 0;

            if (updated) {
                //subtract 1 from count of likes
                preparedStatement = connection.prepareStatement(STATEMENT_UPDATE);
                preparedStatement.setInt(1, idea.getID());
                preparedStatement.executeUpdate();
            }

        } finally {
            cleaningOperations(preparedStatement, null, connection);
        }

        return updated;
    }


    /**
     * Like an Idea by a user
     *
     * @param idea the {@code Idea}.
     * @param user the {@code User} that like the idea
     * @return {@code true} if idea liked, {@code false} otherwise.
     * @throws SQLException    If SQLException.
     * @throws NamingException If NamingException.
     */
    public static boolean likeIdea(Idea idea, User user) throws SQLException, NamingException {
        PreparedStatement preparedStatement = null;
        Connection connection = null;
        //SQL queries
        final String STATEMENT_INS = "INSERT INTO likes (id_idea, id_user) " +
                "VALUES (?, ?)";
        final String STATEMENT_UPDATE = "UPDATE Idea SET Num_Likes = Num_Likes + 1 WHERE id = ?";

        boolean updated;

        try {
            //connection to the database and getting the statement
            connection = DataSourceProvider.getDataSource().getConnection();
            preparedStatement = connection.prepareStatement(STATEMENT_INS);
            //put the parameter of the method in the query
            preparedStatement.setInt(1, idea.getID());
            preparedStatement.setInt(2, user.getID());
            // execute query retrieving the generated keys (ID)
            updated = preparedStatement.executeUpdate() > 0;

            if (updated) {
                //updates count of likes
                preparedStatement = connection.prepareStatement(STATEMENT_UPDATE);
                preparedStatement.setInt(1, idea.getID());
                preparedStatement.executeUpdate();
            }

        } finally {
            cleaningOperations(preparedStatement, null, connection);
        }

        return updated;
    }

    /**
     * Check if an idea is liked
     *
     * @param idea the {@code Idea}.
     * @param user the {@code User} that like the idea
     * @return {@code true} if idea liked, {@code false} otherwise.
     * @throws SQLException    If SQLException.
     * @throws NamingException If NamingException.
     */
    public static boolean ideaLiked(Idea idea, User user) throws SQLException, NamingException {
        PreparedStatement preparedStatement = null;
        Connection connection = null;
        ResultSet resultSet = null;
        //SQL queries
        final String STATEMENT = "SELECT COUNT(*) AS Liked FROM Likes WHERE ID_Idea = ? AND ID_User = ?";

        boolean updated;

        try {
            //connection to the database and getting the statement
            connection = DataSourceProvider.getDataSource().getConnection();
            preparedStatement = connection.prepareStatement(STATEMENT);
            //put the parameter of the method in the query
            preparedStatement.setInt(1, idea.getID());
            preparedStatement.setInt(2, user.getID());
            // execute query
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                if(resultSet.getInt("Liked") > 0) {
                    return true;
                }
            }

        } finally {
            cleaningOperations(preparedStatement, resultSet, connection);
        }

        return false;
    }

}
