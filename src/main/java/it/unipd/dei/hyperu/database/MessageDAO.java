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

import com.google.gson.Gson;
import it.unipd.dei.hyperu.resource.*;
import it.unipd.dei.hyperu.utils.DataSourceProvider;

import javax.naming.NamingException;

import java.sql.*;

/**
 * Database Access Object for the users.
 *
 * @author Marco Alecci marco.alecci@studenti.unipd.it
 * @version 1.00
 * @since 1.00
 */
public class MessageDAO extends AbstractDAO {

    /**
     * Send a new message in the Team chat
     *
     * @param message the {@code Message} to send.
     * @return {@code true} if message sent, {@code false} otherwise.
     * @throws SQLException    If SQLException.
     * @throws NamingException If NamingException.
     */
    public static int sendMessage(Message message) throws SQLException, NamingException {
        //SQL query
        final String STATEMENT = "INSERT INTO Message (Content, File, FileInfo, ID_Group, ID_User) VALUES (?, ?, ?::JSON, ?, ?)";
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Connection connection = null;

        int messageID = -1;

        try {

            //connection to the database and getting the statement
            connection = DataSourceProvider.getDataSource().getConnection();

            // prepare the connection and return the generated keys after execution
            preparedStatement = connection.prepareStatement(STATEMENT, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, message.getContent());
            preparedStatement.setBytes(2, message.getFile());
            preparedStatement.setObject(3, message.getFile() != null ? new Gson().toJson(message.getFileInfo()) : null);
            preparedStatement.setInt(4, message.getTeam().getID());
            preparedStatement.setInt(5, message.getUser().getID());

            // execute query retrieving the generated keys (ID)
            preparedStatement.executeUpdate();
            resultSet = preparedStatement.getGeneratedKeys();

            if (resultSet.next()) {
                // get the ID of the inserted message
                messageID = resultSet.getInt(1);
            }
        } finally {
            cleaningOperations(preparedStatement, resultSet, connection);
        }

        return messageID;
    }

    /**
     * update a message in the Team chat
     *
     * @param message the {@code Message} to send.
     * @return {@code true} if message sent updated, {@code false} otherwise.
     * @throws SQLException    If SQLException.
     * @throws NamingException If NamingException.
     */
    public static boolean updateMessage(Message message) throws SQLException, NamingException {
        //SQL query
        final String STATEMENT = "UPDATE Message SET Content = ? WHERE ID = ?";
        PreparedStatement preparedStatement = null;
        Connection connection = null;

        boolean updated;

        try {
            //connection to the database and getting the statement
            connection = DataSourceProvider.getDataSource().getConnection();
            preparedStatement = connection.prepareStatement(STATEMENT);
            //put the parameters of the method in the queries
            preparedStatement.setString(1, message.getContent());
            preparedStatement.setInt(2, message.getID());

            // execute query retrieving the generated keys (ID)
            updated = preparedStatement.executeUpdate() > 0;
        } finally {
            cleaningOperations(preparedStatement, null, connection);
        }

        return updated;
    }

    /**
     * Delete a message in the Team chat
     *
     * @param message the {@code Message} to send.
     * @return {@code true} if message deleted, {@code false} otherwise.
     * @throws SQLException    If SQLException.
     * @throws NamingException If NamingException.
     */
    public static boolean deleteMessage(Message message) throws SQLException, NamingException {
        //SQL query
        final String STATEMENT = "DELETE FROM Message WHERE ID = ?";
        PreparedStatement preparedStatement = null;
        Connection connection = null;

        boolean updated;

        try {
            // prepare the connection
            connection = DataSourceProvider.getDataSource().getConnection();
            preparedStatement = connection.prepareStatement(STATEMENT);
            //put the parameters of the method in the queries
            preparedStatement.setInt(1, message.getID());

            // execute query retrieving the generated keys (ID)
            updated = preparedStatement.executeUpdate() > 0;
        } finally {
            cleaningOperations(preparedStatement, null, connection);
        }

        return updated;
    }

    /**
     * Retrieve a message by his ID
     *
     * @param message the {@code Message} to send
     * @return the {@code message}
     * @throws SQLException    If SQLException.
     * @throws NamingException If NamingException.
     */
    public static Message getMessageByID(int messageID) throws NamingException, SQLException {
        //SQL query
        final String STATEMENT = "SELECT *\n" +
                "FROM Message\n" +
                "WHERE id = ?";
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Connection connection = null;

        Message message = null;

        try {
            // prepare the connection
            connection = DataSourceProvider.getDataSource().getConnection();
            //put the parameters of the method in the queries
            preparedStatement = connection.prepareStatement(STATEMENT);
            preparedStatement.setInt(1, messageID);
            // execute query
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                //prepare the message to return
                Team messageTeam = TeamDAO.getTeamByID(resultSet.getInt("ID_Group"));
                User messageUser = UserDAO.getUserByID(resultSet.getInt("ID_User"));

                FileInfo fileInfo = new Gson().fromJson(resultSet.getString("FileInfo"), FileInfo.class);
                message = new Message(resultSet.getInt("ID"),
                        resultSet.getString("Content"),
                        resultSet.getBytes("File"),
                        fileInfo,
                        resultSet.getTimestamp("Sent_Time"),
                        messageTeam,
                        messageUser);

            }
        } finally {
            cleaningOperations(preparedStatement, resultSet, connection);
        }

        return message;
    }
}
