//
//  ========================================================================
//  Copyright (c) 1995-2013 Mort Bay Consulting Pty. Ltd.
//  ------------------------------------------------------------------------
//  All rights reserved. This program and the accompanying materials
//  are made available under the terms of the Eclipse Public License v1.0
//  and Apache License v2.0 which accompanies this distribution.
//
//      The Eclipse Public License is available at
//      http://www.eclipse.org/legal/epl-v10.html
//
//      The Apache License v2.0 is available at
//      http://www.opensource.org/licenses/apache2.0.php
//
//  You may elect to redistribute this code under either of these licenses.
//  ========================================================================
//

package org.eclipse.jetty.server;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.handler.AbstractHandler;
import org.junit.Test;

/**
 *
 */
public class CheckReverseProxyHeadersTest
{
    @Test
    public void testCheckReverseProxyHeaders() throws Exception
    {
        // Classic ProxyPass from example.com:80 to localhost:8080
        testRequest("Host: localhost:8080\n" +
                    "X-Forwarded-For: 10.20.30.40\n" +
                    "X-Forwarded-Host: example.com", new RequestValidator()
        {
            public void validate(HttpServletRequest request)
            {
                assertEquals("example.com", request.getServerName());
                assertEquals(80, request.getServerPort());
                assertEquals("10.20.30.40", request.getRemoteAddr());
                assertEquals("10.20.30.40", request.getRemoteHost());
                assertEquals("example.com", request.getHeader("Host"));
                assertEquals("http",request.getScheme());
                assertFalse(request.isSecure());
            }
        });

        // ProxyPass from example.com:81 to localhost:8080
        testRequest("Host: localhost:8080\n" +
                    "X-Forwarded-For: 10.20.30.40\n" +
                    "X-Forwarded-Host: example.com:81\n" +
                    "X-Forwarded-Server: example.com\n"+
                    "X-Forwarded-Proto: https", new RequestValidator()
        {
            public void validate(HttpServletRequest request)
            {
                assertEquals("example.com", request.getServerName());
                assertEquals(81, request.getServerPort());
                assertEquals("10.20.30.40", request.getRemoteAddr());
                assertEquals("10.20.30.40", request.getRemoteHost());
                assertEquals("example.com:81", request.getHeader("Host"));
                assertEquals("https",request.getScheme());
                assertTrue(request.isSecure());
            }
        });

        // Multiple ProxyPass from example.com:80 to rp.example.com:82 to localhost:8080
        testRequest("Host: localhost:8080\n" +
                    "X-Forwarded-For: 10.20.30.40, 10.0.0.1\n" +
                    "X-Forwarded-Host: example.com, rp.example.com:82\n" +
                    "X-Forwarded-Server: example.com, rp.example.com\n"+
                    "X-Forwarded-Proto: https, http", new RequestValidator()
        {
            public void validate(HttpServletRequest request)
            {
                assertEquals("example.com", request.getServerName());
                assertEquals(443, request.getServerPort());
                assertEquals("10.20.30.40", request.getRemoteAddr());
                assertEquals("10.20.30.40", request.getRemoteHost());
                assertEquals("example.com", request.getHeader("Host"));
                assertEquals("https",request.getScheme());
                assertTrue(request.isSecure());
            }
        });
    }

    private void testRequest(String headers, RequestValidator requestValidator) throws Exception
    {
        Server server = new Server();
        LocalConnector connector = new LocalConnector();

        // Activate reverse proxy headers checking
        connector.setForwarded(true);

        server.setConnectors(new Connector[] {connector});
        ValidationHandler validationHandler = new ValidationHandler(requestValidator);
        server.setHandler(validationHandler);

        try
        {
            server.start();
            connector.getResponses("GET / HTTP/1.1\n" + headers + "\n\n");

            Error error = validationHandler.getError();

            if (error != null)
            {
                throw error;
            }
        }
        finally
        {
            server.stop();
        }
    }

    /**
     * Interface for validate a wrapped request.
     */
    private static interface RequestValidator
    {
        /**
         * Validate the current request.
         * @param request the request.
         */
        void validate(HttpServletRequest request);
    }

    /**
     * Handler for validation.
     */
    private static class ValidationHandler extends AbstractHandler
    {
        private final RequestValidator _requestValidator;
        private Error _error;

        private ValidationHandler(RequestValidator requestValidator)
        {
            _requestValidator = requestValidator;
        }

        /**
         * Retrieve the validation error.
         * @return the validation error or <code>null</code> if there was no error.
         */
        public Error getError()
        {
            return _error;
        }

        public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
        {
            try
            {
                _requestValidator.validate(request);
            }
            catch (Error e)
            {
                _error = e;
            }
            catch (Throwable e)
            {
                _error = new Error(e);
            }
        }
    }
}
