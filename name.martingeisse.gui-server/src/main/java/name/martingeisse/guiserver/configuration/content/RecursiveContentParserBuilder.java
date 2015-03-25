/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.configuration.content;

import name.martingeisse.guiserver.xml.builder.BindAttribute;
import name.martingeisse.guiserver.xml.builder.BindContent;
import name.martingeisse.guiserver.xml.builder.BindElement;
import name.martingeisse.guiserver.xml.builder.ElementParserBuilder;
import name.martingeisse.guiserver.xml.builder.RegisterComponentElement;
import name.martingeisse.guiserver.xml.content.ContentParser;
import name.martingeisse.guiserver.xml.content.ContentParserRegistry;
import name.martingeisse.guiserver.xml.element.ElementParser;
import name.martingeisse.guiserver.xml.element.ElementParserRegistry;
import name.martingeisse.guiserver.xml.element.NameSelectedElementParser;
import name.martingeisse.guiserver.xml.value.ValueParser;
import name.martingeisse.guiserver.xml.value.ValueParserRegistry;

/**
 * This class builds the parser for the XML format.
 * 
 * This class also allows to obtain the unfinished component element parser being
 * built to parse component elements, to use it while building parser loops. Like
 * the markup content parser, this parser is not fully functional yet.
 * 
 * TODO this class is ugly. Move to the configuration package?
 *
 * @param <C> the type of components used in markup content
 */
public final class RecursiveContentParserBuilder<C> {
	
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
	 * the componentElementParser
	 */
	private final NameSelectedElementParser<C> componentElementParser;
	
	/**
	 * Constructor.
	 */
	public RecursiveContentParserBuilder() {
		valueParserRegistry = new ValueParserRegistry();
		elementParserRegistry = new ElementParserRegistry();
		contentParserRegistry = new ContentParserRegistry();
		elementParserBuilder = new ElementParserBuilder(valueParserRegistry, elementParserRegistry, contentParserRegistry);
		componentElementParser = new NameSelectedElementParser<>();
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
	public void autoAddComponentElementParser(Class<? extends C> targetClass) {
		RegisterComponentElement annotation = targetClass.getAnnotation(RegisterComponentElement.class);
		if (annotation == null) {
			throw new RuntimeException("class " + targetClass + " is not annotated with @RegisterComponentElement");
		}
		addComponentElementParser(annotation.localName(), elementParserBuilder.build(targetClass));
	}

	/**
	 * Adds a component group configuration parser to this builder.
	 * 
	 * @param localElementName the local element name
	 * @param parser the parser
	 */
	public void addComponentElementParser(String localElementName, ElementParser<? extends C> parser) {
		componentElementParser.addParser(localElementName, parser);
	}

	/**
	 * Getter method for the componentElementParser.
	 * @return the componentElementParser
	 */
	public NameSelectedElementParser<C> getComponentElementParser() {
		return componentElementParser;
	}

}
