/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.configuration.content.parser;

import javax.xml.stream.XMLStreamException;

import name.martingeisse.guiserver.configuration.content.AbstractContainerConfiguration;
import name.martingeisse.guiserver.configuration.content.ComponentConfiguration;
import name.martingeisse.guiserver.configuration.content.ComponentConfigurationList;
import name.martingeisse.guiserver.xml.AbstractContainerElementParserBase;
import name.martingeisse.guiserver.xml.ContentStreams;
import name.martingeisse.guiserver.xml.XmlReflectionUtil;
import name.martingeisse.guiserver.xml.attribute.AttributeSpecification;

import com.google.common.collect.ImmutableList;

/**
 * This class contains the component-creation logic for AbstractContainerElementParserBase
 * to keep the XML package self-contained.
 */
public class ContainerElementParser extends AbstractContainerElementParserBase<ComponentConfiguration> {

	/**
	 * the containerClass
	 */
	private final Class<? extends AbstractContainerConfiguration> containerClass;
	
	/**
	 * Constructor.
	 * @param componentIdPrefix the prefix to use for the component ID
	 * @param outputElementName the element name to write to the output for the component
	 * @param containerClass the class of the container to create
	 * @param attributes the attributes, in the order they should be passed to component construction
	 */
	public ContainerElementParser(String componentIdPrefix, String outputElementName, Class<? extends AbstractContainerConfiguration> containerClass, AttributeSpecification... attributes) {
		super(componentIdPrefix, outputElementName, attributes);
		this.containerClass = containerClass;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.xml.AbstractContainerElementParserBase#createContainerConfiguration(java.lang.String, com.google.common.collect.ImmutableList, java.lang.Object[])
	 */
	@Override
	protected AbstractContainerConfiguration createContainerConfiguration(String componentId, ImmutableList<ComponentConfiguration> children, Object[] attributeValues) throws XMLStreamException {
		Object[] arguments = new Object[2 + attributeValues.length];
		arguments[0] = componentId;
		arguments[1] = new ComponentConfigurationList(children);
		System.arraycopy(attributeValues, 0, arguments, 2, attributeValues.length);
		return XmlReflectionUtil.invokeSuitableConstructor(containerClass, arguments);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.xml.AbstractContainerElementParserBase#parseContainerContents(name.martingeisse.guiserver.xml.ContentStreams)
	 */
	@Override
	protected void parseContainerContents(ContentStreams<ComponentConfiguration> streams) throws XMLStreamException {
		DefaultContentParser.INSTANCE.parse(streams);
	}

}
