/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.xml.builder;

import java.util.HashMap;
import java.util.Map;

import name.martingeisse.guiserver.xml.ConfigurationAssemblerAcceptor;
import name.martingeisse.guiserver.xml.content.ContentParser;
import name.martingeisse.guiserver.xml.content.ContentParserRegistry;
import name.martingeisse.guiserver.xml.content.DelegatingContentParser;
import name.martingeisse.guiserver.xml.content.MarkupContentParser;
import name.martingeisse.guiserver.xml.element.ElementParser;
import name.martingeisse.guiserver.xml.element.ElementParserRegistry;
import name.martingeisse.guiserver.xml.element.NameSelectedElementParser;
import name.martingeisse.guiserver.xml.result.MarkupContent;
import name.martingeisse.guiserver.xml.value.ValueParser;
import name.martingeisse.guiserver.xml.value.ValueParserRegistry;

/**
 * This class builds the parser for the XML format.
 * 
 * Calling code can obtain a recursive markup parser from this class. This parser
 * can be used to parse nested markup content. Note that at the time this builder
 * is used, this parser is not fully functional yet.
 *
 * @param <C> the type of components used in markup content
 */
public final class XmlParserBuilder<C extends ConfigurationAssemblerAcceptor<C>> {

	/**
	 * the recursiveMarkupParser
	 */
	private final DelegatingContentParser<MarkupContent<C>> recursiveMarkupParser;

	/**
	 * the componentElementParsers
	 */
	private final Map<String, ElementParser<? extends C>> componentElementParsers;

	/**
	 * the valueParserRegistry
	 */
	private final ValueParserRegistry valueParserRegistry;

	/**
	 * the elementParserRegistry
	 */
	private final ElementParserRegistry elementParserRegistry;
	
	/**
	 * the contentParserRegistry
	 */
	private final ContentParserRegistry contentParserRegistry;
	
	/**
	 * the elementParserBuilder
	 */
	private final ElementParserBuilder elementParserBuilder;
	
	/**
	 * Constructor.
	 */
	public XmlParserBuilder() {
		recursiveMarkupParser = new DelegatingContentParser<>();
		componentElementParsers = new HashMap<>();
		valueParserRegistry = new ValueParserRegistry();
		elementParserRegistry = new ElementParserRegistry();
		contentParserRegistry = new ContentParserRegistry();
		elementParserBuilder = new ElementParserBuilder(valueParserRegistry, elementParserRegistry, contentParserRegistry);
	}

	/**
	 * Adds a value parser that can be used for {@link BindAttribute} annotations.
	 * 
	 * @param type the parsed type
	 * @param parser the value parser
	 */
	public <T> void addValueParser(Class<T> type, ValueParser<T> parser) {
		valueParserRegistry.addParser(type, parser);
	}

	/**
	 * Adds an element parser that can be used for {@link BindElement} annotations.
	 * 
	 * @param type the parsed type
	 * @param parser the element parser
	 */
	public <T> void addElementParser(Class<T> type, ElementParser<T> parser) {
		elementParserRegistry.addParser(type, parser);
	}
	
	/**
	 * Adds an content parser that can be used for {@link BindContent} annotations.
	 * 
	 * @param type the parsed type
	 * @param parser the content parser
	 */
	public <T> void addContentParser(Class<T> type, ContentParser<T> parser) {
		contentParserRegistry.addParser(type, parser);
	}

	/**
	 * Getter method for the recursiveMarkupParser.
	 * @return the recursiveMarkupParser
	 */
	public DelegatingContentParser<MarkupContent<C>> getRecursiveMarkupParser() {
		return recursiveMarkupParser;
	}

	/**
	 * Getter method for the elementParserBuilder.
	 * @return the elementParserBuilder
	 */
	public ElementParserBuilder getElementParserBuilder() {
		return elementParserBuilder;
	}
	
	/**
	 * Adds a component group configuration class to this builder. The class must be annotated
	 * with {@link RegisterComponentElement}.
	 * 
	 * @param targetClass the class to add
	 */
	public void autoAddComponentGroupConfigurationClass(Class<? extends C> targetClass) {
		RegisterComponentElement annotation = targetClass.getAnnotation(RegisterComponentElement.class);
		if (annotation == null) {
			throw new RuntimeException("class " + targetClass + " is not annotated with @RegisterComponentElement");
		}
		addComponentGroupConfigurationParser(annotation.localName(), elementParserBuilder.build(targetClass));
	}

	/**
	 * Adds a component group configuration parser to this builder.
	 * 
	 * @param localElementName the local element name
	 * @param parser the parser
	 */
	public void addComponentGroupConfigurationParser(String localElementName, ElementParser<? extends C> parser) {
		componentElementParsers.put(localElementName, parser);
	}
	
	/**
	 * Returns the component parser for the specified local element name.
	 * 
	 * @param localElementName the local element name
	 * @return the parser
	 */
	public ElementParser<? extends C> getComponentParser(String localElementName) {
		return componentElementParsers.get(localElementName);
	}

	/**
	 * Builds the parser for the XML format.
	 * 
	 * @return the parser
	 */
	public ContentParser<MarkupContent<C>> build() {
		NameSelectedElementParser<C> elementParser = new NameSelectedElementParser<>(componentElementParsers);
		MarkupContentParser<C> markupContentParser = new MarkupContentParser<C>(elementParser);
		recursiveMarkupParser.setDelegate(markupContentParser);
		return markupContentParser;
	}

}
