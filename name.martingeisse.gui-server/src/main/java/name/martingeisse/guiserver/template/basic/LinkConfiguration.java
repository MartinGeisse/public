/**
 * Copyright (c) 2013 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.guiserver.template.basic;

import javax.xml.stream.XMLStreamException;

import name.martingeisse.guiserver.component.ConfigurationDefinedPage;
import name.martingeisse.guiserver.configuration.element.xml.PageConfiguration;
import name.martingeisse.guiserver.template.AbstractSingleContainerConfiguration;
import name.martingeisse.guiserver.template.ComponentGroupConfiguration;
import name.martingeisse.guiserver.template.ConfigurationAssembler;
import name.martingeisse.guiserver.xml.builder.BindAttribute;
import name.martingeisse.guiserver.xml.builder.RegisterComponentElement;
import name.martingeisse.guiserver.xml.builder.StructuredElement;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 * Configuration for a link. This class tries to cover only the common
 * cases to keep it simple.
 */
@StructuredElement
@RegisterComponentElement(localName = "link")
public final class LinkConfiguration extends AbstractSingleContainerConfiguration {

	/**
	 * the targetPagePath
	 */
	private String targetPagePath;

	/**
	 * Getter method for the targetPagePath.
	 * @return the targetPagePath
	 */
	public String getTargetPagePath() {
		return targetPagePath;
	}

	/**
	 * Setter method for the targetPagePath.
	 * @param targetPagePath the targetPagePath to set
	 */
	@BindAttribute(name = "href")
	public void setTargetPagePath(String targetPagePath) {
		this.targetPagePath = targetPagePath;
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
