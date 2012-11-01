/*
 * ====================================================================
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

package org.apache.http.impl.nio.reactor;

import java.io.IOException;
import java.net.BindException;
import java.net.InetSocketAddress;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpResponseInterceptor;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.nio.DefaultHttpServerIODispatch;
import org.apache.http.nio.protocol.HttpAsyncRequestHandlerRegistry;
import org.apache.http.nio.protocol.HttpAsyncService;
import org.apache.http.nio.reactor.IOEventDispatch;
import org.apache.http.nio.reactor.IOReactorExceptionHandler;
import org.apache.http.nio.reactor.IOReactorStatus;
import org.apache.http.nio.reactor.ListenerEndpoint;
import org.apache.http.nio.reactor.ListeningIOReactor;
import org.apache.http.params.HttpParams;
import org.apache.http.params.SyncBasicHttpParams;
import org.apache.http.protocol.HttpProcessor;
import org.apache.http.protocol.ImmutableHttpProcessor;
import org.apache.http.protocol.ResponseConnControl;
import org.apache.http.protocol.ResponseContent;
import org.apache.http.protocol.ResponseDate;
import org.apache.http.protocol.ResponseServer;
import org.junit.Assert;
import org.junit.Test;

/**
 * Basic tests for {@link DefaultListeningIOReactor}.
 */
public class TestDefaultListeningIOReactor {

    private static IOEventDispatch createIOEventDispatch() {
        HttpParams params = new SyncBasicHttpParams();
        HttpProcessor httpproc = new ImmutableHttpProcessor(new HttpResponseInterceptor[] {
                new ResponseDate(),
                new ResponseServer(),
                new ResponseContent(),
                new ResponseConnControl()
        });
        HttpAsyncService serviceHandler = new HttpAsyncService(
                httpproc,
                new DefaultConnectionReuseStrategy(),
                new HttpAsyncRequestHandlerRegistry(),
                params);
        return new DefaultHttpServerIODispatch(serviceHandler, params);
    }

    @Test
    public void testEndpointUpAndDown() throws Exception {
        final IOEventDispatch eventDispatch = createIOEventDispatch();
        IOReactorConfig config = new IOReactorConfig();
        config.setIoThreadCount(1);
        final ListeningIOReactor ioreactor = new DefaultListeningIOReactor(config);

        Thread t = new Thread(new Runnable() {

            public void run() {
                try {
                    ioreactor.execute(eventDispatch);
                } catch (IOException ex) {
                }
            }

        });

        t.start();

        Set<ListenerEndpoint> endpoints = ioreactor.getEndpoints();
        Assert.assertNotNull(endpoints);
        Assert.assertEquals(0, endpoints.size());

        ListenerEndpoint endpoint1 = ioreactor.listen(new InetSocketAddress(0));
        endpoint1.waitFor();

        ListenerEndpoint endpoint2 = ioreactor.listen(new InetSocketAddress(0));
        endpoint2.waitFor();
        int port = ((InetSocketAddress) endpoint2.getAddress()).getPort();

        endpoints = ioreactor.getEndpoints();
        Assert.assertNotNull(endpoints);
        Assert.assertEquals(2, endpoints.size());

        endpoint1.close();

        endpoints = ioreactor.getEndpoints();
        Assert.assertNotNull(endpoints);
        Assert.assertEquals(1, endpoints.size());

        ListenerEndpoint endpoint = endpoints.iterator().next();

        Assert.assertEquals(port, ((InetSocketAddress) endpoint.getAddress()).getPort());

        ioreactor.shutdown(1000);
        t.join(1000);

        Assert.assertEquals(IOReactorStatus.SHUT_DOWN, ioreactor.getStatus());
    }

    @Test
    public void testEndpointAlreadyBoundFatal() throws Exception {
        final IOEventDispatch eventDispatch = createIOEventDispatch();
        IOReactorConfig config = new IOReactorConfig();
        config.setIoThreadCount(1);
        final ListeningIOReactor ioreactor = new DefaultListeningIOReactor(config);

        final CountDownLatch latch = new CountDownLatch(1);

        Thread t = new Thread(new Runnable() {

            public void run() {
                try {
                    ioreactor.execute(eventDispatch);
                    Assert.fail("IOException should have been thrown");
                } catch (IOException ex) {
                    latch.countDown();
                }
            }

        });

        t.start();

        ListenerEndpoint endpoint1 = ioreactor.listen(new InetSocketAddress(0));
        endpoint1.waitFor();
        int port = ((InetSocketAddress) endpoint1.getAddress()).getPort();

        ListenerEndpoint endpoint2 = ioreactor.listen(new InetSocketAddress(port));
        endpoint2.waitFor();
        Assert.assertNotNull(endpoint2.getException());

        // I/O reactor is now expected to be shutting down
        latch.await(2000, TimeUnit.MILLISECONDS);
        Assert.assertTrue(ioreactor.getStatus().compareTo(IOReactorStatus.SHUTTING_DOWN) >= 0);

        Set<ListenerEndpoint> endpoints = ioreactor.getEndpoints();
        Assert.assertNotNull(endpoints);
        Assert.assertEquals(0, endpoints.size());

        ioreactor.shutdown(1000);
        t.join(1000);

        Assert.assertEquals(IOReactorStatus.SHUT_DOWN, ioreactor.getStatus());
    }

    @Test
    public void testEndpointAlreadyBoundNonFatal() throws Exception {
        final IOEventDispatch eventDispatch = createIOEventDispatch();
        IOReactorConfig config = new IOReactorConfig();
        config.setIoThreadCount(1);
        final DefaultListeningIOReactor ioreactor = new DefaultListeningIOReactor(config);

        ioreactor.setExceptionHandler(new IOReactorExceptionHandler() {

            public boolean handle(final IOException ex) {
                return (ex instanceof BindException);
            }

            public boolean handle(final RuntimeException ex) {
                return false;
            }

        });

        Thread t = new Thread(new Runnable() {

            public void run() {
                try {
                    ioreactor.execute(eventDispatch);
                } catch (IOException ex) {
                }
            }

        });

        t.start();

        ListenerEndpoint endpoint1 = ioreactor.listen(new InetSocketAddress(9999));
        endpoint1.waitFor();

        ListenerEndpoint endpoint2 = ioreactor.listen(new InetSocketAddress(9999));
        endpoint2.waitFor();
        Assert.assertNotNull(endpoint2.getException());

        // Sleep a little to make sure the I/O reactor is not shutting down
        Thread.sleep(500);

        Assert.assertEquals(IOReactorStatus.ACTIVE, ioreactor.getStatus());

        ioreactor.shutdown(1000);
        t.join(1000);

        Assert.assertEquals(IOReactorStatus.SHUT_DOWN, ioreactor.getStatus());
    }

}
