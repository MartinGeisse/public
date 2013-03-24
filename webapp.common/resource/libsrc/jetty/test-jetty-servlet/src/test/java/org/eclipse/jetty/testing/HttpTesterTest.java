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

package org.eclipse.jetty.testing;

import junit.framework.TestCase;

public class HttpTesterTest extends TestCase
{
    
    public void testCharset() throws Exception
    {
        HttpTester tester = new HttpTester();
        tester.parse(
                "POST /uri\uA74A HTTP/1.1\r\n"+
                "Host: fakehost\r\n"+
                "Content-Length: 12\r\n" +
                "Content-Type: text/plain; charset=utf-8\r\n" +
                "\r\n" +
                "123456789\uA74A");
        assertEquals("POST",tester.getMethod());
        assertEquals("/uri\uA74A",tester.getURI());
        assertEquals("HTTP/1.1",tester.getVersion());
        assertEquals("fakehost",tester.getHeader("Host"));
        assertEquals("text/plain; charset=utf-8",tester.getContentType());
        assertEquals("utf-8",tester.getCharacterEncoding());
        assertEquals("123456789\uA74A",tester.getContent());
    }
    
    
    public void testHead() throws Exception
    {      
        String headResponse = "HTTP/1.1 200 OK\r\n"+
        "Content-Type: text/html\r\n"+
        "Content-Length: 22\r\n"+
        "\r\n";
        
        HttpTester tester = new HttpTester();
        tester.parse(headResponse, true);
        assertEquals(200, tester.getStatus());
        assertEquals("22", tester.getHeader("Content-Length"));
        assertEquals("text/html",tester.getContentType());
    }

    public void testSetCharset() throws Exception
    {      
        String content = "123456789\uA74A";
        HttpTester tester = new HttpTester();
        tester.setVersion("HTTP/1.0");
        tester.setMethod("POST");
        tester.setHeader("Content-type", "application/json; charset=iso-8859-1");
        tester.setURI("/1/batch");
        tester.setContent(content);
        assertEquals("123456789?",tester.getContent());

        tester.setHeader("Content-type", "application/json; charset=UTF-8");
        tester.setContent(content);
        assertEquals("123456789\uA74A",tester.getContent());
  
        String request=tester.generate();
        assertTrue(request.startsWith("POST "));
        assertTrue(request.trim().endsWith(content));
    }
}
