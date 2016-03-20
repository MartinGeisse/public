/**
 * Copyright (c) 2013 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.guiserver.template.basic;

import javax.xml.stream.XMLStreamException;

import name.martingeisse.guiserver.component.UserDefinedPanel;
import name.martingeisse.guiserver.configuration.ConfigurationHolder;
import name.martingeisse.guiserver.configuration.element.xml.PanelConfiguration;
import name.martingeisse.guiserver.template.AbstractSingleComponentConfiguration;
import name.martingeisse.guiserver.template.ComponentGroupConfiguration;
import name.martingeisse.guiserver.template.ConfigurationAssembler;
import name.martingeisse.guiserver.template.IConfigurationSnippet;
import name.martingeisse.guiserver.xml.builder.BindAttribute;
import name.martingeisse.guiserver.xml.builder.RegisterComponentElement;
import name.martingeisse.guiserver.xml.builder.StructuredElement;

import org.apache.wicket.Component;

/**
 * Configuration for using a gui:panel defined elsewhere.
 */
@StructuredElement
@RegisterComponentElement(localName = "panel")
public class PanelReferenceConfiguration extends AbstractSingleComponentConfiguration implements IConfigurationSnippet {

	/**
	 * the sourcePath
	 */
	private String sourcePath;
	
	/**
	 * the panelConfiguration
	 */
	private PanelConfiguration panelConfiguration;
	
	/**
	 * the snippetHandle
	 */
	private int snippetHandle;

	/**
	 * Setter method for the sourcePath.
	 * @param sourcePath the sourcePath to set
	 */
	@BindAttribute(name = "src")
	public void setSourcePath(String sourcePath) {
		this.sourcePath = sourcePath;
	}
	
	/**
	 * Getter method for the snippetHandle.
	 * @return the snippetHandle
	 */
	public int getSnippetHandle() {
		return snippetHandle;
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.template.IConfigurationSnippet#setSnippetHandle(int)
	 */
	@Override
	public void setSnippetHandle(int snippetHandle) {
		this.snippetHandle = snippetHandle;
	}

	/**
	 * @return the panel configuration
	 */
	public PanelConfiguration getPanelConfiguration() {
		if (panelConfiguration == null) {
			panelConfiguration = ConfigurationHolder.needRequestUniverseConfiguration().getElementOrNull(PanelConfiguration.class, sourcePath);
			if (panelConfiguration == null) {
				throw new RuntimeException("no such panel configuration: " + sourcePath);
			}
		}
		return panelConfiguration;
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.configuration.content.AbstractComponentConfiguration#assemble(name.martingeisse.guiserver.xmlbind.result.ConfigurationAssembler)
	 */
	@Override
	public void assemble(ConfigurationAssembler<ComponentGroupConfiguration> assembler) throws XMLStreamException {
		super.assemble(assembler);
		assembler.getMarkupWriter().writeEmptyElement("div");
		assembler.getMarkupWriter().writeAttribute("wicket:id", getComponentId());
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.configuration.content.ComponentConfiguration#buildComponent()
	 */
	@Override
	public Component buildComponent() {
		UserDefinedPanel panel = new UserDefinedPanel(getComponentId(), this);
		// TODO add children from the panel configuration
		return panel;
	}

}
