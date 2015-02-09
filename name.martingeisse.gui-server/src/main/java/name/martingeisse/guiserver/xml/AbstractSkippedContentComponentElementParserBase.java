/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.xml;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import name.martingeisse.guiserver.xml.attribute.AttributeSpecification;

/**
 * Parses a special element for a component, skipping its contents in the input.
 * 
 * This base class also produces an empty output element. Subclasses may override
 * this behavior.
 * 
 * Certain functionality is missing from this base class to make the XML
 * parsing package self-contained.
 *
 * @param <C> the component type
 */
public abstract class AbstractSkippedContentComponentElementParserBase<C> implements IElementParser<C> {

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
	public AbstractSkippedContentComponentElementParserBase(String componentIdPrefix, String outputElementName, AttributeSpecification... attributeSpecifications) {
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

		// read and skip over the element
		Object[] attributeValues = new Object[attributeSpecifications.length];
		for (int i=0; i<attributeSpecifications.length; i++) {
			attributeValues[i] = attributeSpecifications[i].parse(reader);
		}
		reader.next();
		streams.skipNestedContent();
		reader.next();

		// build the component
		String componentId = (getComponentIdPrefix() + streams.getComponentAccumulatorSize());
		streams.addComponent(createComponentConfiguration(streams, componentId, attributeValues));
		
		// write the output eleent
		writeElement(streams, componentId);
		
	}

	/**
	 * Writes the output element.
	 * 
	 * To add some attributes to that tag, just override, call super and then and call
	 * writer.writeAttribute() at the end.
	 * 
	 * To override the output with something completely different, override this method
	 * and don't call super. 
	 * 
	 * @param streams the content streams
	 * @param componentId the component ID
	 * @throws XMLStreamException on XML processing errors
	 */
	protected void writeElement(ContentStreams<C> streams, String componentId) throws XMLStreamException {
		XMLStreamWriter writer = streams.getWriter();
		writer.writeEmptyElement(getOutputElementName());
		writer.writeAttribute("wicket:id", componentId);
	}
	
	/**
	 * Creates the configuration for the resulting component.
	 * 
	 * @param streams the content streams
	 * @param componentId the wicket id of the component
	 * @param attributeValues the parsed attribute values
	 * @return the component configuration
	 * @throws XMLStreamException on XML processing errors
	 */
	protected abstract C createComponentConfiguration(ContentStreams<C> streams, String componentId, Object[] attributeValues) throws XMLStreamException;
	
}
