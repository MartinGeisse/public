/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.configurationNew.content;

import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;

/**
 * Base class for {@link MarkupContainer} component configurations.
 */
public abstract class AbstractContainerConfiguration extends AbstractComponentConfiguration {

	/**
	 * the children
	 */
	private final ComponentConfigurationList children;

	/**
	 * Constructor.
	 * @param id the wicket id
	 * @param children the children
	 */
	public AbstractContainerConfiguration(String id, ComponentConfigurationList children) {
		super(id);
		this.children = children;
	}

	/**
	 * Getter method for the children.
	 * @return the children
	 */
	public final ComponentConfigurationList getChildren() {
		return children;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.configurationNew.content.ComponentConfiguration#buildComponent()
	 */
	@Override
	public Component buildComponent() {
		MarkupContainer container = buildContainer();
		children.buildAndAddComponents(container);
		return container;
	}
	
	/**
	 * Builds the container itself, not adding any children.
	 * @return the container
	 */
	protected abstract MarkupContainer buildContainer();
	
}
