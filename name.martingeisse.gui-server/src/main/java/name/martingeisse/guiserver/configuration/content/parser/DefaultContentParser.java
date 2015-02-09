/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.configuration.content.parser;

import javax.xml.stream.XMLStreamException;

import name.martingeisse.guiserver.configuration.content.ComponentConfiguration;
import name.martingeisse.guiserver.configuration.content.EnclosureConfiguration;
import name.martingeisse.guiserver.configuration.content.FieldPathFeedbackPanelConfiguration;
import name.martingeisse.guiserver.configuration.content.FormConfiguration;
import name.martingeisse.guiserver.configuration.content.IncludeBackendConfiguration;
import name.martingeisse.guiserver.configuration.content.LazyLoadContainerConfiguration;
import name.martingeisse.guiserver.configuration.content.LinkConfiguration;
import name.martingeisse.guiserver.configuration.content.PieChartConfiguration;
import name.martingeisse.guiserver.configuration.content.TextFieldConfiguration;
import name.martingeisse.guiserver.xml.AbstractReplacingMacroElementParser;
import name.martingeisse.guiserver.xml.ContentStreams;
import name.martingeisse.guiserver.xml.MixedNestedMarkupParser;
import name.martingeisse.guiserver.xml.attribute.AttributeSpecification;
import name.martingeisse.guiserver.xml.attribute.BooleanAttributeParser;
import name.martingeisse.guiserver.xml.attribute.TextAttributeParser;

/**
 * This parser is used on top level to parse a content XML file.
 */
public class DefaultContentParser extends MixedNestedMarkupParser<ComponentConfiguration> {

	/**
	 * the shared instance of this class
	 */
	public static final DefaultContentParser INSTANCE = new DefaultContentParser();
	
	/**
	 * Constructor.
	 */
	public DefaultContentParser() {
		addSpecialElementParser("enclosure",
			new ContainerElementParser("enclosure", "div", EnclosureConfiguration.class));
		addSpecialElementParser("lazy",
			new ContainerElementParser("lazy", "div", LazyLoadContainerConfiguration.class));
		addSpecialElementParser("link",
			new ContainerElementParser("link", "a", LinkConfiguration.class,
				new AttributeSpecification("href", TextAttributeParser.INSTANCE)));
		addSpecialElementParser("includeBackend",
			new SkippedContentComponentElementParser("include", "wicket:container", IncludeBackendConfiguration.class,
				new AttributeSpecification("url", TextAttributeParser.INSTANCE),
				new AttributeSpecification("escape", true, true, BooleanAttributeParser.INSTANCE)));
		addSpecialElementParser("form",
			new ContainerElementParser("form", "form", FormConfiguration.class,
				new AttributeSpecification("backendUrl", TextAttributeParser.INSTANCE)));
		addSpecialElementParser("textField",
			new SkippedContentComponentElementParser("field", "input", TextFieldConfiguration.class,
				new AttributeSpecification("name", TextAttributeParser.INSTANCE),
				new AttributeSpecification("required", true, true, BooleanAttributeParser.INSTANCE)));
		addSpecialElementParser("validation",
			new SkippedContentComponentElementParser("validation", "div", FieldPathFeedbackPanelConfiguration.class,
				new AttributeSpecification("name", TextAttributeParser.INSTANCE)));
		addSpecialElementParser("submit",
			new AbstractReplacingMacroElementParser<ComponentConfiguration>() {
				@Override
				protected void writeMarkup(ContentStreams<ComponentConfiguration> streams, Object[] attributeValues) throws XMLStreamException {
					streams.getWriter().writeEmptyElement("input");
					streams.getWriter().writeAttribute("type", "submit");
				}
			}
		);
		addSpecialElementParser("navbar", new NavigationBarParser());
		addSpecialElementParser("tabPanel", new TabPanelParser());
		addSpecialElementParser("pieChart", new SkippedContentComponentElementParser("pie", "img", PieChartConfiguration.class,
			new AttributeSpecification("backendUrl", TextAttributeParser.INSTANCE)));
	}

}
