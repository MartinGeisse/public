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

package org.eclipse.jetty.spdy.frames;

import java.nio.ByteBuffer;

import org.eclipse.jetty.spdy.StandardByteBufferPool;
import org.eclipse.jetty.spdy.StandardCompressionFactory;
import org.eclipse.jetty.spdy.api.SPDY;
import org.eclipse.jetty.spdy.generator.Generator;
import org.eclipse.jetty.spdy.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

public class GoAwayGenerateParseTest
{
    @Test
    public void testGenerateParse() throws Exception
    {
        int lastStreamId = 13;
        int statusCode = 1;
        GoAwayFrame frame1 = new GoAwayFrame(SPDY.V3, lastStreamId, statusCode);
        Generator generator = new Generator(new StandardByteBufferPool(), new StandardCompressionFactory().newCompressor());
        ByteBuffer buffer = generator.control(frame1);

        Assert.assertNotNull(buffer);

        TestSPDYParserListener listener = new TestSPDYParserListener();
        Parser parser = new Parser(new StandardCompressionFactory().newDecompressor());
        parser.addListener(listener);
        parser.parse(buffer);
        ControlFrame frame2 = listener.getControlFrame();

        Assert.assertNotNull(frame2);
        Assert.assertEquals(ControlFrameType.GO_AWAY, frame2.getType());
        GoAwayFrame goAway = (GoAwayFrame)frame2;
        Assert.assertEquals(SPDY.V3, goAway.getVersion());
        Assert.assertEquals(lastStreamId, goAway.getLastStreamId());
        Assert.assertEquals(0, goAway.getFlags());
        Assert.assertEquals(statusCode, goAway.getStatusCode());
    }

    @Test
    public void testGenerateParseOneByteAtATime() throws Exception
    {
        int lastStreamId = 13;
        int statusCode = 1;
        GoAwayFrame frame1 = new GoAwayFrame(SPDY.V3, lastStreamId, statusCode);
        Generator generator = new Generator(new StandardByteBufferPool(), new StandardCompressionFactory().newCompressor());
        ByteBuffer buffer = generator.control(frame1);

        Assert.assertNotNull(buffer);

        TestSPDYParserListener listener = new TestSPDYParserListener();
        Parser parser = new Parser(new StandardCompressionFactory().newDecompressor());
        parser.addListener(listener);
        while (buffer.hasRemaining())
            parser.parse(ByteBuffer.wrap(new byte[]{buffer.get()}));
        ControlFrame frame2 = listener.getControlFrame();

        Assert.assertNotNull(frame2);
        Assert.assertEquals(ControlFrameType.GO_AWAY, frame2.getType());
        GoAwayFrame goAway = (GoAwayFrame)frame2;
        Assert.assertEquals(SPDY.V3, goAway.getVersion());
        Assert.assertEquals(lastStreamId, goAway.getLastStreamId());
        Assert.assertEquals(0, goAway.getFlags());
        Assert.assertEquals(statusCode, goAway.getStatusCode());
    }
}
