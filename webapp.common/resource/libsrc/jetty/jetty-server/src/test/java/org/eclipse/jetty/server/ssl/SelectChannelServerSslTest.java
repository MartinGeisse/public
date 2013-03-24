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

package org.eclipse.jetty.server.ssl;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.security.KeyStore;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.TrustManagerFactory;

import org.eclipse.jetty.io.AsyncEndPoint;
import org.eclipse.jetty.io.Connection;
import org.eclipse.jetty.io.nio.SslConnection;
import org.eclipse.jetty.server.HttpServerTestBase;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import static org.hamcrest.Matchers.lessThan;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * HttpServer Tester.
 */
public class SelectChannelServerSslTest extends HttpServerTestBase
{
    static SSLContext __sslContext;
    {
        _scheme="https";
    }
    
    @Override
    protected Socket newSocket(String host, int port) throws Exception
    {
        return __sslContext.getSocketFactory().createSocket(host,port);
    }
    
    private static final AtomicInteger _handlecount = new AtomicInteger();

    @BeforeClass
    public static void init() throws Exception
    {   
        SslSelectChannelConnector connector = new SslSelectChannelConnector()
        {
            @Override
            protected SslConnection newSslConnection(AsyncEndPoint endPoint, SSLEngine engine)
            {
                return new SslConnection(engine, endPoint)
                {
                    @Override
                    public Connection handle() throws IOException
                    {
                        _handlecount.incrementAndGet();
                        return super.handle();
                    }
                };
            }
        };
        
        String keystorePath = System.getProperty("basedir",".") + "/src/test/resources/keystore";
        SslContextFactory cf = connector.getSslContextFactory();
        cf.setKeyStorePath(keystorePath);
        cf.setKeyStorePassword("storepwd");
        cf.setKeyManagerPassword("keypwd");
        cf.setTrustStore(keystorePath);
        cf.setTrustStorePassword("storepwd");
        connector.setUseDirectBuffers(true);
        startServer(connector);
        

        KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
        keystore.load(new FileInputStream(connector.getKeystore()), "storepwd".toCharArray());
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(keystore);
        __sslContext = SSLContext.getInstance("TLS");
        __sslContext.init(null, trustManagerFactory.getTrustManagers(), null);
        
        try
        {
            HttpsURLConnection.setDefaultHostnameVerifier(__hostnameverifier);
            SSLContext sc = SSLContext.getInstance("TLS"); 
            sc.init(null, SslContextFactory.TRUST_ALL_CERTS, new java.security.SecureRandom()); 
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        }
        catch(Exception e)
        {
            e.printStackTrace();
            throw new RuntimeException(e);
        }   
    }   

    public void testRequest2Fragments() throws Exception
    {
        super.testRequest2Fragments();
    }

    @Test
    public void testRequest2FixedFragments() throws Exception
    {
        configureServer(new EchoHandler());

        byte[] bytes=REQUEST2.getBytes();
        int[] points=new int[]{74,325};

        // Sort the list
        Arrays.sort(points);

        Socket client=newSocket(HOST,_connector.getLocalPort());
        try
        {
            OutputStream os=client.getOutputStream();

            int last=0;

            // Write out the fragments
            for (int j=0; j<points.length; ++j)
            {
                int point=points[j];                
                os.write(bytes,last,point-last);
                last=point;
                os.flush();
                Thread.sleep(PAUSE);

            }

            // Write the last fragment
            os.write(bytes,last,bytes.length-last);
            os.flush();
            Thread.sleep(PAUSE);
            

            // Read the response
            String response=readResponse(client);

            // Check the response
            assertEquals(RESPONSE2,response);
        }
        finally
        {
            client.close();
        }
    }

    @Override
    @Test
    @Ignore("Override and ignore this test as SSLSocket.shutdownOutput() is not supported, " +
            "but shutdownOutput() is needed by the test.")
    public void testInterruptedRequest(){}

    @Override
    @Ignore
    public void testAvailable() throws Exception
    {
    }
    
    @Override
    public void testSuspendedPipeline() throws Exception
    {
        _handlecount.set(0);
        super.testSuspendedPipeline();
        assertThat(_handlecount.get(),lessThan(50));
    }
}
