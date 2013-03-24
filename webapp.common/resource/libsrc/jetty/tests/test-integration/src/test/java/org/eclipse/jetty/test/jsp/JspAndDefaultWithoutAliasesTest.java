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

package org.eclipse.jetty.test.jsp;

import static org.hamcrest.Matchers.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.jetty.security.HashLoginService;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.toolchain.test.MavenTestingUtils;
import org.eclipse.jetty.util.IO;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;


/**
 * Test various paths for JSP resources that tickle various java.io.File bugs to get around the JspServlet matching, that then flows to the DefaultServlet to be
 * served as source files.
 */
@RunWith(Parameterized.class)
public class JspAndDefaultWithoutAliasesTest
{
    private static final Logger LOG = Log.getLogger(JspAndDefaultWithAliasesTest.class);
    private static Server server;
    private static URI serverURI;

    @Parameters
    public static Collection<Object[]> data()
    {
        List<Object[]> data = new ArrayList<Object[]>();
        
        double javaVersion = Double.parseDouble(System.getProperty("java.specification.version"));

        // @formatter:off
        data.add(new Object[] { "/dump.jsp" });
        data.add(new Object[] { "/dump.jsp%00" });
        data.add(new Object[] { "/dump.jsp%00x" });
        data.add(new Object[] { "/dump.jsp%00x/dump.jsp" });
        data.add(new Object[] { "/dump.jsp%00/dump.jsp" });
        data.add(new Object[] { "/dump.jsp%00/index.html" });

        if (javaVersion >= 1.7) 
        {
            data.add(new Object[] { "/dump.jsp%00/" });
            data.add(new Object[] { "/dump.jsp%00x/" });
        }
        // @formatter:on

        return data;
    }

    @BeforeClass
    public static void startServer() throws Exception
    {
        server = new Server();
        SelectChannelConnector connector = new SelectChannelConnector();
        connector.setPort(0);
        server.addConnector(connector);

        // Configure LoginService
        HashLoginService login = new HashLoginService();
        login.setName("Test Realm");
        File realmFile = MavenTestingUtils.getTestResourceFile("realm.properties");
        login.setConfig(realmFile.getAbsolutePath());
        server.addBean(login);

        // Configure WebApp
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        File webappBase = MavenTestingUtils.getTestResourceDir("docroots/jsp");
        context.setResourceBase(webappBase.getAbsolutePath());
        context.setClassLoader(Thread.currentThread().getContextClassLoader());

        // add default servlet
        ServletHolder defaultServHolder = context.addServlet(DefaultServlet.class,"/");
        defaultServHolder.setInitParameter("aliases","false"); // important! must be FALSE

        // add jsp
        ServletHolder jsp = new ServletHolder(new FakeJspServlet());
        context.addServlet(jsp,"*.jsp");        
        jsp.setInitParameter("classpath",context.getClassPath());

        // add context
        server.setHandler(context);

        server.start();

        serverURI = new URI("http://localhost:" + connector.getLocalPort() + "/");
    }

    @AfterClass
    public static void stopServer() throws Exception
    {
        server.stop();
    }

    private String path;
    
    public JspAndDefaultWithoutAliasesTest(String encodedRequestPath)
    {
        LOG.info("Path \"" + encodedRequestPath + "\"");
        this.path = encodedRequestPath;
    }
    
    private void assertProcessedByJspServlet(HttpURLConnection conn) throws IOException
    {
        // make sure that jsp actually ran, and didn't just get passed onto
        // the default servlet to return the jsp source
        String body = getResponseBody(conn);
        Assert.assertThat("Body",body,not(containsString("<%@")));
        Assert.assertThat("Body",body,not(containsString("<jsp:")));
    }

    private void assertResponse(HttpURLConnection conn) throws IOException
    {
        if (conn.getResponseCode() == 200)
        {
            // Serving content is allowed, but it better be the processed JspServlet
            assertProcessedByJspServlet(conn);
            return;
        }

        // Of other possible paths, only 404 Not Found is expected
        Assert.assertThat("Response Code",conn.getResponseCode(),is(404));
    }

    @Test
    public void testGetReference() throws Exception
    {
        URI uri = serverURI.resolve(path);

        HttpURLConnection conn = null;
        try
        {
            conn = (HttpURLConnection)uri.toURL().openConnection();
            conn.setConnectTimeout(1000);
            conn.setReadTimeout(1000);
            assertResponse(conn);
        }
        finally
        {
            conn.disconnect();
        }
    }

    protected String getResponseBody(HttpURLConnection conn) throws IOException
    {
        InputStream in = null;
        try
        {
            in = conn.getInputStream();
            return IO.toString(in);
        }
        finally
        {
            IO.close(in);
        }
    }
}
