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

package com.acme;

import javax.servlet.DispatcherType;
import javax.servlet.ServletContextAttributeEvent;
import javax.servlet.ServletContextAttributeListener;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRequestAttributeEvent;
import javax.servlet.ServletRequestAttributeListener;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.ServletRegistration;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletSecurityElement;
import javax.servlet.HttpConstraintElement;
import javax.servlet.HttpMethodConstraintElement;
import javax.servlet.annotation.ServletSecurity;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSessionActivationListener;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import java.util.EnumSet;
import java.util.Set;

public class TestListener implements HttpSessionListener,  HttpSessionAttributeListener, HttpSessionActivationListener, ServletContextListener, ServletContextAttributeListener, ServletRequestListener, ServletRequestAttributeListener
{
    public void attributeAdded(HttpSessionBindingEvent se)
    {
        // System.err.println("attributedAdded "+se);
    }

    public void attributeRemoved(HttpSessionBindingEvent se)
    {
        // System.err.println("attributeRemoved "+se);
    }

    public void attributeReplaced(HttpSessionBindingEvent se)
    {
        // System.err.println("attributeReplaced "+se);
    }

    public void sessionWillPassivate(HttpSessionEvent se)
    {
        // System.err.println("sessionWillPassivate "+se);
    }

    public void sessionDidActivate(HttpSessionEvent se)
    {
        // System.err.println("sessionDidActivate "+se);
    }

    public void contextInitialized(ServletContextEvent sce)
    {	
        //configure programmatic security
        ServletRegistration.Dynamic rego = sce.getServletContext().addServlet("RegoTest", RegTest.class.getName());
        rego.addMapping("/rego/*");
        HttpConstraintElement constraintElement = new HttpConstraintElement(ServletSecurity.EmptyRoleSemantic.PERMIT, 
                                                                            ServletSecurity.TransportGuarantee.NONE, new String[]{"admin"});
        ServletSecurityElement securityElement = new ServletSecurityElement(constraintElement, null);
        Set<String> unchanged = rego.setServletSecurity(securityElement);
        //System.err.println("Security constraints registered: "+unchanged.isEmpty());

        //Test that a security constraint from web.xml can't be overridden programmatically
        ServletRegistration.Dynamic rego2 = sce.getServletContext().addServlet("RegoTest2", RegTest.class.getName());
        rego2.addMapping("/rego2/*");
        securityElement = new ServletSecurityElement(constraintElement, null);
        unchanged = rego2.setServletSecurity(securityElement);
        //System.err.println("Overridding web.xml constraints not possible:" +!unchanged.isEmpty());

    	/* For servlet 3.0 */
    	FilterRegistration.Dynamic registration = sce.getServletContext().addFilter("TestFilter",TestFilter.class.getName());
        if (registration != null) //otherwise it was configured in web.xml
        {
    	    registration.setInitParameter("remote", "false");
    	    registration.setAsyncSupported(true);
    	    registration.addMappingForUrlPatterns(
    	        EnumSet.of(DispatcherType.ERROR,DispatcherType.ASYNC,DispatcherType.FORWARD,DispatcherType.INCLUDE,DispatcherType.REQUEST),
    	        true, 
    	        new String[]{"/*"});
        }
    }

    public void contextDestroyed(ServletContextEvent sce)
    {
        // System.err.println("contextDestroyed "+sce);
    }

    public void attributeAdded(ServletContextAttributeEvent scab)
    {
        // System.err.println("attributeAdded "+scab);
    }

    public void attributeRemoved(ServletContextAttributeEvent scab)
    {
        // System.err.println("attributeRemoved "+scab);
    }

    public void attributeReplaced(ServletContextAttributeEvent scab)
    {
        // System.err.println("attributeReplaced "+scab);
    }

    public void requestDestroyed(ServletRequestEvent sre)
    {
        ((HttpServletRequest)sre.getServletRequest()).getSession(false);
        sre.getServletRequest().setAttribute("requestInitialized",null);
        // System.err.println("requestDestroyed "+sre);
    }

    public void requestInitialized(ServletRequestEvent sre)
    {
        sre.getServletRequest().setAttribute("requestInitialized","'"+sre.getServletContext().getContextPath()+"'");
        // System.err.println("requestInitialized "+sre);
    }

    public void attributeAdded(ServletRequestAttributeEvent srae)
    {
        // System.err.println("attributeAdded "+srae);
    }

    public void attributeRemoved(ServletRequestAttributeEvent srae)
    {
        // System.err.println("attributeRemoved "+srae);
    }

    public void attributeReplaced(ServletRequestAttributeEvent srae)
    {
        // System.err.println("attributeReplaced "+srae);
    }

    public void sessionCreated(HttpSessionEvent se)
    {
        // System.err.println("sessionCreated "+se);
    }

    public void sessionDestroyed(HttpSessionEvent se)
    {
        // System.err.println("sessionDestroyed "+se);
    }

    public void requestCompleted(ServletRequestEvent rre)
    {
        // TODO Auto-generated method stub
        
    }

    public void requestResumed(ServletRequestEvent rre)
    {
        // TODO Auto-generated method stub
        
    }

    public void requestSuspended(ServletRequestEvent rre)
    {
        // TODO Auto-generated method stub
        
    }

}
