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

package org.eclipse.jetty.server;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.eclipse.jetty.io.EndPoint;
import org.eclipse.jetty.io.nio.SelectChannelEndPoint;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.util.IO;
import org.junit.Test;

public class SelectChannelAsyncContextTest extends LocalAsyncContextTest
{
    volatile SelectChannelEndPoint _endp;

    @Override
    protected Connector initConnector()
    {
        return new SelectChannelConnector(){

            @Override
            public void customize(EndPoint endpoint, Request request) throws IOException
            {
                super.customize(endpoint,request);
                _endp=(SelectChannelEndPoint)endpoint;
            }
            
        };
    }

    @Override
    protected String getResponse(String request) throws Exception
    {
        SelectChannelConnector connector = (SelectChannelConnector)_connector;
        Socket socket = new Socket((String)null,connector.getLocalPort());
        socket.getOutputStream().write(request.getBytes("UTF-8"));
        return IO.toString(socket.getInputStream());
    }

    @Test
    public void testSuspendResumeWithAsyncDispatch() throws Exception
    {
        // Test that suspend/resume works in the face of spurious asyncDispatch call that may be
        // produced by the SslConnection
        final AtomicBoolean running = new AtomicBoolean(true);
        Thread thread = new Thread()
        {
            public void run()
            {
                while (running.get())
                {
                    try 
                    {
                        TimeUnit.MILLISECONDS.sleep(200);
                        SelectChannelEndPoint endp=_endp;
                        if (endp!=null && endp.isOpen())
                            endp.asyncDispatch();
                    }
                    catch(Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        };
        
        try
        {
            thread.start();
            testSuspendResume();
        }
        finally
        {
            running.set(false);
            thread.join();
        }
    }
}
