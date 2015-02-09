/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.configuration.content.parser;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import name.martingeisse.guiserver.configuration.content.ComponentConfiguration;
import name.martingeisse.guiserver.configuration.content.TabPanelConfiguration;
import name.martingeisse.guiserver.configuration.content.TabPanelConfiguration.TabEntry;
import name.martingeisse.guiserver.xml.ContentStreams;
import name.martingeisse.guiserver.xml.IElementParser;
import name.martingeisse.guiserver.xml.ObjectListParser;
import name.martingeisse.wicket.component.misc.PageParameterDrivenTabPanel;

import com.google.common.collect.ImmutableList;

/**
 * The parser for a {@link TabPanelConfiguration}.
 */
public final class TabPanelParser implements IElementParser<ComponentConfiguration> {

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.xml.IElementParser#parse(name.martingeisse.guiserver.xml.ContentStreams)
	 */
	@Override
	public void parse(ContentStreams<ComponentConfiguration> streams) throws XMLStreamException {
		XMLStreamWriter writer = streams.getWriter();

		// build the tab panel component configuration
		final String tabPanelComponentId = ("tabPanel" + streams.getComponentAccumulatorSize());
		final TabPanelConfiguration tabPanelConfiguration = new TabPanelConfiguration(tabPanelComponentId, tabPanelComponentId);
		streams.addComponent(tabPanelConfiguration);
		writer.writeStartElement("div");
		writer.writeAttribute("wicket:id", tabPanelComponentId + "-container");
		writer.writeEmptyElement("div");
		writer.writeAttribute("wicket:id", tabPanelComponentId);

		// parse the contents, build markup fragments for them and add the TabInfo objects to the panel configuration
		// the markup for the fragments will be inside the tab panel tags (i.e. in the part that gets replaced by the Panel code)
		streams.next();
		new ObjectListParser<ComponentConfiguration>("tab") {
			@Override
			protected void handleSpecialElement(ContentStreams<ComponentConfiguration> streams) throws XMLStreamException {
				XMLStreamWriter writer = streams.getWriter();
				String selector = streams.getMandatoryAttribute("selector");
				String tabComponentId = tabPanelComponentId + "-" + selector;
				String title = streams.getMandatoryAttribute("title");
				streams.next();
				writer.writeStartElement("wicket:fragment");
				writer.writeAttribute("wicket:id", tabComponentId);
				streams.beginComponentAccumulator();
				DefaultContentParser.INSTANCE.parse(streams);
				ImmutableList<ComponentConfiguration> tabContentComponents = streams.finishComponentAccumulator();
				streams.next();
				writer.writeEndElement();
				PageParameterDrivenTabPanel.TabInfo tabInfo = new PageParameterDrivenTabPanel.TabInfo(title, selector);
				TabEntry tabEntry = new TabEntry(tabComponentId, tabInfo, tabContentComponents);
				tabPanelConfiguration.addTab(tabEntry);
			}
		}.parse(streams);
		streams.next();
		
		// finish the tab panel
		writer.writeEndElement();
	}
	
}
