/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.configuration.content.parser;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import name.martingeisse.guiserver.configuration.content.ComponentConfiguration;
import name.martingeisse.guiserver.configuration.content.EnclosureConfiguration;
import name.martingeisse.guiserver.configuration.content.FieldPathFeedbackPanelConfiguration;
import name.martingeisse.guiserver.configuration.content.FormConfiguration;
import name.martingeisse.guiserver.configuration.content.IncludeBackendConfiguration;
import name.martingeisse.guiserver.configuration.content.LazyLoadContainerConfiguration;
import name.martingeisse.guiserver.configuration.content.LinkConfiguration;
import name.martingeisse.guiserver.configuration.content.NavigationBarConfiguration;
import name.martingeisse.guiserver.configuration.content.TabPanelConfiguration;
import name.martingeisse.guiserver.configuration.content.TextFieldConfiguration;
import name.martingeisse.guiserver.configuration.content.TabPanelConfiguration.TabEntry;
import name.martingeisse.wicket.component.misc.PageParameterDrivenTabPanel;

import com.google.common.collect.ImmutableList;

/**
 * This parser is used on top level to parse a content XML file.
 */
public class DefaultContentParser extends ContentParser {

	/**
	 * Constructor.
	 * 
	 * @param streams the content streams
	 */
	public DefaultContentParser(ContentStreams streams) {
		super(streams);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.configuration.content.parser.ContentParser#handleSpecialElement(java.lang.String)
	 */
	@Override
	protected void handleSpecialElement(String localName) throws XMLStreamException {
		ContentStreams streams = getStreams();
		XMLStreamWriter writer = streams.getWriter();
		XMLStreamReader reader = streams.getReader();
		switch (localName) {

		case "link": {
			String href = streams.getMandatoryAttribute("href");
			String componentId = newComponentId("link");
			writer.writeStartElement("a");
			writer.writeAttribute("wicket:id", componentId);
			reader.next();
			streams.addComponent(new LinkConfiguration(componentId, parseComponentContent(), href));
			writer.writeEndElement();
			reader.next();
			break;
		}

		case "navbar": {
			String componentId = newComponentId("navbar");
			writer.writeStartElement("div");
			writer.writeAttribute("wicket:id", componentId);
			streams.next();
			streams.skipWhitespace();
			
			ComponentConfiguration brandLink = null;
			String brandLinkElementLocalName = streams.recognizeStartSpecialElement();
			if (brandLinkElementLocalName != null && brandLinkElementLocalName.equals("brandLink")) {
				streams.next();
				streams.skipWhitespace();
				String linkElementLocalName = streams.recognizeStartSpecialElement();
				if (linkElementLocalName == null) {
					throw new RuntimeException("link-related special tag expected inside brandLink element");
				}
				streams.beginComponentAccumulator();
				handleSpecialElement(linkElementLocalName);
				ImmutableList<ComponentConfiguration> brandLinkList = streams.finishComponentAccumulator();
				if (brandLinkList.size() != 1) {
					throw new RuntimeException("expected exactly 1 brandLink");
				}
				brandLink = brandLinkList.get(0);
				streams.skipWhitespace();
				streams.next();
			}
			streams.addComponent(new NavigationBarConfiguration(componentId, parseComponentContent(), brandLink));
			
			writer.writeEndElement();
			reader.next();
			break;
		}
		
		case "includeBackend": {
			String url = streams.getMandatoryAttribute("url");
			String escapeSpec = streams.getOptionalAttribute("escape");
			boolean escape = (escapeSpec == null ? true : Boolean.valueOf(escapeSpec));
			String componentId = newComponentId("include");
			streams.next();
			skipNestedContent();
			streams.next();
			writer.writeEmptyElement("wicket:container");
			writer.writeAttribute("wicket:id", componentId);
			streams.addComponent(new IncludeBackendConfiguration(componentId, url, escape));
			break;
		}

		case "lazy": {
			String componentId = newComponentId("lazy");
			writer.writeStartElement("div");
			writer.writeAttribute("wicket:id", componentId);
			streams.next();
			streams.addComponent(new LazyLoadContainerConfiguration(componentId, parseComponentContent()));
			streams.next();
			writer.writeEndElement();
			break;
		}
		
		case "tabPanel": {
			
			// build the tab panel component configuration
			final String tabPanelComponentId = newComponentId("tabPanel");
			final TabPanelConfiguration tabPanelConfiguration = new TabPanelConfiguration(tabPanelComponentId, tabPanelComponentId);
			streams.addComponent(tabPanelConfiguration);
			writer.writeStartElement("div");
			writer.writeAttribute("wicket:id", tabPanelComponentId + "-container");
			writer.writeEmptyElement("div");
			writer.writeAttribute("wicket:id", tabPanelComponentId);

			// parse the contents, build markup fragments for them and add the TabInfo objects to the panel configuration
			// the markup for the fragments will be inside the tab panel tags (i.e. in the part that gets replaced by the Panel code)
			// TODO throw error on non-special-tag content
			streams.next();
			new ContentParser(streams) {
				@Override
				protected void handleSpecialElement(String localName) throws XMLStreamException {
					ContentStreams streams = getStreams();
					XMLStreamWriter writer = streams.getWriter();
					if (!localName.equals("tab")) {
						throw new RuntimeException("invalid special tag inside a TabPanel: " + localName);
					}
					String selector = streams.getMandatoryAttribute("selector");
					String tabComponentId = tabPanelComponentId + "-" + selector;
					String title = streams.getMandatoryAttribute("title");
					streams.next();
					writer.writeStartElement("wicket:fragment");
					writer.writeAttribute("wicket:id", tabComponentId);
					ImmutableList<ComponentConfiguration> tabContentComponents = DefaultContentParser.this.parseComponentContent();
					streams.next();
					writer.writeEndElement();
					PageParameterDrivenTabPanel.TabInfo tabInfo = new PageParameterDrivenTabPanel.TabInfo(title, selector);
					TabEntry tabEntry = new TabEntry(tabComponentId, tabInfo, tabContentComponents);
					tabPanelConfiguration.addTab(tabEntry);
				}
			}.parseNestedContent();
			streams.next();
			
			// finish the tab panel
			writer.writeEndElement();
			break;
			
		}
		
		case "form": {
			String componentId = newComponentId("form");
			String backendUrl = streams.getMandatoryAttribute("backendUrl");
			writer.writeStartElement("form");
			writer.writeAttribute("wicket:id", componentId);
			reader.next();
			FormConfiguration formConfiguration = new FormConfiguration(componentId, parseComponentContent(), backendUrl);
			formConfiguration.setConfigurationHandle(streams.addSnippet(formConfiguration));
			streams.addComponent(formConfiguration);
			writer.writeEndElement();
			reader.next();
			break;
		}
		
		case "textField": {
			String componentId = newComponentId("field");
			String fieldName = streams.getMandatoryAttribute("name");
			String requiredText = streams.getOptionalAttribute("required");
			boolean required = (requiredText == null ? true : !requiredText.equals("false"));
			writer.writeEmptyElement("input");
			writer.writeAttribute("wicket:id", componentId);
			streams.expectAndSkipEmptyElement();
			streams.addComponent(new TextFieldConfiguration(componentId, fieldName, required));
			break;
		}
		
		case "submit": {
			reader.next();
			writer.writeStartElement("input");
			writer.writeAttribute("type", "submit");
			parseNestedContent();
			reader.next();
			writer.writeEndElement();
			break;
		}
		
		case "validation": {
			String componentId = newComponentId("field");
			String fieldPath = streams.getMandatoryAttribute("name");
			reader.next();
			writer.writeEmptyElement("div");
			writer.writeAttribute("wicket:id", componentId);
			skipNestedContent();
			reader.next();
			streams.addComponent(new FieldPathFeedbackPanelConfiguration(componentId, fieldPath));
			break;
		}
		
		case "enclosure": {
			String componentId = newComponentId("link");
			writer.writeStartElement("div");
			writer.writeAttribute("wicket:id", componentId);
			reader.next();
			streams.addComponent(new EnclosureConfiguration(componentId, parseComponentContent()));
			writer.writeEndElement();
			reader.next();
			break;
		}

		default:
			throw new RuntimeException("unknown special tag: " + reader.getLocalName());

		}
	}
	
}
