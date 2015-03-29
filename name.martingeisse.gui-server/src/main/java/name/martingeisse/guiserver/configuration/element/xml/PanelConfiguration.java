/**
 * Copyright (c) 2015 Martin Geisse
 */

package name.martingeisse.guiserver.configuration.element.xml;

import name.martingeisse.guiserver.configuration.element.Element;
import name.martingeisse.guiserver.template.Template;

/**
 * The configuration for a panel.
 */
public final class PanelConfiguration extends Element {

	/**
	 * the template
	 */
	private Template template;

	/**
	 * Constructor.
	 * @param path the path to this page
	 * @param markupSourceCode the source code for the page's markup
	 */
	public PanelConfiguration(String path, Template template) {
		super(path);
		this.template = template;
	}

	/**
	 * Getter method for the template.
	 * @return the template
	 */
	public Template getTemplate() {
		return template;
	}
	
}
