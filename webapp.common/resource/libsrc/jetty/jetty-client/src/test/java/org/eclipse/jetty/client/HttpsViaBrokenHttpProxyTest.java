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

import java.io.IOException;
import java.net.ProtocolException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.http.HttpStatus;
import org.eclipse.jetty.server.AbstractHttpConnection;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ConnectHandler;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/* ------------------------------------------------------------ */
/**
 * This UnitTest class executes two tests. Both will send a http request to https://google.com through a misbehaving proxy server.
 * <p/>
 * The first test runs against a proxy which simply closes the connection (as nginx does) for a connect request. The second proxy server always responds with a
 * 500 error.
 * <p/>
 * The expected result for both tests is an exception and the HttpExchange should have status HttpExchange.STATUS_EXCEPTED.
 */
public class HttpsViaBrokenHttpProxyTest
{
    private Server _proxy = new Server();
    private HttpClient _client = new HttpClient();

    @Before
    public void init() throws Exception
    {
        // setup proxies with different behaviour
        _proxy.addConnector(new SelectChannelConnector());
        _proxy.setHandler(new BadBehavingConnectHandler());
        _proxy.start();
        int proxyClosingConnectionPort = _proxy.getConnectors()[0].getLocalPort();

        _client.setProxy(new Address("localhost", proxyClosingConnectionPort));
        _client.start();
    }

    @After
    public void destroy() throws Exception
    {
        _client.stop();
        _proxy.stop();
    }

    @Test
    public void httpsViaProxyThatClosesConnectionOnConnectRequestTest() throws Exception
    {
        sendRequestThroughProxy(new ContentExchange()
        {

            @Override
            protected void onException(Throwable x)
            {
                
            }
            
        }, "close", 9);
    }

    @Test
    public void httpsViaProxyThatReturns500ErrorTest() throws Exception
    {
        HttpExchange exchange = new ContentExchange()
        {
            @Override
            protected void onException(Throwable x)
            {
                // Suppress logging for expected exception
                if (!(x instanceof ProtocolException))
                    super.onException(x);
            }
        };
        sendRequestThroughProxy(exchange, "error500", 9);
    }

    @Test
    public void httpsViaProxyThatReturns504ErrorTest() throws Exception
    {
        sendRequestThroughProxy(new ContentExchange(), "error504", 8);
    }

    private void sendRequestThroughProxy(HttpExchange exchange, String desiredBehaviour, int exptectedStatus) throws Exception
    {
        String url = "https://" + desiredBehaviour + ".com/";
        exchange.setURL(url);
        exchange.addRequestHeader("behaviour", desiredBehaviour);
        _client.send(exchange);
        assertEquals(HttpExchange.toState(exptectedStatus) + " status awaited", exptectedStatus, exchange.waitForDone());
    }

    private class BadBehavingConnectHandler extends ConnectHandler
    {
        @Override
        protected void handleConnect(Request baseRequest, HttpServletRequest request, HttpServletResponse response, String serverAddress)
                throws ServletException, IOException
        {
            if (serverAddress.contains("close"))
            {
                AbstractHttpConnection.getCurrentConnection().getEndPoint().close();
            }
            else if (serverAddress.contains("error500"))
            {
                response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR_500);
            }
            else if (serverAddress.contains("error504"))
            {
                response.setStatus(HttpStatus.GATEWAY_TIMEOUT_504);
            }
            baseRequest.setHandled(true);
        }
    }
}
