/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.docfmt.dfault;

import java.io.File;
import java.io.FileWriter;

import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLOutputFactory;

/**
 * Default output consumer. The main output is an XSL:FO file.
 */
public final class DefaultOutputConsumer extends AbstractOutputConsumer {

	/**
	 * the mainOutputFile
	 */
	private final File mainOutputFile;
	
	/**
	 * Constructor.
	 * @param mainOutputFile the main output file
	 */
	public DefaultOutputConsumer(File mainOutputFile) {
		this.mainOutputFile = mainOutputFile;
	}
	
	/**
	 * Getter method for the mainOutputFile.
	 * @return the mainOutputFile
	 */
	public File getMainOutputFile() {
		return mainOutputFile;
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.docfmt.core.IOutputConsumer#getMainOutput()
	 */
	@Override
	public XMLEventWriter getMainOutput() {
		return createEventWriterForFile(mainOutputFile);
	}
	
	/**
	 * 
	 */
	private XMLEventWriter createEventWriterForFile(File file) {
		try {
			FileWriter fileWriter = new FileWriter(file);
			registerWriterToClose(fileWriter);
			XMLEventWriter xmlEventWriter = XMLOutputFactory.newFactory().createXMLEventWriter(fileWriter);
			registerXmlEventWriterToClose(xmlEventWriter);
			return xmlEventWriter;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
}
