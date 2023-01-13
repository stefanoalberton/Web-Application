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

package it.unipd.dei.hyperu.filter;

import it.unipd.dei.hyperu.resource.User;
import it.unipd.dei.hyperu.utils.ErrorCode;
import it.unipd.dei.hyperu.resource.ResultMessage;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Luca Martinelli (luca.martinelli.1@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public class LoginFilter extends AbstractFilter {

    /**
     * Check the filter
     *
     * @param req   the HTTP request from the client.
     * @param res   the HTTP response from the server.
     * @param chain the filter chain.
     * @throws IOException if any error occurs in the client/server communication.
     */
    public void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        String[] whiteList = {"/api/user/login", "/api/user/token", "/api/user/register"};

        for (String whitePath : whiteList) {
            if (req.getRequestURI().startsWith(req.getContextPath() + whitePath)) {
                chain.doFilter(req, res);
                return;
            }
        }

        User loggedUser = getLoggedUser(req);

        if (loggedUser != null && !loggedUser.isBanned()) {
            chain.doFilter(req, res); // User is logged in, just continue request.
        } else {
            sendError(res, new ResultMessage(ErrorCode.USER_NOT_LOGGED));
        }
    }

}
