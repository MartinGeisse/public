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

import java.io.File;

import org.eclipse.jetty.client.security.Realm;
import org.eclipse.jetty.client.security.SimpleRealmResolver;
import org.eclipse.jetty.http.HttpMethods;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.toolchain.test.MavenTestingUtils;
import org.eclipse.jetty.toolchain.test.PathAssert;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Functional testing for HttpExchange.
 */
public class WebdavListenerTest
{
    protected String _scheme = "http://";
    protected Server _server;
    protected int _port;
    protected HttpClient _httpClient;
    protected Connector _connector;

    private String _username = "janb";
    private String _password = "xxxxx";

    private String _singleFileURL;
    private String _dirFileURL;
    private String _dirURL;
    
    @Before
    public void setUp() throws Exception
    {
        _singleFileURL = "https://dav.codehaus.org/user/" + _username + "/foo.txt";
        _dirURL = "https://dav.codehaus.org/user/" + _username + "/ttt/";
        _dirFileURL = _dirURL+"foo.txt";
        _scheme="https://";
 
        _httpClient=new HttpClient();
        _httpClient.setConnectorType(HttpClient.CONNECTOR_SELECT_CHANNEL);
        //_httpClient.setMaxConnectionsPerAddress(4);

        _httpClient.setRealmResolver( new SimpleRealmResolver (
                new Realm(){
                    public String getId()
                    {
                        return _username + "'s webspace";  //To change body of implemented methods use File | Settings | File Templates.
                    }

                    public String getPrincipal()
                    {
                        return _username;  //To change body of implemented methods use File | Settings | File Templates.
                    }

                    public String getCredentials()
                    {
                        return _password;  //To change body of implemented methods use File | Settings | File Templates.
                    }
                }
        ));

        _httpClient.registerListener( "org.eclipse.jetty.client.webdav.WebdavListener");
        _httpClient.start();
    }
    
    
    @After
    public void tearDown () throws Exception
    {
        _httpClient.stop();
    }
   

    @Test
    @Ignore("Only works with real WebDAV server")
    public void testPUTandDELETEwithSSL() throws Exception
    {
    	File file = MavenTestingUtils.getTestResourceFile("foo.txt");
    	PathAssert.assertFileExists("WebDAV test file", file);
    	
        //PUT a FILE
        ContentExchange singleFileExchange = new ContentExchange();
        singleFileExchange.setURL(_singleFileURL);
        singleFileExchange.setMethod( HttpMethods.PUT );
        singleFileExchange.setFileForUpload(file);
        singleFileExchange.setRequestHeader( "Content-Type", "application/octet-stream");
        singleFileExchange.setRequestHeader("Content-Length", String.valueOf( file.length() ));
        _httpClient.send(singleFileExchange);
        singleFileExchange.waitForDone();
        
        String result = singleFileExchange.getResponseContent();
        Assert.assertEquals(201, singleFileExchange.getResponseStatus());    
       
        //PUT a FILE in a directory hierarchy
        ContentExchange dirFileExchange = new ContentExchange();
        dirFileExchange.setURL(_dirFileURL);
        dirFileExchange.setMethod( HttpMethods.PUT );
        dirFileExchange.setFileForUpload(file);
        dirFileExchange.setRequestHeader( "Content-Type", "application/octet-stream");
        dirFileExchange.setRequestHeader("Content-Length", String.valueOf( file.length() ));
        _httpClient.send(dirFileExchange);
        dirFileExchange.waitForDone();
        result = dirFileExchange.getResponseContent();        
        Assert.assertEquals(201, singleFileExchange.getResponseStatus());
       
     
        //DELETE the single file
        HttpExchange del = new HttpExchange();
        del.setURL(_singleFileURL);
        del.setMethod(HttpMethods.DELETE);
        _httpClient.send(del);
        del.waitForDone();
          
        //DELETE the whole dir
        del.setURL(_dirURL);
        del.setMethod(HttpMethods.DELETE);  
        del.setRequestHeader("Depth", "infinity");
        _httpClient.send(del);
        del.waitForDone();
    }
}
