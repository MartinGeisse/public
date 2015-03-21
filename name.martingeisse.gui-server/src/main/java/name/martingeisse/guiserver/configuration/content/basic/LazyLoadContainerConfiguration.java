/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.configuration.content.basic;

import name.martingeisse.guiserver.configuration.content.AbstractSingleContainerConfiguration;
import name.martingeisse.guiserver.configuration.content.ComponentGroupConfiguration;
import name.martingeisse.guiserver.xml.builder.BindElement;
import name.martingeisse.guiserver.xml.result.MarkupContent;
import name.martingeisse.wicket.component.misc.LongLoadingContainer;

import org.apache.wicket.MarkupContainer;

/**
 * A lazy-loading container.
 */
@BindElement(localName = "lazy", acceptsMarkupContent = true)
public final class LazyLoadContainerConfiguration extends AbstractSingleContainerConfiguration {

	/**
	 * Constructor.
	 * @param markupContent the markup content
	 */
	public LazyLoadContainerConfiguration(MarkupContent<ComponentGroupConfiguration> markupContent) {
		super(markupContent);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.configuration.content.AbstractContainerConfiguration#buildContainer()
	 */
	@Override
	protected MarkupContainer buildContainer() {
		return new LongLoadingContainer(getComponentId());
	}

}
