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
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.GenericServlet;
import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletRequestWrapper;
import javax.servlet.ServletResponse;
import javax.servlet.ServletResponseWrapper;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import junit.framework.Assert;

import org.eclipse.jetty.server.Dispatcher;
import org.eclipse.jetty.server.LocalConnector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.toolchain.test.MavenTestingUtils;
import org.eclipse.jetty.util.TypeUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DispatcherTest
{
    private Server _server;
    private LocalConnector _connector;
    private ContextHandlerCollection _contextCollection;
    private ServletContextHandler _contextHandler;
    private ResourceHandler _resourceHandler;
    
    @Before
    public void init() throws Exception
    {
        _server = new Server();
        _server.setSendServerVersion(false);
        _connector = new LocalConnector();
        
        _contextCollection = new ContextHandlerCollection();
        _contextHandler = new ServletContextHandler();
        _contextHandler.setContextPath("/context");
        _contextCollection.addHandler(_contextHandler);
        _resourceHandler = new ResourceHandler();
        _resourceHandler.setResourceBase(MavenTestingUtils.getTestResourceDir("dispatchResourceTest").getAbsolutePath());
        ContextHandler resourceContextHandler = new ContextHandler("/resource");
        resourceContextHandler.setHandler(_resourceHandler);
        _contextCollection.addHandler(resourceContextHandler);
        _server.setHandler(_contextCollection);
        _server.addConnector( _connector );

        _server.start();
    }

    @After
    public void destroy() throws Exception
    {
        _server.stop();
        _server.join();
    }

    @Test
    public void testForward() throws Exception
    {
        _contextHandler.addServlet(ForwardServlet.class, "/ForwardServlet/*");
        _contextHandler.addServlet(AssertForwardServlet.class, "/AssertForwardServlet/*");

        String expected=
            "HTTP/1.1 200 OK\r\n"+
            "Content-Type: text/html\r\n"+
            "Content-Length: 0\r\n"+
            "\r\n";

        String responses = _connector.getResponses("GET /context/ForwardServlet?do=assertforward&do=more&test=1 HTTP/1.1\n" + "Host: localhost\n\n");

        assertEquals(expected, responses);
    }
    
 

   @Test public void testForwardNonUTF8() throws Exception
    {
        _contextHandler.addServlet(ForwardNonUTF8Servlet.class, "/ForwardServlet/*");
        _contextHandler.addServlet(AssertNonUTF8ForwardServlet.class, "/AssertForwardServlet/*");
        
        String expected=
            "HTTP/1.1 200 OK\r\n"+
            "Content-Type: text/html\r\n"+
            "Content-Length: 0\r\n"+
            "\r\n";
        String responses = _connector.getResponses("GET /context/ForwardServlet?do=assertforward&foreign=%d2%e5%ec%ef%e5%f0%e0%f2%f3%f0%e0&test=1 HTTP/1.1\n" + "Host: localhost\n\n");

        assertEquals(expected, responses);
    }
    
    @Test
    public void testForwardWithParam() throws Exception
    {
        _contextHandler.addServlet(ForwardServlet.class, "/ForwardServlet/*");
        _contextHandler.addServlet(EchoURIServlet.class, "/EchoURI/*");

        String expected=
            "HTTP/1.1 200 OK\r\n"+
            "Content-Type: text/plain\r\n"+
            "Content-Length: 54\r\n"+
            "\r\n"+
            "/context\r\n"+
            "/EchoURI\r\n"+
            "/x x\r\n"+
            "/context/EchoURI/x%20x;a=1\r\n";

        String responses = _connector.getResponses("GET /context/ForwardServlet;ignore=true?do=req.echo&uri=EchoURI%2Fx%2520x%3Ba=1%3Fb=2 HTTP/1.1\n" + "Host: localhost\n\n");

        assertEquals(expected, responses);
    }

    @Test
    public void testInclude() throws Exception
    {
        _contextHandler.addServlet(IncludeServlet.class, "/IncludeServlet/*");
        _contextHandler.addServlet(AssertIncludeServlet.class, "/AssertIncludeServlet/*");

        String expected=
            "HTTP/1.1 200 OK\r\n"+
            "Content-Length: 0\r\n"+
            "\r\n";

        String responses = _connector.getResponses("GET /context/IncludeServlet?do=assertinclude&do=more&test=1 HTTP/1.1\n" + "Host: localhost\n\n");

        assertEquals(expected, responses);
    }

    @Test
    public void testForwardThenInclude() throws Exception
    {
        _contextHandler.addServlet(ForwardServlet.class, "/ForwardServlet/*");
        _contextHandler.addServlet(IncludeServlet.class, "/IncludeServlet/*");
        _contextHandler.addServlet(AssertForwardIncludeServlet.class, "/AssertForwardIncludeServlet/*");

        String expected=
            "HTTP/1.1 200 OK\r\n"+
            "Content-Length: 0\r\n"+
            "\r\n";

        String responses = _connector.getResponses("GET /context/ForwardServlet/forwardpath?do=include HTTP/1.1\n" + "Host: localhost\n\n");

        assertEquals(expected, responses);
    }

    @Test
    public void testIncludeThenForward() throws Exception
    {
        _contextHandler.addServlet(IncludeServlet.class, "/IncludeServlet/*");
        _contextHandler.addServlet(ForwardServlet.class, "/ForwardServlet/*");
        _contextHandler.addServlet(AssertIncludeForwardServlet.class, "/AssertIncludeForwardServlet/*");


        String expected=
            "HTTP/1.1 200 OK\r\n"+
            "Transfer-Encoding: chunked\r\n"+
            "\r\n"+
            "0\r\n"+
            "\r\n";

        String responses = _connector.getResponses("GET /context/IncludeServlet/includepath?do=forward HTTP/1.1\n" + "Host: localhost\n\n");

        assertEquals(expected, responses);
    }
    
    @Test
    public void testServletForward() throws Exception
    {
        _contextHandler.addServlet(DispatchServletServlet.class, "/dispatch/*");
        _contextHandler.addServlet(RogerThatServlet.class, "/roger/*");

        String expected=
            "HTTP/1.1 200 OK\r\n"+
            "Content-Length: 11\r\n"+
            "\r\n"+
            "Roger That!";

        String responses = _connector.getResponses("GET /context/dispatch/test?forward=/roger/that HTTP/1.0\n" + "Host: localhost\n\n");

        assertEquals(expected, responses);
    }
    
    @Test
    public void testServletInclude() throws Exception
    {
        _contextHandler.addServlet(DispatchServletServlet.class, "/dispatch/*");
        _contextHandler.addServlet(RogerThatServlet.class, "/roger/*");

        String expected=
            "HTTP/1.1 200 OK\r\n"+
            "Content-Length: 11\r\n"+
            "\r\n"+
            "Roger That!";

        String responses = _connector.getResponses("GET /context/dispatch/test?include=/roger/that HTTP/1.0\n" + "Host: localhost\n\n");

        assertEquals(expected, responses);
    }

    @Test
    public void testWorkingResourceHandler() throws Exception
    {        
        String responses = _connector.getResponses("GET /resource/content.txt HTTP/1.0\n" + "Host: localhost\n\n");
        
        assertTrue(responses.contains("content goes here")); // from inside the context.txt file
    }
    
    @Test
    public void testIncludeToResourceHandler() throws Exception
    {
        _contextHandler.addServlet(DispatchToResourceServlet.class, "/resourceServlet/*");

        String responses = _connector.getResponses("GET /context/resourceServlet/content.txt?do=include HTTP/1.0\n" + "Host: localhost\n\n");      
        
        // from inside the context.txt file
        Assert.assertNotNull(responses);
        
        assertTrue(responses.contains("content goes here"));
    }
    
    @Test
    public void testForwardToResourceHandler() throws Exception
    {
        _contextHandler.addServlet(DispatchToResourceServlet.class, "/resourceServlet/*");

        String responses = _connector.getResponses("GET /context/resourceServlet/content.txt?do=forward HTTP/1.0\n" + "Host: localhost\n\n");      
        
        // from inside the context.txt file
        assertTrue(responses.contains("content goes here"));
    }
    
    @Test
    public void testWrappedIncludeToResourceHandler() throws Exception
    {
        _contextHandler.addServlet(DispatchToResourceServlet.class, "/resourceServlet/*");

        String responses = _connector.getResponses("GET /context/resourceServlet/content.txt?do=include&wrapped=true HTTP/1.0\n" + "Host: localhost\n\n");      
        
        // from inside the context.txt file
        assertTrue(responses.contains("content goes here"));
    }
    
    @Test
    public void testWrappedForwardToResourceHandler() throws Exception
    {
        _contextHandler.addServlet(DispatchToResourceServlet.class, "/resourceServlet/*");

        String responses = _connector.getResponses("GET /context/resourceServlet/content.txt?do=forward&wrapped=true HTTP/1.0\n" + "Host: localhost\n\n");      
        
        // from inside the context.txt file
        assertTrue(responses.contains("content goes here"));
    }
    
    @Test
    public void testForwardFilterToRogerServlet() throws Exception
    {
        _contextHandler.addServlet(RogerThatServlet.class, "/*");
        _contextHandler.addServlet(ReserveEchoServlet.class,"/recho/*");
        _contextHandler.addServlet(EchoServlet.class, "/echo/*");
        _contextHandler.addFilter(ForwardFilter.class, "/*", EnumSet.of(DispatcherType.REQUEST));

        String rogerResponse = _connector.getResponses("GET /context/ HTTP/1.0\n" + "Host: localhost\n\n");

        String echoResponse = _connector.getResponses("GET /context/foo?echo=echoText HTTP/1.0\n" + "Host: localhost\n\n");
        
        String rechoResponse = _connector.getResponses("GET /context/?echo=echoText HTTP/1.0\n" + "Host: localhost\n\n");
                    
        assertTrue(rogerResponse.contains("Roger That!"));
        assertTrue(echoResponse.contains("echoText"));
        assertTrue(rechoResponse.contains("txeTohce"));
    }
    
    
    public static class ForwardServlet extends HttpServlet implements Servlet
    {
        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
        {
            RequestDispatcher dispatcher = null;

            if(request.getParameter("do").equals("include"))
                dispatcher = getServletContext().getRequestDispatcher("/IncludeServlet/includepath?do=assertforwardinclude");
            else if(request.getParameter("do").equals("assertincludeforward"))
                dispatcher = getServletContext().getRequestDispatcher("/AssertIncludeForwardServlet/assertpath?do=end");
          
            else if(request.getParameter("do").equals("assertforward"))
                dispatcher = getServletContext().getRequestDispatcher("/AssertForwardServlet?do=end&do=the");
            else if(request.getParameter("do").equals("ctx.echo"))
                dispatcher = getServletContext().getRequestDispatcher(request.getParameter("uri"));
            else if(request.getParameter("do").equals("req.echo"))
                dispatcher = request.getRequestDispatcher(request.getParameter("uri"));
            dispatcher.forward(request, response);
        }
    }
    
    
    public static class ForwardNonUTF8Servlet extends HttpServlet implements Servlet
    {
        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
        {
            RequestDispatcher dispatcher = null;
            request.setAttribute("org.eclipse.jetty.server.Request.queryEncoding", "cp1251");          
            dispatcher = getServletContext().getRequestDispatcher("/AssertForwardServlet?do=end&else=%D0%B2%D1%8B%D0%B1%D1%80%D0%B0%D0%BD%D0%BE%3D%D0%A2%D0%B5%D0%BC%D0%BF%D0%B5%D1%80%D0%B0%D1%82%D1%83%D1%80%D0%B0");
            dispatcher.forward(request, response);
        }
    }
    
    /*
     * Forward filter works with roger, echo and reverse echo servlets to test various 
     * forwarding bits using filters.
     * 
     * when there is an echo parameter and the path info is / it forwards to the reverse echo
     * anything else in the pathInfo and it sends straight to the echo servlet...otherwise its
     * all roger servlet
     */
    public static class ForwardFilter implements Filter
    {
        ServletContext servletContext;
        
        public void init(FilterConfig filterConfig) throws ServletException
        {
            servletContext = filterConfig.getServletContext().getContext("/context");
        }

        public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
        {
            
            if ( servletContext == null || !(request instanceof HttpServletRequest) || !(response instanceof HttpServletResponse))
            {
                chain.doFilter(request,response);
                return;
            }
            
            HttpServletRequest req = (HttpServletRequest)request;
            HttpServletResponse resp = (HttpServletResponse)response;
             
            if ( req.getParameter("echo") != null && "/".equals(req.getPathInfo()))
            {
                RequestDispatcher dispatcher = servletContext.getRequestDispatcher("/recho");
                dispatcher.forward(request,response);
            }
            else if ( req.getParameter("echo") != null )
            {
                RequestDispatcher dispatcher = servletContext.getRequestDispatcher("/echo");
                dispatcher.forward(request,response);
            }
            else
            {
                chain.doFilter(request,response);
                return;
            }
        }

        public void destroy()
        {
            
        }       
    }
    
    
    public static class DispatchServletServlet extends HttpServlet implements Servlet
    {
        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
        {
            RequestDispatcher dispatcher = null;

            if(request.getParameter("include")!=null)
            {
                dispatcher = getServletContext().getRequestDispatcher(request.getParameter("include"));
                dispatcher.include(new ServletRequestWrapper(request), new ServletResponseWrapper(response));
            }
            else if(request.getParameter("forward")!=null)
            {
                dispatcher = getServletContext().getRequestDispatcher(request.getParameter("forward"));
                dispatcher.forward(new ServletRequestWrapper(request), new ServletResponseWrapper(response));
            }
            
        }
    }


    public static class IncludeServlet extends HttpServlet implements Servlet
    {
        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
        {
            RequestDispatcher dispatcher = null;

            if(request.getParameter("do").equals("forward"))
                dispatcher = getServletContext().getRequestDispatcher("/ForwardServlet/forwardpath?do=assertincludeforward");
            else if(request.getParameter("do").equals("assertforwardinclude"))
                dispatcher = getServletContext().getRequestDispatcher("/AssertForwardIncludeServlet/assertpath?do=end");
            else if(request.getParameter("do").equals("assertinclude"))
                dispatcher = getServletContext().getRequestDispatcher("/AssertIncludeServlet?do=end&do=the");
            dispatcher.include(request, response);
        }
    }
    
    public static class RogerThatServlet extends GenericServlet
    {
        @Override
        public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException
        {
            res.getWriter().print("Roger That!");
        }
    }

    public static class EchoServlet extends GenericServlet
    {
        @Override
        public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException
        {
            String echoText = req.getParameter("echo");
            
            if ( echoText == null )
            {
                throw new ServletException("echo is a required parameter");
            }
            else
            {
                res.getWriter().print(echoText);
            }
        }
    }
    
    public static class ReserveEchoServlet extends GenericServlet
    {
        @Override
        public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException
        {
            String echoText = req.getParameter("echo");
            
            if ( echoText == null )
            {
                throw new ServletException("echo is a required parameter");
            }
            else
            {
                res.getWriter().print(new StringBuffer(echoText).reverse().toString());
            }
        }
    }
    
    public static class DispatchToResourceServlet extends HttpServlet implements Servlet
    {
        @Override
        public void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException
        {
            ServletContext targetContext = getServletConfig().getServletContext().getContext("/resource");        
        
            RequestDispatcher dispatcher = targetContext.getRequestDispatcher(req.getPathInfo());
            
            if ( "true".equals(req.getParameter("wrapped")))
            {                
                if (req.getParameter("do").equals("forward"))
                {
                    dispatcher.forward(new HttpServletRequestWrapper(req),new HttpServletResponseWrapper(res));
                }
                else if (req.getParameter("do").equals("include"))
                {
                    dispatcher.include(new HttpServletRequestWrapper(req),new HttpServletResponseWrapper(res));
                }
                else
                {
                    throw new ServletException("type of forward or include is required");
                }
            }
            else
            {
                if (req.getParameter("do").equals("forward"))
                {
                    dispatcher.forward(req,res);
                }
                else if (req.getParameter("do").equals("include"))
                {
                    dispatcher.include(req,res);
                }
                else
                {
                    throw new ServletException("type of forward or include is required");
                }
            }
        }
    }
    
    public static class EchoURIServlet extends HttpServlet implements Servlet
    {
        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
        {
            response.setContentType("text/plain");
            response.setStatus(HttpServletResponse.SC_OK);
            response.getOutputStream().println(request.getContextPath());
            response.getOutputStream().println(request.getServletPath());
            response.getOutputStream().println(request.getPathInfo());
            response.getOutputStream().println(request.getRequestURI());
        }
    }
    
    public static class AssertForwardServlet extends HttpServlet implements Servlet
    {
        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
        {
            assertEquals( "/context/ForwardServlet", request.getAttribute(Dispatcher.FORWARD_REQUEST_URI));
            assertEquals( "/context", request.getAttribute(Dispatcher.FORWARD_CONTEXT_PATH) );
            assertEquals( "/ForwardServlet", request.getAttribute(Dispatcher.FORWARD_SERVLET_PATH));
            assertEquals( null, request.getAttribute(Dispatcher.FORWARD_PATH_INFO));
            assertEquals( "do=assertforward&do=more&test=1", request.getAttribute(Dispatcher.FORWARD_QUERY_STRING) );

            List<String> expectedAttributeNames = Arrays.asList(Dispatcher.FORWARD_REQUEST_URI, Dispatcher.FORWARD_CONTEXT_PATH,
                    Dispatcher.FORWARD_SERVLET_PATH, Dispatcher.FORWARD_QUERY_STRING);
            List<String> requestAttributeNames = Collections.list(request.getAttributeNames());
            assertTrue(requestAttributeNames.containsAll(expectedAttributeNames));

            assertEquals(null, request.getPathInfo());
            assertEquals(null, request.getPathTranslated());
            assertEquals("do=end&do=the&test=1", request.getQueryString());
            assertEquals("/context/AssertForwardServlet", request.getRequestURI());
            assertEquals("/context", request.getContextPath());
            assertEquals("/AssertForwardServlet", request.getServletPath());

            response.setContentType("text/html");
            response.setStatus(HttpServletResponse.SC_OK);
        }
    }
    
    
    
    public static class AssertNonUTF8ForwardServlet extends HttpServlet implements Servlet
    {
        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
        {
            byte[] cp1251_bytes = TypeUtil.fromHexString("d2e5ecefe5f0e0f2f3f0e0");
            String expectedCP1251String = new String(cp1251_bytes, "cp1251");
            
            assertEquals( "/context/ForwardServlet", request.getAttribute(Dispatcher.FORWARD_REQUEST_URI));
            assertEquals( "/context", request.getAttribute(Dispatcher.FORWARD_CONTEXT_PATH) );
            assertEquals( "/ForwardServlet", request.getAttribute(Dispatcher.FORWARD_SERVLET_PATH));
            assertEquals( null, request.getAttribute(Dispatcher.FORWARD_PATH_INFO));
            assertEquals( "do=assertforward&foreign=%d2%e5%ec%ef%e5%f0%e0%f2%f3%f0%e0&test=1", request.getAttribute(Dispatcher.FORWARD_QUERY_STRING) );

            List<String> expectedAttributeNames = Arrays.asList(Dispatcher.FORWARD_REQUEST_URI, Dispatcher.FORWARD_CONTEXT_PATH,
                    Dispatcher.FORWARD_SERVLET_PATH, Dispatcher.FORWARD_QUERY_STRING);
            List<String> requestAttributeNames = Collections.list(request.getAttributeNames());
            assertTrue(requestAttributeNames.containsAll(expectedAttributeNames));

            assertEquals(null, request.getPathInfo());
            assertEquals(null, request.getPathTranslated());           
            assertTrue(request.getQueryString().startsWith("do=end&else=%D0%B2%D1%8B%D0%B1%D1%80%D0%B0%D0%BD%D0%BE%3D%D0%A2%D0%B5%D0%BC%D0%BF%D0%B5%D1%80%D0%B0%D1%82%D1%83%D1%80%D0%B0&test=1&foreign="));                                                  
            
            String[] vals = request.getParameterValues("foreign");
            assertTrue(vals!=null);
            assertEquals(1, vals.length);
            assertEquals(expectedCP1251String, vals[0]);
            
            assertEquals("/context/AssertForwardServlet", request.getRequestURI());
            assertEquals("/context", request.getContextPath());
            assertEquals("/AssertForwardServlet", request.getServletPath());

            response.setContentType("text/html");
            response.setStatus(HttpServletResponse.SC_OK);
        }
    }
    

    public static class AssertIncludeServlet extends HttpServlet implements Servlet
    {
        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
        {
            assertEquals( "/context/AssertIncludeServlet", request.getAttribute(Dispatcher.INCLUDE_REQUEST_URI));
            assertEquals( "/context", request.getAttribute(Dispatcher.INCLUDE_CONTEXT_PATH) );
            assertEquals( "/AssertIncludeServlet", request.getAttribute(Dispatcher.INCLUDE_SERVLET_PATH));
            assertEquals( null, request.getAttribute(Dispatcher.INCLUDE_PATH_INFO));
            assertEquals( "do=end&do=the", request.getAttribute(Dispatcher.INCLUDE_QUERY_STRING));

            List expectedAttributeNames = Arrays.asList(Dispatcher.INCLUDE_REQUEST_URI, Dispatcher.INCLUDE_CONTEXT_PATH,
                    Dispatcher.INCLUDE_SERVLET_PATH, Dispatcher.INCLUDE_QUERY_STRING);
            List requestAttributeNames = Collections.list(request.getAttributeNames());
            assertTrue(requestAttributeNames.containsAll(expectedAttributeNames));

            assertEquals(null, request.getPathInfo());
            assertEquals(null, request.getPathTranslated());
            assertEquals("do=assertinclude&do=more&test=1", request.getQueryString());
            assertEquals("/context/IncludeServlet", request.getRequestURI());
            assertEquals("/context", request.getContextPath());
            assertEquals("/IncludeServlet", request.getServletPath());

            response.setContentType("text/html");
            response.setStatus(HttpServletResponse.SC_OK);
        }
    }

    public static class AssertForwardIncludeServlet extends HttpServlet implements Servlet
    {
        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
        {
            // include doesn't hide forward
            assertEquals( "/context/ForwardServlet/forwardpath", request.getAttribute(Dispatcher.FORWARD_REQUEST_URI));
            assertEquals( "/context", request.getAttribute(Dispatcher.FORWARD_CONTEXT_PATH) );
            assertEquals( "/ForwardServlet", request.getAttribute(Dispatcher.FORWARD_SERVLET_PATH));
            assertEquals( "/forwardpath", request.getAttribute(Dispatcher.FORWARD_PATH_INFO));
            assertEquals( "do=include", request.getAttribute(Dispatcher.FORWARD_QUERY_STRING) );

            assertEquals( "/context/AssertForwardIncludeServlet/assertpath", request.getAttribute(Dispatcher.INCLUDE_REQUEST_URI));
            assertEquals( "/context", request.getAttribute(Dispatcher.INCLUDE_CONTEXT_PATH) );
            assertEquals( "/AssertForwardIncludeServlet", request.getAttribute(Dispatcher.INCLUDE_SERVLET_PATH));
            assertEquals( "/assertpath", request.getAttribute(Dispatcher.INCLUDE_PATH_INFO));
            assertEquals( "do=end", request.getAttribute(Dispatcher.INCLUDE_QUERY_STRING));

            List expectedAttributeNames = Arrays.asList(Dispatcher.FORWARD_REQUEST_URI, Dispatcher.FORWARD_CONTEXT_PATH, Dispatcher.FORWARD_SERVLET_PATH,
                    Dispatcher.FORWARD_PATH_INFO, Dispatcher.FORWARD_QUERY_STRING,
                    Dispatcher.INCLUDE_REQUEST_URI, Dispatcher.INCLUDE_CONTEXT_PATH, Dispatcher.INCLUDE_SERVLET_PATH,
                    Dispatcher.INCLUDE_PATH_INFO, Dispatcher.INCLUDE_QUERY_STRING);
            List requestAttributeNames = Collections.list(request.getAttributeNames());
            assertTrue(requestAttributeNames.containsAll(expectedAttributeNames));

            assertEquals("/includepath", request.getPathInfo());
            assertEquals(null, request.getPathTranslated());
            assertEquals("do=assertforwardinclude", request.getQueryString());
            assertEquals("/context/IncludeServlet/includepath", request.getRequestURI());
            assertEquals("/context", request.getContextPath());
            assertEquals("/IncludeServlet", request.getServletPath());

            response.setContentType("text/html");
            response.setStatus(HttpServletResponse.SC_OK);
        }
     }

    public static class AssertIncludeForwardServlet extends HttpServlet implements Servlet
    {
        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
        {
            // forward hides include
            assertEquals( null, request.getAttribute(Dispatcher.INCLUDE_REQUEST_URI));
            assertEquals( null, request.getAttribute(Dispatcher.INCLUDE_CONTEXT_PATH) );
            assertEquals( null, request.getAttribute(Dispatcher.INCLUDE_SERVLET_PATH));
            assertEquals( null, request.getAttribute(Dispatcher.INCLUDE_PATH_INFO));
            assertEquals( null, request.getAttribute(Dispatcher.INCLUDE_QUERY_STRING));

            assertEquals( "/context/IncludeServlet/includepath", request.getAttribute(Dispatcher.FORWARD_REQUEST_URI));
            assertEquals( "/context", request.getAttribute(Dispatcher.FORWARD_CONTEXT_PATH) );
            assertEquals( "/IncludeServlet", request.getAttribute(Dispatcher.FORWARD_SERVLET_PATH));
            assertEquals( "/includepath", request.getAttribute(Dispatcher.FORWARD_PATH_INFO));
            assertEquals( "do=forward", request.getAttribute(Dispatcher.FORWARD_QUERY_STRING) );

            List expectedAttributeNames = Arrays.asList(Dispatcher.FORWARD_REQUEST_URI, Dispatcher.FORWARD_CONTEXT_PATH, Dispatcher.FORWARD_SERVLET_PATH,
                    Dispatcher.FORWARD_PATH_INFO, Dispatcher.FORWARD_QUERY_STRING);
            List requestAttributeNames = Collections.list(request.getAttributeNames());
            assertTrue(requestAttributeNames.containsAll(expectedAttributeNames));

            assertEquals("/assertpath", request.getPathInfo());
            assertEquals(null, request.getPathTranslated());
            assertEquals("do=end", request.getQueryString());
            assertEquals("/context/AssertIncludeForwardServlet/assertpath", request.getRequestURI());
            assertEquals("/context", request.getContextPath());
            assertEquals("/AssertIncludeForwardServlet", request.getServletPath());

            response.setContentType("text/html");
            response.setStatus(HttpServletResponse.SC_OK);
        }
    }
}
