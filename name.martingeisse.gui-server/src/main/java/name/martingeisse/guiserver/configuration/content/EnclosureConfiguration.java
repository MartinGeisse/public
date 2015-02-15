/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.configuration.content;

import name.martingeisse.guiserver.gui.FirstChildEnclosureContainer;
import name.martingeisse.guiserver.xmlbind.element.BindComponentElement;
import name.martingeisse.guiserver.xmlbind.result.MarkupContent;

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
