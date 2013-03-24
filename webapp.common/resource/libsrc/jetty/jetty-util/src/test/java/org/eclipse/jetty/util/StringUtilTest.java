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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import junit.framework.Assert;

import org.junit.Test;


/**
 * 
 *
 */
public class StringUtilTest
{
    @Test
    public void testAsciiToLowerCase()
    {
        String lc="\u0690bc def 1\u06903";
        assertEquals(StringUtil.asciiToLowerCase("\u0690Bc DeF 1\u06903"), lc);
        assertTrue(StringUtil.asciiToLowerCase(lc)==lc);
    }

    @Test
    public void testStartsWithIgnoreCase()
    {
        
        assertTrue(StringUtil.startsWithIgnoreCase("\u0690b\u0690defg", "\u0690b\u0690"));
        assertTrue(StringUtil.startsWithIgnoreCase("\u0690bcdefg", "\u0690bc"));
        assertTrue(StringUtil.startsWithIgnoreCase("\u0690bcdefg", "\u0690Bc"));
        assertTrue(StringUtil.startsWithIgnoreCase("\u0690Bcdefg", "\u0690bc"));
        assertTrue(StringUtil.startsWithIgnoreCase("\u0690Bcdefg", "\u0690Bc"));
        assertTrue(StringUtil.startsWithIgnoreCase("\u0690bcdefg", ""));
        assertTrue(StringUtil.startsWithIgnoreCase("\u0690bcdefg", null));
        assertTrue(StringUtil.startsWithIgnoreCase("\u0690bcdefg", "\u0690bcdefg"));

        assertFalse(StringUtil.startsWithIgnoreCase(null, "xyz")); 
        assertFalse(StringUtil.startsWithIgnoreCase("\u0690bcdefg", "xyz"));
        assertFalse(StringUtil.startsWithIgnoreCase("\u0690", "xyz")); 
    }

    @Test
    public void testEndsWithIgnoreCase()
    {
        assertTrue(StringUtil.endsWithIgnoreCase("\u0690bcd\u0690f\u0690", "\u0690f\u0690"));
        assertTrue(StringUtil.endsWithIgnoreCase("\u0690bcdefg", "efg"));
        assertTrue(StringUtil.endsWithIgnoreCase("\u0690bcdefg", "eFg"));
        assertTrue(StringUtil.endsWithIgnoreCase("\u0690bcdeFg", "efg"));
        assertTrue(StringUtil.endsWithIgnoreCase("\u0690bcdeFg", "eFg"));
        assertTrue(StringUtil.endsWithIgnoreCase("\u0690bcdefg", ""));
        assertTrue(StringUtil.endsWithIgnoreCase("\u0690bcdefg", null));
        assertTrue(StringUtil.endsWithIgnoreCase("\u0690bcdefg", "\u0690bcdefg"));

        assertFalse(StringUtil.endsWithIgnoreCase(null, "xyz")); 
        assertFalse(StringUtil.endsWithIgnoreCase("\u0690bcdefg", "xyz"));
        assertFalse(StringUtil.endsWithIgnoreCase("\u0690", "xyz"));  
    }

    @Test
    public void testIndexFrom()
    {
        assertEquals(StringUtil.indexFrom("\u0690bcd", "xyz"),-1);
        assertEquals(StringUtil.indexFrom("\u0690bcd", "\u0690bcz"),0);
        assertEquals(StringUtil.indexFrom("\u0690bcd", "bcz"),1);
        assertEquals(StringUtil.indexFrom("\u0690bcd", "dxy"),3);
    }

    @Test
    public void testReplace()
    {
        String s="\u0690bc \u0690bc \u0690bc";
        assertEquals(StringUtil.replace(s, "\u0690bc", "xyz"),"xyz xyz xyz");
        assertTrue(StringUtil.replace(s,"xyz","pqy")==s);
        
        s=" \u0690bc ";
        assertEquals(StringUtil.replace(s, "\u0690bc", "xyz")," xyz ");
        
    }

    @Test
    public void testUnquote()
    {
        String uq =" not quoted ";
        assertTrue(StringUtil.unquote(uq)==uq);
        assertEquals(StringUtil.unquote("' quoted string '")," quoted string ");
        assertEquals(StringUtil.unquote("\" quoted string \"")," quoted string ");
        assertEquals(StringUtil.unquote("' quoted\"string '")," quoted\"string ");
        assertEquals(StringUtil.unquote("\" quoted'string \"")," quoted'string ");
    }


    @Test
    public void testNonNull()
    {
        String nn="";
        assertTrue(nn==StringUtil.nonNull(nn));
        assertEquals("",StringUtil.nonNull(null));
    }

    /*
     * Test for boolean equals(String, char[], int, int)
     */
    @Test
    public void testEqualsStringcharArrayintint()
    {
        assertTrue(StringUtil.equals("\u0690bc", new char[] {'x','\u0690','b','c','z'},1,3));
        assertFalse(StringUtil.equals("axc", new char[] {'x','a','b','c','z'},1,3));
    }

    @Test
    public void testAppend()
    {
        StringBuilder buf = new StringBuilder();
        buf.append('a');
        StringUtil.append(buf, "abc", 1, 1);
        StringUtil.append(buf, (byte)12, 16);
        StringUtil.append(buf, (byte)16, 16);
        StringUtil.append(buf, (byte)-1, 16);
        StringUtil.append(buf, (byte)-16, 16);
        assertEquals("ab0c10fff0",buf.toString());
        
    }
    
    @Test
    public void testSidConversion() throws Exception
    {
        String sid4 = "S-1-4-21-3623811015-3361044348-30300820";
        String sid5 = "S-1-5-21-3623811015-3361044348-30300820-1013";
        String sid6 = "S-1-6-21-3623811015-3361044348-30300820-1013-23445";
        String sid12 = "S-1-12-21-3623811015-3361044348-30300820-1013-23445-21-3623811015-3361044348-30300820-1013-23445";

        byte[] sid4Bytes = StringUtil.sidStringToBytes(sid4);
        byte[] sid5Bytes = StringUtil.sidStringToBytes(sid5);
        byte[] sid6Bytes = StringUtil.sidStringToBytes(sid6);
        byte[] sid12Bytes = StringUtil.sidStringToBytes(sid12);

        Assert.assertEquals(sid4, StringUtil.sidBytesToString(sid4Bytes));
        Assert.assertEquals(sid5, StringUtil.sidBytesToString(sid5Bytes));
        Assert.assertEquals(sid6, StringUtil.sidBytesToString(sid6Bytes));
        Assert.assertEquals(sid12, StringUtil.sidBytesToString(sid12Bytes));

    }
    
    
    public static void main(String[] arg) throws Exception
    {
        String string = "Now \u0690xxxxxxxx";
        System.err.println(string);
        byte[] bytes=string.getBytes("UTF-8");
        System.err.println(new String(bytes));
        System.err.println(bytes.length);
        long calc=0;
        Utf8StringBuffer strbuf = new Utf8StringBuffer(bytes.length);
        for (int i=0;i<10;i++)
        {
            long s1=System.currentTimeMillis();
            for (int j=1000000; j-->0;)
            {
                calc+=new String(bytes,0,bytes.length,"UTF-8").hashCode();
            }
            long s2=System.currentTimeMillis();
            for (int j=1000000; j-->0;)
            {
                calc+=StringUtil.toUTF8String(bytes,0,bytes.length).hashCode();
            }
            long s3=System.currentTimeMillis();
            for (int j=1000000; j-->0;)
            {
                Utf8StringBuffer buffer = new Utf8StringBuffer(bytes.length);
                buffer.append(bytes,0,bytes.length);
                calc+=buffer.toString().hashCode();
            }
            long s4=System.currentTimeMillis();
            for (int j=1000000; j-->0;)
            {
                strbuf.reset();
                strbuf.append(bytes,0,bytes.length);
                calc+=strbuf.toString().hashCode();
            }
            long s5=System.currentTimeMillis();
            
            System.err.println((s2-s1)+", "+(s3-s2)+", "+(s4-s3)+", "+(s5-s4));
        }
        System.err.println(calc);
    }
}
