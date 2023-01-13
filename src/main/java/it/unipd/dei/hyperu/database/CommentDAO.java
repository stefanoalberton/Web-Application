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

import javax.naming.NamingException;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;

/**
 * Database Access Object for the users.
 *
 * @author Marco Alecci marco.alecci@studenti.unipd.it
 * @version 1.00
 * @since 1.00
 */
public class CommentDAO extends AbstractDAO {

    /**
     * Get all the comments under an Idea
     *
     * @param idea {@code Idea}.
     * @param page the offset of the comments.
     * @return A list of {@code Comment} of the idea.
     * @throws SQLException    If SQLException.
     * @throws NamingException If NamingException.
     */
    public static List<Comment> listIdeaComments(Idea idea, int page) throws SQLException, NamingException {
        final int offset = COMMENTS_PER_PAGE * page;

        //SQL query
        final String STATEMENT = "SELECT Comment.id, Comment.Sent_Time, Comment.text, Comment.id_user, Users.Username " +
                "FROM Comment INNER JOIN Users ON Comment.ID_User = Users.ID " +
                "WHERE Comment .ID_Idea = ? " +
                "AND Users.Banned != True " +
                "ORDER BY Comment.Sent_Time ASC " +
                "LIMIT ? OFFSET ?";
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Connection connection = null;

        final List<Comment> ideaComments = new ArrayList<>();

        try {
            //connection to the database and getting the statement
            connection = DataSourceProvider.getDataSource().getConnection();
            preparedStatement = connection.prepareStatement(STATEMENT);
            //put the parameter of the method in the query and execute it
            preparedStatement.setInt(1, idea.getID());
            preparedStatement.setInt(2, COMMENTS_PER_PAGE);
            preparedStatement.setInt(3, offset);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                //creation of the user and addition of the idea to the list
                User user = new User(resultSet.getInt("id_user"),
                        resultSet.getString("username"),
                        null,
                        null,
                        false,
                        null);

                ideaComments.add(new Comment(resultSet.getInt("id"),
                        resultSet.getString("text"),
                        resultSet.getTimestamp("sent_time"),
                        null,
                        user
                ));
            }
        } finally {
            cleaningOperations(preparedStatement, resultSet, connection);
        }

        return ideaComments;
    }

    /**
     * Get the comment by his ID
     *
     * @param commentID the ID(identification) code of the comment.
     * @return A {@code Comment} object.
     * @throws SQLException    If SQLException.
     * @throws NamingException If NamingException.
     */

    public static Comment getCommentByID(int commentID) throws NamingException, SQLException {
        //SQL query
        final String STATEMENT = "SELECT *\n" +
                "FROM Comment\n" +
                "WHERE id = ?";
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Connection connection = null;

        Comment comment = null;

        try {
            //connection to the database and getting the statement
            connection = DataSourceProvider.getDataSource().getConnection();
            preparedStatement = connection.prepareStatement(STATEMENT);
            //put the parameter of the method in the query and execute it
            preparedStatement.setInt(1, commentID);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                //create the object Comment to show
                User commentUser = UserDAO.getUserByID(resultSet.getInt("ID_User"));
                Idea commentIdea = IdeaDAO.getIdeaByID(resultSet.getInt("ID_Idea"));

                comment = new Comment(resultSet.getInt("ID"),
                        resultSet.getString("Text"),
                        resultSet.getTimestamp("Sent_Time"),
                        commentIdea,
                        commentUser);
            }
        } finally {
            cleaningOperations(preparedStatement, resultSet, connection);
        }

        return comment;
    }

    /**
     * Send a new comment to an Idea
     *
     * @param comment the {@code Comment} to be sent.
     * @return the ID of the new {@code Comment}
     * @throws SQLException    If SQLException.
     * @throws NamingException If NamingException.
     */
    public static int sendComment(Comment comment) throws SQLException, NamingException {
        //SQL query
        final String STATEMENT = "INSERT INTO Comment (Text,Id_Idea,ID_User) VALUES (?, ?, ?)";
        PreparedStatement preparedStatement = null;
        Connection connection = null;
        ResultSet resultSet;
        int commentID = -1;

        try {
            //connection to the database and getting the statement
            connection = DataSourceProvider.getDataSource().getConnection();
            preparedStatement = connection.prepareStatement(STATEMENT, Statement.RETURN_GENERATED_KEYS);
            //put the parameters of the method in the query and execute it
            preparedStatement.setString(1, comment.getText());
            preparedStatement.setInt(2, comment.getIdea().getID());
            preparedStatement.setInt(3, comment.getUser().getID());

            // execute query retrieving the generated keys (ID)
            preparedStatement.executeUpdate();
            resultSet = preparedStatement.getGeneratedKeys();

            if (resultSet.next()) {
                // get the ID of the inserted message
                commentID = resultSet.getInt(1);
            }
        } finally {
            cleaningOperations(preparedStatement, null, connection);
        }

        return commentID;
    }

    /**
     * Delete a comment
     *
     * @param comment the {@code Comment}.
     * @return {@code true} if updated, {@code false} otherwise.
     * @throws SQLException    If SQLException.
     * @throws NamingException If NamingException.
     */
    public static boolean deleteComment(Comment comment) throws SQLException, NamingException {
        //SQL query
        final String STATEMENT = "DELETE FROM comment WHERE id = ?";

        PreparedStatement preparedStatement = null;
        Connection connection = null;
        boolean updated;

        try {
            //connection to the database and getting the statement
            connection = DataSourceProvider.getDataSource().getConnection();
            preparedStatement = connection.prepareStatement(STATEMENT);
            //put the parameter of the method in the query and execute it
            preparedStatement.setInt(1, comment.getID());


            updated = preparedStatement.executeUpdate() > 0;

        } finally {
            cleaningOperations(preparedStatement, null, connection);
        }

        return updated;
    }


    /**
     * Modify a comment
     *
     * @param comment {@code Comment}.
     * @return {@code true} if updated, {@code false} otherwise.
     * @throws SQLException    If SQLException.
     * @throws NamingException If NamingException.
     */
    public static boolean updateComment(Comment comment) throws SQLException, NamingException {
        //SQL query
        final String STATEMENT = "UPDATE comment " +
                "SET text = ? " +
                "WHERE id = ?;";

        PreparedStatement preparedStatement = null;
        Connection connection = null;
        boolean updated;

        try {
            //connection to the database and getting the statement
            connection = DataSourceProvider.getDataSource().getConnection();
            preparedStatement = connection.prepareStatement(STATEMENT);
            //put the parameter of the method in the query and execute it
            preparedStatement.setString(1, comment.getText());
            preparedStatement.setInt(2, comment.getID());

            // execute update
            updated = preparedStatement.executeUpdate() > 0;

        } finally {
            cleaningOperations(preparedStatement, null, connection);
        }

        return updated;
    }
}
