/**
 * Copyright (c) 2015 Martin Geisse
 */

package name.martingeisse.guiserver.gui.navbar;

import name.martingeisse.guiserver.configuration.content.ContentElementConfiguration;
import name.martingeisse.guiserver.configuration.content.HtmlContentConfiguration;
import name.martingeisse.guiserver.configuration.content.LinkConfiguration;
import name.martingeisse.guiserver.gui.ContentElementComponentBuilder;
import name.martingeisse.guiserver.gui.link.SimpleLinkPanel;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;

/**
 * The implementation of {@link ContentElementComponentBuilder} used for navigation
 * bar elements.
 */
public final class NavigationBarContentElementComponentBuilder implements ContentElementComponentBuilder {

	/**
	 * The shared instance of this class.
	 */
	public static final NavigationBarContentElementComponentBuilder INSTANCE = new NavigationBarContentElementComponentBuilder();
	
	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.gui.ContentElementComponentBuilder#buildComponent(java.lang.String, name.martingeisse.guiserver.configuration.content.ContentElementConfiguration)
	 */
	@Override
	public Component buildComponent(String id, ContentElementConfiguration configuration) {
		if (id == null) {
			throw new IllegalArgumentException("id argument is null");
		}
		if (configuration == null) {
			throw new IllegalArgumentException("configuration argument is null");
		}
		if (configuration instanceof HtmlContentConfiguration) {
			HtmlContentConfiguration config = (HtmlContentConfiguration)configuration;
			return new Label(id, config.getHtml()).setEscapeModelStrings(false);
		} else if (configuration instanceof LinkConfiguration) {
			return new SimpleLinkPanel(id, (LinkConfiguration)configuration);
		} else {
			throw new RuntimeException("the NavigationBarContentElementComponentBuilder doesn't understand a " + configuration.getClass());
		}
	}

}
