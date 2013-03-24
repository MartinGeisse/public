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

package org.eclipse.jetty.servlets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.Socket;
import java.util.EnumSet;
import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.http.HttpURI;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.testing.ServletTester;
import org.eclipse.jetty.util.IO;
import org.eclipse.jetty.util.log.Log;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

/**
 * @version $Revision$ $Date$
 */
public abstract class AbstractDoSFilterTest
{
    private static ServletTester _tester;
    private static String _host;
    private static int _port;
    private static long _requestMaxTime = 200;
    private static FilterHolder _dosFilter;
    private static FilterHolder _timeoutFilter;

    public static void startServer(Class<? extends Filter> filter) throws Exception
    {
        _tester = new ServletTester();
        HttpURI uri = new HttpURI(_tester.createChannelConnector(true));
        _host = uri.getHost();
        _port = uri.getPort();

        _tester.setContextPath("/ctx");
        _tester.addServlet(TestServlet.class, "/*");

        _dosFilter = _tester.addFilter(filter, "/dos/*", EnumSet.allOf(DispatcherType.class));
        _dosFilter.setInitParameter("maxRequestsPerSec", "4");
        _dosFilter.setInitParameter("delayMs", "200");
        _dosFilter.setInitParameter("throttledRequests", "1");
        _dosFilter.setInitParameter("waitMs", "10");
        _dosFilter.setInitParameter("throttleMs", "4000");
        _dosFilter.setInitParameter("remotePort", "false");
        _dosFilter.setInitParameter("insertHeaders", "true");

        _timeoutFilter = _tester.addFilter(filter, "/timeout/*", EnumSet.allOf(DispatcherType.class));
        _timeoutFilter.setInitParameter("maxRequestsPerSec", "4");
        _timeoutFilter.setInitParameter("delayMs", "200");
        _timeoutFilter.setInitParameter("throttledRequests", "1");
        _timeoutFilter.setInitParameter("waitMs", "10");
        _timeoutFilter.setInitParameter("throttleMs", "4000");
        _timeoutFilter.setInitParameter("remotePort", "false");
        _timeoutFilter.setInitParameter("insertHeaders", "true");
        _timeoutFilter.setInitParameter("maxRequestMs", _requestMaxTime + "");

        _tester.start();
    }

    @AfterClass
    public static void stopServer() throws Exception
    {
        _tester.stop();
    }

    @Before
    public void startFilters() throws Exception
    {
        _dosFilter.start();
        _timeoutFilter.start();
    }

    @After
    public void stopFilters() throws Exception
    {
        _timeoutFilter.stop();
        _dosFilter.stop();
    }

    private String doRequests(String requests, int loops, long pause0, long pause1, String request) throws Exception
    {
        Socket socket = new Socket(_host, _port);
        socket.setSoTimeout(30000);

        for (int i=loops;i-->0;)
        {
            socket.getOutputStream().write(requests.getBytes("UTF-8"));
            socket.getOutputStream().flush();
            if (i>0 && pause0>0)
                Thread.sleep(pause0);
        }
        if (pause1>0)
            Thread.sleep(pause1);
        socket.getOutputStream().write(request.getBytes("UTF-8"));
        socket.getOutputStream().flush();


        String response;
        if (requests.contains("/unresponsive"))
        {
            // don't read in anything, forcing the request to time out
            Thread.sleep(_requestMaxTime * 2);
            response = IO.toString(socket.getInputStream(),"UTF-8");
        }
        else
        {
            response = IO.toString(socket.getInputStream(),"UTF-8");
        }
        socket.close();
        return response;
    }

    private int count(String responses,String substring)
    {
        int count=0;
        int i=responses.indexOf(substring);
        while (i>=0)
        {
            count++;
            i=responses.indexOf(substring,i+substring.length());
        }

        return count;
    }

    @Test
    public void testEvenLowRateIP() throws Exception
    {
        String request="GET /ctx/dos/test HTTP/1.1\r\nHost: localhost\r\n\r\n";
        String last="GET /ctx/dos/test HTTP/1.1\r\nHost: localhost\r\nConnection: close\r\n\r\n";
        String responses = doRequests(request,11,300,300,last);
        assertEquals(12,count(responses,"HTTP/1.1 200 OK"));
        assertEquals(0,count(responses,"DoSFilter:"));
    }

    @Test
    public void testBurstLowRateIP() throws Exception
    {
        String request="GET /ctx/dos/test HTTP/1.1\r\nHost: localhost\r\n\r\n";
        String last="GET /ctx/dos/test HTTP/1.1\r\nHost: localhost\r\nConnection: close\r\n\r\n";
        String responses = doRequests(request+request+request+request,2,1100,1100,last);

        assertEquals(9,count(responses,"HTTP/1.1 200 OK"));
        assertEquals(0,count(responses,"DoSFilter:"));
    }

    @Test
    public void testDelayedIP() throws Exception
    {
        String request="GET /ctx/dos/test HTTP/1.1\r\nHost: localhost\r\n\r\n";
        String last="GET /ctx/dos/test HTTP/1.1\r\nHost: localhost\r\nConnection: close\r\n\r\n";
        String responses = doRequests(request+request+request+request+request,2,1100,1100,last);

        assertEquals(2,count(responses,"DoSFilter: delayed"));
        assertEquals(11,count(responses,"HTTP/1.1 200 OK"));
    }

    @Test
    public void testThrottledIP() throws Exception
    {
        Thread other = new Thread()
        {
            @Override
            public void run()
            {
                try
                {
                    // Cause a delay, then sleep while holding pass
                    String request="GET /ctx/dos/sleeper HTTP/1.1\r\nHost: localhost\r\n\r\n";
                    String last="GET /ctx/dos/sleeper?sleep=2000 HTTP/1.1\r\nHost: localhost\r\nConnection: close\r\n\r\n";
                    String responses = doRequests(request+request+request+request,1,0,0,last);
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
        };
        other.start();
        Thread.sleep(1500);

        String request="GET /ctx/dos/test HTTP/1.1\r\nHost: localhost\r\n\r\n";
        String last="GET /ctx/dos/test HTTP/1.1\r\nHost: localhost\r\nConnection: close\r\n\r\n";
        String responses = doRequests(request+request+request+request,1,0,0,last);
        System.out.println("responses are " + responses);
        assertEquals("200 OK responses", 5,count(responses,"HTTP/1.1 200 OK"));
        assertEquals("delayed responses", 1,count(responses,"DoSFilter: delayed"));
        assertEquals("throttled responses", 1,count(responses,"DoSFilter: throttled"));
        assertEquals("unavailable responses", 0,count(responses,"DoSFilter: unavailable"));

        other.join();
    }

    @Test
    public void testUnavailableIP() throws Exception
    {
        Thread other = new Thread()
        {
            @Override
            public void run()
            {
                try
                {
                    // Cause a delay, then sleep while holding pass
                    String request="GET /ctx/dos/test HTTP/1.1\r\nHost: localhost\r\n\r\n";
                    String last="GET /ctx/dos/test?sleep=5000 HTTP/1.1\r\nHost: localhost\r\nConnection: close\r\n\r\n";
                    String responses = doRequests(request+request+request+request,1,0,0,last);
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
        };
        other.start();
        Thread.sleep(500);

        String request="GET /ctx/dos/test HTTP/1.1\r\nHost: localhost\r\n\r\n";
        String last="GET /ctx/dos/test HTTP/1.1\r\nHost: localhost\r\nConnection: close\r\n\r\n";
        String responses = doRequests(request+request+request+request,1,0,0,last);

        System.err.println("RESPONSES: \n"+responses);
        
        assertEquals(4,count(responses,"HTTP/1.1 200 OK"));
        assertEquals(1,count(responses,"HTTP/1.1 503"));
        assertEquals(1,count(responses,"DoSFilter: delayed"));
        assertEquals(1,count(responses,"DoSFilter: throttled"));
        assertEquals(1,count(responses,"DoSFilter: unavailable"));

        other.join();
    }

    @Test
    public void testSessionTracking() throws Exception
    {
        // get a session, first
        String requestSession="GET /ctx/dos/test?session=true HTTP/1.1\r\nHost: localhost\r\nConnection: close\r\n\r\n";
        String response=doRequests("",1,0,0,requestSession);
        String sessionId=response.substring(response.indexOf("Set-Cookie: ")+12, response.indexOf(";"));

        // all other requests use this session
        String request="GET /ctx/dos/test HTTP/1.1\r\nHost: localhost\r\nCookie: " + sessionId + "\r\n\r\n";
        String last="GET /ctx/dos/test HTTP/1.1\r\nHost: localhost\r\nConnection: close\r\nCookie: " + sessionId + "\r\n\r\n";
        String responses = doRequests(request+request+request+request+request,2,1100,1100,last);

        assertEquals(11,count(responses,"HTTP/1.1 200 OK"));
        assertEquals(2,count(responses,"DoSFilter: delayed"));
    }

    @Test
    public void testMultipleSessionTracking() throws Exception
    {
        // get some session ids, first
        String requestSession="GET /ctx/dos/test?session=true HTTP/1.1\r\nHost: localhost\r\n\r\n";
        String closeRequest="GET /ctx/dos/test?session=true HTTP/1.1\r\nHost: localhost\r\nConnection: close\r\n\r\n";
        String response=doRequests(requestSession+requestSession,1,0,0,closeRequest);

        String[] sessions = response.split("\r\n\r\n");

        String sessionId1=sessions[0].substring(sessions[0].indexOf("Set-Cookie: ")+12, sessions[0].indexOf(";"));
        String sessionId2=sessions[1].substring(sessions[1].indexOf("Set-Cookie: ")+12, sessions[1].indexOf(";"));

        // alternate between sessions
        String request1="GET /ctx/dos/test HTTP/1.1\r\nHost: localhost\r\nCookie: " + sessionId1 + "\r\n\r\n";
        String request2="GET /ctx/dos/test HTTP/1.1\r\nHost: localhost\r\nCookie: " + sessionId2 + "\r\n\r\n";
        String last="GET /ctx/dos/test HTTP/1.1\r\nHost: localhost\r\nConnection: close\r\nCookie: " + sessionId2 + "\r\n\r\n";

        // ensure the sessions are new
        String responses = doRequests(request1+request2,1,1100,1100,last);
        Thread.sleep(1000);

        responses = doRequests(request1+request2+request1+request2+request1,2,1100,1100,last);

        assertEquals(11,count(responses,"HTTP/1.1 200 OK"));
        assertEquals(0,count(responses,"DoSFilter: delayed"));

        // alternate between sessions
        responses = doRequests(request1+request2+request1+request2+request1,2,350,550,last);

        assertEquals(11,count(responses,"HTTP/1.1 200 OK"));
        int delayedRequests = count(responses,"DoSFilter: delayed");
        assertTrue("delayedRequests: " + delayedRequests + " is not between 2 and 3",delayedRequests >= 2 && delayedRequests <= 3);
    }

    @Test
    public void testUnresponsiveClient() throws Exception
    {
        int numRequests = 1000;

        String last="GET /ctx/timeout/unresponsive?lines="+numRequests+" HTTP/1.1\r\nHost: localhost\r\nConnection: close\r\n\r\n";
        String responses = doRequests("",0,0,0,last);
        // was expired, and stopped before reaching the end of the requests
        int responseLines = count(responses, "Line:");
        assertTrue(responses.contains("DoSFilter: timeout"));
        assertTrue(responseLines > 0 && responseLines < numRequests);
    }

    public static class TestServlet extends HttpServlet implements Servlet
    {
        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
        {
            if (request.getParameter("session")!=null)
                request.getSession(true);
            if (request.getParameter("sleep")!=null)
            {
                try
                {
                    Thread.sleep(Long.parseLong(request.getParameter("sleep")));
                }
                catch(InterruptedException e)
                {
                }
            }

            if (request.getParameter("lines")!=null)
            {
                int count = Integer.parseInt(request.getParameter("lines"));
                for(int i = 0; i < count; ++i)
                {
                    response.getWriter().append("Line: " + i+"\n");
                    response.flushBuffer();

                    try
                    {
                        Thread.sleep(10);
                    }
                    catch(InterruptedException e)
                    {
                    }

                }
            }

            response.setContentType("text/plain");
        }
    }
}
