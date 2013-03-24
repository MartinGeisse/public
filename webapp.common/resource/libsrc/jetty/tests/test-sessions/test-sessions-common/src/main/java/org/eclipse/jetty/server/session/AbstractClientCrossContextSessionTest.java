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

package org.eclipse.jetty.server.session;

import java.io.IOException;
import java.util.Collections;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.eclipse.jetty.client.ContentExchange;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.http.HttpMethods;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


/**
 * AbstractClientCrossContextSessionTest
 */
public abstract class AbstractClientCrossContextSessionTest
{

    public abstract AbstractTestServer createServer(int port);

    @Test
    public void testCrossContextDispatch() throws Exception
    {
        String contextA = "/contextA";
        String contextB = "/contextB";
        String servletMapping = "/server";
        AbstractTestServer server = createServer(0);
        TestServletA servletA = new TestServletA();
        ServletHolder holderA = new ServletHolder(servletA);
        ServletContextHandler ctxA = server.addContext(contextA);
        ctxA.addServlet(holderA, servletMapping);
        ServletContextHandler ctxB = server.addContext(contextB);
        TestServletB servletB = new TestServletB();
        ServletHolder holderB = new ServletHolder(servletB);
        ctxB.addServlet(holderB, servletMapping);
        server.start();
        int port = server.getPort();
        
        try
        {
            HttpClient client = new HttpClient();
            client.setConnectorType(HttpClient.CONNECTOR_SOCKET);
            client.start();
            try
            {
                // Perform a request to contextA
                ContentExchange exchangeA = new ContentExchange(true);
                exchangeA.setMethod(HttpMethods.GET);
                exchangeA.setURL("http://localhost:" + port + contextA + servletMapping);
                client.send(exchangeA);
                exchangeA.waitForDone();
                assertEquals(HttpServletResponse.SC_OK,exchangeA.getResponseStatus());
                String sessionCookie = exchangeA.getResponseFields().getStringField("Set-Cookie");
                assertTrue(sessionCookie != null);
                // Mangle the cookie, replacing Path with $Path, etc.
                sessionCookie = sessionCookie.replaceFirst("(\\W)(P|p)ath=", "$1\\$Path=");

                // Perform a request to contextB with the same session cookie
                ContentExchange exchangeB = new ContentExchange(true);
                exchangeB.setMethod(HttpMethods.GET);
                exchangeB.setURL("http://localhost:" + port + contextB + servletMapping);
                System.err.println("Cookie = "+sessionCookie);
                exchangeB.getRequestFields().add("Cookie", sessionCookie);  
                client.send(exchangeB);
                exchangeB.waitForDone();
                assertEquals(HttpServletResponse.SC_OK,exchangeB.getResponseStatus());
                assertEquals(servletA.sessionId, servletB.sessionId);
            }
            finally
            {
                client.stop();
            }
        }
        finally
        {
            server.stop();
        }
    }

    public static class TestServletA extends HttpServlet
    {
        public String sessionId;
        
        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
        {
            HttpSession session = request.getSession(false);
            if (session == null)
            {
                session = request.getSession(true);
                sessionId = session.getId();
            }

            // Add something to the session
            session.setAttribute("A", "A");

            // Check that we don't see things put in session by contextB
            Object objectB = session.getAttribute("B");
            assertTrue(objectB == null);
        }
    }

    public static class TestServletB extends HttpServlet
    {
        public String sessionId;
        
        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse httpServletResponse) throws ServletException, IOException
        {
            HttpSession session = request.getSession(false);
            if (session == null)
                session = request.getSession(true);

            sessionId = session.getId();



            // Add something to the session
            session.setAttribute("B", "B");

            // Check that we don't see things put in session by contextA
            Object objectA = session.getAttribute("A");
            assertTrue(objectA == null);
        }
    }
}
