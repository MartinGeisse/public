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

package org.eclipse.jetty.plus.jndi;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.naming.Binding;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.Name;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.RefAddr;
import javax.naming.Reference;
import javax.naming.Referenceable;
import javax.naming.StringRefAddr;
import javax.naming.spi.ObjectFactory;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 *
 */
public class TestNamingEntries
{
    public class ScopeA
    {
        public String toString()
        {
            return this.getClass().getName()+"@"+super.hashCode();
        }
    }

    public class ScopeB extends ScopeA
    {
    }

    public static class SomeObject
    {
        private int value;
        public SomeObject (int value)
        {this.value = value;}

        public int getValue ()
        {
            return this.value;
        }
    }

    public static class SomeObjectFactory implements ObjectFactory
    {
        public SomeObjectFactory()
        {
        }

        public Object getObjectInstance(Object arg0, Name arg1, Context arg2, Hashtable arg3) throws Exception
        {
            Reference ref = (Reference)arg0;

            RefAddr refAddr = ref.get(0);
            String valueName = refAddr.getType();
            if (!valueName.equalsIgnoreCase("val"))
                throw new RuntimeException("Unrecognized refaddr type = "+valueName);

            String value = (String)refAddr.getContent();

            return new SomeObject(Integer.parseInt(value.trim()));
        }
    }

    public static class SomeOtherObject extends SomeObject implements Referenceable
    {
        public SomeOtherObject (String value)
        {
            super(Integer.parseInt(value.trim()));
        }

        public Reference getReference() throws NamingException
        {
            RefAddr refAddr = new StringRefAddr("val", String.valueOf(getValue()));
            return new Reference(SomeOtherObject.class.getName(), refAddr, SomeOtherObjectFactory.class.getName(), null);
        }
    }

    public static class SomeOtherObjectFactory implements ObjectFactory
    {
        public SomeOtherObjectFactory()
        {
        }

        public Object getObjectInstance(Object arg0, Name arg1, Context arg2, Hashtable arg3) throws Exception
        {
            Reference ref = (Reference)arg0;

            RefAddr refAddr = ref.get(0);
            String valueName = refAddr.getType();
            if (!valueName.equalsIgnoreCase("val"))
                throw new RuntimeException("Unrecognized refaddr type = "+valueName);

            String value = (String)refAddr.getContent();

            return new SomeOtherObject(value.trim());
        }
    }

    private SomeObject someObject;

    @Before
    public void init()
    {
        this.someObject = new SomeObject(4);
        
        

        
    }
    
    /** 
     * after each test we should scrape out any lingering bindings to prevent cross test pollution
     * as observed when running java 7
     * 
     * @throws Exception
     */
    @After
    public void after() throws Exception
    {
        InitialContext icontext = new InitialContext();

        NamingEnumeration<Binding> bindings = icontext.listBindings("");
        List<String> names = new ArrayList<String>();
        while (bindings.hasMore())
        {
            Binding bd = (Binding)bindings.next();
            names.add(bd.getName());
        }

        for (String name : names)
        {
            icontext.unbind(name);
        }
    }

    @Test
    public void testEnvEntryNoScope() throws Exception
    {
        EnvEntry ee = new EnvEntry("nameZ", "zstring", true);
        List list = NamingEntryUtil.lookupNamingEntries(null, EnvEntry.class);
        assertNotNull(list);
        assertFalse(list.isEmpty());
        assertEquals(1, list.size());
        Object o = list.get(0);
        assertTrue (o instanceof EnvEntry);
        EnvEntry eo = (EnvEntry)o;
        assertEquals ("nameZ", eo.getJndiName());
    }

    @Test
    public void testEnvEntryOverride() throws Exception
    {
        ScopeA scope = new ScopeA();
        EnvEntry ee = new EnvEntry (scope, "nameA", someObject, true);

        NamingEntry ne = NamingEntryUtil.lookupNamingEntry(scope, "nameA");
        assertNotNull(ne);
        assertTrue(ne instanceof EnvEntry);
        assertTrue (((EnvEntry)ne).isOverrideWebXml());

        Context scopeContext = NamingEntryUtil.getContextForScope(scope);
        assertNotNull(scopeContext);
        Context namingEntriesContext = NamingEntryUtil.getContextForNamingEntries(scope);
        assertNotNull(namingEntriesContext);
        assertEquals(someObject, scopeContext.lookup("nameA"));
    }

    @Test
    public void testEnvEntryNonOverride() throws Exception
    {
        ScopeA scope = new ScopeA();
        EnvEntry ee = new EnvEntry (scope, "nameA", someObject, false);

        NamingEntry ne = NamingEntryUtil.lookupNamingEntry(scope, "nameA");
        assertNotNull(ne);
        assertTrue(ne instanceof EnvEntry);
        assertFalse (((EnvEntry)ne).isOverrideWebXml());

        Context scopeContext = NamingEntryUtil.getContextForScope(scope);
        assertNotNull(scopeContext);
        Context namingEntriesContext = NamingEntryUtil.getContextForNamingEntries(scope);
        assertNotNull(namingEntriesContext);
        assertEquals(someObject, scopeContext.lookup("nameA"));
    }

    @Test
    public void testResource () throws Exception
    {
        InitialContext icontext = new InitialContext();

        Resource resource = new Resource (null, "resourceA/b/c", someObject);
        NamingEntry ne = NamingEntryUtil.lookupNamingEntry(null, "resourceA/b/c");
        assertNotNull(ne);
        assertTrue(ne instanceof Resource);
        assertEquals(icontext.lookup("resourceA/b/c"), someObject);

        Object scope = new ScopeA();
        Resource resource2 = new Resource (scope, "resourceB", someObject);
        ne = NamingEntryUtil.lookupNamingEntry(scope, "resourceB");
        assertNotNull(ne);
        assertTrue(ne instanceof Resource);

        ne = NamingEntryUtil.lookupNamingEntry(null, "resourceB");
        assertNull(ne);

        ne = NamingEntryUtil.lookupNamingEntry(new ScopeB(), "resourceB");
        assertNull(ne);
        testLink();
    }

    @Test
    public void testLink () throws Exception
    {
        ScopeA scope = new ScopeA();
        InitialContext icontext = new InitialContext();
        Link link = new Link ("resourceA", "resourceB");
        NamingEntry ne = NamingEntryUtil.lookupNamingEntry(null, "resourceA");
        assertNotNull(ne);
        assertTrue(ne instanceof Link);
        assertEquals(icontext.lookup("resourceA"), "resourceB");

        link = new Link (scope, "jdbc/resourceX", "jdbc/resourceY");
        ne = NamingEntryUtil.lookupNamingEntry(scope, "jdbc/resourceX");
        assertNotNull(ne);
        assertTrue(ne instanceof Link);
    }

    @Test
    public void testResourceReferenceable() throws Exception
    {
        SomeOtherObject someOtherObj = new SomeOtherObject("100");
        InitialContext icontext = new InitialContext();
        Resource res = new Resource("resourceByReferenceable", someOtherObj);
        Object o = icontext.lookup("resourceByReferenceable");
        assertNotNull(o);
        assertTrue (o instanceof SomeOtherObject);
        assertEquals(((SomeOtherObject)o).getValue(), 100);
    }

    @Test
    public void testResourceReference () throws Exception
    {
        RefAddr refAddr = new StringRefAddr("val", "10");
        Reference ref = new Reference(SomeObject.class.getName(), refAddr, SomeObjectFactory.class.getName(), null);

        InitialContext icontext = new InitialContext();
        Resource resource = new Resource (null, "resourceByRef", ref);
        NamingEntry ne = NamingEntryUtil.lookupNamingEntry(null, "resourceByRef");
        assertNotNull(ne);
        assertTrue (ne instanceof Resource);

        Object o = icontext.lookup("resourceByRef");
        assertNotNull (o);
        assertTrue (o instanceof SomeObject);

        assertEquals(((SomeObject)o).getValue(), 10);
    }
}
