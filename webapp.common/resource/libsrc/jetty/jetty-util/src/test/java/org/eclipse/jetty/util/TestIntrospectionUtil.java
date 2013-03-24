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

package org.eclipse.jetty.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.junit.BeforeClass;
import org.junit.Test;


/**
 * TestInjection
 *
 *
 */
public class TestIntrospectionUtil
{
    public final static Class[] __INTEGER_ARG = new Class[] {Integer.class};
    static Field privateAField;
    static Field protectedAField;
    static Field publicAField;
    static Field defaultAField;
    static Field privateBField;
    static Field protectedBField;
    static Field publicBField;
    static Field defaultBField;
    static Method privateCMethod;
    static Method protectedCMethod;
    static Method publicCMethod;
    static Method defaultCMethod;
    static Method privateDMethod;
    static Method protectedDMethod;
    static Method publicDMethod;
    static Method defaultDMethod;
    
    public class ServletA 
    {
        private Integer privateA; 
        protected Integer protectedA;
        Integer defaultA;
        public Integer publicA;
    }
    
    public class ServletB extends ServletA
    {
        private String privateB;
        protected String protectedB;
        public String publicB;
        String defaultB;
    }
    
    public class ServletC
    {
        private void setPrivateC (Integer c) {}      
        protected void setProtectedC (Integer c) {}
        public void setPublicC(Integer c) {}
        void setDefaultC(Integer c) {}
    }
    
    public class ServletD extends ServletC
    {
        private void setPrivateD(Integer d) {}
        protected void setProtectedD(Integer d) {}
        public void setPublicD(Integer d) {}
        void setDefaultD(Integer d) {}
    }
    
    @BeforeClass
    public static void setUp()
    throws Exception
    {
        privateAField = ServletA.class.getDeclaredField("privateA");
        protectedAField = ServletA.class.getDeclaredField("protectedA");
        publicAField = ServletA.class.getDeclaredField("publicA");
        defaultAField = ServletA.class.getDeclaredField("defaultA");
        privateBField = ServletB.class.getDeclaredField("privateB");
        protectedBField = ServletB.class.getDeclaredField("protectedB");
        publicBField = ServletB.class.getDeclaredField("publicB");
        defaultBField = ServletB.class.getDeclaredField("defaultB");
        privateCMethod = ServletC.class.getDeclaredMethod("setPrivateC", __INTEGER_ARG);
        protectedCMethod = ServletC.class.getDeclaredMethod("setProtectedC", __INTEGER_ARG);
        publicCMethod = ServletC.class.getDeclaredMethod("setPublicC", __INTEGER_ARG);
        defaultCMethod = ServletC.class.getDeclaredMethod("setDefaultC", __INTEGER_ARG);
        privateDMethod = ServletD.class.getDeclaredMethod("setPrivateD", __INTEGER_ARG); 
        protectedDMethod = ServletD.class.getDeclaredMethod("setProtectedD", __INTEGER_ARG);
        publicDMethod = ServletD.class.getDeclaredMethod("setPublicD", __INTEGER_ARG);
        defaultDMethod = ServletD.class.getDeclaredMethod("setDefaultD", __INTEGER_ARG);
    }
   
    @Test
    public void testFieldPrivate ()
    throws Exception
    {
        //direct
        Field f = IntrospectionUtil.findField(ServletA.class, "privateA", Integer.class, true, false);
        assertEquals(privateAField,f);

        //inheritance
        try
        {
            IntrospectionUtil.findField(ServletB.class, "privateA", Integer.class, true, false);
            fail("Private fields should not be inherited");
        }
        catch (NoSuchFieldException e)
        {
            //expected
        }
    }
    
    @Test
    public void testFieldProtected()    
    throws Exception
    {
        //direct
        Field f = IntrospectionUtil.findField(ServletA.class, "protectedA", Integer.class, true, false);
        assertEquals(f, protectedAField);
        
        //inheritance
        f = IntrospectionUtil.findField(ServletB.class, "protectedA", Integer.class, true, false);
        assertEquals(f, protectedAField);
    }
    
    @Test
    public void testFieldPublic()
    throws Exception
    {
        //direct
        Field f = IntrospectionUtil.findField(ServletA.class, "publicA", Integer.class, true, false);
        assertEquals(f, publicAField);
        
        //inheritance
        f = IntrospectionUtil.findField(ServletB.class, "publicA", Integer.class, true, false);
        assertEquals(f, publicAField);
    }
    
    @Test
    public void testFieldDefault()
    throws Exception
    {
        //direct
        Field f = IntrospectionUtil.findField(ServletA.class, "defaultA", Integer.class, true, false);
        assertEquals(f, defaultAField);
        
        //inheritance
        f = IntrospectionUtil.findField(ServletB.class, "defaultA", Integer.class, true, false);
        assertEquals(f, defaultAField);
    }
    
    @Test
    public void testMethodPrivate ()
    throws Exception
    {
        //direct
        Method m = IntrospectionUtil.findMethod(ServletC.class, "setPrivateC", __INTEGER_ARG, true, false);
        assertEquals(m, privateCMethod);
        
        //inheritance
        try
        {
            IntrospectionUtil.findMethod(ServletD.class, "setPrivateC", __INTEGER_ARG, true, false);
            fail();
        }
        catch (NoSuchMethodException e)
        {
            //expected
        }
    }
    
    @Test
    public void testMethodProtected ()
    throws Exception
    {
        // direct
        Method m = IntrospectionUtil.findMethod(ServletC.class, "setProtectedC", __INTEGER_ARG, true, false);
        assertEquals(m, protectedCMethod);
        
        //inherited
        m = IntrospectionUtil.findMethod(ServletD.class, "setProtectedC", __INTEGER_ARG, true, false);
        assertEquals(m, protectedCMethod);
    }
    
    @Test
    public void testMethodPublic ()
    throws Exception
    {
        // direct
        Method m = IntrospectionUtil.findMethod(ServletC.class, "setPublicC",  __INTEGER_ARG, true, false);
        assertEquals(m, publicCMethod);
        
        //inherited
       m = IntrospectionUtil.findMethod(ServletD.class, "setPublicC",  __INTEGER_ARG, true, false);
       assertEquals(m, publicCMethod);
    }
    
    @Test
    public void testMethodDefault ()
    throws Exception
    {
        // direct
        Method m = IntrospectionUtil.findMethod(ServletC.class, "setDefaultC", __INTEGER_ARG, true, false);
        assertEquals(m, defaultCMethod);
        
        //inherited
        m = IntrospectionUtil.findMethod(ServletD.class, "setDefaultC", __INTEGER_ARG, true, false);
        assertEquals(m, defaultCMethod);
    }
}
