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

package it.unipd.dei.hyperu.utils;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 * Gets the {@code DataSource} for managing pool connection to database.
 *
 * @author Luca Martinelli (luca.martinelli.1@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public class DataSourceProvider {

    /**
     * The {@code DataSource} for managing connections.
     */
    private static DataSource dataSource = null;

    /**
     * Initialize the connection to the database.
     *
     * @return The {@code DataSource} to manage pool connection to DB.
     * @throws NamingException If NamingException
     */
    public static synchronized DataSource getDataSource() throws NamingException {

        if (dataSource == null) {
            InitialContext ctx = new InitialContext();

            dataSource = (DataSource) ctx.lookup("java:/comp/env/jdbc/hyperu-db");
        }
        return dataSource;
    }
}
