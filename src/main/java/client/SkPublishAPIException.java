/*
 * Copyright (c) 2012, IDM
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted
 * provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright notice, this list of
 *       conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright notice, this list
 *       of conditions and the following disclaimer in the documentation and/or other materials
 *       provided with the distribution.
 *     * Neither the name of the IDM nor the names of its contributors may be used to endorse or
 *       promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN
 * ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 */

package client;

/**
 * Exception class for SkPublish API.
 *
 * @author Arnaud de Bossoreille
 */
public class SkPublishAPIException extends Exception {

    private static final long serialVersionUID = 1L;

    private final String response;

    private final int statusCode;

    /**
     * Creates a new exception.
     *
     * @param statusCode
     * @param response
     */
    public SkPublishAPIException(int statusCode, String response) {
        super();
        this.response = response;
        this.statusCode = statusCode;
    }

    /**
     * Creates a new exception.
     *
     * @param message
     * @param statusCode
     * @param response
     */
    public SkPublishAPIException(String message, int statusCode, String response) {
        super(message);
        this.response = response;
        this.statusCode = statusCode;
    }

    /**
     * Creates a new exception.
     *
     * @param message
     * @param cause
     * @param statusCode
     * @param response
     */
    public SkPublishAPIException(String message, Throwable cause, int statusCode, String response) {
        super(message, cause);
        this.response = response;
        this.statusCode = statusCode;
    }

    /**
     * Creates a new exception.
     *
     * @param cause
     * @param statusCode
     * @param response
     */
    public SkPublishAPIException(Throwable cause, int statusCode, String response) {
        super(cause);
        this.response = response;
        this.statusCode = statusCode;
    }

    /**
     * Gets the API response.
     *
     * @return the response.
     */
    public String getResponse() {
        return response;
    }

    /**
     * Gets the HTTP status code.
     *
     * @return the HTTP status code.
     */
    public int getStatusCode() {
        return statusCode;
    }

    @Override
    public String toString() {
        return super.toString() + "\nstatusCode: " + statusCode + "\nresponse: " + response;
    }

}
