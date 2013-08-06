/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.docfmt.dfault;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import name.martingeisse.docfmt.core.IOutputConsumer;

/**
 * Base implementation for {@link IOutputConsumer} that keeps track
 * of opened {@link XMLEventWriter}, {@link XMLStreamWriter},
 * {@link Writer} and {@link OutputStream} objects, and closes
 * them in the close() method.
 */
public abstract class AbstractOutputConsumer implements IOutputConsumer {

	/**
	 * the xmlEventWriters
	 */
	private final List<XMLEventWriter> xmlEventWriters = new ArrayList<XMLEventWriter>();
	
	/**
	 * the xmlStreamWriters
	 */
	private final List<XMLStreamWriter> xmlStreamWriters = new ArrayList<XMLStreamWriter>();

	/**
	 * the writers
	 */
	private final List<Writer> writers = new ArrayList<Writer>();

	/**
	 * the outputStreams
	 */
	private final List<OutputStream> outputStreams = new ArrayList<OutputStream>();
	
	/**
	 * Constructor.
	 */
	public AbstractOutputConsumer() {
	}
	
	/**
	 * Registers an {@link XMLEventWriter} to be closed in the close() method.
	 * @param xmlEventWriter the XML event writer to register
	 */
	protected final void registerXmlEventWriterToClose(XMLEventWriter xmlEventWriter) {
		xmlEventWriters.add(xmlEventWriter);
	}
	
	/**
	 * Registers an {@link XMLStreamWriter} to be closed in the close() method.
	 * @param xmlStreamWriter the XML stream writer to register
	 */
	protected final void registerXmlStreamWriterToClose(XMLStreamWriter xmlStreamWriter) {
		xmlStreamWriters.add(xmlStreamWriter);
	}
	
	/**
	 * Registers a {@link Writer} to be closed in the close() method.
	 * @param writer the writer to register
	 */
	protected final void registerWriterToClose(Writer writer) {
		writers.add(writer);
	}
	
	/**
	 * Registers an {@link OutputStream} to be closed in the close() method.
	 * @param outputStream the output stream to register
	 */
	protected final void registerOutputStreamToClose(OutputStream outputStream) {
		outputStreams.add(outputStream);
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.docfmt.core.IOutputConsumer#close()
	 */
	@Override
	public void close() {
		
		for (XMLEventWriter xmlEventWriter : xmlEventWriters) {
			try {
				xmlEventWriter.close();
			} catch (XMLStreamException e) {
			}
		}
		xmlEventWriters.clear();
		
		for (XMLStreamWriter xmlStreamWriter : xmlStreamWriters) {
			try {
				xmlStreamWriter.close();
			} catch (XMLStreamException e) {
			}
		}
		xmlStreamWriters.clear();
		
		for (Writer writer : writers) {
			try {
				writer.close();
			} catch (IOException e) {
			}
		}
		writers.clear();
		
		for (OutputStream outputStream : outputStreams) {
			try {
				outputStream.close();
			} catch (IOException e) {
			}
		}
		outputStreams.clear();
		
	}
	
}
