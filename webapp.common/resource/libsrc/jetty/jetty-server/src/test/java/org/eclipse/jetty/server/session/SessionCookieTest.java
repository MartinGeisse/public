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

package org.eclipse.jetty.server.session;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import javax.servlet.SessionCookieConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.eclipse.jetty.http.HttpCookie;
import org.junit.Test;
/**
 * SessionCookieTest
 *
 *
 */
public class SessionCookieTest
{
    
    public class MockSession extends AbstractSession
    {

        
        /**
         * @param abstractSessionManager
         * @param created
         * @param accessed
         * @param clusterId
         */
        protected MockSession(AbstractSessionManager abstractSessionManager, long created, long accessed, String clusterId)
        {
            super(abstractSessionManager, created, accessed, clusterId);
        }
        
    }
    
    public class MockSessionIdManager extends AbstractSessionIdManager
    {

        /** 
         * @see org.eclipse.jetty.server.SessionIdManager#idInUse(java.lang.String)
         */
        public boolean idInUse(String id)
        {
            return false;
        }

        /** 
         * @see org.eclipse.jetty.server.SessionIdManager#addSession(javax.servlet.http.HttpSession)
         */
        public void addSession(HttpSession session)
        {
           
        }

        /** 
         * @see org.eclipse.jetty.server.SessionIdManager#removeSession(javax.servlet.http.HttpSession)
         */
        public void removeSession(HttpSession session)
        {
             
        }

        /** 
         * @see org.eclipse.jetty.server.SessionIdManager#invalidateAll(java.lang.String)
         */
        public void invalidateAll(String id)
        {
           
        }

        /** 
         * @see org.eclipse.jetty.server.SessionIdManager#getClusterId(java.lang.String)
         */
        public String getClusterId(String nodeId)
        {
            int dot=nodeId.lastIndexOf('.');
            return (dot>0)?nodeId.substring(0,dot):nodeId;
        }

        /** 
         * @see org.eclipse.jetty.server.SessionIdManager#getNodeId(java.lang.String, javax.servlet.http.HttpServletRequest)
         */
        public String getNodeId(String clusterId, HttpServletRequest request)
        {
            return clusterId+'.'+_workerName;
        }

    }
    
    public class MockSessionManager extends AbstractSessionManager
    {

        /** 
         * @see org.eclipse.jetty.server.session.AbstractSessionManager#addSession(org.eclipse.jetty.server.session.AbstractSession)
         */
        protected void addSession(AbstractSession session)
        {
            
        }

        /** 
         * @see org.eclipse.jetty.server.session.AbstractSessionManager#getSession(java.lang.String)
         */
        public AbstractSession getSession(String idInCluster)
        {
            return null;
        }

        /** 
         * @see org.eclipse.jetty.server.session.AbstractSessionManager#invalidateSessions()
         */
        protected void invalidateSessions() throws Exception
        {
            
        }

        /** 
         * @see org.eclipse.jetty.server.session.AbstractSessionManager#newSession(javax.servlet.http.HttpServletRequest)
         */
        protected AbstractSession newSession(HttpServletRequest request)
        {
            return null;
        }

        /** 
         * @see org.eclipse.jetty.server.session.AbstractSessionManager#removeSession(java.lang.String)
         */
        protected boolean removeSession(String idInCluster)
        {
            return false;
        }
        
    }
    
    @Test
    public void testSecureSessionCookie () throws Exception
    {
        MockSessionIdManager idMgr = new MockSessionIdManager();
        idMgr.setWorkerName("node1");
        MockSessionManager mgr = new MockSessionManager();
        mgr.setSessionIdManager(idMgr);
        MockSession session = new MockSession(mgr, System.currentTimeMillis(), System.currentTimeMillis(), "node1123"); //clusterId
        
        SessionCookieConfig sessionCookieConfig = mgr.getSessionCookieConfig();
        sessionCookieConfig.setSecure(true);
        
        //sessionCookieConfig.secure == true, always mark cookie as secure, irrespective of if requestIsSecure
        HttpCookie cookie = mgr.getSessionCookie(session, "/foo", true);
        assertTrue(cookie.isSecure());
        //sessionCookieConfig.secure == true, always mark cookie as secure, irrespective of if requestIsSecure
        cookie = mgr.getSessionCookie(session, "/foo", false);
        assertTrue(cookie.isSecure());
        
        //sessionCookieConfig.secure==false, setSecureRequestOnly==true, requestIsSecure==true
        //cookie should be secure: see SessionCookieConfig.setSecure() javadoc
        sessionCookieConfig.setSecure(false);
        cookie = mgr.getSessionCookie(session, "/foo", true);
        assertTrue(cookie.isSecure());
        
        //sessionCookieConfig.secure=false, setSecureRequestOnly==true, requestIsSecure==false
        //cookie is not secure: see SessionCookieConfig.setSecure() javadoc
        cookie = mgr.getSessionCookie(session, "/foo", false);
        assertFalse(cookie.isSecure());
        
        //sessionCookieConfig.secure=false, setSecureRequestOnly==false, requestIsSecure==false
        //cookie is not secure: not a secure request
        mgr.setSecureRequestOnly(false);
        cookie = mgr.getSessionCookie(session, "/foo", false);
        assertFalse(cookie.isSecure());
        
        //sessionCookieConfig.secure=false, setSecureRequestOnly==false, requestIsSecure==true
        //cookie is not secure: not on secured requests and request is secure
        cookie = mgr.getSessionCookie(session, "/foo", true);
        assertFalse(cookie.isSecure());
        
        
    }

}
