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

package org.eclipse.jetty.server.session;

import org.junit.Test;

/**
 * SessionExpiryTest
 *
 *
 *
 */
public class SessionExpiryTest extends AbstractSessionExpiryTest
{

    /** 
     * @see org.eclipse.jetty.server.session.AbstractSessionExpiryTest#createServer(int, int, int)
     */
    @Override
    public AbstractTestServer createServer(int port, int max, int scavenge)
    {
        return new JdbcTestServer(port,max,scavenge);
    }

    @Test
    public void testSessionExpiry() throws Exception
    {
        super.testSessionExpiry();
    }

    @Test
    public void testSessionNotExpired() throws Exception
    {
        super.testSessionNotExpired();
    }

    
}
