/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.xml;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import name.martingeisse.guiserver.xml.attribute.AttributeSpecification;

import com.google.common.collect.ImmutableList;

/**
 * Parses a special element that corresponds to a container configuration.
 * Parses the contents of the element to recognize components to add to
 * the container.
 * 
 * Certain functionality is missing from this base class to make the XML
 * parsing package self-contained.
 *
 * @param <C> the component type
 */
public abstract class AbstractContainerElementParserBase<C> implements IElementParser<C> {

	/**
	 * the componentIdPrefix
	 */
	private final String componentIdPrefix;

	/**
	 * the outputElementName
	 */
	private final String outputElementName;
	
	/**
	 * the attributeSpecifications
	 */
	private final AttributeSpecification[] attributeSpecifications;

	/**
	 * Constructor.
	 * @param componentIdPrefix the prefix to use for the component ID
	 * @param outputElementName the element name to write to the output for the component
	 * @param attributeSpecifications the attribute specifications, in the order they should be passed to component construction
	 */
	public AbstractContainerElementParserBase(String componentIdPrefix, String outputElementName, AttributeSpecification... attributeSpecifications) {
		this.componentIdPrefix = componentIdPrefix;
		this.outputElementName = outputElementName;
		this.attributeSpecifications = attributeSpecifications;
	}

	/**
	 * Getter method for the componentIdPrefix.
	 * @return the componentIdPrefix
	 */
	public String getComponentIdPrefix() {
		return componentIdPrefix;
	}

	/**
	 * Getter method for the outputElementName.
	 * @return the outputElementName
	 */
	public String getOutputElementName() {
		return outputElementName;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.xml.IElementParser#parse(name.martingeisse.guiserver.xml.ContentStreams)
	 */
	@Override
	public void parse(ContentStreams<C> streams) throws XMLStreamException {
		XMLStreamReader reader = streams.getReader();

		// read and skip over the opening input tag
		Object[] attributeValues = new Object[attributeSpecifications.length];
		for (int i=0; i<attributeSpecifications.length; i++) {
			attributeValues[i] = attributeSpecifications[i].parse(reader);
		}
		reader.next();

		// write the opening wicket tag for the container
		String componentId = (getComponentIdPrefix() + streams.getComponentAccumulatorSize());
		writeOpeningTag(streams, componentId);

		// parse the container contents
		streams.beginComponentAccumulator();
		parseContainerContents(streams);
		ImmutableList<C> children = streams.finishComponentAccumulator();
		
		// build the container
		streams.addComponent(createContainerConfiguration(streams, componentId, children, attributeValues));
		
		// finish the component element
		streams.getWriter().writeEndElement();
		reader.next();
		
	}

	/**
	 * Writes the opening tag to the output.
	 * 
	 * To add some attributes to that tag, just override, call super and then call
	 * writer.writeAttribute() at the end.
	 * 
	 * @param streams the content streams
	 * @param componentId the component ID
	 * @throws XMLStreamException on XML processing errors
	 */
	protected void writeOpeningTag(ContentStreams<C> streams, String componentId) throws XMLStreamException {
		XMLStreamWriter writer = streams.getWriter();
		writer.writeStartElement(getOutputElementName());
		writer.writeAttribute("wicket:id", componentId);
	}
	
	/**
	 * Parses the container contents, writing markup and generating components
	 * using the provided streams object.
	 * 
	 * @param streams the content streams
	 * @throws XMLStreamException on XML processing errors
	 */
	protected abstract void parseContainerContents(ContentStreams<C> streams) throws XMLStreamException;

	/**
	 * Creates the configuration for the resulting container.
	 * 
	 * @param streams the content streams
	 * @param componentId the wicket id of the container component
	 * @param children the configuration for the container's children
	 * @param attributeValues the parsed attribute values
	 * @return the container configuration
	 * @throws XMLStreamException on XML processing errors
	 */
	protected abstract C createContainerConfiguration(ContentStreams<C> streams, String componentId, ImmutableList<C> children, Object[] attributeValues) throws XMLStreamException;
	
}
