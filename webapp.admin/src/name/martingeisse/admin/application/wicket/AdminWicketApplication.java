/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.application.wicket;

import name.martingeisse.admin.application.ApplicationConfiguration;
import name.martingeisse.admin.application.security.SecurityConfigurationUtil;
import name.martingeisse.admin.component.page.AbstractAdminPage;
import name.martingeisse.admin.component.page.HomePage;
import name.martingeisse.admin.component.page.images.Dummy;
import name.martingeisse.admin.entity.schema.ApplicationSchema;
import name.martingeisse.admin.navigation.NavigationConfigurationUtil;
import name.martingeisse.admin.readonly.ReadOnlyRenderingConfigurationUtil;
import name.martingeisse.wicket.application.AbstractMyWicketApplication;
import name.martingeisse.wicket.util.json.JsonEncodingContainer;

import org.apache.log4j.Logger;
import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.Page;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.WicketTag;
import org.apache.wicket.markup.parser.filter.WicketTagIdentifier;
import org.apache.wicket.markup.resolver.IComponentResolver;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.IExceptionMapper;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.util.IProvider;

/**
 * Wicket {@link WebApplication} implementation for this application.
 */
public class AdminWicketApplication extends AbstractMyWicketApplication {

	/**
	 * the exceptionMapper
	 */
	private ExceptionMapper exceptionMapper = new ExceptionMapper();
	
	/**
	 * the exceptionMapperProvider
	 */
	private IProvider<IExceptionMapper> exceptionMapperProvider = new IProvider<IExceptionMapper>() {
		@Override
		public IExceptionMapper get() {
			return exceptionMapper;
		}
	};
	
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

		// register custom wicket:* tags
		logger.trace("registering custom tags...");
		WicketTagIdentifier.registerWellKnownTagName("json");
		getPageSettings().addComponentResolver(new JsonComponentResolver());
		logger.trace("custom tags registered");
		
		// The default encryption code in Wicket use a fixed key and a fixed salt!
		// We currently don't need encryption since we don't use "remember me"
		// cookies nor URL encryption. If encryption IS used in the future,
		// however, we should absolutely generate our own key and salt and
		// maybe even use per-session generated keys.
		logger.trace("registering Wicket encryption strategy...");
		getSecuritySettings().setCryptFactory(new BrokenCryptFactory());
		logger.trace("Wicket encryption strategy registered");

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
		logger.trace("running post-schema initialization...");
		ReadOnlyRenderingConfigurationUtil.prepareConfiguration();
		NavigationConfigurationUtil.getNavigationTree().prepare();
		logger.trace("post-schema initialization finished");

		// some more Wicket configuration
		getApplicationSettings().setPageExpiredErrorPage(HomePage.class);
		getMarkupSettings().setDefaultBeforeDisabledLink("<span class=\"disabled-link\">");
		getMarkupSettings().setDefaultAfterDisabledLink("</span>");
		getSecuritySettings().setAuthenticationStrategy(WicketConfigurationUtil.determineEffectiveWicketAuthenticationStrategy());

		// mount resource URLs
		logger.trace("mounting resource URLs...");
		mountResources(AbstractAdminPage.class, "", "common.css", "common.js", "jquery.dataTables.css");
		mountResources(Dummy.class, "images/", "back_enabled.png", "forward_disabled.png", "forward_enabled_hover.png", "sort_asc_disabled.png", "sort_desc.png", "back_disabled.png", "back_enabled_hover.png", "forward_enabled.png", "sort_asc.png", "sort_both.png", "sort_desc_disabled.png");
		logger.trace("resource URLs mounted");

		// mount navigation URLs
		logger.trace("mounting navigation URLs...");
		NavigationConfigurationUtil.getNavigationTree().mountRequestMappers(this);
		logger.trace("navigation URLs mounted");

		// mount other URLs
		logger.trace("mounting misc URLs...");
		mountPage("/login", SecurityConfigurationUtil.getSecurityConfiguration().getLoginPageClass());
		logger.trace("misc URLs mounted");
		
		// let plugins contribute
		logger.trace("invoking web application initialization contributors...");
		WicketConfigurationUtil.invokeWebApplicationInitializationContributors(this);
		logger.trace("application initialization contributors invoked");

		// add fallback string loaders
		getResourceSettings().getStringResourceLoaders().add(new PrefixedIdentityStringResourceLoader("schema.entity."));
		
		// redirect to the login page if not logged in
		getRequestCycleListeners().add(new LoginRequestCycleListener());

		logger.debug("AdminWicketApplication.init(): end");
	}

	/**
	 * Mounts multiple resources using their file name.
	 * @param anchorClass the class that specifies the package of the resources to mount.
	 * @param prefix the URL prefix
	 * @param names the file names of the resources
	 */
	public void mountResources(final Class<?> anchorClass, final String prefix, final String... names) {
		for (final String name : names) {
			mountResource(prefix + name, new PackageResourceReference(anchorClass, name));
		}
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
	
	/* (non-Javadoc)
	 * @see org.apache.wicket.Application#getExceptionMapperProvider()
	 */
	@Override
	public IProvider<IExceptionMapper> getExceptionMapperProvider() {
		return exceptionMapperProvider;
	}

	/**
	 * This resolver knows wicket:json tags and adds a {@link JsonEncodingContainer} for them.
	 */
	private static class JsonComponentResolver implements IComponentResolver {

		/* (non-Javadoc)
		 * @see org.apache.wicket.markup.resolver.IComponentResolver#resolve(org.apache.wicket.MarkupContainer, org.apache.wicket.markup.MarkupStream, org.apache.wicket.markup.ComponentTag)
		 */
		@Override
		public Component resolve(final MarkupContainer container, final MarkupStream markupStream, final ComponentTag tag) {
			if (tag instanceof WicketTag) {
				final WicketTag wicketTag = (WicketTag)tag;
				if ("json".equalsIgnoreCase(wicketTag.getName()) && (wicketTag.getNamespace() != null)) {
					final String id = tag.getId() + "-" + container.getPage().getAutoIndex();
					return new JsonEncodingContainer(id);
				}
			}
			return null;
		}

	}

}
