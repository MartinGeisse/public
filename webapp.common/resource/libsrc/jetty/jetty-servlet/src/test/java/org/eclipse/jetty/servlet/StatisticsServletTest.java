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

package org.eclipse.jetty.servlet;
 
import junit.framework.AssertionFailedError;

import org.eclipse.jetty.server.LocalConnector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.StatisticsHandler;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class StatisticsServletTest
{
    private Server server;
    private LocalConnector connector;
    private ServletContextHandler context;

    @Before
    public void init() throws Exception
    {
        server = new Server();
        server.setSendServerVersion(false);
        context = new ServletContextHandler();
        context.setContextPath("/");
        ServletHolder holder = new ServletHolder();
        holder.setServlet(new org.eclipse.jetty.servlet.StatisticsServlet());
        holder.setInitParameter("restrictToLocalhost", "false");
        context.addServlet(holder, "/stats");

        server.setHandler(context);
        connector = new LocalConnector();
        server.addConnector(connector);
    }

    @After
    public void destroy() throws Exception
    {
        server.stop();
        server.join();
    }

    @Test
    public void testNoHandler () throws Exception
    {
        server.start();

        StringBuffer req1 = new StringBuffer();
        req1.append("GET /stats HTTP/1.1\n");
        req1.append("Host: localhost\n");
        req1.append("\n");

        String response = connector.getResponses(req1.toString());
        assertResponseContains("503", response);
    }

    @Test
    public void testWithHandler () throws Exception
    {
        StatisticsHandler statsHandler = new StatisticsHandler();
        statsHandler.setHandler(context);
        server.setHandler(statsHandler);
        server.start();

        StringBuffer req1 = new StringBuffer();
        req1.append("GET /stats HTTP/1.1\n");
        req1.append("Host: localhost\n");
        req1.append("\n");

        String response = connector.getResponses(req1.toString());
        assertResponseContains("Statistics gathering started ", response);
    }

    private void assertResponseContains(String expected, String response)
    {
        int idx = response.indexOf(expected);
        if (idx == (-1))
        {
            // Not found
            StringBuffer err = new StringBuffer();
            err.append("Response does not contain expected string \"").append(expected).append("\"");
            err.append("\n").append(response);

            System.err.println(err);
            throw new AssertionFailedError(err.toString());
        }
    }
}
