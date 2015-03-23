/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.configuration.content.basic;

import name.martingeisse.guiserver.configuration.content.AbstractSingleContainerConfiguration;
import name.martingeisse.guiserver.gui.FirstChildEnclosureContainer;
import name.martingeisse.guiserver.xml.builder.RegisterComponentElement;
import name.martingeisse.guiserver.xml.builder.StructuredElement;

import org.apache.wicket.MarkupContainer;

/**
 * Configuration for a (wicket:enclosure)-like container.
 */
@StructuredElement
@RegisterComponentElement(localName = "enclosure")
public final class EnclosureConfiguration extends AbstractSingleContainerConfiguration {

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.configuration.content.AbstractContainerConfiguration#buildContainer()
	 */
	@Override
	protected MarkupContainer buildContainer() {
		return new FirstChildEnclosureContainer(getComponentId());
	}

}
