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

package org.eclipse.jetty.osgi.boot;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.eclipse.jetty.deploy.App;
import org.eclipse.jetty.osgi.boot.internal.serverfactory.ServerInstanceWrapper;
import org.eclipse.jetty.osgi.boot.utils.EventSender;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceRegistration;



/**
 * BundleWebAppProvider
 *
 * A Jetty Provider that knows how to deploy a WebApp contained inside a Bundle.
 * 
 */
public class BundleWebAppProvider extends AbstractWebAppProvider implements BundleProvider
{     
    private static final Logger LOG = Log.getLogger(AbstractWebAppProvider.class);
    
    /**
     * Map of Bundle to App. Used when a Bundle contains a webapp.
     */
    private Map<Bundle, App> _bundleMap = new HashMap<Bundle, App>();
    
    private ServiceRegistration _serviceRegForBundles;
    

    /* ------------------------------------------------------------ */
    /**
     * @param wrapper
     */
    public BundleWebAppProvider (ServerInstanceWrapper wrapper)
    {
        super(wrapper);
    }
    
    
    
    
    /* ------------------------------------------------------------ */
    /** 
     * @see org.eclipse.jetty.util.component.AbstractLifeCycle#doStart()
     */
    protected void doStart() throws Exception
    {
        //register as an osgi service for deploying bundles, advertising the name of the jetty Server instance we are related to
        Dictionary<String,String> properties = new Hashtable<String,String>();
        properties.put(OSGiServerConstants.MANAGED_JETTY_SERVER_NAME, getServerInstanceWrapper().getManagedServerName());
        _serviceRegForBundles = FrameworkUtil.getBundle(this.getClass()).getBundleContext().registerService(BundleProvider.class.getName(), this, properties);
        super.doStart();
    }

    /* ------------------------------------------------------------ */
    /** 
     * @see org.eclipse.jetty.util.component.AbstractLifeCycle#doStop()
     */
    @Override
    protected void doStop() throws Exception
    {
        //unregister ourselves
        if (_serviceRegForBundles != null)
        {
            try
            {
                _serviceRegForBundles.unregister();
            }
            catch (Exception e)
            {
                LOG.warn(e);
            }
        }
     
        super.doStop();
    }
    
    
    

    
    
    /* ------------------------------------------------------------ */
    /**
     * A bundle has been added that could be a webapp 
     * @param bundle
     */
    public boolean bundleAdded (Bundle bundle) throws Exception
    {
        if (bundle == null)
            return false;

        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(getServerInstanceWrapper().getParentClassLoaderForWebapps());
        String contextPath = null;
        try 
        {
            Dictionary headers = bundle.getHeaders();

            //does the bundle have a OSGiWebappConstants.JETTY_WAR_FOLDER_PATH 
            if (headers.get(OSGiWebappConstants.JETTY_WAR_FOLDER_PATH) != null)
            {
                String base = (String)headers.get(OSGiWebappConstants.JETTY_WAR_FOLDER_PATH);
                contextPath = getContextPath(bundle);
                String originId = getOriginId(bundle, base);
 
                //TODO : we don't know whether an app is actually deployed, as deploymentManager swallows all
                //exceptions inside the impl of addApp. Need to send the Event and also register as a service
                //only if the deployment succeeded
                OSGiApp app = new OSGiApp(getDeploymentManager(), this, bundle, originId);
                app.setWebAppPath(base);
                app.setContextPath(contextPath);
                _bundleMap.put(bundle, app);
                getDeploymentManager().addApp(app);
                return true;
            }


            //does the bundle have a WEB-INF/web.xml
            if (bundle.getEntry("/WEB-INF/web.xml") != null)
            {
                String base = ".";
                contextPath = getContextPath(bundle);
                String originId = getOriginId(bundle, base);
       
                OSGiApp app = new OSGiApp(getDeploymentManager(), this, bundle, originId);
                app.setContextPath(contextPath);
                app.setWebAppPath(base);
                _bundleMap.put(bundle, app);
                getDeploymentManager().addApp(app);               
                return true;
            }

            //does the bundle define a OSGiWebappConstants.RFC66_WEB_CONTEXTPATH
            if (headers.get(OSGiWebappConstants.RFC66_WEB_CONTEXTPATH) != null)
            {
                //Could be a static webapp with no web.xml
                String base = ".";
                contextPath = (String)headers.get(OSGiWebappConstants.RFC66_WEB_CONTEXTPATH);
                String originId = getOriginId(bundle,base);
                
                OSGiApp app = new OSGiApp(getDeploymentManager(), this, bundle, originId);
                app.setContextPath(contextPath);
                app.setWebAppPath(base);
                _bundleMap.put(bundle, app);
                getDeploymentManager().addApp(app);                
                return true;
            }

            return false;
        }
        catch (Exception e)
        {
            
            throw e;
        }
        finally
        {
            Thread.currentThread().setContextClassLoader(cl);
        }
    }

    
    /* ------------------------------------------------------------ */
    /** 
     * Bundle has been removed. If it was a webapp we deployed, undeploy it.
     * @param bundle
     * 
     * @return true if this was a webapp we had deployed, false otherwise
     */
    public boolean bundleRemoved (Bundle bundle) throws Exception
    {
        App app = _bundleMap.remove(bundle);
        if (app != null)
        {
            getDeploymentManager().removeApp(app); 
            return true;
        }
        return false;
    }
    
   

    
    
    /* ------------------------------------------------------------ */
    private static String getContextPath(Bundle bundle)
    {
        Dictionary<?, ?> headers = bundle.getHeaders();
        String contextPath = (String) headers.get(OSGiWebappConstants.RFC66_WEB_CONTEXTPATH);
        if (contextPath == null)
        {
            // extract from the last token of the bundle's location:
            // (really ?could consider processing the symbolic name as an alternative
            // the location will often reflect the version.
            // maybe this is relevant when the file is a war)
            String location = bundle.getLocation();
            String toks[] = location.replace('\\', '/').split("/");
            contextPath = toks[toks.length - 1];
            // remove .jar, .war etc:
            int lastDot = contextPath.lastIndexOf('.');
            if (lastDot != -1)
                contextPath = contextPath.substring(0, lastDot);
        }
        if (!contextPath.startsWith("/"))
            contextPath = "/" + contextPath;
 
        return contextPath;
    }
    
    

}
