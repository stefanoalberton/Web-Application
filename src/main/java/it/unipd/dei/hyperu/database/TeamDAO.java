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
import it.unipd.dei.hyperu.utils.StatusType;
import it.unipd.dei.hyperu.utils.UserType;

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
public class TeamDAO extends AbstractDAO {

    /**
     * Get Team Members of a specific Team
     *
     * @param team the {@code Team}.
     * @return A list of {@code User} object representing the members of a Team.
     * @throws SQLException    If SQLException.
     * @throws NamingException If NamingException.
     */
    public static List<User> listGroupMembers(Team team) throws SQLException, NamingException {
        //SQL query
        final String STATEMENT = "SELECT Users.ID, Users.Username, Users.Email, Users.Role, Users.Banned FROM " +
                "Users JOIN Request ON Users.ID = Request.ID_User " +
                "WHERE Request.ID_Team = ? AND Request.Status = 'Accepted' AND Users.Banned != True " +
                "UNION SELECT Users.ID, Users.Username, Users.Email, Users.Role, Users.Banned FROM " +
                "Users JOIN Idea ON Idea.ID_Creator = Users.ID JOIN Team ON Team.ID_Idea = Idea.ID " +
                "WHERE Team.ID = ? AND Users.Banned != True";
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Connection connection = null;


        final List<User> groupMembers = new ArrayList<>();

        try {
            // prepare the connection
            connection = DataSourceProvider.getDataSource().getConnection();
            preparedStatement = connection.prepareStatement(STATEMENT);
            //put the parameters of the method in the query
            preparedStatement.setInt(1, team.getID());
            preparedStatement.setInt(2, team.getID());

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                //add the group member to the list
                groupMembers.add(new User(resultSet.getInt("ID"),
                        resultSet.getString("Username"),
                        resultSet.getString("Email"),
                        UserType.valueOfLabel(resultSet.getString("Role")),
                        resultSet.getBoolean("Banned")));
            }

        } finally {
            cleaningOperations(preparedStatement, resultSet, connection);
        }

        return groupMembers;
    }

    /**
     * Check if a user is member of a team
     *
     * @param team the {@code Team}.
     * @param user the {@code User}.
     * @return A list of {@code User} object representing the members of a Team.
     * @throws SQLException    If SQLException.
     * @throws NamingException If NamingException.
     */
    public static boolean isTeamMember(Team team, User user) throws SQLException, NamingException {
        //SQL query
        final String STATEMENT = "SELECT Users.ID FROM " +
                "Users JOIN Request ON Users.ID = Request.ID_User " +
                "WHERE Request.ID_Team = ? AND Request.Status = 'Accepted' AND Users.Banned != True AND Users.ID = ? " +
                "UNION SELECT Users.ID FROM " +
                "Users JOIN Idea ON Idea.ID_Creator = Users.ID JOIN Team ON Team.ID_Idea = Idea.ID " +
                "WHERE Team.ID = ? AND Users.Banned != True AND Users.ID = ?";
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Connection connection = null;

        try {
            // prepare the connection
            connection = DataSourceProvider.getDataSource().getConnection();
            preparedStatement = connection.prepareStatement(STATEMENT);
            //put the parameters of the method in the query
            preparedStatement.setInt(1, team.getID());
            preparedStatement.setInt(2, user.getID());
            preparedStatement.setInt(3, team.getID());
            preparedStatement.setInt(4, user.getID());

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                if (resultSet.getInt("id") == user.getID()) return true;
            }

        } finally {
            cleaningOperations(preparedStatement, resultSet, connection);
        }

        return false;
    }

    /**
     * Get Team from ID without image
     *
     * @param teamID the ID (identification) code of the {@code Team}.
     * @return A {@code Team} object.
     * @throws SQLException    If SQLException.
     * @throws NamingException If NamingException.
     */
    public static Team getTeamByID(int teamID) throws SQLException, NamingException {
        return getTeamByID(teamID, false);
    }

    /**
     * Get Team from ID
     *
     * @param teamID the ID (identification) of the team.
     * @param withImage the image of the group chat of the team
     * @return A {@code Team} object.
     * @throws SQLException    If SQLException.
     * @throws NamingException If NamingException.
     */
    public static Team getTeamByID(int teamID, boolean withImage) throws SQLException, NamingException {
        //SQL query
        final String STATEMENT = "SELECT Team.*, Group_Chat.* " +
                "FROM team JOIN group_chat ON Team.id = group_chat.id " +
                "JOIN Idea ON Idea.ID = Team.ID_Idea " +
                "JOIN Users ON Users.ID = Idea.ID_Creator " +
                "WHERE Team.id = ? AND Users.Banned != True";

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Connection connection = null;

        Team team = null;

        try {
            connection = DataSourceProvider.getDataSource().getConnection();
            preparedStatement = connection.prepareStatement(STATEMENT);
            preparedStatement.setInt(1, teamID);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                Idea teamIdea = IdeaDAO.getIdeaByID(resultSet.getInt("ID_Idea"), withImage);

                team = new Team(resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getTimestamp("creation_time"),
                        teamIdea,
                        resultSet.getBoolean("Accept_Requests"),
                        (withImage) ? resultSet.getBytes("image") : null,
                        resultSet.getString("description"));

            }
        } finally {
            cleaningOperations(preparedStatement, resultSet, connection);
        }

        return team;
    }

    /**
     * Get Team from ID, only showing the team idea and the accept_reqeust parameter
     *
     * @param teamID the ID (identification) of the team.
     * @param withImage the image of the group chat of the team
     * @return A {@code Team} object.
     * @throws SQLException    If SQLException.
     * @throws NamingException If NamingException.
     */
    public static Team getTeamByIDMinimal(int teamID) throws SQLException, NamingException {
        //SQL query
        final String STATEMENT = "SELECT Team.*, Team.ID_Idea, Idea.ID_Creator " +
                "FROM team JOIN group_chat ON Team.id = group_chat.id " +
                "JOIN Idea ON Idea.ID = Team.ID_Idea " +
                "JOIN Users ON Users.ID = Idea.ID_Creator " +
                "WHERE Team.id = ? AND Users.Banned != True";

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Connection connection = null;

        Team team = null;

        try {
            connection = DataSourceProvider.getDataSource().getConnection();
            preparedStatement = connection.prepareStatement(STATEMENT);
            preparedStatement.setInt(1, teamID);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                Idea teamIdea = new Idea(resultSet.getInt("id_idea"),
                        null, null, null, null, 0,
                        new User(resultSet.getInt("id_creator")));

                team = new Team(resultSet.getInt("id"),
                        null,
                        null,
                        teamIdea,
                        resultSet.getBoolean("Accept_Requests"),
                        null,
                        null);
            }
        } finally {
            cleaningOperations(preparedStatement, resultSet, connection);
        }

        return team;
    }

    /**
     * List all the messaged of the group
     *
     * @param team the {@code Team}.
     * @param page the offset of the message chat.
     * @return A list of {@code Message} object representing the members of a Team.
     * @throws SQLException    If SQLException.
     * @throws NamingException If NamingException.
     */
    public static List<Message> listGroupMessages(Team team, int page) throws SQLException, NamingException {
        final int offset = MESSAGES_PER_PAGE * page;
        //SQL query
        final String STATEMENT = "SELECT Message.*, " +
                "Users.Username, Users.Email, Users.Role, Users.Banned " +
                "FROM Message INNER JOIN Users ON Message.ID_User = Users.ID " +
                "WHERE Message.ID_Group = ? " +
                "ORDER BY Message.Sent_Time DESC " +
                "LIMIT ? OFFSET ?";

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Connection connection = null;

        // the results of the search
        final List<Message> groupMessages = new ArrayList<>();

        try {
            //connection to the database and getting the statement
            connection = DataSourceProvider.getDataSource().getConnection();
            preparedStatement = connection.prepareStatement(STATEMENT);
            //put the parameter of the method in the query
            preparedStatement.setInt(1, team.getID());
            preparedStatement.setInt(2, MESSAGES_PER_PAGE);
            preparedStatement.setInt(3, offset);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                User user = new User(resultSet.getInt("id_user"),
                        resultSet.getString("username"),
                        resultSet.getString("email"),
                        UserType.valueOfLabel(resultSet.getString("role")),
                        resultSet.getBoolean("banned"),
                        null);

                FileInfo fileInfo = new Gson().fromJson(resultSet.getString("FileInfo"), FileInfo.class);
                groupMessages.add(new Message(resultSet.getInt("id"),
                        resultSet.getString("content"),
                        null,
                        fileInfo,
                        resultSet.getTimestamp("sent_time"),
                        null,
                        user
                ));
            }
        } finally {
            cleaningOperations(preparedStatement, resultSet, connection);
        }

        return groupMessages;
    }

    /**
     * Create a new Team.
     *
     * @param team the {@code Team} object of the team.
     * @return the id of the created team.
     * @throws SQLException    If SQLException.
     * @throws NamingException If NamingException.
     */
    public static int createTeam(Team team) throws SQLException, NamingException {
        //SQL query
        final String STATEMENT = "INSERT INTO Team (Name, ID_Idea, Accept_Requests) VALUES (?, ?, ?)";
        final String STATEMENT_GROUP = "INSERT INTO Group_Chat (ID, Description) VALUES (?, ?)";

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Connection connection = null;

        int teamID = -1;

        try {
            //connection to the database and getting the statement
            connection = DataSourceProvider.getDataSource().getConnection();
            preparedStatement = connection.prepareStatement(STATEMENT, Statement.RETURN_GENERATED_KEYS);
            //put the parameter of the method in the query
            preparedStatement.setString(1, team.getName());
            preparedStatement.setInt(2, team.getIdea().getID());
            preparedStatement.setBoolean(3, team.canAcceptRequests());

            // execute query retrieving the generated keys (ID)
            preparedStatement.executeUpdate();
            resultSet = preparedStatement.getGeneratedKeys();

            if (resultSet.next()) {
                // get the ID of the inserted Idea
                teamID = resultSet.getInt(1);

                preparedStatement = connection.prepareStatement(STATEMENT_GROUP);
                preparedStatement.setInt(1, teamID);
                preparedStatement.setString(2, team.getDescription());

                // execute query
                preparedStatement.executeUpdate();
            }

        } finally {
            cleaningOperations(preparedStatement, resultSet, connection);
        }

        return teamID;
    }

    /**
     * Update team informations
     *
     * @param team the {@code Team} object of the team to be updated.
     * @return {@code true} if the team is updated, {@code false} otherwise.
     * @throws SQLException    If SQLException.
     * @throws NamingException If NamingException.
     */
    public static boolean updateTeam(Team team) throws SQLException, NamingException {
        //SQL query
        final String STATEMENT = "UPDATE Team SET Name = ?, Accept_Requests = ? WHERE ID = ?";
        final String STATEMENT_GROUP = "UPDATE Group_Chat SET Description = ? WHERE ID = ?";

        PreparedStatement preparedStatement = null;
        Connection connection = null;

        boolean updated;

        try {
            // prepare the connection
            connection = DataSourceProvider.getDataSource().getConnection();
            preparedStatement = connection.prepareStatement(STATEMENT);
            //put the parameter of the method in the query
            preparedStatement.setString(1, team.getName());
            preparedStatement.setBoolean(2, team.canAcceptRequests());
            preparedStatement.setInt(3, team.getID());
            //execute query of team retrieving the generated keys (ID)
            updated = preparedStatement.executeUpdate() > 0;


            preparedStatement = connection.prepareStatement(STATEMENT_GROUP);

            preparedStatement.setString(1, team.getDescription());
            preparedStatement.setInt(2, team.getID());

            // execute query of group chat retrieving the generated keys (ID)
            updated = preparedStatement.executeUpdate() > 0 || updated;
        } finally {
            cleaningOperations(preparedStatement, null, connection);
        }

        return updated;
    }

    /**
     * Updated the image of the group chat
     *
     * @param team the {@code Team} object of the team.
     * @return {@code true} if the team is updated, {@code false} otherwise.
     * @throws SQLException    If SQLException.
     * @throws NamingException If NamingException.
     */
    public static boolean updateImageTeam(Team team) throws SQLException, NamingException {
        //SQL query
        final String STATEMENT = "UPDATE Group_Chat SET Image = ? WHERE ID = ?";

        PreparedStatement preparedStatement = null;
        Connection connection = null;

        boolean updated;

        try {
            // prepare the connection
            connection = DataSourceProvider.getDataSource().getConnection();
            preparedStatement = connection.prepareStatement(STATEMENT);
            //put the parameter of the method in the query
            preparedStatement.setBytes(1, team.getImage());
            preparedStatement.setInt(2, team.getID());
            // execute query retrieving the generated keys (ID)
            updated = preparedStatement.executeUpdate() > 0;
        } finally {
            cleaningOperations(preparedStatement, null, connection);
        }

        return updated;
    }

    /**
     * Updated the image of the group chat
     *
     * @param team the {@code Team} object of the team.
     * @return {@code true} if the team is updated, {@code false} otherwise.
     * @throws SQLException    If SQLException.
     * @throws NamingException If NamingException.
     */
    public static boolean removeImageTeam(Team team) throws SQLException, NamingException {
        //SQL query
        final String STATEMENT = "UPDATE Group_Chat SET Image = ? WHERE ID = ?";

        PreparedStatement preparedStatement = null;
        Connection connection = null;

        boolean updated;

        try {
            connection = DataSourceProvider.getDataSource().getConnection();
            // prepare the connection
            preparedStatement = connection.prepareStatement(STATEMENT);
            //put the parameter of the method in the query
            preparedStatement.setBytes(1, null);
            preparedStatement.setInt(2, team.getID());

            // execute query retrieving the generated keys (ID)
            updated = preparedStatement.executeUpdate() > 0;
        } finally {
            cleaningOperations(preparedStatement, null, connection);
        }

        return updated;
    }

    /**
     * Delete a Team.
     *
     * @param team the {@code Team} to be deleted.
     * @return {@code true} if the team is deleted, {@code false} otherwise.
     * @throws SQLException    If SQLException.
     * @throws NamingException If NamingException.
     */
    public static boolean deleteTeam(Team team) throws SQLException, NamingException {
        //SQL query
        final String STATEMENT = "DELETE FROM Team WHERE ID = ?";

        PreparedStatement preparedStatement = null;
        Connection connection = null;

        boolean updated;

        try {
            // prepare the connection
            connection = DataSourceProvider.getDataSource().getConnection();
            preparedStatement = connection.prepareStatement(STATEMENT);
            //put the parameter of the method in the query
            preparedStatement.setInt(1, team.getID());
            // execute query retrieving the generated keys (ID)
            updated = preparedStatement.executeUpdate() > 0;
        } finally {
            cleaningOperations(preparedStatement, null, connection);
        }

        return updated;
    }

    /**
     * List the join request of the team
     *
     * @param team the {@code Team} to be deleted.
     * @return a list of {@code JoinRequest} object.
     * @throws SQLException    If SQLException.
     * @throws NamingException If NamingException.
     */
    public static List<JoinRequest> listRequestsOfTeam(Team team) throws SQLException, NamingException {
        //SQL query
        final String STATEMENT = "select Users.Username, Users.Email, Users.Role, Users.Banned,\n" +
                "Request.status, Request.message, Request.requested_time, Request.id_user\n" +
                "from Request\n" +
                "inner join users\n" +
                "on users.id = request.id_user\n" +
                "where request.id_team = ?\n" +
                "and Users.banned != True \n" +
                "and status = 'Pending' \n" +
                "order by request.requested_time asc";

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Connection connection = null;

        final List<JoinRequest> requestList = new ArrayList<>();

        try {
            //prepare the connection
            connection = DataSourceProvider.getDataSource().getConnection();
            preparedStatement = connection.prepareStatement(STATEMENT);
            //put the parameter of the method in the query
            preparedStatement.setInt(1, team.getID());

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                    //add the join request to the list
                User userRequest = new User(resultSet.getInt("id_user"),
                        resultSet.getString("username"),
                        resultSet.getString("email"),
                        UserType.valueOfLabel(resultSet.getString("role")),
                        resultSet.getBoolean("banned"));

                JoinRequest request = new JoinRequest(null
                        , userRequest
                        , StatusType.valueOfLabel("status")
                        , resultSet.getString("message")
                        , resultSet.getTimestamp("requested_time"));

                requestList.add(request);
            }
        } finally {
            cleaningOperations(preparedStatement, resultSet, connection);
        }

        return requestList;
    }

}
