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

package org.eclipse.jetty.servlets;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.util.EnumSet;
import java.util.Enumeration;
import java.util.Map;

import javax.servlet.DispatcherType;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.FilterMapping;
import org.eclipse.jetty.testing.HttpTester;
import org.eclipse.jetty.testing.ServletTester;
import org.eclipse.jetty.util.IO;
import org.eclipse.jetty.util.QuotedStringTokenizer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class MultipartFilterTest
{
    private File _dir;
    private ServletTester tester;

    
    public static class BoundaryServlet extends TestServlet
    {
        @Override
        protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
        {
            assertNotNull(req.getParameter("fileName"));
            assertEquals(getServletContext().getAttribute("fileName"), req.getParameter("fileName"));
            assertNotNull(req.getParameter("desc"));
            assertEquals(getServletContext().getAttribute("desc"), req.getParameter("desc"));
            assertNotNull(req.getParameter("title"));
            assertEquals(getServletContext().getAttribute("title"), req.getParameter("title"));
            super.doPost(req, resp);
        }
    }
    
    public static class TestServlet extends DumpServlet
    {

        @Override
        protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
        {
            assertNotNull(req.getParameter("fileup"));
            assertNotNull(req.getParameter("fileup"+MultiPartFilter.CONTENT_TYPE_SUFFIX));
            assertEquals(req.getParameter("fileup"+MultiPartFilter.CONTENT_TYPE_SUFFIX), "application/octet-stream");
            super.doPost(req, resp);
        }
        
    }
    

    
    @Before
    public void setUp() throws Exception
    {
        _dir = File.createTempFile("testmultupart",null);
        assertTrue(_dir.delete());
        assertTrue(_dir.mkdir());
        _dir.deleteOnExit();
        assertTrue(_dir.isDirectory());

        tester=new ServletTester();
        tester.setContextPath("/context");
        tester.setResourceBase(_dir.getCanonicalPath());
        tester.addServlet(TestServlet.class, "/");
        tester.setAttribute("javax.servlet.context.tempdir", _dir);
        FilterHolder multipartFilter = tester.addFilter(MultiPartFilter.class,"/*", EnumSet.of(DispatcherType.REQUEST));
        multipartFilter.setInitParameter("deleteFiles", "true");
        tester.start();
    }

    @After
    public void tearDown() throws Exception
    {
        tester.stop();
    }

    @Test
    public void testBadPost() throws Exception
    {
        // generated and parsed test
        HttpTester request = new HttpTester();
        HttpTester response = new HttpTester();

        // test GET
        request.setMethod("POST");
        request.setVersion("HTTP/1.0");
        request.setHeader("Host","tester");
        request.setURI("/context/dump");
        
        String boundary="XyXyXy";
        request.setHeader("Content-Type","multipart/form-data; boundary="+boundary);
        
        
        String content = "--" + boundary + "\r\n"+
        "Content-Disposition: form-data; name=\"fileup\"; filename=\"test.upload\"\r\n"+
        "Content-Type: application/octet-stream\r\n\r\n"+
        "How now brown cow."+
        "\r\n--" + boundary + "-\r\n\r\n";
        
        request.setContent(content);
        
        
        response.parse(tester.getResponses(request.generate()));
        assertTrue(response.getMethod()==null);
        assertEquals(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,response.getStatus());
    }
    

    @Test
    public void testPost() throws Exception
    {
        // generated and parsed test
        HttpTester request = new HttpTester();
        HttpTester response = new HttpTester();

        // test GET
        request.setMethod("POST");
        request.setVersion("HTTP/1.0");
        request.setHeader("Host","tester");
        request.setURI("/context/dump");
        
        String boundary="XyXyXy";
        request.setHeader("Content-Type","multipart/form-data; boundary=\""+boundary+"\"");
        
        
        String content = "--" + boundary + "\r\n"+
        "Content-Disposition: form-data; name=\"fileup\"; filename=\"test.upload\"\r\n"+
        "Content-Type: application/octet-stream\r\n\r\n"+
        "How now brown cow."+
        "\r\n--" + boundary + "--\r\n\r\n";
        
        request.setContent(content);
        
        response.parse(tester.getResponses(request.generate()));
        assertTrue(response.getMethod()==null);
        assertEquals(HttpServletResponse.SC_OK,response.getStatus());
        assertTrue(response.getContent().indexOf("brown cow")>=0);
    }
  

    @Test
    public void testEncodedPost() throws Exception
    {
        // generated and parsed test
        HttpTester request = new HttpTester();
        HttpTester response = new HttpTester();

        // test GET
        request.setMethod("POST");
        request.setVersion("HTTP/1.0");
        request.setHeader("Host","tester");
        request.setURI("/context/dump");
        
        String boundary="XyXyXy";
        request.setHeader("Content-Type","multipart/form-data; boundary="+boundary);
        
        
        String content = "--" + boundary + "\r\n"+
        "Content-Disposition: form-data; name=\"fileup\"; filename=\"Diplomsko Delo Lektorirano KON&#268;NA.doc\"\r\n"+
        "Content-Type: application/octet-stream\r\n\r\n"+
        "How now brown cow."+
        "\r\n--" + boundary + "--\r\n\r\n";
        
        request.setContent(content);
        
        response.parse(tester.getResponses(request.generate()));
        assertTrue(response.getMethod()==null);
        assertEquals(HttpServletResponse.SC_OK,response.getStatus());
        assertTrue(response.getContent().indexOf("brown cow")>=0);
    }
    
    @Test
    public void testBadlyEncodedFilename() throws Exception
    {
        // generated and parsed test
        HttpTester request = new HttpTester();
        HttpTester response = new HttpTester();

        // test GET
        request.setMethod("POST");
        request.setVersion("HTTP/1.0");
        request.setHeader("Host","tester");
        request.setURI("/context/dump");
        
        String boundary="XyXyXy";
        request.setHeader("Content-Type","multipart/form-data; boundary="+boundary);
        
        
        String content = "--" + boundary + "\r\n"+
        "Content-Disposition: form-data; name=\"fileup\"; filename=\"Taken on Aug 22 \\ 2012.jpg\"\r\n"+
        "Content-Type: application/octet-stream\r\n\r\n"+
        "How now brown cow."+
        "\r\n--" + boundary + "--\r\n\r\n";
        
        request.setContent(content);
        
        response.parse(tester.getResponses(request.generate()));
        
        //System.out.printf("Content: [%s]%n", response.getContent());

        assertThat(response.getMethod(), nullValue());
        assertThat(response.getStatus(), is(HttpServletResponse.SC_OK));
        
        assertThat(response.getContent(), containsString("Filename [Taken on Aug 22 \\ 2012.jpg]"));
        assertThat(response.getContent(), containsString("How now brown cow."));
    }
    
    @Test
    public void testBadlyEncodedMSFilename() throws Exception
    {
        // generated and parsed test
        HttpTester request = new HttpTester();
        HttpTester response = new HttpTester();

        // test GET
        request.setMethod("POST");
        request.setVersion("HTTP/1.0");
        request.setHeader("Host","tester");
        request.setURI("/context/dump");
        
        String boundary="XyXyXy";
        request.setHeader("Content-Type","multipart/form-data; boundary="+boundary);
        
        
        String content = "--" + boundary + "\r\n"+
        "Content-Disposition: form-data; name=\"fileup\"; filename=\"c:\\this\\really\\is\\some\\path\\to\\a\\file.txt\"\r\n"+
        "Content-Type: application/octet-stream\r\n\r\n"+
        "How now brown cow."+
        "\r\n--" + boundary + "--\r\n\r\n";
        
        request.setContent(content);
        
        response.parse(tester.getResponses(request.generate()));
        
        //System.out.printf("Content: [%s]%n", response.getContent());

        assertThat(response.getMethod(), nullValue());
        assertThat(response.getStatus(), is(HttpServletResponse.SC_OK));
        
        assertThat(response.getContent(), containsString("Filename [c:\\this\\really\\is\\some\\path\\to\\a\\file.txt]"));
        assertThat(response.getContent(), containsString("How now brown cow.")); 
    }

    @Test
    public void testCorrectlyEncodedMSFilename() throws Exception
    {
        // generated and parsed test
        HttpTester request = new HttpTester();
        HttpTester response = new HttpTester();

        // test GET
        request.setMethod("POST");
        request.setVersion("HTTP/1.0");
        request.setHeader("Host","tester");
        request.setURI("/context/dump");
        
        String boundary="XyXyXy";
        request.setHeader("Content-Type","multipart/form-data; boundary="+boundary);
        
        
        String content = "--" + boundary + "\r\n"+
        "Content-Disposition: form-data; name=\"fileup\"; filename=\"c:\\\\this\\\\really\\\\is\\\\some\\\\path\\\\to\\\\a\\\\file.txt\"\r\n"+
        "Content-Type: application/octet-stream\r\n\r\n"+
        "How now brown cow."+
        "\r\n--" + boundary + "--\r\n\r\n";
        
        request.setContent(content);
        
        response.parse(tester.getResponses(request.generate()));
        
        //System.out.printf("Content: [%s]%n", response.getContent());

        assertThat(response.getMethod(), nullValue());
        assertThat(response.getStatus(), is(HttpServletResponse.SC_OK));
        
        assertThat(response.getContent(), containsString("Filename [c:\\this\\really\\is\\some\\path\\to\\a\\file.txt]"));
        assertThat(response.getContent(), containsString("How now brown cow.")); 
    }

    
    /*
     * Test multipart with parts encoded in base64 (RFC1521 section 5)
     */
    @Test
    public void testPostWithContentTransferEncodingBase64() throws Exception {
        // generated and parsed test
        HttpTester request = new HttpTester();
        HttpTester response = new HttpTester();

        // test GET
        request.setMethod("POST");
        request.setVersion("HTTP/1.0");
        request.setHeader("Host","tester");
        request.setURI("/context/dump");
        
        String boundary="XyXyXy";
        request.setHeader("Content-Type","multipart/form-data; boundary="+boundary);
        
        // part content is "How now brown cow." run through a base64 encoder
        String content = "--" + boundary + "\r\n"+
        "Content-Disposition: form-data; name=\"fileup\"; filename=\"test.upload\"\r\n"+
        "Content-Transfer-Encoding: base64\r\n"+
        "Content-Type: application/octet-stream\r\n\r\n"+
        "SG93IG5vdyBicm93biBjb3cuCg=="+
        "\r\n--" + boundary + "--\r\n\r\n";
        
        request.setContent(content);
        
        response.parse(tester.getResponses(request.generate()));
        assertTrue(response.getMethod()==null);
        assertEquals(HttpServletResponse.SC_OK,response.getStatus());
        assertTrue(response.getContent().indexOf("brown cow")>=0);
    }

    /*
     * Test multipart with parts encoded in quoted-printable (RFC1521 section 5)
     */
    @Test
    public void testPostWithContentTransferEncodingQuotedPrintable() throws Exception {
        // generated and parsed test
        HttpTester request = new HttpTester();
        HttpTester response = new HttpTester();

        // test GET
        request.setMethod("POST");
        request.setVersion("HTTP/1.0");
        request.setHeader("Host","tester");
        request.setURI("/context/dump");
        
        String boundary="XyXyXy";
        request.setHeader("Content-Type","multipart/form-data; boundary="+boundary);
        
        /*
         * Part content is "How now brown cow." run through Apache Commons Codec
         * quoted printable encoding.
         */
        String content = "--" + boundary + "\r\n"+
        "Content-Disposition: form-data; name=\"fileup\"; filename=\"test.upload\"\r\n"+
        "Content-Transfer-Encoding: quoted-printable\r\n"+
        "Content-Type: application/octet-stream\r\n\r\n"+
        "=48=6F=77=20=6E=6F=77=20=62=72=6F=77=6E=20=63=6F=77=2E"+
        "\r\n--" + boundary + "--\r\n\r\n";
        
        request.setContent(content);
        
        response.parse(tester.getResponses(request.generate()));
        assertTrue(response.getMethod()==null);
        assertEquals(HttpServletResponse.SC_OK,response.getStatus());
        assertTrue(response.getContent().indexOf("brown cow")>=0);
    }
    
    
    @Test
    public void testNoBoundary() throws Exception
    {
        // generated and parsed test
        HttpTester request = new HttpTester();
        HttpTester response = new HttpTester();
        tester.addServlet(BoundaryServlet.class,"/testb");
        tester.setAttribute("fileName", "abc");
        tester.setAttribute("desc", "123");
        tester.setAttribute("title", "ttt");
        request.setMethod("POST");
        request.setVersion("HTTP/1.0");
        request.setHeader("Host","tester");
        request.setURI("/context/testb");

        request.setHeader("Content-Type","multipart/form-data");

        String content = "--\r\n"+
        "Content-Disposition: form-data; name=\"fileName\"\r\n"+
        "Content-Type: text/plain; charset=US-ASCII\r\n"+
        "Content-Transfer-Encoding: 8bit\r\n"+
        "\r\n"+
        "abc\r\n"+
        "--\r\n"+
        "Content-Disposition: form-data; name=\"desc\"\r\n"+ 
        "Content-Type: text/plain; charset=US-ASCII\r\n"+ 
        "Content-Transfer-Encoding: 8bit\r\n"+
        "\r\n"+
        "123\r\n"+ 
        "--\r\n"+ 
        "Content-Disposition: form-data; name=\"title\"\r\n"+
        "Content-Type: text/plain; charset=US-ASCII\r\n"+
        "Content-Transfer-Encoding: 8bit\r\n"+ 
        "\r\n"+
        "ttt\r\n"+ 
        "--\r\n"+
        "Content-Disposition: form-data; name=\"fileup\"; filename=\"test.upload\"\r\n"+
        "Content-Type: application/octet-stream\r\n"+
        "Content-Transfer-Encoding: binary\r\n"+ 
        "\r\n"+
        "000\r\n"+ 
        "----\r\n";
        request.setContent(content);

        response.parse(tester.getResponses(request.generate()));
        assertTrue(response.getMethod()==null);
        assertEquals(HttpServletResponse.SC_OK,response.getStatus());
    }
    
    
    @Test
    public void testLFOnlyRequest() throws Exception
    { 
        String boundary="XyXyXy";
        // generated and parsed test
        HttpTester request = new HttpTester();
        HttpTester response = new HttpTester();
        tester.addServlet(BoundaryServlet.class,"/testb");
        tester.setAttribute("fileName", "abc");
        tester.setAttribute("desc", "123");
        tester.setAttribute("title", "ttt");
        request.setMethod("POST");
        request.setVersion("HTTP/1.0");
        request.setHeader("Host","tester");
        request.setURI("/context/testb");
        request.setHeader("Content-Type","multipart/form-data; boundary="+boundary);

        String content = "--XyXyXy\n"+
        "Content-Disposition: form-data; name=\"fileName\"\n"+
        "Content-Type: text/plain; charset=US-ASCII\n"+
        "Content-Transfer-Encoding: 8bit\n"+
        "\n"+
        "abc\n"+
        "--XyXyXy\n"+
        "Content-Disposition: form-data; name=\"desc\"\n"+ 
        "Content-Type: text/plain; charset=US-ASCII\n"+ 
        "Content-Transfer-Encoding: 8bit\n"+
        "\n"+
        "123\n"+ 
        "--XyXyXy\n"+ 
        "Content-Disposition: form-data; name=\"title\"\n"+
        "Content-Type: text/plain; charset=US-ASCII\n"+
        "Content-Transfer-Encoding: 8bit\n"+ 
        "\n"+
        "ttt\n"+ 
        "--XyXyXy\n"+
        "Content-Disposition: form-data; name=\"fileup\"; filename=\"test.upload\"\n"+
        "Content-Type: application/octet-stream\n"+
        "Content-Transfer-Encoding: binary\n"+ 
        "\n"+
        "000\n"+ 
        "--XyXyXy--\n";
        request.setContent(content);

        response.parse(tester.getResponses(request.generate()));
        assertTrue(response.getMethod()==null);
        assertEquals(HttpServletResponse.SC_OK,response.getStatus()); 
    }
    
    
    @Test
    public void testCROnlyRequest() throws Exception
    { 
        String boundary="XyXyXy";
        // generated and parsed test
        HttpTester request = new HttpTester();
        HttpTester response = new HttpTester();
        tester.addServlet(BoundaryServlet.class,"/testb");
        tester.setAttribute("fileName", "abc");
        tester.setAttribute("desc", "123");
        tester.setAttribute("title", "ttt");
        request.setMethod("POST");
        request.setVersion("HTTP/1.0");
        request.setHeader("Host","tester");
        request.setURI("/context/testb");
        request.setHeader("Content-Type","multipart/form-data; boundary="+boundary);

        String content = "--XyXyXy\r"+
        "Content-Disposition: form-data; name=\"fileName\"\r"+
        "Content-Type: text/plain; charset=US-ASCII\r"+
        "Content-Transfer-Encoding: 8bit\r"+
        "\r"+
        "abc\r"+
        "--XyXyXy\r"+
        "Content-Disposition: form-data; name=\"desc\"\r"+ 
        "Content-Type: text/plain; charset=US-ASCII\r"+ 
        "Content-Transfer-Encoding: 8bit\r"+
        "\r"+
        "123\r"+ 
        "--XyXyXy\r"+ 
        "Content-Disposition: form-data; name=\"title\"\r"+
        "Content-Type: text/plain; charset=US-ASCII\r"+
        "Content-Transfer-Encoding: 8bit\r"+ 
        "\r"+
        "ttt\r"+ 
        "--XyXyXy\r"+
        "Content-Disposition: form-data; name=\"fileup\"; filename=\"test.upload\"\r"+
        "Content-Type: application/octet-stream\r"+
        "Content-Transfer-Encoding: binary\r"+ 
        "\r"+
        "000\r"+ 
        "--XyXyXy--\r";
        request.setContent(content);

        response.parse(tester.getResponses(request.generate()));
        assertTrue(response.getMethod()==null);
        assertEquals(HttpServletResponse.SC_OK,response.getStatus()); 
    }
    
    
    @Test
    public void testCROnlyWithEmbeddedLFRequest() throws Exception
    { 
        String boundary="XyXyXy";
        // generated and parsed test
        HttpTester request = new HttpTester();
        HttpTester response = new HttpTester();
        tester.addServlet(BoundaryServlet.class,"/testb");
        tester.setAttribute("fileName", "\nabc\n");
        tester.setAttribute("desc", "\n123\n");
        tester.setAttribute("title", "\nttt\n");
        request.setMethod("POST");
        request.setVersion("HTTP/1.0");
        request.setHeader("Host","tester");
        request.setURI("/context/testb");
        request.setHeader("Content-Type","multipart/form-data; boundary="+boundary);

        String content = "--XyXyXy\r"+
        "Content-Disposition: form-data; name=\"fileName\"\r"+
        "Content-Type: text/plain; charset=US-ASCII\r"+
        "Content-Transfer-Encoding: 8bit\r"+
        "\r"+
        "\nabc\n"+
        "\r"+
        "--XyXyXy\r"+
        "Content-Disposition: form-data; name=\"desc\"\r"+ 
        "Content-Type: text/plain; charset=US-ASCII\r"+ 
        "Content-Transfer-Encoding: 8bit\r"+
        "\r"+
        "\n123\n"+ 
        "\r"+
        "--XyXyXy\r"+ 
        "Content-Disposition: form-data; name=\"title\"\r"+
        "Content-Type: text/plain; charset=US-ASCII\r"+
        "Content-Transfer-Encoding: 8bit\r"+ 
        "\r"+
        "\nttt\n"+ 
        "\r"+
        "--XyXyXy\r"+
        "Content-Disposition: form-data; name=\"fileup\"; filename=\"test.upload\"\r"+
        "Content-Type: application/octet-stream\r"+
        "Content-Transfer-Encoding: binary\r"+ 
        "\r"+
        "000\r"+ 
        "--XyXyXy--\r";
        request.setContent(content);

        response.parse(tester.getResponses(request.generate()));
        assertTrue(response.getMethod()==null);
        assertEquals(HttpServletResponse.SC_OK,response.getStatus()); 
    }
    
    
    @Test
    public void testNoBody()
    throws Exception
    {
        String boundary="XyXyXy";
        // generated and parsed test
        HttpTester request = new HttpTester();
        HttpTester response = new HttpTester();
        request.setMethod("POST");
        request.setVersion("HTTP/1.0");
        request.setHeader("Host","tester");
        request.setURI("/context/dump");
        request.setHeader("Content-Type","multipart/form-data; boundary="+boundary);
        
        response.parse(tester.getResponses(request.generate()));
        assertTrue(response.getMethod()==null);
        assertEquals(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, response.getStatus());
        assertTrue(response.getContent().indexOf("Missing content")>=0);
    }

    @Test
    public void testWhitespaceBodyWithCRLF()
    throws Exception
    {
        String whitespace = "              \n\n\n\r\n\r\n\r\n\r\n";

        String boundary="XyXyXy";
        // generated and parsed test
        HttpTester request = new HttpTester();
        HttpTester response = new HttpTester();
        request.setMethod("POST");
        request.setVersion("HTTP/1.0");
        request.setHeader("Host","tester");
        request.setURI("/context/dump");
        request.setHeader("Content-Type","multipart/form-data; boundary="+boundary);
        request.setContent(whitespace);
        
        response.parse(tester.getResponses(request.generate()));
        assertTrue(response.getMethod()==null);
        assertEquals(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, response.getStatus());
        assertTrue(response.getContent().indexOf("Missing initial")>=0);
    }
    
  
    @Test
    public void testWhitespaceBody()
    throws Exception
    {
        String whitespace = " ";

        String boundary="XyXyXy";
        // generated and parsed test
        HttpTester request = new HttpTester();
        HttpTester response = new HttpTester();
        request.setMethod("POST");
        request.setVersion("HTTP/1.0");
        request.setHeader("Host","tester");
        request.setURI("/context/dump");
        request.setHeader("Content-Type","multipart/form-data; boundary="+boundary);
        request.setContent(whitespace);
        
        response.parse(tester.getResponses(request.generate()));
        assertTrue(response.getMethod()==null);
        assertEquals(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, response.getStatus());
        assertTrue(response.getContent().indexOf("Missing initial")>=0);
    }

    @Test
    public void testLeadingWhitespaceBodyWithCRLF()
    throws Exception
    {
        String boundary = "AaB03x";

        String body = "              \n\n\n\r\n\r\n\r\n\r\n"+
        "--AaB03x\r\n"+
        "content-disposition: form-data; name=\"field1\"\r\n"+
        "\r\n"+
        "Joe Blow\r\n"+
        "--AaB03x\r\n"+
        "Content-Disposition: form-data; name=\"fileup\"; filename=\"test.upload\"\r\n"+
        "Content-Type: application/octet-stream\r\n"+
        "\r\n" +
        "aaaa,bbbbb"+"\r\n" +
        "--AaB03x--\r\n";

        // generated and parsed test
        HttpTester request = new HttpTester();
        HttpTester response = new HttpTester();
        request.setMethod("POST");
        request.setVersion("HTTP/1.0");
        request.setHeader("Host","tester");
        request.setURI("/context/dump");
        request.setHeader("Content-Type","multipart/form-data; boundary="+boundary);
        request.setContent(body);

        response.parse(tester.getResponses(request.generate()));
        assertTrue(response.getMethod()==null);
        assertEquals(HttpServletResponse.SC_OK, response.getStatus());
        assertTrue(response.getContent().contains("aaaa,bbbbb"));
    }

    @Test
    public void testLeadingWhitespaceBodyWithoutCRLF()
    throws Exception
    {
        String boundary = "AaB03x";

        String body = "              "+
        "--AaB03x\r\n"+
        "content-disposition: form-data; name=\"field1\"\r\n"+
        "\r\n"+
        "Joe Blow\r\n"+
        "--AaB03x\r\n"+
        "Content-Disposition: form-data; name=\"fileup\"; filename=\"test.upload\"\r\n"+
        "Content-Type: application/octet-stream\r\n"+
        "\r\n" +
        "aaaa,bbbbb"+"\r\n" +
        "--AaB03x--\r\n";

        // generated and parsed test
        HttpTester request = new HttpTester();
        HttpTester response = new HttpTester();
        request.setMethod("POST");
        request.setVersion("HTTP/1.0");
        request.setHeader("Host","tester");
        request.setURI("/context/dump");
        request.setHeader("Content-Type","multipart/form-data; boundary="+boundary);
        request.setContent(body);

        response.parse(tester.getResponses(request.generate()));
        assertTrue(response.getMethod()==null);
        assertEquals(HttpServletResponse.SC_OK, response.getStatus());
        assertTrue(response.getContent().contains("aaaa,bbbbb"));
    }
    

    /*
     * see the testParameterMap test
     *
     */
    public static class TestServletParameterMap extends DumpServlet
    {
        @Override
        protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
        {
            String[] content = req.getParameterMap().get("\"strup\"Content-Type: application/octet-stream");           
            assertThat (content[0], containsString("How now brown cow."));
            super.doPost(req, resp);
        }        
    }
    
    /** 
     * Validate that the getParameterMap() call is correctly unencoding the parameters in the 
     * map that it returns.
     * @throws Exception
     */
    @Test
    public void testParameterMap() throws Exception
    {
        // generated and parsed test
        HttpTester request = new HttpTester();
        HttpTester response = new HttpTester();

        tester.addServlet(TestServletParameterMap.class,"/test2");
        
        // test GET
        request.setMethod("POST");
        request.setVersion("HTTP/1.0");
        request.setHeader("Host","tester");
        request.setURI("/context/test2");
        
        String boundary="XyXyXy";
        request.setHeader("Content-Type","multipart/form-data; boundary="+boundary);
        
        
        String content = "--" + boundary + "\r\n"+
        "Content-Disposition: form-data; name=\"fileup\"; filename=\"Diplomsko Delo Lektorirano KON&#268;NA.doc\"\r\n"+
        "Content-Type: application/octet-stream\r\n\r\n"+
        "How now brown cow."+
        "\r\n--" + boundary + "\r\n"+
        "Content-Disposition: form-data; name=\"strup\""+
        "Content-Type: application/octet-stream\r\n\r\n"+
        "How now brown cow."+
        "\r\n--" + boundary + "--\r\n\r\n";
        
        request.setContent(content);
        
        response.parse(tester.getResponses(request.generate()));
        assertTrue(response.getMethod()==null);
        assertEquals(HttpServletResponse.SC_OK,response.getStatus());
        assertTrue(response.getContent().indexOf("brown cow")>=0);
    }
    
    public static class DumpServlet extends HttpServlet
    {
        private static final long serialVersionUID = 201012011130L;

        /* ------------------------------------------------------------ */
        /**
         * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
         */
        @Override
        protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
        {
            FileInputStream in = null;
            try {
                File file = (File)req.getAttribute("fileup");
                in = new FileInputStream(file);
                
                PrintWriter out = resp.getWriter();
                out.printf("Filename [%s]\r\n", req.getParameter("fileup"));
                out.println(IO.toString(in));
            } finally {
                IO.close(in);
            }
        }
    }
}
