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

package org.eclipse.jetty.webapp;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.webapp.Ordering.AbsoluteOrdering;
import org.eclipse.jetty.webapp.Ordering.RelativeOrdering;
import org.junit.Test;

/**
 * OrderingTest
 *
 *
 */
public class OrderingTest
{
    public class TestResource extends Resource
    {
        public String _name;
        
        public TestResource (String name)
        {
            _name =name;
        }

        /** 
         * @see org.eclipse.jetty.util.resource.Resource#addPath(java.lang.String)
         */
        @Override
        public Resource addPath(String path) throws IOException, MalformedURLException
        {
            return null;
        }

        /** 
         * @see org.eclipse.jetty.util.resource.Resource#delete()
         */
        @Override
        public boolean delete() throws SecurityException
        {
            return false;
        }

        /** 
         * @see org.eclipse.jetty.util.resource.Resource#exists()
         */
        @Override
        public boolean exists()
        {
            return false;
        }

        /** 
         * @see org.eclipse.jetty.util.resource.Resource#getFile()
         */
        @Override
        public File getFile() throws IOException
        {
            return null;
        }

        /** 
         * @see org.eclipse.jetty.util.resource.Resource#getInputStream()
         */
        @Override
        public InputStream getInputStream() throws IOException
        {
            return null;
        }

        /** 
         * @see org.eclipse.jetty.util.resource.Resource#getName()
         */
        @Override
        public String getName()
        {
            return _name;
        }

        /** 
         * @see org.eclipse.jetty.util.resource.Resource#getOutputStream()
         */
        @Override
        public OutputStream getOutputStream() throws IOException, SecurityException
        {
            return null;
        }

        /** 
         * @see org.eclipse.jetty.util.resource.Resource#getURL()
         */
        @Override
        public URL getURL()
        {
            return null;
        }

        /** 
         * @see org.eclipse.jetty.util.resource.Resource#isContainedIn(org.eclipse.jetty.util.resource.Resource)
         */
        @Override
        public boolean isContainedIn(Resource r) throws MalformedURLException
        {
            return false;
        }

        /** 
         * @see org.eclipse.jetty.util.resource.Resource#isDirectory()
         */
        @Override
        public boolean isDirectory()
        {
            return false;
        }

        /** 
         * @see org.eclipse.jetty.util.resource.Resource#lastModified()
         */
        @Override
        public long lastModified()
        {
            return 0;
        }

        /** 
         * @see org.eclipse.jetty.util.resource.Resource#length()
         */
        @Override
        public long length()
        {
            return 0;
        }

        /** 
         * @see org.eclipse.jetty.util.resource.Resource#list()
         */
        @Override
        public String[] list()
        {
            return null;
        }

        /** 
         * @see org.eclipse.jetty.util.resource.Resource#release()
         */
        @Override
        public void release()
        {
        }

        /** 
         * @see org.eclipse.jetty.util.resource.Resource#renameTo(org.eclipse.jetty.util.resource.Resource)
         */
        @Override
        public boolean renameTo(Resource dest) throws SecurityException
        {
            return false;
        }
        
    }
    
    @Test
    public void testRelativeOrdering0 ()
    throws Exception
    {
        //Example from ServletSpec p.70
        WebAppContext wac = new WebAppContext();
        MetaData metaData = new MetaData();
        List<Resource> resources = new ArrayList<Resource>();
        metaData._ordering = new RelativeOrdering(metaData);
        
        //A: after others, after C
        TestResource jar1 = new TestResource("A");
        resources.add(jar1);
        TestResource r1 = new TestResource("A/web-fragment.xml");
        FragmentDescriptor f1 = new FragmentDescriptor(r1);
        f1._name = "A";
        metaData._webFragmentNameMap.put(f1._name, f1);
        metaData._webFragmentResourceMap.put(jar1, f1);
        f1._otherType = FragmentDescriptor.OtherType.After;
        //((RelativeOrdering)metaData._ordering).addAfterOthers(r1);
        f1._afters.add("C");
        
        //B: before others
        TestResource jar2 = new TestResource("B");
        resources.add(jar2);
        TestResource r2 = new TestResource("B/web-fragment.xml");
        FragmentDescriptor f2 = new FragmentDescriptor(r2);
        f2._name="B";
        metaData._webFragmentNameMap.put(f2._name, f2);
        metaData._webFragmentResourceMap.put(jar2, f2);
        f2._otherType = FragmentDescriptor.OtherType.Before;
        //((RelativeOrdering)metaData._ordering).addBeforeOthers(r2);
        
        //C: after others
        TestResource jar3 = new TestResource("C");
        resources.add(jar3);
        TestResource r3 = new TestResource("C/web-fragment.xml");
        FragmentDescriptor f3 = new FragmentDescriptor(r3);
        f3._name="C";
        metaData._webFragmentNameMap.put(f3._name, f3);
        metaData._webFragmentResourceMap.put(jar3, f3);
        f3._otherType = FragmentDescriptor.OtherType.After;
        //((RelativeOrdering)metaData._ordering).addAfterOthers(r3);
        
        //D: no ordering
        TestResource jar4 = new TestResource("D");
        resources.add(jar4);
        TestResource r4 = new TestResource("D/web-fragment.xml");
        FragmentDescriptor f4 = new FragmentDescriptor(r4);
        f4._name="D"; 
        metaData._webFragmentNameMap.put(f4._name, f4);
        metaData._webFragmentResourceMap.put(jar4, f4);
        f4._otherType = FragmentDescriptor.OtherType.None;
        //((RelativeOrdering)metaData._ordering).addNoOthers(r4);
        
        //E: no ordering   
        TestResource jar5 = new TestResource("E");     
        resources.add(jar5);
        TestResource r5 = new TestResource("E/web-fragment.xml");
        FragmentDescriptor f5 = new FragmentDescriptor(r5);
        f5._name="E"; 
        metaData._webFragmentNameMap.put(f5._name, f5);
        metaData._webFragmentResourceMap.put(jar5, f5);
        f5._otherType = FragmentDescriptor.OtherType.None;
        //((RelativeOrdering)metaData._ordering).addNoOthers(r5);
        
        //F: before others, before B
        TestResource jar6 = new TestResource("F");
        resources.add(jar6);
        TestResource r6 = new TestResource("F/web-fragment.xml");
        FragmentDescriptor f6 = new FragmentDescriptor(r6);
        f6._name="F";
        metaData._webFragmentNameMap.put(f6._name, f6);
        metaData._webFragmentResourceMap.put(jar6,f6);
        f6._otherType = FragmentDescriptor.OtherType.Before;
        //((RelativeOrdering)metaData._ordering).addBeforeOthers(r6);
        f6._befores.add("B");
        
        //
        // p.70 outcome: F, B, D, E, C, A
        //
        String[] outcomes = {"FBDECA"};
        List<Resource> orderedList = metaData._ordering.order(resources);
        
        String result = "";
        for (Resource r:orderedList)
            result+=(((TestResource)r)._name);
        
        if (!checkResult(result, outcomes))
            fail("No outcome matched "+result);
    }
 
  

    @Test
    public void testRelativeOrdering1 ()
    throws Exception
    {
        List<Resource> resources = new ArrayList<Resource>();
        WebAppContext wac = new WebAppContext();
        MetaData metaData = new MetaData();
        metaData._ordering = new RelativeOrdering(metaData);
        
        //Example from ServletSpec p.70-71
        //No name: after others, before C
        TestResource jar1 = new TestResource("plain");
        resources.add(jar1);
        TestResource r1 = new TestResource("plain/web-fragment.xml");
        FragmentDescriptor f1 = new FragmentDescriptor(r1);
        f1._name = FragmentDescriptor.NAMELESS+"1";
        metaData._webFragmentNameMap.put(f1._name, f1); 
        metaData._webFragmentResourceMap.put(jar1,f1);
        f1._otherType = FragmentDescriptor.OtherType.After;
        //((RelativeOrdering)metaData._ordering).addAfterOthers(f1);
        f1._befores.add("C");
        
        //B: before others
        TestResource jar2 = new TestResource("B");
        resources.add(jar2);
        TestResource r2 = new TestResource("B/web-fragment.xml");
        FragmentDescriptor f2 = new FragmentDescriptor(r2);
        f2._name="B";
        metaData._webFragmentNameMap.put(f2._name, f2);
        metaData._webFragmentResourceMap.put(jar2,f2);
        f2._otherType = FragmentDescriptor.OtherType.Before;
        //((RelativeOrdering)metaData._ordering).addBeforeOthers(f2);
               
        //C: no ordering
        TestResource jar3 = new TestResource("C");
        resources.add(jar3);
        TestResource r3 = new TestResource("C/web-fragment.xml");
        FragmentDescriptor f3 = new FragmentDescriptor(r3);
        f3._name="C";
        metaData._webFragmentNameMap.put(f3._name, f3);
        metaData._webFragmentResourceMap.put(jar3,f3);
        //((RelativeOrdering)metaData._ordering).addNoOthers(f3);
        f3._otherType = FragmentDescriptor.OtherType.None;
        
        //D: after others
        TestResource jar4 = new TestResource("D");
        resources.add(jar4);
        TestResource r4 = new TestResource("D/web-fragment.xml");
        FragmentDescriptor f4 = new FragmentDescriptor(r4);
        f4._name="D"; 
        metaData._webFragmentNameMap.put(f4._name, f4);
        metaData._webFragmentResourceMap.put(jar4,f4);
        //((RelativeOrdering)metaData._ordering).addAfterOthers(f4);
        f4._otherType = FragmentDescriptor.OtherType.After;
        
        //E: before others
        TestResource jar5 = new TestResource("E");
        resources.add(jar5);
        TestResource r5 = new TestResource("E/web-fragment.xml");
        FragmentDescriptor f5 = new FragmentDescriptor(r5);
        f5._name="E";
        metaData._webFragmentNameMap.put(f5._name, f5);
        metaData._webFragmentResourceMap.put(jar5,f5);
        //((RelativeOrdering)metaData._ordering).addBeforeOthers(f5);
        f5._otherType = FragmentDescriptor.OtherType.Before;
        
        //F: no ordering
        TestResource jar6 = new TestResource("F");
        resources.add(jar6);
        TestResource r6 = new TestResource("F/web-fragment.xml");
        FragmentDescriptor f6 = new FragmentDescriptor(r6);
        f6._name="F";
        metaData._webFragmentNameMap.put(f6._name, f6);
        metaData._webFragmentResourceMap.put(jar6,f6);
        //((RelativeOrdering)metaData._ordering).addNoOthers(f6);
        f6._otherType = FragmentDescriptor.OtherType.None;
        
        List<Resource> orderedList = metaData._ordering.order(resources);

        // p.70-71 Possible outcomes are:
        // B, E, F, noname, C, D
        // B, E, F, noname, D, C
        // E, B, F, noname, C, D
        // E, B, F, noname, D, C
        // E, B, F, D, noname, C
        //
        String[] outcomes = {"BEFplainCD", 
                             "BEFplainDC",
                             "EBFplainCD",
                             "EBFplainDC",
                             "EBFDplain"};
        
        String orderedNames = "";
        for (Resource r:orderedList)
            orderedNames+=(((TestResource)r)._name);
        
        if (!checkResult(orderedNames, outcomes))
            fail("No outcome matched "+orderedNames);
    }
    
    @Test
    public void testRelativeOrdering2 ()
    throws Exception
    {
        List<Resource> resources = new ArrayList<Resource>();
        WebAppContext wac = new WebAppContext();
        MetaData metaData = new MetaData();
        metaData._ordering = new RelativeOrdering(metaData);
        
        //Example from Spec p. 71-72
        
        //A: after B
        TestResource jar1 = new TestResource("A");
        resources.add(jar1);
        TestResource r1 = new TestResource("A/web-fragment.xml");
        FragmentDescriptor f1 = new FragmentDescriptor(r1);
        f1._name = "A";
        metaData._webFragmentNameMap.put(f1._name, f1);
        metaData._webFragmentResourceMap.put(jar1, f1);
        //((RelativeOrdering)metaData._ordering).addNoOthers(f1);
        f1._otherType = FragmentDescriptor.OtherType.None;
        f1._afters.add("B");
        
        //B: no order
        TestResource jar2 = new TestResource("B");
        resources.add(jar2);
        TestResource r2 = new TestResource("B/web-fragment.xml");
        FragmentDescriptor f2 = new FragmentDescriptor(r2);
        f2._name="B";
        metaData._webFragmentNameMap.put(f2._name, f2);
        metaData._webFragmentResourceMap.put(jar2, f2);
        //((RelativeOrdering)metaData._ordering).addNoOthers(f2);
        f2._otherType = FragmentDescriptor.OtherType.None;
        
        //C: before others
        TestResource jar3 = new TestResource("C");
        resources.add(jar3);
        TestResource r3 = new TestResource("C/web-fragment.xml");
        FragmentDescriptor f3 = new FragmentDescriptor(r3);
        f3._name="C";
        metaData._webFragmentNameMap.put(f3._name, f3);
        metaData._webFragmentResourceMap.put(jar3,f3);
        //((RelativeOrdering)metaData._ordering).addBeforeOthers(f3);
        f3._otherType = FragmentDescriptor.OtherType.Before;

        //D: no order
        TestResource jar4 = new TestResource("D");
        resources.add(jar4);
        TestResource r4 = new TestResource("D/web-fragment.xml");
        FragmentDescriptor f4 = new FragmentDescriptor(r4);
        f4._name="D"; 
        metaData._webFragmentNameMap.put(f4._name, f4);
        metaData._webFragmentResourceMap.put(jar4, f4);
        //((RelativeOrdering)metaData._ordering).addNoOthers(f4);
        f4._otherType = FragmentDescriptor.OtherType.None;
        //
        // p.71-72 possible outcomes are:
        // C,B,D,A
        // C,D,B,A
        // C,B,A,D
        //
        String[] outcomes = {"CBDA",
                             "CDBA",
                             "CBAD"};
       
 
        List<Resource> orderedList = metaData._ordering.order(resources);
        String result = "";
        for (Resource r:orderedList)
           result+=(((TestResource)r)._name);
        
        if (!checkResult(result, outcomes))
            fail ("No outcome matched "+result);
    }
    
    @Test
    public void testRelativeOrdering3 ()
    throws Exception
    { 
        List<Resource> resources = new ArrayList<Resource>();
        WebAppContext wac = new WebAppContext();
        MetaData metaData = new MetaData();
        metaData._ordering = new RelativeOrdering(metaData);

        //A: after others, before C
        TestResource jar1 = new TestResource("A");
        resources.add(jar1);
        TestResource r1 = new TestResource("A/web-fragment.xml");
        FragmentDescriptor f1 = new FragmentDescriptor(r1);
        f1._name = "A";
        metaData._webFragmentNameMap.put(f1._name, f1);
        metaData._webFragmentResourceMap.put(jar1, f1);
        //((RelativeOrdering)metaData._ordering).addAfterOthers(f1);
        f1._otherType = FragmentDescriptor.OtherType.After;
        f1._befores.add("C");

        //B: before others, before C
        TestResource jar2 = new TestResource("B");
        resources.add(jar2);
        TestResource r2 = new TestResource("B/web-fragment.xml");
        FragmentDescriptor f2 = new FragmentDescriptor(r2);
        f2._name="B";
        metaData._webFragmentNameMap.put(f2._name, f2);
        metaData._webFragmentResourceMap.put(jar2,f2);
        //((RelativeOrdering)metaData._ordering).addBeforeOthers(f2);
        f2._otherType = FragmentDescriptor.OtherType.Before;
        f2._befores.add("C");

        //C: no ordering   
        TestResource jar3 = new TestResource("C");
        resources.add(jar3);
        TestResource r3 = new TestResource("C/web-fragment.xml");
        FragmentDescriptor f3 = new FragmentDescriptor(r3);
        f3._name="C";
        metaData._webFragmentNameMap.put(f3._name, f3);
        metaData._webFragmentResourceMap.put(jar3,f3);
        //((RelativeOrdering)metaData._ordering).addNoOthers(f3);
        f3._otherType = FragmentDescriptor.OtherType.None;
        
        //result: BAC
        String[] outcomes = {"BAC"};
        
        List<Resource> orderedList = metaData._ordering.order(resources);
        String result = "";
        for (Resource r:orderedList)
           result+=(((TestResource)r)._name);
        
        if (!checkResult(result, outcomes))
            fail ("No outcome matched "+result);
    }
    
    @Test
    public void testCircular1 ()
    throws Exception
    {
        
        //A: after B
        //B: after A
        List<Resource> resources = new ArrayList<Resource>();
        WebAppContext wac = new WebAppContext();
        MetaData metaData = new MetaData();
        metaData._ordering = new RelativeOrdering(metaData);
        
        //A: after B
        TestResource jar1 = new TestResource("A");
        resources.add(jar1);
        TestResource r1 = new TestResource("A/web-fragment.xml");
        FragmentDescriptor f1 = new FragmentDescriptor(r1);
        f1._name = "A";
        metaData._webFragmentNameMap.put(f1._name, f1);
        metaData._webFragmentResourceMap.put(jar1, f1);
        //((RelativeOrdering)metaData._ordering).addNoOthers(f1);
        f1._otherType = FragmentDescriptor.OtherType.None;
        f1._afters.add("B");
        
        //B: after A
        TestResource jar2 = new TestResource("B");
        resources.add(jar2);
        TestResource r2 = new TestResource("B/web-fragment.xml");
        FragmentDescriptor f2 = new FragmentDescriptor(r2);
        f2._name="B";
        metaData._webFragmentNameMap.put(f2._name, f2);
        metaData._webFragmentResourceMap.put(jar2, f2);
        //((RelativeOrdering)metaData._ordering).addNoOthers(f2);
        f2._otherType = FragmentDescriptor.OtherType.None;
        f2._afters.add("A");

        try
        {
            List<Resource> orderedList = metaData._ordering.order(resources);
            fail("No circularity detected");
        }
        catch (Exception e)
        {
            assertTrue (e instanceof IllegalStateException);
        }
    }
 
    
    
    @Test
    public void testInvalid1 ()
    throws Exception
    {      
        List<Resource> resources = new ArrayList<Resource>();
        WebAppContext wac = new WebAppContext();
        MetaData metaData = new MetaData();
        metaData._ordering = new RelativeOrdering(metaData);
        
        //A: after others, before C
        TestResource jar1 = new TestResource("A");
        resources.add(jar1);
        TestResource r1 = new TestResource("A/web-fragment.xml");
        FragmentDescriptor f1 = new FragmentDescriptor(r1);
        f1._name = "A";
        metaData._webFragmentNameMap.put(f1._name, f1);
        metaData._webFragmentResourceMap.put(jar1,f1);
        //((RelativeOrdering)metaData._ordering).addAfterOthers(r1);
        f1._otherType = FragmentDescriptor.OtherType.After;
        f1._befores.add("C");
        
        //B: before others, after C
        TestResource jar2 = new TestResource("B");
        resources.add(jar2);
        TestResource r2 = new TestResource("B/web-fragment.xml");
        FragmentDescriptor f2 = new FragmentDescriptor(r2);
        f2._name="B";
        metaData._webFragmentNameMap.put(f2._name, f2);
        metaData._webFragmentResourceMap.put(jar2,f2);
        //((RelativeOrdering)metaData._ordering).addBeforeOthers(r2);
        f2._otherType = FragmentDescriptor.OtherType.Before;
        f2._afters.add("C");
        
        //C: no ordering
        TestResource jar3 = new TestResource("C");
        resources.add(jar3);
        TestResource r3 = new TestResource("C/web-fragment.xml");
        FragmentDescriptor f3 = new FragmentDescriptor(r3);
        f3._name="C";
        metaData._webFragmentNameMap.put(f3._name, f3);
        metaData._webFragmentResourceMap.put(jar3,f3);
        //((RelativeOrdering)metaData._ordering).addNoOthers(r3);
        f3._otherType = FragmentDescriptor.OtherType.None;
        
        try
        {
            List<Resource> orderedList = metaData._ordering.order(resources);
            String result = "";
            for (Resource r:orderedList)
                result +=((TestResource)r)._name;
            System.err.println("Invalid Result = "+result);
            fail("A and B have an impossible relationship to C");
        }
        catch (Exception e)
        {
            assertTrue (e instanceof IllegalStateException);
        }
    }

    @Test
    public void testAbsoluteOrdering1 ()
    throws Exception
    {
        //
        // A,B,C,others
        //
        List<Resource> resources = new ArrayList<Resource>();
        WebAppContext wac = new WebAppContext();
        MetaData metaData = new MetaData();
        metaData._ordering = new AbsoluteOrdering(metaData);
        ((AbsoluteOrdering)metaData._ordering).add("A");
        ((AbsoluteOrdering)metaData._ordering).add("B");
        ((AbsoluteOrdering)metaData._ordering).add("C");
        ((AbsoluteOrdering)metaData._ordering).addOthers();
        
        TestResource jar1 = new TestResource("A");
        resources.add(jar1);
        TestResource r1 = new TestResource("A/web-fragment.xml");
        FragmentDescriptor f1 = new FragmentDescriptor(r1);
        f1._name = "A";
        metaData._webFragmentNameMap.put(f1._name, f1);
        metaData._webFragmentResourceMap.put(jar1,f1);
        
        TestResource jar2 = new TestResource("B");
        resources.add(jar2);
        TestResource r2 = new TestResource("B/web-fragment.xml");
        FragmentDescriptor f2 = new FragmentDescriptor(r2);
        f2._name="B";
        metaData._webFragmentNameMap.put(f2._name, f2);
        metaData._webFragmentResourceMap.put(jar2, f2);
        
        TestResource jar3 = new TestResource("C");
        resources.add(jar3);
        TestResource r3 = new TestResource("C/web-fragment.xml");
        FragmentDescriptor f3 = new FragmentDescriptor(r3);
        f3._name="C";
        metaData._webFragmentNameMap.put(f3._name, f3);
        metaData._webFragmentResourceMap.put(jar3, f3);
        
        TestResource jar4 = new TestResource("D");
        resources.add(jar4);
        TestResource r4 = new TestResource("D/web-fragment.xml");
        FragmentDescriptor f4 = new FragmentDescriptor((Resource)null);
        f4._name="D"; 
        metaData._webFragmentNameMap.put(f4._name, f4);
        metaData._webFragmentResourceMap.put(jar4, f4);
        
        TestResource jar5 = new TestResource("E");
        resources.add(jar5);
        TestResource r5 = new TestResource("E/web-fragment.xml");
        FragmentDescriptor f5 = new FragmentDescriptor((Resource)null);
        f5._name="E";
        metaData._webFragmentNameMap.put(f5._name, f5);
        metaData._webFragmentResourceMap.put(jar5, f5);
        
        TestResource jar6 = new TestResource("plain");
        resources.add(jar6);
        TestResource r6 = new TestResource ("plain/web-fragment.xml");
        FragmentDescriptor f6 = new FragmentDescriptor((Resource)null);
        f6._name=FragmentDescriptor.NAMELESS+"1";
        metaData._webFragmentNameMap.put(f6._name, f6);
        metaData._webFragmentResourceMap.put(jar6, f6);
        
        List<Resource> list = metaData._ordering.order(resources);
        
        String[] outcomes = {"ABCDEplain"};
        String result = "";
        for (Resource r:list)
            result += ((TestResource)r)._name;
        
        if (!checkResult(result, outcomes))
            fail("No outcome matched "+result);
    }
   
    @Test
    public void testAbsoluteOrdering2 ()
    throws Exception
    {
        // C,B,A 
        List<Resource> resources = new ArrayList<Resource>();
        
        WebAppContext wac = new WebAppContext();
        MetaData metaData = new MetaData();
        metaData._ordering = new AbsoluteOrdering(metaData);
        ((AbsoluteOrdering)metaData._ordering).add("C");
        ((AbsoluteOrdering)metaData._ordering).add("B");
        ((AbsoluteOrdering)metaData._ordering).add("A");
        
        TestResource jar1 = new TestResource("A");
        resources.add(jar1);
        TestResource r1 = new TestResource("A/web-fragment.xml");
        FragmentDescriptor f1 = new FragmentDescriptor(r1);
        f1._name = "A";
        metaData._webFragmentNameMap.put(f1._name, f1);
        metaData._webFragmentResourceMap.put(jar1,f1);
        
        TestResource jar2 = new TestResource("B");  
        resources.add(jar2);
        TestResource r2 = new TestResource("B/web-fragment.xml");
        FragmentDescriptor f2 = new FragmentDescriptor(r2);
        f2._name="B";
        metaData._webFragmentNameMap.put(f2._name, f2);
        metaData._webFragmentResourceMap.put(jar2,f2);
        
        TestResource jar3 = new TestResource("C");
        resources.add(jar3);
        TestResource r3 = new TestResource("C/web-fragment.xml");
        FragmentDescriptor f3 = new FragmentDescriptor(r3);
        f3._name="C";
        metaData._webFragmentNameMap.put(f3._name, f3);
        metaData._webFragmentResourceMap.put(jar3,f3);
        
        TestResource jar4 = new TestResource("D");
        resources.add(jar4);
        TestResource r4 = new TestResource("D/web-fragment.xml");
        FragmentDescriptor f4 = new FragmentDescriptor(r4);
        f4._name="D"; 
        metaData._webFragmentNameMap.put(f4._name, f4);
        metaData._webFragmentResourceMap.put(jar4,f4);
        
        TestResource jar5 = new TestResource("E");
        resources.add(jar5);
        TestResource r5 = new TestResource("E/web-fragment.xml");
        FragmentDescriptor f5 = new FragmentDescriptor(r5);
        f5._name="E";
        metaData._webFragmentNameMap.put(f5._name, f5);
        metaData._webFragmentResourceMap.put(jar5,f5);
        
        TestResource jar6 = new TestResource("plain");
        resources.add(jar6);
        TestResource r6 = new TestResource("plain/web-fragment.xml");
        FragmentDescriptor f6 = new FragmentDescriptor(r6);
        f6._name=FragmentDescriptor.NAMELESS+"1";
        metaData._webFragmentNameMap.put(f6._name, f6);
        metaData._webFragmentResourceMap.put(jar6,f6);
        
        List<Resource> list = metaData._ordering.order(resources);
        String[] outcomes = {"CBA"};
        String result = "";
        for (Resource r:list)
            result += ((TestResource)r)._name;
        
        
        if (!checkResult(result, outcomes))
            fail("No outcome matched "+result);
    }
    
    @Test
    public void testAbsoluteOrdering3 ()
    throws Exception
    {
        //empty <absolute-ordering>
        
        WebAppContext wac = new WebAppContext();
        MetaData metaData = new MetaData();
        metaData._ordering = new AbsoluteOrdering(metaData);
        List<Resource> resources = new ArrayList<Resource>();
        
        resources.add(new TestResource("A"));
        resources.add(new TestResource("B"));
        
        List<Resource> list = metaData._ordering.order(resources);
        assertTrue(list.isEmpty());
    }
    
    @Test
    public void testRelativeOrderingWithPlainJars ()
    throws Exception
    {
        //B,A,C other jars with no fragments
        List<Resource> resources = new ArrayList<Resource>();
        WebAppContext wac = new WebAppContext();
        MetaData metaData = new MetaData();
        metaData._ordering = new RelativeOrdering(metaData);

        //A: after others, before C
        TestResource jar1 = new TestResource("A");
        resources.add(jar1);
        TestResource r1 = new TestResource("A/web-fragment.xml");
        FragmentDescriptor f1 = new FragmentDescriptor(r1);
        f1._name = "A";
        metaData._webFragmentNameMap.put(f1._name, f1);
        metaData._webFragmentResourceMap.put(jar1, f1);
        //((RelativeOrdering)metaData._ordering).addAfterOthers(f1);
        f1._otherType = FragmentDescriptor.OtherType.After;
        f1._befores.add("C");

        //B: before others, before C
        TestResource jar2 = new TestResource("B");
        resources.add(jar2);
        TestResource r2 = new TestResource("B/web-fragment.xml");
        FragmentDescriptor f2 = new FragmentDescriptor(r2);
        f2._name="B";
        metaData._webFragmentNameMap.put(f2._name, f2);
        metaData._webFragmentResourceMap.put(jar2,f2);
        //((RelativeOrdering)metaData._ordering).addBeforeOthers(f2);
        f2._otherType = FragmentDescriptor.OtherType.Before;
        f2._befores.add("C");

        //C: after A   
        TestResource jar3 = new TestResource("C");
        resources.add(jar3);
        TestResource r3 = new TestResource("C/web-fragment.xml");
        FragmentDescriptor f3 = new FragmentDescriptor(r3);
        f3._name="C";
        metaData._webFragmentNameMap.put(f3._name, f3);
        metaData._webFragmentResourceMap.put(jar3,f3);
        //((RelativeOrdering)metaData._ordering).addNoOthers(f3);
        f3._otherType = FragmentDescriptor.OtherType.None;
        f3._afters.add("A");
        
        //No fragment jar 1
        TestResource r4 = new TestResource("plain1");
        resources.add(r4);
        
        //No fragment jar 2
        TestResource r5 = new TestResource("plain2");
        resources.add(r5);
        
        //result: BAC
        String[] outcomes = {"Bplain1plain2AC"};

        List<Resource> orderedList = metaData._ordering.order(resources);
        String result = "";
        for (Resource r:orderedList)
           result+=(((TestResource)r)._name);
        
        if (!checkResult(result, outcomes))
            fail ("No outcome matched "+result);
    }
    
    @Test
    public void testRelativeOrderingWithPlainJars2 ()
    throws Exception
    {
        //web.xml has no ordering, jar A has fragment after others, jar B is plain, jar C is plain
        List<Resource> resources = new ArrayList<Resource>();
        WebAppContext wac = new WebAppContext();
        MetaData metaData = new MetaData();
        metaData._ordering = new RelativeOrdering(metaData);
        
        //A has after others
        TestResource jar1 = new TestResource("A");
        resources.add(jar1);
        TestResource r1 = new TestResource("A/web-fragment.xml");
        FragmentDescriptor f1 = new FragmentDescriptor(r1);
        f1._name = "A";
        metaData._webFragmentNameMap.put(f1._name, f1);
        metaData._webFragmentResourceMap.put(jar1, f1);
        f1._otherType = FragmentDescriptor.OtherType.After;
        
        //No fragment jar B
        TestResource r4 = new TestResource("plainB");
        resources.add(r4);
        
        //No fragment jar C
        TestResource r5 = new TestResource("plainC");
        resources.add(r5);
        
        List<Resource> orderedList = metaData._ordering.order(resources);
        String[] outcomes = {"plainBplainCA"};
        String result = "";
        for (Resource r:orderedList)
           result+=(((TestResource)r)._name);
        
        if (!checkResult(result, outcomes))
            fail ("No outcome matched "+result);
    }
    
    @Test
    public void testAbsoluteOrderingWithPlainJars()
    throws Exception
    {
        //
        // A,B,C,others
        //
        List<Resource> resources = new ArrayList<Resource>();
        WebAppContext wac = new WebAppContext();
        MetaData metaData = new MetaData();
        metaData._ordering = new AbsoluteOrdering(metaData);
        ((AbsoluteOrdering)metaData._ordering).add("A");
        ((AbsoluteOrdering)metaData._ordering).add("B");
        ((AbsoluteOrdering)metaData._ordering).add("C");
        ((AbsoluteOrdering)metaData._ordering).addOthers();
        
        TestResource jar1 = new TestResource("A");
        resources.add(jar1);
        TestResource r1 = new TestResource("A/web-fragment.xml");
        FragmentDescriptor f1 = new FragmentDescriptor(r1);
        f1._name = "A";
        metaData._webFragmentNameMap.put(f1._name, f1);
        metaData._webFragmentResourceMap.put(jar1,f1);
        
        TestResource jar2 = new TestResource("B");
        resources.add(jar2);
        TestResource r2 = new TestResource("B/web-fragment.xml");
        FragmentDescriptor f2 = new FragmentDescriptor(r2);
        f2._name="B";
        metaData._webFragmentNameMap.put(f2._name, f2);
        metaData._webFragmentResourceMap.put(jar2, f2);
        
        TestResource jar3 = new TestResource("C");
        resources.add(jar3);
        TestResource r3 = new TestResource("C/web-fragment.xml");
        FragmentDescriptor f3 = new FragmentDescriptor(r3);
        f3._name="C";
        metaData._webFragmentNameMap.put(f3._name, f3);
        metaData._webFragmentResourceMap.put(jar3, f3);
        
        TestResource jar4 = new TestResource("D");
        resources.add(jar4);
        TestResource r4 = new TestResource("D/web-fragment.xml");
        FragmentDescriptor f4 = new FragmentDescriptor((Resource)null);
        f4._name="D"; 
        metaData._webFragmentNameMap.put(f4._name, f4);
        metaData._webFragmentResourceMap.put(jar4, f4);
        
        TestResource jar5 = new TestResource("E");
        resources.add(jar5);
        TestResource r5 = new TestResource("E/web-fragment.xml");
        FragmentDescriptor f5 = new FragmentDescriptor((Resource)null);
        f5._name="E";
        metaData._webFragmentNameMap.put(f5._name, f5);
        metaData._webFragmentResourceMap.put(jar5, f5);
        
        TestResource jar6 = new TestResource("plain");
        resources.add(jar6);
        TestResource r6 = new TestResource("plain/web-fragment.xml");
        FragmentDescriptor f6 = new FragmentDescriptor((Resource)null);
        f6._name=FragmentDescriptor.NAMELESS+"1";
        metaData._webFragmentNameMap.put(f6._name, f6);
        metaData._webFragmentResourceMap.put(jar6, f6);
        
        //plain jar
        TestResource r7 = new TestResource("plain1");
        resources.add(r7);
        
        TestResource r8 = new TestResource("plain2");
        resources.add(r8);
        
        List<Resource> list = metaData._ordering.order(resources);
        
        String[] outcomes = {"ABCDEplainplain1plain2"};
        String result = "";
        for (Resource r:list)
            result += ((TestResource)r)._name;
        
        if (!checkResult(result, outcomes))
            fail("No outcome matched "+result);
    }
    
    
    
    public boolean checkResult (String result, String[] outcomes)
    {
        boolean matched = false;
        for (String s:outcomes)
        {
            if (s.equals(result))
                matched = true;
        }
        return matched;
    }
}
