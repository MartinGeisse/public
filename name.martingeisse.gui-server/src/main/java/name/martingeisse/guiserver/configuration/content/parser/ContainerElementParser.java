/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.configuration.content.parser;

import javax.xml.stream.XMLStreamException;

import name.martingeisse.guiserver.configuration.content.AbstractContainerConfiguration;
import name.martingeisse.guiserver.configuration.content.ComponentConfiguration;
import name.martingeisse.guiserver.configuration.content.ComponentConfigurationList;
import name.martingeisse.guiserver.configuration.content.IConfigurationSnippet;
import name.martingeisse.guiserver.xml.AbstractContainerElementParserBase;
import name.martingeisse.guiserver.xml.ContentStreams;
import name.martingeisse.guiserver.xml.XmlReflectionUtil;
import name.martingeisse.guiserver.xml.attribute.AttributeSpecification;

import com.google.common.collect.ImmutableList;

/**
 * This class contains the component-creation logic for AbstractContainerElementParserBase
 * to keep the XML package self-contained.
 * 
 * This class also detects if the container implements {@link IConfigurationSnippet},
 * and if so, assigns it a snippet handle.
 */
public class ContainerElementParser extends AbstractContainerElementParserBase<ComponentConfiguration> {

	/**
	 * the componentConfigurationClass
	 */
	private final Class<? extends AbstractContainerConfiguration> containerConfigurationClass;
	
	/**
	 * Constructor.
	 * @param componentIdPrefix the prefix to use for the component ID
	 * @param outputElementName the element name to write to the output for the component
	 * @param componentConfigurationClass the class of the container configuration to create
	 * @param attributes the attributes, in the order they should be passed to component construction
	 */
	public ContainerElementParser(String componentIdPrefix, String outputElementName, Class<? extends AbstractContainerConfiguration> componentConfigurationClass, AttributeSpecification... attributes) {
		super(componentIdPrefix, outputElementName, attributes);
		this.containerConfigurationClass = componentConfigurationClass;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.xml.AbstractContainerElementParserBase#createContainerConfiguration(name.martingeisse.guiserver.xml.ContentStreams, java.lang.String, com.google.common.collect.ImmutableList, java.lang.Object[])
	 */
	@Override
	protected ComponentConfiguration createContainerConfiguration(ContentStreams<ComponentConfiguration> streams, String componentId, ImmutableList<ComponentConfiguration> children, Object[] attributeValues) throws XMLStreamException {
		Object[] arguments = new Object[2 + attributeValues.length];
		arguments[0] = componentId;
		arguments[1] = new ComponentConfigurationList(children);
		System.arraycopy(attributeValues, 0, arguments, 2, attributeValues.length);
		AbstractContainerConfiguration container = XmlReflectionUtil.invokeSuitableConstructor(containerConfigurationClass, arguments);
		if (container instanceof IConfigurationSnippet) {
			IConfigurationSnippet snippet = (IConfigurationSnippet)container;
			snippet.setSnippetHandle(streams.addSnippet(snippet));
		}
		return container;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.xml.AbstractContainerElementParserBase#parseContainerContents(name.martingeisse.guiserver.xml.ContentStreams)
	 */
	@Override
	protected void parseContainerContents(ContentStreams<ComponentConfiguration> streams) throws XMLStreamException {
		DefaultContentParser.INSTANCE.parse(streams);
	}

}
