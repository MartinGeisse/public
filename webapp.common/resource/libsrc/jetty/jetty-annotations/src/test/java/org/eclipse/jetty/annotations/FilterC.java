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

package org.eclipse.jetty.annotations;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.annotation.security.RunAs;
import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebFilter(filterName="CFilter", dispatcherTypes={DispatcherType.REQUEST}, urlPatterns = {"/*"}, initParams={@WebInitParam(name="a", value="99")}, asyncSupported=false)
@RunAs("admin")
public class FilterC implements Filter
{
    @Resource (mappedName="foo")
    private Double foo;
    
    @PreDestroy
    public void pre ()
    {
        
    }
    
    @PostConstruct
    public void post()
    {
        
    }


    public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain arg2)
    throws IOException, ServletException
    {
        HttpServletRequest request = (HttpServletRequest)arg0;
        HttpServletResponse response = (HttpServletResponse)arg1;
        HttpSession session = request.getSession(true);
        String val = request.getParameter("action");
        if (val!=null)
            session.setAttribute("action", val);
        arg2.doFilter(request, response);
    }

    public void destroy()
    { 
    }

    public void init(FilterConfig arg0) throws ServletException
    {  
    }
}
