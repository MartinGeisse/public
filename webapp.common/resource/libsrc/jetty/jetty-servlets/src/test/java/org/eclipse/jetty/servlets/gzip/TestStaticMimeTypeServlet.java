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

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.http.MimeTypes;
import org.eclipse.jetty.io.Buffer;

/**
 * Test servlet for testing against unusual MimeTypes and Content-Types.
 */
@SuppressWarnings("serial")
public class TestStaticMimeTypeServlet extends TestDirContentServlet
{
    private MimeTypes mimeTypes;

    @Override
    public void init(ServletConfig config) throws ServletException
    {
        super.init(config);
        mimeTypes = new MimeTypes();
        // Some real world, yet not terribly common, mime type mappings.
        mimeTypes.addMimeMapping("bz2","application/bzip2");
        mimeTypes.addMimeMapping("bmp","image/bmp");
        mimeTypes.addMimeMapping("tga","application/tga");
        mimeTypes.addMimeMapping("xcf","image/xcf");
        mimeTypes.addMimeMapping("jp2","image/jpeg2000");
        
        // Some of the other gzip mime-types seen in the wild.
        // NOTE: Using oddball extensions just so that the calling request can specify
        //       which strange mime type to use.
        mimeTypes.addMimeMapping("x-gzip","application/x-gzip");
        mimeTypes.addMimeMapping("x-gunzip","application/x-gunzip");
        mimeTypes.addMimeMapping("gzipped","application/gzippped");
        mimeTypes.addMimeMapping("gzip-compressed","application/gzip-compressed");
        mimeTypes.addMimeMapping("x-compressed","application/x-compressed");
        mimeTypes.addMimeMapping("x-compress","application/x-compress");
        mimeTypes.addMimeMapping("gzipdoc","gzip/document");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        String fileName = request.getServletPath();
        byte[] dataBytes = loadContentFileBytes(fileName);

        response.setContentLength(dataBytes.length);

        Buffer buf = mimeTypes.getMimeByExtension(fileName);
        if (buf == null)
        {
            response.setContentType("application/octet-stream");
        }
        else
        {
            response.setContentType(buf.toString());
        }

        ServletOutputStream out = response.getOutputStream();
        out.write(dataBytes);
    }
}
