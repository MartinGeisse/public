/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.configuration.content.basic;

import name.martingeisse.guiserver.configuration.content.AbstractContainerConfiguration;
import name.martingeisse.guiserver.configuration.content.ComponentConfiguration;
import name.martingeisse.guiserver.gui.FirstChildEnclosureContainer;
import name.martingeisse.guiserver.xml.element.BindComponentElement;
import name.martingeisse.guiserver.xml.result.MarkupContent;

import org.apache.wicket.MarkupContainer;

/**
 * Configuration for a (wicket:enclosure)-like container.
 */
@BindComponentElement(localName = "enclosure", acceptsMarkupContent = true)
public final class EnclosureConfiguration extends AbstractContainerConfiguration {

	/**
	 * Constructor.
	 * @param markupContent the markup content
	 */
	public EnclosureConfiguration(MarkupContent<ComponentConfiguration> markupContent) {
		super(markupContent);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.configuration.content.AbstractContainerConfiguration#buildContainer()
	 */
	@Override
	protected MarkupContainer buildContainer() {
		return new FirstChildEnclosureContainer(getId());
	}

}
