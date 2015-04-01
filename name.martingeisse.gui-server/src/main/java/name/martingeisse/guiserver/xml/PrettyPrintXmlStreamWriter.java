/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.xml;

import javax.xml.namespace.NamespaceContext;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.apache.commons.lang3.StringUtils;

/**
 * Wraps another XML stream writer and pretty-prints the output.
 */
public class PrettyPrintXmlStreamWriter implements XMLStreamWriter {

	/**
	 * Formatter states
	 */
	private static enum State {
		START_OF_LINE,
		INDENTED,
		RIGHT_AFTER_TAG,
		WITHIN_TEXT
	}
	
	/**
	 * the WHITESPACE
	 */
	private static final String WHITESPACE = " \t\n\r";
	
	/**
	 * the wrapped
	 */
	private final XMLStreamWriter wrapped;

	/**
	 * the indentationSegment
	 */
	private final String indentationSegment;

	/**
	 * the indentationLevel
	 */
	private int indentationLevel;
	
	/**
	 * the rightAfterTag
	 */
	private State state;
	
	/**
	 * the wasStartTag
	 */
	private boolean wasStartTag;
	
	/**
	 * the retainedWhitespace
	 */
	private String retainedWhitespace;

	/**
	 * Constructor.
	 * @param wrapped the wrapped XML stream writer
	 * @param indentationSegment a single segment to use for indentation
	 */
	public PrettyPrintXmlStreamWriter(XMLStreamWriter wrapped, String indentationSegment) {
		this.wrapped = wrapped;
		this.indentationSegment = indentationSegment;
		this.indentationLevel = 0;
		this.state = State.START_OF_LINE;
		this.wasStartTag = false;
		this.retainedWhitespace = null;
	}

	/**
	 * Getter method for the wrapped.
	 * @return the wrapped
	 */
	public XMLStreamWriter getWrapped() {
		return wrapped;
	}

	private void writeIndentation() throws XMLStreamException {
		for (int i=0; i<indentationLevel; i++) {
			wrapped.writeCharacters(indentationSegment);
		}
	}
	
	/**
	 * 
	 */
	private void prepareIndented() throws XMLStreamException {
		if (state != State.START_OF_LINE) {
			wrapped.writeCharacters("\n");
		}
		writeIndentation();
		state = State.INDENTED;
	}
	
	/**
	 * 
	 */
	private void onBeforeStartingTag() throws XMLStreamException {
		retainedWhitespace = null;
		prepareIndented();
	}

	/**
	 * 
	 */
	private void onAfterStartingTag() throws XMLStreamException {
		indentationLevel++;
		state = State.RIGHT_AFTER_TAG;
		wasStartTag = true;
	}

	/**
	 * 
	 */
	private void onBeforeEndingTag() throws XMLStreamException {
		retainedWhitespace = null;
		indentationLevel--;
		if (state == State.RIGHT_AFTER_TAG && wasStartTag) {
			// append directly after the start tag; will be collapsed to an open-close element
		} else {
			prepareIndented();
		}
	}

	/**
	 * 
	 */
	private void onAfterEndingTag() throws XMLStreamException {
		state = State.RIGHT_AFTER_TAG;
		wasStartTag = false;
	}

	/**
	 * 
	 */
	private void flushRetainedWhitespace() throws XMLStreamException {
		if (retainedWhitespace != null) {
			wrapped.writeCharacters(retainedWhitespace);
			retainedWhitespace = null;
		}
	}
	
	/**
	 * @param text
	 */
	private void writeText(String text) throws XMLStreamException {
		if (state == State.RIGHT_AFTER_TAG) {
			text = StringUtils.stripStart(text, WHITESPACE);
			if (text.isEmpty()) {
				return;
			}
		}
		if (state != State.WITHIN_TEXT) {
			prepareIndented();
		}
		flushRetainedWhitespace();
		{
			String oldText = text;
			text = StringUtils.stripEnd(text, WHITESPACE);
			if (text.length() != oldText.length()) {
				retainedWhitespace = oldText.substring(text.length());
			}
		}
		wrapped.writeCharacters(text);
		state = State.WITHIN_TEXT;
	}
	
	/**
	 * 
	 */
	private void onBeforeInvincibleText() throws XMLStreamException {
		flushRetainedWhitespace();
		if (state != State.WITHIN_TEXT) {
			prepareIndented();
		}
	}
	
	/**
	 * 
	 */
	private void onAfterInvincibleText() throws XMLStreamException {
		state = State.WITHIN_TEXT;
	}

	/* (non-Javadoc)
	 * @see javax.xml.stream.XMLStreamWriter#writeStartElement(java.lang.String)
	 */
	@Override
	public void writeStartElement(String localName) throws XMLStreamException {
		onBeforeStartingTag();
		wrapped.writeStartElement(localName);
		onAfterStartingTag();
	}

	/* (non-Javadoc)
	 * @see javax.xml.stream.XMLStreamWriter#writeStartElement(java.lang.String, java.lang.String)
	 */
	@Override
	public void writeStartElement(String namespaceURI, String localName) throws XMLStreamException {
		onBeforeStartingTag();
		wrapped.writeStartElement(namespaceURI, localName);
		onAfterStartingTag();
	}

	/* (non-Javadoc)
	 * @see javax.xml.stream.XMLStreamWriter#writeStartElement(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void writeStartElement(String prefix, String localName, String namespaceURI) throws XMLStreamException {
		onBeforeStartingTag();
		wrapped.writeStartElement(prefix, localName, namespaceURI);
		onAfterStartingTag();
	}

	/* (non-Javadoc)
	 * @see javax.xml.stream.XMLStreamWriter#writeEmptyElement(java.lang.String, java.lang.String)
	 */
	@Override
	public void writeEmptyElement(String namespaceURI, String localName) throws XMLStreamException {
		onBeforeStartingTag();
		wrapped.writeEmptyElement(namespaceURI, localName);
		onAfterEndingTag();
	}

	/* (non-Javadoc)
	 * @see javax.xml.stream.XMLStreamWriter#writeEmptyElement(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void writeEmptyElement(String prefix, String localName, String namespaceURI) throws XMLStreamException {
		onBeforeStartingTag();
		wrapped.writeEmptyElement(prefix, localName, namespaceURI);
		onAfterEndingTag();
	}

	/* (non-Javadoc)
	 * @see javax.xml.stream.XMLStreamWriter#writeEmptyElement(java.lang.String)
	 */
	@Override
	public void writeEmptyElement(String localName) throws XMLStreamException {
		onBeforeStartingTag();
		wrapped.writeEmptyElement(localName);
		onAfterEndingTag();
	}

	/* (non-Javadoc)
	 * @see javax.xml.stream.XMLStreamWriter#writeEndElement()
	 */
	@Override
	public void writeEndElement() throws XMLStreamException {
		onBeforeEndingTag();
		wrapped.writeEndElement();
		onAfterEndingTag();
	}

	/* (non-Javadoc)
	 * @see javax.xml.stream.XMLStreamWriter#writeEndDocument()
	 */
	@Override
	public void writeEndDocument() throws XMLStreamException {
		wrapped.writeEndDocument();
	}

	/* (non-Javadoc)
	 * @see javax.xml.stream.XMLStreamWriter#close()
	 */
	@Override
	public void close() throws XMLStreamException {
		wrapped.close();
	}

	/* (non-Javadoc)
	 * @see javax.xml.stream.XMLStreamWriter#flush()
	 */
	@Override
	public void flush() throws XMLStreamException {
		wrapped.flush();
	}

	/* (non-Javadoc)
	 * @see javax.xml.stream.XMLStreamWriter#writeAttribute(java.lang.String, java.lang.String)
	 */
	@Override
	public void writeAttribute(String localName, String value) throws XMLStreamException {
		wrapped.writeAttribute(localName, value);
	}

	/* (non-Javadoc)
	 * @see javax.xml.stream.XMLStreamWriter#writeAttribute(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void writeAttribute(String prefix, String namespaceURI, String localName, String value) throws XMLStreamException {
		wrapped.writeAttribute(prefix, namespaceURI, localName, value);
	}

	/* (non-Javadoc)
	 * @see javax.xml.stream.XMLStreamWriter#writeAttribute(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void writeAttribute(String namespaceURI, String localName, String value) throws XMLStreamException {
		wrapped.writeAttribute(namespaceURI, localName, value);
	}

	/* (non-Javadoc)
	 * @see javax.xml.stream.XMLStreamWriter#writeNamespace(java.lang.String, java.lang.String)
	 */
	@Override
	public void writeNamespace(String prefix, String namespaceURI) throws XMLStreamException {
		wrapped.writeNamespace(prefix, namespaceURI);
	}

	/* (non-Javadoc)
	 * @see javax.xml.stream.XMLStreamWriter#writeDefaultNamespace(java.lang.String)
	 */
	@Override
	public void writeDefaultNamespace(String namespaceURI) throws XMLStreamException {
		wrapped.writeDefaultNamespace(namespaceURI);
	}

	/* (non-Javadoc)
	 * @see javax.xml.stream.XMLStreamWriter#writeComment(java.lang.String)
	 */
	@Override
	public void writeComment(String data) throws XMLStreamException {
		onBeforeStartingTag();
		wrapped.writeComment(data);
		onAfterEndingTag();
	}

	/* (non-Javadoc)
	 * @see javax.xml.stream.XMLStreamWriter#writeProcessingInstruction(java.lang.String)
	 */
	@Override
	public void writeProcessingInstruction(String target) throws XMLStreamException {
		onBeforeStartingTag();
		wrapped.writeProcessingInstruction(target);
		onAfterEndingTag();
	}

	/* (non-Javadoc)
	 * @see javax.xml.stream.XMLStreamWriter#writeProcessingInstruction(java.lang.String, java.lang.String)
	 */
	@Override
	public void writeProcessingInstruction(String target, String data) throws XMLStreamException {
		onBeforeStartingTag();
		wrapped.writeProcessingInstruction(target, data);
		onAfterEndingTag();
	}

	/* (non-Javadoc)
	 * @see javax.xml.stream.XMLStreamWriter#writeCData(java.lang.String)
	 */
	@Override
	public void writeCData(String data) throws XMLStreamException {
		onBeforeInvincibleText();
		wrapped.writeCData(data);
		onAfterInvincibleText();
	}

	/* (non-Javadoc)
	 * @see javax.xml.stream.XMLStreamWriter#writeDTD(java.lang.String)
	 */
	@Override
	public void writeDTD(String dtd) throws XMLStreamException {
		wrapped.writeDTD(dtd);
	}

	/* (non-Javadoc)
	 * @see javax.xml.stream.XMLStreamWriter#writeEntityRef(java.lang.String)
	 */
	@Override
	public void writeEntityRef(String name) throws XMLStreamException {
		onBeforeInvincibleText();
		wrapped.writeEntityRef(name);
		onAfterInvincibleText();
	}

	/* (non-Javadoc)
	 * @see javax.xml.stream.XMLStreamWriter#writeStartDocument()
	 */
	@Override
	public void writeStartDocument() throws XMLStreamException {
		wrapped.writeStartDocument();
	}

	/* (non-Javadoc)
	 * @see javax.xml.stream.XMLStreamWriter#writeStartDocument(java.lang.String)
	 */
	@Override
	public void writeStartDocument(String version) throws XMLStreamException {
		wrapped.writeStartDocument(version);
	}

	/* (non-Javadoc)
	 * @see javax.xml.stream.XMLStreamWriter#writeStartDocument(java.lang.String, java.lang.String)
	 */
	@Override
	public void writeStartDocument(String encoding, String version) throws XMLStreamException {
		wrapped.writeStartDocument(encoding, version);
	}

	/* (non-Javadoc)
	 * @see javax.xml.stream.XMLStreamWriter#writeCharacters(java.lang.String)
	 */
	@Override
	public void writeCharacters(String text) throws XMLStreamException {
		writeText(text);
	}

	/* (non-Javadoc)
	 * @see javax.xml.stream.XMLStreamWriter#writeCharacters(char[], int, int)
	 */
	@Override
	public void writeCharacters(char[] text, int start, int len) throws XMLStreamException {
		writeText(new String(text, start, len));
	}

	/* (non-Javadoc)
	 * @see javax.xml.stream.XMLStreamWriter#getPrefix(java.lang.String)
	 */
	@Override
	public String getPrefix(String uri) throws XMLStreamException {
		return wrapped.getPrefix(uri);
	}

	/* (non-Javadoc)
	 * @see javax.xml.stream.XMLStreamWriter#setPrefix(java.lang.String, java.lang.String)
	 */
	@Override
	public void setPrefix(String prefix, String uri) throws XMLStreamException {
		wrapped.setPrefix(prefix, uri);
	}

	/* (non-Javadoc)
	 * @see javax.xml.stream.XMLStreamWriter#setDefaultNamespace(java.lang.String)
	 */
	@Override
	public void setDefaultNamespace(String uri) throws XMLStreamException {
		wrapped.setDefaultNamespace(uri);
	}

	/* (non-Javadoc)
	 * @see javax.xml.stream.XMLStreamWriter#setNamespaceContext(javax.xml.namespace.NamespaceContext)
	 */
	@Override
	public void setNamespaceContext(NamespaceContext context) throws XMLStreamException {
		wrapped.setNamespaceContext(context);
	}

	/* (non-Javadoc)
	 * @see javax.xml.stream.XMLStreamWriter#getNamespaceContext()
	 */
	@Override
	public NamespaceContext getNamespaceContext() {
		return wrapped.getNamespaceContext();
	}

	/* (non-Javadoc)
	 * @see javax.xml.stream.XMLStreamWriter#getProperty(java.lang.String)
	 */
	@Override
	public Object getProperty(String name) throws IllegalArgumentException {
		return wrapped.getProperty(name);
	}

}
