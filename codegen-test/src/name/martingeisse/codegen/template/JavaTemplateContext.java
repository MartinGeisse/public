/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.codegen.template;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.apache.commons.lang3.StringUtils;

/**
 * Defines the context in which Java templates are rendered.
 */
public class JavaTemplateContext {

	/**
	 * the sourcePath
	 */
	private File sourcePath = new File(".");
	
	/**
	 * Constructor.
	 */
	public JavaTemplateContext() {
	}
	
	/**
	 * Getter method for the sourcePath.
	 * @return the sourcePath
	 */
	public File getSourcePath() {
		return sourcePath;
	}
	
	/**
	 * Setter method for the sourcePath.
	 * @param sourcePath the sourcePath to set
	 */
	public void setSourcePath(File sourcePath) {
		this.sourcePath = sourcePath;
	}
	
	/**
	 * Opens a text file for writing using a {@link PrintWriter},
	 * creating any package folders necessary for that and overwriting
	 * the file if it already exists.
	 * 
	 * @param packageName the name of the package that contains the file
	 * @param fileName the name of the file to open
	 * @return the {@link PrintWriter}
	 * @throws IOException on I/O errors, especially if there is a file in the
	 * way of a package folder, or a folder in place for the file to create
	 */
	public PrintWriter openTextFile(String packageName, String fileName) throws IOException {
		File cursor = sourcePath;
		for (String packageSegment : StringUtils.split(packageName, '.')) {
			File folder = new File(cursor, packageSegment);
			if (!folder.exists() && !folder.mkdir()) {
				throw new IOException("could not create package folder: " + packageSegment);
			}
			cursor = folder;
		}
		File file = new File(cursor, fileName);
		FileWriter fileWriter = new FileWriter(file);
		return new PrintWriter(fileWriter);
	}
	
}
