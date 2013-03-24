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

package org.eclipse.jetty.util.ssl;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;

import org.eclipse.jetty.util.component.AbstractLifeCycle;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.StdErrLog;
import org.eclipse.jetty.util.resource.Resource;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertTrue;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;


public class SslContextFactoryTest
{

    private SslContextFactory cf;

    @Before
    public void setUp() throws Exception
    {
        cf = new SslContextFactory();
    }

    @Test
    public void testNoTsFileKs() throws Exception
    {
        String keystorePath = System.getProperty("basedir",".") + "/src/test/resources/keystore";
        cf.setKeyStorePassword("storepwd");
        cf.setKeyManagerPassword("keypwd");
        
        cf.start();
        
        assertTrue(cf.getSslContext()!=null);
    }
    
    @Test
    public void testNoTsStreamKs() throws Exception
    {
        InputStream keystoreInputStream = this.getClass().getResourceAsStream("keystore");

        cf.setKeyStoreInputStream(keystoreInputStream);
        cf.setKeyStorePassword("storepwd");
        cf.setKeyManagerPassword("keypwd");
        
        cf.start();
        
        assertTrue(cf.getSslContext()!=null);
    }
    
    @Test
    public void testNoTsSetKs() throws Exception
    {
        InputStream keystoreInputStream = this.getClass().getResourceAsStream("keystore");

        KeyStore ks = KeyStore.getInstance("JKS");
        ks.load(keystoreInputStream, "storepwd".toCharArray());

        cf.setKeyStore(ks);
        cf.setKeyManagerPassword("keypwd");
        
        cf.start();
        
        assertTrue(cf.getSslContext()!=null);
    }
    
    @Test
    public void testNoTsNoKs() throws Exception
    {
        cf.start();
        assertTrue(cf.getSslContext()!=null);
    }
    
    @Test
    public void testTrustAll() throws Exception
    {
        cf.start();
        assertTrue(cf.getSslContext()!=null);
    }

    @Test
    public void testNoTsResourceKs() throws Exception
    {
        Resource keystoreResource = Resource.newSystemResource("keystore");

        cf.setKeyStoreResource(keystoreResource);
        cf.setKeyStorePassword("storepwd");
        cf.setKeyManagerPassword("keypwd");

        cf.start();

        assertTrue(cf.getSslContext()!=null);
    }

    @Test
    public void testResourceTsResourceKs() throws Exception
    {
        Resource keystoreResource = Resource.newSystemResource("keystore");
        Resource truststoreResource = Resource.newSystemResource("keystore");

        cf.setKeyStoreResource(keystoreResource);
        cf.setTrustStoreResource(truststoreResource);
        cf.setKeyStorePassword("storepwd");
        cf.setKeyManagerPassword("keypwd");
        cf.setTrustStorePassword("storepwd");

        cf.start();

        assertTrue(cf.getSslContext()!=null);
    }

    @Test
    public void testResourceTsResourceKsWrongPW() throws Exception
    {
        Resource keystoreResource = Resource.newSystemResource("keystore");
        Resource truststoreResource = Resource.newSystemResource("keystore");

        cf.setKeyStoreResource(keystoreResource);
        cf.setTrustStoreResource(truststoreResource);
        cf.setKeyStorePassword("storepwd");
        cf.setKeyManagerPassword("wrong_keypwd");
        cf.setTrustStorePassword("storepwd");

        try
        {
            ((StdErrLog)Log.getLogger(AbstractLifeCycle.class)).setHideStacks(true);
            cf.start();
            Assert.fail();
        }
        catch(java.security.UnrecoverableKeyException e)
        {
        }
    }

    @Test
    public void testResourceTsWrongPWResourceKs() throws Exception
    {
        Resource keystoreResource = Resource.newSystemResource("keystore");
        Resource truststoreResource = Resource.newSystemResource("keystore");

        cf.setKeyStoreResource(keystoreResource);
        cf.setTrustStoreResource(truststoreResource);
        cf.setKeyStorePassword("storepwd");
        cf.setKeyManagerPassword("keypwd");
        cf.setTrustStorePassword("wrong_storepwd");

        try
        {
            ((StdErrLog)Log.getLogger(AbstractLifeCycle.class)).setHideStacks(true);
            cf.start();
            Assert.fail();
        }
        catch(IOException e)
        {
        }
    }
    
    @Test
    public void testNoKeyConfig() throws Exception
    {
        try
        {
            ((StdErrLog)Log.getLogger(AbstractLifeCycle.class)).setHideStacks(true);
            cf.setTrustStore("/foo");
            cf.start();
            Assert.fail();
        }
        catch (IllegalStateException e)
        {
            
        }
        catch (Exception e)
        {
            Assert.fail("Unexpected exception");
        }
    }

    @Test
    public void testSetIncludeCipherSuitesPreservesOrder()
    {
        String[] supportedCipherSuites = new String[]{"cipher4", "cipher2", "cipher1", "cipher3"};
        String[] includeCipherSuites = {"cipher1", "cipher3", "cipher4"};

        cf.setIncludeCipherSuites(includeCipherSuites);
        String[] selectedCipherSuites = cf.selectCipherSuites(null, supportedCipherSuites);

        assertSelectedMatchesIncluded(includeCipherSuites, selectedCipherSuites);
    }

    @Test
    public void testSetIncludeProtocolsPreservesOrder()
    {
        String[] supportedProtocol = new String[]{"cipher4", "cipher2", "cipher1", "cipher3"};
        String[] includeProtocol = {"cipher1", "cipher3", "cipher4"};

        cf.setIncludeProtocols(includeProtocol);
        String[] selectedProtocol = cf.selectProtocols(null, supportedProtocol);

        assertSelectedMatchesIncluded(includeProtocol, selectedProtocol);
    }

    private void assertSelectedMatchesIncluded(String[] includeStrings, String[] selectedStrings)
    {
        assertThat(includeStrings.length + " strings are selected", selectedStrings.length, is(includeStrings.length));
        assertThat("order from includeStrings is preserved", selectedStrings[0], equalTo(includeStrings[0]));
        assertThat("order from includeStrings is preserved", selectedStrings[1], equalTo(includeStrings[1]));
        assertThat("order from includeStrings is preserved", selectedStrings[2], equalTo(includeStrings[2]));
    }
}
