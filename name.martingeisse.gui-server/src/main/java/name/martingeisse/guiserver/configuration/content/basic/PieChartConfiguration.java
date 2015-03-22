/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.configuration.content.basic;

import javax.xml.stream.XMLStreamException;

import name.martingeisse.guiserver.configuration.content.AbstractSingleComponentConfiguration;
import name.martingeisse.guiserver.configuration.content.ComponentGroupConfiguration;
import name.martingeisse.guiserver.configuration.content.IConfigurationSnippet;
import name.martingeisse.guiserver.gui.PieChartImageResource;
import name.martingeisse.guiserver.xml.builder.AttributeValueBindingOptionality;
import name.martingeisse.guiserver.xml.builder.BindPropertyAttribute;
import name.martingeisse.guiserver.xml.builder.BindComponentElement;
import name.martingeisse.guiserver.xml.result.ConfigurationAssembler;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.image.Image;

/**
 * Configuration for a pie chart component.
 */
@BindComponentElement(localName = "pieChart")
public final class PieChartConfiguration extends AbstractSingleComponentConfiguration implements IConfigurationSnippet {

	/**
	 * the backendUrl
	 */
	private String backendUrl;

	/**
	 * the legend
	 */
	private boolean legend;

	/**
	 * the snippetHandle
	 */
	private int snippetHandle;

	/**
	 * Getter method for the backendUrl.
	 * @return the backendUrl
	 */
	public String getBackendUrl() {
		return backendUrl;
	}

	/**
	 * Setter method for the backendUrl.
	 * @param backendUrl the backendUrl to set
	 */
	@BindPropertyAttribute(name = "backendUrl")
	public void setBackendUrl(String backendUrl) {
		this.backendUrl = backendUrl;
	}
	
	/**
	 * Getter method for the legend.
	 * @return the legend
	 */
	public boolean isLegend() {
		return legend;
	}
	
	/**
	 * Setter method for the legend.
	 * @param legend the legend to set
	 */
	@BindPropertyAttribute(name = "legend", optionality = AttributeValueBindingOptionality.OPTIONAL_WITH_DEFAULT, defaultValue = "false")
	public void setLegend(boolean legend) {
		this.legend = legend;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.configuration.content.AbstractComponentConfiguration#assemble(name.martingeisse.guiserver.xmlbind.result.ConfigurationAssembler)
	 */
	@Override
	public void assemble(ConfigurationAssembler<ComponentGroupConfiguration> assembler) throws XMLStreamException {
		super.assemble(assembler);
		assembler.getMarkupWriter().writeEmptyElement("img");
		assembler.getMarkupWriter().writeAttribute("wicket:id", getComponentId());
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.configuration.content.ComponentConfiguration#buildComponent()
	 */
	@Override
	public Component buildComponent() {
		return new Image(getComponentId(), new PieChartImageResource(500, 300, this));
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.configuration.content.IConfigurationSnippet#setSnippetHandle(int)
	 */
	@Override
	public void setSnippetHandle(int handle) {
		this.snippetHandle = handle;
	}

	/**
	 * Getter method for the snippetHandle.
	 * @return the snippetHandle
	 */
	public int getSnippetHandle() {
		return snippetHandle;
	}

}
