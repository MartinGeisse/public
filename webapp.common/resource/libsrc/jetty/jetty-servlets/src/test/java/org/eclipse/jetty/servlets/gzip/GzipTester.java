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

package org.eclipse.jetty.servlets.gzip;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.DigestOutputStream;
import java.security.MessageDigest;
import java.util.EnumSet;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;
import java.util.zip.Inflater;
import javax.servlet.DispatcherType;
import java.util.zip.InflaterInputStream;

import javax.servlet.Servlet;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.io.ByteArrayBuffer;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.servlets.GzipFilter;
import org.eclipse.jetty.testing.HttpTester;
import org.eclipse.jetty.testing.ServletTester;
import org.eclipse.jetty.toolchain.test.IO;
import org.eclipse.jetty.toolchain.test.MavenTestingUtils;
import org.eclipse.jetty.toolchain.test.TestingDir;
import org.junit.Assert;

public class GzipTester
{
    private Class<? extends GzipFilter> gzipFilterClass = GzipFilter.class;
    private String encoding = "ISO8859_1";
    private String userAgent = null;
    private ServletTester servletTester;
    private TestingDir testdir;
    private String compressionType;

    public GzipTester(TestingDir testingdir, String compressionType)
    {
        this.testdir = testingdir;
        this.compressionType = compressionType;
        // Make sure we start with a clean testing directory.
        // DOES NOT WORK IN WINDOWS - this.testdir.ensureEmpty();
    }

    public HttpTester assertIsResponseGzipCompressed(String method,String filename) throws Exception
    {
        return assertIsResponseGzipCompressed(method,filename,filename);
    }

    public HttpTester assertIsResponseGzipCompressed(String method,String requestedFilename, String serverFilename) throws Exception
    {
        System.err.printf("[GzipTester] requesting /context/%s%n",requestedFilename);
        HttpTester request = new HttpTester();
        HttpTester response = new HttpTester();

        request.setMethod(method);
        request.setVersion("HTTP/1.0");
        request.setHeader("Host","tester");
        request.setHeader("Accept-Encoding",compressionType);
        if (this.userAgent != null)
            request.setHeader("User-Agent", this.userAgent);
        request.setURI("/context/" + requestedFilename);

        // Issue the request
        ByteArrayBuffer reqsBuff = new ByteArrayBuffer(request.generate().getBytes());
        // Collect the response(s)
        ByteArrayBuffer respBuff = servletTester.getResponses(reqsBuff);
        response.parse(respBuff.asArray());

        // Assert the response headers
        Assert.assertThat("Response.method",response.getMethod(),nullValue());
        //        Assert.assertThat("Response.status",response.getStatus(),is(HttpServletResponse.SC_OK));
        Assert.assertThat("Response.header[Content-Length]",response.getHeader("Content-Length"),notNullValue());
        int qindex = compressionType.indexOf(";");
        if (qindex < 0)
            Assert.assertThat("Response.header[Content-Encoding]",response.getHeader("Content-Encoding"),containsString(compressionType));
        else
            Assert.assertThat("Response.header[Content-Encoding]", response.getHeader("Content-Encoding"),containsString(compressionType.substring(0,qindex)));

        // Assert that the decompressed contents are what we expect.
        File serverFile = testdir.getFile(serverFilename);
        String expected = IO.readToString(serverFile);
        String actual = null;

        ByteArrayInputStream bais = null;
        InputStream in = null;
        ByteArrayOutputStream out = null;
        try
        {
            bais = new ByteArrayInputStream(response.getContentBytes());
            if (compressionType.startsWith(GzipFilter.GZIP))
            {
                in = new GZIPInputStream(bais);
            }
            else if (compressionType.startsWith(GzipFilter.DEFLATE))
            {
                in = new InflaterInputStream(bais, new Inflater(true));
            }
            out = new ByteArrayOutputStream();
            IO.copy(in,out);

            actual = out.toString(encoding);
            assertThat("Uncompressed contents",actual,equalTo(expected));
        }
        finally
        {
            IO.close(out);
            IO.close(in);
            IO.close(bais);
        }
        
        return response;
    }

    /**
     * Makes sure that the response contains an unfiltered file contents.
     * <p>
     * This is used to test exclusions and passthroughs in the GzipFilter.
     * <p>
     * An example is to test that it is possible to configure GzipFilter to not recompress content that shouldn't be
     * compressed by the GzipFilter.
     *
     * @param requestedFilename
     *            the filename used to on the GET request,.
     * @param testResourceSha1Sum
     *            the sha1sum file that contains the SHA1SUM checksum that will be used to verify that the response
     *            contents are what is intended.
     * @param expectedContentType
     */
    public void assertIsResponseNotGzipFiltered(String requestedFilename, String testResourceSha1Sum, String expectedContentType) throws Exception
    {
        System.err.printf("[GzipTester] requesting /context/%s%n",requestedFilename);
        HttpTester request = new HttpTester();
        HttpTester response = new HttpTester();

        request.setMethod("GET");
        request.setVersion("HTTP/1.0");
        request.setHeader("Host","tester");
        request.setHeader("Accept-Encoding",compressionType);
        if (this.userAgent != null)
            request.setHeader("User-Agent", this.userAgent);
        request.setURI("/context/" + requestedFilename);

        // Issue the request
        ByteArrayBuffer reqsBuff = new ByteArrayBuffer(request.generate().getBytes());
        // Collect the response(s)
        ByteArrayBuffer respBuff = servletTester.getResponses(reqsBuff);
        response.parse(respBuff.asArray());

        dumpHeaders(requestedFilename + " / Response Headers",response);

        // Assert the response headers
        String prefix = requestedFilename + " / Response";
        Assert.assertThat(prefix + ".method",response.getMethod(),nullValue());
        Assert.assertThat(prefix + ".status",response.getStatus(),is(HttpServletResponse.SC_OK));
        Assert.assertThat(prefix + ".header[Content-Length]",response.getHeader("Content-Length"),notNullValue());
        Assert.assertThat(prefix + ".header[Content-Encoding] (should not be recompressed by GzipFilter)",response.getHeader("Content-Encoding"),nullValue());
        Assert.assertThat(prefix + ".header[Content-Type] (should have a Content-Type associated with it)",response.getHeader("Content-Type"),notNullValue());
        Assert.assertThat(prefix + ".header[Content-Type]",response.getHeader("Content-Type"),is(expectedContentType));

        ByteArrayInputStream bais = null;
        DigestOutputStream digester = null;
        try
        {
            MessageDigest digest = MessageDigest.getInstance("SHA1");
            bais = new ByteArrayInputStream(response.getContentBytes());
            digester = new DigestOutputStream(new NoOpOutputStream(),digest);
            IO.copy(bais,digester);

            String actualSha1Sum = Hex.asHex(digest.digest());
            String expectedSha1Sum = loadExpectedSha1Sum(testResourceSha1Sum);
            Assert.assertEquals(requestedFilename + " / SHA1Sum of content",expectedSha1Sum,actualSha1Sum);
        }
        finally
        {
            IO.close(digester);
            IO.close(bais);
        }
    }

    private void dumpHeaders(String prefix, HttpTester http)
    {
        System.out.println(prefix);
        @SuppressWarnings("unchecked")
        Enumeration<String> names = http.getHeaderNames();
        while (names.hasMoreElements())
        {
            String name = names.nextElement();
            String value = http.getHeader(name);
            System.out.printf("  [%s] = %s%n",name,value);
        }
    }

    private String loadExpectedSha1Sum(String testResourceSha1Sum) throws IOException
    {
        File sha1File = MavenTestingUtils.getTestResourceFile(testResourceSha1Sum);
        String contents = IO.readToString(sha1File);
        Pattern pat = Pattern.compile("^[0-9A-Fa-f]*");
        Matcher mat = pat.matcher(contents);
        Assert.assertTrue("Should have found HEX code in SHA1 file: " + sha1File,mat.find());
        return mat.group();
    }

    /**
     * Asserts that the requested filename results in a properly structured GzipFilter response, where the content is
     * not compressed, and the content-length is returned appropriately.
     *
     * @param filename
     *            the filename used for the request, and also used to compare the response to the server file, assumes
     *            that the file is suitable for {@link Assert#assertEquals(Object, Object)} use. (in other words, the
     *            contents of the file are text)
     * @param expectedFilesize
     *            the expected filesize to be specified on the Content-Length portion of the response headers. (note:
     *            passing -1 will disable the Content-Length assertion)
     * @throws Exception
     */
    public HttpTester assertIsResponseNotGzipCompressed(String method,String filename, int expectedFilesize, int status) throws Exception
    {
        String uri = "/context/"+filename;
        HttpTester response = executeRequest(method,uri);
        assertResponseHeaders(expectedFilesize,status,response);

        // Assert that the contents are what we expect.
        if (filename != null)
        {
            File serverFile = testdir.getFile(filename);
            String expectedResponse = IO.readToString(serverFile);
            
            String actual = readResponse(response);
            Assert.assertEquals("Expected response equals actual response",expectedResponse,actual);
        }
        
        return response;
    }
    

    /**
     * Asserts that the request results in a properly structured GzipFilter response, where the content is
     * not compressed, and the content-length is returned appropriately.
     *
     * @param expectedResponse
     *            the expected response body string
     * @param expectedFilesize
     *            the expected filesize to be specified on the Content-Length portion of the response headers. (note:
     *            passing -1 will disable the Content-Length assertion)
     * @throws Exception
     */
    public void assertIsResponseNotGzipCompressedAndEqualToExpectedString(String method,String expectedResponse, int expectedFilesize, int status) throws Exception
    {
        String uri = "/context/";
        HttpTester response = executeRequest(method,uri);
        assertResponseHeaders(expectedFilesize,status,response);

        String actual = readResponse(response);
        Assert.assertEquals("Expected response equals actual response",expectedResponse,actual);
    }
    
    /**
     * Asserts that the request results in a properly structured GzipFilter response, where the content is
     * not compressed, and the content-length is returned appropriately.
     *
     * @param expectedFilesize
     *            the expected filesize to be specified on the Content-Length portion of the response headers. (note:
     *            passing -1 will disable the Content-Length assertion)
     * @throws Exception
     */
    public void assertIsResponseNotGzipCompressed(String method,int expectedFilesize, int status) throws Exception
    {
        String uri = "/context/";
        HttpTester response = executeRequest(method, uri);
        assertResponseHeaders(expectedFilesize,status,response);
    }

    private void assertResponseHeaders(int expectedFilesize, int status, HttpTester response)
    {
        Assert.assertThat("Response.method",response.getMethod(),nullValue());
        Assert.assertThat("Response.status",response.getStatus(),is(status));
        Assert.assertThat("Response.header[Content-Encoding]",response.getHeader("Content-Encoding"),not(containsString(compressionType)));
        if (expectedFilesize != (-1))
        {
            Assert.assertThat("Response.header[Content-Length]",response.getHeader("Content-Length"),notNullValue());
            int serverLength = Integer.parseInt(response.getHeader("Content-Length"));
            Assert.assertThat("Response.header[Content-Length]",serverLength,is(expectedFilesize));
        }
    }

    private HttpTester executeRequest(String method,String uri) throws IOException, Exception
    {
        System.err.printf("[GzipTester] requesting %s%n",uri);
        HttpTester request = new HttpTester();
        HttpTester response = new HttpTester();
        
        request.setMethod(method);
        request.setVersion("HTTP/1.0");
        request.setHeader("Host","tester");
        request.setHeader("Accept-Encoding",compressionType);
        if (this.userAgent != null)
            request.setHeader("User-Agent", this.userAgent);
        
        request.setURI(uri);
        
        // Issue the request
        ByteArrayBuffer reqsBuff = new ByteArrayBuffer(request.generate().getBytes());
        // Collect the response(s)
        ByteArrayBuffer respBuff = servletTester.getResponses(reqsBuff);
        response.parse(respBuff.asArray());
        return response;
    }

    private String readResponse(HttpTester response) throws IOException, UnsupportedEncodingException
    {
        String actual = null;
        InputStream in = null;
        ByteArrayOutputStream out = null;
        try
        {
            in = new ByteArrayInputStream(response.getContentBytes());
            out = new ByteArrayOutputStream();
            IO.copy(in,out);

            actual = out.toString(encoding);
        }
        finally
        {
            IO.close(out);
            IO.close(in);
        }
        return actual;
    }


    /**
     * Generate string content of arbitrary length.
     *
     * @param length
     *            the length of the string to generate.
     * @return the string content.
     */
    public String generateContent(int length)
    {
        StringBuilder builder = new StringBuilder();
        do
        {
            builder.append("Lorem ipsum dolor sit amet, consectetur adipiscing elit. In quis felis nunc.\n");
            builder.append("Quisque suscipit mauris et ante auctor ornare rhoncus lacus aliquet. Pellentesque\n");
            builder.append("habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas.\n");
            builder.append("Vestibulum sit amet felis augue, vel convallis dolor. Cras accumsan vehicula diam\n");
            builder.append("at faucibus. Etiam in urna turpis, sed congue mi. Morbi et lorem eros. Donec vulputate\n");
            builder.append("velit in risus suscipit lobortis. Aliquam id urna orci, nec sollicitudin ipsum.\n");
            builder.append("Cras a orci turpis. Donec suscipit vulputate cursus. Mauris nunc tellus, fermentum\n");
            builder.append("eu auctor ut, mollis at diam. Quisque porttitor ultrices metus, vitae tincidunt massa\n");
            builder.append("sollicitudin a. Vivamus porttitor libero eget purus hendrerit cursus. Integer aliquam\n");
            builder.append("consequat mauris quis luctus. Cras enim nibh, dignissim eu faucibus ac, mollis nec neque.\n");
            builder.append("Aliquam purus mauris, consectetur nec convallis lacinia, porta sed ante. Suspendisse\n");
            builder.append("et cursus magna. Donec orci enim, molestie a lobortis eu, imperdiet vitae neque.\n");
        }
        while (builder.length() < length);

        // Make sure we are exactly at requested length. (truncate the extra)
        if (builder.length() > length)
        {
            builder.setLength(length);
        }

        return builder.toString();
    }

    public String getEncoding()
    {
        return encoding;
    }

    /**
     * Create a file on the server resource path of a specified filename and size.
     *
     * @param filename
     *            the filename to create
     * @param filesize
     *            the file size to create (Note: this isn't suitable for creating large multi-megabyte files)
     */
    public File prepareServerFile(String filename, int filesize) throws IOException
    {
        File dir = testdir.getDir();
        File testFile = new File(dir,filename);
        // Make sure we have a uniq filename (to work around windows File.delete bug)
        int i = 0;
        while (testFile.exists())
        {
            testFile = new File(dir,(i++) + "-" + filename);
        }

        FileOutputStream fos = null;
        ByteArrayInputStream in = null;
        try
        {
            fos = new FileOutputStream(testFile,false);
            in = new ByteArrayInputStream(generateContent(filesize).getBytes(encoding));
            IO.copy(in,fos);
            return testFile;
        }
        finally
        {
            IO.close(in);
            IO.close(fos);
        }
    }

    /**
     * Copy a src/test/resource file into the server tree for eventual serving.
     *
     * @param filename
     *            the filename to look for in src/test/resources
     */
    public void copyTestServerFile(String filename) throws IOException
    {
        File srcFile = MavenTestingUtils.getTestResourceFile(filename);
        File testFile = testdir.getFile(filename);

        IO.copy(srcFile,testFile);
    }

    /**
     * Set the servlet that provides content for the GzipFilter in being tested.
     *
     * @param servletClass
     *            the servlet that will provide content.
     * @return the FilterHolder for configuring the GzipFilter's initParameters with
     */
    public FilterHolder setContentServlet(Class<? extends Servlet> servletClass) throws IOException
    {
        servletTester = new ServletTester();
        servletTester.setContextPath("/context");
        servletTester.setResourceBase(testdir.getDir().getCanonicalPath());
        ServletHolder servletHolder = servletTester.addServlet(servletClass,"/");
        servletHolder.setInitParameter("baseDir",testdir.getDir().getAbsolutePath());
        FilterHolder holder = servletTester.addFilter(gzipFilterClass,"/*",EnumSet.allOf(DispatcherType.class));
        holder.setInitParameter("vary","Accept-Encoding");
        return holder;
    }

    public Class<? extends GzipFilter> getGzipFilterClass()
    {
        return gzipFilterClass;
    }

    public void setGzipFilterClass(Class<? extends GzipFilter> gzipFilterClass)
    {
        this.gzipFilterClass = gzipFilterClass;
    }

    public void setEncoding(String encoding)
    {
        this.encoding = encoding;
    }
    
    public void setUserAgent(String ua)
    {
        this.userAgent = ua;
    }

    public void start() throws Exception
    {
        Assert.assertThat("No servlet defined yet.  Did you use #setContentServlet()?",servletTester,notNullValue());
        servletTester.dump();
        servletTester.start();
    }

    public void stop()
    {
        // NOTE: Do not cleanup the testdir.  Failures can't be diagnosed if you do that.
        // IO.delete(testdir.getDir()):
        try
        {
            servletTester.stop();
        }
        catch (Exception e)
        {
            // Don't toss this out into Junit as this would be the last exception
            // that junit will report as being the cause of the test failure.
            // when in reality, the earlier setup issue is the real cause.
            e.printStackTrace(System.err);
        }
    }
}
