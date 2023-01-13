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

package it.unipd.dei.hyperu.rest;

import it.unipd.dei.hyperu.resource.User;
import it.unipd.dei.hyperu.resource.ResultMessage;
import it.unipd.dei.hyperu.utils.ServletFunctions;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;

/**
 * Gets the {@code Logger} for managing log.
 *
 * @author Luca Martinelli (luca.martinelli.1@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public abstract class AbstractRestResource {

    protected final HttpServletRequest req;
    protected final HttpServletResponse res;
    protected final Logger logger;

    protected final static long MAX_FILE_UPLOAD = 200 * 1024 * 1024;
    protected final static long MAX_IMAGE_UPLOAD = 5 * 1024 * 1024;
    protected final static String DUPLICATE_ERROR_CODE = "23505";
    protected final static HashSet<String> IMAGE_SUPPORTED_TYPES = new HashSet<>();

    public AbstractRestResource(HttpServletRequest req, HttpServletResponse res) {
        this.req = req;
        this.res = res;

        IMAGE_SUPPORTED_TYPES.add("image/jpeg");
        IMAGE_SUPPORTED_TYPES.add("image/png");

        this.logger = LogManager.getLogger(this.getClass());
    }

    protected void writeResult(ResultMessage message, Object data) throws IOException {
        ServletFunctions.writeResult(res, logger, message, data);
    }

    protected void writeResult(ResultMessage message) throws IOException {
        ServletFunctions.writeResult(res, logger, message, null);
    }

    protected String[] getTokens(String path) {
        return ServletFunctions.getTokens(req, path);
    }

    protected <T> T getJSON(Class<T> classOfT) throws IOException {
        return ServletFunctions.getJSON(req, classOfT);
    }

    protected User getLoggedUser() {
        return ServletFunctions.getLoggedUser(req);
    }

    protected void setLoggedUser(User user) {
        ServletFunctions.setLoggedUser(req, user);
    }
}