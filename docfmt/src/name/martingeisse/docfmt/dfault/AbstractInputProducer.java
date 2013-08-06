/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.docfmt.dfault;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import name.martingeisse.docfmt.core.IInputProducer;

/**
 * Base implementation for {@link IInputProducer} that keeps track
 * of opened {@link XMLEventReader}, {@link XMLStreamReader},
 * {@link Reader} and {@link InputStream} objects, and closes
 * them in the close() method.
 */
public abstract class AbstractInputProducer implements IInputProducer {

	/**
	 * the xmlEventReaders
	 */
	private final List<XMLEventReader> xmlEventReaders = new ArrayList<XMLEventReader>();
	
	/**
	 * the xmlStreamReaders
	 */
	private final List<XMLStreamReader> xmlStreamReaders = new ArrayList<XMLStreamReader>();

	/**
	 * the readers
	 */
	private final List<Reader> readers = new ArrayList<Reader>();

	/**
	 * the inputStreams
	 */
	private final List<InputStream> inputStreams = new ArrayList<InputStream>();
	
	/**
	 * Constructor.
	 */
	public AbstractInputProducer() {
	}
	
	/**
	 * Registers an {@link XMLEventReader} to be closed in the close() method.
	 * @param xmlEventReader the XML event reader to register
	 */
	protected final void registerXmlEventReaderToClose(XMLEventReader xmlEventReader) {
		xmlEventReaders.add(xmlEventReader);
	}
	
	/**
	 * Registers an {@link XMLStreamReader} to be closed in the close() method.
	 * @param xmlStreamReader the XML stream reader to register
	 */
	protected final void registerXmlStreamReaderToClose(XMLStreamReader xmlStreamReader) {
		xmlStreamReaders.add(xmlStreamReader);
	}
	
	/**
	 * Registers a {@link Reader} to be closed in the close() method.
	 * @param reader the reader to register
	 */
	protected final void registerReaderToClose(Reader reader) {
		readers.add(reader);
	}
	
	/**
	 * Registers an {@link InputStream} to be closed in the close() method.
	 * @param inputStream the input stream to register
	 */
	protected final void registerInputStreamToClose(InputStream inputStream) {
		inputStreams.add(inputStream);
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.docfmt.core.IInputProducer#close()
	 */
	@Override
	public void close() {
		
		for (XMLEventReader xmlEventReader : xmlEventReaders) {
			try {
				xmlEventReader.close();
			} catch (XMLStreamException e) {
			}
		}
		xmlEventReaders.clear();
		
		for (XMLStreamReader xmlStreamReader : xmlStreamReaders) {
			try {
				xmlStreamReader.close();
			} catch (XMLStreamException e) {
			}
		}
		xmlStreamReaders.clear();
		
		for (Reader reader : readers) {
			try {
				reader.close();
			} catch (IOException e) {
			}
		}
		readers.clear();
		
		for (InputStream inputStream : inputStreams) {
			try {
				inputStream.close();
			} catch (IOException e) {
			}
		}
		inputStreams.clear();
		
	}
	
}
