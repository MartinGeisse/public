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

package org.eclipse.jetty.client;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.client.security.Realm;
import org.eclipse.jetty.client.security.SimpleRealmResolver;
import org.eclipse.jetty.http.HttpMethods;
import org.eclipse.jetty.http.HttpStatus;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


/* ------------------------------------------------------------ */
public class HttpGetRedirectTest
{
    private static String _content =
        "Lorem ipsum dolor sit amet, consectetur adipiscing elit. In quis felis nunc. "+
        "Quisque suscipit mauris et ante auctor ornare rhoncus lacus aliquet. Pellentesque "+
        "habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. "+
        "Vestibulum sit amet felis augue, vel convallis dolor. Cras accumsan vehicula diam "+
        "at faucibus. Etiam in urna turpis, sed congue mi. Morbi et lorem eros. Donec vulputate "+
        "velit in risus suscipit lobortis. Aliquam id urna orci, nec sollicitudin ipsum. "+
        "Cras a orci turpis. Donec suscipit vulputate cursus. Mauris nunc tellus, fermentum "+
        "eu auctor ut, mollis at diam. Quisque porttitor ultrices metus, vitae tincidunt massa "+
        "sollicitudin a. Vivamus porttitor libero eget purus hendrerit cursus. Integer aliquam "+
        "consequat mauris quis luctus. Cras enim nibh, dignissim eu faucibus ac, mollis nec neque. "+
        "Aliquam purus mauris, consectetur nec convallis lacinia, porta sed ante. Suspendisse "+
        "et cursus magna. Donec orci enim, molestie a lobortis eu, imperdiet vitae neque.";

    private File _docRoot;
    private Server _server;
    private HttpClient _client;
    private Realm _realm;
    private String _protocol;
    private String _requestUrl;
    private String _requestUrl2;
    private RedirectHandler _handler;

    /* ------------------------------------------------------------ */
    @Before
    public void setUp()
        throws Exception
    {
        _docRoot = new File("target/test-output/docroot/");
        _docRoot.mkdirs();
        _docRoot.deleteOnExit();

        _server = new Server();
        configureServer(_server);
        org.eclipse.jetty.server.bio.SocketConnector connector = new org.eclipse.jetty.server.bio.SocketConnector();
        _server.addConnector(connector);
        _server.start();

        int port = _server.getConnectors()[0].getLocalPort();
        _requestUrl = _protocol+"://localhost:"+port+ "/content.txt";
        
        _handler._toURL=_protocol+"://localhost:"+connector.getLocalPort()+ "/moved.txt";
    }

    /* ------------------------------------------------------------ */
    @After
    public void tearDown()
        throws Exception
    {
        if (_server != null)
        {
            _server.stop();
            _server = null;
        }
    }

    /* ------------------------------------------------------------ */
    @Test
    public void testGet() throws Exception
    {
        startClient(_realm);
        
        ContentExchange getExchange = new ContentExchange();
        getExchange.setURL(_requestUrl);
        getExchange.setMethod(HttpMethods.GET);

        _client.send(getExchange);
        int state = getExchange.waitForDone();

        String content = "";
        int responseStatus = getExchange.getResponseStatus();
        if (responseStatus == HttpStatus.OK_200)
        {
            content = getExchange.getResponseContent();
        }

        assertEquals(HttpStatus.OK_200,responseStatus);
        assertEquals(_content,content);
        
        stopClient();
    }

    /* ------------------------------------------------------------ */
    protected void configureServer(Server server)
        throws Exception
    {
        setProtocol("http");

        SelectChannelConnector connector = new SelectChannelConnector();
        server.addConnector(connector);

        _handler = new RedirectHandler(HttpStatus.MOVED_PERMANENTLY_301, "/content.txt", "WAIT FOR IT", 2);
        server.setHandler( _handler );

    }

    /* ------------------------------------------------------------ */
    protected void startClient(Realm realm)
        throws Exception
    {
        _client = new HttpClient();
        _client.setConnectorType(HttpClient.CONNECTOR_SELECT_CHANNEL);
        _client.registerListener("org.eclipse.jetty.client.RedirectListener");
        if (realm != null)
            _client.setRealmResolver(new SimpleRealmResolver(realm));
        _client.start();
    }

    /* ------------------------------------------------------------ */
    protected void stopClient()
        throws Exception
    {
        if (_client != null)
        {
            _client.stop();
            _client = null;
        }
    }

    /* ------------------------------------------------------------ */
    protected String getBasePath()
    {
        return _docRoot.getAbsolutePath();
    }

    /* ------------------------------------------------------------ */
    protected void setProtocol(String protocol)
    {
        _protocol = protocol;
    }

    /* ------------------------------------------------------------ */
    protected void setRealm(Realm realm)
    {
        _realm = realm;
    }


    /* ------------------------------------------------------------ */
    private static class RedirectHandler
        extends AbstractHandler
    {
        private final String _fromURI;
        private final int _code;
        private final int _maxRedirects;
        private int _redirectCount = 0;
        private String _toURL;

        /* ------------------------------------------------------------ */
        public RedirectHandler( final int code, final String fromURI, final String toURL, final int maxRedirects )
        {
            this._code = code;
            this._fromURI = fromURI;
            this._toURL = toURL;
            this._maxRedirects = maxRedirects;
            
            if (_fromURI==null || _toURL==null)
                throw new IllegalArgumentException();
            
        }

        /* ------------------------------------------------------------ */
        public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException
        {
            if ( baseRequest.isHandled() )
            {
                return;
            }

            if (request.getRequestURI().equals(_fromURI))
            {
                _redirectCount++;

                String location = ( _redirectCount <= _maxRedirects )?_fromURI:_toURL;

                response.setStatus( _code );
                response.setHeader( "Location", location );
                
                ( (Request) request ).setHandled( true );
            }
            else
            {
                PrintWriter out = response.getWriter();
                out.write(_content);

                baseRequest.setHandled( true );
            }
        }
    }
}
