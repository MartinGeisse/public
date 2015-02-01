/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.gui.link;

import name.martingeisse.guiserver.configuration.content.LinkConfiguration;

import org.apache.wicket.markup.html.panel.Panel;

/**
 * Renders a link from a {@link LinkConfiguration} directly to the page.
 */
public class SimpleLinkPanel extends Panel {

	/**
	 * Constructor.
	 * @param id the wicket id
	 * @param configuration the configuration
	 */
	public SimpleLinkPanel(String id, LinkConfiguration configuration) {
		super(id);
		// PageConfiguration targetPageConfiguration = Configuration.getInstance().getElementAbsolute(configuration.getTargetPageConfigurationPath(), PageConfiguration.class);
		add(LinkBuilder.buildLink("link", configuration));
	}
	
}
