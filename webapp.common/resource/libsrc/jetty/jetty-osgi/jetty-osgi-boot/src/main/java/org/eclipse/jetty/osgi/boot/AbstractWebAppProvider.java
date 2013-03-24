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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;


import org.eclipse.jetty.deploy.App;
import org.eclipse.jetty.deploy.AppProvider;
import org.eclipse.jetty.deploy.DeploymentManager;
import org.eclipse.jetty.osgi.boot.internal.serverfactory.ServerInstanceWrapper;
import org.eclipse.jetty.osgi.boot.internal.webapp.BundleFileLocatorHelperFactory;
import org.eclipse.jetty.osgi.boot.internal.webapp.OSGiWebappClassLoader;
import org.eclipse.jetty.osgi.boot.utils.EventSender;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.util.component.AbstractLifeCycle;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;
import org.eclipse.jetty.webapp.WebAppContext;
import org.eclipse.jetty.xml.XmlConfiguration;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.packageadmin.PackageAdmin;




/**
 * AbstractWebAppProvider
 *
 *
 */
public abstract class AbstractWebAppProvider extends AbstractLifeCycle implements AppProvider
{
    private static final Logger LOG = Log.getLogger(AbstractWebAppProvider.class);
    
    public static String __defaultConfigurations[] = {
                                                            "org.eclipse.jetty.osgi.boot.OSGiWebInfConfiguration",
                                                            "org.eclipse.jetty.webapp.WebXmlConfiguration",
                                                            "org.eclipse.jetty.osgi.boot.OSGiMetaInfConfiguration",
                                                            "org.eclipse.jetty.webapp.FragmentConfiguration",
                                                            "org.eclipse.jetty.webapp.JettyWebXmlConfiguration"//,
                                                            //"org.eclipse.jetty.osgi.boot.jsp.TagLibOSGiConfiguration"                            
                                                     };
    
    public static void setDefaultConfigurations (String[] defaultConfigs)
    {
        __defaultConfigurations = defaultConfigs;
    }
    
    public static String[] getDefaultConfigurations ()
    {
        return __defaultConfigurations;
    }
    

    private boolean _parentLoaderPriority;

    private String _defaultsDescriptor;

    private boolean _extractWars = true; //See WebAppContext.extractWars

    private String _tldBundles;
    
    private DeploymentManager _deploymentManager;
    
    private String[] _configurationClasses;
    
    private ServerInstanceWrapper _serverWrapper;
    
    /* ------------------------------------------------------------ */
    /**
     * OSGiApp
     *
     *
     */
    public class OSGiApp extends AbstractOSGiApp
    {
        private String _contextPath;
        private String _webAppPath;
        private WebAppContext _webApp;

        public OSGiApp(DeploymentManager manager, AppProvider provider, Bundle bundle, String originId)
        {
            super(manager, provider, bundle, originId);
        }
        
        public OSGiApp(DeploymentManager manager, AppProvider provider, Bundle bundle, Dictionary properties, String originId)
        {
            super(manager, provider, bundle, properties, originId);
        }
     
        public void setWebAppContext (WebAppContext webApp)
        {
            _webApp = webApp;
        }

        public String getContextPath()
        {
            return _contextPath;
        }

        public void setContextPath(String contextPath)
        {
            this._contextPath = contextPath;
        }

        public String getBundlePath()
        {
            return _webAppPath;
        }

        public void setWebAppPath(String path)
        {
            this._webAppPath = path;
        }
        
        
        public ContextHandler createContextHandler()
        throws Exception
        {
            if (_webApp != null)
            {
                configureWebApp();
                return _webApp;
            }
            
            createWebApp();
            return _webApp;
        }
        
      
        
        protected void createWebApp ()
        throws Exception
        {
            _webApp = newWebApp();
            configureWebApp();
        }
        
        protected WebAppContext newWebApp ()
        {
            WebAppContext webApp = new WebAppContext();
            webApp.setAttribute(OSGiWebappConstants.WATERMARK, OSGiWebappConstants.WATERMARK);

            //make sure we protect also the osgi dirs specified by OSGi Enterprise spec
            String[] targets = webApp.getProtectedTargets();
            String[] updatedTargets = null;
            if (targets != null)
            {
                updatedTargets = new String[targets.length+OSGiWebappConstants.DEFAULT_PROTECTED_OSGI_TARGETS.length];
                System.arraycopy(targets, 0, updatedTargets, 0, targets.length);
            }
            else
                updatedTargets = new String[OSGiWebappConstants.DEFAULT_PROTECTED_OSGI_TARGETS.length];
            System.arraycopy(OSGiWebappConstants.DEFAULT_PROTECTED_OSGI_TARGETS, 0, updatedTargets, targets.length, OSGiWebappConstants.DEFAULT_PROTECTED_OSGI_TARGETS.length);
            webApp.setProtectedTargets(updatedTargets);

           return webApp;
        }


        public void configureWebApp() 
        throws Exception
        {                     
            //TODO turn this around and let any context.xml file get applied first, and have the properties override
            _webApp.setContextPath(_contextPath);
            
            //osgi Enterprise Spec r4 p.427
            _webApp.setAttribute(OSGiWebappConstants.OSGI_BUNDLECONTEXT, _bundle.getBundleContext());

            String overrideBundleInstallLocation = (String)_properties.get(OSGiWebappConstants.JETTY_BUNDLE_INSTALL_LOCATION_OVERRIDE);
            File bundleInstallLocation = 
                (overrideBundleInstallLocation == null 
                        ? BundleFileLocatorHelperFactory.getFactory().getHelper().getBundleInstallLocation(_bundle) 
                        : new File(overrideBundleInstallLocation));
            URL url = null;

            //if the path wasn't set or it was ., then it is the root of the bundle's installed location
            if (_webAppPath == null || _webAppPath.length() == 0 || ".".equals(_webAppPath))
            {
                url = bundleInstallLocation.toURI().toURL();
            }
            else
            {
                //Get the location of the root of the webapp inside the installed bundle
                if (_webAppPath.startsWith("/") || _webAppPath.startsWith("file:"))
                {
                    url = new File(_webAppPath).toURI().toURL();
                }
                else if (bundleInstallLocation != null && bundleInstallLocation.isDirectory())
                {
                    url = new File(bundleInstallLocation, _webAppPath).toURI().toURL();
                }
                else if (bundleInstallLocation != null)
                {
                    Enumeration<URL> urls = BundleFileLocatorHelperFactory.getFactory().getHelper().findEntries(_bundle, _webAppPath);
                    if (urls != null && urls.hasMoreElements())
                        url = urls.nextElement();
                }
            }

            if (url == null)
            { 
                throw new IllegalArgumentException("Unable to locate " + _webAppPath
                                                   + " in "
                                                   + (bundleInstallLocation != null ? bundleInstallLocation.getAbsolutePath() : "unlocated bundle '" + _bundle.getSymbolicName()+ "'"));
            }

            // converts bundleentry: protocol if necessary
            _webApp.setWar(BundleFileLocatorHelperFactory.getFactory().getHelper().getLocalURL(url).toString());

            // Set up what has been configured on the provider
            _webApp.setParentLoaderPriority(isParentLoaderPriority());
            _webApp.setExtractWAR(isExtract());
            if (getConfigurationClasses() != null)
                _webApp.setConfigurationClasses(getConfigurationClasses());
            else
                _webApp.setConfigurationClasses(__defaultConfigurations);

            if (getDefaultsDescriptor() != null)
                _webApp.setDefaultsDescriptor(getDefaultsDescriptor());

            //Set up configuration from manifest headers
            //extra classpath
            String tmp = (String)_properties.get(OSGiWebappConstants.JETTY_EXTRA_CLASSPATH);
            if (tmp != null)
                _webApp.setExtraClasspath(tmp);

            //web.xml
            tmp = (String)_properties.get(OSGiWebappConstants.JETTY_WEB_XML_PATH);
            if (tmp != null && tmp.trim().length() != 0)
            {
                File webXml = getFile (tmp, bundleInstallLocation);
                if (webXml != null && webXml.exists())
                    _webApp.setDescriptor(webXml.getAbsolutePath());
            }

            //webdefault.xml
            tmp = (String)_properties.get(OSGiWebappConstants.JETTY_DEFAULT_WEB_XML_PATH);
            if (tmp != null)
            {
                File defaultWebXml = getFile (tmp, bundleInstallLocation);
                if (defaultWebXml != null && defaultWebXml.exists())
                    _webApp.setDefaultsDescriptor(defaultWebXml.getAbsolutePath());
            }

            //Handle Require-TldBundle
            //This is a comma separated list of names of bundles that contain tlds that this webapp uses.
            //We add them to the webapp classloader.
            String requireTldBundles = (String)_properties.get(OSGiWebappConstants.REQUIRE_TLD_BUNDLE);
            String pathsToTldBundles = getPathsToRequiredBundles(requireTldBundles);


            // make sure we provide access to all the jetty bundles by going
            // through this bundle.
            OSGiWebappClassLoader webAppLoader = new OSGiWebappClassLoader(_serverWrapper.getParentClassLoaderForWebapps(), _webApp, _bundle);

            if (pathsToTldBundles != null)
                webAppLoader.addClassPath(pathsToTldBundles);
            _webApp.setClassLoader(webAppLoader);


            // apply any META-INF/context.xml file that is found to configure
            // the webapp first
            applyMetaInfContextXml();

            // pass the value of the require tld bundle so that the TagLibOSGiConfiguration
            // can pick it up.
            _webApp.setAttribute(OSGiWebappConstants.REQUIRE_TLD_BUNDLE, requireTldBundles);

            //Set up some attributes
            // rfc66
            _webApp.setAttribute(OSGiWebappConstants.RFC66_OSGI_BUNDLE_CONTEXT, _bundle.getBundleContext());

            // spring-dm-1.2.1 looks for the BundleContext as a different attribute.
            // not a spec... but if we want to support
            // org.springframework.osgi.web.context.support.OsgiBundleXmlWebApplicationContext
            // then we need to do this to:
            _webApp.setAttribute("org.springframework.osgi.web." + BundleContext.class.getName(), _bundle.getBundleContext());

            // also pass the bundle directly. sometimes a bundle does not have a
            // bundlecontext.
            // it is still useful to have access to the Bundle from the servlet
            // context.
            _webApp.setAttribute(OSGiWebappConstants.JETTY_OSGI_BUNDLE, _bundle);
        }

        protected String getPathsToRequiredBundles (String requireTldBundles)
        throws Exception
        {
            if (requireTldBundles == null) return null;

            ServiceReference ref = _bundle.getBundleContext().getServiceReference(org.osgi.service.packageadmin.PackageAdmin.class.getName());
            PackageAdmin packageAdmin = (ref == null) ? null : (PackageAdmin)_bundle.getBundleContext().getService(ref);
            if (packageAdmin == null)
                throw new IllegalStateException("Unable to get PackageAdmin reference to locate required Tld bundles");

            StringBuilder paths = new StringBuilder();         
            String[] symbNames = requireTldBundles.split(", ");

            for (String symbName : symbNames)
            {
                Bundle[] bs = packageAdmin.getBundles(symbName, null);
                if (bs == null || bs.length == 0) 
                { 
                    throw new IllegalArgumentException("Unable to locate the bundle '" + symbName
                                                       + "' specified by "
                                                       + OSGiWebappConstants.REQUIRE_TLD_BUNDLE
                                                       + " in manifest of "
                                                       + (_bundle == null ? "unknown" : _bundle.getSymbolicName())); 
                }

                File f = BundleFileLocatorHelperFactory.getFactory().getHelper().getBundleInstallLocation(bs[0]);
                if (paths.length() > 0) paths.append(", ");
                paths.append(f.toURI().toURL().toString());
                LOG.debug("getPathsToRequiredBundles: bundle path=" + bs[0].getLocation() + " uri=" + f.toURI());
            }

            return paths.toString();
        }
        
        
        protected void applyMetaInfContextXml()
        throws Exception
        {
            if (_bundle == null) return;
            if (_webApp == null) return;

            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            LOG.debug("Context classloader = " + cl);
            try
            {
               
                Thread.currentThread().setContextClassLoader(_webApp.getClassLoader());

                //TODO replace this with getting the InputStream so we don't cache in URL
                // find if there is a META-INF/context.xml file
                URL contextXmlUrl = _bundle.getEntry("/META-INF/jetty-webapp-context.xml");
                if (contextXmlUrl == null) return;

                // Apply it just as the standard jetty ContextProvider would do
                LOG.info("Applying " + contextXmlUrl + " to " + _webApp);

                XmlConfiguration xmlConfiguration = new XmlConfiguration(contextXmlUrl);
                HashMap properties = new HashMap();
                properties.put("Server", getDeploymentManager().getServer());
                xmlConfiguration.getProperties().putAll(properties);
                xmlConfiguration.configure(_webApp);
            }
            finally
            {
                Thread.currentThread().setContextClassLoader(cl);
            }
        }
        
        private File getFile (String file, File bundleInstall)
        {
            if (file == null)
                return null;
            
            if (file.startsWith("/") || file.startsWith("file:/"))
                return new File(file);
            else
                return new File(bundleInstall, file);
        }
    }
   
    /* ------------------------------------------------------------ */
    public AbstractWebAppProvider (ServerInstanceWrapper wrapper)
    {
        _serverWrapper = wrapper;
    }
    
    
    
    /* ------------------------------------------------------------ */
    /**
     * Get the parentLoaderPriority.
     * 
     * @return the parentLoaderPriority
     */
    public boolean isParentLoaderPriority()
    {
        return _parentLoaderPriority;
    }

    /* ------------------------------------------------------------ */
    /**
     * Set the parentLoaderPriority.
     * 
     * @param parentLoaderPriority the parentLoaderPriority to set
     */
    public void setParentLoaderPriority(boolean parentLoaderPriority)
    {
        _parentLoaderPriority = parentLoaderPriority;
    }

    /* ------------------------------------------------------------ */
    /**
     * Get the defaultsDescriptor.
     * 
     * @return the defaultsDescriptor
     */
    public String getDefaultsDescriptor()
    {
        return _defaultsDescriptor;
    }

    /* ------------------------------------------------------------ */
    /**
     * Set the defaultsDescriptor.
     * 
     * @param defaultsDescriptor the defaultsDescriptor to set
     */
    public void setDefaultsDescriptor(String defaultsDescriptor)
    {
        _defaultsDescriptor = defaultsDescriptor;
    }
    
    
    /* ------------------------------------------------------------ */
    public boolean isExtract()
    {
        return _extractWars;
    }
    
    
    /* ------------------------------------------------------------ */
    public void setExtract(boolean extract)
    {
        _extractWars = extract;
    }
    
    
    /* ------------------------------------------------------------ */
    /**
     * @param tldBundles Comma separated list of bundles that contain tld jars
     *            that should be setup on the jetty instances created here.
     */
    public void setTldBundles(String tldBundles)
    {
        _tldBundles = tldBundles;
    }
    
    
    /* ------------------------------------------------------------ */
    /**
     * @return The list of bundles that contain tld jars that should be setup on
     *         the jetty instances created here.
     */
    public String getTldBundles()
    {
        return _tldBundles;
    }
    
    /* ------------------------------------------------------------ */
    /**
     * @param configurations The configuration class names.
     */
    public void setConfigurationClasses(String[] configurations)
    {
        _configurationClasses = configurations == null ? null : (String[]) configurations.clone();
    }

    /* ------------------------------------------------------------ */
    /**
     * 
     */
    public String[] getConfigurationClasses()
    {
        return _configurationClasses;
    }

    /* ------------------------------------------------------------ */
    public void setServerInstanceWrapper(ServerInstanceWrapper wrapper)
    {
        _serverWrapper = wrapper;
    }

    public ServerInstanceWrapper getServerInstanceWrapper()
    {
        return _serverWrapper;
    }

    /* ------------------------------------------------------------ */
    /**
     * @return
     */
    public DeploymentManager getDeploymentManager()
    {
        return _deploymentManager;
    }

    /* ------------------------------------------------------------ */
    /** 
     * @see org.eclipse.jetty.deploy.AppProvider#setDeploymentManager(org.eclipse.jetty.deploy.DeploymentManager)
     */
    public void setDeploymentManager(DeploymentManager deploymentManager)
    {
        _deploymentManager = deploymentManager;
    }
    
    
    /* ------------------------------------------------------------ */
    public ContextHandler createContextHandler(App app) throws Exception
    {
        if (app == null)
            return null;
        if (!(app instanceof OSGiApp))
            throw new IllegalStateException(app+" is not a BundleApp");

        //Create a WebAppContext suitable to deploy in OSGi
        ContextHandler ch = ((OSGiApp)app).createContextHandler();
        return ch;
    }

    
    /* ------------------------------------------------------------ */
    public static String getOriginId(Bundle contributor, String path)
    {
        return contributor.getSymbolicName() + "-" + contributor.getVersion().toString() + (path.startsWith("/") ? path : "/" + path);
    }
    
}
