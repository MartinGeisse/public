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

package org.eclipse.jetty.annotations.resources;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * ResourceA
 *
 *
 */
public class ResourceA implements javax.servlet.Servlet
{
    private Integer e;
    private Integer h;
    private Integer k;

    
    @Resource(name="myf", mappedName="resB") //test giving both a name and mapped name from the environment
    private Integer f;//test a non inherited field that needs injection
    
    @Resource(mappedName="resA") //test the default naming scheme but using a mapped name from the environment
    private Integer g;
    
    @Resource(name="resA") //test using the given name as the name from the environment
    private Integer j;
    
    @Resource(mappedName="resB") //test using the default name on an inherited field
    protected Integer n; //TODO - if it's inherited, is it supposed to use the classname of the class it is inherited by?
    
    
    @Resource(name="mye", mappedName="resA", type=Integer.class)
    public void setE(Integer e)
    {
        this.e=e;
    }
    public Integer getE()
    {
        return this.e;
    }
    
    public Integer getF()
    {
        return this.f;
    }
    
    public Integer getG()
    {
        return this.g;
    }
    
    public Integer getJ()
    {
        return this.j;
    }
    
    @Resource(mappedName="resA")
    public void setH(Integer h)
    {
        this.h=h;
    }
    
    @Resource(name="resA")
    public void setK(Integer k)
    {
        this.k=k;
    }
    public void x()
    {
        System.err.println("ResourceA.x");
    }
    public void destroy()
    {
        // TODO Auto-generated method stub
        
    }
    public ServletConfig getServletConfig()
    {
        // TODO Auto-generated method stub
        return null;
    }
    public String getServletInfo()
    {
        // TODO Auto-generated method stub
        return null;
    }
    public void init(ServletConfig arg0) throws ServletException
    {
        // TODO Auto-generated method stub
        
    }
    public void service(ServletRequest arg0, ServletResponse arg1)
            throws ServletException, IOException
    {
        // TODO Auto-generated method stub
        
    }
}
