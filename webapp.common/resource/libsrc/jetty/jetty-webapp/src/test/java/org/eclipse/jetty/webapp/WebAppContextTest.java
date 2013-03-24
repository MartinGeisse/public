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

package org.eclipse.jetty.webapp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.servlet.GenericServlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import junit.framework.Assert;

import org.eclipse.jetty.server.LocalConnector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.util.resource.ResourceCollection;
import org.junit.Test;

public class WebAppContextTest
{
    @Test
    public void testConfigurationClassesFromDefault ()
    {
        Server server = new Server();
        //test if no classnames set, its the defaults
        WebAppContext wac = new WebAppContext();
        assertNull(wac.getConfigurations());
        String[] classNames = wac.getConfigurationClasses();
        assertNotNull(classNames);

        //test if no classname set, and none from server its the defaults
        wac.setServer(server);
        assertTrue(Arrays.equals(classNames, wac.getConfigurationClasses()));
    }

    @Test
    public void testConfigurationClassesExplicit ()
    {
        String[] classNames = {"x.y.z"};

        Server server = new Server();
        server.setAttribute(WebAppContext.SERVER_CONFIG, classNames);

        //test an explicitly set classnames list overrides that from the server
        WebAppContext wac = new WebAppContext();
        String[] myClassNames = {"a.b.c", "d.e.f"};
        wac.setConfigurationClasses(myClassNames);
        wac.setServer(server);
        String[] names = wac.getConfigurationClasses();
        assertTrue(Arrays.equals(myClassNames, names));


        //test if no explicit classnames, they come from the server
        WebAppContext wac2 = new WebAppContext();
        wac2.setServer(server);
        assertTrue(Arrays.equals(classNames, wac2.getConfigurationClasses()));
    }

    @Test
    public void testConfigurationInstances ()
    {
        Configuration[] configs = {new WebInfConfiguration()};
        WebAppContext wac = new WebAppContext();
        wac.setConfigurations(configs);
        assertTrue(Arrays.equals(configs, wac.getConfigurations()));

        //test that explicit config instances override any from server
        String[] classNames = {"x.y.z"};
        Server server = new Server();
        server.setAttribute(WebAppContext.SERVER_CONFIG, classNames);
        wac.setServer(server);
        assertTrue(Arrays.equals(configs,wac.getConfigurations()));
    }
    
    @Test
    public void testRealPathDoesNotExist() throws Exception
    {
        Server server = new Server(0);
        WebAppContext context = new WebAppContext(".", "/");
        server.setHandler(context);
        server.start();

        ServletContext ctx = context.getServletContext();
        assertNotNull(ctx.getRealPath("/doesnotexist"));
        assertNotNull(ctx.getRealPath("/doesnotexist/"));
    }
    
    /**
     * tests that the servlet context white list works
     * 
     * @throws Exception
     */
    @Test 
    public void testContextWhiteList() throws Exception
    {
        Server server = new Server(0);
        HandlerList handlers = new HandlerList();
        WebAppContext contextA = new WebAppContext(".", "/A"); 
        
        contextA.addServlet( ServletA.class, "/s");
        handlers.addHandler(contextA);
        WebAppContext contextB = new WebAppContext(".", "/B");
        
        contextB.addServlet(ServletB.class, "/s");
        contextB.setContextWhiteList(new String [] { "/doesnotexist", "/B/s" } );
        handlers.addHandler(contextB);
        
        server.setHandler(handlers);
        server.start();
        
        // context A should be able to get both A and B servlet contexts
        Assert.assertNotNull(contextA.getServletHandler().getServletContext().getContext("/A/s"));
        Assert.assertNotNull(contextA.getServletHandler().getServletContext().getContext("/B/s"));

        // context B has a contextWhiteList set and should only be able to get ones that are approved
        Assert.assertNull(contextB.getServletHandler().getServletContext().getContext("/A/s"));
        Assert.assertNotNull(contextB.getServletHandler().getServletContext().getContext("/B/s"));
    }
    

    @Test 
    public void testAlias() throws Exception
    {
        File dir = File.createTempFile("dir",null);
        dir.delete();
        dir.mkdir();
        dir.deleteOnExit();
        
        File webinf = new File(dir,"WEB-INF");
        webinf.mkdir();
        
        File classes = new File(dir,"classes");
        classes.mkdir();
        
        File someclass = new File(classes,"SomeClass.class");
        someclass.createNewFile();
        
        WebAppContext context = new WebAppContext();
        context.setBaseResource(new ResourceCollection(dir.getAbsolutePath()));
        
        context.setResourceAlias("/WEB-INF/classes/", "/classes/");

        assertTrue(Resource.newResource(context.getServletContext().getResource("/WEB-INF/classes/SomeClass.class")).exists());
        assertTrue(Resource.newResource(context.getServletContext().getResource("/classes/SomeClass.class")).exists());

    }
    
    
    @Test
    public void testIsProtected() throws Exception
    {
        WebAppContext context = new WebAppContext();
        assertTrue(context.isProtectedTarget("/web-inf/lib/foo.jar"));
        assertTrue(context.isProtectedTarget("/meta-inf/readme.txt"));
        assertFalse(context.isProtectedTarget("/something-else/web-inf"));
    }
    
    
    @Test
    public void testNullPath() throws Exception
    {
        Server server = new Server(0);
        HandlerList handlers = new HandlerList();
        ContextHandlerCollection contexts = new ContextHandlerCollection();
        WebAppContext context = new WebAppContext();
        context.setBaseResource(Resource.newResource("./src/test/webapp"));
        context.setContextPath("/");
        server.setHandler(handlers);
        handlers.addHandler(contexts);
        contexts.addHandler(context);
        
        LocalConnector connector = new LocalConnector();
        server.addConnector(connector);
        
        server.start();
        try
        {
            String response = connector.getResponses("GET http://localhost:8080 HTTP/1.1\r\nHost: localhost:8080\r\nConnection: close\r\n\r\n");
            Assert.assertTrue(response.indexOf("200 OK")>=0);
        }
        finally
        {
            server.stop();
        }
    }
    
    
    class ServletA extends GenericServlet
    {
        @Override
        public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException
        {
            this.getServletContext().getContext("/A/s");
        }      
    }
    
    class ServletB extends GenericServlet
    {
        @Override
        public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException
        {
            this.getServletContext().getContext("/B/s");
        }      
    }
}
