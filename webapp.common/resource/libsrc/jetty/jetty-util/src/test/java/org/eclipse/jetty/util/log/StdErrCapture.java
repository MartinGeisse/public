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

package org.eclipse.jetty.util.log;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.Assert;

public class StdErrCapture
{
    private ByteArrayOutputStream test;
    private PrintStream err;

    public StdErrCapture(StdErrLog log)
    {
        this();
        log.setStdErrStream(err);
    }

    public StdErrCapture()
    {
        test = new ByteArrayOutputStream();
        err = new PrintStream(test);
    }

    public void capture(StdErrLog log)
    {
        log.setStdErrStream(err);
    }

    public void assertContains(String expectedString)
    {
        err.flush();
        String output = new String(test.toByteArray());
        Assert.assertThat(output,containsString(expectedString));
    }

    public void assertNotContains(String unexpectedString)
    {
        err.flush();
        String output = new String(test.toByteArray());
        Assert.assertThat(output,not(containsString(unexpectedString)));
    }
    
    public String toString()
    {
        err.flush();
        return new String(test.toByteArray());
    }
}
