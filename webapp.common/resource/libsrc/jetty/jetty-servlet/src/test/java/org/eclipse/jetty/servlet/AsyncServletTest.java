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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.AsyncContext;
import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.DispatcherType;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.util.IO;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class AsyncServletTest 
{    

    protected AsyncServlet _servlet=new AsyncServlet();
    protected int _port;

    protected Server _server = new Server();
    protected ServletHandler _servletHandler;
    protected SelectChannelConnector _connector;

    @Before
    public void setUp() throws Exception
    {
        _connector = new SelectChannelConnector();
        _server.setConnectors(new Connector[]{ _connector });
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.NO_SECURITY|ServletContextHandler.NO_SESSIONS);
        context.setContextPath("/ctx");
        _server.setHandler(context);
        _servletHandler=context.getServletHandler();
        ServletHolder holder=new ServletHolder(_servlet);
        holder.setAsyncSupported(true);
        _servletHandler.addServletWithMapping(holder,"/path/*");
        _server.start();
        _port=_connector.getLocalPort();
    }

    @After
    public void tearDown() throws Exception
    {
        _server.stop();
    }
    
    @Test
    public void testNormal() throws Exception
    {
        String response=process(null,null);
        assertEquals("HTTP/1.1 200 OK",response.substring(0,15));
        assertContains(
                "history: REQUEST\r\n"+
                "history: initial\r\n",response);
        assertContains("NORMAL",response);
        assertNotContains("history: onTimeout",response);
        assertNotContains("history: onComplete",response);
    }

    @Test
    public void testSleep() throws Exception
    {
        String response=process("sleep=200",null);
        assertEquals("HTTP/1.1 200 OK",response.substring(0,15));
        assertContains(
                "history: REQUEST\r\n"+
                "history: initial\r\n",response);
        assertContains("SLEPT",response);
        assertNotContains("history: onTimeout",response);
        assertNotContains("history: onComplete",response);
    }

    @Test
    public void testSuspend() throws Exception
    {
        String response=process("suspend=200",null);
        assertEquals("HTTP/1.1 500 Async Timeout",response.substring(0,26));
        assertContains(
            "history: REQUEST\r\n"+
            "history: initial\r\n"+
            "history: suspend\r\n"+
            "history: onTimeout\r\n"+
            "history: ERROR\r\n"+
            "history: !initial\r\n"+
            "history: onComplete\r\n",response);
                    
        assertContains("ERROR: /ctx/path/info",response);
    }
    
    @Test
    public void testSuspendOnTimeoutDispatch() throws Exception
    {
        String response=process("suspend=200&timeout=dispatch",null);
        assertEquals("HTTP/1.1 200 OK",response.substring(0,15));
        assertContains(
            "history: REQUEST\r\n"+
            "history: initial\r\n"+
            "history: suspend\r\n"+
            "history: onTimeout\r\n"+
            "history: dispatch\r\n"+
            "history: ASYNC\r\n"+
            "history: !initial\r\n"+
            "history: onComplete\r\n",response);
                    
        assertContains("DISPATCHED",response);
    }
    
    @Test
    public void testSuspendOnTimeoutComplete() throws Exception
    {
        String response=process("suspend=200&timeout=complete",null);
        assertEquals("HTTP/1.1 200 OK",response.substring(0,15));
        assertContains(
            "history: REQUEST\r\n"+
            "history: initial\r\n"+
            "history: suspend\r\n"+
            "history: onTimeout\r\n"+
            "history: complete\r\n"+
            "history: onComplete\r\n",response);
                    
        assertContains("COMPLETED",response);
    }

    @Test
    public void testSuspendWaitResume() throws Exception
    {
        String response=process("suspend=200&resume=10",null);
        assertEquals("HTTP/1.1 200 OK",response.substring(0,15));
        assertContains(
            "history: REQUEST\r\n"+
            "history: initial\r\n"+
            "history: suspend\r\n"+
            "history: resume\r\n"+
            "history: ASYNC\r\n"+
            "history: !initial\r\n"+
            "history: onComplete\r\n",response);
        assertNotContains("history: onTimeout",response);
    }

    @Test
    public void testSuspendResume() throws Exception
    {
        String response=process("suspend=200&resume=0",null);
        assertEquals("HTTP/1.1 200 OK",response.substring(0,15));
        assertContains(
            "history: REQUEST\r\n"+
            "history: initial\r\n"+
            "history: suspend\r\n"+
            "history: resume\r\n"+
            "history: ASYNC\r\n"+
            "history: !initial\r\n"+
            "history: onComplete\r\n",response);
        assertContains("history: onComplete",response);
    }

    @Test
    public void testSuspendWaitComplete() throws Exception
    {
        String response=process("suspend=200&complete=50",null);
        assertEquals("HTTP/1.1 200 OK",response.substring(0,15));
        assertContains(
            "history: REQUEST\r\n"+
            "history: initial\r\n"+
            "history: suspend\r\n"+
            "history: complete\r\n"+
            "history: onComplete\r\n",response);
        assertContains("COMPLETED",response);
        assertNotContains("history: onTimeout",response);
        assertNotContains("history: !initial",response);
    }

    @Test
    public void testSuspendComplete() throws Exception
    {
        String response=process("suspend=200&complete=0",null);
        assertEquals("HTTP/1.1 200 OK",response.substring(0,15));
        assertContains(
            "history: REQUEST\r\n"+
            "history: initial\r\n"+
            "history: suspend\r\n"+
            "history: complete\r\n"+
            "history: onComplete\r\n",response);
        assertContains("COMPLETED",response);
        assertNotContains("history: onTimeout",response);
        assertNotContains("history: !initial",response);
    }

    @Test
    public void testSuspendWaitResumeSuspendWaitResume() throws Exception
    {
        String response=process("suspend=1000&resume=10&suspend2=1000&resume2=10",null);
        assertEquals("HTTP/1.1 200 OK",response.substring(0,15));
        assertContains(
            "history: REQUEST\r\n"+
            "history: initial\r\n"+
            "history: suspend\r\n"+
            "history: resume\r\n"+
            "history: ASYNC\r\n"+
            "history: !initial\r\n"+
            "history: suspend\r\n"+
            "history: resume\r\n"+
            "history: ASYNC\r\n"+
            "history: !initial\r\n"+
            "history: onComplete\r\n",response);
        assertContains("DISPATCHED",response);
    }

    @Test
    public void testSuspendWaitResumeSuspendComplete() throws Exception
    {
        String response=process("suspend=1000&resume=10&suspend2=1000&complete2=10",null);
        assertEquals("HTTP/1.1 200 OK",response.substring(0,15));
        assertContains(
            "history: REQUEST\r\n"+
            "history: initial\r\n"+
            "history: suspend\r\n"+
            "history: resume\r\n"+
            "history: ASYNC\r\n"+
            "history: !initial\r\n"+
            "history: suspend\r\n"+
            "history: complete\r\n"+
            "history: onComplete\r\n",response);
        assertContains("COMPLETED",response);
    }

    @Test
    public void testSuspendWaitResumeSuspend() throws Exception
    {
        String response=process("suspend=1000&resume=10&suspend2=10",null);
        assertEquals("HTTP/1.1 500 Async Timeout",response.substring(0,26));
        assertContains(
            "history: REQUEST\r\n"+
            "history: initial\r\n"+
            "history: suspend\r\n"+
            "history: resume\r\n"+
            "history: ASYNC\r\n"+
            "history: !initial\r\n"+
            "history: suspend\r\n"+
            "history: onTimeout\r\n"+
            "history: ERROR\r\n"+
            "history: !initial\r\n"+
            "history: onComplete\r\n",response);
        assertContains("ERROR: /ctx/path/info",response);
    }

    @Test
    public void testSuspendTimeoutSuspendResume() throws Exception
    {
        String response=process("suspend=10&suspend2=1000&resume2=10",null);
        assertEquals("HTTP/1.1 200 OK",response.substring(0,15));
        assertContains(
            "history: REQUEST\r\n"+
            "history: initial\r\n"+
            "history: suspend\r\n"+
            "history: onTimeout\r\n"+
            "history: ERROR\r\n"+
            "history: !initial\r\n"+
            "history: suspend\r\n"+
            "history: resume\r\n"+
            "history: ASYNC\r\n"+
            "history: !initial\r\n"+
            "history: onComplete\r\n",response);
        assertContains("DISPATCHED",response);
    }

    @Test
    public void testSuspendTimeoutSuspendComplete() throws Exception
    {
        String response=process("suspend=10&suspend2=1000&complete2=10",null);
        assertEquals("HTTP/1.1 200 OK",response.substring(0,15));
        assertContains(
            "history: REQUEST\r\n"+
            "history: initial\r\n"+
            "history: suspend\r\n"+
            "history: onTimeout\r\n"+
            "history: ERROR\r\n"+
            "history: !initial\r\n"+
            "history: suspend\r\n"+
            "history: complete\r\n"+
            "history: onComplete\r\n",response);
        assertContains("COMPLETED",response);
    }

    @Test
    public void testSuspendTimeoutSuspend() throws Exception
    {
        String response=process("suspend=10&suspend2=10",null);
        assertContains(
            "history: REQUEST\r\n"+
            "history: initial\r\n"+
            "history: suspend\r\n"+
            "history: onTimeout\r\n"+
            "history: ERROR\r\n"+
            "history: !initial\r\n"+
            "history: suspend\r\n"+
            "history: onTimeout\r\n"+
            "history: ERROR\r\n"+
            "history: !initial\r\n"+
            "history: onComplete\r\n",response);
        assertContains("ERROR: /ctx/path/info",response);
    }

    
    protected void assertContains(String content,String response)
    {
        Assert.assertThat(response,Matchers.containsString(content));
    }
    
    protected void assertNotContains(String content,String response)
    {
        Assert.assertThat(response,Matchers.not(Matchers.containsString(content)));
    }
    
    public synchronized String process(String query,String content) throws Exception
    {
        String request = "GET /ctx/path/info";
        
        if (query!=null)
            request+="?"+query;
        request+=" HTTP/1.1\r\n"+
        "Host: localhost\r\n"+
        "Connection: close\r\n";
        if (content==null)
            request+="\r\n";
        else
        {
            request+="Content-Length: "+content.length()+"\r\n";
            request+="\r\n" + content;
        }
        
        int port=_port;
        String response=null;
        try
        {
            Socket socket = new Socket("localhost",port);
            socket.setSoTimeout(1000000);
            socket.getOutputStream().write(request.getBytes("UTF-8"));

            response = IO.toString(socket.getInputStream());
        }
        catch(Exception e)
        {
            System.err.println("failed on port "+port);
            e.printStackTrace();
            throw e;
        }
        return response;
    }
    
       
    
    private static class AsyncServlet extends HttpServlet
    {
        private static final long serialVersionUID = -8161977157098646562L;
        private Timer _timer=new Timer();
        
        public AsyncServlet()
        {}
        
        /* ------------------------------------------------------------ */
        @Override
        public void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException
        {
            response.addHeader("history",request.getDispatcherType().toString());
            
            int read_before=0;
            long sleep_for=-1;
            long suspend_for=-1;
            long suspend2_for=-1;
            long resume_after=-1;
            long resume2_after=-1;
            long complete_after=-1;
            long complete2_after=-1;
            
            if (request.getParameter("read")!=null)
                read_before=Integer.parseInt(request.getParameter("read"));
            if (request.getParameter("sleep")!=null)
                sleep_for=Integer.parseInt(request.getParameter("sleep"));
            if (request.getParameter("suspend")!=null)
                suspend_for=Integer.parseInt(request.getParameter("suspend"));
            if (request.getParameter("suspend2")!=null)
                suspend2_for=Integer.parseInt(request.getParameter("suspend2"));
            if (request.getParameter("resume")!=null)
                resume_after=Integer.parseInt(request.getParameter("resume"));
            if (request.getParameter("resume2")!=null)
                resume2_after=Integer.parseInt(request.getParameter("resume2"));
            if (request.getParameter("complete")!=null)
                complete_after=Integer.parseInt(request.getParameter("complete"));
            if (request.getParameter("complete2")!=null)
                complete2_after=Integer.parseInt(request.getParameter("complete2"));
            
            if (request.getDispatcherType()==DispatcherType.REQUEST)
            {
                ((HttpServletResponse)response).addHeader("history","initial");
                if (read_before>0)
                {
                    byte[] buf=new byte[read_before];
                    request.getInputStream().read(buf);
                }
                else if (read_before<0)
                {
                    InputStream in = request.getInputStream();
                    int b=in.read();
                    while(b!=-1)
                        b=in.read();
                }

                if (suspend_for>=0)
                {
                    final AsyncContext async=request.startAsync();
                    if (suspend_for>0)
                        async.setTimeout(suspend_for);
                    async.addListener(__listener);
                    ((HttpServletResponse)response).addHeader("history","suspend");
                    
                    if (complete_after>0)
                    {
                        TimerTask complete = new TimerTask()
                        {
                            @Override
                            public void run()
                            {
                                try
                                {
                                    response.setStatus(200);
                                    response.getOutputStream().println("COMPLETED\n");
                                    response.addHeader("history","complete");
                                    async.complete();
                                }
                                catch(Exception e)
                                {
                                    e.printStackTrace();
                                }
                            }
                        };
                        synchronized (_timer)
                        {
                            _timer.schedule(complete,complete_after);
                        }
                    }
                    else if (complete_after==0)
                    {
                        response.setStatus(200);
                        response.getOutputStream().println("COMPLETED\n");
                        response.addHeader("history","complete");
                        async.complete();
                    }
                    else if (resume_after>0)
                    {
                        TimerTask resume = new TimerTask()
                        {
                            @Override
                            public void run()
                            {
                                ((HttpServletResponse)async.getResponse()).addHeader("history","resume");
                                async.dispatch();
                            }
                        };
                        synchronized (_timer)
                        {
                            _timer.schedule(resume,resume_after);
                        }
                    }
                    else if (resume_after==0)
                    {
                        ((HttpServletResponse)async.getResponse()).addHeader("history","resume");
                        async.dispatch();
                    }
                    
                }
                else if (sleep_for>=0)
                {
                    try
                    {
                        Thread.sleep(sleep_for);
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                    response.setStatus(200);
                    response.getOutputStream().println("SLEPT\n");
                }
                else
                {
                    response.setStatus(200);
                    response.getOutputStream().println("NORMAL\n");
                }
            }
            else
            {
                ((HttpServletResponse)response).addHeader("history","!initial");

                if (suspend2_for>=0 && request.getAttribute("2nd")==null)
                {
                    final AsyncContext async=request.startAsync();
                    async.addListener(__listener);
                    request.setAttribute("2nd","cycle");

                    if (suspend2_for>0)
                    {
                        async.setTimeout(suspend2_for);
                    }
                    // continuation.addContinuationListener(__listener);
                    ((HttpServletResponse)response).addHeader("history","suspend");

                    if (complete2_after>0)
                    {
                        TimerTask complete = new TimerTask()
                        {
                            @Override
                            public void run()
                            {
                                try
                                {
                                    response.setStatus(200);
                                    response.getOutputStream().println("COMPLETED\n");
                                    response.addHeader("history","complete");
                                    async.complete();
                                }
                                catch(Exception e)
                                {
                                    e.printStackTrace();
                                }
                            }
                        };
                        synchronized (_timer)
                        {
                            _timer.schedule(complete,complete2_after);
                        }
                    }
                    else if (complete2_after==0)
                    {
                        response.setStatus(200);
                        response.getOutputStream().println("COMPLETED\n");
                        response.addHeader("history","complete");
                        async.complete();
                    }
                    else if (resume2_after>0)
                    {
                        TimerTask resume = new TimerTask()
                        {
                            @Override
                            public void run()
                            {
                                ((HttpServletResponse)response).addHeader("history","resume");
                                async.dispatch();
                            }
                        };
                        synchronized (_timer)
                        {
                            _timer.schedule(resume,resume2_after);
                        }
                    }
                    else if (resume2_after==0)
                    {
                        ((HttpServletResponse)response).addHeader("history","dispatch");
                        async.dispatch();
                    }
                }
                else if(request.getDispatcherType()==DispatcherType.ERROR)
                {
                    response.getOutputStream().println("ERROR: "+request.getContextPath()+request.getServletPath()+request.getPathInfo());
                }
                else
                {
                    response.setStatus(200);
                    response.getOutputStream().println("DISPATCHED");
                }
            }
        }
    }
    
    
    private static AsyncListener __listener = new AsyncListener()
    {
        @Override
        public void onTimeout(AsyncEvent event) throws IOException
        {            
            ((HttpServletResponse)event.getSuppliedResponse()).addHeader("history","onTimeout");
            String action=((HttpServletRequest)event.getSuppliedRequest()).getParameter("timeout");
            if (action!=null)
            {
                ((HttpServletResponse)event.getSuppliedResponse()).addHeader("history",action);
                if ("dispatch".equals(action))
                    event.getAsyncContext().dispatch();
                if ("complete".equals(action))
                {
                    ((HttpServletResponse)event.getSuppliedResponse()).getOutputStream().println("COMPLETED\n");
                    event.getAsyncContext().complete();
                }
            }
        }
        
        @Override
        public void onStartAsync(AsyncEvent event) throws IOException
        {
            // TODO Auto-generated method stub
            
        }
        
        @Override
        public void onError(AsyncEvent event) throws IOException
        {
            // TODO Auto-generated method stub
            
        }
        
        @Override
        public void onComplete(AsyncEvent event) throws IOException
        {
            ((HttpServletResponse)event.getSuppliedResponse()).addHeader("history","onComplete");
        }
    };

}
