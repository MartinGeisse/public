/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.xml;

import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import com.google.common.collect.ImmutableList;

/**
 * This class keeps track of the XML reader and writer as well as the current component
 * accumulation list. It provides convenience methods to deal with the content being
 * processed.
 *
 * @param <C> the component type
 */
public final class ContentStreams<C> {

	/**
	 * The namespace URI for special GUI elements.
	 */
	public static final String GUI_NAMESPACE_URI = "http://guiserver.martingeisse.name/v1";
	
	/**
	 * the reader
	 */
	private final XMLStreamReader reader;
	
	/**
	 * the stringWriter
	 */
	private final StringWriter stringWriter;
	
	/**
	 * the writer
	 */
	private final XMLStreamWriter writer;
	
	/**
	 * the snippets
	 */
	private final List<Object> snippets;

	/**
	 * the componentAccumulatorStack
	 */
	private final Stack<List<C>> componentAccumulatorStack;
	
	/**
	 * the componentAccumulator
	 */
	private List<C> componentAccumulator;

	/**
	 * Constructor.
	 * @param inputStream the input stream to read the configuration from
	 * @param snippets the global list that collects configuration snippets
	 * @throws XMLStreamException on XML processing errors
	 */
	public ContentStreams(InputStream inputStream, List<Object> snippets) throws XMLStreamException {
		this.reader = XMLInputFactory.newInstance().createXMLStreamReader(inputStream);
		this.stringWriter = new StringWriter();
		this.writer = XMLOutputFactory.newInstance().createXMLStreamWriter(stringWriter);
		this.snippets = snippets;
		this.componentAccumulatorStack = new Stack<>();
		this.componentAccumulator = new ArrayList<>();
	}
	
	/**
	 * Getter method for the reader.
	 * @return the reader
	 */
	public XMLStreamReader getReader() {
		return reader;
	}
	
	/**
	 * Getter method for the writer.
	 * @return the writer
	 */
	public XMLStreamWriter getWriter() {
		return writer;
	}
	
	/**
	 * Getter method for the assembled markup.
	 * @return the markup
	 */
	public String getMarkup() {
		return stringWriter.toString();
	}
	
	/**
	 * Adds a component configuration to the current component accumulator.
	 * @param configuration the configuration to add
	 */
	public void addComponent(C configuration) {
		componentAccumulator.add(configuration);
	}
	
	/**
	 * Begins with a fresh, empty component accumulator and pushes the previous one
	 * on the accumulator stack.
	 */
	public void beginComponentAccumulator() {
		componentAccumulatorStack.push(componentAccumulator);
		componentAccumulator = new ArrayList<>();
	}
	
	/**
	 * Finishes the current component accumulator, returning its accumulated components
	 * as an immutable list and returning to the previous one.
	 */
	public ImmutableList<C> finishComponentAccumulator() {
		ImmutableList<C> result = ImmutableList.copyOf(componentAccumulator);
		componentAccumulator = componentAccumulatorStack.pop();
		return result;
	}

	/**
	 * Finishes the root component accumulator, returning its accumulated components
	 * as an immutable list.
	 */
	public ImmutableList<C> finishRootComponentAccumulator() {
		if (!componentAccumulatorStack.isEmpty()) {
			throw new IllegalStateException("cannot finish the root component accumulator -- stack is not empty");
		}
		return ImmutableList.copyOf(componentAccumulator);
	}
	
	/**
	 * Returns the number of components in the component accumulator
	 * @return the accumulator size
	 */
	public int getComponentAccumulatorSize() {
		return componentAccumulator.size();
	}
	
	/**
	 * This method should only be called directly after creating the reader and this streams object. It checks that
	 * the document element is a special element with the specified local name and skips over the opening tag,
	 * otherwise throws an exception.
	 * 
	 * @param expectedLocalName the expected local name
	 */
	public void expectSpecialDocumentElement(String expectedLocalName) throws XMLStreamException {
		if (reader.getEventType() != XMLStreamConstants.START_DOCUMENT) {
			throw new IllegalStateException("reader is not at the START_DOCUMENT");
		}
		reader.next();
		String actualLocalName = recognizeStartSpecialElement();
		if (actualLocalName == null || !actualLocalName.equals(expectedLocalName)) {
			throw new RuntimeException("invalid document element, expected " + GUI_NAMESPACE_URI + " / " + expectedLocalName);
		}
		reader.next();
	}
	
	/**
	 * Checks if the current input event is a START_ELEMENT for a special element. If so, returns the local
	 * name of that element. Otherwise returns null.
	 * 
	 * @return the local element name or null
	 */
	public String recognizeStartSpecialElement() throws XMLStreamException {
		if (reader.getEventType() == XMLStreamConstants.START_ELEMENT && ContentStreams.GUI_NAMESPACE_URI.equals(reader.getNamespaceURI())) {
			return reader.getLocalName();
		} else {
			return null;
		}
	}

	/**
	 * Copies the current input event to the writer.
	 * 
	 * @throws XMLStreamException on XML processing errors
	 */
	public void copyEvent() throws XMLStreamException {
		switch (reader.getEventType()) {
		
		case XMLStreamConstants.START_ELEMENT: {
			if (reader.getNamespaceURI() == null) {
				writer.writeStartElement(reader.getLocalName());
			} else {
				writer.writeStartElement(reader.getNamespaceURI(), reader.getLocalName());
			}
			int count = reader.getAttributeCount();
			for (int i = 0; i < count; i++) {
				if (reader.getAttributeNamespace(i) == null) {
					writer.writeAttribute(reader.getAttributeLocalName(i), reader.getAttributeValue(i));
				} else {
					writer.writeAttribute(reader.getAttributeNamespace(i), reader.getAttributeLocalName(i), reader.getAttributeValue(i));
				}
			}
			break;
		}
			
		case XMLStreamConstants.END_ELEMENT: {
			writer.writeEndElement();
			break;
		}
			
		case XMLStreamConstants.CDATA:
		case XMLStreamConstants.CHARACTERS:
		case XMLStreamConstants.SPACE:
		case XMLStreamConstants.ENTITY_REFERENCE:
			writer.writeCharacters(reader.getText());
			break;
			
		case XMLStreamConstants.COMMENT:
			writer.writeComment(reader.getText());
			break;
			
		default:
			throw new RuntimeException("invalid XML event: " + reader.getEventType());
			
		}
	}
	
	/**
	 * Moves the reader to the next event.
	 * 
	 * @throws XMLStreamException on XML processing errors
	 */
	public void next() throws XMLStreamException {
		reader.next();
	}

	/**
	 * Expects the current event to be the start of an empty element,
	 * and skips both the start and end tag.
	 * 
	 * @throws XMLStreamException on XML processing errors
	 */
	public void expectAndSkipEmptyElement() throws XMLStreamException {
		if (reader.getEventType() != XMLStreamConstants.START_ELEMENT) {
			throw new IllegalStateException("current event is not a START_ELEMENT");
		}
		reader.next();
		if (reader.getEventType() != XMLStreamConstants.END_ELEMENT) {
			throw new RuntimeException("expected empty element");
		}
		reader.next();
	}

	/**
	 * Skips all pure-whitespace events. If any non-whitespace text events are encountered, this
	 * method throws an exception.
	 * 
	 * @throws XMLStreamException on XML processing errors
	 */
	public void skipWhitespace() throws XMLStreamException {
		while (true) {
			switch (reader.getEventType()) {

			case XMLStreamConstants.SPACE:
			case XMLStreamConstants.COMMENT:
				reader.next();
				break;

			case XMLStreamConstants.CDATA:
			case XMLStreamConstants.CHARACTERS:
			case XMLStreamConstants.ENTITY_REFERENCE: {
				String text = reader.getText().trim();
				if (!text.isEmpty()) {
					throw new RuntimeException("expected pure whitespace, found '" + text + "'");
				}
				reader.next();
				break;
			}
				
			default:
				return;
				
			}
		}
	}

	/**
	 * Skips content until a closing tag is encountered to which the start tag has already
	 * been handled, i.e. this method only skips a snippet of properly nested content.
	 * 
	 * Any special elements in the skipped content trigger an error.
	 * 
	 * @throws XMLStreamException on XML processing errors
	 */
	public void skipNestedContent() throws XMLStreamException {
		skipNestedContent(false);
	}

	/**
	 * Skips content until a closing tag is encountered to which the start tag has already
	 * been handled, i.e. this method only skips a snippet of properly nested content.
	 * 
	 * If any special elements are found while skipping, the allowSpecialElements parameter
	 * decides whether those are allowed and skipped, or if they should trigger an error.
	 * 
	 * @param allowSpecialElements whether special elements are allowed in the skipped content
	 * @throws XMLStreamException on XML processing errors
	 */
	public void skipNestedContent(boolean allowSpecialElements) throws XMLStreamException {
		int nesting = 0;
		loop: while (true) {
			switch (reader.getEventType()) {

			case XMLStreamConstants.START_ELEMENT:
				if (!allowSpecialElements && recognizeStartSpecialElement() != null) {
					throw new InvalidSpecialElementException("no special elements allowed here");
				}
				next();
				nesting++;
				break;

			case XMLStreamConstants.END_ELEMENT:
				if (nesting > 0) {
					next();
					nesting--;
					break;
				} else {
					break loop;
				}

			case XMLStreamConstants.CDATA:
			case XMLStreamConstants.CHARACTERS:
			case XMLStreamConstants.SPACE:
			case XMLStreamConstants.ENTITY_REFERENCE:
				next();
				break;

			case XMLStreamConstants.COMMENT:
				next();
				break;
				
			default:
				throw new RuntimeException("invalid XML event while skipping nested content: " + reader.getEventType());

			}
		}
	}
	
	/**
	 * Looks for an attribute for the current START_ELEMENT, and returns its value.
	 * Returns null if the element is not present.
	 * 
	 * @param name the name of the element
	 * @return the value or null
	 */
	public String getOptionalAttribute(String name) {
		return reader.getAttributeValue(null, name);
	}
	
	/**
	 * Expects an attribute to be present for the current START_ELEMENT, and returns its value.
	 * Throws an exception if the element is not present.
	 * 
	 * @param name the name of the element
	 * @return the value
	 */
	public String getMandatoryAttribute(String name) {
		String value = reader.getAttributeValue(null, name);
		if (value == null) {
			throw new RuntimeException("expected " + name + " attribute");
		}
		return value;
	}
	
	/**
	 * Adds a snippet and returns its handle.
	 * @param snippet the snippet to add
	 * @return the handle
	 */
	public int addSnippet(Object snippet) {
		int handle = snippets.size();
		snippets.add(snippet);
		return handle;
	}
	
}
