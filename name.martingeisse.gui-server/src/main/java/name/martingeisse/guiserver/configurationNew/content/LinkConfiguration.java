/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.configurationNew.content;

import name.martingeisse.guiserver.configurationNew.PageConfiguration;
import name.martingeisse.guiserver.gui.ConfigurationDefinedPage;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.google.common.collect.ImmutableList;


/**
 * Configuration for a link. This class tries to cover only the common
 * cases to keep it simple.
 */
public final class LinkConfiguration extends AbstractContainerConfiguration {

	/**
	 * the targetPagePath
	 */
	private final String targetPagePath;

	/**
	 * Constructor.
	 * 
	 * @param id the wicket id
	 * @param children the children
	 * @param targetPagePath the path of the page to link to
	 */
	public LinkConfiguration(String id, ImmutableList<ComponentConfiguration> children, String targetPagePath) {
		super(id, children);
		this.targetPagePath = targetPagePath;
	}

	/**
	 * Constructor.
	 * 
	 * @param id the wicket id
	 * @param children the children
	 * @param targetPagePath the path of the page to link to
	 */
	public LinkConfiguration(String id, ComponentConfigurationList children, String targetPagePath) {
		super(id, children);
		this.targetPagePath = targetPagePath;
	}

	/**
	 * Getter method for the targetPagePath.
	 * @return the targetPagePath
	 */
	public String getTargetPagePath() {
		return targetPagePath;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.configurationNew.content.AbstractContainerConfiguration#buildContainer()
	 */
	@Override
	protected MarkupContainer buildContainer() {
		PageParameters targetPageParameters = new PageParameters();
		targetPageParameters.add(PageConfiguration.CONFIGURATION_ELEMENT_PATH_PAGE_PARAMETER_NAME, targetPagePath);
		return new BookmarkablePageLink<>(getId(), ConfigurationDefinedPage.class, targetPageParameters);
	}
	
}
