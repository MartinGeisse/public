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
import java.io.PrintWriter;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.eclipse.jetty.client.ContentExchange;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.http.HttpMethods;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


/**
 * AbstractLastAccessTimeTest
 */
public abstract class AbstractSessionValueSavingTest
{
    public abstract AbstractTestServer createServer(int port, int max, int scavenge);
    
    @Test
    public void testSessionValueSaving() throws Exception
    {
        String contextPath = "";
        String servletMapping = "/server";
        int maxInactivePeriod = 10000;
        int scavengePeriod = 20000;
        AbstractTestServer server1 = createServer(0, maxInactivePeriod, scavengePeriod);
        server1.addContext(contextPath).addServlet(TestServlet.class, servletMapping);
        server1.start();
        int port1=server1.getPort();
        try
        {
            
                HttpClient client = new HttpClient();
                client.setConnectorType(HttpClient.CONNECTOR_SOCKET);
                client.start();
                try
                {
                    long sessionTestValue = 0;

                    // Perform one request to server1 to create a session
                    ContentExchange exchange1 = new ContentExchange(true);
                    exchange1.setMethod(HttpMethods.GET);
                    exchange1.setURL("http://localhost:" + port1 + contextPath + servletMapping + "?action=init");
                    client.send(exchange1);
                    exchange1.waitForDone();
                    assertEquals(HttpServletResponse.SC_OK, exchange1.getResponseStatus());
                    
                    System.out.println("Checking: " + sessionTestValue + " vs " + exchange1.getResponseContent());
                    assertTrue(sessionTestValue < Long.parseLong(exchange1.getResponseContent()));
                   
                    sessionTestValue = Long.parseLong(exchange1.getResponseContent());
                    
                    String sessionCookie = exchange1.getResponseFields().getStringField("Set-Cookie");
                    assertTrue( sessionCookie != null );
                    // Mangle the cookie, replacing Path with $Path, etc.
                    sessionCookie = sessionCookie.replaceFirst("(\\W)(P|p)ath=", "$1\\$Path=");

                    // Perform some request to server2 using the session cookie from the previous request
                    // This should migrate the session from server1 to server2, and leave server1's
                    // session in a very stale state, while server2 has a very fresh session.
                    // We want to test that optimizations done to the saving of the shared lastAccessTime
                    // do not break the correct working
                    int requestInterval = 500;
                    
                    
                    for (int i = 0; i < 10; ++i)
                    {
                        ContentExchange exchange2 = new ContentExchange(true);
                        exchange2.setMethod(HttpMethods.GET);
                        exchange2.setURL("http://localhost:" + port1 + contextPath + servletMapping);
                        exchange2.getRequestFields().add("Cookie", sessionCookie);
                        client.send(exchange2);
                        exchange2.waitForDone();
                        assertEquals(HttpServletResponse.SC_OK , exchange2.getResponseStatus());
                        
                        System.out.println("Checking: " + sessionTestValue + " vs " + exchange2.getResponseContent());
                        assertTrue(sessionTestValue < Long.parseLong(exchange2.getResponseContent()));
                        
                        sessionTestValue = Long.parseLong(exchange2.getResponseContent());
                        
                        String setCookie = exchange1.getResponseFields().getStringField("Set-Cookie");
                        if (setCookie!=null)                    
                            sessionCookie = setCookie.replaceFirst("(\\W)(P|p)ath=", "$1\\$Path=");
                        
                        Thread.sleep(requestInterval);
                    }

                }
                finally
                {
                    client.stop();
                }
        }
        finally
        {
            server1.stop();
        }
    }

    public static class TestServlet extends HttpServlet
    {
        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse httpServletResponse) throws ServletException, IOException
        {
            String action = request.getParameter("action");
            if ("init".equals(action))
            {
                HttpSession session = request.getSession(true);
                session.setAttribute("test", System.currentTimeMillis());
                
                sendResult(session, httpServletResponse.getWriter());
            }
            else
            {
                HttpSession session = request.getSession(false);
                System.out.println("not init call " + session);
                if (session!=null)
                {
                	long value = System.currentTimeMillis();
                	System.out.println("Setting test to : " + value);
                    session.setAttribute("test", value);
                    
                }
                
                sendResult(session, httpServletResponse.getWriter());

            }
            
            
        }
        
        private void sendResult(HttpSession session, PrintWriter writer)
        {
        	if (session != null)
        	{
        		writer.print(session.getAttribute("test"));
        	}
        	else
        	{
        		writer.print(0);
        	}
        }
        
    }
}
