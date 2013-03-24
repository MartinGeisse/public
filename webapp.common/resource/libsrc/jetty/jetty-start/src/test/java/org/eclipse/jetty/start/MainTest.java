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

package org.eclipse.jetty.start;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.eclipse.jetty.toolchain.test.MavenTestingUtils;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/* ------------------------------------------------------------ */
/**
 */
public class MainTest
{
    /* ------------------------------------------------------------ */
    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception
    {
        File testJettyHome = MavenTestingUtils.getTestResourceDir("jetty.home");
        System.setProperty("jetty.home",testJettyHome.getAbsolutePath());
    }

    @Test
    public void testLoadStartIni() throws IOException
    {
        Main main = new Main();
        List<String> args = main.parseStartIniFiles();
        assertEquals("Expected 5 uncommented lines in start.ini",9,args.size());
        assertEquals("First uncommented line in start.ini doesn't match expected result","OPTIONS=Server,jsp,resources,websocket,ext",args.get(0));
        assertEquals("Last uncommented line in start.ini doesn't match expected result","etc/jetty-testrealm.xml",args.get(8));
    }

    @Test
    public void testExpandCommandLine() throws Exception
    {
        Main main = new Main();
        List<String> args = main.expandCommandLine(new String[] {});
        assertEquals("start.ini OPTIONS","OPTIONS=Server,jsp,resources,websocket,ext",args.get(0));
        assertEquals("start.d/jmx OPTIONS","OPTIONS=jmx",args.get(5));
        assertEquals("start.d/jmx XML","--pre=etc/jetty-jmx.xml",args.get(6));
        assertEquals("start.d/websocket OPTIONS","OPTIONS=websocket",args.get(7));
    }

    @Test
    public void testProcessCommandLine() throws Exception
    {
        Main main = new Main();
        List<String> args = main.expandCommandLine(new String[] {});
        List<String> xmls = main.processCommandLine(args);

        assertEquals("jmx --pre","etc/jetty-jmx.xml",xmls.get(0));
        assertEquals("start.ini","etc/jetty.xml",xmls.get(1));
        assertEquals("start.d","etc/jetty-testrealm.xml",xmls.get(5));
    }

    @Test
    public void testBuildCommandLine() throws IOException, NoSuchFieldException, IllegalAccessException
    {
        List<String> jvmArgs = new ArrayList<String>();
        jvmArgs.add("--exec");
        jvmArgs.add("-Xms1024m");
        jvmArgs.add("-Xmx1024m");

        List<String> xmls = new ArrayList<String>();
        xmls.add("jetty.xml");
        xmls.add("jetty-jmx.xml");
        xmls.add("jetty-logging.xml");

        Main main = new Main();
        main.addJvmArgs(jvmArgs);

        Classpath classpath = nastyWayToCreateAClasspathObject("/jetty/home with spaces/");
        CommandLineBuilder cmd = main.buildCommandLine(classpath,xmls);
        assertThat("CommandLineBuilder shouldn't be null",cmd,notNullValue());

        List<String> commandArgs = cmd.getArgs();
        assertThat("commandArgs should contain 11 elements",commandArgs.size(),equalTo(11));
        assertThat("args does not contain -cp",commandArgs,hasItems("-cp"));
        assertThat("Classpath should be correctly quoted and match expected value",commandArgs,
                hasItems("/jetty/home with spaces/somejar.jar:/jetty/home with spaces/someotherjar.jar"));
        assertThat("args does not contain --exec",commandArgs,hasItems("--exec"));
        assertThat("CommandLine should contain jvmArgs",commandArgs,hasItems("-Xms1024m"));
        assertThat("CommandLine should contain jvmArgs", commandArgs, hasItems("-Xmx1024m"));
        assertThat("CommandLine should contain xmls",commandArgs,hasItems("jetty.xml"));
        assertThat("CommandLine should contain xmls",commandArgs,hasItems("jetty-jmx.xml"));
        assertThat("CommandLine should contain xmls", commandArgs, hasItems("jetty-logging.xml"));

        String commandLine = cmd.toString();
        assertThat("cmd.toString() should be properly escaped",commandLine,containsString("-cp /jetty/home\\ with\\ " +
                "spaces/somejar.jar:/jetty/home\\ with\\ spaces/someotherjar.jar"));
        assertThat("cmd.toString() doesn't contain xml config files",commandLine,containsString(" jetty.xml jetty-jmx.xml jetty-logging.xml"));
    }

    private Classpath nastyWayToCreateAClasspathObject(String jettyHome) throws NoSuchFieldException, IllegalAccessException
    {
        Classpath classpath = new Classpath();
        Field classpathElements = Classpath.class.getDeclaredField("_elements");
        classpathElements.setAccessible(true);
        File file = new File(jettyHome + "somejar.jar");
        File file2 = new File(jettyHome + "someotherjar.jar");
        Vector<File> elements = new Vector<File>();
        elements.add(file);
        elements.add(file2);
        classpathElements.set(classpath,elements);
        return classpath;
    }

}
