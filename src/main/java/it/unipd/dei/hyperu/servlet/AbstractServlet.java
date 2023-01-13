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

package it.unipd.dei.hyperu.servlet;

import it.unipd.dei.hyperu.resource.User;
import it.unipd.dei.hyperu.resource.ResultMessage;
import it.unipd.dei.hyperu.utils.ServletFunctions;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Gets the {@code Logger} for managing log.
 *
 * @author Luca Martinelli (luca.martinelli.1@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public abstract class AbstractServlet extends HttpServlet {

    /**
     * The logger.
     */
    Logger logger;

    /**
     * Gets the {@code Logger} for managing log.
     *
     * @param config a {@code ServletConfig} object containing the servlet's
     *               configuration and initialization parameters.
     * @throws ServletException if an exception has occurred that interferes with the servlet's normal operation
     */
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        logger = LogManager.getLogger(this.getClass());
    }

    protected void writeResult(HttpServletResponse res, ResultMessage message, Object data) throws IOException {
        ServletFunctions.writeResult(res, logger, message, data);
    }

    protected void writeResult(HttpServletResponse res, ResultMessage message) throws IOException {
        ServletFunctions.writeResult(res, logger, message, null);
    }

    protected String[] getTokens(HttpServletRequest req, String path) {
        return ServletFunctions.getTokens(req, path);
    }

    protected User getLoggedUser(HttpServletRequest req) {
        return ServletFunctions.getLoggedUser(req);
    }

}