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
import name.martingeisse.guiserver.xml.builder.BindAttribute;
import name.martingeisse.guiserver.xml.builder.BindComponentElement;
import name.martingeisse.guiserver.xml.result.ConfigurationAssembler;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.image.Image;

/**
 * Configuration for a pie chart component.
 */
@BindComponentElement(localName = "pieChart", attributes = {
	@BindAttribute(name = "backendUrl"), @BindAttribute(name = "legend", optionality = AttributeValueBindingOptionality.OPTIONAL_WITH_DEFAULT, defaultValue = "false")
})
public final class PieChartConfiguration extends AbstractSingleComponentConfiguration implements IConfigurationSnippet {

	/**
	 * the backendUrl
	 */
	private final String backendUrl;

	/**
	 * the legend
	 */
	private final boolean legend;

	/**
	 * the snippetHandle
	 */
	private int snippetHandle;

	/**
	 * Constructor.
	 * @param backendUrl the URL to load chart data from
	 * @param legend whether to draw a legend
	 */
	public PieChartConfiguration(String backendUrl, boolean legend) {
		this.backendUrl = backendUrl;
		this.legend = legend;
	}

	/**
	 * Getter method for the backendUrl.
	 * @return the backendUrl
	 */
	public String getBackendUrl() {
		return backendUrl;
	}

	/**
	 * Getter method for the legend.
	 * @return the legend
	 */
	public boolean isLegend() {
		return legend;
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
