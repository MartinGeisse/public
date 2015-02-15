///**
// * Copyright (c) 2013 Shopgate GmbH
// */
//
//package name.martingeisse.guiserver.configuration.content.parser;
//
//import javax.xml.stream.XMLStreamException;
//
//import name.martingeisse.guiserver.configuration.content.CheckboxConfiguration;
//import name.martingeisse.guiserver.configuration.content.ComponentConfiguration;
//import name.martingeisse.guiserver.configuration.content.EnclosureConfiguration;
//import name.martingeisse.guiserver.configuration.content.FieldPathFeedbackPanelConfiguration;
//import name.martingeisse.guiserver.configuration.content.FormConfiguration;
//import name.martingeisse.guiserver.configuration.content.IncludeBackendConfiguration;
//import name.martingeisse.guiserver.configuration.content.LazyLoadContainerConfiguration;
//import name.martingeisse.guiserver.configuration.content.LinkConfiguration;
//import name.martingeisse.guiserver.configuration.content.PieChartConfiguration;
//import name.martingeisse.guiserver.configuration.content.TextFieldConfiguration;
//import name.martingeisse.guiserver.xml.AbstractReplacingMacroElementParser;
//import name.martingeisse.guiserver.xml.ContentStreams;
//import name.martingeisse.guiserver.xml.MixedNestedMarkupParser;
//import name.martingeisse.guiserver.xml.attribute.AttributeSpecification;
//import name.martingeisse.guiserver.xml.attribute.BooleanAttributeParser;
//import name.martingeisse.guiserver.xml.attribute.TextAttributeParser;
//
///**
// * This parser is used on top level to parse a content XML file.
// */
//public class DefaultContentParser extends MixedNestedMarkupParser<ComponentConfiguration> {
//
//	/**
//	 * the shared instance of this class
//	 */
//	public static final DefaultContentParser INSTANCE = new DefaultContentParser();
//	
//	/**
//	 * Constructor.
//	 */
//	public DefaultContentParser() {
//		addSpecialElementParser("textField", new FormFieldParser("text", TextFieldConfiguration.class));
//		addSpecialElementParser("checkbox", new FormFieldParser("checkbox", CheckboxConfiguration.class));


//		addSpecialElementParser("navbar", new NavigationBarParser());
//		addSpecialElementParser("tabPanel", new TabPanelParser());

//
//}
