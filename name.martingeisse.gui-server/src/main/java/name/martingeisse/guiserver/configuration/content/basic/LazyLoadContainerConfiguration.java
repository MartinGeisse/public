/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.configuration.content.basic;

import name.martingeisse.guiserver.configuration.content.AbstractSingleContainerConfiguration;
import name.martingeisse.guiserver.xml.builder.RegisterComponentElement;
import name.martingeisse.guiserver.xml.builder.StructuredElement;
import name.martingeisse.wicket.component.misc.LongLoadingContainer;

import org.apache.wicket.MarkupContainer;

/**
 * A lazy-loading container.
 */
@StructuredElement
@RegisterComponentElement(localName = "lazy")
public final class LazyLoadContainerConfiguration extends AbstractSingleContainerConfiguration {

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.configuration.content.AbstractContainerConfiguration#buildContainer()
	 */
	@Override
	protected MarkupContainer buildContainer() {
		return new LongLoadingContainer(getComponentId());
	}

}
