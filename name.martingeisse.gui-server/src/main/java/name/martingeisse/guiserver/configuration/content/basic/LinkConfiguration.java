/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.configuration.content.basic;

import javax.xml.stream.XMLStreamException;

import name.martingeisse.guiserver.configuration.content.AbstractSingleContainerConfiguration;
import name.martingeisse.guiserver.configuration.content.ComponentGroupConfiguration;
import name.martingeisse.guiserver.configuration.elements.PageConfiguration;
import name.martingeisse.guiserver.gui.ConfigurationDefinedPage;
import name.martingeisse.guiserver.xml.builder.BindAttribute;
import name.martingeisse.guiserver.xml.builder.BindComponentElement;
import name.martingeisse.guiserver.xml.result.ConfigurationAssembler;
import name.martingeisse.guiserver.xml.result.MarkupContent;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 * Configuration for a link. This class tries to cover only the common
 * cases to keep it simple.
 */
@BindComponentElement(localName = "link", attributes = {
	@BindAttribute(name = "href")
}, acceptsMarkupContent = true)
public final class LinkConfiguration extends AbstractSingleContainerConfiguration {

	/**
	 * the targetPagePath
	 */
	private final String targetPagePath;

	/**
	 * Constructor.
	 * @param targetPagePath the path of the page to link to
	 * @param markupContent the markup content
	 */
	public LinkConfiguration(String targetPagePath, MarkupContent<ComponentGroupConfiguration> markupContent) {
		super(markupContent);
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
	 * @see name.martingeisse.guiserver.configuration.content.AbstractContainerConfiguration#assembleContainerIntro(name.martingeisse.guiserver.xmlbind.result.ConfigurationAssembler)
	 */
	@Override
	protected void assembleContainerIntro(ConfigurationAssembler<ComponentGroupConfiguration> assembler) throws XMLStreamException {
		writeOpeningComponentTag(assembler, "a");
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.configurationNew.content.AbstractContainerConfiguration#buildContainer()
	 */
	@Override
	protected MarkupContainer buildContainer() {
		PageParameters targetPageParameters = new PageParameters();
		targetPageParameters.add(PageConfiguration.CONFIGURATION_ELEMENT_PATH_PAGE_PARAMETER_NAME, targetPagePath);
		return new BookmarkablePageLink<>(getComponentId(), ConfigurationDefinedPage.class, targetPageParameters);
	}

}
