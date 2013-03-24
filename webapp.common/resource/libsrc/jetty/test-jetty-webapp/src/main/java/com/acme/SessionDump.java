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
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Enumeration;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


/* ------------------------------------------------------------ */
/** Test Servlet Sessions.
 *
 * 
 */
public class SessionDump extends HttpServlet
{

    int redirectCount=0;
    /* ------------------------------------------------------------ */
    String pageType;

    /* ------------------------------------------------------------ */
    @Override
    public void init(ServletConfig config)
         throws ServletException
    {
        super.init(config);        
    }

    /* ------------------------------------------------------------ */
    protected void handleForm(HttpServletRequest request,
                          HttpServletResponse response) 
    {
        HttpSession session = request.getSession(false);
        String action = request.getParameter("Action");
        String name =  request.getParameter("Name");
        String value =  request.getParameter("Value");

        if (action!=null)
        {
            if(action.equals("New Session"))
            {   
                session = request.getSession(true);
                session.setAttribute("test","value");
            }
            else if (session!=null)
            {
                if (action.equals("Invalidate"))
                    session.invalidate();
                else if (action.equals("Set") && name!=null && name.length()>0)
                    session.setAttribute(name,value);
                else if (action.equals("Remove"))
                    session.removeAttribute(name);
            }       
        }
    }
    
    /* ------------------------------------------------------------ */
    @Override
    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) 
        throws ServletException, IOException
    {
        handleForm(request,response);
        String nextUrl = getURI(request)+"?R="+redirectCount++;
        String encodedUrl=response.encodeRedirectURL(nextUrl);
        response.sendRedirect(encodedUrl);
    }
        
    /* ------------------------------------------------------------ */
    @Override
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) 
        throws ServletException, IOException
    {
        handleForm(request,response);
        
        response.setContentType("text/html");

        HttpSession session = request.getSession(getURI(request).indexOf("new")>0);
        try
        {
            if (session!=null) 
                session.isNew();
        }
        catch(IllegalStateException e)
        {
            session=null;
        }
        
        PrintWriter out = response.getWriter();
        out.println("<h1>Session Dump Servlet:</h1>"); 
        out.println("<form action=\""+response.encodeURL(getURI(request))+"\" method=\"post\">");       
        
        if (session==null)
        {
            out.println("<H3>No Session</H3>");
            out.println("<input type=\"submit\" name=\"Action\" value=\"New Session\"/>");
        }
        else
        {
            try
            {  
                out.println("<b>ID:</b> "+session.getId()+"<br/>");
                out.println("<b>New:</b> "+session.isNew()+"<br/>");
                out.println("<b>Created:</b> "+new Date(session.getCreationTime())+"<br/>");
                out.println("<b>Last:</b> "+new Date(session.getLastAccessedTime())+"<br/>");
                out.println("<b>Max Inactive:</b> "+session.getMaxInactiveInterval()+"<br/>");
                out.println("<b>Context:</b> "+session.getServletContext()+"<br/>");
                
              
                Enumeration keys=session.getAttributeNames();
                while(keys.hasMoreElements())
                {
                    String name=(String)keys.nextElement();
                    String value=""+session.getAttribute(name);

                    out.println("<b>"+name+":</b> "+value+"<br/>");
                }

                out.println("<b>Name:</b><input type=\"text\" name=\"Name\" /><br/>");
                out.println("<b>Value:</b><input type=\"text\" name=\"Value\" /><br/>");

                out.println("<input type=\"submit\" name=\"Action\" value=\"Set\"/>");
                out.println("<input type=\"submit\" name=\"Action\" value=\"Remove\"/>");
                out.println("<input type=\"submit\" name=\"Action\" value=\"Refresh\"/>");
                out.println("<input type=\"submit\" name=\"Action\" value=\"Invalidate\"/><br/>");
                
                out.println("</form><br/>");
                
                if (request.isRequestedSessionIdFromCookie())
                    out.println("<P>Turn off cookies in your browser to try url encoding<BR>");
                
                if (request.isRequestedSessionIdFromURL())
                    out.println("<P>Turn on cookies in your browser to try cookie encoding<BR>");
                out.println("<a href=\""+response.encodeURL(request.getRequestURI()+"?q=0")+"\">Encoded Link</a><BR>");
                
            }
            catch (IllegalStateException e)
            {
                e.printStackTrace();
            }
        }

    }

    /* ------------------------------------------------------------ */
    @Override
    public String getServletInfo() {
        return "Session Dump Servlet";
    }

    /* ------------------------------------------------------------ */
    private String getURI(HttpServletRequest request)
    {
        String uri=(String)request.getAttribute("javax.servlet.forward.request_uri");
        if (uri==null)
            uri=request.getRequestURI();
        return uri;
    }
    
}
