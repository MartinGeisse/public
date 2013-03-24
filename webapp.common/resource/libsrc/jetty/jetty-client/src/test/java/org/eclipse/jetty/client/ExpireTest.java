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

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test contributed by: Michiel Thuys for JETTY-806
 */
public class ExpireTest
{
    private Server server;
    private HttpClient client;
    private int port;

    @Before
    public void init() throws Exception
    {
        server = new Server();
        SelectChannelConnector connector = new SelectChannelConnector();
        connector.setHost("localhost");
        connector.setPort(0);
        server.addConnector(connector);
        server.setHandler(new AbstractHandler()
        {
            public void handle(String target, Request request, HttpServletRequest httpRequest, HttpServletResponse httpResponse)
                    throws IOException, ServletException
            {
                request.setHandled(true);
                try
                {
                    Thread.sleep(2000);
                }
                catch (InterruptedException x)
                {
                }
            }
        });
        server.start();
        port = connector.getLocalPort();

        client = new HttpClient();
        client.setConnectorType(HttpClient.CONNECTOR_SELECT_CHANNEL);
        client.setTimeout(200);
        client.setMaxRetries(0);
        client.setMaxConnectionsPerAddress(100);
        client.start();
    }

    @After
    public void destroy() throws Exception
    {
        client.stop();
        server.stop();
        server.join();
    }

    @Test
    public void testExpire() throws Exception
    {
        String baseUrl = "http://" + "localhost" + ":" + port + "/";

        int count = 200;
        final CountDownLatch expires = new CountDownLatch(count);

        for (int i=0;i<count;i++)
        {
            final ContentExchange exchange = new ContentExchange()
            {
                @Override
                protected void onExpire()
                {
                    expires.countDown();
                }
            };
            exchange.setMethod("GET");
            exchange.setURL(baseUrl);

            client.send(exchange);
        }

        // Wait to be sure that all exchanges have expired
        assertTrue(expires.await(5, TimeUnit.SECONDS));
    }
}
