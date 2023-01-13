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
import it.unipd.dei.hyperu.utils.*;

import javax.naming.NamingException;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Database Access Object for the users.
 *
 * @author Elia Ziroldo (elia.ziroldo@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public class UserDAO extends AbstractDAO {

    /**
     * Get user by username and password.
     *
     * @param loginName the username of the user.
     * @param password  the password of the user.
     * @return -1 if authentication failed, userID if authentication success.
     * @throws SQLException    If SQLException.
     * @throws NamingException If NamingException.
     */
    public static int authenticateUser(String loginName, String password) throws SQLException, NamingException {
        final String STATEMENT = "SELECT ID FROM Users WHERE Username = ? AND Password = MD5(?) AND Banned != True";
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Connection connection = null;

        int userID = -1;

        try {
            //prepare the connection
            connection = DataSourceProvider.getDataSource().getConnection();
            preparedStatement = connection.prepareStatement(STATEMENT);

            preparedStatement.setString(1, loginName);
            preparedStatement.setString(2, PasswordEncryptor.addSalt(password));
            //execute the query
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                userID = resultSet.getInt("ID");
            }
        } finally {
            cleaningOperations(preparedStatement, resultSet, connection);
        }

        return userID;
    }

    /**
     * generate the token for the user
     *
     * @param user the {@code User}.
     * @return the object Token.
     * @throws SQLException    If SQLException.
     * @throws NamingException If NamingException.
     */
    public static UUID createToken(User user) throws SQLException, NamingException {
        final String STATEMENT = "INSERT INTO Tokens (ID_Token, ID_User) VALUES (?, ?)";
        PreparedStatement preparedStatement = null;
        Connection connection = null;

        UUID token = UUID.randomUUID();

        try {
            connection = DataSourceProvider.getDataSource().getConnection();
            preparedStatement = connection.prepareStatement(STATEMENT);
            //put the parameter of the method in the query
            preparedStatement.setObject(1, token);
            preparedStatement.setInt(2, user.getID());

            boolean insert = preparedStatement.executeUpdate() > 0;
            if (!insert) {
                token = null;
            }
        } finally {
            cleaningOperations(preparedStatement, null, connection);
        }

        return token;
    }

    /**
     * generate the token for the user
     *
     * @param loginUserID the id of the login.
     * @param token       the token used to authenticate.
     * @return the object Token.
     * @throws SQLException    If SQLException.
     * @throws NamingException If NamingException.
     */
    public static int authenticateToken(int loginUserID, UUID token) throws SQLException, NamingException {
        //sql queries
        final String STATEMENT = "SELECT Users.ID, Tokens.ID_Token FROM Users JOIN Tokens ON Tokens.ID_User = Users.ID " +
                "WHERE Tokens.ID_User = ? AND Tokens.ID_Token = ? AND Users.Banned != True AND CURRENT_DATE <= Tokens.Creation_Time + INTERVAL '1 week'";
        final String STATEMENT_DEL = "DELETE FROM Tokens WHERE ID_Token = ?";

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Connection connection = null;

        int userID = -1;

        try {

            connection = DataSourceProvider.getDataSource().getConnection();
            preparedStatement = connection.prepareStatement(STATEMENT);
            //put the parameter of the method in the query
            preparedStatement.setInt(1, loginUserID);
            preparedStatement.setObject(2, token);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                userID = resultSet.getInt("ID");

                preparedStatement = connection.prepareStatement(STATEMENT_DEL);
                preparedStatement.setObject(1, token);

                preparedStatement.executeUpdate();
            }
        } finally {
            cleaningOperations(preparedStatement, resultSet, connection);
        }

        return userID;
    }

    /**
     * Get user by ID.
     *
     * @param ID the ID (identification) of the user.
     * @return A {@code User} object representing the user.
     * @throws SQLException    If SQLException.
     * @throws NamingException If NamingException.
     */
    public static User getUserByID(int ID) throws SQLException, NamingException {
        //SQL query
        final String STATEMENT = "SELECT * FROM Users WHERE ID = ? AND Users.Banned != True";

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Connection connection = null;

        // the results of the search
        User user = null;

        try {
            //put the parameter of the method in the query
            connection = DataSourceProvider.getDataSource().getConnection();
            preparedStatement = connection.prepareStatement(STATEMENT);
            //put the parameter of the method in the query
            preparedStatement.setInt(1, ID);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                user = new User(resultSet.getInt("ID"),
                        resultSet.getString("Username"),
                        resultSet.getString("Email"),
                        UserType.valueOfLabel(resultSet.getString("Role")),
                        resultSet.getBoolean("Banned"),
                        null);
            }
        } finally {
            cleaningOperations(preparedStatement, resultSet, connection);
        }

        return user;
    }

    /**
     * Get user by username.
     *
     * @param username the username of the user.
     * @return A {@code User} object representing the user.
     * @throws SQLException    If SQLException.
     * @throws NamingException If NamingException.
     */
    public static User getUserByUsername(String username) throws SQLException, NamingException {
        //SQL query
        final String STATEMENT = "SELECT Users.* FROM Users WHERE Username = ? AND Banned != True";
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Connection connection = null;

        User user = null;

        try {
            connection = DataSourceProvider.getDataSource().getConnection();
            preparedStatement = connection.prepareStatement(STATEMENT);
            //put the parameter of the method in the query
            preparedStatement.setString(1, username);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                //prepare the user to return
                user = new User(resultSet.getInt("ID"),
                        resultSet.getString("Username"),
                        resultSet.getString("Email"),
                        UserType.valueOfLabel(resultSet.getString("Role")),
                        resultSet.getBoolean("Banned"),
                        null);
            }
        } finally {
            cleaningOperations(preparedStatement, resultSet, connection);
        }

        return user;
    }

    /**
     * Get Team Members of a specific Team
     *
     * @param query the query used to search users.
     * @param page  the offset.
     * @return A list of {@code User} object representing the members of a Team.
     * @throws SQLException    If SQLException.
     * @throws NamingException If NamingException.
     */
    public static List<User> searchUsers(String query, int page) throws SQLException, NamingException {
        final int offset = USERS_PER_PAGE * page;

        final String STATEMENT = "SELECT Users.* " +
                "FROM Users JOIN Profile ON Profile.ID = Users.ID " +
                "WHERE (LOWER(Users.Username) LIKE ? " +
                "OR LOWER(Users.Email) LIKE ? " +
                "OR LOWER(Profile.Name) LIKE ? " +
                "OR LOWER(Profile.Surname) LIKE ?)  " +
                "AND Banned != True " +
                "ORDER BY Profile.Name ASC " +
                "LIMIT ? OFFSET ?";
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Connection connection = null;

        final List<User> usersFound = new ArrayList<>();

        try {
            connection = DataSourceProvider.getDataSource().getConnection();
            preparedStatement = connection.prepareStatement(STATEMENT);
            //put the parameter of the method in the query
            preparedStatement.setString(1, "%" + query.toLowerCase() + "%");
            preparedStatement.setString(2, "%" + query.toLowerCase() + "%");
            preparedStatement.setString(3, "%" + query.toLowerCase() + "%");
            preparedStatement.setString(4, "%" + query.toLowerCase() + "%");
            preparedStatement.setInt(5, USERS_PER_PAGE);
            preparedStatement.setInt(6, offset);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                //prepare the user to return
                Profile userProfile = UserDAO.getUserProfile(new User(resultSet.getInt("ID")));

                usersFound.add(new User(resultSet.getInt("ID"),
                        resultSet.getString("Username"),
                        resultSet.getString("Email"),
                        UserType.valueOfLabel(resultSet.getString("Role")),
                        resultSet.getBoolean("Banned"),
                        userProfile));
            }
        } finally {
            cleaningOperations(preparedStatement, resultSet, connection);
        }

        return usersFound;
    }

    /**
     * Return personal information about a specific User
     *
     * @param user      {@code User}
     * @param withImage decide if return also the image
     * @return A {@code Profile} object that contains all the required information
     * @throws SQLException    If SQLException.
     * @throws NamingException If NamingException.
     */
    public static Profile getUserProfile(User user, boolean withImage) throws SQLException, NamingException {
        final String userStatement = withImage ? "Profile.*" : "Profile.ID, Profile.Name, Profile.Surname, Profile.BirthDate, Profile.Gender, Profile.Biography";
        final String STATEMENT = "SELECT " + userStatement + " FROM Profile JOIN Users ON Users.ID = Profile.ID WHERE Profile.ID = ? AND Users.Banned != True";

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Connection connection = null;

        Profile personalInfo = null;

        try {
            connection = DataSourceProvider.getDataSource().getConnection();
            preparedStatement = connection.prepareStatement(STATEMENT);

            preparedStatement.setInt(1, user.getID());

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                //prepare the user to return
                personalInfo = new Profile(resultSet.getString("Name"),
                        resultSet.getString("Surname"),
                        resultSet.getDate("BirthDate"),
                        GenderType.valueOfLabel(resultSet.getString("Gender")),
                        resultSet.getString("Biography"),
                        withImage ? resultSet.getBytes("Profile_Picture") : null);
            }
        } finally {
            cleaningOperations(preparedStatement, resultSet, connection);
        }

        return personalInfo;
    }

    /**
     * Return personal information about a specific User, without image
     *
     * @param user the {@code user}
     * @return a {@code Profile} object that contains all the required information
     * @throws SQLException    if any error occurs while listing Skills.
     * @throws NamingException if any error occurs while connecting to DB.
     */
    public static Profile getUserProfile(User user) throws SQLException, NamingException {
        return getUserProfile(user, false);
    }

    /**
     * register a new user
     *
     * @param user     the {@code user}
     * @param password the new password that a user want
     * @return {@code true} if user registered, {@code false} otherwise
     * @throws SQLException    if any error occurs while listing Skills.
     * @throws NamingException if any error occurs while connecting to DB.
     */
    public static int registerUser(User user, String password) throws SQLException, NamingException {
        final String STATEMENT = "INSERT INTO Users (Username, Email, Password) VALUES (?, LOWER(?), MD5(?))";
        final String STATEMENT_PROFILE = "INSERT INTO Profile (ID, Name, Surname, BirthDate, Gender, Biography) VALUES (?, ?, ?, ?, ?::gendertype, ?)";
        final String STATEMENT_TOPICS = "INSERT INTO Follow (ID_User, ID_Topic) VALUES (?, ?)";
        final String STATEMENT_SKILLS = "INSERT INTO Has (ID_User, ID_Skill, Level) VALUES (?, ?, ?)";

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Connection connection = null;

        int userID = -1;

        try {
            connection = DataSourceProvider.getDataSource().getConnection();
            // prepare the connection and return the generated keys after execution
            preparedStatement = connection.prepareStatement(STATEMENT, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setString(3, PasswordEncryptor.addSalt(password));

            // execute query retrieving the generated keys (ID)
            preparedStatement.executeUpdate();
            resultSet = preparedStatement.getGeneratedKeys();

            if (resultSet.next()) {
                // get the ID of the inserted Idea
                userID = resultSet.getInt(1);
                Profile userProfile = user.getProfile();

                preparedStatement = connection.prepareStatement(STATEMENT_PROFILE);

                preparedStatement.setInt(1, userID);
                preparedStatement.setString(2, userProfile.getName());
                preparedStatement.setString(3, userProfile.getSurname());
                preparedStatement.setDate(4, new Date(userProfile.getBirthDate().getTime()));
                preparedStatement.setString(5, userProfile.getGender().toString());
                preparedStatement.setString(6, userProfile.getBiography());

                preparedStatement.executeUpdate();

                try {
                    if (userProfile.getSkills() != null && userProfile.getSkills().size() > 0) {
                        preparedStatement = connection.prepareStatement(STATEMENT_SKILLS);
                        for (Skill skill : userProfile.getSkills()) {
                            preparedStatement.setInt(1, userID);
                            preparedStatement.setInt(2, skill.getID());
                            preparedStatement.setInt(3, skill.getLevel() != null ? skill.getLevel() : 0);
                            preparedStatement.addBatch();
                        }
                        preparedStatement.executeBatch();
                    }
                    if (userProfile.getTopics() != null && userProfile.getTopics().size() > 0) {
                        preparedStatement = connection.prepareStatement(STATEMENT_TOPICS);
                        for (Topic topic : userProfile.getTopics()) {
                            preparedStatement.setInt(1, userID);
                            preparedStatement.setInt(2, topic.getID());
                            preparedStatement.addBatch();
                        }
                        preparedStatement.executeBatch();
                    }
                } catch (SQLException ignore) {
                }
            }

        } finally {
            cleaningOperations(preparedStatement, resultSet, connection);
        }

        return userID;
    }

    /**
     * update the password of a user
     *
     * @param user        the {@code user}
     * @param password    the new password that a user want
     * @param oldPassword the old password used
     * @return {@code true} if password updated, {@code false} otherwise
     * @throws SQLException    if any error occurs while listing Skills.
     * @throws NamingException if any error occurs while connecting to DB.
     */
    public static boolean updatePassword(User user, String password, String oldPassword) throws SQLException, NamingException {
        final String STATEMENT = "UPDATE Users SET Password = MD5(?) WHERE ID = ? AND Password = MD5(?) AND Banned != True";

        PreparedStatement preparedStatement = null;
        Connection connection = null;

        boolean updated;

        try {
            connection = DataSourceProvider.getDataSource().getConnection();

            preparedStatement = connection.prepareStatement(STATEMENT, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, PasswordEncryptor.addSalt(password));
            preparedStatement.setInt(2, user.getID());
            preparedStatement.setString(3, PasswordEncryptor.addSalt(oldPassword));


            // execute query retrieving the generated keys (ID)
            updated = preparedStatement.executeUpdate() > 0;
        } finally {
            cleaningOperations(preparedStatement, null, connection);
        }

        return updated;
    }

    /**
     * updated the profile of a user
     *
     * @param user the {@code user}
     * @return {@code true} if profile updated, {@code false} otherwise
     * @throws SQLException    if any error occurs while listing Skills.
     * @throws NamingException if any error occurs while connecting to DB.
     */
    public static boolean updateUserProfile(User user) throws SQLException, NamingException {
        //different statement for different type of updates
        final String STATEMENT = "UPDATE Users SET Username = ?, Email = LOWER(?) WHERE ID = ? AND Banned != True";
        final String STATEMENT_PROFILE = "UPDATE Profile SET Name = ?, Surname = ?, BirthDate = ?, Gender = ?::gendertype, Biography = ? WHERE ID = ?";

        PreparedStatement preparedStatement = null;
        Connection connection = null;

        boolean updated;

        try {
            connection = DataSourceProvider.getDataSource().getConnection();

            preparedStatement = connection.prepareStatement(STATEMENT);
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setInt(3, user.getID());

            // execute query retrieving the generated keys (ID)
            updated = preparedStatement.executeUpdate() > 0;

            Profile userProfile = user.getProfile();
            // prepare the connection
            preparedStatement = connection.prepareStatement(STATEMENT_PROFILE);

            preparedStatement.setString(1, userProfile.getName());
            preparedStatement.setString(2, userProfile.getSurname());
            preparedStatement.setDate(3, new Date(userProfile.getBirthDate().getTime()));
            preparedStatement.setString(4, userProfile.getGender().toString());
            preparedStatement.setString(5, userProfile.getBiography());
            preparedStatement.setInt(6, user.getID());

            // execute query retrieving the generated keys (ID)
            updated = preparedStatement.executeUpdate() > 0 || updated;
        } finally {
            cleaningOperations(preparedStatement, null, connection);
        }

        return updated;
    }

    /**
     * updated a profile picture of a user
     *
     * @param user the {@code user}
     * @return {@code true} if picture updated, {@code false} otherwise
     * @throws SQLException    if any error occurs while listing Skills.
     * @throws NamingException if any error occurs while connecting to DB.
     */
    public static boolean updateUserProfilePicture(User user) throws SQLException, NamingException {
        final String STATEMENT = "UPDATE Profile SET Profile_Picture = ? WHERE ID = ?";

        PreparedStatement preparedStatement = null;
        Connection connection = null;

        boolean updated;

        try {
            connection = DataSourceProvider.getDataSource().getConnection();
            // prepare the connection
            preparedStatement = connection.prepareStatement(STATEMENT);

            preparedStatement.setBytes(1, user.getProfile().getProfilePicture());
            preparedStatement.setInt(2, user.getID());

            // execute query retrieving the generated keys (ID)
            updated = preparedStatement.executeUpdate() > 0;
        } finally {
            cleaningOperations(preparedStatement, null, connection);
        }

        return updated;
    }

    /**
     * remove a profile picture of a user
     *
     * @param user the {@code user}
     * @return {@code true} if user deleted, {@code false} otherwise
     * @throws SQLException    if any error occurs while listing Skills.
     * @throws NamingException if any error occurs while connecting to DB.
     */
    public static boolean removeUserProfilePicture(User user) throws SQLException, NamingException {

        final String STATEMENT = "UPDATE Profile SET Profile_Picture = ? WHERE ID = ?";

        PreparedStatement preparedStatement = null;
        Connection connection = null;

        boolean updated;

        try {
            connection = DataSourceProvider.getDataSource().getConnection();
            // prepare the connection
            preparedStatement = connection.prepareStatement(STATEMENT);

            preparedStatement.setBytes(1, null);
            preparedStatement.setInt(2, user.getID());

            // execute query retrieving the generated keys (ID)
            updated = preparedStatement.executeUpdate() > 0;
        } finally {
            cleaningOperations(preparedStatement, null, connection);
        }

        return updated;
    }

    /**
     * delete a user
     *
     * @param user the {@code user}
     * @return {@code true} if user deleted, {@code false} otherwise
     * @throws SQLException    if any error occurs while listing Skills.
     * @throws NamingException if any error occurs while connecting to DB.
     */
    public static boolean deleteUser(User user) throws SQLException, NamingException {
        final String STATEMENT = "DELETE FROM Users WHERE ID = ?";

        PreparedStatement preparedStatement = null;
        Connection connection = null;
        boolean deleted;

        try {
            connection = DataSourceProvider.getDataSource().getConnection();
            // prepare the connection
            preparedStatement = connection.prepareStatement(STATEMENT);
            preparedStatement.setInt(1, user.getID());

            // execute query retrieving the generated keys (ID)
            deleted = preparedStatement.executeUpdate() > 0;

        } finally {
            cleaningOperations(preparedStatement, null, connection);
        }

        return deleted;
    }

    /**
     * list {@code Skill} that a user has
     *
     * @param user the {@code user}
     * @return a list of {@code Skill}
     * @throws SQLException    if any error occurs while listing Skills.
     * @throws NamingException if any error occurs while connecting to DB.
     */
    public static List<Skill> listUserSkills(User user) throws SQLException, NamingException {
        //SQL query
        final String STATEMENT = "SELECT Skill.ID, Skill.Name, Skill.Description, Has.Level " +
                "FROM Skill JOIN Has ON Skill.ID = Has.ID_Skill " +
                "WHERE Has.ID_User = ? " +
                "ORDER BY Skill.Name ASC ";

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Connection connection = null;

        final List<Skill> skillList = new ArrayList<>();

        try {
            connection = DataSourceProvider.getDataSource().getConnection();
            preparedStatement = connection.prepareStatement(STATEMENT);


            preparedStatement.setInt(1, user.getID());

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                skillList.add(new Skill(resultSet.getInt("ID"),
                        resultSet.getString("Name"),
                        resultSet.getString("Description"),
                        resultSet.getInt("Level")));

            }
        } finally {
            cleaningOperations(preparedStatement, resultSet, connection);
        }

        return skillList;
    }

    /**
     * list {@code Topic} that a user follows
     *
     * @param user the {@code user}
     * @return a list of {@code Topic} followed by a user
     * @throws SQLException    if any error occurs while listing Skills.
     * @throws NamingException if any error occurs while connecting to DB.
     */
    public static List<Topic> listUserTopics(User user) throws SQLException, NamingException {
        final String STATEMENT = "SELECT Topic.ID, Topic.Name, Topic.Description " +
                "FROM Topic JOIN Follow ON Topic.ID = Follow.ID_Topic " +
                "WHERE Follow.ID_User = ? " +
                "ORDER BY Topic.Name ASC ";

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Connection connection = null;

        final List<Topic> topics = new ArrayList<>();

        try {
            //prepare the connection
            connection = DataSourceProvider.getDataSource().getConnection();
            preparedStatement = connection.prepareStatement(STATEMENT);
            //put the parameter of the method in the query
            preparedStatement.setInt(1, user.getID());

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                //add the topic to the list
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
     * add a {@code Skill} to the user
     *
     * @param user  the {@code user}
     * @param skill the {@code skill}
     * @return {@code true} if skill added, {@code false} otherwise
     * @throws SQLException    if any error occurs while listing Skills.
     * @throws NamingException if any error occurs while connecting to DB.
     */
    public static boolean addSkillToUser(User user, Skill skill) throws SQLException, NamingException {

        //different statement for different type of updates
        final String STATEMENT = "INSERT INTO Has (ID_User, ID_Skill, Level) VALUES (?, ?, ?)";

        PreparedStatement preparedStatement = null;
        Connection connection = null;

        boolean updated;

        try {
            connection = DataSourceProvider.getDataSource().getConnection();
            // prepare the connection
            preparedStatement = connection.prepareStatement(STATEMENT);

            preparedStatement.setInt(1, user.getID());
            preparedStatement.setInt(2, skill.getID());
            preparedStatement.setInt(3, skill.getLevel());

            // execute query retrieving the generated keys (ID)
            updated = preparedStatement.executeUpdate() > 0;
        } finally {
            cleaningOperations(preparedStatement, null, connection);
        }

        return updated;
    }

    /**
     * add a {@code Skill} to the user
     *
     * @param user  the {@code user}
     * @param skill the {@code skill}
     * @return {@code true} if skill updated, {@code false} otherwise
     * @throws SQLException    if any error occurs while listing Skills.
     * @throws NamingException if any error occurs while connecting to DB.
     */
    public static boolean updateSkillOfUser(User user, Skill skill) throws SQLException, NamingException {
        final String STATEMENT = "UPDATE Has SET Level = ? WHERE ID_User = ? AND ID_Skill = ?";

        PreparedStatement preparedStatement = null;
        Connection connection = null;

        boolean updated;

        try {
            connection = DataSourceProvider.getDataSource().getConnection();
            // prepare the connection
            preparedStatement = connection.prepareStatement(STATEMENT);

            preparedStatement.setInt(1, skill.getLevel());
            preparedStatement.setInt(2, user.getID());
            preparedStatement.setInt(3, skill.getID());

            // execute query retrieving the generated keys (ID)
            updated = preparedStatement.executeUpdate() > 0;
        } finally {
            cleaningOperations(preparedStatement, null, connection);
        }

        return updated;
    }

    /**
     * remove a {@code Skill} from a user
     *
     * @param user  the {@code user}
     * @param skill the {@code skill}
     * @return {@code true} if skill removed, {@code false} otherwise
     * @throws SQLException    if any error occurs while listing Skills.
     * @throws NamingException if any error occurs while connecting to DB.
     */
    public static boolean removeSkillFromUser(User user, Skill skill) throws SQLException, NamingException {
        final String STATEMENT = "DELETE FROM Has WHERE ID_User = ? AND ID_Skill = ?";

        PreparedStatement preparedStatement = null;
        Connection connection = null;

        boolean updated;

        try {
            connection = DataSourceProvider.getDataSource().getConnection();
            // prepare the connection
            preparedStatement = connection.prepareStatement(STATEMENT);

            preparedStatement.setInt(1, user.getID());
            preparedStatement.setInt(2, skill.getID());

            // execute query retrieving the generated keys (ID)
            updated = preparedStatement.executeUpdate() > 0;
        } finally {
            cleaningOperations(preparedStatement, null, connection);
        }

        return updated;
    }

    /**
     * Follow a topic by a specific user
     *
     * @param user  the {@code user}
     * @param topic the {@code topic}
     * @return {@code true} if topic added, {@code false} otherwise
     * @throws SQLException    if any error occurs while listing Skills.
     * @throws NamingException if any error occurs while connecting to DB.
     */
    public static boolean addTopicToUser(User user, Topic topic) throws SQLException, NamingException {
        final String STATEMENT = "INSERT INTO Follow (ID_User, ID_Topic) VALUES (?, ?)";

        PreparedStatement preparedStatement = null;
        Connection connection = null;

        boolean updated;

        try {
            connection = DataSourceProvider.getDataSource().getConnection();
            // prepare the connection
            preparedStatement = connection.prepareStatement(STATEMENT);

            preparedStatement.setInt(1, user.getID());
            preparedStatement.setInt(2, topic.getID());

            // execute query retrieving the generated keys (ID)
            updated = preparedStatement.executeUpdate() > 0;
        } finally {
            cleaningOperations(preparedStatement, null, connection);
        }

        return updated;
    }

    /**
     * Unfollow a topic bu a specific user
     *
     * @param user  the {@code user}
     * @param topic the {@code Topic}
     * @return {@code true} if topic removed, {@code false} otherwise
     * @throws SQLException    if any error occurs while listing Skills.
     * @throws NamingException if any error occurs while connecting to DB.
     */
    public static boolean removeTopicFromUser(User user, Topic topic) throws SQLException, NamingException {
        final String STATEMENT = "DELETE FROM Follow WHERE ID_User = ? AND ID_Topic = ?";

        PreparedStatement preparedStatement = null;
        Connection connection = null;

        boolean updated;

        try {
            connection = DataSourceProvider.getDataSource().getConnection();
            // prepare the connection
            preparedStatement = connection.prepareStatement(STATEMENT);

            preparedStatement.setInt(1, user.getID());
            preparedStatement.setInt(2, topic.getID());

            // execute query retrieving the generated keys (ID)
            updated = preparedStatement.executeUpdate() > 0;
        } finally {
            cleaningOperations(preparedStatement, null, connection);
        }

        return updated;
    }

    /**
     * list all the ideas created by a user
     *
     * @param user the {@code user}
     * @param page the offset of the list
     * @return the list of {@code Idea} created by a user
     * @throws SQLException    if any error occurs while listing Skills.
     * @throws NamingException if any error occurs while connecting to DB.
     */
    public static List<Idea> listUserCreatedIdeas(User user, int page) throws SQLException, NamingException {
        final int offset = IDEAS_PER_PAGE * page;
        final String STATEMENT = "SELECT Users.Username, Users.Email, Users.Role, Users.Banned, Idea.* " +
                "FROM Idea JOIN Users ON Idea.ID_Creator = Users.ID " +
                "WHERE ID_Creator = ? " +
                "AND Users.Banned != True " +
                "ORDER BY Idea.Posted_Time DESC " +
                "LIMIT ? OFFSET ?";

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Connection connection = null;


        final List<Idea> ideas = new ArrayList<>();

        try {
            connection = DataSourceProvider.getDataSource().getConnection();
            preparedStatement = connection.prepareStatement(STATEMENT);

            preparedStatement.setInt(1, user.getID());
            preparedStatement.setInt(2, IDEAS_PER_PAGE);
            preparedStatement.setInt(3, offset);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
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
     * list all the moderators
     *
     * @return the list of all moderators
     * @throws SQLException    if any error occurs while listing Skills.
     * @throws NamingException if any error occurs while connecting to DB.
     */
    public static List<User> listModerators() throws SQLException, NamingException {
        final String STATEMENT = "SELECT * FROM Users WHERE role = 'Moderator'";
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Connection connection = null;

        final List<User> moderators = new ArrayList<>();

        try {
            connection = DataSourceProvider.getDataSource().getConnection();
            preparedStatement = connection.prepareStatement(STATEMENT);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                moderators.add(new User(resultSet.getInt("ID"),
                        resultSet.getString("Username"),
                        resultSet.getString("Email"),
                        UserType.valueOfLabel(resultSet.getString("Role")),
                        resultSet.getBoolean("Banned")));
            }
        } finally {
            cleaningOperations(preparedStatement, resultSet, connection);
        }

        return moderators;
    }

    /**
     * list all the administrators
     *
     * @return the list of all administrators
     * @throws SQLException    if any error occurs while listing Skills.
     * @throws NamingException if any error occurs while connecting to DB.
     */
    public static List<User> listAdministrators() throws SQLException, NamingException {
        final String STATEMENT = "SELECT * FROM Users WHERE role = 'Administrator' ";
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Connection connection = null;

        final List<User> administrators = new ArrayList<>();

        try {
            connection = DataSourceProvider.getDataSource().getConnection();
            preparedStatement = connection.prepareStatement(STATEMENT);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                administrators.add(new User(resultSet.getInt("ID"),
                        resultSet.getString("Username"),
                        resultSet.getString("Email"),
                        UserType.valueOfLabel(resultSet.getString("Role")),
                        resultSet.getBoolean("Banned")));
            }
        } finally {
            cleaningOperations(preparedStatement, resultSet, connection);
        }

        return administrators;
    }

    /**
     * list all the bnned users
     *
     * @return the list of all banned {@code User}
     * @throws SQLException    if any error occurs while listing Skills.
     * @throws NamingException if any error occurs while connecting to DB.
     */
    public static List<User> listBannedUsers() throws SQLException, NamingException {
        final String STATEMENT = "SELECT * FROM Users WHERE banned = 'True' ";
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Connection connection = null;

        final List<User> bannedUsers = new ArrayList<>();

        try {
            connection = DataSourceProvider.getDataSource().getConnection();
            preparedStatement = connection.prepareStatement(STATEMENT);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                bannedUsers.add(new User(resultSet.getInt("ID"),
                        resultSet.getString("Username"),
                        resultSet.getString("Email"),
                        UserType.valueOfLabel(resultSet.getString("Role")),
                        resultSet.getBoolean("Banned")));
            }
        } finally {
            cleaningOperations(preparedStatement, resultSet, connection);
        }

        return bannedUsers;
    }

    /**
     * ban a specific user
     *
     * @param user the {@code User}
     * @return {@code true} if user banned, {@code false} otherwise
     * @throws SQLException    if any error occurs while listing Skills.
     * @throws NamingException if any error occurs while connecting to DB.
     */
    public static boolean banUser(User user) throws SQLException, NamingException {
        String STATEMENT = "UPDATE users " +
                "SET banned = true " +
                "WHERE id = ? AND Role = 'Common User'";

        if (user.getUsername() != null) {
            STATEMENT = "UPDATE users " +
                    "SET banned = true " +
                    "WHERE username = ? AND Role = 'Common User'";
        }

        PreparedStatement preparedStatement = null;
        Connection connection = null;

        boolean updated;

        try {
            connection = DataSourceProvider.getDataSource().getConnection();
            preparedStatement = connection.prepareStatement(STATEMENT);

            if (user.getUsername() != null) {
                preparedStatement.setString(1, user.getUsername());
            } else {
                preparedStatement.setInt(1, user.getID());
            }

            // execute query
            updated = preparedStatement.executeUpdate() > 0;

        } finally {
            cleaningOperations(preparedStatement, null, connection);
        }

        return updated;
    }

    /**
     * readmit a banned user
     *
     * @param user the {@code User}
     * @return {@code true} if user readmitted, {@code false} otherwise
     * @throws SQLException    if any error occurs while listing Skills.
     * @throws NamingException if any error occurs while connecting to DB.
     */
    public static boolean readmitUser(User user) throws SQLException, NamingException {
        final String STATEMENT = "UPDATE users " +
                "SET banned = false " +
                "WHERE id = ?";

        PreparedStatement preparedStatement = null;
        Connection connection = null;

        boolean updated;

        try {
            connection = DataSourceProvider.getDataSource().getConnection();
            preparedStatement = connection.prepareStatement(STATEMENT);

            preparedStatement.setInt(1, user.getID());

            // execute query
            updated = preparedStatement.executeUpdate() > 0;

        } finally {
            cleaningOperations(preparedStatement, null, connection);
        }

        return updated;
    }

    /**
     * promote a user to the role of moderator
     *
     * @param user the {@code User}
     * @return {@code true} if user promoted, {@code false} otherwise
     * @throws SQLException    if any error occurs while listing Skills.
     * @throws NamingException if any error occurs while connecting to DB.
     */
    public static boolean promoteToModerator(User user) throws SQLException, NamingException {
        String STATEMENT = "UPDATE users " +
                "SET role = 'Moderator' " +
                "WHERE id = ? AND role != 'Administrator' AND Banned != True";

        if (user.getUsername() != null) {
            STATEMENT = "UPDATE users " +
                    "SET role = 'Moderator' " +
                    "WHERE username = ? AND role != 'Administrator' AND Banned != True";
        }

        PreparedStatement preparedStatement = null;
        Connection connection = null;

        boolean updated;

        try {
            connection = DataSourceProvider.getDataSource().getConnection();
            preparedStatement = connection.prepareStatement(STATEMENT);

            if (user.getUsername() != null) {
                preparedStatement.setString(1, user.getUsername());
            } else {
                preparedStatement.setInt(1, user.getID());
            }

            // execute query
            updated = preparedStatement.executeUpdate() > 0;

        } finally {
            cleaningOperations(preparedStatement, null, connection);
        }

        return updated;
    }

    /**
     * downgrade a user to the role of common user
     *
     * @param user the {@code User}
     * @return {@code true} if user downgraded, {@code false} otherwise
     * @throws SQLException    if any error occurs while listing Skills.
     * @throws NamingException if any error occurs while connecting to DB.
     */
    public static boolean downgradeToUser(User user) throws SQLException, NamingException {
        final String STATEMENT = "UPDATE users " +
                "SET role = 'Common User' " +
                "WHERE id = ? AND role != 'Administrator'";

        PreparedStatement preparedStatement = null;
        Connection connection = null;

        boolean updated;

        try {
            connection = DataSourceProvider.getDataSource().getConnection();
            preparedStatement = connection.prepareStatement(STATEMENT);

            preparedStatement.setInt(1, user.getID());

            // execute query
            updated = preparedStatement.executeUpdate() > 0;

        } finally {
            cleaningOperations(preparedStatement, null, connection);
        }

        return updated;
    }

    /**
     * Get the numbers of idea created by a user
     *
     * @param user the {@code User}
     * @return the number of ideas that the users has created {@code Topic} object.
     * @throws SQLException    if any error occurs while listing Skills.
     * @throws NamingException if any error occurs while connecting to DB.
     */
    public static int getNumberIdeasOfUser(User user) throws SQLException, NamingException {
        final String STATEMENT = "SELECT COUNT(ID) AS PostNumbers " +
                "FROM Idea " +
                "WHERE ID_Creator = ? ";

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Connection connection = null;

        int counter = 0;

        try {
            connection = DataSourceProvider.getDataSource().getConnection();
            preparedStatement = connection.prepareStatement(STATEMENT);

            preparedStatement.setInt(1, user.getID());
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                counter = resultSet.getInt("PostNumbers");
            }
        } finally {
            cleaningOperations(preparedStatement, resultSet, connection);
        }

        return counter;
    }

    /**
     * Get the total numbers of likes received by a user
     *
     * @param user the {@code User}
     * @return the number of likes received by a user
     * @throws SQLException    if any error occurs while listing Skills.
     * @throws NamingException if any error occurs while connecting to DB.
     */
    public static int getTotalLikesOfUser(User user) throws SQLException, NamingException {
        final String STATEMENT = "SELECT SUM(Num_Likes) AS TotalLikes " +
                "FROM Idea " +
                "WHERE ID_Creator = ?";

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Connection connection = null;

        int likeCounter = 0;

        try {
            connection = DataSourceProvider.getDataSource().getConnection();
            preparedStatement = connection.prepareStatement(STATEMENT);

            preparedStatement.setInt(1, user.getID());
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                likeCounter += resultSet.getInt("TotalLikes");
            }
        } finally {
            cleaningOperations(preparedStatement, resultSet, connection);
        }

        return likeCounter;
    }

    /**
     * create the feed for the user
     *
     * @param user the {@code User}
     * @param page the offset of the feed.
     * @return the list of {@code Idea} to show in the feed
     * @throws SQLException    if any error occurs while listing Skills.
     * @throws NamingException if any error occurs while connecting to DB.
     */
    public static List<Idea> listFeed(User user, int page) throws SQLException, NamingException {
        final int offset = IDEAS_PER_PAGE * page;

        final String STATEMENT = "select distinct creator.Username, creator.Email, creator.Role, creator.Banned," +
                "idea.id, idea.title, idea.description, idea.image, idea.posted_time, idea.num_likes, idea.id_creator \n" +
                "from idea \n" +
                "join users as creator " +
                "on idea.id_creator = creator.id " +
                "left join is_related\n" +
                "on idea.id = is_related.id_idea\n" +
                "left join topic \n" +
                "on is_related.id_topic = topic.id\n" +
                "left join follow\n" +
                "on topic.id = follow.id_topic\n" +
                "left join users \n" +
                "on users.id = follow.id_user \n" +
                "where id_user = ? " +
                "AND creator.Banned != True " +
                "ORDER BY Idea.Posted_Time DESC " +
                "LIMIT ? OFFSET ?";


        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Connection connection = null;

        // the results of the search
        final List<Idea> ideaList = new ArrayList<>();

        try {
            connection = DataSourceProvider.getDataSource().getConnection();
            preparedStatement = connection.prepareStatement(STATEMENT);

            preparedStatement.setInt(1, user.getID());
            preparedStatement.setInt(2, IDEAS_PER_PAGE);
            preparedStatement.setInt(3, offset);
            resultSet = preparedStatement.executeQuery();


            while (resultSet.next()) {
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

                ideaList.add(idea);
            }
        } finally {
            cleaningOperations(preparedStatement, resultSet, connection);
        }

        return ideaList;
    }

    /**
     * list groups in which user is part
     *
     * @param user the {@code User}
     * @return the list of {@code Team} in which user is part
     * @throws SQLException    if any error occurs while listing Skills.
     * @throws NamingException if any error occurs while connecting to DB.
     */
    public static List<Team> listUserGroups(User user) throws SQLException, NamingException {
        final String STATEMENT = "(select distinct Users.Username, Users.Email, Users.Role, Users.Banned,\n" +
                "idea.id, idea.description, idea.title, idea.description as ideaDescription, idea.posted_time, idea.num_likes, idea.id_creator,\n" +
                "team.id as teamID, team.name, team.creation_time, team.accept_requests, group_chat.description as groupDescription\n" +
                "from group_chat\n" +
                "inner join team\n" +
                "on team.id = group_chat.id\n" +
                "inner join idea\n" +
                "on team.id_idea = idea.id\n" +
                "inner join request \n" +
                "on request.id_team = team.id\n" +
                "inner join users \n" +
                "on users.id = idea.id_creator\n" +
                "where status = 'Accepted' and request.id_user = ?)\n" +
                "union\n" +
                "(select distinct Users.Username, Users.Email, Users.Role, Users.Banned,\n" +
                "idea.id, idea.description, idea.title, idea.description as ideaDescription, idea.posted_time, idea.num_likes, idea.id_creator,\n" +
                "team.id as teamID, team.name, team.creation_time, team.accept_requests, group_chat.description as groupDescription\n" +
                "from group_chat\n" +
                "inner join team on team.id = group_chat.id\n" +
                "inner join idea on team.id_idea = idea.id\n" +
                "inner join users on users.id = idea.id_creator\n" +
                "inner join request on request.id_team = team.id\n" +
                "where idea.id_creator = ? and request.status = 'Accepted')\n" +
                "order by name asc";


        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Connection connection = null;

        final List<Team> groupList = new ArrayList<>();

        try {
            connection = DataSourceProvider.getDataSource().getConnection();
            preparedStatement = connection.prepareStatement(STATEMENT);

            preparedStatement.setInt(1, user.getID());
            preparedStatement.setInt(2, user.getID());
            resultSet = preparedStatement.executeQuery();


            while (resultSet.next()) {
                User ideaUser = new User(resultSet.getInt("ID_Creator"),
                        resultSet.getString("Username"),
                        resultSet.getString("Email"),
                        UserType.valueOfLabel(resultSet.getString("Role")),
                        resultSet.getBoolean("Banned"));

                Idea ideaGroup = new Idea(resultSet.getInt("id"),
                        resultSet.getString("Title"),
                        resultSet.getString("ideaDescription"),
                        null,
                        resultSet.getTimestamp("Posted_Time"),
                        resultSet.getInt("Num_Likes"),
                        ideaUser);

                Team group = new Team(resultSet.getInt("teamID"),
                        resultSet.getString("name"),
                        resultSet.getTimestamp("creation_time"),
                        ideaGroup,
                        resultSet.getBoolean("accept_requests"),
                        null,
                        resultSet.getString("groupDescription"));


                groupList.add(group);
            }
        } finally {
            cleaningOperations(preparedStatement, resultSet, connection);
        }

        return groupList;
    }

    /**
     * list the pending request for a specific user
     *
     * @param user the {@code User}
     * @return the list of {@code JoinRequest} pending for the user
     * @throws SQLException    if any error occurs while listing Skills.
     * @throws NamingException if any error occurs while connecting to DB.
     */
    public static List<JoinRequest> listPendingRequests(User user) throws SQLException, NamingException {
        final String STATEMENT = "select team.id as teamID, team.name, team.creation_time, team.accept_requests, group_chat.image as imageGROUP, group_chat.description as descriptionGROUP,\n" +
                "idea.id as ideaID, idea.description as descriptionIDEA, idea.title, idea.image as imageIDEA, idea.posted_time, idea.num_likes, idea.id_creator," +
                "creator.Username as creatorUsername, creator.Email as creatorEmail, creator.Role as creatorRole, creator.Banned as creatorBanned,\n" +
                "requester.Username as requesterUsername, requester.Email as requesterEmail, requester.Role as requesterRole, requester.Banned as requesterBanned,\n" +
                "request.status, request.message, request.requested_time, request.id_user\n" +
                "from request\n" +
                "inner join team\n" +
                "on request.id_team = team.id\n" +
                "inner join users as requester\n" +
                "on requester.id = request.id_user\n" +
                "inner join idea\n" +
                "on idea.id = team.id_idea\n" +
                "inner join users creator\n" +
                "on creator.id = idea.id_creator\n" +
                "inner join group_chat\n" +
                "on group_chat.id = team.id\n" +
                "where idea.id_creator = ?\n" +
                "AND requester.Banned != True " +
                "and status = 'Pending' " +
                "order by request.requested_time asc";


        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Connection connection = null;

        // the results of the search
        final List<JoinRequest> requestList = new ArrayList<>();

        try {
            connection = DataSourceProvider.getDataSource().getConnection();
            preparedStatement = connection.prepareStatement(STATEMENT);

            preparedStatement.setInt(1, user.getID());
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                User ideaCreator = new User(resultSet.getInt("ID_Creator"),
                        resultSet.getString("creatorUsername"),
                        resultSet.getString("creatorEmail"),
                        UserType.valueOfLabel(resultSet.getString("creatorRole")),
                        resultSet.getBoolean("creatorBanned"));

                Idea ideaRequest = new Idea(resultSet.getInt("ideaID"),
                        resultSet.getString("Title"),
                        resultSet.getString("descriptionIDEA"),
                        null,
                        resultSet.getTimestamp("Posted_Time"),
                        resultSet.getInt("Num_Likes"),
                        ideaCreator);

                Team groupRequest = new Team(resultSet.getInt("teamID"),
                        resultSet.getString("name"),
                        resultSet.getTimestamp("creation_time"),
                        ideaRequest,
                        resultSet.getBoolean("accept_requests"),
                        null,
                        resultSet.getString("descriptionGROUP"));

                User userRequest = new User(resultSet.getInt("id_user"),
                        resultSet.getString("requesterUsername"),
                        resultSet.getString("requesterEmail"),
                        UserType.valueOfLabel(resultSet.getString("requesterRole")),
                        resultSet.getBoolean("requesterBanned"));

                JoinRequest request = new JoinRequest(groupRequest
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
