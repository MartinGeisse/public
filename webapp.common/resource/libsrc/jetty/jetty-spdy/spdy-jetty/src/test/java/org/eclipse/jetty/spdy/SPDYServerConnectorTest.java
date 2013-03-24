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


package org.eclipse.jetty.spdy;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import junit.framework.Assert;
import org.eclipse.jetty.spdy.api.GoAwayInfo;
import org.eclipse.jetty.spdy.api.Session;
import org.eclipse.jetty.spdy.api.SessionFrameListener;
import org.junit.Test;

public class SPDYServerConnectorTest extends AbstractTest
{
    @Test
    public void testStoppingServerConnectorSendsGoAway() throws Exception
    {
        final CountDownLatch latch = new CountDownLatch(1);
        startClient(startServer(null), new SessionFrameListener.Adapter()
        {
            @Override
            public void onGoAway(Session session, GoAwayInfo goAwayInfo)
            {
                latch.countDown();
            }
        });

        // Sleep a while to avoid the connector is
        // stopped before a session can be opened
        TimeUnit.SECONDS.sleep(1);

        connector.stop();

        Assert.assertTrue(latch.await(5, TimeUnit.SECONDS));
        Assert.assertTrue(connector.getSessions().isEmpty());
    }

    @Test
    public void testSessionClosedIsRemovedFromServerConnector() throws Exception
    {
        Session session = startClient(startServer(null), null);

        session.goAway().get(5, TimeUnit.SECONDS);

        // Sleep a while to allow the connector to remove the session
        // since it is done asynchronously by the selector thread
        TimeUnit.SECONDS.sleep(1);

        Assert.assertTrue(connector.getSessions().isEmpty());
    }
}
