/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.application.wicket;

import name.martingeisse.admin.application.ApplicationConfiguration;
import name.martingeisse.admin.common.Dummy;
import name.martingeisse.admin.entity.schema.ApplicationSchema;
import name.martingeisse.admin.navigation.NavigationConfigurationUtil;
import name.martingeisse.admin.pages.HomePage;
import name.martingeisse.admin.readonly.ReadOnlyRenderingConfigurationUtil;
import name.martingeisse.wicket.application.AbstractMyWicketApplication;

import org.apache.log4j.Logger;
import org.apache.wicket.Page;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.resource.PackageResourceReference;

/**
 * Wicket {@link WebApplication} implementation for this application.
 */
public class AdminWicketApplication extends AbstractMyWicketApplication {

	/**
	 * the logger
	 */
	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(AdminWicketApplication.class);
	
	/* (non-Javadoc)
	 * @see org.apache.wicket.protocol.http.WebApplication#init()
	 */
	@Override
	protected void init() {
		logger.debug("AdminWicketApplication.init(): begin");
		
		// superclass initialization
		logger.trace("initializing base application class...");
		super.init();
		logger.trace("base application class initialized");

		// initialize plugins and capabilities
		logger.trace("initializing ApplicationConfiguration...");
		ApplicationConfiguration.get().initialize();
		logger.trace("ApplicationConfiguration initialized");
		
		// initialize the application schema from the database
		logger.trace("initializing ApplicationSchema...");
		ApplicationSchema.initialize();
		logger.trace("ApplicationSchema initialized");

		// initialize module-specific data
		// TODO: move the code below to a module (instead of centralized) location.
		// We need a way to control the order in which initialization steps happen --
		// maybe use an event broadcasting system for that: When a step is finished,
		// it broadcasts an event; other steps that are now ready to run register
		// themselves to run (or run immediately ?); steps with multiple dependencies
		// set a flag on each event and (register to) run when all flags are set.
		logger.trace("running post-schema initialization...");
		ReadOnlyRenderingConfigurationUtil.prepareConfiguration();
		NavigationConfigurationUtil.getNavigationTree().prepare();
		logger.trace("post-schema initialization finished");
		
		// some more Wicket configuration
		getApplicationSettings().setPageExpiredErrorPage(HomePage.class);
		
		// mount resource URLs
		mountResource("common.css", new PackageResourceReference(Dummy.class, "common.css"));
		
		// mount navigation URLs
		logger.trace("mounting navigation URLs...");
		NavigationConfigurationUtil.getNavigationTree().mountRequestMappers(this);
		logger.trace("navigation URLs mounted");
		
		// let plugins contribute
		logger.trace("invoking web application initialization contributors...");
		WicketConfigurationUtil.invokeWebApplicationInitializationContributors(this);
		logger.trace("application initialization contributors invoked");
		
		// add fallback string loaders
		getResourceSettings().getStringResourceLoaders().add(new PrefixedIdentityStringResourceLoader("schema.entity."));
		
		logger.debug("AdminWicketApplication.init(): end");
	}

	/**
	 * @return the singleton instance of this class
	 */
	public static AdminWicketApplication get() {
		return (AdminWicketApplication)(WebApplication.get());
	}
	
	/* (non-Javadoc)
	 * @see org.apache.wicket.Application#getHomePage()
	 */
	@Override
	public Class<? extends Page> getHomePage() {
		return HomePage.class;
	}

}
