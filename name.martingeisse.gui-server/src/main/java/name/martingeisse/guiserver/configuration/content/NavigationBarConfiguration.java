/**
 * Copyright (c) 2015 Martin Geisse
 */

package name.martingeisse.guiserver.configuration.content;

import name.martingeisse.guiserver.gui.NavigationBar;

import org.apache.wicket.MarkupContainer;

import com.google.common.collect.ImmutableList;

/**
 * The configuration for a navigation bar.
 */
public final class NavigationBarConfiguration extends AbstractContainerConfiguration {

	/**
	 * the brandLink
	 */
	private final ComponentConfiguration brandLink;

	/**
	 * Constructor.
	 * @param id the wicket id
	 * @param children the children
	 * @param brandLink the brand link, or null for none
	 */
	public NavigationBarConfiguration(String id, ImmutableList<ComponentConfiguration> children, ComponentConfiguration brandLink) {
		super(id, children);
		this.brandLink = brandLink;
	}

	/**
	 * Getter method for the brandLink.
	 * @return the brandLink
	 */
	public ComponentConfiguration getBrandLink() {
		return brandLink;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.configurationNew.content.AbstractContainerConfiguration#buildContainer()
	 */
	@Override
	protected MarkupContainer buildContainer() {
		return new NavigationBar(getId(), brandLink);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.configuration.content.ComponentConfiguration#accept(name.martingeisse.guiserver.configuration.content.IComponentConfigurationVisitor)
	 */
	@Override
	public void accept(IComponentConfigurationVisitor visitor) {
		if (visitor.beginVisit(this)) {
			brandLink.accept(visitor);
			getChildren().accept(visitor);
			visitor.endVisit(this);
		}
	}
	
}
