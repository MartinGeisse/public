/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.api.tools;

import java.io.File;
import java.util.LinkedList;

import org.apache.commons.lang3.StringUtils;

/**
 * This class iterates over all localization files in one or multiple
 * source folders and calls a subclass method for each of them.
 */
public class LocalizationFileAction {

	/**
	 * the DOT_PROPERTIES
	 */
	private static final String DOT_PROPERTIES = ".properties";
	
	/**
	 * Constructor.
	 */
	public LocalizationFileAction() {
	}
	
	/**
	 * Runs this action on the specified source folders.
	 * @param sourceFolders the folders to act on
	 */
	public void run(File... sourceFolders) {
		LinkedList<String> packageStack = new LinkedList<String>();
		for (File sourceFolder : sourceFolders) {
			handleFolder(sourceFolder, packageStack);
		}
	}
	
	private void handleFolder(File folder, LinkedList<String> packageStack) {
		if (!folder.isDirectory()) {
			throw new IllegalArgumentException("not a folder: " + folder);
		}
		for (File file : folder.listFiles()) {
			if (file.isDirectory()) {
				packageStack.addLast(file.getName());
				handleFolder(file, packageStack);
				packageStack.removeLast();
			} else if (file.isFile()) {
				String fileName = file.getName();
				int underscoreIndex = fileName.indexOf('_');
				if (underscoreIndex != -1 && fileName.endsWith(DOT_PROPERTIES)) {
					String baseName = fileName.substring(0, underscoreIndex);
					String localeName = fileName.substring(underscoreIndex + 1, fileName.length() - DOT_PROPERTIES.length());
					if (acceptLocale(localeName)) {
						onLocalizationFile(packageStack, file, baseName, localeName);
					}
				}
			}
		}
	}
	
	/**
	 * This method is invoked for every localization file. The default implementation just prints
	 * some information to stdout.
	 * 
	 * @param packageStack the package in which the file was found. This stack must not be
	 * modified by this method!
	 * @param file the properties file
	 * @param baseName the base name of the file
	 * @param localeName the locale name contained in the file name
	 */
	protected void onLocalizationFile(LinkedList<String> packageStack, File file, String baseName, String localeName) {
		System.out.println("found .properties file at " + file);
		System.out.println("package: " + getPackageName(packageStack));
		System.out.println("base name: " + baseName + ", locale: " + localeName);
		System.out.println();
	}
	
	/**
	 * Returns true if properties for the specified locale shall be handled, false to
	 * skip them. The default implementation accepts all locales.
	 * 
	 * @param localeName the locale name
	 * @return whether this action accepts the locale
	 */
	protected boolean acceptLocale(String localeName) {
		return true;
	}
	
	/**
	 * Returns the (dot-separated) Java package name for the specified package stack.
	 * @param packageStack the package stack
	 * @return the Java package name
	 */
	protected static String getPackageName(LinkedList<String> packageStack) {
		return StringUtils.join(packageStack, '.');
	}

}
