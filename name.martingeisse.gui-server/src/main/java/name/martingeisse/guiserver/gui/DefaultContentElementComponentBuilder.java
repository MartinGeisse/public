/**
 * Copyright (c) 2015 Martin Geisse
 */

package name.martingeisse.guiserver.gui;

import name.martingeisse.guiserver.configuration.content.ContentElementConfiguration;
import name.martingeisse.guiserver.configuration.content.HtmlContentConfiguration;
import name.martingeisse.guiserver.configuration.content.NavigationBarContentConfiguration;
import name.martingeisse.guiserver.gui.navbar.NavigationBar;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;

/**
 * The default implementation of {@link ContentElementComponentBuilder}.
 */
public class DefaultContentElementComponentBuilder implements ContentElementComponentBuilder {

	/**
	 * The shared instance of this class.
	 */
	public static final DefaultContentElementComponentBuilder INSTANCE = new DefaultContentElementComponentBuilder();
	
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
		} else if (configuration instanceof NavigationBarContentConfiguration) {
			return new NavigationBar(id);
		} else {
			throw new RuntimeException("this ContentElementComponentBuilder doesn't understand a " + configuration.getClass());
		}
	}

}
