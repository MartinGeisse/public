/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.configuration.content.basic;

import name.martingeisse.guiserver.configuration.content.AbstractSingleContainerConfiguration;
import name.martingeisse.guiserver.configuration.content.ComponentGroupConfiguration;
import name.martingeisse.guiserver.gui.FirstChildEnclosureContainer;
import name.martingeisse.guiserver.xml.element.BindComponentElement;
import name.martingeisse.guiserver.xml.result.MarkupContent;

import org.apache.wicket.MarkupContainer;

/**
 * Configuration for a (wicket:enclosure)-like container.
 */
@BindComponentElement(localName = "enclosure", acceptsMarkupContent = true)
public final class EnclosureConfiguration extends AbstractSingleContainerConfiguration {

	/**
	 * Constructor.
	 * @param markupContent the markup content
	 */
	public EnclosureConfiguration(MarkupContent<ComponentGroupConfiguration> markupContent) {
		super(markupContent);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.configuration.content.AbstractContainerConfiguration#buildContainer()
	 */
	@Override
	protected MarkupContainer buildContainer() {
		return new FirstChildEnclosureContainer(getComponentId());
	}

}
