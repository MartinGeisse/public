/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.docfmt.dfault;

import java.io.File;
import java.io.FileReader;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;

/**
 * Default input producer. The main input is a file. Included inputs
 * are file paths resolved relative to that file.
 */
public final class DefaultInputProducer extends AbstractInputProducer {

	/**
	 * the mainInputFile
	 */
	private final File mainInputFile;
	
	/**
	 * Constructor.
	 * @param mainInputFile the main input file
	 */
	public DefaultInputProducer(File mainInputFile) {
		this.mainInputFile = mainInputFile;
	}
	
	/**
	 * Getter method for the mainInputFile.
	 * @return the mainInputFile
	 */
	public File getMainInputFile() {
		return mainInputFile;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.docfmt.core.IInputProducer#getMainInput()
	 */
	@Override
	public XMLEventReader getMainInput() {
		return createEventReaderForFile(mainInputFile);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.docfmt.core.IInputProducer#getIncludedInput(java.lang.String)
	 */
	@Override
	public XMLEventReader getIncludedInput(String specifier) {
		return createEventReaderForFile(new File(mainInputFile, specifier));
	}
	
	/**
	 * 
	 */
	private XMLEventReader createEventReaderForFile(File file) {
		try {
			FileReader fileReader = new FileReader(file);
			registerReaderToClose(fileReader);
			XMLEventReader xmlEventReader = XMLInputFactory.newFactory().createXMLEventReader(fileReader);
			registerXmlEventReaderToClose(xmlEventReader);
			return xmlEventReader;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
}
