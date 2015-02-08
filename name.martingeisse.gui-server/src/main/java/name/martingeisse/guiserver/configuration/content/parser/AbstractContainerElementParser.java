/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.configuration.content.parser;

import javax.xml.stream.XMLStreamException;

import name.martingeisse.guiserver.configuration.content.ComponentConfiguration;
import name.martingeisse.guiserver.xml.AbstractContainerElementParserBase;
import name.martingeisse.guiserver.xml.ContentStreams;

/**
 * This class contains the component-creation logic for AbstractContainerElementParserBase
 * to keep the XML package self-contained.
 */
public abstract class AbstractContainerElementParser extends AbstractContainerElementParserBase<ComponentConfiguration> {

	/**
	 * Constructor.
	 * @param componentIdPrefix the prefix to use for the component ID
	 * @param outputElementName the element name to write to the output for the component
	 */
	public AbstractContainerElementParser(String componentIdPrefix, String outputElementName) {
		super(componentIdPrefix, outputElementName);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.xml.AbstractContainerElementParserBase#parseContainerContents(name.martingeisse.guiserver.xml.ContentStreams)
	 */
	@Override
	protected void parseContainerContents(ContentStreams<ComponentConfiguration> streams) throws XMLStreamException {
		DefaultContentParser.INSTANCE.parse(streams);
	}
	
}
