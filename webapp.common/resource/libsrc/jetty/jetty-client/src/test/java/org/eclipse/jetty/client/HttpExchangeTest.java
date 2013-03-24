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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.matchers.JUnitMatchers.containsString;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.jetty.client.helperClasses.HttpServerAndClientCreator;
import org.eclipse.jetty.client.helperClasses.ServerAndClientCreator;
import org.eclipse.jetty.client.security.ProxyAuthorization;
import org.eclipse.jetty.http.HttpFields;
import org.eclipse.jetty.http.HttpMethods;
import org.eclipse.jetty.http.HttpStatus;
import org.eclipse.jetty.io.Buffer;
import org.eclipse.jetty.io.ByteArrayBuffer;
import org.eclipse.jetty.io.Connection;
import org.eclipse.jetty.io.EofException;
import org.eclipse.jetty.io.nio.DirectNIOBuffer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.toolchain.test.Stress;
import org.eclipse.jetty.util.component.AbstractLifeCycle;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/* ------------------------------------------------------------ */
/**
 * Functional testing for HttpExchange.
 */
public class HttpExchangeTest
{
    final static boolean verbose=HttpExchange.LOG.isDebugEnabled();
    protected static int _maxConnectionsPerAddress = 2;
    protected static String _scheme = "http";
    protected static Server _server;
    protected static int _port;
    protected static HttpClient _httpClient;
    protected static AtomicInteger _count = new AtomicInteger();
    protected static ServerAndClientCreator serverAndClientCreator = new HttpServerAndClientCreator();

    protected static URI getBaseURI()
    {
        return URI.create(_scheme + "://localhost:" + _port + "/");
    }

    /* ------------------------------------------------------------ */
    // TODO work out why BeforeClass does not work here?
    @Before
    public void setUpOnce() throws Exception
    {
        _scheme = "http";
        _server = serverAndClientCreator.createServer();
        _httpClient = serverAndClientCreator.createClient(3000L,3500L,2000);
        _port = _server.getConnectors()[0].getLocalPort();
    }

    /* ------------------------------------------------------------ */
    @After
    public void tearDownOnce() throws Exception
    {
        _httpClient.stop();
        long startTime = System.currentTimeMillis();
        while (!_httpClient.getState().equals(AbstractLifeCycle.STOPPED))
        {
            if (System.currentTimeMillis() - startTime > 1000)
                break;
            Thread.sleep(5);
        }
        _server.stop();
    }

    /* ------------------------------------------------------------ */
    @Test
    public void testResetNewExchange() throws Exception
    {
        HttpExchange exchange = new HttpExchange();
        exchange.reset();
    }

    /* ------------------------------------------------------------ */
    @Test
    public void testPerf() throws Exception
    {
        sender(1,false);
        sender(1,true);
        sender(10,false);
        sender(10,true);

        if (Stress.isEnabled())
        {
            sender(100,false);
            sender(100,true);
            sender(10000,false);
            sender(10000,true);
        }
    }

    /* ------------------------------------------------------------ */
    /**
     * Test sending data through the exchange.
     *
     * @throws IOException
     */
    public void sender(final int nb, final boolean close) throws Exception
    {
        // System.err.printf("%nSENDER %d %s%n",nb,close);
        _count.set(0);
        final CountDownLatch complete = new CountDownLatch(nb);
        final AtomicInteger allcontent = new AtomicInteger(nb);
        HttpExchange[] httpExchange = new HttpExchange[nb];
        long start = System.currentTimeMillis();
        for (int i = 0; i < nb; i++)
        {
            final int n = i;

            httpExchange[n] = new HttpExchange()
            {
                String result = "pending";
                int len = 0;

                /* ------------------------------------------------------------ */
                @Override
                protected void onRequestCommitted()
                {
                    if (verbose)
                        System.err.println(n+" [ "+this);
                    result = "committed";
                }

                /* ------------------------------------------------------------ */
                @Override
                protected void onRequestComplete() throws IOException
                {
                    if (verbose)
                        System.err.println(n+" [ ==");
                    result = "sent";
                }

                @Override
                /* ------------------------------------------------------------ */
                protected void onResponseStatus(Buffer version, int status, Buffer reason)
                {
                    if (verbose)
                        System.err.println(n+" ] "+version+" "+status+" "+reason);
                    result = "status";
                }

                /* ------------------------------------------------------------ */
                @Override
                protected void onResponseHeader(Buffer name, Buffer value)
                {
                    if (verbose)
                        System.err.println(n+" ] "+name+": "+value);
                }

                /* ------------------------------------------------------------ */
                @Override
                protected void onResponseHeaderComplete() throws IOException
                {
                    if (verbose)
                        System.err.println(n+" ] -");
                    result = "content";
                    super.onResponseHeaderComplete();
                }

                /* ------------------------------------------------------------ */
                @Override
                protected void onResponseContent(Buffer content)
                {
                    len += content.length();
                    if (verbose)
                        System.err.println(n+" ] "+content.length()+" -> "+len);
                }

                /* ------------------------------------------------------------ */
                @Override
                protected void onResponseComplete()
                {
                    if (verbose)
                        System.err.println(n+" ] == "+len+" "+complete.getCount()+"/"+nb);
                    result = "complete";
                    if (len == 2009)
                        allcontent.decrementAndGet();
                    else
                        System.err.println(n+ " ONLY " + len+ "/2009");
                    complete.countDown();
                }

                /* ------------------------------------------------------------ */
                @Override
                protected void onConnectionFailed(Throwable ex)
                {
                    if (verbose)
                        System.err.println(n+" ] "+ex);
                    complete.countDown();
                    result = "failed";
                    System.err.println(n+ " FAILED " + ex);
                    super.onConnectionFailed(ex);
                }

                /* ------------------------------------------------------------ */
                @Override
                protected void onException(Throwable ex)
                {
                    if (verbose)
                        System.err.println(n+" ] "+ex);
                    complete.countDown();
                    result = "excepted";
                    System.err.println(n+ " EXCEPTED " + ex);
                    super.onException(ex);
                }

                /* ------------------------------------------------------------ */
                @Override
                protected void onExpire()
                {
                    if (verbose)
                        System.err.println(n+" ] expired");
                    complete.countDown();
                    result = "expired";
                    System.err.println(n + " EXPIRED " + len);
                    super.onExpire();
                }

                /* ------------------------------------------------------------ */
                @Override
                public String toString()
                {
                    return n+"/"+result+"/"+len+"/"+super.toString();
                }
            };

            httpExchange[n].setURI(getBaseURI().resolve("/" + n));
            httpExchange[n].addRequestHeader("arbitrary","value");
            if (close)
                httpExchange[n].setRequestHeader("Connection","close");

            _httpClient.send(httpExchange[n]);
        }

        if (!complete.await(2,TimeUnit.SECONDS))
            System.err.println(_httpClient.dump());

        assertTrue(complete.await(20,TimeUnit.SECONDS));

        assertEquals("nb="+nb+" close="+close,0,allcontent.get());
    }

    /* ------------------------------------------------------------ */
    @Test
    public void testPostWithContentExchange() throws Exception
    {
        for (int i=0;i<20;i++)
        {
            ContentExchange httpExchange=new ContentExchange();
            httpExchange.setURI(getBaseURI());
            httpExchange.setMethod(HttpMethods.POST);
            httpExchange.setRequestContent(new ByteArrayBuffer("<hello />"));
            _httpClient.send(httpExchange);
            int status = httpExchange.waitForDone();
            //httpExchange.waitForStatus(HttpExchange.STATUS_COMPLETED);
            String result=httpExchange.getResponseContent();
            assertEquals(HttpExchange.STATUS_COMPLETED, status);
            assertEquals("i="+i,"<hello />",result);
        }
    }

    /* ------------------------------------------------------------ */
    @Test
    public void testGetWithContentExchange() throws Exception
    {
        for (int i=0;i<10;i++)
        {
            ContentExchange httpExchange=new ContentExchange();
            URI uri = getBaseURI().resolve("?i=" + i);
            httpExchange.setURI(uri);
            httpExchange.setMethod(HttpMethods.GET);
            _httpClient.send(httpExchange);
            int status = httpExchange.waitForDone();
            //httpExchange.waitForStatus(HttpExchange.STATUS_COMPLETED);
            String result=httpExchange.getResponseContent();
            assertNotNull("Should have received response content", result);
            assertEquals("i="+i,0,result.indexOf("<hello>"));
            assertEquals("i="+i,result.length()-10,result.indexOf("</hello>"));
            assertEquals(HttpExchange.STATUS_COMPLETED, status);
            Thread.sleep(5);
        }
    }

    /* ------------------------------------------------------------ */
    @Test
    public void testLocalAddressAvailabilityWithContentExchange() throws Exception
    {
        for (int i=0;i<10;i++)
        {
            ContentExchange httpExchange=new ContentExchange();
            URI uri = getBaseURI().resolve("?i=" + i);
            httpExchange.setURI(uri);
            httpExchange.setMethod(HttpMethods.GET);
            _httpClient.send(httpExchange);
            int status = httpExchange.waitForDone();

            assertNotNull(httpExchange.getLocalAddress());

            String result=httpExchange.getResponseContent();
            assertNotNull("Should have received response content", result);
            assertEquals("i="+i,0,result.indexOf("<hello>"));
            assertEquals("i="+i,result.length()-10,result.indexOf("</hello>"));
            assertEquals(HttpExchange.STATUS_COMPLETED, status);
            Thread.sleep(5);
        }
    }

    /* ------------------------------------------------------------ */
    @Test
    public void testShutdownWithExchange() throws Exception
    {
        final AtomicReference<Throwable> throwable=new AtomicReference<Throwable>();

        HttpExchange httpExchange=new HttpExchange()
        {

            /* ------------------------------------------------------------ */
            /**
             * @see org.eclipse.jetty.client.HttpExchange#onException(java.lang.Throwable)
             */
            @Override
            protected void onException(Throwable x)
            {
                throwable.set(x);
            }

            /* ------------------------------------------------------------ */
            /**
             * @see org.eclipse.jetty.client.HttpExchange#onConnectionFailed(java.lang.Throwable)
             */
            @Override
            protected void onConnectionFailed(Throwable x)
            {
                throwable.set(x);
            }
        };
        httpExchange.setURI(getBaseURI());
        httpExchange.setMethod("SLEEP");
        _httpClient.send(httpExchange);
        new Thread()
        {
            @Override
            public void run()
            {
                try {
                    Thread.sleep(500);
                    _httpClient.stop();
                } catch(Exception e) {e.printStackTrace();}
            }
        }.start();
        int status = httpExchange.waitForDone();

        System.err.println(throwable.get());
        assertTrue(throwable.get().toString().indexOf("close")>=0);
        assertEquals(HttpExchange.STATUS_EXCEPTED, status);
        _httpClient.start();
    }

    /* ------------------------------------------------------------ */
    @Test
    public void testBigPostWithContentExchange() throws Exception
    {
        int size =32;
        ContentExchange httpExchange=new ContentExchange()
        {
            int total;

            @Override
            protected synchronized void onResponseStatus(Buffer version, int status, Buffer reason) throws IOException
            {
                if (verbose)
                    System.err.println("] "+version+" "+status+" "+reason);
                super.onResponseStatus(version,status,reason);
            }

            @Override
            protected synchronized void onResponseHeader(Buffer name, Buffer value) throws IOException
            {
                if (verbose)
                    System.err.println("] "+name+": "+value);
                super.onResponseHeader(name,value);
            }

            @Override
            protected synchronized void onResponseContent(Buffer content) throws IOException
            {
                if (verbose)
                {
                    total+=content.length();
                    System.err.println("] "+content.length()+" -> "+total);
                }
                super.onResponseContent(content);
            }

            @Override
            protected void onRequestComplete() throws IOException
            {
                if (verbose)
                    System.err.println("] ==");
                super.onRequestComplete();
            }

            @Override
            protected void onResponseHeaderComplete() throws IOException
            {
                if (verbose)
                    System.err.println("] --");
                super.onResponseHeaderComplete();
            }

        };

        Buffer babuf = new ByteArrayBuffer(size*36*1024);
        Buffer niobuf = new DirectNIOBuffer(size*36*1024);

        byte[] bytes="0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ".getBytes();

        for (int i=0;i<size*1024;i++)
        {
            babuf.put(bytes);
            niobuf.put(bytes);
        }

        httpExchange.setURI(getBaseURI());
        httpExchange.setMethod(HttpMethods.POST);
        httpExchange.setRequestContentType("application/data");
        httpExchange.setRequestContent(babuf);
        _httpClient.send(httpExchange);

        long start=System.currentTimeMillis();
        while(!httpExchange.isDone())
        {
            long now=System.currentTimeMillis();
            if ((now-start)>=10000)
            {
                System.err.println("TEST IS TAKING TOOOOO LONG!!!!!!!!!!!!!!!!!!!!");
                System.err.println("CLIENT:");
                System.err.println(_httpClient.dump());
                System.err.println("SERVER:");
                _server.dumpStdErr();
                break;
            }
            Thread.sleep(100);
        }
        int status = httpExchange.waitForDone();
        assertEquals(HttpExchange.STATUS_COMPLETED,status);
        String result=httpExchange.getResponseContent();
        assertEquals(babuf.length(),result.length());

        httpExchange.reset();
        httpExchange.setURI(getBaseURI());
        httpExchange.setMethod(HttpMethods.POST);
        httpExchange.setRequestContentType("application/data");
        httpExchange.setRequestContent(niobuf);
        _httpClient.send(httpExchange);

        start=System.currentTimeMillis();
        while(!httpExchange.isDone())
        {
            long now=System.currentTimeMillis();
            if ((now-start)>=10000)
            {
                System.err.println("TEST IS TAKING TOOOOO LONG!!!!!!!!!!!!!!!!!!!!");
                System.err.println("CLIENT:");
                System.err.println(_httpClient.dump());
                System.err.println("SERVER:");
                _server.dumpStdErr();
                break;
            }
            Thread.sleep(100);
        }
        status = httpExchange.waitForDone();
        assertEquals(HttpExchange.STATUS_COMPLETED, status);
        result=httpExchange.getResponseContent();
        assertEquals(niobuf.length(),result.length());
    }

    /* ------------------------------------------------------------ */
    @Test
    public void testSlowPost() throws Exception
    {
        ContentExchange httpExchange=new ContentExchange();
        httpExchange.setURI(getBaseURI());
        httpExchange.setMethod(HttpMethods.POST);

        final String data="012345678901234567890123456789012345678901234567890123456789";

        InputStream content = new InputStream()
        {
            int _index=0;

            @Override
            public int read() throws IOException
            {
                // System.err.printf("reading 1 of %d/%d%n",_index,data.length());
                if (_index>=data.length())
                    return -1;

                try
                {
                    Thread.sleep(5);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }

                // System.err.printf("read 1%n");
                return data.charAt(_index++);
            }

            @Override
            public int read(byte[] b, int off, int len) throws IOException
            {
                // System.err.printf("reading %d of %d/%d%n",len,_index,data.length());
                if (_index >= data.length())
                    return -1;

                try
                {
                    Thread.sleep(25);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }

                int l = 0;

                while (l < 5 && _index < data.length() && l < len)
                    b[off + l++] = (byte)data.charAt(_index++);
                // System.err.printf("read %d%n",l);
                return l;
            }

        };

        httpExchange.setRequestContentSource(content);

        _httpClient.send(httpExchange);

        int status = httpExchange.waitForDone();
        String result = httpExchange.getResponseContent();
        assertEquals(HttpExchange.STATUS_COMPLETED,status);
        assertEquals(data,result);
    }

    /* ------------------------------------------------------------ */
    @Test
    public void testProxy() throws Exception
    {
        if (_scheme.equals("https"))
            return;
        try
        {
            _httpClient.setProxy(new Address("127.0.0.1",_port));
            _httpClient.setProxyAuthentication(new ProxyAuthorization("user","password"));

            ContentExchange httpExchange=new ContentExchange();
            httpExchange.setAddress(new Address("jetty.eclipse.org",8080));
            httpExchange.setMethod(HttpMethods.GET);
            httpExchange.setRequestURI("/jetty-6");
            _httpClient.send(httpExchange);
            int status = httpExchange.waitForDone();
            //httpExchange.waitForStatus(HttpExchange.STATUS_COMPLETED);
            String result=httpExchange.getResponseContent();
            assertNotNull("Should have received response content", result);
            result=result.trim();
            assertEquals(HttpExchange.STATUS_COMPLETED, status);
            assertTrue(result.startsWith("Proxy request: http://jetty.eclipse.org:8080/jetty-6"));
            assertTrue(result.endsWith("Basic dXNlcjpwYXNzd29yZA=="));
        }
        finally
        {
            _httpClient.setProxy(null);
        }
    }


    /* ------------------------------------------------------------ */
    @Test
    public void testReserveConnections () throws Exception
    {
        _httpClient = serverAndClientCreator.createClient(3000L,3500L,2000);
        final HttpDestination destination = _httpClient.getDestination(new Address("localhost",_port),_scheme.equalsIgnoreCase("https"));
        final org.eclipse.jetty.client.AbstractHttpConnection[] connections = new org.eclipse.jetty.client.AbstractHttpConnection[_maxConnectionsPerAddress];
        for (int i = 0; i < _maxConnectionsPerAddress; i++)
        {
            connections[i] = destination.reserveConnection(200);
            assertNotNull(connections[i]);
            HttpExchange ex = new ContentExchange();
            ex.setURI(getBaseURI().resolve("?i=" + i));
            ex.setMethod(HttpMethods.GET);
            connections[i].send(ex);
        }

        // try to get a connection, and only wait 500ms, as we have
        // already reserved the max, should return null
        Connection c = destination.reserveConnection(500);
        assertNull(c);

        // unreserve first connection
        destination.returnConnection(connections[0],false);

        // reserving one should now work
        c = destination.reserveConnection(500);
        assertNotNull(c);

        // release connections
        for (AbstractHttpConnection httpConnection : connections){
            destination.returnConnection(httpConnection,false);
        }
    }

    @Test
    public void testOptionsWithExchange() throws Exception
    {
        ContentExchange httpExchange = new ContentExchange(true);
        httpExchange.setURL(getBaseURI().toASCIIString());
        httpExchange.setRequestURI("*");
        httpExchange.setMethod(HttpMethods.OPTIONS);
    //    httpExchange.setRequestHeader("Connection","close");
        _httpClient.send(httpExchange);

        int state = httpExchange.waitForDone();
        assertEquals(HttpExchange.STATUS_COMPLETED, state);
        assertEquals(HttpStatus.OK_200,httpExchange.getResponseStatus());

        HttpFields headers = httpExchange.getResponseFields();
        HttpAsserts.assertContainsHeaderKey("Content-Length", headers);
        assertEquals("Content-Length header value", 0, headers.getLongField("Content-Length"));

        HttpAsserts.assertContainsHeaderKey("Allow",headers);
        String allow = headers.getStringField("Allow");
        String expectedMethods[] =
        { "GET", "HEAD", "POST", "PUT", "DELETE", "MOVE", "OPTIONS", "TRACE" };
        for (String expectedMethod : expectedMethods)
        {
            assertThat(allow,containsString(expectedMethod));
        }
    }

    /* ------------------------------------------------------------ */
    public static void copyStream(InputStream in, OutputStream out)
    {
        try
        {
            byte[] buffer = new byte[1024];
            int len;
            while ((len = in.read(buffer)) >= 0)
            {
                out.write(buffer,0,len);
            }
        }
        catch (EofException e)
        {
            System.err.println("HttpExchangeTest#copyStream: " + e);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
