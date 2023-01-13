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
import it.unipd.dei.hyperu.utils.DataSourceProvider;

import javax.naming.NamingException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Database Access Object for the skills.
 *
 * @author Luca Martinelli (luca.martinelli.1@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public final class SkillDAO extends AbstractDAO {

    /**
     * List all the skills from database
     *
     * @return a list of {@code Skill} object.
     * @throws SQLException    if any error occurs while listing Skills.
     * @throws NamingException if any error occurs while connecting to DB.
     */
    public static List<Skill> listSkills() throws SQLException, NamingException {
        //SQL query
        final String STATEMENT = "SELECT * FROM Skill ORDER BY Name ASC";
        PreparedStatement pstmt = null;
        ResultSet resultSet = null;
        Connection connection = null;

        final List<Skill> skills = new ArrayList<>();

        try {
            //prepare the connection
            connection = DataSourceProvider.getDataSource().getConnection();
            pstmt = connection.prepareStatement(STATEMENT);
            //execute the query
            resultSet = pstmt.executeQuery();

            while (resultSet.next()) {
                //add the skill to the list
                skills.add(new Skill(resultSet.getInt("ID"),
                        resultSet.getString("Name"),
                        resultSet.getString("Description")));
            }
        } finally {
            cleaningOperations(pstmt, resultSet, connection);
        }

        return skills;
    }

    /**
     * Get skill by ID
     *
     * @param skillID the id of the skill
     * @return a {@code Skill} object.
     * @throws SQLException    if any error occurs while listing Skills.
     * @throws NamingException if any error occurs while connecting to DB.
     */
    public static Skill getSkillByID(int skillID) throws SQLException, NamingException {
        //SQL query
        final String STATEMENT = "SELECT * FROM Skill WHERE ID = ?";
        PreparedStatement pstmt = null;
        ResultSet resultSet = null;
        Connection connection = null;

        try {
            //prepare the connection
            connection = DataSourceProvider.getDataSource().getConnection();
            pstmt = connection.prepareStatement(STATEMENT);
            pstmt.setInt(1, skillID);
            //execute the query
            resultSet = pstmt.executeQuery();

            if (resultSet.next()) {
                //add the skill to the list
                return new Skill(resultSet.getInt("ID"),
                        resultSet.getString("Name"),
                        resultSet.getString("Description"));
            }
        } finally {
            cleaningOperations(pstmt, resultSet, connection);
        }

        return null;
    }

    /**
     * Add a new skill in the DB
     *
     * @param skill the {@code Skill} to be added.
     * @return the ID of {@code Skill} created.
     * @throws SQLException    if any error occurs while listing Skills.
     * @throws NamingException if any error occurs while connecting to DB.
     */
    public static int createSkill(Skill skill) throws SQLException, NamingException {
        //SQL query
        final String STATEMENT = "INSERT INTO Skill (Name,description) VALUES (?,?)";
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Connection connection = null;

        int skillID = -1;

        try {
            connection = DataSourceProvider.getDataSource().getConnection();
            // prepare the connection and return the generated keys after execution
            preparedStatement = connection.prepareStatement(STATEMENT, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, skill.getName());
            preparedStatement.setString(2, skill.getDescription());

            // execute query retrieving the generated keys (ID)
            preparedStatement.executeUpdate();
            resultSet = preparedStatement.getGeneratedKeys();

            if (resultSet.next()) {
                // get the ID of the inserted message
                skillID = resultSet.getInt(1);
            }
        } finally {
            cleaningOperations(preparedStatement, resultSet, connection);
        }
        return skillID;
    }

    /**
     * Delete a new skill in the DB
     *
     * @param skill the {@code Skill} to remove.
     * @return {@code true} if skill deleted, {@code false} otherwise.
     * @throws SQLException    if any error occurs while listing Skills.
     * @throws NamingException if any error occurs while connecting to DB.
     */
    public static boolean deleteSkill(Skill skill) throws SQLException, NamingException {
        //SQL query
        final String STATEMENT = "DELETE FROM Skill WHERE ID = ? ";
        PreparedStatement preparedStatement = null;
        boolean updated;
        Connection connection = null;

        try {
            connection = DataSourceProvider.getDataSource().getConnection();
            // prepare the connection and return the generated keys after execution
            preparedStatement = connection.prepareStatement(STATEMENT);
            preparedStatement.setInt(1, skill.getID());

            // execute query retrieving the number of rows deleted
            updated = preparedStatement.executeUpdate() > 0;

        } finally {
            cleaningOperations(preparedStatement, null, connection);
        }
        return updated;
    }

    /**
     * Edit a skill.
     *
     * @param skill the {@code Skill} to be edited.
     * @return {@code true} if updated, {@code false} otherwise.
     * @throws SQLException    if any error occurs while listing Skills.
     * @throws NamingException if any error occurs while connecting to DB.
     */
    public static boolean updateSkill(Skill skill) throws SQLException, NamingException {
        //SQL query
        final String STATEMENT = "UPDATE Skill SET Name = ?, Description = ? WHERE ID = ?";
        PreparedStatement preparedStatement = null;
        boolean updated;
        Connection connection = null;

        try {
            connection = DataSourceProvider.getDataSource().getConnection();
            // prepare the connection and return the generated keys after execution
            preparedStatement = connection.prepareStatement(STATEMENT);
            preparedStatement.setString(1, skill.getName());
            preparedStatement.setString(2, skill.getDescription());
            preparedStatement.setInt(3, skill.getID());

            // execute query retrieving the number of rows deleted
            updated = preparedStatement.executeUpdate() > 0;

        } finally {
            cleaningOperations(preparedStatement, null, connection);
        }
        return updated;
    }
}
