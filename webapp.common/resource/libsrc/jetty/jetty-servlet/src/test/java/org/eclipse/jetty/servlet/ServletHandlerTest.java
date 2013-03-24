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

package org.eclipse.jetty.servlet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.EnumSet;

import javax.servlet.DispatcherType;

import org.eclipse.jetty.servlet.Holder.Source;
import org.junit.Before;
import org.junit.Test;

public class ServletHandlerTest
{
    FilterHolder fh1 = new FilterHolder(Holder.Source.DESCRIPTOR);
    FilterMapping fm1 = new FilterMapping();
    
    FilterHolder fh2 = new FilterHolder(Holder.Source.DESCRIPTOR);
    FilterMapping fm2 = new FilterMapping();
    
    FilterHolder fh3 = new FilterHolder(Holder.Source.JAVAX_API);
    FilterMapping fm3 = new FilterMapping();
    
    FilterHolder fh4 = new FilterHolder(Holder.Source.JAVAX_API);
    FilterMapping fm4 = new FilterMapping();
    
    FilterHolder fh5 = new FilterHolder(Holder.Source.JAVAX_API);
    FilterMapping fm5 = new FilterMapping();
    
    

    @Before
    public void initMappings()
    {
        fh1.setName("fh1");
        fm1.setPathSpec("/*");
        fm1.setFilterHolder(fh1);

        fh2.setName("fh2");
        fm2.setPathSpec("/*");
        fm2.setFilterHolder(fh2);
        
        fh3.setName("fh3");
        fm3.setPathSpec("/*");
        fm3.setFilterHolder(fh3);

        fh4.setName("fh4");
        fm4.setPathSpec("/*");
        fm4.setFilterHolder(fh4);
        
        fh5.setName("fh5");
        fm5.setPathSpec("/*");
        fm5.setFilterHolder(fh5);
    }

    @Test
    public void testAllNonProgrammaticFilterMappings() throws Exception
    {
        ServletHandler handler = new ServletHandler(); 
        handler.addFilter(fh1);
        handler.addFilter(fh2);
        
        //add some ordinary filter mappings
        handler.addFilterMapping(fm1);
        handler.addFilterMapping(fm2);
        
        FilterMapping[] mappings = handler.getFilterMappings();
        assertNotNull(mappings);
        assertTrue(fm1 == mappings[0]);
        assertTrue(fm2 == mappings[1]);
        
        //add another ordinary mapping
        FilterHolder of1 = new FilterHolder(Source.DESCRIPTOR);
        FilterMapping ofm1 = new FilterMapping();
        ofm1.setFilterHolder(of1);
        ofm1.setPathSpec("/*");
        handler.addFilter(of1);
        handler.addFilterMapping(ofm1);
        
        mappings = handler.getFilterMappings();
        assertNotNull(mappings);
        assertTrue(fm1 == mappings[0]);
        assertTrue(fm2 == mappings[1]);
        assertTrue(ofm1 == mappings[2]);
    }
    
    
    
    @Test
    public void testAllBeforeFilterMappings() throws Exception
    {
        ServletHandler handler = new ServletHandler(); 
        
        //do equivalent of FilterRegistration.addMappingForUrlPatterns(isMatchAfter=false)
        handler.addFilter(fh4);
        handler.prependFilterMapping(fm4);
        
        FilterMapping[] mappings = handler.getFilterMappings();
        assertNotNull(mappings);
        assertEquals(1, mappings.length);
        
        //add another with isMatchAfter=false
        handler.addFilter(fh5);
        handler.prependFilterMapping(fm5);
        
        mappings = handler.getFilterMappings();
        assertNotNull(mappings);
        assertEquals(2, mappings.length);
        
        assertTrue(fm4 == mappings[0]);
        assertTrue(fm5 == mappings[1]);
    }
    
    @Test
    public void testAllAfterFilterMappings() throws Exception
    {
        ServletHandler handler = new ServletHandler(); 
        //do equivalent of FilterRegistration.addMappingForUrlPatterns(isMatchAfter=true)
        handler.addFilter(fh4);
        handler.addFilterMapping(fm4);
        FilterMapping[] mappings = handler.getFilterMappings();
        assertEquals(1, mappings.length);
        assertTrue(fm4 == mappings[0]);
        
        //do equivalent of FilterRegistration.addMappingForUrlPatterns(isMatchAfter=true)
        handler.addFilter(fh5);
        handler.addFilterMapping(fm5);
        mappings = handler.getFilterMappings();
        assertEquals(2, mappings.length);
        assertTrue(fm4 == mappings[0]);
        assertTrue(fm5 == mappings[1]);
    }
    
    
    @Test
    public void testMatchAfterAndBefore() throws Exception
    {
        ServletHandler handler = new ServletHandler();
        
        //add a programmatic one, isMatchAfter=true
        handler.addFilter(fh3);
        handler.addFilterMapping(fm3);
        FilterMapping[]  mappings = handler.getFilterMappings();
        assertNotNull(mappings);
        assertEquals(1, mappings.length);
        assertTrue(fm3 == mappings[0]);
        
        //add a programmatic one, isMatchAfter=false
        handler.addFilter(fh4);
        handler.prependFilterMapping(fm4);
        mappings = handler.getFilterMappings();
        assertNotNull(mappings);
        assertEquals(2, mappings.length);
        assertTrue(fm4 == mappings[0]);
        assertTrue(fm3 == mappings[1]);  
    }
    
    
    @Test
    public void testMatchBeforeAndAfter() throws Exception
    {
        ServletHandler handler = new ServletHandler();
        
        //add a programmatic one, isMatchAfter=false
        handler.addFilter(fh3);
        handler.prependFilterMapping(fm3);
        FilterMapping[]  mappings = handler.getFilterMappings();
        assertNotNull(mappings);
        assertEquals(1, mappings.length);
        assertTrue(fm3 == mappings[0]);
        
        //add a programmatic one, isMatchAfter=true
        handler.addFilter(fh4);
        handler.addFilterMapping(fm4);
        mappings = handler.getFilterMappings();
        assertNotNull(mappings);
        assertEquals(2, mappings.length);
        assertTrue(fm3 == mappings[0]); 
        assertTrue(fm4 == mappings[1]);
    }
    
    
    @Test
    public void testExistingFilterMappings() throws Exception
    {
        ServletHandler handler = new ServletHandler();
        handler.addFilter(fh1);
        handler.addFilter(fh2);
        
        //add some ordinary filter mappings first
        handler.addFilterMapping(fm1);
        handler.addFilterMapping(fm2);
        
        FilterMapping[] mappings = handler.getFilterMappings();
        assertNotNull(mappings);
        assertTrue(fm1 == mappings[0]);
        assertTrue(fm2 == mappings[1]);
        
        //do equivalent of FilterRegistration.addMappingForUrlPatterns(isMatchAfter=false)
        handler.addFilter(fh4);
        handler.prependFilterMapping(fm4);
        mappings = handler.getFilterMappings();
        assertEquals(3, mappings.length);
        assertTrue(fm4 == mappings[0]);
        
        //do equivalent of FilterRegistration.addMappingForUrlPatterns(isMatchAfter=true)
        handler.addFilter(fh5);
        handler.addFilterMapping(fm5);
        mappings = handler.getFilterMappings();
        assertEquals(4, mappings.length);
        assertTrue(fm5 == mappings[mappings.length-1]);
    }
    
    @Test
    public void testFilterMappingsMix() throws Exception
    {
        ServletHandler handler = new ServletHandler();

        //add a non-programmatic one to begin with
        handler.addFilter(fh1);
        handler.addFilterMapping(fm1); 
        FilterMapping[] mappings = handler.getFilterMappings();
        assertNotNull(mappings);
        assertTrue(fm1 == mappings[0]);

        //add a programmatic one, isMatchAfter=false
        handler.addFilter(fh4);
        handler.prependFilterMapping(fm4);
        mappings = handler.getFilterMappings();
        assertNotNull(mappings);
        assertEquals(2, mappings.length);
        assertTrue(fm4 == mappings[0]);
        assertTrue(fm1 == mappings[1]);
        
        //add a programmatic one, isMatchAfter=true
        handler.addFilter(fh3);
        handler.addFilterMapping(fm3);
        mappings = handler.getFilterMappings();
        assertNotNull(mappings);
        assertEquals(3, mappings.length);
        assertTrue(fm4 == mappings[0]);
        assertTrue(fm1 == mappings[1]);
        assertTrue(fm3 == mappings[2]);
        
        //add a programmatic one, isMatchAfter=false
        handler.addFilter(fh5);
        handler.prependFilterMapping(fm5);
        mappings = handler.getFilterMappings();
        assertNotNull(mappings);
        assertEquals(4, mappings.length);
        assertTrue(fm4 == mappings[0]);//isMatchAfter = false;
        assertTrue(fm5 == mappings[1]);//isMatchAfter = false;
        assertTrue(fm1 == mappings[2]);//ordinary
        assertTrue(fm3 == mappings[3]);//isMatchAfter = true;
           
        //add a non-programmatic one
        FilterHolder f = new FilterHolder(Source.EMBEDDED);
        f.setName("non-programmatic");
        FilterMapping fm = new FilterMapping();
        fm.setFilterHolder(f);
        fm.setPathSpec("/*");
        handler.addFilter(f);
        handler.addFilterMapping(fm);
        mappings = handler.getFilterMappings();
        assertNotNull(mappings);
        assertEquals(5, mappings.length);
        assertTrue(fm4 == mappings[0]); //isMatchAfter = false;
        assertTrue(fm5 == mappings[1]); //isMatchAfter = false;
        assertTrue(fm1 == mappings[2]); //ordinary
        assertTrue(fm == mappings[3]);  //ordinary
        assertTrue(fm3 == mappings[4]); //isMatchAfter = true;
        
        //add a programmatic one, isMatchAfter=true
        FilterHolder pf = new FilterHolder(Source.JAVAX_API);
        pf.setName("programmaticA");
        FilterMapping pfm = new FilterMapping();
        pfm.setFilterHolder(pf);
        pfm.setPathSpec("/*");
        handler.addFilter(pf);
        handler.addFilterMapping(pfm);
        mappings = handler.getFilterMappings();
        assertNotNull(mappings);
        assertEquals(6, mappings.length);
        assertTrue(fm4 == mappings[0]); //isMatchAfter = false;
        assertTrue(fm5 == mappings[1]); //isMatchAfter = false;
        assertTrue(fm1 == mappings[2]); //ordinary
        assertTrue(fm == mappings[3]);  //ordinary
        assertTrue(fm3 == mappings[4]); //isMatchAfter = true;
        assertTrue(pfm == mappings[5]); //isMatchAfter = true;
        
        //add a programmatic one, isMatchAfter=false
        FilterHolder pf2 = new FilterHolder(Source.JAVAX_API);
        pf2.setName("programmaticB");
        FilterMapping pfm2 = new FilterMapping();
        pfm2.setFilterHolder(pf2);
        pfm2.setPathSpec("/*");
        handler.addFilter(pf2);
        handler.prependFilterMapping(pfm2);
        mappings = handler.getFilterMappings();
        assertNotNull(mappings);
        assertEquals(7, mappings.length);
        assertTrue(fm4 == mappings[0]); //isMatchAfter = false;
        assertTrue(fm5 == mappings[1]); //isMatchAfter = false;
        assertTrue(pfm2 == mappings[2]);//isMatchAfter = false;
        assertTrue(fm1 == mappings[3]); //ordinary
        assertTrue(fm == mappings[4]);  //ordinary
        assertTrue(fm3 == mappings[5]); //isMatchAfter = true;
        assertTrue(pfm == mappings[6]); //isMatchAfter = true;
    }
  
    @Test
    public void testAddFilterWithMappingAPI() throws Exception
    {
        ServletHandler handler = new ServletHandler();

        //add a non-programmatic one to begin with
        handler.addFilterWithMapping(fh1, "/*", EnumSet.allOf(DispatcherType.class));           
        FilterMapping[] mappings = handler.getFilterMappings();
        assertNotNull(mappings);
        assertTrue(fh1 == mappings[0].getFilterHolder());

        //add a programmatic one, isMatchAfter=false
        fh4.setServletHandler(handler);
        handler.addFilter(fh4);
        fh4.getRegistration().addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), false, "/*");
        mappings = handler.getFilterMappings();
        assertNotNull(mappings);
        assertEquals(2, mappings.length);
        assertTrue(fh4 == mappings[0].getFilterHolder());
        assertTrue(fh1 == mappings[1].getFilterHolder());
        
        //add a programmatic one, isMatchAfter=true
        fh3.setServletHandler(handler);
        handler.addFilter(fh3);
        fh3.getRegistration().addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");
        mappings = handler.getFilterMappings();
        assertNotNull(mappings);
        assertEquals(3, mappings.length);
        assertTrue(fh4 == mappings[0].getFilterHolder());
        assertTrue(fh1 == mappings[1].getFilterHolder());
        assertTrue(fh3 == mappings[2].getFilterHolder());
        
        //add a programmatic one, isMatchAfter=false
        fh5.setServletHandler(handler);
        handler.addFilter(fh5);
        fh5.getRegistration().addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), false, "/*");
        mappings = handler.getFilterMappings();
        assertNotNull(mappings);
        assertEquals(4, mappings.length);
        assertTrue(fh4 == mappings[0].getFilterHolder());//isMatchAfter = false;
        assertTrue(fh5 == mappings[1].getFilterHolder());//isMatchAfter = false;
        assertTrue(fh1 == mappings[2].getFilterHolder());//ordinary
        assertTrue(fh3 == mappings[3].getFilterHolder());//isMatchAfter = true;
           
        //add a non-programmatic one
        FilterHolder f = new FilterHolder(Source.EMBEDDED);
        f.setName("non-programmatic");     
        handler.addFilterWithMapping(f, "/*", EnumSet.allOf(DispatcherType.class));
        mappings = handler.getFilterMappings();
        assertNotNull(mappings);
        assertEquals(5, mappings.length);
        assertTrue(fh4 == mappings[0].getFilterHolder()); //isMatchAfter = false;
        assertTrue(fh5 == mappings[1].getFilterHolder()); //isMatchAfter = false;
        assertTrue(fh1 == mappings[2].getFilterHolder()); //ordinary
        assertTrue(f == mappings[3].getFilterHolder());  //ordinary
        assertTrue(fh3 == mappings[4].getFilterHolder()); //isMatchAfter = true;
        
        //add a programmatic one, isMatchAfter=true
        FilterHolder pf = new FilterHolder(Source.JAVAX_API);
        pf.setServletHandler(handler);
        pf.setName("programmaticA");
        handler.addFilter(pf);
        pf.getRegistration().addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");
        mappings = handler.getFilterMappings();
        assertNotNull(mappings);
        assertEquals(6, mappings.length);
        assertTrue(fh4 == mappings[0].getFilterHolder()); //isMatchAfter = false;
        assertTrue(fh5 == mappings[1].getFilterHolder()); //isMatchAfter = false;
        assertTrue(fh1 == mappings[2].getFilterHolder()); //ordinary
        assertTrue(f == mappings[3].getFilterHolder());  //ordinary
        assertTrue(fh3 == mappings[4].getFilterHolder()); //isMatchAfter = true;
        assertTrue(pf == mappings[5].getFilterHolder()); //isMatchAfter = true;
        
        //add a programmatic one, isMatchAfter=false
        FilterHolder pf2 = new FilterHolder(Source.JAVAX_API);
        pf2.setServletHandler(handler);
        pf2.setName("programmaticB");
        handler.addFilter(pf2);
        pf2.getRegistration().addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), false, "/*");
        mappings = handler.getFilterMappings();

        assertNotNull(mappings);
        assertEquals(7, mappings.length);
        assertTrue(fh4 == mappings[0].getFilterHolder()); //isMatchAfter = false;
        assertTrue(fh5 == mappings[1].getFilterHolder()); //isMatchAfter = false;
        assertTrue(pf2 == mappings[2].getFilterHolder());//isMatchAfter = false;
        assertTrue(fh1 == mappings[3].getFilterHolder()); //ordinary
        assertTrue(f == mappings[4].getFilterHolder());  //ordinary
        assertTrue(fh3 == mappings[5].getFilterHolder()); //isMatchAfter = true;
        assertTrue(pf == mappings[6].getFilterHolder()); //isMatchAfter = true;
    }
  
    
    
}
