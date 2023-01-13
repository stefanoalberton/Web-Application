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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Abstract class for cleaning the connection to database.
 *
 * @author Luca Martinelli (luca.martinelli.1@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public abstract class AbstractDAO {

    protected final static int MESSAGES_PER_PAGE = 40;
    protected final static int IDEAS_PER_PAGE = 20;
    protected final static int USERS_PER_PAGE = 20;
    protected final static int COMMENTS_PER_PAGE = 30;

    /**
     * @param preparedStatement The {@code PreparedStatement} of the query
     * @param result            The {@code ResultSet} of the query
     * @param conn              The {@code Connection} of the query
     * @throws SQLException If SQLException
     */
    static void cleaningOperations(PreparedStatement preparedStatement, ResultSet result, Connection conn) throws SQLException {
        if (preparedStatement != null) {
            preparedStatement.close();
        }
        if (result != null) {
            result.close();
        }
        if (conn != null) {
            conn.close();
        }
    }

}
