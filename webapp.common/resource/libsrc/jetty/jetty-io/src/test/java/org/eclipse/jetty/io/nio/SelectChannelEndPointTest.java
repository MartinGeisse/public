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

package org.eclipse.jetty.io.nio;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.greaterThan;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.eclipse.jetty.io.AbstractConnection;
import org.eclipse.jetty.io.AsyncEndPoint;
import org.eclipse.jetty.io.ConnectedEndPoint;
import org.eclipse.jetty.io.EndPoint;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SelectChannelEndPointTest
{
    protected SelectChannelEndPoint _lastEndp;
    protected ServerSocketChannel _connector;
    protected QueuedThreadPool _threadPool = new QueuedThreadPool();
    protected SelectorManager _manager = new SelectorManager()
    {
        @Override
        public boolean dispatch(Runnable task)
        {
            return _threadPool.dispatch(task);
        }

        @Override
        protected void endPointClosed(SelectChannelEndPoint endpoint)
        {
        }

        @Override
        protected void endPointOpened(SelectChannelEndPoint endpoint)
        {
        }

        @Override
        protected void endPointUpgraded(ConnectedEndPoint endpoint, org.eclipse.jetty.io.Connection oldConnection)
        {
        }

        @Override
        public AsyncConnection newConnection(SocketChannel channel, AsyncEndPoint endpoint, Object attachment)
        {
            return SelectChannelEndPointTest.this.newConnection(channel,endpoint);
        }

        @Override
        protected SelectChannelEndPoint newEndPoint(SocketChannel channel, SelectSet selectSet, SelectionKey key) throws IOException
        {
            SelectChannelEndPoint endp = new SelectChannelEndPoint(channel,selectSet,key,2000);
            endp.setConnection(selectSet.getManager().newConnection(channel,endp, key.attachment()));
            _lastEndp=endp;
            return endp;
        }
    };

    // Must be volatile or the test may fail spuriously
    private volatile int _blockAt=0;

    @Before
    public void startManager() throws Exception
    {
        _connector = ServerSocketChannel.open();
        _connector.socket().bind(null);
        _threadPool.start();
        _manager.start();
    }

    @After
    public void stopManager() throws Exception
    {
        _manager.stop();
        _threadPool.stop();
        _connector.close();
    }

    protected Socket newClient() throws IOException
    {
        return new Socket(_connector.socket().getInetAddress(),_connector.socket().getLocalPort());
    }

    protected AsyncConnection newConnection(SocketChannel channel, EndPoint endpoint)
    {
        return new TestConnection(endpoint);
    }

    public class TestConnection extends AbstractConnection implements AsyncConnection
    {
        NIOBuffer _in = new IndirectNIOBuffer(32*1024);
        NIOBuffer _out = new IndirectNIOBuffer(32*1024);

        public TestConnection(EndPoint endp)
        {
            super(endp);
        }

        public org.eclipse.jetty.io.Connection handle() throws IOException
        {
            boolean progress=true;
            while(progress)
            {
                progress=false;
                _in.compact();
                if (_in.space()>0 && _endp.fill(_in)>0)
                    progress=true;

                while (_blockAt>0 && _in.length()>0 && _in.length()<_blockAt)
                {
                    _endp.blockReadable(10000);
                    if (_in.space()>0 && _endp.fill(_in)>0)
                        progress=true;
                }

                if (_in.hasContent() && _in.skip(_out.put(_in))>0)
                    progress=true;

                if (_out.hasContent() && _endp.flush(_out)>0)
                    progress=true;

                _out.compact();

                if (!_out.hasContent() && _endp.isInputShutdown())
                    _endp.shutdownOutput();
            }
            return this;
        }

        public boolean isIdle()
        {
            return false;
        }

        public boolean isSuspended()
        {
            return false;
        }

        public void onClose()
        {
            // System.err.println("onClose");
        }

        public void onInputShutdown() throws IOException
        {
            // System.err.println("onInputShutdown");
        }

    }

    @Test
    public void testEcho() throws Exception
    {
        Socket client = newClient();

        client.setSoTimeout(500);

        SocketChannel server = _connector.accept();
        server.configureBlocking(false);

        _manager.register(server);

        // Write client to server
        client.getOutputStream().write("HelloWorld".getBytes("UTF-8"));

        // Verify echo server to client
        for (char c : "HelloWorld".toCharArray())
        {
            int b = client.getInputStream().read();
            assertTrue(b>0);
            assertEquals(c,(char)b);
        }

        // wait for read timeout
        long start=System.currentTimeMillis();
        try
        {
            client.getInputStream().read();
            Assert.fail();
        }
        catch(SocketTimeoutException e)
        {
            long duration = System.currentTimeMillis()-start;
            Assert.assertThat("timeout duration", duration, greaterThanOrEqualTo(400L));
        }

        // write then shutdown
        client.getOutputStream().write("Goodbye Cruel TLS".getBytes("UTF-8"));

        // Verify echo server to client
        for (char c : "Goodbye Cruel TLS".toCharArray())
        {
            int b = client.getInputStream().read();
            Assert.assertThat("expect valid char integer", b, greaterThan(0));
            assertEquals("expect characters to be same", c,(char)b);
        }
        client.close();

        int i=0;
        while (server.isOpen())
        {
            assert(i++<10);
            Thread.sleep(10);
        }

    }


    @Test
    public void testShutdown() throws Exception
    {
        Socket client = newClient();

        client.setSoTimeout(500);

        SocketChannel server = _connector.accept();
        server.configureBlocking(false);

        _manager.register(server);

        // Write client to server
        client.getOutputStream().write("HelloWorld".getBytes("UTF-8"));

        // Verify echo server to client
        for (char c : "HelloWorld".toCharArray())
        {
            int b = client.getInputStream().read();
            assertTrue(b>0);
            assertEquals(c,(char)b);
        }

        // wait for read timeout
        long start=System.currentTimeMillis();
        try
        {
            client.getInputStream().read();
            Assert.fail();
        }
        catch(SocketTimeoutException e)
        {
            assertTrue(System.currentTimeMillis()-start>=400);
        }

        // write then shutdown
        client.getOutputStream().write("Goodbye Cruel TLS".getBytes("UTF-8"));
        client.shutdownOutput();


        // Verify echo server to client
        for (char c : "Goodbye Cruel TLS".toCharArray())
        {
            int b = client.getInputStream().read();
            assertTrue(b>0);
            assertEquals(c,(char)b);
        }

        // Read close
        assertEquals(-1,client.getInputStream().read());

    }



    @Test
    public void testBlockIn() throws Exception
    {
        Socket client = newClient();

        SocketChannel server = _connector.accept();
        server.configureBlocking(false);

        _manager.register(server);

        OutputStream clientOutputStream = client.getOutputStream();
        InputStream clientInputStream = client.getInputStream();

        int specifiedTimeout = 400;
        client.setSoTimeout(specifiedTimeout);

        // Write 8 and cause block for 10
        _blockAt=10;
        clientOutputStream.write("12345678".getBytes("UTF-8"));
        clientOutputStream.flush();

        Thread.sleep(2 * specifiedTimeout);

        // No echo as blocking for 10
        long start=System.currentTimeMillis();
        try
        {
            int b = clientInputStream.read();
            Assert.fail("Should have timed out waiting for a response, but read "+b);
        }
        catch(SocketTimeoutException e)
        {
            int elapsed = Long.valueOf(System.currentTimeMillis() - start).intValue();
            System.err.println("blocked for " + elapsed+ "ms");
            Assert.assertThat("Expected timeout", elapsed, greaterThanOrEqualTo(3*specifiedTimeout/4));
        }

        // write remaining characters
        clientOutputStream.write("90ABCDEF".getBytes("UTF-8"));
        clientOutputStream.flush();

        // Verify echo server to client
        for (char c : "1234567890ABCDEF".toCharArray())
        {
            int b = clientInputStream.read();
            assertTrue(b>0);
            assertEquals(c,(char)b);
        }
    }
    
    @Test
    public void testIdle() throws Exception
    {
        Socket client = newClient();

        client.setSoTimeout(3000);

        SocketChannel server = _connector.accept();
        server.configureBlocking(false);

        _manager.register(server);

        // Write client to server
        client.getOutputStream().write("HelloWorld".getBytes("UTF-8"));

        // Verify echo server to client
        for (char c : "HelloWorld".toCharArray())
        {
            int b = client.getInputStream().read();
            assertTrue(b>0);
            assertEquals(c,(char)b);
        }

        // Set Max idle
        _lastEndp.setMaxIdleTime(500);

        // read until idle shutdown received
        long start=System.currentTimeMillis();
        int b=client.getInputStream().read();
        assertEquals(-1,b);
        long idle=System.currentTimeMillis()-start;
        assertThat(idle,Matchers.greaterThan(400L));
        assertThat(idle,Matchers.lessThan(3000L));
        
        if (_lastEndp.isOpen())
        {
            // half close so wait another idle period
            assertTrue(_lastEndp.isOutputShutdown());
            Thread.sleep(2000);
        }
        
        // endpoint is closed.
        assertFalse(_lastEndp.isOpen());
        
    }



    @Test
    public void testStress() throws Exception
    {
        Socket client = newClient();
        client.setSoTimeout(30000);

        SocketChannel server = _connector.accept();
        server.configureBlocking(false);

        _manager.register(server);
        int writes = 100000;

        final byte[] bytes="HelloWorld".getBytes("UTF-8");
        final CountDownLatch latch = new CountDownLatch(writes);
        final InputStream in = new BufferedInputStream(client.getInputStream());
        final long start = System.currentTimeMillis();
        client.getOutputStream().write(bytes);
        client.getOutputStream().flush();

        new Thread()
        {
            public void run()
            {
                try
                {
                    while (latch.getCount()>0)
                    {
                        // Verify echo server to client
                        for (byte b0 : bytes)
                        {
                            int b = in.read();
                            assertTrue(b>0);
                            assertEquals(0xff&b0,b);
                        }
                        latch.countDown();
                    }
                }
                catch(Throwable e)
                {
                    System.err.println("latch="+latch.getCount());
                    System.err.println("time="+(System.currentTimeMillis()-start));
                    e.printStackTrace();
                }
            }
        }.start();


        // Write client to server
        for (int i=1;i<writes;i++)
        {
            client.getOutputStream().write(bytes);
            Thread.yield();
        }
        client.getOutputStream().flush();

        assertTrue(latch.await(100,TimeUnit.SECONDS));
    }
}
