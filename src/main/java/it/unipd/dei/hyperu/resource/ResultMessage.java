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

package it.unipd.dei.hyperu.resource;

import it.unipd.dei.hyperu.utils.ErrorCode;
import it.unipd.dei.hyperu.utils.InfoMessage;

/**
 * Represents a message or an error message.
 *
 * @author Luca Martinelli (luca.martinelli.1@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public class ResultMessage {

    /**
     * The message
     */
    private final String message;

    /**
     * The code of the error, if any
     */
    private final Integer errorCode;

    /**
     * The HTTP code of the error, if any
     */
    private transient final Integer httpErrorCode;

    /**
     * Additional details about the error, if any
     */
    private final transient String errorDetails;

    /**
     * Indicates whether the message is about an error or not.
     */
    private final boolean isError;

    /**
     * Creates an error message.
     *
     * @param message       the message.
     * @param errorCode     the code of the error.
     * @param httpErrorCode the HTTP code of the error.
     * @param errorDetails  additional details about the error.
     */
    public ResultMessage(final String message, final int errorCode, final int httpErrorCode, final String errorDetails) {
        this.message = message;
        this.errorCode = errorCode;
        this.httpErrorCode = httpErrorCode;
        this.errorDetails = errorDetails;
        this.isError = true;
    }

    /**
     * Creates an error message.
     *
     * @param errorCode    the {@code ErrorCode}.
     * @param errorDetails additional details about the error.
     */
    public ResultMessage(final ErrorCode errorCode, final String errorDetails) {
        this.message = errorCode.getErrorMessage();
        this.errorCode = errorCode.getErrorCode();
        this.httpErrorCode = errorCode.getHTTPCode();
        this.errorDetails = errorDetails;
        this.isError = true;
    }

    /**
     * Creates an error message.
     *
     * @param errorCode the {@code ErrorCode}.
     */
    public ResultMessage(final ErrorCode errorCode) {
        this.message = errorCode.getErrorMessage();
        this.errorCode = errorCode.getErrorCode();
        this.httpErrorCode = errorCode.getHTTPCode();
        this.errorDetails = "";
        this.isError = true;
    }

    /**
     * Creates a generic message.
     *
     * @param message the message.
     */
    public ResultMessage(final String message) {
        this.message = message;
        this.errorCode = null;
        this.httpErrorCode = null;
        this.errorDetails = null;
        this.isError = false;
    }

    /**
     * Creates a generic message.
     *
     * @param message a {@code InfoMessage} object.
     */
    public ResultMessage(final InfoMessage message) {
        this.message = message.getInfoMessage();
        this.errorCode = null;
        this.httpErrorCode = null;
        this.errorDetails = null;
        this.isError = false;
    }

    /**
     * Returns the message.
     *
     * @return the message.
     */
    public final String getMessage() {
        return message;
    }

    /**
     * Returns the code of the error, if any.
     *
     * @return the code of the error, if any, {@code 0} otherwise.
     */
    public final int getErrorCode() {
        return errorCode;
    }

    /**
     * Returns additional details about the error, if any.
     *
     * @return additional details about the error, if any, {@code null} otherwise.
     */
    public final String getErrorDetails() {
        return errorDetails;
    }

    /**
     * Returns the code of the error, if any.
     *
     * @return the HTTP code of the error, if any, {@code 0} otherwise.
     */
    public final int getHttpErrorCode() {
        return httpErrorCode;
    }

    /**
     * Indicates whether the message is about an error or not.
     *
     * @return {@code true} is the message is about an error, {@code false} otherwise.
     */
    public final boolean isError() {
        return isError;
    }

}