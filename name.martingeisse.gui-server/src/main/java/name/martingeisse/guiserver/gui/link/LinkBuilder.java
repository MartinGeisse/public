/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.gui.link;

import name.martingeisse.guiserver.configuration.ConfigurationDefinedPageMounter;
import name.martingeisse.guiserver.configuration.content.LinkConfiguration;
import name.martingeisse.guiserver.gui.ConfigurationDefinedPage;
import name.martingeisse.wicket.component.misc.InvisibleWebMarkupContainer;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 * Builds link components from {@link LinkConfiguration} objects. To allow
 * maximum flexibility, the type of returned component is just
 * {@link WebMarkupContainer}.
 */
public class LinkBuilder {

	/**
	 * Prevent instantiation.
	 */
	private LinkBuilder() {
	}

	/**
	 * Builds a link from the specified configuration. The link should be attached to
	 * markup that has the same component structure as a {@link SimpleLinkPanel}.
	 * 
	 * Most user-defined links are built by just using a {@link SimpleLinkPanel}. In
	 * some cases however it is useful to provide customized markup. In such cases,
	 * this method can be used to build the actual link, so these links have the
	 * same flexibility in finding the link target.
	 * 
	 * @param id the wicket id
	 * @param configuration the configuration. May be null to hide the link.
	 * @return the link
	 */
	public static WebMarkupContainer buildLink(String id, LinkConfiguration configuration) {
		if (configuration == null) {
			return new InvisibleWebMarkupContainer(id);
		} else {
			PageParameters targetPageParameters = new PageParameters();
			targetPageParameters.add(ConfigurationDefinedPageMounter.CONFIGURATION_PATH_PAGE_PARAMETER_NAME, configuration.getTargetPageConfigurationPath());
			Link<Void> link = new BookmarkablePageLink<>(id, ConfigurationDefinedPage.class, targetPageParameters);
			link.add(new Label("label", configuration.getLabel()));
			return link;
		}
	}
	
}
