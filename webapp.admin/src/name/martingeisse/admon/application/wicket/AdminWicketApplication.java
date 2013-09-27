/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admon.application.wicket;

import name.martingeisse.admin.entity.schema.ApplicationSchema;
import name.martingeisse.admon.application.converter.DateTimeConverter;
import name.martingeisse.admon.application.converter.LocalDateConverter;
import name.martingeisse.admon.application.converter.LocalDateTimeConverter;
import name.martingeisse.admon.application.converter.LocalTimeConverter;
import name.martingeisse.admon.application.pages.AbstractAdminPage;
import name.martingeisse.admon.application.pages.HomePage;
import name.martingeisse.admon.application.pages.images.Dummy;
import name.martingeisse.admon.navigation.NavigationTree;
import name.martingeisse.admon.security.SecurityConfiguration;
import name.martingeisse.common.util.ParameterUtil;
import name.martingeisse.common.util.ReturnValueUtil;
import name.martingeisse.wicket.application.AbstractMyWicketApplication;
import name.martingeisse.wicket.util.json.JsonEncodingContainer;

import org.apache.log4j.Logger;
import org.apache.wicket.Component;
import org.apache.wicket.ConverterLocator;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.Page;
import org.apache.wicket.authentication.strategy.NoOpAuthenticationStrategy;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.WicketTag;
import org.apache.wicket.markup.parser.filter.WicketTagIdentifier;
import org.apache.wicket.markup.resolver.IComponentResolver;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.IExceptionMapper;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.util.IProvider;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;

/**
 * Wicket {@link WebApplication} implementation for this application.
 */
public class AdminWicketApplication extends AbstractMyWicketApplication {
	
	/**
	 * the logger
	 */
	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(AdminWicketApplication.class);

	/**
	 * the exceptionMapper
	 */
	private final ExceptionMapper exceptionMapper = new ExceptionMapper();
	
	/**
	 * the exceptionMapperProvider
	 */
	private final IProvider<IExceptionMapper> exceptionMapperProvider = new IProvider<IExceptionMapper>() {
		@Override
		public IExceptionMapper get() {
			return exceptionMapper;
		}
	};
	
	/**
	 * the schema
	 */
	private final ApplicationSchema schema = new ApplicationSchema();
	
	/**
	 * the navigationTree
	 */
	private final NavigationTree navigationTree = new NavigationTree();

	/**
	 * Getter method for the schema.
	 * @return the schema
	 */
	public final ApplicationSchema getSchema() {
		return schema;
	}
	
	/**
	 * Getter method for the navigationTree.
	 * @return the navigationTree
	 */
	public final NavigationTree getNavigationTree() {
		return navigationTree;
	}
	
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

		// initialize the application schema from the database
		logger.trace("initializing ApplicationSchema...");
		ApplicationSchema.initialize();
		logger.trace("ApplicationSchema initialized");

		// register type converters
		ConverterLocator converterLocator = (ConverterLocator)getConverterLocator();
		converterLocator.set(DateTime.class, new DateTimeConverter());
		converterLocator.set(LocalDateTime.class, new LocalDateTimeConverter());
		converterLocator.set(LocalDate.class, new LocalDateConverter());
		converterLocator.set(LocalTime.class, new LocalTimeConverter());

		// some more Wicket configuration
		getApplicationSettings().setPageExpiredErrorPage(HomePage.class);
		getMarkupSettings().setDefaultBeforeDisabledLink("<span class=\"disabled-link\">");
		getMarkupSettings().setDefaultAfterDisabledLink("</span>");
		getSecuritySettings().setAuthenticationStrategy(new NoOpAuthenticationStrategy());

		// mount resource URLs
		logger.trace("mounting resource URLs...");
		mountResources(AbstractAdminPage.class, "", "common.css", "common.js", "jquery.dataTables.css");
		mountResources(Dummy.class, "images/", "back_enabled.png", "forward_disabled.png", "forward_enabled_hover.png", "sort_asc_disabled.png", "sort_desc.png", "back_disabled.png", "back_enabled_hover.png", "forward_enabled.png", "sort_asc.png", "sort_both.png", "sort_desc_disabled.png");
		mountResources(Dummy.class, "images/", "ui-bg_diagonals-thick_18_b81900_40x40.png", "ui-bg_diagonals-thick_20_666666_40x40.png", "ui-bg_flat_10_000000_40x100.png", "ui-bg_glass_100_f6f6f6_1x400.png", "ui-bg_glass_100_fdf5ce_1x400.png", "ui-bg_glass_65_ffffff_1x400.png", "ui-bg_gloss-wave_35_f6a828_500x100.png", "ui-bg_highlight-soft_100_eeeeee_1x100.png", "ui-bg_highlight-soft_75_ffe45c_1x100.png", "ui-icons_222222_256x240.png", "ui-icons_228ef1_256x240.png", "ui-icons_ef8c08_256x240.png", "ui-icons_ffd27a_256x240.png", "ui-icons_ffffff_256x240.png");
		logger.trace("resource URLs mounted");

		// mount other URLs
		logger.trace("mounting misc URLs...");
		mountPage("/login", ReturnValueUtil.nullMeansMissing(SecurityConfiguration.getInstanceSafe().getLoginPageClass(), "security configuration: login page class"));
		logger.trace("misc URLs mounted");
		
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
	public final void mountResources(final Class<?> anchorClass, final String prefix, final String... names) {
		ParameterUtil.ensureNotNull(anchorClass, "anchorClass");
		ParameterUtil.ensureNotNull(prefix, "prefix");
		ParameterUtil.ensureNotNull(names, "names");
		ParameterUtil.ensureNoNullElement(names, "names");
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
