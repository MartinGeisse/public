/**
 * Copyright (c) 2015 Martin Geisse
 */

package name.martingeisse.guiserver.configuration.content.bootstrap.navbar;

import name.martingeisse.guiserver.configuration.content.AbstractSingleContainerConfiguration;
import name.martingeisse.guiserver.configuration.content.ComponentGroupConfiguration;
import name.martingeisse.guiserver.configuration.content.IComponentGroupConfigurationVisitor;
import name.martingeisse.guiserver.gui.NavigationBar;
import name.martingeisse.guiserver.xml.result.MarkupContent;

import org.apache.wicket.MarkupContainer;

/**
 * The configuration for a navigation bar.
 */
public final class NavigationBarConfiguration extends AbstractSingleContainerConfiguration {

	/**
	 * the brandLink
	 */
	private final ComponentGroupConfiguration brandLink;

	/**
	 * Constructor.
	 * @param contents the contents
	 */
	public NavigationBarConfiguration(NavigationBarContents contents) {
		super(new MarkupContent<>(contents.getLinks().getConfigurations()));
		this.brandLink = contents.getBrandLink();
	}

	/**
	 * Getter method for the brandLink.
	 * @return the brandLink
	 */
	public ComponentGroupConfiguration getBrandLink() {
		return brandLink;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.configurationNew.content.AbstractContainerConfiguration#buildContainer()
	 */
	@Override
	protected MarkupContainer buildContainer() {
		return new NavigationBar(getComponentId(), brandLink);
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.configuration.content.ComponentConfiguration#accept(name.martingeisse.guiserver.configuration.content.IComponentConfigurationVisitor)
	 */
	@Override
	public void accept(IComponentGroupConfigurationVisitor visitor) {
		if (visitor.beginVisit(this)) {
			brandLink.accept(visitor);
			getChildren().accept(visitor);
			visitor.endVisit(this);
		}
	}
	
}
