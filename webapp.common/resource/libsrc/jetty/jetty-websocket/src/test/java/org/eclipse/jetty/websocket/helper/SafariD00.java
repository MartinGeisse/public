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

package org.eclipse.jetty.websocket.helper;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URI;

import org.eclipse.jetty.io.ByteArrayBuffer;
import org.eclipse.jetty.util.TypeUtil;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;
import org.eclipse.jetty.util.log.StdErrLog;
import org.junit.Assert;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.is;

public class SafariD00
{
    private static final Logger LOG = Log.getLogger(SafariD00.class);
    private URI uri;
    private SocketAddress endpoint;
    private Socket socket;
    private OutputStream out;
    private InputStream in;

    public SafariD00(URI uri)
    {
        if (LOG instanceof StdErrLog)
        {
            ((StdErrLog)LOG).setLevel(StdErrLog.LEVEL_DEBUG);
        }
        this.uri = uri;
        this.endpoint = new InetSocketAddress(uri.getHost(),uri.getPort());
    }

    /**
     * Open the Socket to the destination endpoint and
     *
     * @return the open java Socket.
     * @throws IOException
     */
    public Socket connect() throws IOException
    {
        LOG.info("Connecting to endpoint: " + endpoint);
        socket = new Socket();
        socket.setTcpNoDelay(true);
        socket.connect(endpoint,1000);

        out = socket.getOutputStream();
        in = socket.getInputStream();

        return socket;
    }

    /**
     * Issue an Http websocket (Draft-0) upgrade request (using an example request captured from OSX/Safari)
     *
     * @throws UnsupportedEncodingException
     */
    public void issueHandshake() throws IOException
    {
        LOG.debug("Issuing Handshake");
        StringBuilder req = new StringBuilder();
        req.append("GET ").append(uri.getPath()).append(" HTTP/1.1\r\n");
        req.append("Upgrade: WebSocket\r\n");
        req.append("Connection: Upgrade\r\n");
        req.append("Host: ").append(uri.getHost()).append(":").append(uri.getPort()).append("\r\n");
        req.append("Origin: http://www.google.com/\r\n");
        req.append("Sec-WebSocket-Key1: 15{ft  :6@87  0 M 5 c901\r\n");
        req.append("Sec-WebSocket-Key2: 3? C;7~0 8   \" 3 2105 6  `_ {\r\n");
        req.append("\r\n");

        LOG.debug("Request:" + req);

        byte reqBytes[] = req.toString().getBytes("UTF-8");
        byte hixieBytes[] = TypeUtil.fromHexString("e739617916c9daf3");
        byte buf[] = new byte[reqBytes.length + hixieBytes.length];
        System.arraycopy(reqBytes,0,buf,0,reqBytes.length);
        System.arraycopy(hixieBytes,0,buf,reqBytes.length,hixieBytes.length);

        // Send HTTP GET Request (with hixie bytes)
        out.write(buf,0,buf.length);
        out.flush();

        socket.setSoTimeout(10000);

        // Read HTTP 101 Upgrade / Handshake Response
        InputStreamReader reader = new InputStreamReader(in);
        StringBuilder respHeaders = new StringBuilder();

        LOG.debug("Reading http headers");
        int crlfs = 0;
        while (true)
        {
            int read = in.read();
            respHeaders.append((char)read);
            if (read == '\r' || read == '\n')
                ++crlfs;
            else
                crlfs = 0;
            if (crlfs == 4)
                break;
        }
        
        if(respHeaders.toString().startsWith("HTTP/1.1 101 ") == false) {
            String respLine = respHeaders.toString();
            int idx = respLine.indexOf('\r');
            if(idx > 0) {
                respLine = respLine.substring(0,idx);
            }
            LOG.debug("Response Headers: {}",respHeaders.toString());
            throw new IllegalStateException(respLine);
        }

        // Read expected handshake hixie bytes
        byte hixieHandshakeExpected[] = TypeUtil.fromHexString("c7438d956cf611a6af70603e6fa54809");
        byte hixieHandshake[] = new byte[hixieHandshakeExpected.length];
        Assert.assertThat("Hixie handshake buffer size",hixieHandshake.length,is(16));

        LOG.debug("Reading hixie handshake bytes");
        int bytesRead = 0;
        while (bytesRead < hixieHandshake.length)
        {
            int val = in.read();
            if (val >= 0)
            {
                hixieHandshake[bytesRead++] = (byte)val;
            }
        }
        Assert.assertThat("Read hixie handshake bytes",bytesRead,is(hixieHandshake.length));
    }

    public void sendMessage(String... msgs) throws IOException
    {
        int len = 0;
        for (String msg : msgs)
        {
            LOG.debug("sending message: " + msg);
            len += (msg.length() + 2);
        }

        ByteArrayBuffer buf = new ByteArrayBuffer(len);

        for (String msg : msgs)
        {
            buf.put((byte)0x00);
            buf.put(msg.getBytes("UTF-8"));
            buf.put((byte)0xFF);
        }

        out.write(buf.array());
        out.flush();
    }

    public void disconnect() throws IOException
    {
        LOG.debug("disconnect");
        socket.close();
    }
}
