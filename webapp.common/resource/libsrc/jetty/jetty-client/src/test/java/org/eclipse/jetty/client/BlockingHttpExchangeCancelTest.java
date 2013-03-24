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

import org.junit.After;
import org.junit.Before;


/* ------------------------------------------------------------ */
/**
 * @version $Revision$ $Date$
 */
public class BlockingHttpExchangeCancelTest extends AbstractHttpExchangeCancelTest
{
    private HttpClient httpClient;

    /* ------------------------------------------------------------ */
    @Before
    @Override
    public void setUp() throws Exception
    {
        super.setUp();
        httpClient = new HttpClient();
        httpClient.setConnectorType(HttpClient.CONNECTOR_SOCKET);
        httpClient.start();
    }

    /* ------------------------------------------------------------ */
    @After
    @Override
    public void tearDown() throws Exception
    {
        httpClient.stop();
        super.tearDown();
    }

    /* ------------------------------------------------------------ */
    @Override
    protected HttpClient getHttpClient()
    {
        return httpClient;
    }
   
}
