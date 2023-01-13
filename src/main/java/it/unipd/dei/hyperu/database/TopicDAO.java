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

import it.unipd.dei.hyperu.resource.Skill;
import it.unipd.dei.hyperu.resource.Topic;
import it.unipd.dei.hyperu.utils.DataSourceProvider;

import javax.naming.NamingException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Database Access Object for the topics.
 *
 * @author Luca Martinelli (luca.martinelli.1@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public class TopicDAO extends AbstractDAO {

    /**
     * List all the topics from database
     *
     * @return a list of {@code topic} object.
     * @throws SQLException    if any error occurs while listing topics.
     * @throws NamingException if any error occurs while connecting to DB.
     */
    public static List<Topic> listTopics() throws SQLException, NamingException {
        //SQL query
        final String STATEMENT = "SELECT * FROM Topic ORDER BY Name ASC";
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Connection connection = null;

        final List<Topic> topics = new ArrayList<>();

        try {
            connection = DataSourceProvider.getDataSource().getConnection();
            preparedStatement = connection.prepareStatement(STATEMENT);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
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
     * Get topic by ID
     *
     * @param topicID the id of the topic
     * @return a {@code Topic} object.
     * @throws SQLException    if any error occurs while listing Skills.
     * @throws NamingException if any error occurs while connecting to DB.
     */
    public static Topic getTopicByID(int topicID) throws SQLException, NamingException {
        //SQL query
        final String STATEMENT = "SELECT * FROM Topic WHERE ID = ?";
        PreparedStatement pstmt = null;
        ResultSet resultSet = null;
        Connection connection = null;

        try {
            //prepare the connection
            connection = DataSourceProvider.getDataSource().getConnection();
            pstmt = connection.prepareStatement(STATEMENT);
            pstmt.setInt(1, topicID);
            //execute the query
            resultSet = pstmt.executeQuery();

            if (resultSet.next()) {
                //add the skill to the list
                return new Topic(resultSet.getInt("ID"),
                        resultSet.getString("Name"),
                        resultSet.getString("Description"));
            }
        } finally {
            cleaningOperations(pstmt, resultSet, connection);
        }

        return null;
    }


    /**
     * Add a new topic in the DB.
     *
     * @param topic the {@code Topic} to create.
     * @return the ID of the {@code topic} created.
     * @throws SQLException    if any error occurs while creating topic.
     * @throws NamingException if any error occurs while connecting to DB
     */
    public static int createTopic(Topic topic) throws SQLException, NamingException {
        //SQL query
        final String STATEMENT = "INSERT INTO Topic (Name,description) VALUES (?,?)";
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Connection connection = null;

        int topicID = -1;

        try {
            connection = DataSourceProvider.getDataSource().getConnection();
            // prepare the connection and return the generated keys after execution
            preparedStatement = connection.prepareStatement(STATEMENT, Statement.RETURN_GENERATED_KEYS);
            //put the parameter of the method in the query
            preparedStatement.setString(1, topic.getName());
            preparedStatement.setString(2, topic.getDescription());

            // execute query retrieving the generated keys (ID)
            preparedStatement.executeUpdate();
            resultSet = preparedStatement.getGeneratedKeys();

            if (resultSet.next()) {
                // get the ID of the inserted message
                topicID = resultSet.getInt(1);
            }
        } finally {
            cleaningOperations(preparedStatement, resultSet, connection);
        }
        return topicID;
    }

    /**
     * Delete a topic from DB.
     *
     * @param topic the {@code Topic} to be removed.
     * @return {@code true} if updated, {@code false} otherwise.
     * @throws SQLException    if any error occurs while deleting topic
     * @throws NamingException if any error occurs while connecting to DB.
     */
    public static boolean deleteTopic(Topic topic) throws SQLException, NamingException {
        //SQL query
        final String STATEMENT = "DELETE FROM Topic WHERE ID = ? ";
        PreparedStatement preparedStatement = null;
        boolean updated;
        Connection connection = null;

        try {
            connection = DataSourceProvider.getDataSource().getConnection();
            // prepare the connection and return the generated keys after execution
            preparedStatement = connection.prepareStatement(STATEMENT);
            preparedStatement.setInt(1, topic.getID());

            // execute query retrieving the number of rows deleted
            updated = preparedStatement.executeUpdate() > 0;

        } finally {
            cleaningOperations(preparedStatement, null, connection);
        }
        return updated;
    }

    /**
     * Edit a topic.
     *
     * @param topic the {@code Topic} to be edited.
     * @return {@code true} if updated, {@code false} otherwise.
     * @throws SQLException    if any error occurs while listing Skills.
     * @throws NamingException if any error occurs while connecting to DB.
     */
    public static boolean updateTopic(Topic topic) throws SQLException, NamingException {
        //SQL query
        final String STATEMENT = "UPDATE Topic SET Name = ?, Description = ? WHERE ID = ?";
        PreparedStatement preparedStatement = null;
        boolean updated;
        Connection connection = null;

        try {
            connection = DataSourceProvider.getDataSource().getConnection();
            // prepare the connection and return the generated keys after execution
            preparedStatement = connection.prepareStatement(STATEMENT);
            preparedStatement.setString(1, topic.getName());
            preparedStatement.setString(2, topic.getDescription());
            preparedStatement.setInt(3, topic.getID());

            // execute query retrieving the number of rows deleted
            updated = preparedStatement.executeUpdate() > 0;

        } finally {
            cleaningOperations(preparedStatement, null, connection);
        }
        return updated;
    }


}


