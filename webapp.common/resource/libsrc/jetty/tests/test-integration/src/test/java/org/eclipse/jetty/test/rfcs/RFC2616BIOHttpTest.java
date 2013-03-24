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

package org.eclipse.jetty.test.rfcs;

import org.eclipse.jetty.http.HttpSchemes;
import org.eclipse.jetty.test.support.TestableJettyServer;
import org.eclipse.jetty.test.support.rawhttp.HttpSocket;
import org.eclipse.jetty.test.support.rawhttp.HttpSocketImpl;
import org.junit.BeforeClass;

/**
 * Perform the RFC2616 tests against a server running with the Jetty BIO Connector and listening on standard HTTP.
 */
public class RFC2616BIOHttpTest extends RFC2616BaseTest
{
    @BeforeClass
    public static void setupServer() throws Exception
    {
        TestableJettyServer server = new TestableJettyServer();
        server.setScheme(HttpSchemes.HTTP);
        server.addConfiguration("RFC2616Base.xml");
        server.addConfiguration("RFC2616_Redirects.xml");
        server.addConfiguration("RFC2616_Filters.xml");
        server.addConfiguration("BIOHttp.xml");
        setUpServer(server, RFC2616BIOHttpTest.class);
    }

    @Override
    public HttpSocket getHttpClientSocket()
    {
        return new HttpSocketImpl();
    }
}
