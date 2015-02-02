/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.configuration.content;

import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import com.google.common.collect.ImmutableList;

/**
 * This class keeps track of the XML reader and writer as well as the current component
 * accumulation list. It provides convenience methods to deal with the content being
 * processed.
 */
public final class ContentStreams {

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
	 * the componentAccumulatorStack
	 */
	private final Stack<List<ComponentConfiguration>> componentAccumulatorStack;
	
	/**
	 * the componentAccumulator
	 */
	private List<ComponentConfiguration> componentAccumulator;

	/**
	 * Constructor.
	 * @param inputStream the input stream to read the configuration from
	 * @throws XMLStreamException on XML processing errors
	 */
	public ContentStreams(InputStream inputStream) throws XMLStreamException {
		this.reader = XMLInputFactory.newInstance().createXMLStreamReader(inputStream);
		this.stringWriter = new StringWriter();
		this.writer = XMLOutputFactory.newInstance().createXMLStreamWriter(stringWriter);
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
	public void addComponent(ComponentConfiguration configuration) {
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
	public ImmutableList<ComponentConfiguration> finishComponentAccumulator() {
		ImmutableList<ComponentConfiguration> result = ImmutableList.copyOf(componentAccumulator);
		componentAccumulator = componentAccumulatorStack.pop();
		return result;
	}

	/**
	 * Finishes the root component accumulator, returning its accumulated components
	 * as an immutable list.
	 */
	public ImmutableList<ComponentConfiguration> finishRootComponentAccumulator() {
		if (!componentAccumulatorStack.isEmpty()) {
			throw new IllegalStateException("cannot finish the root component accumulator -- stack is not empty");
		}
		return ImmutableList.copyOf(componentAccumulator);
	}

}
