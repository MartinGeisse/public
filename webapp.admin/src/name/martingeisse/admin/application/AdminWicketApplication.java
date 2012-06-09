/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.application;

import name.martingeisse.admin.common.Dummy;
import name.martingeisse.admin.pages.HomePage;
import name.martingeisse.admin.schema.ApplicationSchema;
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
		super.init();
		
		// initialize the application schema
		ApplicationConfiguration.seal();
		ApplicationConfiguration.initializeCapabilities();
		ApplicationSchema.initialize();
		
		// mount page URLs
//		mountPage("user_settings", UserSettingsPage.class);
		mountPage("test", HomePage.class);

		// mount resource URLs
		mountResource("common.css", new PackageResourceReference(Dummy.class, "common.css"));
		
		// register string resource loaders
//		getResourceSettings().getStringResourceLoaders().add(new ClassStringResourceLoader(Market.class));
//		getResourceSettings().getStringResourceLoaders().add(new ClassStringResourceLoader(Language.class));
		
		// run application-specific code
		ApplicationConfiguration.fireOnAfterApplicationInitialized();
		
		// some further initialization needed after custom code
		ApplicationConfiguration.getCapabilities().initializeNavigationBackMappers();
		
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
