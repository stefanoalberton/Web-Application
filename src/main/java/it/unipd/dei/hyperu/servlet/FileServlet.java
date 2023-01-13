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

import it.unipd.dei.hyperu.database.MessageDAO;
import it.unipd.dei.hyperu.resource.*;

import java.io.*;
import java.sql.SQLException;

import javax.naming.NamingException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.unipd.dei.hyperu.utils.ErrorCode;

/**
 * Get images from idea and profiles.
 *
 * @author Luca Martinelli (luca.martinelli.1@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public final class FileServlet extends AbstractServlet {

    /**
     * Get images from idea and profiles.
     *
     * @param req the HTTP request from the client.
     * @param res the HTTP response from the server.
     * @throws IOException if any error occurs in the client/server communication.
     */
    public void doGet(HttpServletRequest req, HttpServletResponse res)
            throws IOException {
        String[] tokens = getTokens(req, "file");

        String imageTable = tokens[0];

        if ("message".equals(imageTable)) {
            writeMessageFile(req, res, tokens);
        } else {
            writeResult(res, new ResultMessage(ErrorCode.OPERATION_UNKNOWN));
        }
    }

    private void writeMessageFile(HttpServletRequest req, HttpServletResponse res, String[] tokens) throws IOException {
        Message message;
        ResultMessage resultMessage = null;

        try {
            int messageID = Integer.parseInt(tokens[1]);

            message = MessageDAO.getMessageByID(messageID);

            if (message != null) {
                byte[] file = message.getFile();
                FileInfo fileInfo = message.getFileInfo();

                if (file != null && fileInfo != null) {
                    writeFile(res, file, fileInfo);
                } else {
                    resultMessage = new ResultMessage(ErrorCode.MESSAGE_NOT_FILE);
                }
            } else {
                resultMessage = new ResultMessage(ErrorCode.NO_MESSAGE_FOUND);
            }
        } catch (SQLException | NamingException ex) {
            resultMessage = new ResultMessage(ErrorCode.CANNOT_ACCESS_DATABASE, ex.getMessage());
        } catch (NumberFormatException | IndexOutOfBoundsException ex) {
            resultMessage = new ResultMessage(ErrorCode.BAD_INPUT, ex.getMessage());
        }

        if (resultMessage != null && resultMessage.isError()) {
            writeResult(res, resultMessage);
        }
    }

    private void writeFile(HttpServletResponse res, byte[] file, FileInfo fileInfo) throws IOException {
        res.setContentType(fileInfo.getFileType());
        res.setHeader("Content-Disposition", "attachment; filename=\"" + fileInfo.getFileName() + "\"");
        res.setContentLength((int) fileInfo.getFileSize());

        ServletOutputStream outputStream = res.getOutputStream();
        outputStream.write(file);

        outputStream.flush();
        outputStream.close();
    }

}