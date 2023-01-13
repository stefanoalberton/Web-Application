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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureAlgorithm;
import it.unipd.dei.hyperu.resource.ResultMessage;
import it.unipd.dei.hyperu.resource.User;
import org.apache.logging.log4j.Logger;

import io.jsonwebtoken.Jwts;

public class ServletFunctions {

    private final static String SECRET_KEY = "NNvT7mdEcqINbixruh2H0yZCCUQ/iVtjFn0ESZUY7lk=";

    /**
     * Print a JSON result of the operations made by the servlet.
     *
     * @param message the message from the servlet.
     * @param data    if any, the data of the response
     * @throws IOException if any error occurs in the client/server communication.
     */
    public static void writeResult(HttpServletResponse res, Logger logger, ResultMessage message, Object data) throws IOException {
        // write the JSON page
        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");

        // get a stream to write the response
        PrintWriter out = res.getWriter();

        // create gson object
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();

        // If no message, send a general result success
        if (message == null) {
            message = new ResultMessage(InfoMessage.GENERAL_SUCCESS);
        }

        // add message to JSON
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("message", gson.toJsonTree(message));

        // if there's, add data to JSON
        if (data != null && !(message.isError() && data.equals(-1))) {
            if (data.getClass().equals(JsonObject.class)) {
                jsonObject.add("data", (JsonObject) data);
            } else {
                jsonObject.add("data", gson.toJsonTree(data));
            }
        }

        if (message.isError()) {
            res.setStatus(message.getHttpErrorCode());
            if (message.getMessage() != null) logger.error(message.getMessage());
            if (message.getErrorDetails() != null) logger.error(message.getErrorDetails());
        } else {
            if (message.getMessage() != null) logger.info(message.getMessage());
        }

        // print JSON result
        out.print(jsonObject);

        // flush the output stream buffer
        out.flush();

        // close the output stream
        out.close();
    }

    public static String[] getTokens(HttpServletRequest req, String path) {
        String requestURI = req.getRequestURI();
        String[] tokens = null;

        try {
            tokens = requestURI.substring(requestURI.indexOf(path) + path.length() + 1).split("/");
            if (tokens[0].isBlank()) {
                tokens = new String[]{};
            }
        } catch (IndexOutOfBoundsException ignored) {
        }

        return tokens;
    }

    public static <T> T getJSON(HttpServletRequest req, Class<T> classOfT) throws IOException {
        return new Gson().fromJson(new InputStreamReader(req.getInputStream(), StandardCharsets.UTF_8), classOfT);
    }

    public static User getLoggedUser(HttpServletRequest req) {
        try {
            String authTokenHeader = req.getHeader("Authorization");
            String authToken = null;

            if (authTokenHeader.startsWith("Bearer")) {
                authToken = authTokenHeader.substring(authTokenHeader.indexOf("Bearer") + 7);
            }

            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(authToken).getBody();
            int userID = claims.get("id", Integer.class);
            UserType role = UserType.valueOfLabel(claims.get("role", String.class));
            boolean banned = claims.get("banned", Boolean.class);

            return new User(userID, null, null, role, banned);
        } catch (Throwable ignore) {
        }

        HttpSession session = req.getSession(false);
        return session != null && session.getAttribute("loggedUser") != null ? (User) session.getAttribute("loggedUser") : null;
    }

    public static String getJWTUser(User user) {
        long now = (new Date()).getTime();
        Date validity;
        validity = new Date(now + 1000 * 60 * 60 * 3);

        return Jwts.builder().setSubject(user.getUsername())
                .claim("id", user.getID())
                .claim("role", user.getRole().toString())
                .claim("banned", user.isBanned())
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .setExpiration(validity).compact();
    }

    public static void setLoggedUser(HttpServletRequest req, User user) {
        HttpSession session = req.getSession();
        session.setAttribute("loggedUser", user);
    }
}
