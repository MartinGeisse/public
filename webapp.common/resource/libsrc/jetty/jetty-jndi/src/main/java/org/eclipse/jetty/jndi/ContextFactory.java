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

package org.eclipse.jetty.jndi;


import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;
import java.util.WeakHashMap;

import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NameParser;
import javax.naming.Reference;
import javax.naming.StringRefAddr;
import javax.naming.spi.ObjectFactory;

import org.eclipse.jetty.server.handler.ContextHandler;

import org.eclipse.jetty.util.log.Logger;



/**
 * ContextFactory.java
 *
 * This is an object factory that produces a jndi naming
 * context based on a classloader. 
 * 
 *  It is used for the java:comp context.
 *  
 *  This object factory is bound at java:comp. When a
 *  lookup arrives for java:comp,  this object factory
 *  is invoked and will return a context specific to
 *  the caller's environment (so producing the java:comp/env
 *  specific to a webapp).
 *  
 *  The context selected is based on classloaders. First
 *  we try looking at the thread context classloader if it is set, and walk its
 *  hierarchy, creating a context if none is found. If the thread context classloader
 *  is not set, then we use the classloader associated with the current Context.
 *  
 *  If there is no current context, or no classloader, we return null.
 * 
 * Created: Fri Jun 27 09:26:40 2003
 *
 * 
 * 
 */
public class ContextFactory implements ObjectFactory
{
    private static Logger __log = NamingUtil.__log;
    
    /**
     * Map of classloaders to contexts.
     */
    private static final WeakHashMap __contextMap = new WeakHashMap();
    
    /**
     * Threadlocal for injecting a context to use
     * instead of looking up the map.
     */
    private static final ThreadLocal __threadContext = new ThreadLocal();

    
    /** 
     * Find or create a context which pertains to a classloader.
     * 
     * If the thread context classloader is set, we try to find an already-created naming context
     * for it. If one does not exist, we walk its classloader hierarchy until one is found, or we 
     * run out of parent classloaders. In the latter case, we will create a new naming context associated
     * with the original thread context classloader.
     * 
     * If the thread context classloader is not set, we obtain the classloader from the current 
     * jetty Context, and look for an already-created naming context. 
     * 
     * If there is no current jetty Context, or it has no associated classloader, we 
     * return null.
     * @see javax.naming.spi.ObjectFactory#getObjectInstance(java.lang.Object, javax.naming.Name, javax.naming.Context, java.util.Hashtable)
     */
    public Object getObjectInstance (Object obj,
                                     Name name,
                                     Context nameCtx,
                                     Hashtable env)
        throws Exception
    {
        //First, see if we have had a context injected into us to use.
        Context ctx = (Context)__threadContext.get();
        if (ctx != null) 
        {
            if(__log.isDebugEnabled()) __log.debug("Using the Context that is bound on the thread");
            return ctx;
        }
        
       
        ClassLoader tccl = Thread.currentThread().getContextClassLoader();
        ClassLoader loader = tccl;
        //If the thread context classloader is set, then try its hierarchy to find a matching context
        if (loader != null)
        {
            if (__log.isDebugEnabled() && loader != null) __log.debug("Trying thread context classloader");
            while (ctx == null && loader != null)
            {
                ctx = getContextForClassLoader(loader);
                if (ctx == null && loader != null)
                    loader = loader.getParent();
            }

            if (ctx == null)
            {
                ctx = newNamingContext(obj, tccl, env, name, nameCtx);
                __contextMap.put (tccl, ctx);
                if(__log.isDebugEnabled())__log.debug("Made context "+name.get(0)+" for classloader: "+tccl);
            }
            return ctx;
        }


        //If trying thread context classloader hierarchy failed, try the
        //classloader associated with the current context
        if (ContextHandler.getCurrentContext() != null)
        {
            
            if (__log.isDebugEnabled() && loader != null) __log.debug("Trying classloader of current org.eclipse.jetty.server.handler.ContextHandler");
            loader = ContextHandler.getCurrentContext().getContextHandler().getClassLoader();
            ctx = (Context)__contextMap.get(loader);    

            if (ctx == null && loader != null)
            {
                ctx = newNamingContext(obj, loader, env, name, nameCtx);
                __contextMap.put (loader, ctx);
                if(__log.isDebugEnabled())__log.debug("Made context "+name.get(0)+" for classloader: "+loader);
            }

            return ctx;
        }
        return null;
    }


    /**
     * Create a new NamingContext.
     * @param obj
     * @param loader
     * @param env
     * @param name
     * @param parentCtx
     * @return
     * @throws Exception
     */
    public NamingContext newNamingContext(Object obj, ClassLoader loader, Hashtable env, Name name, Context parentCtx)
    throws Exception
    {
        Reference ref = (Reference)obj;
        StringRefAddr parserAddr = (StringRefAddr)ref.get("parser");
        String parserClassName = (parserAddr==null?null:(String)parserAddr.getContent());
        NameParser parser = (NameParser)(parserClassName==null?null:loader.loadClass(parserClassName).newInstance());

        return new NamingContext (env,
                                  name.get(0),
                                  (NamingContext)parentCtx,
                                  parser);
    }

  
    /**
     * Find the naming Context for the given classloader
     * @param loader
     * @return
     */
    public Context getContextForClassLoader(ClassLoader loader)
    {
        if (loader == null)
            return null;
        
        return (Context)__contextMap.get(loader);
    }
    

    /**
     * Associate the given Context with the current thread.
     * resetComponentContext method should be called to reset the context.
     * @param ctx the context to associate to the current thread.
     * @return the previous context associated on the thread (can be null)
     */
    public static Context setComponentContext(final Context ctx) 
    {
        Context previous = (Context)__threadContext.get();
        __threadContext.set(ctx);
        return previous;
    }

    /**
     * Set back the context with the given value.
     * Don't return the previous context, use setComponentContext() method for this.
     * @param ctx the context to associate to the current thread.
     */
    public static void resetComponentContext(final Context ctx) 
    {
        __threadContext.set(ctx);
    }

    public static void dump(Appendable out, String indent) throws IOException
    {
        out.append("o.e.j.jndi.ContextFactory@").append(Long.toHexString(__contextMap.hashCode())).append("\n");
        int size=__contextMap.size();
        int i=0;
        for (Map.Entry<ClassLoader,NamingContext> entry : ((Map<ClassLoader,NamingContext>)__contextMap).entrySet())
        {
            boolean last=++i==size;
            ClassLoader loader=entry.getKey();
            out.append(indent).append(" +- ").append(loader.getClass().getSimpleName()).append("@").append(Long.toHexString(loader.hashCode())).append(": ");
            
            NamingContext context = entry.getValue();
            context.dump(out,indent+(last?"    ":" |  "));
        }
    }

} 
