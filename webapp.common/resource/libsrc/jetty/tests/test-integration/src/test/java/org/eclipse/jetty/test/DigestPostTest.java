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

package org.eclipse.jetty.test;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
import java.security.MessageDigest;
import java.util.Collections;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.client.ContentExchange;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.security.Realm;
import org.eclipse.jetty.client.security.SimpleRealmResolver;
import org.eclipse.jetty.http.HttpMethods;
import org.eclipse.jetty.io.ByteArrayBuffer;
import org.eclipse.jetty.security.ConstraintMapping;
import org.eclipse.jetty.security.ConstraintSecurityHandler;
import org.eclipse.jetty.security.HashLoginService;
import org.eclipse.jetty.security.authentication.DigestAuthenticator;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.util.IO;
import org.eclipse.jetty.util.StringUtil;
import org.eclipse.jetty.util.TypeUtil;
import org.eclipse.jetty.util.security.Constraint;
import org.eclipse.jetty.util.security.Password;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class DigestPostTest
{
    private static final String NC = "00000001";
    
    public final static String __message = 
        "0123456789 0123456789 0123456789 0123456789 0123456789 0123456789 0123456789 0123456789 \n"+
        "9876543210 9876543210 9876543210 9876543210 9876543210 9876543210 9876543210 9876543210 \n"+
        "1234567890 1234567890 1234567890 1234567890 1234567890 1234567890 1234567890 1234567890 \n"+
        "0987654321 0987654321 0987654321 0987654321 0987654321 0987654321 0987654321 0987654321 \n"+
        "abcdefghijklmnopqrstuvwxyz abcdefghijklmnopqrstuvwxyz abcdefghijklmnopqrstuvwxyz \n"+
        "ABCDEFGHIJKLMNOPQRSTUVWXYZ ABCDEFGHIJKLMNOPQRSTUVWXYZ ABCDEFGHIJKLMNOPQRSTUVWXYZ \n"+
        "Now is the time for all good men to come to the aid of the party.\n"+
        "How now brown cow.\n"+
        "The quick brown fox jumped over the lazy dog.\n";
    
    public volatile static String _received = null;
    private static Server _server;

    @BeforeClass
    public static void setUpServer()
    {
        try
        {
            _server = new Server();
            _server.setConnectors(new Connector[]
            { new SelectChannelConnector() });

            ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SECURITY);
            context.setContextPath("/test");
            context.addServlet(PostServlet.class,"/");

            HashLoginService realm = new HashLoginService("test");
            realm.putUser("testuser",new Password("password"),new String[]{"test"});
            _server.addBean(realm);
            
            ConstraintSecurityHandler security=(ConstraintSecurityHandler)context.getSecurityHandler();
            security.setAuthenticator(new DigestAuthenticator());
            security.setLoginService(realm);
           
            Constraint constraint = new Constraint("SecureTest","test");
            constraint.setAuthenticate(true);
            ConstraintMapping mapping = new ConstraintMapping();
            mapping.setConstraint(constraint);
            mapping.setPathSpec("/*");
            
            security.setConstraintMappings(Collections.singletonList(mapping));
            
            HandlerCollection handlers = new HandlerCollection();
            handlers.setHandlers(new Handler[]
            { context, new DefaultHandler() });
            _server.setHandler(handlers);
            
            _server.start();
        }
        catch (final Exception e)
        {
            e.printStackTrace();
        }
    }

    @AfterClass
    public static void tearDownServer() throws Exception
    {
        _server.stop();
    }

    @Test
    public void testServerDirectlyHTTP10() throws Exception
    {
        Socket socket = new Socket("127.0.0.1",_server.getConnectors()[0].getLocalPort());
        byte[] bytes = __message.getBytes("UTF-8");

        _received=null;
        socket.getOutputStream().write(
                ("POST /test/ HTTP/1.0\r\n"+
                "Host: 127.0.0.1:"+_server.getConnectors()[0].getLocalPort()+"\r\n"+
                "Content-Length: "+bytes.length+"\r\n"+
                "\r\n").getBytes("UTF-8"));
        socket.getOutputStream().write(bytes);
        socket.getOutputStream().flush();

        String result = IO.toString(socket.getInputStream());

        Assert.assertTrue(result.startsWith("HTTP/1.1 401 Unauthorized"));
        Assert.assertEquals(null,_received);
        
        int n=result.indexOf("nonce=");
        String nonce=result.substring(n+7,result.indexOf('"',n+7));
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] b= md.digest(String.valueOf(System.currentTimeMillis()).getBytes(org.eclipse.jetty.util.StringUtil.__ISO_8859_1));            
        String cnonce=encode(b);
        String digest="Digest username=\"testuser\" realm=\"test\" nonce=\""+nonce+"\" uri=\"/test/\" algorithm=MD5 response=\""+
        newResponse("POST","/test/",cnonce,"testuser","test","password",nonce,"auth")+
        "\" qop=auth nc="+NC+" cnonce=\""+cnonce+"\"";
              
        
        socket = new Socket("127.0.0.1",_server.getConnectors()[0].getLocalPort());

        _received=null;
        socket.getOutputStream().write(
                ("POST /test/ HTTP/1.0\r\n"+
                "Host: 127.0.0.1:"+_server.getConnectors()[0].getLocalPort()+"\r\n"+
                "Content-Length: "+bytes.length+"\r\n"+
                "Authorization: "+digest+"\r\n"+
                "\r\n").getBytes("UTF-8"));
        socket.getOutputStream().write(bytes);
        socket.getOutputStream().flush();

        result = IO.toString(socket.getInputStream());

        Assert.assertTrue(result.startsWith("HTTP/1.1 200 OK"));
        Assert.assertEquals(__message,_received);
    }

    @Test
    public void testServerDirectlyHTTP11() throws Exception
    {
        Socket socket = new Socket("127.0.0.1",_server.getConnectors()[0].getLocalPort());
        byte[] bytes = __message.getBytes("UTF-8");

        _received=null;
        socket.getOutputStream().write(
                ("POST /test/ HTTP/1.1\r\n"+
                "Host: 127.0.0.1:"+_server.getConnectors()[0].getLocalPort()+"\r\n"+
                "Content-Length: "+bytes.length+"\r\n"+
                "\r\n").getBytes("UTF-8"));
        socket.getOutputStream().write(bytes);
        socket.getOutputStream().flush();

        Thread.sleep(100);
        
        byte[] buf=new byte[4096];
        int len=socket.getInputStream().read(buf);
        String result=new String(buf,0,len,"UTF-8");

        Assert.assertTrue(result.startsWith("HTTP/1.1 401 Unauthorized"));
        Assert.assertEquals(null,_received);
        
        int n=result.indexOf("nonce=");
        String nonce=result.substring(n+7,result.indexOf('"',n+7));
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] b= md.digest(String.valueOf(System.currentTimeMillis()).getBytes(StringUtil.__ISO_8859_1));            
        String cnonce=encode(b);
        String digest="Digest username=\"testuser\" realm=\"test\" nonce=\""+nonce+"\" uri=\"/test/\" algorithm=MD5 response=\""+
        newResponse("POST","/test/",cnonce,"testuser","test","password",nonce,"auth")+
        "\" qop=auth nc="+NC+" cnonce=\""+cnonce+"\"";

        _received=null;
        socket.getOutputStream().write(
                ("POST /test/ HTTP/1.0\r\n"+
                "Host: 127.0.0.1:"+_server.getConnectors()[0].getLocalPort()+"\r\n"+
                "Content-Length: "+bytes.length+"\r\n"+
                "Authorization: "+digest+"\r\n"+
                "\r\n").getBytes("UTF-8"));
        socket.getOutputStream().write(bytes);
        socket.getOutputStream().flush();

        result = IO.toString(socket.getInputStream());

        Assert.assertTrue(result.startsWith("HTTP/1.1 200 OK"));
        Assert.assertEquals(__message,_received);
    }

    @Test
    public void testServerWithHttpClientStringContent() throws Exception
    {
        HttpClient client = new HttpClient();
        client.setConnectorType(HttpClient.CONNECTOR_SELECT_CHANNEL);
        client.setRealmResolver(new SimpleRealmResolver(new TestRealm()));
        client.start();

        String srvUrl = "http://127.0.0.1:" + _server.getConnectors()[0].getLocalPort() + "/test/";

        ContentExchange ex = new ContentExchange();
        ex.setMethod(HttpMethods.POST);
        ex.setURL(srvUrl);
        ex.setRequestContent(new ByteArrayBuffer(__message,"UTF-8"));

        _received=null;
        client.send(ex);
        ex.waitForDone();

        Assert.assertEquals(__message,_received);
        Assert.assertEquals(200,ex.getResponseStatus());
    }
    
    @Test
    public void testServerWithHttpClientStreamContent() throws Exception
    {
        HttpClient client = new HttpClient();
        client.setConnectorType(HttpClient.CONNECTOR_SELECT_CHANNEL);
        client.setRealmResolver(new SimpleRealmResolver(new TestRealm()));
        client.start();

        String srvUrl = "http://127.0.0.1:" + _server.getConnectors()[0].getLocalPort() + "/test/";

        ContentExchange ex = new ContentExchange();
        ex.setMethod(HttpMethods.POST);
        ex.setURL(srvUrl);
        ex.setRequestContentSource(new BufferedInputStream(new FileInputStream("src/test/resources/message.txt")));

        _received=null;
        client.send(ex);
        ex.waitForDone();

        String sent = IO.toString(new FileInputStream("src/test/resources/message.txt"));
        Assert.assertEquals(sent,_received);
        Assert.assertEquals(200,ex.getResponseStatus());
    }

    public static class TestRealm implements Realm
    {
        public String getPrincipal()
        {
            return "testuser";
        }

        public String getId()
        {
            return "test";
        }

        public String getCredentials()
        {
            return "password";
        }
    }

    public static class PostServlet extends HttpServlet
    {
        private static final long serialVersionUID = 1L;

        public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException
        {
            String received = IO.toString(request.getInputStream());
            _received = received;

            response.setStatus(200);
            response.getWriter().println("Received "+received.length()+" bytes");
        }

    }

    protected String newResponse(String method, String uri, String cnonce, String principal, String realm, String credentials, String nonce, String qop)
        throws Exception
    {       
        MessageDigest md = MessageDigest.getInstance("MD5");

        // calc A1 digest
        md.update(principal.getBytes(StringUtil.__ISO_8859_1));
        md.update((byte)':');
        md.update(realm.getBytes(StringUtil.__ISO_8859_1));
        md.update((byte)':');
        md.update(credentials.getBytes(StringUtil.__ISO_8859_1));
        byte[] ha1 = md.digest();
        // calc A2 digest
        md.reset();
        md.update(method.getBytes(StringUtil.__ISO_8859_1));
        md.update((byte)':');
        md.update(uri.getBytes(StringUtil.__ISO_8859_1));
        byte[] ha2=md.digest();

        md.update(TypeUtil.toString(ha1,16).getBytes(StringUtil.__ISO_8859_1));
        md.update((byte)':');
        md.update(nonce.getBytes(StringUtil.__ISO_8859_1));
        md.update((byte)':');
        md.update(NC.getBytes(StringUtil.__ISO_8859_1));
        md.update((byte)':');
        md.update(cnonce.getBytes(StringUtil.__ISO_8859_1));
        md.update((byte)':');
        md.update(qop.getBytes(StringUtil.__ISO_8859_1));
        md.update((byte)':');
        md.update(TypeUtil.toString(ha2,16).getBytes(StringUtil.__ISO_8859_1));
        byte[] digest=md.digest();

        // check digest
        return encode(digest);
    }
    
    private static String encode(byte[] data)
    {
        StringBuffer buffer = new StringBuffer();
        for (int i=0; i<data.length; i++) 
        {
            buffer.append(Integer.toHexString((data[i] & 0xf0) >>> 4));
            buffer.append(Integer.toHexString(data[i] & 0x0f));
        }
        return buffer.toString();
    }
}
