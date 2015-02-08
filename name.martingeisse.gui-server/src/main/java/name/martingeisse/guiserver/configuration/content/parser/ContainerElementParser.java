/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.configuration.content.parser;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;

import javax.xml.stream.XMLStreamException;

import name.martingeisse.guiserver.configuration.content.AbstractContainerConfiguration;
import name.martingeisse.guiserver.configuration.content.ComponentConfiguration;
import name.martingeisse.guiserver.configuration.content.ComponentConfigurationList;
import name.martingeisse.guiserver.xml.AbstractContainerElementParserBase;
import name.martingeisse.guiserver.xml.ContentStreams;
import name.martingeisse.guiserver.xml.attribute.AttributeSpecification;

import com.google.common.collect.ImmutableList;
import com.google.common.primitives.Primitives;

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
		constructorLoop: for (Constructor<?> constructor : containerClass.getConstructors()) {
			
			// check if this constructor matches the (id, children, ...) pattern at all
			if (constructor.getParameterCount() != attributeValues.length + 2) {
				continue;
			}
			Parameter[] parameters = constructor.getParameters();
			if (!parameters[0].getType().isAssignableFrom(String.class)) {
				continue;
			}
			if (!parameters[1].getType().isAssignableFrom(ComponentConfigurationList.class)) {
				continue;
			}
			
			// match the parsed attributes against the constructor parameters
			for (int i=0; i<attributeValues.length; i++) {
				Parameter parameter = parameters[2 + i];
				Object argument = attributeValues[i];
				arguments[2 + i] = argument;
				if (parameter.getType().isPrimitive()) {
					if (argument == null) {
						// passing null for a primitive type
						continue constructorLoop;
					} else if (Primitives.wrap(parameter.getType()) != argument.getClass()) {
						// passing some unrelated value for a primitive type
						continue constructorLoop;
					} // else: passing a value of the boxed type for the primitive type is ok
				} else if (argument == null) {
					// passing null is ok for non-primitive types
				} else {
					if (!parameter.getType().isAssignableFrom(argument.getClass())) {
						// passing a value of the wrong type for this constructor
						continue constructorLoop;
					} // else: passing a value of the exact type or subtype is ok
				}
			}
			
			// this constructor will work for us
			try {
				return (AbstractContainerConfiguration)constructor.newInstance(arguments);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			
		}
		throw new RuntimeException("no viable constructor found for class: " + containerClass);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.xml.AbstractContainerElementParserBase#parseContainerContents(name.martingeisse.guiserver.xml.ContentStreams)
	 */
	@Override
	protected void parseContainerContents(ContentStreams<ComponentConfiguration> streams) throws XMLStreamException {
		DefaultContentParser.INSTANCE.parse(streams);
	}

}
