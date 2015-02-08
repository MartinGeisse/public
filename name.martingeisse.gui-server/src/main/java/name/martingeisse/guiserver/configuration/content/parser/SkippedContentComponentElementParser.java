/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.configuration.content.parser;

import javax.xml.stream.XMLStreamException;

import name.martingeisse.guiserver.configuration.content.AbstractComponentConfiguration;
import name.martingeisse.guiserver.configuration.content.ComponentConfiguration;
import name.martingeisse.guiserver.configuration.content.IConfigurationSnippet;
import name.martingeisse.guiserver.xml.AbstractSkippedContentComponentElementParserBase;
import name.martingeisse.guiserver.xml.ContentStreams;
import name.martingeisse.guiserver.xml.XmlReflectionUtil;
import name.martingeisse.guiserver.xml.attribute.AttributeSpecification;

/**
 * This class contains the component-creation logic for AbstractSkippedContentComponentElementParserBase
 * to keep the XML package self-contained.
 * 
 * This class also detects if the container implements {@link IConfigurationSnippet},
 * and if so, assigns it a snippet handle.
 */
public class SkippedContentComponentElementParser extends AbstractSkippedContentComponentElementParserBase<ComponentConfiguration> {

	/**
	 * the componentConfigurationClass
	 */
	private final Class<? extends ComponentConfiguration> componentConfigurationClass;
	
	/**
	 * Constructor.
	 * @param componentIdPrefix the prefix to use for the component ID
	 * @param outputElementName the element name to write to the output for the component
	 * @param componentConfigurationClass the class of the component configuration to create
	 * @param attributes the attributes, in the order they should be passed to component construction
	 */
	public SkippedContentComponentElementParser(String componentIdPrefix, String outputElementName, Class<? extends AbstractComponentConfiguration> componentConfigurationClass, AttributeSpecification... attributes) {
		super(componentIdPrefix, outputElementName, attributes);
		this.componentConfigurationClass = componentConfigurationClass;
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.xml.AbstractSkippedContentComponentElementParserBase#createComponentConfiguration(name.martingeisse.guiserver.xml.ContentStreams, java.lang.String, java.lang.Object[])
	 */
	@Override
	protected ComponentConfiguration createComponentConfiguration(ContentStreams<ComponentConfiguration> streams, String componentId, Object[] attributeValues) throws XMLStreamException {
		Object[] arguments = new Object[1 + attributeValues.length];
		arguments[0] = componentId;
		System.arraycopy(attributeValues, 0, arguments, 1, attributeValues.length);
		ComponentConfiguration componentConfiguration = XmlReflectionUtil.invokeSuitableConstructor(componentConfigurationClass, arguments);
		if (componentConfiguration instanceof IConfigurationSnippet) {
			IConfigurationSnippet snippet = (IConfigurationSnippet)componentConfiguration;
			snippet.setSnippetHandle(streams.addSnippet(snippet));
		}
		return componentConfiguration;
	}

}
