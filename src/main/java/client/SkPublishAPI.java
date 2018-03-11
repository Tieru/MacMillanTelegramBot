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

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.*;
import java.net.URLEncoder;

/**
 * Light implementation of SkPublish API client.
 *
 * @author Arnaud de Bossoreille
 */
public class SkPublishAPI {

    /**
     * Interface used to customize API requests.
     *
     * @author Arnaud de Bossoreille
     */
    public interface RequestHandler {

        /**
         * Preparation hook for HTTP GET requests.
         *
         * It allows setting custom headers such as "Accept: application/xml" to retrieve XML
         * instead of JSON.
         *
         * @param request
         *            the HTTP request.
         */
        public void prepareGetRequest(HttpGet request);

    }

    private static final int BUFFER_SIZE = 4096;

    private String accessKey;

    private String baseUrl;

    private HttpClient httpClient;

    private RequestHandler requestHandler;

    /**
     * Creates a new API client with preconfigured URL and access key.
     *
     * @param baseUrl
     *            the root URL of the SkPublish API.
     * @param accessKey
     *            the access key to use.
     */
    public SkPublishAPI(String baseUrl, String accessKey) {
        setBaseUrl(baseUrl);
        setAccessKey(accessKey);
        setHttpClient(new DefaultHttpClient());
    }

    /**
     * Make a spell checker search
     *
     * @param dictionaryCode
     *            the code of the dictionary.
     * @param searchWord
     *            the searched word
     * @param entryNumber
     *            the number of results
     * @return a list of suggestions.
     * @throws SkPublishAPIException
     */
    public String didYouMean(String dictionaryCode, String searchWord, Integer entryNumber)
            throws SkPublishAPIException {
        StringBuilder uri = new StringBuilder(baseUrl);
        if (!isValidDictionaryCode(dictionaryCode))
            throw new IllegalArgumentException(dictionaryCode);
        uri.append("dictionaries/");
        uri.append(dictionaryCode);
        uri.append("/search/didyoumean?q=");
        try {
            uri.append(URLEncoder.encode(searchWord, "UTF-8"));
        } catch (UnsupportedEncodingException e1) {
        }
        char c = '&';
        if (entryNumber != null) {
            uri.append(c);
            uri.append("entrynumber=");
            uri.append(entryNumber);
        }
        HttpGet request = prepareGetRequest(uri.toString());
        HttpResponse response = null;
        try {
            response = httpClient.execute(request);
            String res = readResponse(response, true);
            return res;
        } catch (IOException e) {
            throw new SkPublishAPIException(e, -1, readResponse(response, false));
        }
    }

    /**
     * Gets the access key.
     *
     * @return the access key.
     */
    public String getAccessKey() {
        return accessKey;
    }

    /**
     * Gets the API root URL.
     *
     * @return the URL including the trailing '/'.
     */
    public String getBaseUrl() {
        return baseUrl;
    }

    /**
     * Gets the list of dictionaries.
     *
     * @return the list of dictionaries.
     * @throws SkPublishAPIException
     */
    public String getDictionaries() throws SkPublishAPIException {
        HttpGet request = prepareGetRequest(baseUrl + "dictionaries");
        HttpResponse response = null;
        try {
            response = httpClient.execute(request);
            String res = readResponse(response, true);
            return res;
        } catch (IOException e) {
            throw new SkPublishAPIException(e, -1, readResponse(response, false));
        }
    }

    /**
     * Gets a dictionary.
     *
     * @param dictionaryCode
     *            the code of the dictionary.
     * @return the dictionary.
     * @throws SkPublishAPIException
     */
    public String getDictionary(String dictionaryCode) throws SkPublishAPIException {
        if (!isValidDictionaryCode(dictionaryCode))
            throw new IllegalArgumentException(dictionaryCode);
        HttpGet request = prepareGetRequest(baseUrl + "dictionaries/" + dictionaryCode);
        HttpResponse response = null;
        try {
            response = httpClient.execute(request);
            String res = readResponse(response, true);
            return res;
        } catch (IOException e) {
            throw new SkPublishAPIException(e, -1, readResponse(response, false));
        }
    }

    /**
     * Gets an entry.
     *
     * @param dictionaryCode
     *            the code of the dictionary.
     * @param entryId
     *            the id of the entry.
     * @param format
     *            the format of the entry, eitheir "html" or "xml".
     * @return the entry.
     * @throws SkPublishAPIException
     */
    public String getEntry(String dictionaryCode, String entryId, String format)
            throws SkPublishAPIException {
        StringBuilder uri = new StringBuilder(baseUrl);
        if (!isValidDictionaryCode(dictionaryCode))
            throw new IllegalArgumentException(dictionaryCode);
        uri.append("dictionaries/");
        uri.append(dictionaryCode);
        uri.append("/entries/");
        try {
            uri.append(URLEncoder.encode(entryId, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
        }
        char c = '?';
        if (format != null) {
            if (!isValidEntryFormat(format))
                throw new IllegalArgumentException(format);
            uri.append(c);
            uri.append("format=");
            uri.append(format);
            c = '&';
        }
        HttpGet request = prepareGetRequest(uri.toString());
        HttpResponse response = null;
        try {
            response = httpClient.execute(request);
            String res = readResponse(response, true);
            return res;
        } catch (IOException e) {
            throw new SkPublishAPIException(e, -1, readResponse(response, false));
        }
    }

    /**
     * Gets the list of pronunciations of an entry.
     *
     * @param dictionaryCode
     *            the code of the dictionary.
     * @param entryId
     *            the id of the entry.
     * @param lang
     *            the lang of the pronunciation.
     * @return a list of pronunciations.
     * @throws SkPublishAPIException
     */
    public String getEntryPronunciations(String dictionaryCode, String entryId, String lang)
            throws SkPublishAPIException {
        StringBuilder uri = new StringBuilder(baseUrl);
        if (!isValidDictionaryCode(dictionaryCode))
            throw new IllegalArgumentException(dictionaryCode);
        uri.append("dictionaries/");
        uri.append(dictionaryCode);
        uri.append("/entries/");
        try {
            uri.append(URLEncoder.encode(entryId, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
        }
        uri.append("/pronunciations");
        char c = '?';
        if (lang != null) {
            if (!isValidEntryLang(lang))
                throw new IllegalArgumentException(lang);
            uri.append(c);
            uri.append("lang=");
            uri.append(lang);
            c = '&';
        }
        HttpGet request = prepareGetRequest(uri.toString());
        HttpResponse response = null;
        try {
            response = httpClient.execute(request);
            String res = readResponse(response, true);
            return res;
        } catch (IOException e) {
            throw new SkPublishAPIException(e, -1, readResponse(response, false));
        }
    }

    /**
     * Gets the HTTP client.
     *
     * @return the HTTP client.
     */
    public HttpClient getHttpClient() {
        return httpClient;
    }

    /**
     * Gets the related entries of an entry.
     *
     * @param dictionaryCode
     *            the code of the dictionary.
     * @param entryId
     *            the id of the entry.
     * @param entryNumber
     *            the number of results preceding/following the given entry
     * @return a list of entries.
     * @throws SkPublishAPIException
     */
    public String getNearbyEntries(String dictionaryCode, String entryId, Integer entryNumber)
            throws SkPublishAPIException {
        StringBuilder uri = new StringBuilder(baseUrl);
        if (!isValidDictionaryCode(dictionaryCode))
            throw new IllegalArgumentException(dictionaryCode);
        uri.append("dictionaries/");
        uri.append(dictionaryCode);
        uri.append("/entries/");
        try {
            uri.append(URLEncoder.encode(entryId, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
        }
        uri.append("/nearbyentries");
        char c = '?';
        if (entryNumber != null) {
            uri.append(c);
            uri.append("entrynumber=");
            uri.append(entryNumber);
            c = '&';
        }
        HttpGet request = prepareGetRequest(uri.toString());
        HttpResponse response = null;
        try {
            response = httpClient.execute(request);
            String res = readResponse(response, true);
            return res;
        } catch (IOException e) {
            throw new SkPublishAPIException(e, -1, readResponse(response, false));
        }
    }

    /**
     * Gets the related entries of an entry.
     *
     * @param dictionaryCode
     *            the code of the dictionary.
     * @param entryId
     *            the id of the entry.
     * @return a list of entries.
     * @throws SkPublishAPIException
     */
    public String getRelatedEntries(String dictionaryCode, String entryId)
            throws SkPublishAPIException {
        StringBuilder uri = new StringBuilder(baseUrl);
        if (!isValidDictionaryCode(dictionaryCode))
            throw new IllegalArgumentException(dictionaryCode);
        uri.append("dictionaries/");
        uri.append(dictionaryCode);
        uri.append("/entries/");
        try {
            uri.append(URLEncoder.encode(entryId, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
        }
        uri.append("/relatedentries");
        HttpGet request = prepareGetRequest(uri.toString());
        HttpResponse response = null;
        try {
            response = httpClient.execute(request);
            String res = readResponse(response, true);
            return res;
        } catch (IOException e) {
            throw new SkPublishAPIException(e, -1, readResponse(response, false));
        }
    }

    /**
     * Gets the request handler if it exists.
     *
     * @return the request handler or <code>null</code>.
     */
    public RequestHandler getRequestHandler() {
        return requestHandler;
    }

    /**
     * Gets a thesaurus list.
     *
     * @param dictionaryCode
     *            the code of the dictionary.
     * @return the thesaurus list.
     * @throws SkPublishAPIException
     */
    public String getThesaurusList(String dictionaryCode) throws SkPublishAPIException {
        StringBuilder uri = new StringBuilder(baseUrl);
        if (!isValidDictionaryCode(dictionaryCode))
            throw new IllegalArgumentException(dictionaryCode);
        uri.append("dictionaries/");
        uri.append(dictionaryCode);
        uri.append("/topics/");
        HttpGet request = prepareGetRequest(uri.toString());
        HttpResponse response = null;
        try {
            response = httpClient.execute(request);
            String res = readResponse(response, true);
            return res;
        } catch (IOException e) {
            throw new SkPublishAPIException(e, -1, readResponse(response, false));
        }
    }

    /**
     * Gets a topic.
     *
     * @param dictionaryCode
     *            the code of the dictionary.
     * @param thesaurusName
     *            the name of the thesaurus.
     * @param topicId
     *            the id of the topic.
     * @return the topic.
     * @throws SkPublishAPIException
     */
    public String getTopic(String dictionaryCode, String thesName, String topicId)
            throws SkPublishAPIException {
        StringBuilder uri = new StringBuilder(baseUrl);
        if (!isValidDictionaryCode(dictionaryCode))
            throw new IllegalArgumentException(dictionaryCode);
        uri.append("dictionaries/");
        uri.append(dictionaryCode);
        uri.append("/topics/");
        try {
            uri.append(URLEncoder.encode(thesName, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
        }
        uri.append("/");
        try {
            uri.append(URLEncoder.encode(topicId, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
        }
        HttpGet request = prepareGetRequest(uri.toString());
        HttpResponse response = null;
        try {
            response = httpClient.execute(request);
            String res = readResponse(response, true);
            return res;
        } catch (IOException e) {
            throw new SkPublishAPIException(e, -1, readResponse(response, false));
        }
    }

    /**
     * Gets a word of the day.
     *
     * @param dictionaryCode
     *            the code of the dictionary.
     * @param day
     *            the day.
     * @param format
     *            the format of the entry.
     * @return a word of the day.
     * @throws SkPublishAPIException
     */
    public String getWordOfTheDay(String dictionaryCode, String day, String format)
            throws SkPublishAPIException {
        StringBuilder uri = new StringBuilder(baseUrl);
        if (dictionaryCode != null) {
            if (!isValidDictionaryCode(dictionaryCode))
                throw new IllegalArgumentException(dictionaryCode);
            uri.append("dictionaries/");
            uri.append(dictionaryCode);
            uri.append('/');
        }
        uri.append("wordoftheday");
        char c = '?';
        if (day != null) {
            if (!isValidWotdDay(day))
                throw new IllegalArgumentException(day);
            uri.append(c);
            uri.append("day=");
            uri.append(day);
            c = '&';
        }
        if (format != null) {
            if (!isValidEntryFormat(format))
                throw new IllegalArgumentException(format);
            uri.append(c);
            uri.append("format=");
            uri.append(format);
            c = '&';
        }
        HttpGet request = prepareGetRequest(uri.toString());
        HttpResponse response = null;
        try {
            response = httpClient.execute(request);
            String res = readResponse(response, true);
            return res;
        } catch (IOException e) {
            throw new SkPublishAPIException(e, -1, readResponse(response, false));
        }
    }

    /**
     * Gets a word of the day preview.
     *
     * @param dictionaryCode
     *            the code of the dictionary.
     * @param day
     *            the day.
     * @return the preview of a word of the day.
     * @throws SkPublishAPIException
     */
    public String getWordOfTheDayPreview(String dictionaryCode, String day)
            throws SkPublishAPIException {
        StringBuilder uri = new StringBuilder(baseUrl);
        if (dictionaryCode != null) {
            if (!isValidDictionaryCode(dictionaryCode))
                throw new IllegalArgumentException(dictionaryCode);
            uri.append("dictionaries/");
            uri.append(dictionaryCode);
            uri.append('/');
        }
        uri.append("wordoftheday/preview");
        char c = '?';
        if (day != null) {
            if (!isValidWotdDay(day))
                throw new IllegalArgumentException(day);
            uri.append(c);
            uri.append("day=");
            uri.append(day);
            c = '&';
        }
        HttpGet request = prepareGetRequest(uri.toString());
        HttpResponse response = null;
        try {
            response = httpClient.execute(request);
            String res = readResponse(response, true);
            return res;
        } catch (IOException e) {
            throw new SkPublishAPIException(e, -1, readResponse(response, false));
        }
    }

    private boolean isValidDictionaryCode(String code) {
        if (code.length() < 1)
            return false;
        for (int i = 0; i < code.length(); ++i) {
            char c = code.charAt(i);
            // Make sure no param are injected
            if (c == '/' || c == '%')
                return false;
            if (c == '*' || c == '$')
                return false;
        }
        return true;
    }

    private boolean isValidEntryFormat(String format) {
        for (int i = 0; i < format.length(); ++i) {
            char c = format.charAt(i);
            // Make sure no param are injected
            if (c == '/' || c == '%')
                return false;
        }
        return true;
    }

    private boolean isValidEntryLang(String lang) {
        for (int i = 0; i < lang.length(); ++i) {
            char c = lang.charAt(i);
            // Make sure no param are injected
            if (c == '/' || c == '%')
                return false;
        }
        return true;
    }

    private boolean isValidWotdDay(String day) {
        for (int i = 0; i < day.length(); ++i) {
            char c = day.charAt(i);
            // Make sure no param are injected
            if (c == '/' || c == '%')
                return false;
        }
        return true;
    }

    private HttpGet prepareGetRequest(String uri) {
        HttpGet request = new HttpGet(uri);
        request.setHeader("accessKey", accessKey);
        if (requestHandler != null)
            requestHandler.prepareGetRequest(request);
        return request;
    }

    private String readResponse(HttpResponse response, boolean checkStatusCode)
            throws SkPublishAPIException {
        if (response == null)
            return null;
        StatusLine statusLine = response.getStatusLine();
        int statusCode = statusLine.getStatusCode();
        HttpEntity entity = response.getEntity();
        InputStream content;
        try {
            content = entity.getContent();
        } catch (IOException e) {
            throw new SkPublishAPIException(e, -1, null);
        }
        try {
            InputStreamReader reader = new InputStreamReader(content, "UTF-8");
            StringWriter writer = new StringWriter();
            try {
                char[] buffer = new char[BUFFER_SIZE];
                int bytesRead = -1;
                while ((bytesRead = reader.read(buffer)) != -1) {
                    writer.write(buffer, 0, bytesRead);
                }
                writer.flush();
                if (!checkStatusCode || statusCode == 200) {
                    return writer.toString();
                } else {
                    throw new SkPublishAPIException(statusCode, writer.toString());
                }
            } catch (IOException e) {
                throw new SkPublishAPIException(e, -1, null);
            } finally {
                try {
                    reader.close();
                } catch (IOException ex) {
                }
                try {
                    writer.close();
                } catch (IOException ex) {
                }
            }
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    /**
     * Make a search
     *
     * @param dictionaryCode
     *            the code of the dictionary.
     * @param searchWord
     *            the searched word
     * @param pageSize
     *            the number of results per page to return
     * @param pageIndex
     *            the index of the result page to return
     * @return a list of entries.
     * @throws SkPublishAPIException
     */
    public String search(String dictionaryCode, String searchWord, Integer pageSize,
                         Integer pageIndex) throws SkPublishAPIException {
        StringBuilder uri = new StringBuilder(baseUrl);
        if (!isValidDictionaryCode(dictionaryCode))
            throw new IllegalArgumentException(dictionaryCode);
        uri.append("dictionaries/");
        uri.append(dictionaryCode);
        uri.append("/search?q=");
        try {
            uri.append(URLEncoder.encode(searchWord, "UTF-8"));
        } catch (UnsupportedEncodingException e1) {
        }
        char c = '&';
        if (pageSize != null) {
            uri.append(c);
            uri.append("pagesize=");
            uri.append(pageSize);
            c = '&';
        }
        if (pageIndex != null) {
            uri.append(c);
            uri.append("pageindex=");
            uri.append(pageIndex);
            c = '&';
        }
        HttpGet request = prepareGetRequest(uri.toString());
        HttpResponse response = null;
        try {
            response = httpClient.execute(request);
            String res = readResponse(response, true);
            return res;
        } catch (IOException e) {
            throw new SkPublishAPIException(e, -1, readResponse(response, false));
        }
    }

    /**
     * Gets the first search result.
     *
     * @param dictionaryCode
     *            the code of the dictionary.
     * @param searchWord
     *            the searched word.
     * @param format
     *            the format of the entry.
     * @return an entry.
     * @throws SkPublishAPIException
     */
    public String searchFirst(String dictionaryCode, String searchWord, String format)
            throws SkPublishAPIException {
        StringBuilder uri = new StringBuilder(baseUrl);
        if (!isValidDictionaryCode(dictionaryCode))
            throw new IllegalArgumentException(dictionaryCode);
        uri.append("dictionaries/");
        uri.append(dictionaryCode);
        uri.append("/search/first?q=");
        try {
            uri.append(URLEncoder.encode(searchWord, "UTF-8"));
        } catch (UnsupportedEncodingException e1) {
        }
        char c = '&';
        if (format != null) {
            if (!isValidEntryFormat(format))
                throw new IllegalArgumentException(format);
            uri.append(c);
            uri.append("format=");
            uri.append(format);
            c = '&';
        }
        HttpGet request = prepareGetRequest(uri.toString());
        HttpResponse response = null;
        try {
            response = httpClient.execute(request);
            String res = readResponse(response, true);
            return res;
        } catch (IOException e) {
            throw new SkPublishAPIException(e, -1, readResponse(response, false));
        }
    }

    /**
     * Sets the access key.
     *
     * @param accessKey
     *            the access key.
     */
    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    /**
     * Sets the API root URL.
     *
     * A trailing '/' will be appended if missing.
     *
     * @param baseUrl
     *            the API URL.
     */
    public void setBaseUrl(String baseUrl) {
        if (baseUrl.endsWith("/"))
            this.baseUrl = baseUrl;
        else
            this.baseUrl = baseUrl + "/";
    }

    /**
     * Sets the HTTP client.
     *
     * @param httpClient
     *            the HTTP client.
     */
    public void setHttpClient(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    /**
     * Sets the request handler.
     *
     * @param requestHandler
     *            the request handler.
     */
    public void setRequestHandler(RequestHandler requestHandler) {
        this.requestHandler = requestHandler;
    }

}
