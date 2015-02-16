/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.configuration.content;

import name.martingeisse.guiserver.xml.element.BindComponentElement;
import name.martingeisse.guiserver.xml.result.MarkupContent;
import name.martingeisse.wicket.component.misc.LongLoadingContainer;

import org.apache.wicket.MarkupContainer;

/**
 * A lazy-loading container.
 */
@BindComponentElement(localName = "lazy", acceptsMarkupContent = true)
public final class LazyLoadContainerConfiguration extends AbstractContainerConfiguration {

	/**
	 * Constructor.
	 * @param markupContent the markup content
	 */
	public LazyLoadContainerConfiguration(MarkupContent<ComponentConfiguration> markupContent) {
		super(markupContent);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.configuration.content.AbstractContainerConfiguration#buildContainer()
	 */
	@Override
	protected MarkupContainer buildContainer() {
		return new LongLoadingContainer(getId());
	}

}
