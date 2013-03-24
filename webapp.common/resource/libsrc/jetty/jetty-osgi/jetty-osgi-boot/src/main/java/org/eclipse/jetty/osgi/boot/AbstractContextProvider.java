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

import java.io.File;
import java.net.URL;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;

import org.eclipse.jetty.deploy.App;
import org.eclipse.jetty.deploy.AppProvider;
import org.eclipse.jetty.deploy.DeploymentManager;
import org.eclipse.jetty.osgi.boot.internal.serverfactory.ServerInstanceWrapper;
import org.eclipse.jetty.osgi.boot.internal.webapp.BundleFileLocatorHelperFactory;
import org.eclipse.jetty.osgi.boot.utils.EventSender;
import org.eclipse.jetty.osgi.boot.utils.OSGiClassLoader;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.util.component.AbstractLifeCycle;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;
import org.eclipse.jetty.util.resource.JarResource;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.webapp.WebAppContext;
import org.eclipse.jetty.xml.XmlConfiguration;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceRegistration;




/**
 * AbstractContextProvider
 *
 *
 */
public abstract class AbstractContextProvider extends AbstractLifeCycle implements AppProvider
{
    private static final Logger LOG = Log.getLogger(AbstractContextProvider.class);
    
    private DeploymentManager _deploymentManager;
    
    
    private ServerInstanceWrapper _serverWrapper;
    
    
    
    
    /* ------------------------------------------------------------ */
    /**
     * BundleApp
     *
     *
     */
    public class OSGiApp extends AbstractOSGiApp
    {
        private String _contextFile;
        private ContextHandler _contextHandler;
        private boolean _configured = false;
        
        public OSGiApp(DeploymentManager manager, AppProvider provider, String originId, Bundle bundle, String contextFile)
        {
            super(manager, provider, bundle, originId);
            _contextFile = contextFile;
        }
        
        public OSGiApp(DeploymentManager manager, AppProvider provider, Bundle bundle, Dictionary properties, String contextFile, String originId)
        {
            super(manager, provider, bundle, properties, originId);
            _contextFile = contextFile;
        }
               
        public String getContextFile ()
        {
            return _contextFile;
        }
        
        public void setHandler(ContextHandler h)
        {
            _contextHandler = h;
        }
       
        public ContextHandler createContextHandler()
        throws Exception
        {
            configureContextHandler();
            return _contextHandler;
        }

        public void configureContextHandler()
        throws Exception
        {
            if (_configured)
                return;

            _configured = true;
            
            //Override for bundle root may have been set
            String bundleOverrideLocation = (String)_properties.get(OSGiWebappConstants.JETTY_BUNDLE_INSTALL_LOCATION_OVERRIDE);
            if (bundleOverrideLocation == null)
                bundleOverrideLocation = (String)_properties.get(OSGiWebappConstants.SERVICE_PROP_BUNDLE_INSTALL_LOCATION_OVERRIDE);

            //Location on filesystem of bundle or the bundle override location
            File bundleLocation = BundleFileLocatorHelperFactory.getFactory().getHelper().getBundleInstallLocation(_bundle);
            File root = (bundleOverrideLocation==null?bundleLocation:new File(bundleOverrideLocation));
            Resource rootResource = Resource.newResource(BundleFileLocatorHelperFactory.getFactory().getHelper().getLocalURL(root.toURI().toURL()));
            
            //try and make sure the rootResource is useable - if its a jar then make it a jar file url
            if (rootResource.exists()&& !rootResource.isDirectory() && !rootResource.toString().startsWith("jar:"))
            {
               Resource jarResource = JarResource.newJarResource(rootResource);
               if (jarResource.exists() && jarResource.isDirectory())
                   rootResource = jarResource;
            }
            
            //Set the base resource of the ContextHandler, if not already set, can also be overridden by the context xml file
            if (_contextHandler != null && _contextHandler.getBaseResource() == null)
            {
                _contextHandler.setBaseResource(rootResource);
            }

            //Use a classloader that knows about the common jetty parent loader, and also the bundle                  
            OSGiClassLoader classLoader = new OSGiClassLoader(getServerInstanceWrapper().getParentClassLoaderForWebapps(), _bundle);

            //if there is a context file, find it and apply it
            if (_contextFile == null && _contextHandler == null)
                throw new IllegalStateException("No context file or ContextHandler");

            if (_contextFile != null)
            {   
                //apply the contextFile, creating the ContextHandler, the DeploymentManager will register it in the ContextHandlerCollection
                Resource res = null;

                //try to find the context file in the filesystem
                if (_contextFile.startsWith("/"))
                    res = getFileAsResource(_contextFile);

                //try to find it relative to jetty home
                if (res == null)
                {
                    //See if the specific server we are related to has jetty.home set
                    String jettyHome = (String)getServerInstanceWrapper().getServer().getAttribute(OSGiServerConstants.JETTY_HOME);
                    if (jettyHome != null)
                        res = getFileAsResource(jettyHome, _contextFile);

                    //try to see if a SystemProperty for jetty.home is set
                    if (res == null)
                    {
                        jettyHome =  System.getProperty(OSGiServerConstants.JETTY_HOME);

                        if (jettyHome != null)
                        {
                            if (jettyHome.startsWith("\"") || jettyHome.startsWith("'"))
                                jettyHome = jettyHome.substring(1);
                            if (jettyHome.endsWith("\"") || (jettyHome.endsWith("'")))
                                jettyHome = jettyHome.substring(0,jettyHome.length()-1);

                            res = getFileAsResource(jettyHome, _contextFile); 
                            if (LOG.isDebugEnabled()) LOG.debug("jetty home context file:"+res);
                        }
                    }
                }

                //try to find it relative to an override location that has been specified
                if (res == null)
                {                 
                    if (bundleOverrideLocation != null)
                    { 
                        res = getFileAsResource(Resource.newResource(bundleOverrideLocation).getFile(), _contextFile);
                        if (LOG.isDebugEnabled()) LOG.debug("Bundle override location context file:"+res);
                    }
                }         

                //try to find it relative to the bundle in which it is being deployed
                if (res == null)
                {
                    if (_contextFile.startsWith("./"))
                        _contextFile = _contextFile.substring(1);

                    if (!_contextFile.startsWith("/"))
                        _contextFile = "/" + _contextFile;

                    URL contextURL = _bundle.getEntry(_contextFile);
                    if (contextURL != null)
                        res = Resource.newResource(contextURL);
                }

                //apply the context xml file, either to an existing ContextHandler, or letting the
                //it create the ContextHandler as necessary
                if (res != null)
                { 
                    ClassLoader cl = Thread.currentThread().getContextClassLoader();

                    LOG.debug("Context classloader = " + cl);
                    try
                    {
                        Thread.currentThread().setContextClassLoader(classLoader);
                        
                        XmlConfiguration xmlConfiguration = new XmlConfiguration(res.getInputStream());
                        HashMap properties = new HashMap();
                        //put the server instance in
                        properties.put("Server", getServerInstanceWrapper().getServer());
                        //put in the location of the bundle root
                        properties.put("bundle.root", rootResource.toString());
                        
                        // insert the bundle's location as a property.
                        xmlConfiguration.getProperties().putAll(properties);

                        if (_contextHandler == null)
                            _contextHandler = (ContextHandler) xmlConfiguration.configure();
                        else
                            xmlConfiguration.configure(_contextHandler);
                    }
                    finally
                    {
                        Thread.currentThread().setContextClassLoader(cl);
                    }
                }
            }

            //Set up the class loader we created
            _contextHandler.setClassLoader(classLoader);
            
            
            //If a bundle/service property specifies context path, let it override the context xml
            String contextPath = (String)_properties.get(OSGiWebappConstants.RFC66_WEB_CONTEXTPATH);
            if (contextPath == null)
                contextPath = (String)_properties.get(OSGiWebappConstants.SERVICE_PROP_CONTEXT_PATH);
            if (contextPath != null)
                _contextHandler.setContextPath(contextPath);    
            
            //osgi Enterprise Spec r4 p.427
            _contextHandler.setAttribute(OSGiWebappConstants.OSGI_BUNDLECONTEXT, _bundle.getBundleContext());
            
            //make sure we protect also the osgi dirs specified by OSGi Enterprise spec
            String[] targets = _contextHandler.getProtectedTargets();
            int length = (targets==null?0:targets.length);
            
            String[] updatedTargets = null;
            if (targets != null)
            {
                updatedTargets = new String[length+OSGiWebappConstants.DEFAULT_PROTECTED_OSGI_TARGETS.length];
                System.arraycopy(targets, 0, updatedTargets, 0, length);
                
            }
            else
                updatedTargets = new String[OSGiWebappConstants.DEFAULT_PROTECTED_OSGI_TARGETS.length];
            System.arraycopy(OSGiWebappConstants.DEFAULT_PROTECTED_OSGI_TARGETS, 0, updatedTargets, length, OSGiWebappConstants.DEFAULT_PROTECTED_OSGI_TARGETS.length);
            _contextHandler.setProtectedTargets(updatedTargets);
           
        }


        private Resource getFileAsResource (String dir, String file)
        {
            Resource r = null;
            try
            {
                File asFile = new File (dir, file);
                if (asFile.exists())
                    r = Resource.newResource(asFile);
            }
            catch (Exception e)
            {
                r = null;
            } 
            return r;
        }
        
        private Resource getFileAsResource (String file)
        {
            Resource r = null;
            try
            {
                File asFile = new File (file);
                if (asFile.exists())
                    r = Resource.newResource(asFile);
            }
            catch (Exception e)
            {
                r = null;
            } 
            return r;
        }
        
        private Resource getFileAsResource (File dir, String file)
        {
            Resource r = null;
            try
            {
                File asFile = new File (dir, file);
                if (asFile.exists())
                    r = Resource.newResource(asFile);
            } 
            catch (Exception e)
            {
                r = null;
            } 
            return r;
        }
    }
    
    /* ------------------------------------------------------------ */
    public AbstractContextProvider(ServerInstanceWrapper wrapper)
    {
        _serverWrapper = wrapper;
    }
    
    
    /* ------------------------------------------------------------ */
    public ServerInstanceWrapper getServerInstanceWrapper()
    {
        return _serverWrapper;
    }
    
    /* ------------------------------------------------------------ */
    /** 
     * @see org.eclipse.jetty.deploy.AppProvider#createContextHandler(org.eclipse.jetty.deploy.App)
     */
    public ContextHandler createContextHandler(App app) throws Exception
    {
        if (app == null)
            return null;
        if (!(app instanceof OSGiApp))
            throw new IllegalStateException(app+" is not a BundleApp");
        
        //Create a ContextHandler suitable to deploy in OSGi
        ContextHandler h = ((OSGiApp)app).createContextHandler();           
        return h;
    }
    
    /* ------------------------------------------------------------ */
    public void setDeploymentManager(DeploymentManager deploymentManager)
    {
        _deploymentManager = deploymentManager;
    }
    
    /* ------------------------------------------------------------ */
    public DeploymentManager getDeploymentManager()
    {
        return _deploymentManager;
    }
}
