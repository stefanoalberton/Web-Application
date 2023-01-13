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


import it.unipd.dei.hyperu.resource.JoinRequest;
import it.unipd.dei.hyperu.utils.DataSourceProvider;

import javax.naming.NamingException;
import java.sql.*;

/**
 * Database Access Object for the skills.
 *
 * @author Luca Martinelli (luca.martinelli.1@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public final class JoinRequestDAO extends AbstractDAO {

    /**
     * send a new request join
     *
     * @param joinRequest the {@code JoinRequest} relative to the request.
     * @return {@code true} if added, {@code false} otherwise.
     * @throws SQLException    If SQLException.
     * @throws NamingException If NamingException.
     */
    public static boolean sendJoinRequest(JoinRequest joinRequest) throws SQLException, NamingException {
        //SQL query
        final String STATEMENT = "INSERT INTO Request (ID_Team,ID_User,Message) VALUES (?, ?, ?)";

        PreparedStatement preparedStatement = null;
        Connection connection = null;

        boolean updated;

        try {
            // prepare the connection
            connection = DataSourceProvider.getDataSource().getConnection();
            preparedStatement = connection.prepareStatement(STATEMENT);

            //put the parameters of the method in the query
            preparedStatement.setInt(1, joinRequest.getTeam().getID());
            preparedStatement.setInt(2, joinRequest.getUser().getID());
            preparedStatement.setString(3, joinRequest.getMessage());

            // execute query
            updated = preparedStatement.executeUpdate() > 0;

        } finally {
            cleaningOperations(preparedStatement, null, connection);
        }

        return updated;
    }

    /**
     * accept the join request
     *
     * @param joinRequest the {@code JoinRequest} relative to the request.
     * @return {@code true} if accepted, {@code false} otherwise.
     * @throws SQLException    If SQLException.
     * @throws NamingException If NamingException.
     */
    public static boolean acceptJoinRequest(JoinRequest joinRequest) throws SQLException, NamingException {
        //SQL query
        final String STATEMENT = "UPDATE request " +
                "SET status = 'Accepted' " +
                "WHERE id_team = ? AND id_user = ?";

        PreparedStatement preparedStatement = null;
        Connection connection = null;
        boolean update;

        try {
            connection = DataSourceProvider.getDataSource().getConnection();
            // prepare the connection and return the generated keys after execution
            preparedStatement = connection.prepareStatement(STATEMENT);
            //put the parameters of the method in the query
            preparedStatement.setInt(1, joinRequest.getTeam().getID());
            preparedStatement.setInt(2, joinRequest.getUser().getID());

            // execute query
            update = preparedStatement.executeUpdate() > 0;

        } finally {
            cleaningOperations(preparedStatement, null, connection);
        }

        return update;
    }

    /**
     * delete the join request
     *
     * @param joinRequest the {@code JoinRequest} relative to the request.
     * @return {@code true} if deleted, {@code false} otherwise.
     * @throws SQLException    If SQLException.
     * @throws NamingException If NamingException.
     */
    public static boolean deleteJoinRequest(JoinRequest joinRequest) throws SQLException, NamingException {
        //SQL query
        final String STATEMENT = "DELETE FROM request WHERE id_team = ? AND id_user = ?";

        PreparedStatement preparedStatement = null;
        Connection connection = null;
        boolean update;

        try {
            connection = DataSourceProvider.getDataSource().getConnection();
            // prepare the connection and return the generated keys after execution
            preparedStatement = connection.prepareStatement(STATEMENT);
            //put the parameters of the method in the query
            preparedStatement.setInt(1, joinRequest.getTeam().getID());
            preparedStatement.setInt(2, joinRequest.getUser().getID());

            // execute query
            update = preparedStatement.executeUpdate() > 0;

        } finally {
            cleaningOperations(preparedStatement, null, connection);
        }

        return update;
    }
}
